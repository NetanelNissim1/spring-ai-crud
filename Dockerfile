# ===================================================================
# Stage 1: Build Spring Boot Application with Maven & Java 21
# ===================================================================
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

ENV MAVEN_OPTS="-Xmx512m -XX:+UseG1GC"

# Copy POM and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy source code and build jar
COPY src ./src
RUN mvn clean package -DskipTests

# ===================================================================
# Stage 2: Minimal Production JRE 21 Runtime Image
# ===================================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root app user and ensure log directory exists
RUN addgroup -S appgroup && adduser -S appuser -G appgroup && mkdir -p /app/logs && chown -R appuser:appgroup /app
USER appuser

# Copy built artifact
COPY --from=build --chown=appuser:appgroup /app/target/*.jar app.jar

# Production runtime settings
ENV SPRING_PROFILES_ACTIVE=postgres
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
