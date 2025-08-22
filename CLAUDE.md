# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot application for managing automotive dealers and their vehicles, with payment processing capabilities. The system supports two subscription tiers (BASIC/PREMIUM) and includes payment gateway simulation.

## Core Architecture

### Technology Stack
- Spring Boot (REST APIs)
- Spring Data JPA (ORM)
- PostgreSQL (Database)
- JWT (Authentication - for payment APIs)
- Swagger/OpenAPI (API Documentation)

### Domain Entities

1. **Dealer**
   - Fields: id, name, email, subscriptionType (BASIC/PREMIUM)
   - Relationships: One-to-many with Vehicle

2. **Vehicle**
   - Fields: id, dealerId (FK), model, price, status (AVAILABLE/SOLD)
   - Relationships: Many-to-one with Dealer

3. **Payment** (for payment processing)
   - Fields: id, dealerId, amount, method (UPI/Card/NetBanking), status (PENDING/SUCCESS/FAILED), timestamp

### Key API Endpoints

- CRUD operations for Dealers: `/api/dealers`
- CRUD operations for Vehicles: `/api/vehicles`
- Premium dealer vehicles: `/api/vehicles/premium`
- Payment initiation: `/api/payment/initiate`

## Development Commands

### Project Setup
```bash
# Initialize Spring Boot project with dependencies
spring init --dependencies=web,data-jpa,postgresql,validation,security --build=maven --java-version=17 --name=dealer-management .

# Or using Spring Initializr: https://start.spring.io/
```

### Build & Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Run with specific profile
mvn spring-boot:run -Dspring.profiles.active=dev

# Run tests
mvn test

# Run specific test class
mvn test -Dtest=DealerControllerTest

# Package as JAR
mvn package
```

### Database Setup
```bash
# Start PostgreSQL (Docker)
docker run --name dealer-db -e POSTGRES_PASSWORD=password -e POSTGRES_DB=dealerdb -p 5432:5432 -d postgres:15

# Connect to database
psql -h localhost -U postgres -d dealerdb
```

### Code Quality
```bash
# Format code
mvn spring-javaformat:apply

# Check code style
mvn checkstyle:check

# Run SonarQube analysis (if configured)
mvn sonar:sonar
```

## Project Structure

```
src/
├── main/
│   ├── java/com/dealersautocenter/
│   │   ├── controller/      # REST controllers
│   │   ├── service/         # Business logic
│   │   ├── repository/      # JPA repositories
│   │   ├── entity/          # JPA entities
│   │   ├── dto/             # Data transfer objects
│   │   ├── config/          # Configuration classes (Security, Swagger)
│   │   ├── exception/       # Custom exceptions and handlers
│   │   └── util/            # Utility classes
│   └── resources/
│       ├── application.yml   # Main configuration
│       └── db/migration/     # Database migrations (if using Flyway)
└── test/                     # Test classes mirroring main structure
```

## Implementation Guidelines

### Payment Processing
- Payment status should automatically transition from PENDING to SUCCESS after 5 seconds
- Use `@Scheduled` annotation or `CompletableFuture` with delay for async status update
- Store payment records in PostgreSQL with proper transaction management

### Security (JWT)
- Secure `/api/payment/*` endpoints with JWT authentication
- Implement authentication endpoint for token generation
- Use Spring Security with JWT filter for request validation

### API Documentation
- Use Swagger/OpenAPI annotations on all controllers
- Access Swagger UI at: `http://localhost:8080/swagger-ui.html`
- Export Postman collection from Swagger for testing

### Database Configuration
Configure in `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/dealerdb
    username: postgres
    password: password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

## Testing Approach

- Unit tests for services using Mockito
- Integration tests for repositories using @DataJpaTest
- Controller tests using MockMvc
- Use @SpringBootTest for end-to-end testing
- Test payment callback simulation with appropriate timeouts

## Error Handling

- Implement global exception handler using @ControllerAdvice
- Return consistent error responses with proper HTTP status codes
- Validate input using Jakarta Bean Validation annotations
- Handle database constraints violations gracefully