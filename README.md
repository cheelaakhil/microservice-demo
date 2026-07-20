# 🛒 Order & Inventory Microservices Demo

A lightweight **Spring Boot Microservices** application demonstrating communication between two independent services using **Spring Cloud OpenFeign**.

The **Order Service** communicates with the **Inventory Service** via REST APIs to verify product availability and update inventory before confirming an order. Each service maintains its own independent **H2 database**, following the microservices architecture principle of database-per-service.

---

## 📌 Architecture

```text
                    +----------------+
                    |     Client     |
                    +----------------+
                            |
                            v
                  +-------------------+
                  |   Order Service   |
                  |    Port: 8082     |
                  +-------------------+
                            |
             OpenFeign REST Communication
                            |
                            v
                +------------------------+
                |   Inventory Service    |
                |      Port: 8081        |
                +------------------------+
                            |
                    H2 In-Memory Database
```

---

## 🚀 Features

- Spring Boot Microservices Architecture
- Independent databases for each service
- RESTful API communication using Spring Cloud OpenFeign
- Automatic inventory validation before order confirmation
- Inventory quantity updates after successful orders
- Graceful handling of service failures
- Layered architecture (Controller → Service → Repository)
- Zero configuration using H2 in-memory databases

---

## 🛠 Tech Stack

| Technology | Version |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.x |
| Spring Cloud OpenFeign | Latest |
| Spring Data JPA | ✓ |
| H2 Database | ✓ |
| Maven | ✓ |
| REST APIs | ✓ |

---

## 📂 Project Structure

```text
microservice-demo/
│
├── inventory-service/
│   ├── src/
│   ├── pom.xml
│   └── README.md
│
├── order-service/
│   ├── src/
│   ├── pom.xml
│   └── README.md
│
└── README.md
```

---

## ▶️ Getting Started

### Prerequisites

- Java 21 or later
- Maven 3.9+
- Git

---

### Clone the Repository

```bash
git clone https://github.com/cheelaakhil/microservice-demo.git

cd microservice-demo
```

---

### Start Inventory Service

```bash
cd inventory-service

./mvnw spring-boot:run
```

Runs on:

```
http://localhost:8081
```

---

### Start Order Service

Open another terminal.

```bash
cd order-service

./mvnw spring-boot:run
```

Runs on:

```
http://localhost:8082
```

> If Maven Wrapper is unavailable, use:

```bash
mvn spring-boot:run
```

---

# 📡 API Usage

## 1. Add a Product

```bash
curl -X POST http://localhost:8081/api/inventory/products \
-H "Content-Type: application/json" \
-d '{
  "name":"Laptop",
  "quantity":10,
  "price":55000
}'
```

### Response

```json
{
  "id": 1,
  "name": "Laptop",
  "quantity": 10,
  "price": 55000
}
```

---

## 2. Place an Order

```bash
curl -X POST \
"http://localhost:8082/api/orders?productId=1&quantity=2"
```

### Successful Response

```json
{
  "status": "CONFIRMED"
}
```

The Inventory Service automatically updates the stock from **10 → 8**.

---

## 3. Out of Stock

If the requested quantity exceeds available inventory:

```json
{
  "status": "OUT_OF_STOCK"
}
```

---

## 4. Inventory Service Unavailable

If the Inventory Service is offline:

```json
{
  "status": "INVENTORY_SERVICE_UNAVAILABLE"
}
```

The Order Service handles the failure gracefully instead of crashing.

---

# 📖 Learning Outcomes

This project demonstrates:

- Microservices architecture using Spring Boot
- Independent deployment of services
- Database-per-service design
- Inter-service communication with OpenFeign
- RESTful API development
- Exception handling and fault tolerance
- Layered software architecture

---

# 🚀 Future Enhancements

- Replace H2 with PostgreSQL or MySQL
- Dockerize both services
- Add Docker Compose support
- Integrate Eureka Service Discovery
- Implement Resilience4j Circuit Breaker
- Add Spring Cloud API Gateway
- Secure APIs with Spring Security and JWT
- Add Swagger/OpenAPI documentation
- Centralized logging using ELK Stack
- Monitoring with Prometheus & Grafana
---

## 👨‍💻 Author

**Akhil Cheela**
---

## 📄 License

This project is licensed under the **MIT License**.
