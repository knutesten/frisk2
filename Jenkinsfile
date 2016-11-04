node {
  env.PATH = "${tool 'Maven 3'}/bin:${env.PATH}"

  stage('Checkout') {
    checkout scm
  }

  stage('Build') {
    sh 'mvn clean install -DskipTests'
  }

  stage('Test') {
    sh 'mvn test'
  }
}
