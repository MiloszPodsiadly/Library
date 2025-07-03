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

- **Java 17**
- **Spring Boot 3.x**
- **Hibernate** / JPA
- **MySQL 8+** (or H2 for testing)
- **MapStruct**
- **Lombok**
- **JUnit 5 & Mockito**
- **Docker & Docker-Compose**

---

## 🚪 Prerequisites

- JDK 17+
- Maven 3.8+
- MySQL 8+
- Docker & Docker Compose (optional)

---

## 📖 Installation

```bash
git clone https://github.com/MiloszPodsiadly/Library.git
cd Library
```

Configure database connection in `src/main/resources/application.yml`.

Build the project:

```bash
gradle clean build
```
---

## ⚖️ Configuration

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
Sample `application.yaml` for local development:
```yml
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

jwt:
  secret: superTajnyKluczJWT12345678901234567890
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
    database: mysql
    hibernate:
      ddl-auto: update
    show-sql: false
    properties:
      hibernate:
        format_sql: false
    database-platform: org.hibernate.dialect.MySQL8Dialect

logging:
  level:
    root: ERROR
    org.springframework: INFO
    org.hibernate: ERROR
    org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration: ERROR
    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration: ERROR

debug: false

jwt:
  secret: superTajnyKluczJWT12345678901234567890
```

---

## 🚀 Running the Application

```bash
docker-compose up --build
```
```bash
gradle bootRun
```
```bash
LibraryApplication <-- with method main class
```
API will be available at: `http://localhost:8080/api/v1`

---

## 📃 API Documentation

### 🔐 Authentication

**POST** `/api/v1/users/token?email={email}&password={password}`

- Generate JWT token
- Response:
  ```json
  {
    "idUser": 1,
    "email": "john.doe@example.com",
    "name": "John",
    "surname": "Doe",
    "token": "eyJhbGciOi..."
  }
  ```

### 👤 Users

| Method | Endpoint      | Description         |
| ------ | ------------- | ------------------- |
| POST   | `/users`      | Register a new user |
| GET    | `/users`      | List all users      |
| GET    | `/users/{id}` | Get user by ID      |
| PUT    | `/users/{id}` | Update user         |
| DELETE | `/users/{id}` | Delete user         |

### 📚 Books

| Method | Endpoint               | Description            |
| ------ | ---------------------- | ---------------------- |
| GET    | `/books`               | List all books         |
| GET    | `/books/{id}`          | Get book by ID         |
| GET    | `/books/search/title`  | Search books by title  |
| GET    | `/books/search/author` | Search books by author |
| POST   | `/books`               | Add new book           |
| PUT    | `/books/{id}`          | Update book            |
| DELETE | `/books/{id}`          | Delete book            |

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

### 📦 Loans

| Method | Endpoint                           | Description          |
| ------ | ---------------------------------- | -------------------- |
| POST   | `/loans?idUser={uid}&idBook={bid}` | Create a new loan    |
| PUT    | `/loans/{loanId}/return`           | Return borrowed book |
| PUT    | `/loans/{loanId}/extend`           | Extend a loan        |
| GET    | `/loans/user/{userId}`             | List loans by user   |
| GET    | `/loans/active`                    | List active loans    |

### 🗓️ Reservations

| Method | Endpoint                                  | Description               |
| ------ | ----------------------------------------- | ------------------------- |
| POST   | `/reservations?idUser={uid}&idBook={bid}` | Create a reservation      |
| DELETE | `/reservations/{id}`                      | Cancel a reservation      |
| DELETE | `/reservations/delete/{id}`               | Delete a reservation      |
| GET    | `/reservations/user/{userId}`             | List reservations by user |
| GET    | `/reservations/book/{bookId}`             | List reservations by book |

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

