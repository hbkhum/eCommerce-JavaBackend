# eCommerce Java Backend

## Description

This project implements a backend for an eCommerce application using Java and Spring Boot. It is designed to provide a robust and scalable infrastructure for handling eCommerce business logic and data management.

## Architecture

The architecture of this application follows the hexagonal/onion architecture pattern, which includes the following layers:

### Domain Layer

- Contains entities, value objects, enums, and exceptions that model the core business logic.
- Applies the principle of encapsulation to isolate the domain from external details.

### Application Layer

- Manages use cases, services, application logic, and job handling.
- Orchestrates the application flow and business logic, integrating external components.

### Infrastructure Layer

- Provides concrete implementations for operations like database access, mail sending, and external integrations.
- Utilizes the dependency inversion principle via interfaces and concrete class implementations.

### Adapters Layer

- Includes REST controllers that expose API endpoints.
- Converts domain objects to DTOs to maintain encapsulation.
- Houses adapters for other systems, such as message queues.

## Technologies

- **Java 11**: The core programming language used.
- **Spring Boot 2.x**: Framework for creating stand-alone, production-grade Spring-based applications.
- **MySQL**: Database for data persistence.
- **Maven**: Build automation and dependency management tool.

## Code Quality

Throughout the project, SOLID principles and design patterns like DAO, Repository, and Facade are applied to ensure a codebase that is decoupled, scalable, and maintainable.

## Getting Started

(Here you can add instructions on how to set up the project locally, how to run it, etc.)

```sh
# Example of setup commands
git clone https://github.com/yourusername/ecommerce-java-backend.git
cd ecommerce-java-backend
mvn install
java -jar target/your-app-name.jar
