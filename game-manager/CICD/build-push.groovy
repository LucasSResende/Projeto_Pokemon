pipeline {
    agent any

    environment {
        GITHUB_SSH_CREDENTIALS = credentials('github-key')
        DOCKER_IMAGE = 'lucassresende/game-manager'
    }

    stages {

        stage('Clean Workspace') {
            steps {
                script{
                    cleanWs()
                    println "Namespace = ${NAMESPACE}"
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


        stage('Build Project') {
            steps {
                script {
                    sh 'cd game-manager && mvn clean install -DskipTests'
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    sh 'cd game-manager && mvn test'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t ${DOCKER_IMAGE}:latest game-manager'
                }
            }
        }

        stage('Docker Login') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', passwordVariable: 'DOCKER_PWD', usernameVariable: 'DOCKER_USR')]) {
                    sh 'echo \$DOCKER_PWD | docker login -u \$DOCKER_USR --password-stdin'
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    sh 'docker push ${DOCKER_IMAGE}:latest'
                }
            }
        }
    }

    post {
        always {
            sh 'docker logout'
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}