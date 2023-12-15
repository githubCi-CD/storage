# Build Stage
FROM openjdk:17-jdk-slim AS build
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
COPY --from=build /app/deploy/pinpoint /app/pinpoint

EXPOSE 8081

ENTRYPOINT ["java", "-javaagent:/app/pinpoint/pinpoint-bootstrap-2.5.3.jar", "-Dpinpoint.adntId=storage", "-Dpinpoint.applicationName=storage", "-Dpinpoint.config=/app/pinpoint/pinpoint-root.config" ,"-jar", "app.jar"]