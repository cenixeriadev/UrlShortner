FROM amazoncorretto:21

# Create a directory for the app
WORKDIR app/

# Copy the jar into the container
COPY target/*.jar app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]

