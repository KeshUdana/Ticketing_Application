# Ticketing Simulation System

## Overview
This project implements a **real-time ticketing simulation system** following the **Producer-Consumer pattern**. The system demonstrates ticket release by vendors (producers) and ticket purchases by customers (consumers). The application is built using **Spring Boot** (backend), **Next.js 13** (frontend with React 17), and **PostgreSQL** (database).

## Features
- Real-time simulation of ticket release and purchase.
- Graphical representation of thread behavior using line graphs.
- Configuration of simulation parameters through a CLI.
- Persistent storage of ticket sales in PostgreSQL.
- REST API for monitoring producer and consumer threads.

---

## Tech Stack
- **Backend**: Spring Boot (MVC architecture) with Maven for dependency management.
- **Frontend**: Next.js 13, React 17, Tailwind CSS, Chart.js for graphical visualizations.
- **Database**: PostgreSQL for storing ticket sales data.

---

## System Architecture
The project is organized into two main folders:

1. **Backend**:
   - **`Startup` Package**:
     - `SystemConfig`: Configures the simulation parameters.
     - `TicketController`: API endpoint for thread count monitoring.
     - `TicketService`: Intermediary between the backend API and simulation logic.
     - `TicketingCLI`: Core simulation process (generates users, vendors, tickets, producer threads, and consumer threads).
     - `WebConfig`: Manages Spring Boot CORS configuration.
   - **`management/backend` Package**:
     - `model`: Contains entities required for the system.
     - `persistence`:
       - `DatabaseConnection`: Manages database connection.
       - `TicketSales`: Defines table structure and data types for PostgreSQL.
     - `repository`:
       - `TicketRepository`: Interface for database operations.

2. **Frontend**:
   - Built with Next.js 13 and styled using Tailwind CSS.
   - Chart.js is used to display line graphs showing real-time producer and consumer thread behavior.

---

## Setup Instructions

### Prerequisites
1. **PostgreSQL**:
   - Ensure PostgreSQL is installed and running.
   - Use **pgAdmin** to manage the database.
2. **Reactjs**:
   - Install Reactjs (v16+).

### Backend Setup
1. Navigate to the `backend` directory.
2. Ensure Maven is installed.
3. Add the following dependencies in `pom.xml`:
   - Spring Web
   - PostgreSQL Driver:
     ```xml
     <dependency>
         <groupId>org.postgresql</groupId>
         <artifactId>postgresql</artifactId>
         <scope>runtime</scope>
     </dependency>
     ```
   - Google Gson (utility dependency):
     ```xml
     <dependency>
         <groupId>com.google.code.gson</groupId>
         <artifactId>gson</artifactId>
     </dependency>
     ```
4. Start the backend server:
   ```bash
   mvn spring-boot:run
   ```
   - The backend server will run on port `8080`.

### Frontend Setup
1. Navigate to the `frontend` directory.
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```
   - The frontend server should run on port `3000`. If prompted for another port (e.g., 3001), restart and ensure it uses `3000`.

---

## How to Run the Simulation

1. **Database Initialization**:
   - Open **pgAdmin** and ensure the database is set up correctly.

2. **Start Backend Server**:
   - Run the backend server first to enable the frontend connection.

3. **Launch Frontend**:
   - Navigate to the frontend folder and start the frontend server.
   - Open your browser and go to `http://localhost:3000` to view real-time graphs.

4. **Run CLI Simulation**:
   - In the backend, the CLI will prompt you to configure the simulation.
     - Parameters include ticket count, release rate, retrieval rate, etc.
     - The configuration is saved in a `config.json` file.
   - Start the simulation when prompted.

5. **Observe Simulation**:
   - The CLI logs ticket sales activity.
   - The frontend displays real-time producer and consumer thread behavior.

6. **Stop and Verify Data**:
   - Stop the system via the CLI.
   - Refresh **pgAdmin** to view updated ticket sales data in the database.

---

## API Documentation
### Base URL
```
http://localhost:8080/events
```

#### **Get Thread Counts**
- **Endpoint**: `/counts`
- **Method**: `GET`
- **Response Example**:
  ```json
  {
    "ProducerCount": 5,
    "ConsumerCount": 10
  }
  ```

---

## Testing
- Use **Postman** or any HTTP client to test the API.
- Validate producer and consumer counts by sending `GET` requests to `/events/counts`.

---

## Notes
- The backend must be running before starting the frontend.
- Ensure consistent port usage (`8080` for backend and `3000` for frontend).
- Data persistence is maintained in PostgreSQL.

---

## Future Enhancements
- Add role-based authentication.
- Enhance visualization with more graph types.
- Implement advanced metrics like ticketing throughput and failure rates.

---

## License
This project is open-source and available under the [MIT License](LICENSE).

