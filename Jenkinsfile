pipeline {
    agent any
    
    stages {
        stage('Clean') {
            steps {
                withEnv(["JAVA_HOME=${tool 'Java 10'}",
                         "PATH+MAVEN=${tool 'Default'}/bin:${env.JAVA_HOME}/bin"]
                       ) {
                    sh "mvn clean -B -V"
                }
            }
        }
        stage('Compile') {
            steps {
                sh "mvn compile -e -B"
            }
        }
        stage('Test') {
            steps {
                sh "mvn test -e -B -Dsurefire.useFile=false"
                step( [ $class: 'JacocoPublisher' ] )
            }
        }
//        stage('Verify') {
//            steps {
//                withMaven(
//                        maven: 'Default',
//                        jdk: 'Java 10'
//                ) {
//                    sh "mvn verify -e -B"
//                }
//            }
//        }
        stage('Install') {
            steps {
                sh "mvn install -Dmaven.test.skip=true -e -B"
            }
        }
    }
}
