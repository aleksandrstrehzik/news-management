FROM openjdk:17-alpine

ARG JAR_FILE=build/libs/security-cloud-1.0.0.jar

WORKDIR /opt/security

COPY ${JAR_FILE} security-cloud.jar

ENTRYPOINT ["java", "-jar", "security-cloud.jar"]
