# Keep Dishes Going

Keep Dishes Going is a full-stack food ordering platform built around Domain-Driven Design, Hexagonal Architecture and event-driven communication.

The platform supports both restaurant owners and customers. Restaurant owners can manage their restaurant, dishes, availability and incoming orders, while customers can browse restaurants, build an order, complete payment and follow the order through the delivery process.

The system is split into separate frontend and backend applications, with external services integrated for authentication, payments and delivery.

## Architecture

The backend is built as a modular Spring Boot application using Domain-Driven Design and Hexagonal Architecture.

The main business domains are separated into bounded contexts, including:

- **Restaurant** – restaurant configuration, menus, dishes, availability and publication
- **Order** – baskets, checkout, payments and order lifecycle
- **Security** – authentication and authorization
- **Common** – shared functionality between the modules

Spring Modulith is used to structure and separate the application modules while still keeping the backend deployable as a single application.

Communication with the delivery service is handled asynchronously through RabbitMQ.

## Event-Driven Communication

RabbitMQ is used to communicate between Keep Dishes Going and the external delivery service.

The application publishes events when an order progresses through the restaurant workflow, such as:

- Order accepted
- Order ready for pickup

It also consumes delivery events such as:

- Order picked up
- Order delivered
- Driver/location updates

This keeps the restaurant and delivery workflows loosely coupled while allowing the order state to stay synchronized.

## Restaurant Management

Restaurant owners can manage the full lifecycle of their restaurant and menu.

The platform supports:

- Creating and configuring a restaurant
- Setting opening hours
- Manually opening or closing the restaurant
- Creating and editing dishes
- Keeping dish changes as drafts
- Publishing and unpublishing dishes
- Scheduling menu publications
- Applying multiple pending changes at once
- Marking dishes in or out of stock
- Managing restaurant price-range criteria
- Accepting or rejecting incoming orders
- Automatically declining orders that are not handled in time
- Marking accepted orders as ready for pickup

## Customer Ordering

Customers can use the platform without creating an account.

The customer flow includes:

1. Browse available restaurants
2. Filter restaurants by cuisine and price range
3. View restaurant menus
4. Filter and sort dishes
5. Build a basket from a single restaurant
6. Validate dish availability before checkout
7. Enter delivery information
8. Complete payment
9. Receive an order confirmation and tracking link
10. Follow the order status through preparation and delivery

## Payments

Payments are integrated using **Stripe**.

The backend handles the payment integration and keeps the Stripe API key outside the source code through an environment variable.

```properties
stripe.api-key=${STRIPE_API_KEY}
```

## Authentication

Restaurant-owner functionality is protected using **Keycloak**.

The React application authenticates through Keycloak and sends JWT access tokens to the Spring Boot backend.

The backend acts as an OAuth2 Resource Server and validates authenticated requests before allowing access to protected restaurant-management functionality.

Customers can browse restaurants and place orders without creating an account.

## Frontend

The frontend is a React and TypeScript application built with Vite.

It provides separate flows for customers and restaurant owners and communicates with the backend through a centralized API layer.

The frontend uses:

- React 19
- TypeScript
- Vite
- React Router
- TanStack Query
- Axios
- Keycloak
- Recharts

## Backend

The backend is built with Java 21 and Spring Boot.

Core technologies include:

- Java 21
- Spring Boot 3
- Spring Modulith
- Spring Data JPA
- Spring Security
- OAuth2 Resource Server
- Spring AMQP
- RabbitMQ
- PostgreSQL
- Stripe Java SDK
- Keycloak

## Infrastructure

The local development environment is managed using Docker Compose.

The infrastructure contains:

- **PostgreSQL** – application database
- **RabbitMQ** – asynchronous messaging
- **Delivery Service** – external service used for delivery processing
- **MySQL** – Keycloak database
- **Keycloak** – identity and access management

These services are placed on separate Docker networks for the backend and authentication infrastructure.

## Repository Structure

```text
KeepDishesGoing/
├── backend/
│   ├── infrastructure/
│   │   └── docker-compose.yaml
│   ├── src/
│   │   ├── main/
│   │   └── test/
│   ├── build.gradle.kts
│   └── README.md
│
└── frontend/
    ├── public/
    ├── src/
    │   ├── assets/
    │   ├── components/
    │   ├── contexts/
    │   ├── hooks/
    │   ├── services/
    │   ├── types/
    │   └── utils/
    ├── package.json
    └── README.md
```

## Running the Project

### 1. Start the backend infrastructure

```bash
cd backend/infrastructure
docker compose up -d
```

This starts PostgreSQL, RabbitMQ, the delivery service, MySQL and Keycloak.

### 2. Configure Stripe

Set the Stripe API key as an environment variable before starting the backend.

```bash
export STRIPE_API_KEY=your_stripe_key
```

### 3. Start the backend

```bash
cd backend
./gradlew bootRun
```

### 4. Start the frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend will then be available through the local Vite development server.

---

This repository combines the frontend and backend projects into a single repository for easier viewing.
