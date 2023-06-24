pipeline {
    agent any
    stages {
        stage('Create Namespace') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'mykubeconfig', variable: 'KUBECONFIG')]) {
                        try {
                            sh "kubectl get namespace kubernetesdev"
                        } catch (Exception ex) {
                            sh "kubectl create namespace kubernetesdev"
                        }
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
                        withCredentials([usernamePassword(credentialsId: 'dockerHubCredentials', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                            docker.withRegistry('https://registry.hub.docker.com', 'dockerHubCredentials') {
                                docker.build("my-app").push('latest')
                            }
                        }
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                container('maven') {
                    withCredentials([file(credentialsId: 'mykubeconfig', variable: 'KUBECONFIG')]) {
                        sh 'kubectl apply -f deployment.yaml'
                    }
                }
            }
        }
    }
}
