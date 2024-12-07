# GestEtudiants - Student Management System

A Spring Boot application for managing student documents and academic records at ENSA Tétouan.

## Features

### Document Management
- Generate and manage academic documents:
  - School attendance certificates ([`TypeDocument.ATTESTATION_SCOLARITE`](src/main/java/ma/ensate/gestetudiants/enums/TypeDocument.java))
  - Academic transcripts ([`TypeDocument.RELEVE_NOTES`](src/main/java/ma/ensate/gestetudiants/enums/TypeDocument.java))

### Authentication & Security
- JWT-based authentication system
- Role-based access control ([`Role.ADMIN`](src/main/java/ma/ensate/gestetudiants/enums/Role.java))
- Secure password encoding with BCrypt

### Student Records
- Document request processing
- Claims handling

### Email Notifications
- Automated email notifications for:
  - Approved document requests
  - Rejected requests
  - Processed claims

### Statistics & Reporting
- Generate comprehensive statistics on:
  - Document requests
  - Processing times
  - Approval/rejection rates
  - Weekly trends

## Technical Stack

- **Backend Framework**: Spring Boot 3.4.0
- **Database**: MySQL
- **Security**: Spring Security with JWT
- **Documentation**: OpenAPI/Swagger
- **PDF Generation**: Flying Saucer + iText
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven

## Prerequisites

- Java 17
- MySQL 8.0+
- Maven 3.6+

## Configuration

1. Configure database connection in [application.properties](src/main/resources/application.properties):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gest_etu_db
spring.datasource.username=root
spring.datasource.password=password
```

2. Configure email settings:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

## Building and Running

1. Build the project:
```sh
./mvnw clean install
```

2. Run the application:
```sh
./mvnw spring-boot:run
```

The application will be available at [http://localhost:8088](http://localhost:8088)

## API Documentation

Once the application is running, access the OpenAPI documentation at:
- [http://localhost:8088/swagger-ui.html](http://localhost:8088/swagger-ui.html)

## API Endpoints

| Method | Endpoint                                  | Description                            |
| ------ | ----------------------------------------- | -------------------------------------- |
| GET    | `/api/admin/demandes`                     | Retrieve all student requests          |
| PUT    | `/api/admin/demandes/{id}/approve`        | Approve a specific student request      |
| PUT    | `/api/admin/demandes/{id}/reject`         | Reject a specific student request       |
| GET    | `/api/admin/demandes/{id}/pdf`            | Download PDF of a specific request      |
| GET    | `/api/admin/reclamations`                 | Retrieve all reclamations               |
| PUT    | `/api/admin/reclamations/{id}/treat`      | Treat a specific reclamation            |
| GET    | `/api/admin/statistiques`                 | Get application statistics              |

## Project Structure

```
src/main/java/ma/ensate/gestetudiants/
├── config/          # Configuration classes
├── controller/      # REST controllers
├── dto/             # Data Transfer Objects
├── entity/          # JPA entities
├── enums/           # Enumerations
├── exception/       # Custom exceptions
├── mapper/          # DTO-Entity mappers
├── repository/      # JPA repositories
├── service/         # Business logic
└── util/            # Utility classes
```

## Authentication

The system uses JWT for authentication. Default admin credentials:
- Username: `admin`
- Password: `admin`

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.
