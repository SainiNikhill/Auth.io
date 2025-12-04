# 1. Use the Eclipse Temurin JDK for the build stage.
FROM eclipse-temurin:17-jdk-jammy as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the mvnw, pom.xml, and src directory to the container
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# NEW LINE ADDED HERE: Grant execute permission to the mvnw script
RUN chmod +x mvnw

# Run the build command
RUN ./mvnw package -DskipTests
# ... (rest of your Dockerfile)