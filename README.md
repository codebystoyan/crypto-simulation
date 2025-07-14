# Crypto trading simulation platform

A crypto trading simulation platform. Users can track real-time crypto prices, simulate buy/sell trades, monitor holdings, view transaction history, and manage their account balance.

---
## Features

-  Real-time crypto prices (via Kraken WebSocket API)
-  Simulated trading: Buy/Sell cryptocurrencies with virtual balance
-  Track your current holdings
-  View full transaction history
-  Reset user balance
-  Fully componentized and hook-driven architecture

---
##  Tech Stack

### Frontend
- React (Hooks, Functional Components)
- Kraken WebSocket API (for live prices)

### Backend
- Java 21
- Spring Boot (REST API)
- MySQL
- JDBC

---
##  Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/crypto-simulation.git
cd crypto-simulation
```

### 2. Set up a MySQL DB

You can use the `create-database.sql` script to create a DB

### 3. Inside /backend

Configure the `application.properties` file to connect to the DB
```
spring.datasource.url=jdbc:mysql:{DB url and port}/crypto_trading
spring.datasource.username={your username}
spring.datasource.password={your password}
```
Then

```
mvn clean install
mvn spring-boot:run
```

### 4. Inside /frontend

Run `npm install` and then `npm run dev`, since project was configured with Vite

##  Screenshots

Screenshots are located in `/assets/screenshots` folder