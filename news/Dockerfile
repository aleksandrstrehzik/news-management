FROM openjdk:17-alpine

ARG JAR_FILE=build/libs/news-1.0.0.jar

WORKDIR /opt/news

COPY ${JAR_FILE} news.jar

ENTRYPOINT ["java", "-jar", "news.jar"]