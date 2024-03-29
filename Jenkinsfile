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

        stage('Setup Minikube Docker Env') {
            steps {
                script {
                    // Obtener la configuración del entorno de Docker desde Minikube
                    def dockerEnv = sh(script: 'minikube -p minikube docker-env', returnStdout: true).trim()
        
                    // Parsear las líneas de la salida y extraer las variables de entorno
                    def envVars = [:]
                    dockerEnv.eachLine { line ->
                        if (line.startsWith("export")) {
                            def parts = line.split(' ')[1].split('=')
                            envVars[parts[0]] = parts[1].replaceAll('"', '')
                        }
                    }
        
                    // Configurar las variables de entorno en el contexto del build
                    envVars.each { key, value ->
                        env."${key}" = value
                    }
        
                    echo "Entorno de Docker configurado con éxito"
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
                        sh "kubectl apply -f ${deploymentFile} -n ecommerce-javabackend"
        
                        // Configurar el reenvío de puertos
                        //sh "kubectl port-forward --namespace ecommerce-javabackend service/ecommerce-java-backend 9444:9444 --address 0.0.0.0 &"
                        //sleep 10 // Esperar un momento para que el reenvío de puertos se establezca correctamente
        
                    } catch (Exception e) {
                        echo "Error during the deploy stage: ${e.getMessage()}"
                        throw e
                    }
                }
            }
        }

        //stage('Apply Metallb Config') {
        //    steps {
        //        script {
        //            try {
        //                echo 'Applying Metallb Config...'
        //                sh "kubectl apply -f metallb-config.yaml -n ecommerce-javabackend"
        //            } catch (Exception e) {
        //                echo "Error applying Metallb Config: ${e.getMessage()}"
        //                throw e
        //            }
        //        }
        //    }
        //}
        
    }
}
