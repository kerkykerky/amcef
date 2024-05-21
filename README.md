README
======

Project Overview
----------------

This project is a Spring Boot application that provides RESTful APIs for managing posts. It integrates with an external JSON Placeholder API to fetch post information. The application uses Spring Data JPA for data persistence, OpenFeign for making external API calls, and Springdoc OpenAPI for API documentation.

Prerequisites
-------------

-   Java 17
-   Maven 3.6+
-   Docker
-   An IDE like IntelliJ IDEA (recommended)

Installation
------------

1.  Clone the repository

    ```sh
    git clone https://github.com/your-username/amcef-task.git
    ```

2.  Navigate to the project directory

    ```sh
    cd amcef-task
    ```

3.  Build the project

    ```sh
    mvn clean install
    ```

Configuration
-------------

The application uses an H2 in-memory database by default, which requires no additional configuration for local development.

Running the Application
-----------------------

1.  Run the Spring Boot application

    ```sh
    mvn spring-boot:run
    ```

Running the Application with Docker
-----------------------------------

1.  Build the Docker image

    ```sh
    docker build -t amcef-task .
    ```

2.  Run the Docker container

    ```sh
    docker run -p 8080:8080 amcef-task
    ```

API Documentation
-----------------

The API documentation is generated using Springdoc OpenAPI and can be accessed at:

`http://localhost:8080/swagger-ui.html`

This will display the Swagger UI where you can interact with the API endpoints.

### Available Endpoints

-   POST /posts

    -   Description: Add a new post
    -   Request Body: `Post` object
    -   Response: `Post` object with status 201
-   GET /posts/{id}

    -   Description: Retrieve a post by ID
    -   Path Parameter: `id` (Integer)
    -   Response: `Post` object with status 200 or 404 if not found
-   GET /posts/user

    -   Description: Retrieve posts by user ID
    -   Query Parameter: `userId` (Integer)
    -   Response: List of `Post` objects with status 200 or 404 if not found
-   DELETE /posts/{id}

    -   Description: Delete a post by ID
    -   Path Parameter: `id` (Integer)
    -   Response: Status 204 or 404 if not found
-   PUT /posts/{id}

    -   Description: Update a post by ID
    -   Path Parameter: `id` (Integer)
    -   Request Body: `Post` object
    -   Response: `Post` object with status 200 or 404 if not found

Testing the Application
-----------------------

1.  Using Postman

    -   Import the provided Postman collection to test the API endpoints.
    -   Ensure the application is running on `http://localhost:8080`.
2.  Using curl

    -   Example to add a post:

        `curl -X POST "http://localhost:8080/posts" -H "Content-Type: application/json" -d '{"userId": 1, "title": "Sample Post", "body": "This is a sample post."}'`

Error Handling
--------------

The application uses custom exception handling to provide meaningful error messages. For example:

-   If a user ID does not exist, a `UserNotFoundException` is thrown.
-   If a post ID does not exist, a `PostNotFoundException` is thrown.

These exceptions return appropriate HTTP status codes and messages.
