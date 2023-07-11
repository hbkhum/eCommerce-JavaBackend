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

        stage('Delete Images') {
            steps {
                script {
                    try {
                        echo 'Deleting all images with the tag ecommerce-app:v1.0...'
                        sh 'docker rmi $(docker images ecommerce-app:v1.0 -q) --force'
                    } catch (Exception e) {
                        echo "Error during the image deletion stage: ${e.getMessage()}"
                        throw e
                    }
                }
            }
        }    

        stage('Delete Pods') {
            steps {
                script {
                    try {
                        // Borra los pods del namespace específico
                        sh 'kubectl delete pods --all -n ecommerce-javabackend'
                    } catch (Exception e) {
                        echo "Error al eliminar los pods: ${e.getMessage()}"
                        throw e
                    }
                }
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

        // stage('Package') {
        //     steps {
        //         script {
        //             try {
        //                 echo 'Packaging the application...'
        //                 sh 'mvn package'
        //             } catch (Exception e) {
        //                 echo "Error during the package stage: ${e.getMessage()}"
        //                 throw e
        //             }
        //         }
        //     }
        // } 
        
        stage('Deploy') {
            steps {
                script {
                    try {
                        echo 'Deploying the application...'
                        sh "sed -i 's|ecommerce-app:v1.0|${imageName}:${imageTag}|' ${deploymentFile}"
                        sh "kubectl apply -f ${deploymentFile} -n ecommerce-javabackend" // Agrega el flag -n para especificar el namespace
                    } catch (Exception e) {
                        echo "Error during the deploy stage: ${e.getMessage()}"
                        throw e
                    }
                }
            }
        }
    }
}
