# SagaLobby
SagaLobby is a Spring Boot backend service for managing player profiles and match records for a game lobby/matchmaking workflow.

The current implementation includes:
- JWT-based request authentication using Supabase JWKS verification.
- Player profile registration for authenticated users.
- Match record creation/join flow for a list of players.
- Liquibase-managed PostgreSQL schema migrations.

## Tech stack
- Java 21
- Spring Boot 3.5.x
- Spring Web, Validation, Security, Actuator
- Spring Data JPA (PostgreSQL)
- Liquibase
- Spring Data Redis + Redisson starter (dependency present for future caching/distributed features)
- Spring Kafka (dependency present; local Kafka compose setup included)
- MapStruct + Lombok
- springdoc OpenAPI UI

## Current module layout
Main source root: `src/main/java/com/example/sagalobby`

- `common/`
  - Shared base entity (`BaseEntity`) with `createdAt`, `updatedAt`, and optimistic locking `version`.
  - Global exception payload model and exception handler.
- `domain/postgres/playerprofile/`
  - `PlayerProfile` entity, region enum, repository, service, and controller.
- `domain/postgres/matchrecord/`
  - `MatchRecord` entity, status enum, repository, service, and controller.
- `mapper/`
  - MapStruct mapper for profile-related DTO/entity conversion.
- `security/`
  - Security config, JWT filter, JWT service, and security utility methods.
- `matchmaking/`
  - Placeholder controller namespace for future matchmaking endpoints.

Resources:
- `src/main/resources/db/changelog/` for Liquibase migrations.

## Data model (Liquibase)
Changelog entrypoint: `src/main/resources/db/changelog/db.changelog-master.yaml`

Current schema includes:
- `player_profile`
  - `id` (UUID, PK)
  - `username` (unique, not null)
  - `mmr_rating` (int, not null)
  - `region` (enum string, not null)
  - `created_at`, `updated_at`, `version`
- `match_record`
  - `id` (UUID, PK)
  - `status` (`IN_PROGRESS`, `COMPLETED`, `FAILED_ROLLBACK`)
  - `created_at`, `updated_at`, `version`
- `player_match_record`
  - Composite key (`match_id`, `player_id`)
  - FK to `match_record.id`
  - FK to `player_profile.id`

## Security model
- Stateless security (`SessionCreationPolicy.STATELESS`).
- `JwtFilter` reads `Authorization: Bearer <token>`.
- Token verification:
  - Reads key ID from JWT.
  - Fetches the public key from configured JWKS URL.
  - Verifies issuer as `${supabase.url}/auth/v1`.
- Extracted claims used to build auth principal:
  - `sub` -> `personId`
  - `user_metadata.name` -> `personName` (fallback: `Unknown`)
  - `user_metadata.role` -> Spring role (fallback: `USER`)

Routes currently configured as public:
- `/api/v1/auth/**`
- `/health`

All other routes require authentication.

## REST API (current)
Base URL (default): `http://localhost:8080`

### 1) Register player profile
- Method: `POST`
- Path: `/api/v1/player-profile/`
- Auth: Required (Bearer token)
- Request body:
```json
{
  "username": "aaryan",
  "region": "ASIA"
}
```
- Notes:
  - User ID is taken from authenticated JWT subject (`sub`) via `SecurityUtils`.
  - If profile already exists for this user ID, service throws `IllegalStateException`.

### 2) Join/create match record
- Method: `POST`
- Path: `/api/v1/match/join`
- Auth: Required (Bearer token)
- Request body:
```json
{
  "players": [
    {
      "id": "11111111-1111-1111-1111-111111111111",
      "region": "ASIA",
      "mmrRating": 1500
    },
    {
      "id": "22222222-2222-2222-2222-222222222222",
      "region": "EUROPE",
      "mmrRating": 1525
    }
  ]
}
```

Validation constraints:
- `players` list size: min `2`, max `10`
- `mmrRating`: `0` to `5000`

Response shape:
```json
{
  "id": "match-uuid",
  "status": "IN_PROGRESS"
}
```

### 3) Matchmaking namespace
- Controller base path exists: `/api/v1/matchmaking`
- No concrete handlers are currently implemented.

## Local development setup
### 1) Prerequisites
- JDK 21
- Docker (optional but recommended for Kafka setup)
- PostgreSQL instance
- Supabase project/JWT setup

### 2) Create local configuration
Create `src/main/resources/application.yaml` (this file is gitignored).

Example baseline:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/sagalobby
    username: {{POSTGRES_USER}}
    password: {{POSTGRES_PASSWORD}}
  jpa:
    hibernate:
      ddl-auto: validate
  liquibase:
    change-log: classpath:db/changelog/db.changelog-master.yaml
  kafka:
    bootstrap-servers: localhost:9092
  data:
    redis:
      host: localhost
      port: 6379

supabase:
  url: https://{{SUPABASE_PROJECT_REF}}.supabase.co
  jwt:
    url: https://{{SUPABASE_PROJECT_REF}}.supabase.co/auth/v1/.well-known/jwks.json
```

### 3) Start optional infrastructure
Kafka is provided via `docker-compose.yml`:
```bash
docker compose up -d kafka
```

Note: PostgreSQL and Redis containers are not defined in the current compose file, so run them separately if needed.

### 4) Run the application
```bash
./mvnw spring-boot:run
```

### 5) Build and test
```bash
./mvnw clean test
./mvnw clean package
```

## Example authenticated requests
Create profile:
```bash
curl -X POST "http://localhost:8080/api/v1/player-profile/" \
  -H "Authorization: Bearer {{SUPABASE_ACCESS_TOKEN}}" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "aaryan",
    "region": "ASIA"
  }'
```

Create/join match:
```bash
curl -X POST "http://localhost:8080/api/v1/match/join" \
  -H "Authorization: Bearer {{SUPABASE_ACCESS_TOKEN}}" \
  -H "Content-Type: application/json" \
  -d '{
    "players": [
      {
        "id": "11111111-1111-1111-1111-111111111111",
        "region": "ASIA",
        "mmrRating": 1400
      },
      {
        "id": "22222222-2222-2222-2222-222222222222",
        "region": "EUROPE",
        "mmrRating": 1450
      }
    ]
  }'
```

## API docs and observability
- OpenAPI JSON (default springdoc): `/v3/api-docs`
- Swagger UI (default springdoc): `/swagger-ui/index.html`
- Spring Boot Actuator dependency is included.

Depending on your security and actuator config, these endpoints may require authentication.

## Known implementation notes
- `ProfileResponseDTO` is currently an empty record; profile registration responses may contain an empty JSON object.
- `MatchmakingController` is scaffolded but does not expose endpoints yet.
- Global exception handler currently provides explicit handling for `JwkException`; additional domain/validation exception mappings can be expanded.
- Security currently permits `/health`, while default Actuator health endpoint is usually `/actuator/health` unless remapped.
