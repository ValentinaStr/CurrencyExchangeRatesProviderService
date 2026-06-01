# CurrencyExchangeRatesProviderService

## About the Project

**CurrencyExchangeRatesProviderService** is a Spring Boot application that provides up-to-date exchange rates for supported currencies.

Exchange rates are fetched from **two external providers**:
- **Fixer.io** — `https://data.fixer.io/api` (free plan: EUR base only)
- **ExchangeRatesAPI.io** — `https://api.exchangeratesapi.io/v1/`

On startup and every hour thereafter, the service fetches rates from both providers, picks the best available rates, stores them in the **PostgreSQL database**, and updates the **in-memory cache**. API responses are served from the cache.

---

## Requirements

- **Java 21**
- **Gradle**
- **Docker** or **Podman** (for PostgreSQL)

---

## Quick Start

### 1. Clone the repository

```bash
git clone https://github.com/ValentinaStr/CurrencyExchangeRatesProviderService.git
cd CurrencyExchangeRatesProviderService
```

### 2. Start PostgreSQL

**Docker:**
```bash
docker-compose up
```

**Podman (rootless, Windows):**
```bash
podman run -d --name postgres-container \
  -e POSTGRES_USER=myuser \
  -e POSTGRES_PASSWORD=mypassword \
  -e POSTGRES_DB=databaseforcurrencies \
  -p 5433:5432 postgres:15-alpine
```

> On Windows, rootless Podman maps ports to IPv6 only. Use `jdbc:postgresql://[::1]:5433/databaseforcurrencies` as the JDBC URL.

### 3. Access the PostgreSQL database via PGAdmin

After starting PostgreSQL with `docker-compose`, PGAdmin is available at `http://localhost:9090`.

- Login: `admin@admin.com` / `admin`
- Add a new server:
  - **Host**: `postgres`
  - **Port**: `5432`
  - **Username**: `myuser`
  - **Password**: `mypassword`

### 4. Run the application

```bash
./gradlew bootRun
```

### 5. Run tests

```bash
./gradlew clean test
```

---

## API Endpoints

All endpoints require HTTP Basic authentication.

| Method | URL | Role | Description |
|--------|-----|------|-------------|
| GET | `/api/v1/currencies` | USER, ADMIN | List all supported currencies |
| POST | `/api/v1/currencies` | ADMIN only | Add a new currency |
| GET | `/api/v1/exchange-rates?currency={code}` | USER, ADMIN | Get exchange rates for a currency |

### Add currency — request body

```json
{ "currency": "USD" }
```

Currency code must be exactly 3 uppercase letters. Returns `201 Created` on success.

### Swagger UI

```
http://localhost:8080/swagger-ui.html
```

---

## Security

Spring Security is configured with HTTP Basic authentication. Users and roles are stored in the database and created via Liquibase migrations.

| User | Password | Role |
|------|----------|------|
| `user` | `user123` | USER |
| `admin` | `admin123` | ADMIN |

- `POST /api/v1/currencies` — restricted to ADMIN
- All other endpoints — any authenticated user
- Access denied responses return `404 Not Found` (role is not exposed)

---

## Database

PostgreSQL with schema managed by **Liquibase**. Spring Data JPA is used for data access.

---

## Test Coverage (JaCoCo)

JaCoCo is integrated into the Gradle build and enforces a minimum of **95% instruction coverage**. After running tests, the HTML report is available at:

```
build/reports/jacoco/test/html/index.html
```

---

## Technologies

| Technology | Purpose |
|------------|---------|
| Spring Boot | Application framework |
| Spring Data JPA | Database access |
| Spring Security | Authentication and authorization |
| Liquibase | Database schema management |
| PostgreSQL | Data storage |
| Gradle | Build automation |
| JaCoCo | Test coverage |
| Checkstyle | Code style enforcement |
| Swagger / OpenAPI | API documentation |
| WireMock | Integration test mocking |
| JUnit 5 + Mockito | Unit and integration testing |
| Testcontainers | Integration tests with real PostgreSQL |
