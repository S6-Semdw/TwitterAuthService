FROM openjdk:19
MAINTAINER SemdeWilde
COPY target/AuthService-0.0.1-SNAPSHOT.jar TwitterAuthService-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/TwitterAuthService-0.0.1-SNAPSHOT.jar"]
FROM openjdk:19