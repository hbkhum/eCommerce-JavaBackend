pipeline {
    agent any
    stages {
        stage('Create Namespace') {
            steps {
                script {
                    try {
                        withCredentials([file(credentialsId: 'mykubeconfig', variable: 'KUBECONFIG')]) {
                            sh "kubectl get namespace kubernetesdev"
                        }
                    } catch (Exception ex) {
                        echo "Error en la etapa Create Namespace: ${ex.getMessage()}"
                        error("La etapa Create Namespace ha fallado")
                    }
                }
            }
        }
        stage('Checkout') {
            steps {
                script {
                    try {
                        git 'https://github.com/hbkhum/eCommerce-JavaBackend.git'
                    } catch (Exception ex) {
                        echo "Error en la etapa Checkout: ${ex.getMessage()}"
                        error("La etapa Checkout ha fallado")
                    }
                }
            }
        }
        stage('Maven Build') {
            steps {
                script {
                    try {
                        container('maven') {
                            sh 'mvn package'
                        }
                    } catch (Exception ex) {
                        echo "Error en la etapa Maven Build: ${ex.getMessage()}"
                        error("La etapa Maven Build ha fallado")
                    }
                }
            }
        }
        stage('Docker Build and Push') {
            steps {
                script {
                    try {
                        container('docker') {
                            withCredentials([usernamePassword(credentialsId: 'dockerHubCredentials', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                                docker.withRegistry('https://registry.hub.docker.com', 'dockerHubCredentials') {
                                    docker.build("${DOCKERHUB_USER}/my-app").push('latest')
                                }
                            }
                        }
                    } catch (Exception ex) {
                        echo "Error en la etapa Docker Build and Push: ${ex.getMessage()}"
                        error("La etapa Docker Build and Push ha fallado")
                    }
                }
            }
        }
        stage('Deploy to Kubernetes') {
            steps {
                script {
                    try {
                        container('maven') {
                            withCredentials([file(credentialsId: 'mykubeconfig', variable: 'KUBECONFIG')]) {
                                sh 'kubectl apply -f deployment.yaml'
                            }
                        }
                    } catch (Exception ex) {
                        echo "Error en la etapa Deploy to Kubernetes: ${ex.getMessage()}"
                        error("La etapa Deploy to Kubernetes ha fallado")
                    }
                }
            }
        }
    }
}
