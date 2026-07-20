# Order & Inventory Microservices Demo

A minimal 2-service Spring Boot microservices setup: **Order Service** talks to
**Inventory Service** over REST (via OpenFeign) to check and decrement stock
before confirming an order. Each service owns its own database (H2 in-memory
by default, so it runs with zero setup).

## Architecture

```
Client -> Order Service (8082) -- Feign/REST --> Inventory Service (8081)
                |                                        |
           orderdb (H2)                            inventorydb (H2)
```

## Run it

Each service is a standalone Maven project. Start Inventory Service first,
then Order Service.

```bash
cd inventory-service
./mvnw spring-boot:run
# runs on http://localhost:8081

# in a second terminal
cd order-service
./mvnw spring-boot:run
# runs on http://localhost:8082
```

(If you don't have the Maven wrapper, run `mvn spring-boot:run` with Maven installed locally.)

## Try it

1. Add a product to Inventory Service:
```bash
curl -X POST http://localhost:8081/api/inventory/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop","quantity":10,"price":55000}'
```
This returns the created product with its `id` (e.g. `1`).

2. Place an order via Order Service (it will call Inventory Service internally):
```bash
curl -X POST "http://localhost:8082/api/orders?productId=1&quantity=2"
```
Response will show `status: CONFIRMED` and stock in Inventory Service will drop to 8.

3. Try ordering more than available stock — you'll get `status: OUT_OF_STOCK`.

4. Stop Inventory Service and place another order — you'll get
`status: INVENTORY_SERVICE_UNAVAILABLE` instead of a silent failure. This
demonstrates basic fault handling between services.

## What this demonstrates (for your resume/interview)

- Two independently deployable Spring Boot services, each with its own database
- Inter-service REST communication using OpenFeign declarative clients
- Basic resilience: failed calls to a downstream service don't crash the caller
- MVC-style layering (Controller -> Service -> Repository) in both services

## Natural next steps (stretch goals, in order of effort)

1. **Swap H2 for PostgreSQL/MySQL** — just change the datasource block in
   `application.properties`. Nothing else changes.
2. **Docker Compose** — containerize both services + Postgres so you can
   `docker compose up` and demo the whole thing in one command.
3. **Eureka service discovery** — replace the hardcoded
   `inventory.service.url` with a Eureka server so Order Service discovers
   Inventory Service by name. This is the single biggest "real microservices"
   credibility boost for an interview.
4. **Resilience4j circuit breaker** — wrap the Feign client so repeated
   Inventory Service failures trip a circuit breaker instead of retrying
   forever.
5. **API Gateway** — add Spring Cloud Gateway in front of both services as a
   single entry point.

Resume bullet once this is built and (ideally) pushed to GitHub with a
Docker Compose file:

> "Designed a 2-service microservices architecture (Order Service, Inventory
> Service) using Spring Boot and Spring Cloud OpenFeign, with independent
> databases per service and fault-tolerant inter-service REST communication."
