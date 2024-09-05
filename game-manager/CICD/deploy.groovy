pipeline {
    agent any

    environment {
        RELEASE_NAME = 'game-manager'
        NAMESPACE = 'game'
        SERVICE_PORT = 8081
        LOCAL_PORT = ''
        CLUSTER_IP = '192.168.49.2'
        SERVICE_NODE_PORT = ''
    }

    stages {
        stage('Check Minikube Status') {
            steps {
                script {
                    def status = sh(script: 'minikube status', returnStatus: true)
                    if (status != 0) {
                        echo 'Minikube não está rodando. Iniciando o Minikube...'
                        sh 'minikube start'
                    } else {
                        echo 'Minikube já está rodando.'
                    }
                }
            }
        }

        stage('Clone Repo') {
            steps {
                script {
                    git branch: 'main', url: 'https://github.com/LucasSResende/Project1_IDP', credentialsId: 'github-key'
                }
            }
        }

        stage('Check Namespace') {
            steps {
                script {
                    def status = sh(script: 'kubectl get namespace game', returnStatus: true)
                    if (status != 0) {
                        echo 'Namespace "game" não existe. Criando o namespace...'
                        sh 'kubectl create namespace game'
                    } else {
                        echo 'Namespace "game" já existe.'
                    }
                }
            }
        }

        stage('Set Kubernetes Context') {
            steps {
                script {
                    sh "kubectl config set-context --current --namespace=${NAMESPACE}"
                }
            }
        }

        stage('Check and Install Helm Releases') {
            steps {
                script {
                    sh 'pwd'
                    sh 'ls -la'

                    def chartPath = './helm/game-manager'
                    def chartExists = sh(script: "test -d ${chartPath} && echo 'exists'", returnStdout: true).trim()
                    if (chartExists == 'exists') {
                        echo 'Chart encontrado. Instalando...'
                        sh "cd game-manager && helm install ${RELEASE_NAME} ${chartPath} --namespace ${NAMESPACE}"
                    } else {
                        error "Erro: Helm Chart no caminho ${chartPath} não encontrado."
                    }
                }
            }
        }

        stage('Fetch Ports') {
            steps {
                script {
                    LOCAL_PORT = sh(script: "kubectl get svc ${RELEASE_NAME} -n ${NAMESPACE} -o jsonpath='{.spec.ports[0].nodePort}'", returnStdout: true).trim()
                    SERVICE_NODE_PORT = LOCAL_PORT
                    echo "Service NodePort: ${SERVICE_NODE_PORT}"

                    CLUSTER_IP = sh(script: "kubectl get svc ${RELEASE_NAME} -n ${NAMESPACE} -o jsonpath='{.spec.clusterIP}'", returnStdout: true).trim()
                    echo "Cluster IP: ${CLUSTER_IP}"
                }
            }
        }

        stage('Check Replicas and Run Pod') {
            steps {
                script {
                    def isDeploymentReady = false
                    for (int i = 0; i < 6; i++) {
                        def readyReplicas = sh(script: "kubectl get deploy game-manager -o 'jsonpath={..status.readyReplicas}'", returnStdout: true).trim()

                        if (readyReplicas == '1') {
                            echo "Deployment is ready with 1 replica."
                            isDeploymentReady = true
                            break
                        } else {
                            echo "Deployment not ready yet. Attempt ${i + 1} of 6. Checking again in 10 seconds..."
                            sleep(10)
                        }
                    }

                    if (!isDeploymentReady) {
                        error "Deployment check failed: No replicas are ready after 6 attempts."
                    }
                }
            }
        }

        stage('Get and Post Tests') {
            steps {
                script {
                    def testApp = sh(script: "curl ${CLUSTER_IP}:${SERVICE_NODE_PORT}/game", returnStatus: true)
                    if (testApp != 0) {
                        error 'Falha ao testar a aplicação com curl.'
                    } else {
                        echo 'Teste inicial da aplicação com curl executado com sucesso.'
                    }

                    sh """
                        echo "CLUSTER_IP=${CLUSTER_IP}, SERVICE_NODE_PORT=${SERVICE_NODE_PORT}"
                        curl --location "http://${CLUSTER_IP}:${SERVICE_NODE_PORT}/game" \
                        --header "Content-Type: application/json" \
                        --header "Cookie: JSESSIONID.bfb4ed19=node01jcgdahjvoy2oy86hgtdfe9v1.node0" \
                        --data '{
                            "publisherId": "nintendo",
                            "name": "Mario",
                            "timePlayed": {
                                "2023-05-01": 10,
                                "2023-05-02": 2,
                                "2023-05-03": 3,
                                "2023-05-04": 4
                            }
                        }'
                    """
                }
            }
        }

        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }
    }

    post {
        success {
            echo 'Deploy Pipeline completed successfully!'
        }
        failure {
            echo 'Deploy Pipeline failed!'
        }
    }
}
