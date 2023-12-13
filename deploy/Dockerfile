FROM openjdk:17-jdk-slim

WORKDIR /app
COPY ./ ./
RUN ./gradlew build

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "./build/libs/Storage-0.0.1-SNAPSHOT.jar"]