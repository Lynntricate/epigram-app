# Epigram Web App

A full-stack web application for creating and viewing epigrams.

Frontend: Svelte
Backend: Spring Boot
Database: PostgreSQL

## Requirements
- [Docker](https://www.docker.com/get-started) installed and running

## Starting the Application
Frontend, backend and database are containerized with Docker. To start the app in one command, run the following from the repository root:

```bash
docker compose up --build 
```

## Stopping the application
```bash
docker compose down
```

## Running tests
Only tests or the backend are implemented. Run tests with:
```bash
./mvnw test
```


## Demo Disclaimer
* This demo runs on HTTP for simplicity, but this should never be used in a production environment
* Before deploying in production, please adjust the demo JWT_SECRET specified in docker-compose.yml
* Before deploying in production, please adjust the demo DB_PASSWORD specified in docker-compose.yml
