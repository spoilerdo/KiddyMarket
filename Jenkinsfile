pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh '''chmod +x gradlew
./gradlew cleanTest :Services:test'''
      }
    }
    stage('Build') {
      steps {
        sh '''chmod +x gradlew
./gradlew build'''
      }
    }
  }
}