pipeline {
    agent {
        kubernetes {
            // Configuración del agente de Kubernetes
            label 'kubernetes'
            yaml """
                // Configuración YAML para el pod de Kubernetes
                apiVersion: v1
                kind: Pod
                metadata:
                  labels:
                    app: my-app
                spec:
                  containers:
                  - name: maven
                    image: maven:3.8.1-openjdk-11
                    command:
                    - sleep
                    - infinity
            """
        }
    }
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
                container('docker') {
                    script {
                        withCredentials([usernamePassword(credentialsId: 'dockerHubCredentials', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
                            docker.withRegistry('https://registry.hub.docker.com', 'dockerHubCredentials') {
                                docker.build("${DOCKERHUB_USER}/my-app").push('latest')
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
