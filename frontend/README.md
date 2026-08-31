# Keep Dishes Going (KDG) - Frontend

A food ordering platform built with React, TypeScript, and Domain-Driven Design principles.

---

## Challenges & Accomplishments

### The most significant challenges I encountered during development:

The biggest challenge was learning how to code with React correctly. Coming from a different background, understanding React's component lifecycle, hooks, and state management patterns took significant effort. Figuring out when to use useState vs useEffect, how to properly structure components, and avoiding common pitfalls was a steep learning curve.

Getting the correct project structure was another major hurdle. Deciding how to organize components, services, contexts, and utilities in a way that made sense and was maintainable required a lot of research and refactoring along the way.

The timeframe was extremely tight. Six weeks to learn React, TypeScript, and build a full-featured food ordering platform while integrating with Keycloak, Stripe, and the backend API felt overwhelming at times. Balancing learning with actual implementation was constantly challenging.

### The aspects of this project I am most proud of:

I'm really proud of how the application looks. The UI came together nicely, and despite being my first major React project, it has a clean, professional appearance that I'm happy to show off.

I'm proud that I managed to complete the project and create a nice flow throughout the application. The customer journey from browsing restaurants to placing orders feels smooth, and the owner dashboard for managing restaurants and orders is intuitive.

Most of all, I'm proud that even with all the challenges - learning React from scratch, tight deadlines, complex integrations - I pushed through and delivered a functional, feature-rich application. Getting 29 out of 32 user stories done while learning an entirely new framework feels like a real accomplishment.

---

## ✅ Finished Features

All features were successfully implemented and tested:

- ✅ UC 1: Owner sign up/sign in(Partial - no sign-up)
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

## Tech Stack

**Frontend:**
- React 19
- TypeScript
- Vite
- React Router
- TanStack Query (React Query)
- Keycloak for authentication
- Axios for API calls
- Recharts for data visualization

**Prerequisites:**
- Node.js 18+
- npm or yarn
- Backend API running on http://localhost:8080
- Keycloak running on http://localhost:8083

---

## Getting Started

**Install dependencies:**
```bash
npm install
```

**Configure environment:**
Create a `.env` file in the root directory (see `.env.example` if available)

**Run development server:**
```bash
npm run dev
```

**Build for production:**
```bash
npm run build
```

**Preview production build:**
```bash
npm run preview
```

**Access:**
- Application: http://localhost:5173 (default Vite port)
- Ensure backend is running on http://localhost:8080
- Ensure Keycloak is running on http://localhost:8083

---

## Project Structure

```
src/
├── components/     # Reusable UI components
├── pages/          # Page components
├── services/       # API services and integrations
├── hooks/          # Custom React hooks
├── contexts/       # React contexts
├── types/          # TypeScript type definitions
└── utils/          # Utility functions
```