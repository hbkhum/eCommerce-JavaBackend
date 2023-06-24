pipeline {
    agent none
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
        stage('Run Pipeline') {
            agent {
                kubernetes {
                    // Utiliza un pod de Kubernetes con Maven y Docker
                    inheritFrom 'eCommerce-JavaBackend'
                    namespace 'kubernetes-Dev'
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
            stages {
                stage('Checkout') {
                    steps {
                        // Clona tu repositorio
                        git 'https://github.com/hbkhum/eCommerce-JavaBackend.git'
                    }
                }
                stage('Build') {
                    steps {
                        // Construye tu proyecto con Maven
                        container('maven') {
                            sh 'mvn package'
                        }
                    }
                }
                stage('Docker Build and Push') {
                    steps {
                        // Construye y sube tu imagen de Docker. Asegúrate de tener las variables de entorno DOCKERHUB_USER y DOCKERHUB_PASS
                        container('docker') {
                            script {
                                docker.build("$DOCKERHUB_USER/my-app").push()
                            }
                        }
                    }
                }
                stage('Deploy to Kubernetes') {
                    steps {
                        // Despliega en Kubernetes. Necesitarás un archivo de despliegue llamado 'deployment.yaml' en tu repositorio
                        container('maven') {
                            sh 'kubectl apply -f deployment.yaml'
                        }
                    }
                }
            }
        }
    }
}
