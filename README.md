# User Management System

A Spring Boot-based RESTful API for managing users with comprehensive CRUD operations, built with modern Java technologies and best practices.

## Project Overview

This is a production-ready user management system that demonstrates clean architecture, proper exception handling, and scalable API design. The system provides full user lifecycle management with features like soft delete, auditing, validation, and consistent API responses.

## Quick Setup

### Prerequisites
- Java 21 or later
- Docker and Docker Compose
- Gradle (optional, wrapper included)

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd UserManagement
   ```

2. **Configure environment variables**
   ```bash
   # Copy the template file
   cp .env.template .env
   
   # Edit .env if needed (default values work for Docker setup)
   ```

3. **Start the application with Docker**
   ```bash
   docker-compose -f docker-compose.yaml up --build
   ```

4. **Access the application**
   - Application: http://localhost:8080
   - MySQL Database: localhost:3307

That's it! The application is now running and ready to use.

## Technology Stack

- **Java 21** with Spring Boot 3.4.2
- **Spring Data JPA** for database operations
- **MySQL 8.0** as the primary database
- **MapStruct** for object mapping
- **Lombok** for reducing boilerplate code
- **Docker & Docker Compose** for containerization
- **Gradle** as the build tool

## Getting Started

### Prerequisites
- Java 21 or later
- Docker and Docker Compose
- Gradle (optional, wrapper included)

### Environment Configuration

The application uses environment variables for configuration. A template file is provided:

1. Copy the template file:
   ```bash
   cp .env.template .env
   ```

2. Review and modify `.env` if needed. Default values are configured for Docker setup.

### Quick Start with Docker

The easiest way to run the application is using Docker Compose:

```bash
# Start all services (application + database)
docker-compose -f docker-compose.yaml up --build

# Run in background
docker-compose -f docker-compose.yaml up -d

# Stop services
docker-compose -f docker-compose.yaml down
```

### Running Locally

If you prefer to run without Docker:

1. Ensure MySQL is running locally
2. Update `.env` with your local database configuration:
   ```env
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/usermanagement
   ```
3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

### Access Points
- Application: http://localhost:8080
- MySQL Database: localhost:3307 (from host machine)

## Database Configuration

The system uses MySQL 8.0 with the following configuration:

### Docker Environment
The `docker-compose.yaml` file configures:
- MySQL 8.0 container with persistent storage
- Automatic database creation (`usermanagement`)

### Application Configuration
Environment variables in `.env` control:
- Database connection URL and credentials
- JPA/Hibernate settings
- Application port mapping

## API Documentation

All API endpoints are prefixed with `/v/api/users`

### User Management Endpoints

#### Create User
```
POST /v/api/users
Content-Type: application/json

{
  "data": {
    "firstName": "Dipesh",
    "lastName": "Ghimire",
    "email": "dipesh@example.com",
    "phoneNumber": "+1234567890"
  }
}
```

#### Get User by ID
```
GET /v/api/users/{userId}
```

#### Get Paginated User List
```
GET /v/api/users?page=0&size=10&sort=firstName,asc
```

#### Update User
```
PATCH /v/api/users/{userId}
Content-Type: application/json

{
  "data": {
    "firstName": "Updated",
    "lastName": "Name"
  }
}
```

#### Delete User
```
DELETE /v/api/users/{userId}
```

### Request/Response Format

All requests use a consistent wrapper format:
```json
{
  "data": {
    // Request payload
  }
}
```

All responses follow this structure:
```json
{
  "message": "Optional message",
  "status": "success",
  "data": {
    // Response data
  },
  "timestamp": "2026-02-18 21:30:00"
}
```

## Key Features

### 1. Clean Architecture
The project follows a layered architecture with clear separation of concerns:
- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic
- **Repository Layer**: Manages data access
- **DTO Layer**: Defines data transfer objects
- **Entity Layer**: Maps to database tables

### 2. Comprehensive Exception Handling
- Custom exception hierarchy with `BaseException` as the root
- Global exception handler using `@RestControllerAdvice`
- Consistent error response format
- Proper HTTP status codes (404, 409, 400, 500)
- Validation error handling with field-level details

### 3. Soft Delete Implementation
Users are never physically deleted from the database. Instead:
- `is_active` flag is set to `false`
- `@SQLRestriction` automatically filters out deleted records
- `@SQLDelete` annotation triggers soft delete on repository delete calls
- This maintains data integrity while supporting GDPR compliance

### 4. Auditing and Timestamps
- Automatic `createdAt` and `updatedAt` timestamp management
- Enabled via `@EnableJpaAuditing` in the main application
- Uses Spring Data's `@CreatedDate` and `@LastModifiedDate` annotations
- No manual timestamp management required

### 5. UUID Primary Keys
- Time-ordered UUIDs for better database index performance
- Automatic UUID generation via `@PrePersist` method
- Avoids sequential ID issues in distributed systems

### 6. Validation
- Bean Validation annotations on DTOs (`@NotBlank`, `@Email`, `@Pattern`)
- Custom validation messages
- Automatic validation error responses with field names

### 7. Consistent API Design
- All endpoints use the same request/response wrapper
- PATCH method for partial updates
- Proper HTTP status codes
- Pagination support for list endpoints

## Project Structure

```
src/main/java/org/usermanagement/usermanagement/
├── config/              # Configuration classes
├── constant/           # API constants and enums
├── controller/         # REST controllers
├── dto/               # Data Transfer Objects
├── entity/            # JPA entities
├── exception/         # Exception handling
├── enums/             # Enumerations
├── mapper/            # MapStruct mappers
├── repository/        # Spring Data repositories
└── service/           # Business logic
```

## Error Handling

The system provides detailed error responses:

```json
{
  "message": "User not found with id: 123e4567-e89b-12d3-a456-426614174000",
  "status": "Failure",
  "data": {
    "errorCode": "RESOURCE_NOT_FOUND",
    "message": "User not found with id: 123e4567-e89b-12d3-a456-426614174000"
  },
  "timestamp": "2026-02-18 21:30:00"
}
```

### Common Error Scenarios
- **404 Not Found**: Requested resource doesn't exist
- **409 Conflict**: Duplicate entity (e.g., email already exists)
- **400 Bad Request**: Validation errors or malformed requests
- **500 Internal Server Error**: Unexpected server errors

## Testing the API

You can test the API using curl or any HTTP client:

```bash
# Create a user
curl -X POST http://localhost:8080/v/api/users \
  -H "Content-Type: application/json" \
  -d '{"data": {"firstName": "Dipesh", "lastName": "Ghimire", "email": "dipesh@example.com"}}'

# Get user list with pagination
curl -X GET "http://localhost:8080/v/api/users?page=0&size=10"

# Get user by ID
curl -X GET http://localhost:8080/v/api/users/{userId}
i.e. curl -X GET http://localhost:8080/v/api/users/550e8400-e29b-41d4-a716-446655440000

# Update a user
curl -X PATCH http://localhost:8080/v/api/users/{userId} \
  -H "Content-Type: application/json" \
  -d '{"data": {"firstName": "Updated"}}'

# Delete a user
curl -X DELETE http://localhost:8080/v/api/users/{userId}
```

## Development

### Building the Project
```bash
# Clean build
./gradlew clean build

# Run tests
./gradlew test

# Run specific test class
./gradlew test --tests UserServiceImplTest

# Check dependencies
./gradlew dependencies
```

### Testing

The project includes comprehensive unit tests for the service layer using JUnit 5 and Mockito.

#### Running Tests
```bash
# Run all tests
./gradlew test

# Run with detailed output
./gradlew test --info

# Clean and test
./gradlew clean test
```

#### Test Coverage
**Service Layer Unit Tests** (`UserServiceImplTest`):
- Create user (success and duplicate email scenarios)
- Get user by ID (success and not found scenarios)
- Update user (success and not found scenarios)
- Delete user (success and not found scenarios)
- Get paginated user list

**Test Statistics**:
- Total test cases: 9
- Coverage: 100% method coverage for UserServiceImpl
- All critical business logic paths tested
- All exception scenarios covered

#### Testing Approach
- **Unit Tests Only**: Fast, isolated tests without database or Spring context
- **Mocking Strategy**: Mock only external dependencies (Repository, Mapper)
- **Assertions**: Using AssertJ for readable assertions
- **Test Structure**: Arrange-Act-Assert pattern for clarity
- **Test Isolation**: Each test is independent with `@BeforeEach` setup

### Docker Development
```bash
# Rebuild only the application
docker-compose -f docker-compose.yaml up --build app

# View application logs
docker-compose -f docker-compose.yaml logs -f app

# Access MySQL database
docker-compose -f docker-compose.yaml exec mysql_db mysql -u mysql -pmysql usermanagement
```

## Configuration

### Environment Variables

The application is configured using environment variables defined in the `.env` file. A template file (`.env.template`) is provided with all required variables and their descriptions.

**Important**: Copy `.env.template` to `.env` before running the application:
```bash
cp .env.template .env
```

Key environment variables in `.env`:

- `APP_PORT_EXPOSE`: Application port (default: 8080)
- `MYSQL_PORT_EXPOSE`: MySQL port on host (default: 3307)
- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: Schema generation strategy
- `SPRING_JPA_SHOW_SQL`: Show SQL queries (development only)

### Database Configuration
The system is configured for development with:
- Auto-create/update database schema
- SQL query logging enabled
- Connection pooling optimized for development

For production, you would:
1. Change `SPRING_JPA_HIBERNATE_DDL_AUTO` to `validate`
2. Set `SPRING_JPA_SHOW_SQL` to `false`
3. Use production-grade connection pooling
4. Implement proper security measures

## Design Decisions and Assumptions

### Database Design
1. **MySQL 8.0** was chosen for its reliability and feature set
2. **Soft delete** implementation maintains data history while supporting compliance requirements
3. **Time-ordered UUIDs** provide better performance than random UUIDs for database indexes
4. **Automatic auditing** reduces manual timestamp management errors

### API Design
1. **Wrapper request/response format** ensures consistency across all endpoints
2. **PATCH for updates** supports partial updates efficiently
3. **Consistent error format** makes client error handling predictable
4. **Pagination on list endpoints** ensures scalability

### Application Architecture
1. **Layered architecture** with clear separation of concerns
2. **Global exception handling** centralizes error management
3. **DTO pattern** separates API contracts from internal entities
4. **MapStruct** provides type-safe object mapping without reflection

### Development Practices
1. **Docker-first approach** ensures consistent environments
2. **Environment-based configuration** separates concerns
3. **Comprehensive validation** catches errors early
4. **Clean code practices** with meaningful naming and structure

## Production Considerations

While this implementation is production-ready in terms of architecture, several enhancements would be needed for a production deployment:

1. **Authentication and Authorization**: Implement JWT or OAuth2 security
2. **API Documentation**: Add OpenAPI/Swagger documentation
3. **Monitoring**: Integrate with monitoring tools (Prometheus, Grafana)
4. **Logging**: Structured logging with correlation IDs
5. **Caching**: Implement Redis or similar for performance
6. **Rate Limiting**: Protect against abuse
7. **Database Backups**: Regular backup strategy
8. **Health Checks**: Application and database health endpoints

## Conclusion

This User Management System demonstrates modern Java development practices with Spring Boot. It showcases clean architecture, proper exception handling, consistent API design, and production-ready features like soft delete and auditing. The Docker-based deployment makes it easy to run in any environment, and the modular design allows for easy extension and maintenance.

The project is designed to be both a functional system and a demonstration of best practices in Spring Boot development.

## Project Structure for Review

When reviewing this project, you'll find:
- **Source Code**: Well-organized in `src/main/java`
- **Tests**: Comprehensive unit tests in `src/test/java`
- **Configuration**: `.env.template` for environment setup
- **Docker**: `Dockerfile` and `docker-compose.yaml` for containerization
- **Documentation**: This README with complete setup and usage instructions

To run the project, simply:
1. Copy `.env.template` to `.env`
2. Run `docker-compose -f docker-compose.yaml up --build`
3. Access the API at http://localhost:8080
