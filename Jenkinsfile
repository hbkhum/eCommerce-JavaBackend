def imageName = 'ecommerce-app'
def imageTag = 'v1.0'
def deploymentFile = 'ecommerce-backend-deployment.yaml'
def dockerImage

pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Namespace') {
            steps {
                script {
                    def namespace = 'ecommerce-javabackend'
                    def namespaceExists = sh(
                        script: "kubectl get namespace ${namespace}",
                        returnStatus: true
                    ) == 0
                    
                    if (!namespaceExists) {
                        sh "kubectl create namespace ${namespace}"
                        echo "Namespace ${namespace} created"
                    } else {
                        echo "Namespace ${namespace} already exists"
                    }
                }
            }
        }
        
        stage('Verificar versión de Docker') {
            steps {
                script {
                    def dockerVersion = sh(
                        script: 'docker version --format \'{{.Server.Version}}\'',
                        returnStdout: true
                    ).trim()
                    
                    echo "Versión de Docker: ${dockerVersion}"
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    try {
                        echo 'Building the Docker image...'
                        dockerImage = docker.build("${imageName}:${imageTag}", '-f Dockerfile .')
                    } catch (Exception e) {
                        echo "Error during the build stage: ${e.getMessage()}"
                        throw e
                    }
                }
            }
        }

        stage('Apply Role and Binding') {
            steps {
                script {
                    try {
                        echo 'Applying Role and Binding...'
                        sh "kubectl apply -f my-app-role.yaml"
                    } catch (Exception e) {
                        echo "Error during applying Role and Binding: ${e.getMessage()}"
                        throw e
                    }
                }
            }
        }
        
        
        stage('Deploy') {
            steps {
                script {
                    try {
                        echo 'Deploying the application...'
                        sh "sed -i 's|${imageName}:${imageTag}|${imageName}:${imageTag}|' ${deploymentFile}"
                        sh "kubectl apply -f ${deploymentFile}"
                    } catch (Exception e) {
                        echo "Error during the deploy stage: ${e.getMessage()}"
                        throw e
                    }
                }
            }
        }
    }
}
