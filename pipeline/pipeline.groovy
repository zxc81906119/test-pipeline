pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: maven
    # In a real Jenkinsfile, it is recommended to pin to a specific version and use Dependabot or Renovate to bump it.
    image: maven:latest
    resources:
      requests:
        memory: "256Mi"
      limits:
        memory: "512Mi"
    command:
    - sleep
    args:
    - infinity
    securityContext:
      # maven runs as root by default, it is recommended or even mandatory in some environments (such as pod security admission "restricted") to run as a non-root user.
      runAsUser: 1000
'''
            retries 2
        }
    }

    stages {

        stage('maven') {

            steps {
                 container('maven') {
                     sh 'id'
                     sh 'pwd'
                     sh 'hostname'
                 }
            }
        }


    }
}