def imageName = 'ecommerce-app'
def imageTag = 'v1.0'
def deploymentFile = 'ecommerce-backend-deployment.yaml'

pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                // Build the Docker image using the Dockerfile
                script {
                    dockerImage = docker.build("${imageName}:${imageTag}", '-f Dockerfile .')
                }
            }
        }

        stage('Test') {
            steps {
                // Run tests on the Docker image
                script {
                    docker.run("--rm ${dockerImage.id}", "mvn test")
                }
            }
        }

        stage('Package') {
            steps {
                // Package the application in the Docker image
                script {
                    docker.run("--rm ${dockerImage.id}", "mvn package")
                }
            }
        }

        stage('Deploy') {
            steps {
                // Deploy the application using the deployment.yaml file
                sh "sed -i 's|${imageName}:${imageTag}|${imageName}:${imageTag}|' ${deploymentFile}"
                sh "kubectl apply -f ${deploymentFile}"
            }
        }
    }
}
