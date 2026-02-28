# Backend Local Run Guide

This service is a Spring Boot application (Java 25).


## Exact `.env` Content for Docker run

Create `backend/.env` with:

```env
DEEPL_AUTH_KEY=your_real_deepl_key
SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/click_to_translate
SPRING_DATASOURCE_USERNAME=click_to_translate
SPRING_DATASOURCE_PASSWORD=click_to_translate
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://localhost:8081/realms/click-to-translate
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_JWK_SET_URI=http://host.docker.internal:8081/realms/click-to-translate/protocol/openid-connect/certs
```

## Exact `.env` Content for local run

Create `backend/.env` with:

```env
DEEPL_AUTH_KEY=your_real_deepl_key
```

## Run Prod Locally in Docker

From `backend/`:

1. Build image

```powershell
docker build -t click-to-translate:backend .
```

2. Start dependencies (Postgres + Keycloak)

```powershell
docker compose -f docker/docker-compose.yml up -d
```

3. Run app with `prod` profile and env file

```powershell
docker run --rm -p 8080:8080 `
  --env-file .env `
  -e SPRING_PROFILES_ACTIVE=prod `
  click-to-translate:backend
```

## Keycloak Redirect URIs for Extension Login
If the browser extension ID changes, Keycloak OAuth redirects must be updated.

For client `click-to-translate-extension`, set:
- Valid Redirect URI: `https://<EXTENSION_ID>.chromiumapp.org/oauth2`
- Web Origin: `https://<EXTENSION_ID>.chromiumapp.org`

If you use the realm import file, update:
- `backend/docker/keycloak/realm-import/click-to-translate-realm.json`

## Run Dev from Command Line (Compose for Dependencies Only)

Use Spring config import to load `.env` directly (works on macOS/Linux/Windows).

1. Start dependencies

```powershell
docker compose -f docker/docker-compose.yml up -d
```

2. Run app

```powershell
mvnw spring-boot:run "-Dspring-boot.run.profiles=dev" "-Dspring-boot.run.arguments=--spring.config.import=optional:file:.env[.properties]"
```
