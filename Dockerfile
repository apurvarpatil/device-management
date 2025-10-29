
# Dockerfile
# Stage 1: build the fat jar using the Gradle wrapper (Java 21)
FROM gradle:8.6-jdk21 AS builder
WORKDIR /home/gradle/project
COPY --chown=gradle:gradle . .
RUN ./gradlew bootJar -x test --no-daemon
#
## Stage 2: runtime image (Temurin 21 JRE)
FROM eclipse-temurin:21-jre
ARG APP_HOME=/app
WORKDIR ${APP_HOME}
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

# Build:
# docker build -t device-management:latest .
# Run:
# docker run -p 8080:8080 device-management:latest
