# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:resolve
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre AS runner
WORKDIR /app
COPY --from=builder /app/target/google-trans-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
