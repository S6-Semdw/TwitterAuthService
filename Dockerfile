FROM openjdk:19-jdk-alpine
MAINTAINER SemdeWilde
COPY target/TwitterAuthService-0.0.1-SNAPSHOT.jar TwitterAuthService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/TwitterAuthService-0.0.1-SNAPSHOT.jar"]