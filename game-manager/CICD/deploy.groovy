pipeline {
    agent any

    environment {
        BD_NAME = 'mysql'
        API_NAME = 'publisher'
        SERVICE_NAME = 'game-manager'
        NAMESPACE = 'game'
        SERVICE_PORT = 8080
        LOCAL_PORT = 31396
        CLUSTER_IP = '192.168.49.2'
        SERVICE_NODE_PORT = 30594
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
                    git branch: 'main', url: 'https://github.com/LucasSResende/Project1_IDP.git', credentialsId: 'github-key'
                }
            }
        }

        stage('Check Namespace') {
            steps {
                script {
                    def status = sh(script: 'kubectl get namespace game', returnStatus: true)
                    if (status != 0) {
                        echo 'Namespace "game" não existe. Criando o namespace...'
                        sh 'cd game-manager && kubectl create namespace game'
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
                    def mysqlExists = sh(script: 'helm list --namespace ${NAMESPACE} --filter "^${BD_NAME}$" | grep ${BD_NAME}', returnStatus: true)
                    if (mysqlExists != 0) {
                        echo 'Release MySQL não encontrada. Instalando...'
                        sh 'helm install ${BD_NAME} ./helm/mysql --namespace ${NAMESPACE}'
                    } else {
                        echo 'Release MySQL já existe.'
                    }

                    def publisherExists = sh(script: 'helm list --namespace ${NAMESPACE} --filter "^${API_NAME}$" | grep ${API_NAME}', returnStatus: true)
                    if (publisherExists != 0) {
                        echo 'Release Publisher não encontrada. Instalando...'
                        sh 'helm install ${API_NAME} ./helm/publisher-manager --namespace ${NAMESPACE}'
                    } else {
                        echo 'Release Publisher já existe.'
                    }

                    def gameManagerExists = sh(script: 'helm list --namespace ${NAMESPACE} --filter "^${SERVICE_NAME}$" | grep ${SERVICE_NAME}', returnStatus: true)
                    if (gameManagerExists != 0) {
                        echo 'Release Game Manager não encontrada. Instalando...'
                        sh 'helm install ${SERVICE_NAME} ./helm/game-manager --namespace ${NAMESPACE}'
                    } else {
                        echo 'Release Game Manager já existe.'
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

                    sh '''
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
                    '''
                }
            }
        }

        stage('Check Pods') {
            steps {
                script {
                    sh 'kubectl get pods'
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
