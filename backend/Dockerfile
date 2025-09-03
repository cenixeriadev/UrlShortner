FROM amazoncorretto:17

# Create a directory for the app
WORKDIR app/

# Copy the jar into the container
COPY target/*.jar app.jar

COPY .env .env

# Set JVM options for better performance and to prevent StackOverflowError
ENV JAVA_OPTS="-Xmx2g -Xms1g -Xss1m -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -XX:+UnlockExperimentalVMOptions -XX:+UseContainerSupport"

# Run the jar with optimized JVM settings
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

