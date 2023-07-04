def imageName = 'ecommerce-app'
def imageTag = 'v1.0'
def deploymentFile = 'ecommerce-backend-deployment.yaml'
def dockerImage

pipeline {
    agent any

    stage('Setup') {
        steps {
            script {
                try {
                    // Aplicar configuraciones iniciales del clúster de Kubernetes
                    sh "kubectl apply -f setup.yaml"
                } catch (Exception e) {
                    echo "Error during the setup stage: ${e.getMessage()}"
                    throw e
                }
            }
        }
    }        

    stages {
        stage('Checkout') {
            steps {
                checkout scm
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

        stage('Test') {
            steps {
                script {
                    try {
                        echo 'Running tests on the Docker image...'
                        sh "docker run --rm -v \$(pwd):/example1 ${dockerImage.id} mvn -f /example1/pom.xml test"
                    } catch (Exception e) {
                        echo "Error during the test stage: ${e.getMessage()}"
                        throw e
                    }
                }
            }
        }
        
        stage('Package') {
            steps {
                script {
                    try {
                        echo 'Packaging the application in the Docker image...'
                        sh "docker run --rm -v \$(pwd):/example1 ${dockerImage.id} mvn -f /example1/pom.xml package"
                    } catch (Exception e) {
                        echo "Error during the package stage: ${e.getMessage()}"
                        throw e
                    }
                }
            }
        }
        
        stage('Apply Role and Binding') {
            steps {
                script {
                    try {
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
