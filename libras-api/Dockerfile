# === Stage 1: build com Maven ===
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /build

# copia apenas os arquivos de build primeiro para aproveitar cache
COPY pom.xml mvnw ./
COPY .mvn .mvn
# copia o código fonte
COPY src ./src

# roda o build sem testes para acelerar
RUN mvn -B -DskipTests package

# === Stage 2: imagem de runtime (JRE leve) ===
FROM eclipse-temurin:17-jdk-alpine

# Instala libs de sistema para o Oracle (rede, crypto e I/O)
RUN apk add --no-cache openssl libaio

# cria usuário não-root
RUN adduser -D -u 1000 appuser

WORKDIR /app

# copia o artefato gerado pelo stage de build e renomeia para app.jar
COPY --from=build /build/target/*.jar app.jar

# ajusta dono do arquivo para o usuário não-root
RUN chown appuser:appuser /app/app.jar

# usa usuário não-root
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
