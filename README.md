# Epigram Web App

A full-stack web application for creating and viewing epigrams.

**Stack**
- Frontend: Svelte
- Backend: Spring Boot
- Database: PostgreSQL

## Requirements
The application can run without Docker, but Docker is strongly recommended for the setup simplicity
- [Docker](https://www.docker.com/get-started) installed and running

## Starting the Application (Docker)
Frontend, backend and database are containerized with Docker. To start the app in one command, run the following from the repository root:

```bash
docker compose up --build 
```

The frontend can now be reached at http://localhost:5173/

The backend can now be reached at http://localhost:8080/

## Stopping the application (Docker)
```bash
docker compose down
```

## Starting the Application (No Docker)
### Backend
* Ensure postgreSQL is installed
* Export environment variables SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, and SPRING_DATASOURCE_PASSWORD (see src/main/resources/application.properties for usage, and docker-compose.yml for example values)
* Export environment variable JWT_SECRET, value should be a string of at least 256 bits (length: >= 32)
* Start backend with:
```bash
cd backend
./mvnw spring-boot:run
```

The backend can now be reached at http://localhost:8080/

### Frontend
* Ensure npm is installed
* Start frontend with:
```bash
cd frontend
npm install
npm run dev
```

The frontend can now be reached at http://localhost:5173/


## Features
This simple epigram app has a few features of note: 
* User registration: register an account by pressing the icon in the top right. Registering an account will immediately log you in
* User authentication with password and username: if you already registered an account in the past, click the icon in the top right and log in
* Epigram posting: to post an epigram, you must be logged into an account first. You can post an epigram as yourself (which will list your username as author), or note a different author
* Random epigram display: Even without loggin in, the application will show a random epigram every few seconds. The refresh time can be adjusted with a dropdown, and can also be set to "never", meaning the epigram does not refresh at all. 

## Demo Disclaimer
* This demo runs on HTTP for simplicity, but this should never be used in a production environment
* Before deploying in production, please adjust the demo JWT_SECRET specified in docker-compose.yml
* Before deploying in production, please adjust the demo SPRING_DATASOURCE_PASSWORD specified in docker-compose.yml

## Running tests
Only backend tests are implemented (service, controller, and authentication flow).
```bash
cd backend
./mvnw test
```

## Notes for Reviewers
This project was built as a short technical assignment.
My goals were to keep a clean architecture, implement an authentication flow, and to ensure easy deployability
