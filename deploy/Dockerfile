# Build Stage
FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar", "app.jar"]