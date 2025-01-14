# CurrencyExchangeRatesProviderService 

## Original requirements 

Currency Exchange Rates Provider Service The Currency Exchange Rates Provider Service aims to deliver up-to-date exchange rates for a list of supported currencies. By default, the currency list is empty but can be extended using existing API endpoints (see API Documentation below). Exchange rates for the supported currencies are updated on service startup and every hour thereafter using the fixer.io client.

Quick Start Prerequisites Java 21 Gradle Docker (for running PostgreSQL) Tips Add your API key for 'fixer.io' encoded in Base64 format to the service.client.key property. After adding new currencies, restart the service to fetch all required exchange rates automatically.

Original Requirements Please create a Spring Boot application with Java any version since 11, preferably use Gradle for building the project. Implement REST API where a customer can execute next actions: get a list of currencies used in the project; get exchange rates for a currency; add new currency for getting exchange rates.

Requirements to the Test task: initially a currency list is empty; you should receive exchange rates from external public sources implementing integration; it can be any public available source, e.g. fixer.io, exchangeratesapi.io, openexchangerates.org, currencylayer.com, etc.; implement the integration with at least 2 of them; receiving of exchange rates should be scheduled (e.g. every hour); they should be logged in the database and stored in memory Map; API gets data from the Map; for a database use PostgreSQL, manage DB schema with Liquibase (any format - xml, sql, yaml, json); data management should be provided with Spring Data JPA or Spring Data JDBC; an application can be started in the native environment (without containers); PostgreSQL and any other possible services should be started in Docker containers; Docker containers should be described with docker-compose file; mostly models, controllers and services should be covered by unit and functional tests, preferably JUnit 5 and Spring Test Framework; use WireMocks to validate requests to the endpoints in integration tests; the project should have API documentation (preferably Swagger/OpenAPI Specification - dynamic or static); implement exception handling logic using @RestControllerAdvice; handle them appropriately to return corresponding error JSON with the correct status code; cover expected exception scenarios in Integration Tests; add validation annotations (e.g., @NotEmpty) and cover controller validation with tests using @WebMvcTest; configure linters and static code analyzers (e.g., Jacoco, CheckStyle); add Spring Security to the project; There should be a login page, users and roles; Users with ADMIN role should have access to POST http://localhost:8080/api/v1/currencies?currency=USD endpoint, users with USER role should not; Users and roles should be stored in the database with encrypted passwords; one user can have many roles; Comments: if you have any difficulties with currency switching in a chosen public exchange source, please use a single currency passing off it as another;

## About the Project
**CurrencyExchangeRatesProviderService** is a Spring Boot application designed to provide up-to-date exchange rates for supported currencies.

---

## 🎓 Requirements
For local development, make sure you have the following tools installed:
- **Java 21** or higher
- **Gradle** for building the project
- **Docker** (for running PostgreSQL and PGAdmin)

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

## 🛠️ Tools and Technologies

This project uses the following technologies:
- **Spring Boot** — for configuring and setting up the application
- **Spring Data JPA** — for working with the database
- **Liquibase** — for managing the database schema
- **PostgreSQL** — for data storage
- **PGAdmin** — for managing the PostgreSQL database
- **Docker** — for containerizing services
- **Gradle** — for automating project builds
