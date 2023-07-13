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



        stage('Delete Pods') {
            steps {
                script {
                    try {
                        // Comprueba si existen los pods en el namespace específico
                        def podExists = sh(
                            script: 'kubectl get pods --namespace=ecommerce-javabackend',
                            returnStatus: true
                        ) == 0
        
                        if (podExists) {
                            sh 'kubectl delete pods --all -n ecommerce-javabackend --ignore-not-found'
                            echo 'Pods deleted'
                        } else {
                            echo 'No pods found, skipping deletion'
                        }
                    } catch (Exception e) {
                        echo "Error deleting pods: ${e.getMessage()}"
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
        
                    if (namespaceExists) {
                        sh "kubectl delete namespace ${namespace}"
                        echo "Namespace ${namespace} deleted"
                    }
        
                    sh "kubectl create namespace ${namespace}"
                    echo "Namespace ${namespace} created"
                }
            }
        }
       
        stage('Build') {
            steps {
                script {
                    try {
                        echo 'Building the Docker image...'
                        dockerImage = docker.build("${imageName}:${imageTag}", '-f Dockerfile .')
                        
                        echo 'Tagging the Docker image...'
                        sh "docker tag ${imageName}:${imageTag} localhost:5000/${imageName}:${imageTag}"
                        
                        echo 'Saving the Docker image...'
                        sh "docker save -o ecommerce-app.tar localhost:5000/${imageName}:${imageTag}"
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
