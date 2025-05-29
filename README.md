# Kotlin Spring Boot Demo Application

This is a sample web application built with Spring Boot 3.3.0, Kotlin 1.9.23, and Java 21.

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
