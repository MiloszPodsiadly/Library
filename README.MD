# 📚 Library Management System – Backend API

&#x20;

Comprehensive, production-ready RESTful API for an online library, built with Spring Boot and Java. Provides endpoints for managing users, books, loans, reservations, reviews, favorites, fines, and statistics.

---

## ✨ Features

- 🔐 **JWT-based Authentication & Authorization**
- 👥 **User Management** (registration, update, deletion)
- 📚 **Book Catalog** (CRUD + search by title/author)
- 📊 **Book Statistics** (top-loaned, top-reserved, average rating, etc.)
- 📦 **Loan Management** (borrow, return, extend)
- 🗓️ **Reservation System**
- ⭐ **Favorites** (add/remove/list)
- 📝 **Reviews & Ratings**
- 💰 **Fines** (issue & payment)
- ⚙️ **Exception Handling** with consistent JSON error responses
- 📖 **DTO + Mapper** pattern for clean architecture

---

## 📊 Tech Stack

- **Java 21**
- **Spring Boot 3.4.4**
- **Hibernate** / JPA
- **MySQL 8.0 || H2 for testing**
- **MapStruct**
- **Lombok**
- **JUnit 5 & Mockito**
- **Docker & Docker-Compose**

---

## 🚪 Prerequisites

- JDK 21+
- Gradle 8.7.1+
- MySQL 8+
- Docker & Docker Compose (optional)

---

## 📖 Installation

```bash
git clone https://github.com/MiloszPodsiadly/Library.git
```

Configure database connection in `src/main/resources/application.yml`.

Build the project:

```bash
gradle clean build
```
---

## ⚖️ Configuration

Sample `application.yml` for local development:
```yml
jwt:
  secret: superTajnyKluczJWT12345678901234567890

spring:
  profiles:
    active: dev

---

spring:
  config:
    activate:
      on-profile: dev

  datasource:
    url: jdbc:mysql://localhost:3306/kodilla_library?serverTimezone=Europe/Warsaw&useSSL=false&allowPublicKeyRetrieval=true
    username: kodilla_user
    password: kodilla_password

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    database-platform: org.hibernate.dialect.MySQL8Dialect
    properties:
      hibernate:
        format_sql: true

---

spring:
  config:
    activate:
      on-profile: docker

  datasource:
    url: jdbc:mysql://mysql:3306/kodilla_library?serverTimezone=Europe/Warsaw&useSSL=false&allowPublicKeyRetrieval=true
    username: kodilla_user
    password: kodilla_password

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
    database-platform: org.hibernate.dialect.MySQL8Dialect
    properties:
      hibernate:
        format_sql: false

logging:
  level:
    root: ERROR
    org.springframework: INFO
    org.hibernate: ERROR
    org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration: ERROR
    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration: ERROR

debug: false

---

spring:
  config:
    activate:
      on-profile: test

  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
    username: sa
    password:

  jpa:
    hibernate:
      ddl-auto: create-drop
    database-platform: org.hibernate.dialect.H2Dialect
    generate-ddl: true
    show-sql: false
```
Sample `docker-compose.yml` for local development:
```yml
services:
  app:
    build: .
    container_name: library-app
    ports:
      - "8080:8080"
    depends_on:
      - mysql
    environment:
      SPRING_PROFILES_ACTIVE: docker
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/kodilla_library?serverTimezone=Europe/Warsaw&useSSL=false&allowPublicKeyRetrieval=true
      SPRING_DATASOURCE_USERNAME: kodilla_user
      SPRING_DATASOURCE_PASSWORD: kodilla_password
      JWT_SECRET: superTajnyKluczJWT12345678901234567890
    networks:
      - library-network


  mysql:
    image: mysql:8.0
    container_name: library-mysql
    environment:
      MYSQL_DATABASE: kodilla_library
      MYSQL_USER: kodilla_user
      MYSQL_PASSWORD: kodilla_password
      MYSQL_ROOT_PASSWORD: root
    ports:
      - "3306:3306"
    networks:
      - library-network
    volumes:
      - mysql-data:/var/lib/mysql

networks:
  library-network:

volumes:
  mysql-data:
```
Sample `build.gradle` for local development:
```bash
plugins {
    id 'java'
    id 'org.springframework.boot' version '3.4.4'
    id 'io.spring.dependency-management' version '1.1.7'
}

group = 'com.kodilla'
version = '0.0.1-SNAPSHOT'

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
    implementation 'org.projectlombok:lombok:1.18.30'

    runtimeOnly 'com.h2database:h2'
    runtimeOnly 'com.mysql:mysql-connector-j'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.5'

    testImplementation 'org.mockito:mockito-core'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'

    annotationProcessor 'org.projectlombok:lombok:1.18.30'
}

tasks.named('test') {
    useJUnitPlatform()
}
```
---

## 🚀 Running the Application

```bash
docker-compose up --build
```
```bash
LibraryApplication <-- with method main class
```
```bash
gradle bootRun
```

---

## 📃 API Documentation

API will be available at: `http://localhost:8080/api/v1`

### 🔐 Authentication

**PUT** `/users/{id}`
**DELETE** `/users/{id}`

Authorization: Bearer <your_token_here>

### 👤 Users

| Method | Endpoint       | Description         |
|--------|----------------|---------------------|
| POST   | `/users`       | Register a new user |
| GET    | `/users`       | List all users      |
| GET    | `/users/{id}`  | Get user by ID      |
| PUT    | `/users/{id}`  | Update user         |
| POST   | `/users/token` | Generate token      |
| DELETE | `/users/{id}`  | Delete user         |


- Register user of library
- Example request:
  ```json
  {
    "email": "john.doe@example.com",
    "name": "John Doe",
    "passwordHash": "DoePassword"
  }
  ```

### 📚 Books

| Method | Endpoint                                 | Description            |
| ------ |------------------------------------------| ---------------------- |
| GET    | `/books`                                 | List all books         |
| GET    | `/books/{id}`                            | Get book by ID         |
| GET    | `/books/search/title?keyword={keyword}`  | Search books by title  |
| GET    | `/books/search/author?keyword={keyword}` | Search books by author |
| POST   | `/books`                                 | Add new book           |
| PUT    | `/books/{id}`                            | Update book            |
| DELETE | `/books/{id}`                            | Delete book            |

- Add new book to library
- Example request:
  ```json
  {
    "title": "Book Title",
    "author": "Author",
    "isbn": "0000000000000001"
  } 
  ```
### 🗓️ Reservations

| Method | Endpoint                                | Description               |
|--------|-----------------------------------------|---------------------------|
| POST   | `/reservations?idUser={id}&idBook={id}` | Create a reservation      |
| DELETE | `/reservations/{id}`                    | Cancel a reservation      |
| DELETE | `/reservations/delete/{id}`             | Delete a reservation      |
| GET    | `/reservations/user/{userId}`           | List reservations by user |
| GET    | `/reservations/book/{bookId}`           | List reservations by book |

- Make a new reservation
- Example request:
  ```bash
  POST http://localhost:8080/api/v1/reservations?idUser={id}&idBook={id}
  ```
### 📦 Loans

| Method | Endpoint                           | Description          |
| ------ | ---------------------------------- | -------------------- |
| POST   | `/loans?idUser={uid}&idBook={bid}` | Create a new loan    |
| PUT    | `/loans/{loanId}/return`           | Return borrowed book |
| PUT    | `/loans/{loanId}/extend`           | Extend a loan        |
| GET    | `/loans/user/{userId}`             | List loans by user   |
| GET    | `/loans/active`                    | List active loans    |

### 📊 Book Statistics

| Method | Endpoint                              | Description                  |
| ------ | ------------------------------------- | ---------------------------- |
| GET    | `/books/statistics/{id}`              | Get stats for one book       |
| GET    | `/books/statistics/top-loaned`        | Top 10 most loaned books     |
| GET    | `/books/statistics/top-reserved`      | Top 3 most reserved books    |
| GET    | `/books/statistics/top-favorite`      | Top 3 most favorited books   |
| GET    | `/books/statistics/{id}/loans`        | Loan count for one book      |
| GET    | `/books/statistics/{id}/reservations` | Reservation count for a book |
| GET    | `/books/statistics/{id}/rating`       | Average rating of a book     |
| GET    | `/books/statistics/{id}/favorites`    | Favorite count for a book    |

### ⭐ Favorites

| Method | Endpoint                               | Description           |
| ------ | -------------------------------------- | --------------------- |
| GET    | `/favorites/user/{userId}`             | List user's favorites |
| POST   | `/favorites?idUser={uid}&idBook={bid}` | Add book to favorites |
| DELETE | `/favorites?idUser={uid}&idBook={bid}` | Remove from favorites |

### 📕 Reviews

| Method | Endpoint                                                    | Description             |
| ------ | ----------------------------------------------------------- | ----------------------- |
| POST   | `/reviews?idUser={uid}&idBook={bid}&comment={c}&rating={r}` | Create a review         |
| DELETE | `/reviews/{id}`                                             | Delete a review         |
| GET    | `/reviews/book/{bookId}`                                    | List reviews for a book |
| GET    | `/reviews/user/{userId}`                                    | List reviews by a user  |

### 💳 Fines

| Method | Endpoint                              | Description         |
| ------ | ------------------------------------- | ------------------- |
| GET    | `/fines/user/{userId}`                | List fines for user |
| POST   | `/fines?idUser={uid}&reason={reason}` | Issue a new fine    |
| PUT    | `/fines/{fineId}/pay`                 | Mark a fine as paid |

---

## 🙌 Contributing

1. Fork the repository
2. Create a new branch: `git checkout -b feature/YourFeature`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to your branch: `git push origin feature/YourFeature`
5. Open a Pull Request

Please follow the [Contributor Covenant](https://www.contributor-covenant.org/) code of conduct.

---

## 📚 License

This project is licensed under the [MIT License](LICENSE).

---

## 💎 Contact

**Project Maintainer:**

- **Name:** Milosz Podsiadly
- **Email:** [m.podsiadly99@gmail.com]
- **GitHub:** [https://github.com/MiloszPodsiadly/Library](https://github.com/MiloszPodsiadly/Library.git)

Maintained with ❤️ 

