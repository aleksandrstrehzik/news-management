FROM openjdk:17-alpine

ARG JAR_FILE=build/libs/config-cloud-1.0.0.jar

WORKDIR /opt/config

COPY ${JAR_FILE} config.jar

ENTRYPOINT ["java", "-jar", "config.jar"]