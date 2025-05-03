# URL Shortener Service

[![Linting and Static Analysis](https://github.com/cenixeriadev/UrlShortner/actions/workflows/linting-static-analysis.yml/badge.svg)](https://github.com/cenixeriadev/UrlShortner/actions/workflows/linting-static-analysis.yml) [![CodeQL Advanced](https://github.com/cenixeriadev/UrlShortner/actions/workflows/codeql.yml/badge.svg?branch=main)](https://github.com/cenixeriadev/UrlShortner/actions/workflows/codeql.yml) [![Maven Build and Test](https://github.com/cenixeriadev/UrlShortner/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/cenixeriadev/UrlShortner/actions/workflows/maven.yml) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


A robust backend service that shortens long URLs into unique, compact codes. Built with Spring Boot, the service uses PostgresSQL for persistence, Redis for caching, and ZooKeeper (via Apache Curator) to generate unique, incremental sequences. The code is organized using a layered architecture (controllers, services, and repositories) and is designed to scale.



---

## Table of Contents
- [Features](#features)
- [Architecture](#architecture)
- [Technologies Used](#technologies-used)
- [API Endpoints](#api-endpoints)
- [How to Run the Project](#how-to-run-the-project)
  - [Running in Your IDE](#running-in-your-ide)
  - [Running via Maven CLI](#running-via-maven-cli)
  - [Running with Docker Compose](#running-with-docker-compose)
- [Testing](#testing)
- [API Documentation](#api-documentation)
- [License](#license)
- [Contact](#contact)

---

## Features

- **URL Shortening:** Generate a unique, compact code for any long URL using Base62 encoding of an incremental sequence.
- **Redirection:** Upon receiving a short code, the service redirects to the original URL.
- **Access Statistics:** Tracks the number of times each short URL has been accessed.
- **Caching:** Uses Redis to cache URL lookups for faster redirection and reduced load on PostgresSQL.
- **Distributed Coordination:** Uses ZooKeeper for generating unique sequences in a distributed environment.
- **Layered Architecture:** Separation of concerns among controllers (REST endpoints), services (business logic), and repositories (data persistence).

---

## Architecture

This system follows a scalable and fault-tolerant architecture composed of several layers and components:

- **API Gateway / Load Balancer:**  
  Routes incoming traffic to either **Read Services** or **Write Services** based on HTTP method and endpoint. For example:
  - `GET` requests → Read Services (e.g., `GET /shorten/{shortcode}`)
  - `POST`, `PUT`, `DELETE` requests → Write Services

  This enables horizontal scaling by allowing independent scaling of read-heavy and write-heavy workloads.

- **Controller Layer:**  
  Exposes REST endpoints for shortening, resolving, updating, and deleting URLs, split into:
  - `ReadController` → handles URL resolution and stats.
  - `WriteController` → handles creation, update, and deletion of short URLs.

- **Service Layer:**  
  Contains the business logic:
  - Generates short codes using Base62 encoding of sequences from ZooKeeper.
  - Interacts with Redis to cache lookups and statistics.
  - Persists data into PostgresSQL via repositories.

- **Repository Layer:**  
  Uses Spring Data JPA for CRUD operations on URL mappings and statistics.

- **ZooKeeper (via Apache Curator):**  
  Ensures globally unique and incrementing sequences are generated across distributed instances.

- **Redis (Cache Layer):**  
  Reduces latency by caching frequently accessed short codes and their target URLs.

---

## Technologies Used

- **Java 21**
- **Spring Boot** with Spring WebFlux (for reactive endpoints)  
- **Spring Data JPA** (with PostgresSQL)
- **Redis** (for caching)
- **ZooKeeper (Apache Curator)** (for distributed sequence generation)
- **Maven** for build and dependency management
- **JUnit 5 and Mockito** for testing
- **H2 Database** (used for integration testing)
- **Docker & Docker Compose** for containerization

---

## API Endpoints

Below are the key endpoints provided by the service, along with detailed explanations:

### Write API Endpoints

- **POST `/api/v1/write/shorten`**
  
  **Description:**
  
  Accepts a JSON payload containing a URL to be shortened. The service generates a unique short code (using ZooKeeper for sequence generation and Base62 encoding) and stores the mapping in PostgresSQL. It also caches the result in Redis.
  
  **Example Request Body:**
  
  ```json
  {
    "url": "https://example.com/very-long-url"
  }
  ```
  
  **Response:**
  
  Returns a 201 Created status with the generated short code in the response body, e.g., "1Z3".

- **PUT `/api/v1/write/shorten/{shortcode}`**
  
  **Description:**
  
  Updates the original URL associated with an existing short code.
  
  **Example Request Body:**
  
  Provide the new URL in the JSON payload.
  
  ```json
  {
    "url": "https://updatedexample.com/new-long-url"
  }
  ```
  **Response:**
  Returns a 200 OK with a confirmation message like "Short URL updated successfully".
  
- **DELETE `/api/v1/write/shorten/{shortcode}`**
  
  **Description:**
  
  Deletes the URL mapping for a given short code from the database and clears the corresponding Redis cache.
  
  **Response:**
  
    Returns a 204 No Content status on success.
  
### Read API Endpoints

- **GET `/api/v1/read/shorten/{shortcode}`**
  
  **Description:**
  
  Retrieves the original URL for a given short code. The controller first checks Redis for a cached entry; if not found, it falls back to fetching from PostgresSQL. It also increments the access count.

  **Response:**
  
  Returns an HTTP 200 OK status with the Location header set to the original URL to trigger a client redirection.
  
- **GET `/api/v1/read/shorten/{shortcode}/stats`**
  
  **Description:**
  
  Returns the access statistics (e.g., how many times the short URL has been accessed) for a given short code.

  **Response:**
  
  Returns a JSON object with statistics:
  
  ```json
  {
    "accessCount": 25
  }
  ```


## How to Run the Project


**You can run this project in three different ways:**

### Running in Your IDE:
  
  - **Clone the Repository:**
     ```bash
     git clone https://github.com/cenixeriadev/UrlShortner.git
     cd UrlShortner
     ```
     
  - **Environment Setup: Create a .env file in the project root:**

     ```env
     DB_USER=your_db_username
     DB_PASSWORD=your_db_password
     ```
    
  Run the Application: Open the project in your IDE (e.g., IntelliJ IDEA) and run the main class (BackendApplication.java).
  
> [!WARNING]
>  Ensure that PostgresSQL, Redis, and ZooKeeper services are running on their default ports (or update the configuration accordingly).

### Running via Maven CLI

  - **Build the Project:**
    
     ```bash
     ./mvnw clean package -DskipTests
     ```
    
  - **Run the Application:**
    
     ```bash
     ./mvnw spring-boot:run
     ```
    
  - **Or Run the JAR File:**
    
     ```bash
     java -jar target/backend-0.0.1-SNAPSHOT.jar
     ```
      
>[!Note]
>Make sure environment variables are set (or provided via a .env file) so that database credentials are correctly injected.

### Running with Docker Compose

  - **Ensure Docker and Docker Compose Are Installed.**

  
  - **Verify Your docker-compose.yml is Configured Correctly:**

    The Docker Compose file should include services for the backend, PostgresSQL, Redis, and ZooKeeper. Here’s an example:

     ```yaml
        version: '3.8' 
        services:
    
          backend:
          
            build: .
          
            ports:
              - "8080:8080"
            env_file:
                - .env
            environment:
              - DB_USER=${DB_USER}
              - DB_PASSWORD=${DB_PASSWORD}
            depends_on:
              - db
              - redis
              - zookeeper
            restart: always
        
          db:
            image: postgres:16
            
            env_file:
              - .env
            environment:
              POSTGRES_USER: ${DB_USER}
              POSTGRES_PASSWORD: ${DB_PASSWORD}
              POSTGRES_DB: your_database_name
            
            ports:
              - "5432:5432"
            volumes:
              - pgdata:/var/lib/postgresql/data
              # Optionally mount SQL scripts for initialization:
              # - ./db:/docker-entrypoint-initdb.d
            restart: always
        
          redis:
            image: redis:alpine
            ports:
              - "6379:6379"
            restart: always
        
          zookeeper:
            image: zookeeper:3.8
            ports:
              - "2181:2181"
            environment:
              ZOO_MY_ID: 1
              ZOO_PORT: 2181
              ZOO_SERVERS: server.1=0.0.0.0:2888:3888
            restart: always
        
        volumes:
          pgdata:
     ```    
  - **Start the Services:**
     ```bash
     docker-compose up --build
     ```

## Testing

  Unit and integration tests have been implemented for controllers, services, and repositories. To run the tests, execute:
   ```bash
   ./mvnw test
   ```
 
>[!NOTE]
>Integration tests use an embedded H2 database (configured in application-test.properties under src/test/resources), while unit tests use Mockito to isolate components.

## API Documentation
The API documentation is available through Swagger , a tool that allows you to visualize and test endpoints interactively. Once the project is running, you can access the Swagger UI at the following URL: 
 
 
    http://localhost:8081/swagger-ui.html
 
 
Steps to Access the Documentation: 

- Ensure the project is up and running. If you're using Docker Compose, follow the instructions in the Running with Docker Compose  section.
- Open a web browser and navigate to the provided URL.
- Explore the available endpoints and test different operations directly from the interface.
     
>[!NOTE]
>The above URL (http://localhost:8081/swagger-ui.html) is valid only when running the project locally. If the project is deployed in a different environment, make sure to adjust the URL accordingly. 
     
## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Road Map Project
- Link: https://roadmap.sh/projects/url-shortening-service

## Contact
If you have any questions or need further assistance, feel free to contact me at [codeartprogrammer@gmail.com].
