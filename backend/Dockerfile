# 1. Use an OpenJDK base image for the build stage.
# We choose a JDK version that is compatible with your Spring Boot project (e.g., 17 or 21).
# Using 'slim-bullseye' minimizes the image size.
FROM openjdk:17-jdk-slim as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the mvnw, pom.xml, and src directory to the container
# This copies necessary files for the build process.
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Run the build command to compile the code and package it into a JAR file.
# We use -DskipTests to speed up the build, assuming tests passed locally.
# The resulting JAR will be in the 'target' directory.
RUN ./mvnw package -DskipTests

# --- Second Stage: Final Production Image ---
# 2. Use a smaller JRE-only base image for the final production image.
# This significantly reduces the final image size and attack surface.
FROM openjdk:17-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the 'builder' stage into the final image
# The JAR is typically named 'target/your-app-name-version.jar'.
# You may need to adjust the actual name based on your pom.xml <artifactId> and <version>.
# **IMPORTANT: REPLACE 'your-app-name-0.0.1-SNAPSHOT.jar' with your actual JAR file name.**
COPY --from=builder /app/target/your-app-name-0.0.1-SNAPSHOT.jar app.jar

# Expose the port that Spring Boot listens on (default is 8080)
EXPOSE 8080

# Define the entry point to run the application
# 'app.jar' is the file we copied into the working directory.
ENTRYPOINT ["java", "-jar", "app.jar"]