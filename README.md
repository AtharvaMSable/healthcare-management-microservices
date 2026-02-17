# 🏥 Patient Management System — Enterprise Microservices

A production-ready, **enterprise-level Patient Management System** built using a **Microservices Architecture** with **Java 21 + Spring Boot 3**.

This project demonstrates a **hybrid communication strategy**:

* **REST (JSON)** for external client communication
* **gRPC (Protobuf)** for low-latency internal calls
* **Apache Kafka** for event-driven asynchronous workflows

The infrastructure is defined using **AWS CDK (IaC)** and can be deployed locally using **LocalStack** (AWS simulation).

---

## 🏗 Architecture Overview

The system is composed of multiple decoupled microservices hidden behind an **API Gateway**.

### Communication Strategy

✅ **Synchronous (Internal)**

* gRPC (Protocol Buffers)
* Example: Billing account creation when a patient is registered

✅ **Asynchronous (Event Driven)**

* Apache Kafka
* Example: Analytics and Notifications consume `PatientCreated` events

✅ **External Client Access**

* REST APIs with JSON via API Gateway

---

## 🧩 Microservices

| Service               | Description                                                        |
| --------------------- | ------------------------------------------------------------------ |
| **API Gateway**       | Spring Cloud Gateway, single entry point, routing + JWT validation |
| **Patient Service**   | Patient CRUD, PostgreSQL, Kafka Producer                           |
| **Auth Service**      | User + role management, JWT issue & validation                     |
| **Billing Service**   | gRPC server, creates billing accounts automatically                |
| **Analytics Service** | Kafka consumer for `PatientCreated` events                         |

---

## 🚀 Tech Stack

* **Language:** Java 21 (LTS)
* **Framework:** Spring Boot 3
* **API Gateway:** Spring Cloud Gateway
* **Containerization:** Docker, Docker Compose
* **Database:** PostgreSQL (Docker/Production), H2 (Local Dev)
* **Messaging:** Apache Kafka (Spring Kafka, MSK)
* **RPC:** gRPC + Protobuf
* **Infrastructure:** AWS CDK + LocalStack
* **Testing:** JUnit, Rest Assured
* **Docs:** OpenAPI / Swagger

---

## 🛠 Prerequisites

Before running the project, install:

* Java 21
* Maven
* Docker Desktop
* AWS CLI (configured for LocalStack)

---

## 🏃‍♂️ Getting Started

### 1️⃣ Local Development (Docker)

Each microservice contains its own Dockerfile. You can run services individually or bring up the entire stack using Docker Compose.

#### Clone the repository

```bash
git clone <your-repo-url>
cd patient-management-system
```

#### Build Maven projects

```bash
mvn clean install
```

#### Run using Docker Compose

```bash
docker compose up --build
```

---

### 2️⃣ Cloud Simulation (LocalStack)

To simulate AWS services locally (**ECS Fargate, RDS, MSK, ALB**) without incurring costs:

#### Start LocalStack

Run LocalStack via Docker or LocalStack Desktop.

```bash
docker compose up localstack
```

#### Deploy Infrastructure (AWS CDK)

Navigate to the infrastructure module and deploy:

```bash
cd infrastructure
chmod +x deploy.sh
./deploy.sh
```

This script uses AWS CDK to synthesize CloudFormation templates and deploy the stack into LocalStack.

---

## 🔒 Security

* **Authentication:** Dedicated Auth Service
* **Authorization:** Requires `Authorization: Bearer <token>` header
* **Gateway Filter:** API Gateway intercepts requests and validates JWT against Auth Service before forwarding traffic

---

## 🧪 Testing

The project includes automated **Integration Tests** using **Rest Assured**.

Tests validate the end-to-end flow:

1. Login
2. Get JWT Token
3. Access protected Patient endpoints

Run tests:

```bash
mvn test
```

---

## 📄 API Documentation

Once services are running, access Swagger UI via API Gateway:

* **Swagger URL:** `http://localhost:4004/api-docs/patient`

---

## 📌 Key Highlights

* Enterprise Microservices design
* Hybrid communication: REST + gRPC + Kafka
* Event-driven architecture using Kafka
* Infrastructure as Code using AWS CDK
* Local AWS simulation using LocalStack
* Production-ready Docker setup
* Integration testing with Rest Assured

---

## 📜 License

This project is intended for learning, portfolio, and enterprise architecture demonstration.
