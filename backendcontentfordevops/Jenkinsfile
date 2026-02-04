pipeline {
    agent {
        label 'unix || win' // Use either Unix or Windows agent
    }
    environment {
        // Define environment variables (can be loaded from Jenkins credentials)
        EXPRESS = credentials('EXPRESS')
        TCP = credentials('TCP')
        MONGO_URL = credentials('MONGO_URL')
        JWT_SECRET = credentials('JWT_SECRET')
        GMAIL_SMTP_EMAIL=credentials('GMAIL_SMTP_EMAIL')
        GMAIL_SMTP_PASSWORD=credentials('GMAIL_SMTP_WORD')
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm    
            }
        }
        stage('Add Env File') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                        cat <<EOF > .env
                        EXPRESS=${EXPRESS}
                        JWT_SECRET=${JWT_SECRET}
                        TCP=${TCP}
                        MONGO_URL=${MONGO_URL}
                        GMAIL_SMTP_EMAIL=${GMAIL_SMTP_EMAIL}
                        GMAIL_SMTP_PASSWORD=${GMAIL_SMTP_PASSWORD}
                        EOF
                        '''
                    } else {
                        bat '''
                        echo EXPRESS=%EXPRESS% > .env
                        echo JWT_SECRET=%JWT_SECRET% >> .env
                        echo TCP=%TCP% >> .env
                        echo MONGO_URL=%MONGO_URL% >> .env
                        echo GMAIL_SMTP_EMAIL=%GMAIL_SMTP_EMAIL% >> .env
                        echo GMAIL_SMTP_PASSWORD=%GMAIL_SMTP_PASSWORD% >> .env
                        '''
                    }
                }
            }
        }
        stage('Upload Server With Docker Compose') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'docker compose up -build --d'
                        
                    } else {
                        bat 'docker compose up -build --d'
                     
                    }
                }
            }
        }
    }
    post {
        // Actions to perform after the pipeline completes
        success {
            echo 'Pipeline succeeded! 🎉'
            // Notify the team (e.g., via email, Slack, etc.)
        }
        failure {
            echo 'Pipeline failed! 😢'
            // Notify the team (e.g., via email, Slack, etc.)
        }
    }
}