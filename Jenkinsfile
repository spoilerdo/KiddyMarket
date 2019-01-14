pipeline {
  agent any
  stages {
    stage('Test') {
      steps {
        sh '''chmod +x gradlew
./gradlew cleanTest test'''
        sh '''chmod +x gradlew
./gradlew :Services:showReport'''
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