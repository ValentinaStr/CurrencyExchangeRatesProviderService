# CurrencyExchangeRatesProviderService 🚀

## About the Project
**CurrencyExchangeRatesProviderService** is a Spring Boot application designed to provide up-to-date exchange rates for supported currencies. By default, the currency list is empty, but it can be extended via API endpoints. The service integrates with **fixer.io** to fetch and update exchange rates every hour.

### Key features include:
- **Up-to-date exchange rates**: Fetching exchange rates from fixer.io.
- **Periodic updates**: Exchange rates are automatically updated every hour.
- **Database storage**: Exchange rates are stored in a PostgreSQL database for long-term storage.
- **Extendable currency list**: The ability to add new currencies to the system.
- **Testing**: Includes comprehensive unit and integration tests for reliable service operation.

---

## 🎓 Requirements
For local development, make sure you have the following tools installed:
- **Java 21** or higher
- **Gradle** for building the project
- **Docker** (for running PostgreSQL and other necessary services)

---
# CurrencyExchangeRatesProviderService 


## Original requirements

Currency Exchange Rates Provider Service
The Currency Exchange Rates Provider Service aims to deliver up-to-date exchange rates for a list of supported currencies. By default, the currency list is empty but can be extended using existing API endpoints (see API Documentation below). Exchange rates for the supported currencies are updated on service startup and every hour thereafter using the fixer.io client.
 
Quick Start
Prerequisites
Java 21
Gradle
Docker (for running PostgreSQL)
Tips
Add your API key for 'fixer.io' encoded in Base64 format to the service.client.key property.
After adding new currencies, restart the service to fetch all required exchange rates automatically.
 
Original Requirements
Please create a Spring Boot application with Java any version since 11, preferably use Gradle for building the project. Implement REST API where a customer can execute next actions:
get a list of currencies used in the project;
get exchange rates for a currency;
add new currency for getting exchange rates.

## ⚙️ Quick Start

1. **Clone the repository**:

   ```bash
   git clone https://github.com/ValentinaStr/CurrencyExchangeRatesProviderService.git
   cd CurrencyExchangeRatesProviderService

2. **Set up PostgreSQL with Docker**:

   Run PostgreSQL and manage the database schema using Docker. This will start PostgreSQL in a container, which will be used for storing exchange rates.

   ```bash
   docker-compose up

3. **Access the PostgreSQL database via PGAdmin**:

     After running PostgreSQL in Docker, you can use PGAdmin to interact with the database.

   - Open PGAdmin in your browser (https://localhost:9090/browser/). 
   - Log in with the default credentials (username: admin@admin.com, password: admin).
   - Add a new server in PGAdmin:
   - Name: Choose any name (e.g., CurrencyExchangeRates).
   - Host: postgres.
   - Port: 5432 (default PostgreSQL port).
   - Username: myuser
   - Password: mypassword.

     Once connected, you can interact with the database to view, modify, or manage the data being stored by the application.


4. **Build and run the service**:

   To build and run the service, use the following commands:

   ```bash
   ./gradlew build
   ./gradlew bootRun

---

## 🛠️ How It Works

The service integrates with fixer.io to fetch up-to-date exchange rates. These rates are cached in memory for fast access and saved in PostgreSQL for long-term storage. Exchange rates are automatically updated every hour using scheduled tasks in Spring. The service provides flexibility, allowing users to easily add new currencies to the list.

### Database and schema management:
Exchange rates are stored in a PostgreSQL database using Spring Data JPA. The database schema is managed with Liquibase, ensuring consistency and ease of updates.

---

## 🛠️ Tools and Technologies

This project uses the following technologies:
- **Spring Boot** — for configuring and setting up the application
- **Spring Data JPA** — for working with the database
- **Liquibase** — for managing the database schema
- **PostgreSQL** — for data storage
- **PGAdmin** — for managing the PostgreSQL database
- **Docker** — for containerizing services
- **Gradle** — for automating project builds
