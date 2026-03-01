

# Build
FROM maven:3.9.4-eclipse-temurin-21 AS build

WORKDIR /app

# Copy only dependency descriptor first for caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build your Spring Boot app JAR
RUN mvn clean package -DskipTests -B

# Stage 2: run the JAR with a smaller JDK image
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]