FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /workspace/app

# Copy maven executable and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Build dependencies separately (for better Docker caching)
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B

# Copy and build the source code
COPY src src
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Create the production image
FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp

# Copy application dependencies
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Set the working directory and entrypoint
WORKDIR /app
ENTRYPOINT ["java", "-cp", ".:/app/lib/*", "com.lookbook.LookbookApplication"]

# Expose the application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s \
    CMD wget -q --spider http://localhost:8080/api/actuator/health || exit 1 