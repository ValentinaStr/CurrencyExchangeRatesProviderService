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

## ⚙️ Quick Start

1. **Clone the repository**:

   ```bash
   git clone https://github.com/ValentinaStr/CurrencyExchangeRatesProviderService.git
   cd CurrencyExchangeRatesProviderService

2. **Set up PostgreSQL with Docker**:

   Run PostgreSQL and manage the database schema using Docker. This will start PostgreSQL in a container, which will be used for storing exchange rates.

   ```bash
   docker-compose up

3. **Build and run the service**:

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
- **Docker** — for containerizing services
- **Gradle** — for automating project builds
