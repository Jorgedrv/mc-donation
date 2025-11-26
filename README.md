# 🟦 mc-donation — Donation Management Microservice (Spring Boot 3 + AWS)

**mc-donation** is a production-ready microservice built with **Spring Boot 3**, applying **Clean Architecture**, **TDD**, and **Domain-Driven Design (DDD)** principles.  
It manages **Campaigns**, **Donors**, and **Donations**, and is fully prepared to run on cloud environments like **AWS**.

The project includes strong testing coverage, SonarQube quality checks, Swagger documentation, and a clean architecture suitable for real-world systems and portfolio demonstrations.

---

## 🚀 Features

### ✔ Clean Architecture
- Domain models isolated from infrastructure
- Repository interfaces + JPA adapters
- Dedicated DTO ↔ Domain ↔ Entity mappers

### ✔ RESTful API + Swagger
Interactive API documentation available at:


### ✔ Full Testing & Code Quality
- JUnit 5 + Mockito
- Jacoco coverage reporting
- SonarQube integration
- Strict TDD on core modules

### ✔ Cloud Ready (AWS)
Compatible with:
- RDS (PostgreSQL)
- EC2 / Elastic Beanstalk
- API Gateway
- AWS SQS for async flows (future extension)

### ✔ Centralized Exception Handling
Handles:
- CampaignNotFoundException
- DonorNotFoundException
- DonationNotFoundException

---

## 🧱 Architecture Overview

mc-donation
├── domain
│ ├── model
│ ├── service
│ └── repository
│
├── infrastructure
│ ├── controller
│ ├── repository/jpa
│ ├── mapper
│ └── config
│
└── common
├── exception
└── handler

---

## 📚 Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java 17 |
| Framework | Spring Boot 3 |
| API Docs | SpringDoc OpenAPI |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL (prod), H2 (dev) |
| Testing | JUnit 5, Mockito |
| Quality | Jacoco, SonarQube |
| Build | Gradle |

---

## 📌 API Endpoints

### **Campaigns**
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/campaigns` | Create campaign |
| GET | `/campaigns/all` | List campaigns |
| GET | `/campaigns/{id}` | Get by ID |
| PUT | `/campaigns/{id}` | Update |
| DELETE | `/campaigns/{id}` | Delete |

### **Donors**
| Method | Endpoint |
|--------|----------|
| POST | `/donors` |
| GET | `/donors/all` |
| GET | `/donors/{id}` |
| PUT | `/donors/{id}` |
| DELETE | `/donors/{id}` |

### **Donations**
| Method | Endpoint |
|--------|----------|
| POST | `/donations` |
| GET | `/donations/all` |
| GET | `/donations/{id}` |
| PUT | `/donations/{id}` |
| DELETE | `/donations/{id}` |

---

## 🧪 Testing & Code Quality

### Run Tests
```sh
./gradlew test 
```

### Generate Jacoco Coverage
```sh
./gradlew jacocoTestReport
```
### Coverage report:
build/reports/jacoco/test/html/index.html

### Run SonarQube
```sh
./gradlew sonar
```
### Running the Application
```sh
./gradlew bootRun
```

### Swagger Docs
http://localhost:8080/swagger-ui.html

### ☁️ AWS Deployment (Recommended)
Frontend (React)
↓
API Gateway → Spring Boot (EC2 or Elastic Beanstalk)
↓
RDS (PostgreSQL)
↓
SQS Queue (future async processing)

### 🤝 Contribution

This project is a personal learning & portfolio initiative.
Contributions and suggestions are welcome.
