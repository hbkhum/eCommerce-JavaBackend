pipeline {
    agent {
        label 'jenkins-agent'
    }
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
        stage('Build') {
            agent {
                kubernetes {
                    // Utiliza un pod de Kubernetes con Maven y Docker
                    yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    some-label: some-label-value
spec:
  containers:
  - name: maven
    image: maven:3.8.1
    command:
    - cat
    tty: true
  - name: docker
    image: docker:19.03.1
    command:
    - cat
    tty: true
  volumes:
  - name: docker-sock
    hostPath:
      path: /var/run/docker.sock
"""
                }
            }
            steps {
                stage('Checkout') {
                    steps {
                        // Clona tu repositorio
                        git 'https://github.com/hbkhum/eCommerce-JavaBackend.git'
                    }
                }
                stage('Maven Build') {
                    steps {
                        container('maven') {
                            // Construye tu proyecto con Maven
                            sh 'mvn package'
                        }
                    }
                }
                stage('Docker Build and Push') {
                    steps {
                        container('docker') {
                            // Construye y sube tu imagen de Docker. Asegúrate de tener las variables de entorno DOCKERHUB_USER y DOCKERHUB_PASS
                            withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', passwordVariable: 'DOCKERHUB_PASS', usernameVariable: 'DOCKERHUB_USER')]) {
                                script {
                                    docker.withRegistry('https://registry.hub.docker.com', 'dockerhub-credentials') {
                                        docker.build("$DOCKERHUB_USER/my-app").push()
                                    }
                                }
                            }
                        }
                    }
                }
                stage('Deploy to Kubernetes') {
                    steps {
                        container('maven') {
                            // Despliega en Kubernetes. Necesitarás un archivo de despliegue llamado 'deployment.yaml' en tu repositorio
                            sh 'kubectl apply -f deployment.yaml'
                        }
                    }
                }
            }
        }
    }
}
