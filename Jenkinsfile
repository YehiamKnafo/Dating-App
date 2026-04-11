pipeline {
    agent {
        label 'yehiamE'
    }
    environment {
        // Define environment variables (can be loaded from Jenkins credentials)
        JAVA_DATING_APP_VERSION = "1.1.0"
        VERSION_TYPE = "BETA"
        GITHUB_TOKEN = credentials("GITHUB_TOKEN")
        RENDER_DEPLOY_HOOK = credentials("RENDER_DEPLOY_HOOK")
        WHATS_NEW = "Added check for updates and better app versioning"
    }




    stages {
        // Stage 1: Checkout code from SCM (Git)

        stage('Checkout') {
            steps {
                checkout scm // Checkout code from the configured SCM (e.g., Git)
            
            }
            
        }
        stage('Build Docker') {
            steps {
                dir('miniBackendForCredentials'){
                    
                    powershell """
                        docker build --pull --rm -f "Dockerfile" `
                        --build-arg APP_VERSION="${env.JAVA_DATING_APP_VERSION}" `
                        --build-arg VERSION_TYPE="${env.VERSION_TYPE}" `
                        -t "yehiamfinseshyt/yehiam-dating-app-offical-site:${env.JAVA_DATING_APP_VERSION}" `
                        -t "yehiamfinseshyt/yehiam-dating-app-offical-site:latest" .
                    """
                }
                // Using env. to access the variables above
            }
        }
        stage('Push to Docker Hub') {
            steps {
                dir('miniBackendForCredentials'){
                    powershell """
                        Write-Host "Pushing image to Docker Hub..."
                        docker push yehiamfinseshyt/yehiam-dating-app-offical-site:${env.JAVA_DATING_APP_VERSION}
                        docker push "yehiamfinseshyt/yehiam-dating-app-offical-site:latest"
                    """ 
                }
            }
        }
        stage('Deploy to Render') {
            steps {
                powershell """
                    Write-Host "Pinging Render to pull the latest image..."
                    Invoke-RestMethod -Uri "${env.RENDER_DEPLOY_HOOK}" -Method Post
                """
            }
        }
        stage('Prepare Version') {
            steps {
                dir('Frontend/src/main/resources'){
                powershell """
                    (Get-Content version.properties) -replace 'app.version=.*', 'app.version=${env.JAVA_DATING_APP_VERSION}' | Set-Content version.properties
                """

                }
            }
        }
        stage('createInstaller'){
            steps{
                dir('Frontend'){
                    script{
                        bat 'gradle clean jpackage'
                    }
                }
            }
            
        }
        // Stage 4: Create ZIP file
        stage('Create Zip') {
            steps {
                dir('Frontend'){
                    dir('build/jpackage'){
                        script{
                            bat "dir /b"
                            bat "rename DatingApp-${env.JAVA_DATING_APP_VERSION}-${VERSION_TYPE} DatingApp" // make only the exe hold the 
                        }
                    }
                    script {
                            bat 'gradle zipExe' // Use 'sh' for Linux/macOS
                    
                    }
                }
            }
        }
   

    

        

        // Stage 5: Publish GitHub Release
      stage('GitHub Release') {
        steps {
            withCredentials([string(credentialsId: 'GITHUB_TOKEN', variable: 'GITHUB_TOKEN')]) {
                script {
                    dir('Frontend') {
                        bat "echo - Built automatically by Jenkins > release-notes.md"
                        bat "echo - High performance Beta build >> release-notes.md"
                        bat "echo - ${env.WHATS_NEW} >> release-notes.md"
                        def releaseExists = bat(script: "gh release view ${env.JAVA_DATING_APP_VERSION}-${env.VERSION_TYPE}", returnStatus: true) == 0
                        if(releaseExists){
                            echo "Old release found. Deleting..."
                            bat "gh release delete ${env.JAVA_DATING_APP_VERSION}-${env.VERSION_TYPE} --yes"
                        }else {
                            echo "No existing release found. Proceeding to create."
                        }
                        // 1. Create the release
                        // Use --target to tell GH which branch to tag
                        bat "gh release create ${env.JAVA_DATING_APP_VERSION}-${env.VERSION_TYPE} --target main --title \"Release ${env.JAVA_DATING_APP_VERSION}-${env.VERSION_TYPE}\" --notes-file release-notes.md"
                        
                        // 2. Upload the asset (streams from disk, no OOM error!)
                        dir('build/distributions'){
                            bat "rename DatingApp.zip DatingApp-${env.JAVA_DATING_APP_VERSION}-${env.VERSION_TYPE}.zip"
                            bat "gh release upload ${env.JAVA_DATING_APP_VERSION}-${env.VERSION_TYPE} DatingApp-${env.JAVA_DATING_APP_VERSION}-${env.VERSION_TYPE}.zip"
                        }
                        dir('build/jpackage') {
                            bat "dir /b"
                            bat "rename DatingApp-Installer-1.0.msi DatingApp-Installer.msi"
                            bat "gh release upload ${env.JAVA_DATING_APP_VERSION}-${env.VERSION_TYPE} DatingApp-Installer.msi"
                        }
                    }
                }
            }
        }
}


    }

    post {
        success {
            echo 'Pipeline succeeded! 🎉'
            // Notify the team
        }
        failure {
            echo 'Pipeline failed! 😢'
            // Notify the team
        }
        cleanup {
            script {
                // This safely deletes the workspace
                deleteDir()
            }
        }
    }

}