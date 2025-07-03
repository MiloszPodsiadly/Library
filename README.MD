# 📚 Library Management System – Backend API

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/your-org/library-backend/actions) [![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Comprehensive, production-ready RESTful API for an online library, built with Spring Boot and Java. Provides endpoints for managing users, books, loans, reservations, reviews, favorites, fines and statistics.

---

## Table of Contents

1. [Features](#features)  
2. [Tech Stack](#tech-stack)  
3. [Prerequisites](#prerequisites)  
4. [Installation](#installation)  
5. [Configuration](#configuration)  
6. [Running the Application](#running-the-application)  
7. [API Documentation](#api-documentation)  
   - [Authentication](#authentication)  
   - [Users](#users)  
   - [Books](#books)  
   - [Book Statistics](#book-statistics)  
   - [Loans](#loans)  
   - [Reservations](#reservations)  
   - [Favorites](#favorites)  
   - [Reviews](#reviews)  
   - [Fines](#fines)  
8. [Contributing](#contributing)  
9. [License](#license)  
10. [Contact](#contact)  

---

## Features

- 🔐 **JWT-based Authentication & Authorization**  
- 👥 **User Management** (registration, update, deletion)  
- 📚 **Book Catalog** (CRUD operations)  
- 📈 **Book Statistics** (top-loaned, top-reserved, average rating, etc.)  
- 📦 **Loan Management** (borrow, return, extend)  
- 📅 **Reservation System**  
- ⭐ **Favorites** (add/remove/list)  
- 📝 **Reviews & Ratings**  
- 💰 **Fines** (issue & payment)  
- ⚙️ **Exception Handling** with consistent JSON error responses  
- 📖 **DTO + Mapper** pattern for clear separation of layers  

---

## Tech Stack

- **Java 17**  
- **Spring Boot 3.x** (Web, Data JPA, Security)  
- **Hibernate** ORM  
- **MySQL 8+** (or H2 for testing)  
- **Lombok**  
- **MapStruct** (DTO mapping)  
- **JUnit 5 & Mockito** (unit/integration tests)  
- **Docker & Docker-Compose**  

---

## Prerequisites

- JDK 17 or higher  
- Maven 3.8+  
- MySQL 8+ (or Docker)  
- (Optional) Docker & Docker-Compose  

---

## Installation

```bash
git clone https://github.com/your-org/library-backend.git
cd library-backend
Configure database (see Configuration).

Build the project

bash
Kopiuj
Edytuj
mvn clean install -DskipTests
Configuration
Create src/main/resources/application.yml with your environment settings:

yaml
Kopiuj
Edytuj
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC
    username: root
    password: password123
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false

jwt:
  secret: VerySecretKey12345
  expiration-ms: 3600000

server:
  port: 8080
Note: For production, inject sensitive values via environment variables or a secrets manager.

Running the Application
Locally
bash
Kopiuj
Edytuj
mvn spring-boot:run
With Docker
bash
Kopiuj
Edytuj
docker-compose up --build
Once started, the API will be available at:

bash
Kopiuj
Edytuj
http://localhost:8080/api/v1
API Documentation
All endpoints that modify state require a valid JWT in the Authorization header:

makefile
Kopiuj
Edytuj
Authorization: Bearer <token>
Authentication
Generate JWT
http
Kopiuj
Edytuj
POST /api/v1/users/token?email={email}&password={password}
Request Params

email (string)

password (string)

Response

json
Kopiuj
Edytuj
{
  "idUser": 1,
  "email": "john.doe@example.com",
  "name": "John",
  "surname": "Doe",
  "token": "eyJhbGciOiJIUzUxMiJ9..."
}
Users
Method	Endpoint	Description
POST	/users	Register a new user
GET	/users	List all users
GET	/users/{id}	Get user by ID
PUT	/users/{id}	Update user
DELETE	/users/{id}	Delete user (auth required)

Register Example

http
Kopiuj
Edytuj
POST /api/v1/users
Content-Type: application/json

{
  "email": "jane.doe@example.com",
  "name": "Jane",
  "surname": "Doe"
}
json
Kopiuj
Edytuj
{
  "idUser": 2,
  "email": "jane.doe@example.com",
  "name": "Jane",
  "surname": "Doe",
  "token": "..."
}
Books
Method	Endpoint	Description
GET	/books	List all books
GET	/books/{id}	Get book by ID
POST	/books	Add a new book
PUT	/books/{id}	Update book
DELETE	/books/{id}	Delete book

Add Book Example

http
Kopiuj
Edytuj
POST /api/v1/books
Content-Type: application/json
Authorization: Bearer <token>

{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "publicationYear": 2008
}
json
Kopiuj
Edytuj
{
  "idBook": 10,
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "publicationYear": 2008
}
Book Statistics
Method	Endpoint	Description
GET	/books/statistics/{id}	Stats for a single book
GET	/books/statistics/top-loaned	Top 10 most loaned
GET	/books/statistics/top-reserved	Top 3 most reserved
GET	/books/statistics/top-favorite	Top 3 most favorited
GET	/books/statistics/{id}/loans	Loan count for one book
GET	/books/statistics/{id}/reservations	Reservation count for one book
GET	/books/statistics/{id}/rating	Average rating
GET	/books/statistics/{id}/favorites	Favorite count

Loans
Method	Endpoint	Description
POST	/loans?userId={uid}&bookId={bid}	Create a new loan
PUT	/loans/{loanId}/return	Return a borrowed book
PUT	/loans/{loanId}/extend	Extend a loan
GET	/loans/user/{userId}	List loans by user
GET	/loans/active	List active loans

Reservations
Method	Endpoint	Description
POST	/reservations?userId={uid}&bookId={bid}	Create a reservation
DELETE	/reservations/{id}	Cancel a reservation
GET	/reservations/user/{userId}	List user’s reservations
GET	/reservations/book/{bookId}	List reservations for a book

Favorites
Method	Endpoint	Description
GET	/favorites/user/{userId}	List user’s favorites
POST	/favorites?userId={uid}&bookId={bid}	Add to favorites
DELETE	/favorites?userId={uid}&bookId={bid}	Remove from favorites

Reviews
Method	Endpoint	Description
POST	/reviews?userId={uid}&bookId={bid}&comment={c}&rating={r}	Create a review
DELETE	/reviews/{reviewId}	Delete a review
GET	/reviews/book/{bookId}	List reviews for a book
GET	/reviews/user/{userId}	List reviews by a user

Fines
Method	Endpoint	Description
GET	/fines/user/{userId}	List fines for a user
POST	/fines?userId={uid}&reason={reason}	Issue a new fine
PUT	/fines/{fineId}/pay	Mark a fine as paid

Contributing
Fork the repository

Create a feature branch: git checkout -b feature/YourFeature

Commit your changes: git commit -m 'Add some feature'

Push to your branch: git push origin feature/YourFeature

Open a Pull Request

Please follow the Contributor Covenant code of conduct.

License
This project is licensed under the MIT License. See the LICENSE file for details.

Contact
Project Maintainer:

Name: Jane Developer

Email: jane.dev@example.com

GitHub: github.com/your-org/library-backend

Maintained with ❤️ by Your Org

Kopiuj
Edytuj
