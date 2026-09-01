# Enterprise AI Product Catalog Platform

A production-grade RESTful product management service built with **Spring Boot 3 (Java 21)** and **Spring AI**, featuring **PostgreSQL** relational persistence, AI-powered content enrichment, conversational catalog assistant with autonomous tool calling, daily rolling audit and error logging, and a modern responsive web dashboard.

---

## 🚀 Key Features

- **Full Relational CRUD**: Comprehensive entity lifecycle management for product catalog (Create, Read, Update, Delete, Filtering, Search, Pagination, Sorting).
- **AI Auto-Enrichment**: Generate marketing copy, SEO tags, bullet selling points, and automatic categorization from product names using Spring AI.
- **Conversational Assistant (`@Tool` Calling)**: Chat naturally with the catalog assistant to query database stats, search inventory, find low-stock items, or create products autonomously.
- **Review & Sentiment Analyzer**: In-depth sentiment evaluation, scoring (0-10), pros/cons extraction, and buyer recommendations.
- **Daily Rolling Logs**:
  - `logs/rest-execution-%d{yyyy-MM-dd}.log`: Automatic request/response audit logging.
  - `logs/error-%d{yyyy-MM-dd}.log`: Dedicated application error and exception log.
- **PostgreSQL Database**: Enterprise-grade persistence on `localhost:5432/productdb`.
- **Modern Web Dashboard**: Single-page responsive Hebrew interface at `http://localhost:8080`.

---

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3.3.4**
- **Spring AI 1.0.0-M4** (`spring-ai-openai-spring-boot-starter`)
- **Spring Data JPA** & **Hibernate**
- **PostgreSQL 16** (Driver `org.postgresql:postgresql`)
- **Spring Boot Starter Validation**
- **SpringDoc OpenAPI 2.6.0**
- **Lombok**
- **JUnit 5 & Mockito (36 Unit Tests)**

---

## ⚙️ Configuration (`src/main/resources/application.properties`)

```properties
# Server
server.port=8080

# PostgreSQL Production Database
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/productdb}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:postgres}
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# AI Engine Configuration
spring.ai.openai.api-key=${SPRING_AI_OPENAI_API_KEY:demo-key}
spring.ai.openai.chat.options.model=${SPRING_AI_OPENAI_MODEL:gpt-4o-mini}
spring.ai.openai.chat.options.temperature=0.7
app.ai.mock-fallback-enabled=true

# Logging
logging.file.path=logs
logging.rest.file-name=logs/rest-execution.log
logging.error.file-name=logs/error.log
```

---

## 🚀 Running the Application

### 1. Start PostgreSQL (Docker or Local Service)
```powershell
docker compose up -d
```

### 2. Run Spring Boot
```powershell
mvn spring-boot:run
```

- **Web Dashboard**: [http://localhost:8080](http://localhost:8080)
- **OpenAPI / Swagger**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **pgAdmin (Optional)**: [http://localhost:5050](http://localhost:5050)

---

## 🧪 Running Unit & Integration Tests
```powershell
mvn clean test
```
