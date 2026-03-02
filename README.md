# Epigram Web App

A full-stack web application for creating and viewing epigrams.

**Stack**
- Frontend: Svelte
- Backend: Spring Boot
- Database: PostgreSQL

## Requirements
The application can run without Docker, but Docker is strongly recommended for setup simplicity
- [Docker](https://www.docker.com/get-started) installed and running

## Starting the Application (Docker)
Note: The no-docker startup procedure can be found at the end of this file.

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

## Features
This simple epigram app has a few features of note: 
* User registration: register an account by pressing the icon in the top right, entering valid credentials, and pressing register. Registering an account will immediately log you in
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


## Starting the Application (No Docker)
### Set up database
* Ensure postgreSQL is installed
* Run:
```bash
psql -U postgres
```
* Then:
```postgresql
CREATE USER epigram WITH PASSWORD 'epigram';
CREATE DATABASE epigram OWNER epigram;
GRANT ALL PRIVILEGES ON DATABASE epigram TO epigram;
```
* And exit with
```postgresql
\q
```

Now the database is set up, and the backend can be started

### Backend

```bash
cd backend

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/epigram \
SPRING_DATASOURCE_USERNAME=epigram \
SPRING_DATASOURCE_PASSWORD=epigram \
JWT_SECRET=super-secret-secretive-secret-for-dev-purposes-only-for-demo-purposes-only \
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
