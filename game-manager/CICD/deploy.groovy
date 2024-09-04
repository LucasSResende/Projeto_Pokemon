pipeline {
    agent any

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

        stage('Deploy with Helm') {
            steps {
                script {
                    sh "cd game-manager && helm upgrade --install game-manager helm/game-manager --set image.repository=lucassresende/game-manager:latest --namespace game"
                }
            }
        }

        stage('Set Kubernetes Context') {
            steps {
                script {
                    sh "kubectl config set-context --current --namespace=game"
                }
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
