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
                        // Maneja el error de Maven Build aquí
                        error('La compilación de Maven ha fallado')
                    }
                }
            }
        }
        stage('Docker Build and Push') {
            steps {
                container('docker') {
                    script {
                        withCredentials([usernamePassword(credentialsId: 'dockerHubCredentials', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                            try {
                                docker.withRegistry('https://registry.hub.docker.com', 'dockerHubCredentials') {
                                    docker.build("${DOCKERHUB_USER}/my-app").push('latest')
                                }
                            } catch (Exception ex) {
                                // Maneja el error de Docker Build and Push aquí
                                error('La compilación y el push de Docker han fallado')
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
                            sh 'kubectl apply -f /var/jenkins_home/workspace/eCommerce-JavaBackend/path/to/deployment.yaml'
                        } catch (Exception ex) {
                            // Maneja el error de Deploy to Kubernetes aquí
                            error('El despliegue a Kubernetes ha fallado')
                        }
                    }
                }
            }
        }
    }
}
