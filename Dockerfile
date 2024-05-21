# Use official OpenJDK as base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the jar file into the container
COPY target/task-0.0.1-SNAPSHOT.jar app.jar

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
