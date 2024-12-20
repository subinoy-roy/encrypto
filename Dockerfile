FROM openjdk:17-jdk-alpine
LABEL authors="subinoy-roy"

WORKDIR /app
COPY target/encrypo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8089

ENTRYPOINT ["java", "-jar", "app.jar"]