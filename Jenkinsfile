pipeline {
    agent any
    stages {
        stage('Create Namespace') {
            steps {
                script {
                    try {
                        sh "kubectl get namespace kubernetes-Dev"
                    } catch (Exception ex) {
                        sh "kubectl create namespace kubernetes-Dev"
                    }
                }
            }
        }
        stage('Checkout') {
            steps {
                git 'https://github.com/hbkhum/eCommerce-JavaBackend.git'
            }
        }
        stage('Maven Build') {
            steps {
                container('maven') {
                    sh 'mvn package'
                }
            }
        }
        stage('Docker Build and Push') {
            steps {
                container('docker') {
                    script {
                        docker.build("$DOCKERHUB_USER/my-app").push()
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                container('maven') {
                    sh 'kubectl apply -f deployment.yaml'
                }
            }
        }
    }
}
