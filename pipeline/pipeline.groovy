pipeline {
    agent {
        kubernetes {
            inheritFrom 'maven,nodejs'
        }
    }

    stages {

        stage('ci-cd') {

            steps {
                 container('nodejs'){
                     sh 'node -v'
                 }
                 container('maven') {
                     sh 'mvn -v -X'
                     writeFile file: 'pom.xml', text: '''
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <groupId>sample</groupId>
    <artifactId>sample</artifactId>
    <version>1.0-SNAPSHOT</version>
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
            </plugin>
        </plugins>
    </build>
    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.release>17</maven.compiler.release>
    </properties>
</project>
        '''
                     writeFile file: 'src/test/java/sample/SomeTest.java', text: '''
package sample;
public class SomeTest {
    @org.junit.Test
    public void checks() {}
}
        '''
                     // 改 HOME 環境變數,  這樣就會放到 $HOME/.m2 中
                     // Maven needs write access to $HOME/.m2, which it doesn't have in the maven image because only root is a real user.
                     sh 'HOME=$WORKSPACE_TMP/maven mvn -B -ntp -Dmaven.test.failure.ignore verify'
                     junit '**/target/surefire-reports/TEST-*.xml'
                     archiveArtifacts '**/target/*.jar'
                 }
            }
        }


    }
}