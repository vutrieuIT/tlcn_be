pipeline {
    agent any
    environment {
        APP_DB_URL = 'jdbc:mysql://host.docker.internal:3306/tlcn'
        APP_DB_USERNAME = 'root'
        APP_DB_PASSWORD = '12345'
        TZ = 'Asia/Ho_Chi_Minh'
        APP_KEYSTORE = '/app/keystore.p12'
    }
    stages {
        stage('Clone') {
            steps {
                git branch: 'protect/deploy', url: 'https://github.com/vutrieuIT/fe_client.git'
            }
        }
        stage('Check Branch') {
            steps {
                script {
                    if (env.GIT_BRANCH != 'origin/protect/deploy') {
                        echo "Not on protect/deploy branch, exiting pipeline. Branch is ${env.GIT_BRANCH}"
                        error("Not on protect/deploy branch, exiting pipeline.")
                    }
                }
            }
        }
        stage('Build Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'app_mail_user', passwordVariable: 'APP_MAIL_PASS', usernameVariable: 'APP_MAIL_USER'),
                                 string(credentialsId: 'app_keystore_pass', variable: 'APP_KEYSTORE_PASS')]) {
                    // Command to build Docker image
                    sh '''
                    docker build --build-arg APP_MAIL_USER=$APP_MAIL_USER \
                                 --build-arg APP_MAIL_PASS=$APP_MAIL_PASS \
                                 --build-arg APP_KEYSTORE_PASS=$APP_KEYSTORE_PASS \
                                 -t myapp .
                    '''
                }
            }
        }
    }
     post {
            success {
                mail bcc: '', body: 'build backend done', cc: '', from: '', replyTo: '', subject: 'jenkins build', to: 'vutrieu2002@gmail.com'
            }
            failure {
                mail bcc: '', body: 'build backend fail', cc: '', from: '', replyTo: '', subject: 'jenkins build', to: 'vutrieu2002@gmail.com'
            }
        }
}
