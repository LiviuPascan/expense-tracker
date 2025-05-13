# ExpenseTracker

Personal expense tracking application with Spring Boot REST API, JWT authentication, PostgreSQL, and Swagger documentation.

## Table of Contents

- [Overview](#overview)  
- [Tech Stack](#tech-stack)  
- [Features](#features)  
- [Getting Started](#getting-started)  
- [Configuration](#configuration)  
- [Swagger UI](#swagger-ui)  
- [API Reference](#api-reference)  
- [Testing](#testing)  
- [Flyway Migrations](#flyway-migrations)  
- [Roadmap](#roadmap)  
- [Contributing](#contributing)  
- [License](#license)  

## Overview

**ExpenseTracker** is a backend application for managing personal finances.  
It allows users to register, log in, and track expenses by category with a secure REST API and JWT-based authentication.

## Tech Stack

- Java 17+
- Spring Boot 3.2.x
- Spring Security + JWT
- Spring Data JPA + Hibernate
- PostgreSQL + Flyway
- MapStruct (DTO mapping)
- Swagger (springdoc-openapi)
- JUnit 5 + Mockito
- H2 (for testing)
- Maven

## Features

### Authentication

- User registration and login with JWT
- Secured API endpoints with Bearer token
- Error handling: 401 / 403 / 400

### Categories

- User-specific categories
- Add, list, and delete categories

### Expenses

- Create and view expenses
- Filter by date, category, amount
- Pagination & sorting
- Total amount calculation
- DTOs with MapStruct

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/LiviuPascan/expense-tracker.git
cd expense-tracker
```

### 2. Start PostgreSQL (via Docker)

```bash
docker run -d \
  --name pg-expense \
  -e POSTGRES_DB=expense_tracker \
  -e POSTGRES_USER=liviupostgre \
  -e POSTGRES_PASSWORD=rootroot \
  -p 5432:5432 \
  postgres:15
```

### 3. Update application config

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/expense_tracker
    username: liviupostgre
    password: rootroot
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true

jwt:
  secret: 6D69736473757065727365637265746B65793132333435363738393031
  expiration: 86400000
```

### 4. Start the app

```bash
./mvnw spring-boot:run
```

## Configuration (for testing)

In `src/test/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.flyway.enabled=false

jwt.secret=test-secret
jwt.expiration=3600000
```

## Swagger UI

1. Open in browser:  
   ```
   http://localhost:8080/swagger-ui.html
   ```

2. Click “Authorize” and enter:
   ```
   Bearer <your-JWT-token>
   ```

3. Explore and test the API directly.

## API Reference

| Method | Endpoint           | Description                                  |
|--------|--------------------|----------------------------------------------|
| POST   | `/auth/register`   | Register a new user                          |
| POST   | `/auth/login`      | Login and get a JWT token                    |
| GET    | `/api/categories`  | Get user’s categories                        |
| POST   | `/api/categories`  | Create a new category                        |
| GET    | `/api/expenses`    | Get filtered and paginated expenses         |
| POST   | `/api/expenses`    | Create a new expense                         |

Example request with filters:

```
GET /api/expenses?from=2024-01-01&to=2024-02-01&categoryId=2&minAmount=50&sortBy=amount&order=desc
Authorization: Bearer eyJhbGciOi...
```

## Testing

```bash
# Run all tests
./mvnw test

# Run specific test classes
./mvnw -Dtest=AuthServiceTest,ExpenseControllerTest test
```

Test coverage includes:

- Business logic (unit tests)
- Controllers (`@WebMvcTest`)
- Repositories (`@DataJpaTest`)
- Error handling

## Flyway Migrations

Migration files are located in:

```
src/main/resources/db/migration/
```

Example migration:

```sql
INSERT INTO users (id, username, password, role)
VALUES (1, 'admin', '$2a$10$...', 'ADMIN')
ON CONFLICT DO NOTHING;
```

## Roadmap

### Done

- JWT Authentication
- Registration & Login
- Swagger UI
- Categories CRUD
- Expense filtering & pagination
- DTO with MapStruct
- Unit and integration tests

### In Progress

- [ ] Global Exception Handling
- [ ] Controller test refactoring
- [ ] Expense update/delete support

### Planned

- [ ] Expense stats by category
- [ ] Export to CSV / PDF
- [ ] CI/CD (GitHub Actions)
- [ ] Frontend in React
- [ ] Deploy to Railway / Render

## Contributing

1. Fork the project  
2. Create a feature branch: `feature/YourFeatureName`  
3. Commit your changes  
4. Open a pull request  

## License

[MIT License](LICENSE)


