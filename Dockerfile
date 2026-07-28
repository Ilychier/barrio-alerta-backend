# ============================================================
# Dockerfile — Barrio Alerta Backend
# Build multi-stage: Maven → JRE Alpine
# ============================================================

# ============================================================
# STAGE 1: Build — Compila el JAR con Maven
# ============================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# ============================================================
# STAGE 2: Runtime — JRE Alpine mínimo
# ============================================================
FROM eclipse-temurin:21-jre-alpine

LABEL org.opencontainers.image.title="Barrio Alerta Backend" \
      org.opencontainers.image.description="API Spring Boot 4 con PostgreSQL para Barrio Alerta" \
      org.opencontainers.image.vendor="Barrio Alerta" \
      org.opencontainers.image.authors="juliancamilohah@gmail.com" \
      org.opencontainers.image.source="https://github.com/Ilychier/barrio-alerta-frontend"

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]