# 🚀 Launch Pad: Centralized Configuration Management

**Launch Pad** is a highly resilient, centralized configuration management system designed to act as the source of truth for dynamic JSON configurations across high-throughput microservices.

It allows engineering teams to safely create, validate, and distribute configurations without requiring code deployments, while maintaining strict audit trails and deployment safety through payload diffing.

---

## 🛠️ Architecture & Tech Stack
* **Language:** Java 21
* **Framework:** Spring Boot 3.x
* **Database:** PostgreSQL 16 (Native `JSONB` via Hibernate 6)
* **Caching:** Redis 7
* **Validation & Diffing:** `zjsonpatch` (RFC 6902), `networknt/json-schema-validator`
* **Architecture:** Hexagonal / Clean Architecture

---

## 📂 1. Directory Structure

```text
launchpad-backend/
├── docker-compose.yml
├── pom.xml
├── src/main/resources/
│   └── application.yml
├── src/main/java/com/launchpad/config/
│   ├── LaunchPadApplication.java
│   ├── domain/
│   │   ├── exception/
│   │   │   ├── LaunchpadException.java
│   │   │   ├── ConfigNotFoundException.java
│   │   │   ├── InvalidConfigPayloadException.java
│   │   ├── model/
│   │   │   ├── ConfigMaster.java
│   │   │   ├── ConfigHistory.java
│   │   ├── port/
│   │   │   ├── ConfigRepository.java
│   │   │   ├── ConfigHistoryRepository.java
│   ├── application/
│   │   ├── event/
│   │   │   ├── ConfigUpdatedEvent.java
│   │   │   ├── ConfigAuditListener.java
│   │   ├── service/
│   │   │   ├── ConfigLifecycleService.java
│   │   ├── validation/
│   │   │   ├── SchemaRegistry.java
│   │   │   ├── JsonValidatorStrategy.java
│   │   │   ├── JsonFormatValidator.java
│   ├── infrastructure/
│   │   ├── cache/
│   │   │   ├── RedisConfig.java
│   │   ├── persistence/
│   │   │   ├── JpaConfigRepository.java
│   │   │   ├── JpaConfigHistoryRepository.java
│   │   │   ├── ConfigPersistenceAdapter.java
│   │   │   ├── ConfigHistoryPersistenceAdapter.java
│   ├── presentation/
│   │   ├── controller/
│   │   │   ├── ConfigController.java
│   │   ├── dto/
│   │   │   ├── ConfigRequest.java
│   │   │   ├── ConfigResponse.java
│   │   ├── handler/
│   │   │   ├── GlobalExceptionHandler.java
```

🚀 Local Development Setup
Prerequisites
Java 21+ installed

Maven or Gradle

Docker & Docker Compose

1. Start Infrastructure
Launch Pad relies on PostgreSQL and Redis. A docker-compose.yml is provided to spin these up with persistent data volumes.

Bash
# Start Postgres and Redis in the background
docker-compose up -d
Note: The database is exposed on localhost:5432 with the username launchpad and password launchpad.

2. Run the Application
Start the Spring Boot application using your IDE or via the command line:

Bash
./mvnw spring-boot:run
The API will be available at http://localhost:8080.

📖 API Reference
Authentication is currently simulated via the X-User-Id header for audit attribution.

1. Create or Update a Configuration
PUT /api/v1/configs/{configKey}

```Bash
curl -X PUT http://localhost:8080/api/v1/configs/feature.video_playback.cmcd \
  -H "Content-Type: application/json" \
  -H "X-User-Id: dev_avijeet" \
  -d '{
    "payload": {
      "cmcd_enabled": true,
      "buffer_length_ms": 5000
    },
    "apiEndpoint": "/api/v1/player/config",
    "status": "ENABLED"
  }'
  ```

2. Generate Diff (Dry Run)
Test a payload against the current database state to see what will change.
POST /api/v1/configs/{configKey}/diff

```Bash
curl -X POST http://localhost:8080/api/v1/configs/feature.video_playback.cmcd/diff \
  -H "Content-Type: application/json" \
  -d '{
    "cmcd_enabled": true,
    "buffer_length_ms": 8000
  }'
```

3. Toggle Configuration Status
Safely enable or disable a configuration feature flag.
PATCH /api/v1/configs/{configKey}/status?status={ENABLED|DISABLED}

```Bash
curl -X PATCH "http://localhost:8080/api/v1/configs/feature.video_playback.cmcd/status?status=DISABLED" \
  -H "X-User-Id: dev_avijeet"
```

4. View Audit History
Retrieve the 3 most recent modifications (including the JSON diffs and the author).
GET /api/v1/configs/{configKey}/history

```Bash
curl -X GET http://localhost:8080/api/v1/configs/feature.video_playback.cmcd/history
5. Fetch a Configuration
GET /api/v1/configs/{configKey}

Bash
curl -X GET http://localhost:8080/api/v1/configs/feature.video_playback.cmcd
```

🛡️ Error Handling
The API implements RFC 7807 Problem Details for HTTP APIs. If you send an invalid payload or request a missing config, you will receive a structured JSON response:

```JSON
{
  "type": "[https://launchpad.internal/errors/config-not-found](https://launchpad.internal/errors/config-not-found)",
  "title": "Configuration Not Found",
  "status": 404,
  "detail": "Configuration not found for key: feature.unknown",
  "instance": "/api/v1/configs/feature.unknown"
}
```