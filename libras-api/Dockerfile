FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /build

COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src ./src

RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jdk-alpine

RUN apk add --no-cache openssl libaio

RUN adduser -D -u 1000 appuser

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

RUN chown appuser:appuser /app/app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
