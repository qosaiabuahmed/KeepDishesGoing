# Keep Dishes Going (KDG)

A food ordering platform built with Domain-Driven Design, Hexagonal Architecture, and Event-Driven Communication.

---

## Challenges & Accomplishments

### The most significant challenges I encountered during development:

The biggest headache was definitely RabbitMQ. Getting the exchanges, routing keys, and bindings to work properly between the delivery service took way more time than expected. The versioned routing key pattern (`restaurant.*.order.accepted.v1`) made sense on paper but debugging why messages weren't arriving was painful.

Stripe integration was also tricky. Figuring out how to properly structure the payment calls and handle the responses took some experimentation.

The project itself was a bit too much for only 6 weeks. It was a nice challenge though, and I hope I implemented everything correctly. Would've loved more time to polish things and implement the missing pieces.



### The aspects of this project I am most proud of:

I'm proud of getting the architecture right - implementing hexagonal architecture with proper bounded contexts and keeping the domain logic separated from infrastructure code.

Getting all the projections working from both sides was challenging but it works. Keeping the restaurant and order contexts in sync through events required careful planning.

Tackling things I'd never worked with before like RabbitMQ and payment integration was tough but rewarding. Learning these technologies while building the project pushed me out of my comfort zone.

Most of all, I'm proud that I managed to implement most of the functionality in only 6 weeks. Getting 29 out of 32 user stories done while learning all these new patterns and technologies feels like a solid achievement.

---

## ✅ Finished Features

All features were successfully implemented and tested:

- ✅ UC 1: Owner sign up/sign in
- ✅ UC 2: Create restaurant with all details
- ✅ UC 3: Edit dish as draft without affecting live menu
- ✅ UC 4: Publish dish to make it available
- ✅ UC 5: Unpublish dish to remove it from customers
- ✅ UC 6: Apply all pending dish changes at once
- ✅ UC 7: Schedule publications to go live at chosen time
- ✅ UC 8: Mark dish out of stock or back in stock immediately
- ✅ UC 9: Set opening hours and manual open/close restaurant
- ✅ UC 10: Accept or reject orders with reason
- ✅ UC 11: Auto-decline orders after 5 minutes
- ✅ UC 12: Mark accepted orders as ready for pickup
- ✅ UC 13: Customer landing page
- ✅ UC 14: Explore restaurants in list view
- ✅ UC 15: View restaurant details and dishes
- ✅ UC 16: Filter restaurants by cuisine type and price range (partial - no distance/delivery time)
- ✅ UC 17: Filter dishes by type and food tags
- ✅ UC 18: Sort dishes by price
- ✅ UC 20: Build basket from single restaurant
- ✅ UC 21: Basket validation blocks checkout if dish unavailable
- ✅ UC 22: Provide delivery info at checkout
- ✅ UC 23: Payment integration (Stripe)
- ✅ UC 24: Order confirmation with tracking link
- ✅ UC 25: Track order progress with status updates
- ✅ UC 26: View price range evolution over time
- ✅ UC 27: Maximum 10 visible dishes enforced
- ✅ UC 28: Publish messages to delivery service (order accepted, ready)
- ✅ UC 29: Consume delivery service messages (picked up, delivered, location)
- ✅ UC 30: Customers order without sign-up
- ✅ UC 31: Each owner manages one restaurant
- ✅ UC 32: Adjust price range criteria

---

## ❌ Unfinished / Planned Features

Features not implemented:

- ❌ UC 14: Explore restaurants on map view
- ❌ UC 16: Filter by distance (partial - cuisine and price work, but not distance)
- ❌ UC 19: Guesstimated delivery time calculation

---


**Prerequisites:**
- Java 21, Spring Boot 3.x
- PostgreSQL
- RabbitMQ
- Keycloak

---

## Getting Started

**Start infrastructure:**
```bash
cd infrastructure
docker-compose up -d
```

**Run application:**
```bash
./gradlew bootRun
```

**Access:**
- API: http://localhost:8080
- RabbitMQ Management: http://localhost:15672 (User/password)
- Keycloak: http://localhost:8083 (admin/admin)