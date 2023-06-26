stage('Deploy') {
    steps {
        script {
            // Clonar el repositorio
            git 'https://github.com/hbkhum/eCommerce-JavaBackend.git'

            // Aplicar el archivo de despliegue
            sh 'kubectl apply -f ecommerce-backend-deployment.yaml'
        }
    }
}
