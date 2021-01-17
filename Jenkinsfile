pipeline {
    environment {
        registry = "dermacon/jenkins-test"
        registryCredentials = 'dermacon'
        dockerImage = ''
    }
    agent any
    tools {
        maven 'Maven 5.4.0-42-generic'
        jdk 'JDK 11.0.8'
    }
    stages {
        stage ('Initialize') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                    mvn -v
                '''
            }
        }

        stage ('Maven Build') {
            steps {
                sh 'mvn clean package'
            }
            post {
                success {
                    junit 'target/surefire-reports/**/*.xml' 
                    sh 'echo "about to push war to git"'
                    sh 'ls -la ./target/'
                    sh 'git rev-parse --is-inside-work-tree'
                }
            }
        }
       
    }
}
