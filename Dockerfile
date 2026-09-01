# ===================================================================
# Stage 1: Build Spring Boot Application with Maven & Java 21
# ===================================================================
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app

# Cache Maven dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B || true

# Copy source code and build production jar
COPY src ./src
RUN mvn clean package -DskipTests

# ===================================================================
# Stage 2: Minimal Production JRE 21 Runtime Image
# ===================================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root app user
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copy built artifact from builder stage
COPY --from=build /app/target/*.jar app.jar

# Expose web server port
EXPOSE 8080

# Production environment variables
ENV SPRING_PROFILES_ACTIVE=postgres
ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
