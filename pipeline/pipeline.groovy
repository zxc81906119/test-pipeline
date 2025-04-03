pipeline{
    agent any

    stages{

        stage('test'){
            steps{

                sh 'id'
                sh 'pwd'
                sh 'hostname'
            }
        }


    }
}