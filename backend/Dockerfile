# 1. Use the Eclipse Temurin JDK for the build stage.
# 'jammy' refers to Ubuntu 22.04, a common and stable base OS.
FROM eclipse-temurin:17-jdk-jammy as builder

# Set the working directory inside the container
WORKDIR /app

# Copy the mvnw, pom.xml, and src directory to the container
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Run the build command
RUN ./mvnw package -DskipTests

# --- Second Stage: Final Production Image ---
# 2. Use the Eclipse Temurin JRE-only image for the final production image.
FROM eclipse-temurin:17-jre-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the 'builder' stage
# **IMPORTANT: REPLACE 'your-app-name-0.0.1-SNAPSHOT.jar' with your actual JAR file name.**
COPY --from=builder /app/target/your-app-name-0.0.1-SNAPSHOT.jar app.jar

# Expose the port (default is 8080)
EXPOSE 8080

# Define the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]