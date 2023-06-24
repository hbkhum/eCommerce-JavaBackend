pipeline {
    agent any
    stages {
        stage('Create Namespace') {
            steps {
                script {
                    withCredentials([file(credentialsId: 'mykubeconfig', variable: 'KUBECONFIG')]) {
                        try {
                            sh "kubectl get namespace kubernetes-dev"
                        } catch (Exception ex) {
                            sh "kubectl create namespace kubernetes-dev"
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
                    try {
                        sh 'mvn package'
                    } catch (Exception ex) {
                        error('La compilación de Maven ha fallado')
                    }
                }
            }
        }
        stage('Docker Build') {
            steps {
                container('docker') {
                    script {
                        withCredentials([usernamePassword(credentialsId: 'dockerHubCredentials', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                            try {
                                sh 'docker build -t ${DOCKERHUB_USER}/my-app .'
                            } catch (Exception ex) {
                                error('La compilación de Docker ha fallado')
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
                        try {
                            sh 'kubectl apply -f deployment.yaml'
                        } catch (Exception ex) {
                            error('El despliegue a Kubernetes ha fallado')
                        }
                    }
                }
            }
        }
    }
}
