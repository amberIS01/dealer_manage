# Dealer Management System

A Spring Boot REST API application for managing automotive dealers, vehicles, and payment processing.

## Features

- **Dealer Management**: Full CRUD operations for managing dealers with subscription types (BASIC/PREMIUM)
- **Vehicle Management**: CRUD operations for vehicles with dealer association
- **Premium Filtering**: Special endpoint to fetch vehicles belonging only to PREMIUM dealers
- **Payment Processing**: Simulated payment gateway with automatic status updates
- **JWT Authentication**: Secure payment endpoints with JWT token-based authentication
- **API Documentation**: Swagger UI for interactive API testing
- **PostgreSQL Database**: Persistent data storage with JPA/Hibernate

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose (for PostgreSQL)
- Postman (optional, for API testing)

## Quick Start

### 1. Start PostgreSQL Database

```bash
docker-compose up -d
```

This will start PostgreSQL on port 5432 with:
- Database: `dealerdb`
- Username: `postgres`
- Password: `password`

### 2. Build the Application

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

## Authentication

### Login to get JWT Token

**Endpoint**: `POST /api/auth/login`

**Request Body**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "admin"
}
```

Use this token in the `Authorization` header for secured endpoints:
```
Authorization: Bearer <your-token>
```

## API Endpoints

### Dealers (Public)
- `GET /api/dealers` - Get all dealers
- `GET /api/dealers/{id}` - Get dealer by ID
- `POST /api/dealers` - Create new dealer
- `PUT /api/dealers/{id}` - Update dealer
- `DELETE /api/dealers/{id}` - Delete dealer

### Vehicles (Public)
- `GET /api/vehicles` - Get all vehicles
- `GET /api/vehicles/{id}` - Get vehicle by ID
- `GET /api/vehicles/dealer/{dealerId}` - Get vehicles by dealer
- `GET /api/vehicles/premium` - Get all vehicles of PREMIUM dealers
- `POST /api/vehicles` - Create new vehicle
- `PUT /api/vehicles/{id}` - Update vehicle
- `DELETE /api/vehicles/{id}` - Delete vehicle

### Payments (Secured - Requires JWT)
- `POST /api/payment/initiate` - Initiate payment (auto-updates to SUCCESS after 5 seconds)
- `GET /api/payment/{id}` - Get payment by ID
- `GET /api/payment` - Get all payments
- `GET /api/payment/dealer/{dealerId}` - Get payments by dealer

## Sample Requests

### Create Dealer
```bash
curl -X POST http://localhost:8080/api/dealers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Premium Auto Dealer",
    "email": "premium@dealer.com",
    "subscriptionType": "PREMIUM"
  }'
```

### Create Vehicle
```bash
curl -X POST http://localhost:8080/api/vehicles \
  -H "Content-Type: application/json" \
  -d '{
    "dealerId": 1,
    "model": "Toyota Camry 2024",
    "price": 35000.00,
    "status": "AVAILABLE"
  }'
```

### Initiate Payment (Requires Authentication)
```bash
curl -X POST http://localhost:8080/api/payment/initiate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{
    "dealerId": 1,
    "amount": 5000.00,
    "method": "CARD"
  }'
```

## Payment Processing Flow

1. Client sends payment request to `/api/payment/initiate`
2. System creates payment record with status `PENDING`
3. Response is sent immediately to client
4. After 5 seconds, system automatically updates status to `SUCCESS`
5. Client can check payment status using GET endpoint

## Project Structure

```
src/main/java/com/dealersautocenter/
├── controller/      # REST API endpoints
├── service/         # Business logic
├── repository/      # Data access layer
├── entity/          # JPA entities
├── dto/             # Data transfer objects
├── config/          # Configuration classes
├── security/        # JWT security components
└── exception/       # Exception handling

src/main/resources/
├── application.yml  # Application configuration
```

## Testing

### Using Swagger UI
1. Navigate to `http://localhost:8080/swagger-ui.html`
2. For secured endpoints, click "Authorize" and enter JWT token
3. Test endpoints directly from the browser

### Using Postman
Import the included Postman collection for pre-configured requests.

## Technologies Used

- Spring Boot 3.2.0
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL
- Swagger/OpenAPI 3.0
- Docker & Docker Compose
- Maven

## Submission

- GitHub Repository: [Your Repository URL]
- Deployment: [Your Deployment URL]
- Email: myreport@dealersautocenter.com
- Subject: Backend Task / [Your Name]