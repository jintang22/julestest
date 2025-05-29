# Kotlin Spring Boot Demo Application

This is a sample web application built with Spring Boot 3.3.0, Kotlin 1.9.23, and Java 21. It includes a REST API for managing users and uses an H2 in-memory database.

## Prerequisites

- Java 21 or higher
- Gradle (the project uses the Gradle wrapper, so a local installation is not strictly required)

## Building the Application

To build the application, run the following command in the project root:

```bash
./gradlew build
```

This will compile the code and run the unit tests.

## Running the Application

To run the application, use the following command:

```bash
./gradlew bootRun
```

The application will start, and you can access it at `http://localhost:8080`.

## Available Endpoints

- `GET /hello`: Returns a "Hello, Spring Boot with Kotlin!" message.

### User API Endpoints (`/api/users`)

- `POST /api/users`: Creates a new user.
  - Request Body: JSON object with `username` and `email`.
    ```json
    {
      "username": "newuser",
      "email": "newuser@example.com"
    }
    ```
- `GET /api/users/{id}`: Retrieves a specific user by their ID.
- `GET /api/users`: Retrieves a list of all users.

## H2 Database Console

When the application is running, you can access the H2 in-memory database console in your browser.

- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb` (this should be pre-filled)
- **User Name**: `sa`
- **Password**: (leave blank)

Click "Connect" to access the database tables and run SQL queries.
