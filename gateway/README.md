# Gateway Run Guide

## Quick start

From the repository root:

1. Start local infrastructure:

```bash
docker compose -f docker/docker-compose.yml up -d
```

2. Run the backend (required by gateway routes) on `http://localhost:8080`.

3. Run the gateway:

```bash
cd gateway
./mvnw spring-boot:run
```

The gateway starts on `http://localhost:8082` (dev profile by default).

