# Deployment and Configuration Guide

This document describes how to deploy and configure the Lookbook API for various environments.

## Configuration Properties

The application uses Spring Boot's configuration system with profiles for different environments.

### Common Properties

These properties are defined in `application.properties` and apply to all environments:

```properties
# Server Configuration
server.port=8080
server.servlet.context-path=/api/v1

# Jackson Configuration
spring.jackson.property-naming-strategy=SNAKE_CASE
spring.jackson.date-format=yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
spring.jackson.time-zone=UTC
spring.jackson.serialization.write-dates-as-timestamps=false

# Security Configuration
application.security.jwt.secret-key=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
application.security.jwt.expiration=86400000
application.security.jwt.refresh-token.expiration=604800000

# Logging
logging.level.root=INFO
logging.level.com.lookbook=DEBUG
logging.level.org.springframework.web=INFO
logging.level.org.hibernate=ERROR
```

### Environment-Specific Properties

#### Development Environment (application-dev.properties)

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/lookbook_dev
spring.datasource.username=postgres
spring.datasource.password=postgres

# JPA Configuration
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration,classpath:db/testdata

# Logging
logging.level.com.lookbook=DEBUG
logging.level.org.springframework.web=DEBUG
```

#### Test Environment (application-test.properties)

```properties
# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA Configuration
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration,classpath:db/testdata
```

#### Production Environment (application-prod.properties)

```properties
# Database Configuration
spring.datasource.url=${JDBC_DATABASE_URL}
spring.datasource.username=${JDBC_DATABASE_USERNAME}
spring.datasource.password=${JDBC_DATABASE_PASSWORD}

# JPA Configuration
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration

# Logging
logging.level.root=WARN
logging.level.com.lookbook=INFO
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %logger{36} - %msg%n
```

## Environment Variables

In production, the following environment variables should be set:

| Variable | Description | Example |
|----------|-------------|---------|
| `JDBC_DATABASE_URL` | JDBC URL for database connection | jdbc:postgresql://hostname:5432/lookbook_prod |
| `JDBC_DATABASE_USERNAME` | Database username | db_user |
| `JDBC_DATABASE_PASSWORD` | Database password | secure_password |
| `JWT_SECRET_KEY` | Secret key for JWT token signing | (64+ character random string) |
| `ACTIVE_PROFILE` | Spring active profile | prod |
| `SERVER_PORT` | Application server port | 8080 |

## Starting the Application

### Development

To start the application in development mode:

```bash
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

### Production

To start the application in production mode:

```bash
java -jar lookbook-api.jar --spring.profiles.active=prod
```

## Deployment Options

### Docker Deployment

A Dockerfile is provided to build a Docker image:

```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/lookbook-api.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run the Docker image:

```bash
# Build the Docker image
docker build -t lookbook-api .

# Run the Docker container
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JDBC_DATABASE_URL=jdbc:postgresql://db-host:5432/lookbook_prod \
  -e JDBC_DATABASE_USERNAME=username \
  -e JDBC_DATABASE_PASSWORD=password \
  -e JWT_SECRET_KEY=your_secret_key \
  lookbook-api
```

### Docker Compose Deployment

A `docker-compose.yml` file is provided for local deployment:

```yaml
version: '3.8'

services:
  app:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - JDBC_DATABASE_URL=jdbc:postgresql://db:5432/lookbook_prod
      - JDBC_DATABASE_USERNAME=postgres
      - JDBC_DATABASE_PASSWORD=postgres
      - JWT_SECRET_KEY=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
    depends_on:
      - db
    restart: always
    
  db:
    image: postgres:15
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=postgres
      - POSTGRES_DB=lookbook_prod
    volumes:
      - postgres_data:/var/lib/postgresql/data
    restart: always

volumes:
  postgres_data:
```

Run with Docker Compose:

```bash
docker-compose up -d
```

### Kubernetes Deployment

For Kubernetes deployment, use the provided Kubernetes manifests:

```yaml
# k8s/deployment.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: lookbook-api
  labels:
    app: lookbook-api
spec:
  replicas: 2
  selector:
    matchLabels:
      app: lookbook-api
  template:
    metadata:
      labels:
        app: lookbook-api
    spec:
      containers:
      - name: lookbook-api
        image: lookbook-api:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        - name: JDBC_DATABASE_URL
          valueFrom:
            secretKeyRef:
              name: lookbook-api-secrets
              key: database-url
        - name: JDBC_DATABASE_USERNAME
          valueFrom:
            secretKeyRef:
              name: lookbook-api-secrets
              key: database-username
        - name: JDBC_DATABASE_PASSWORD
          valueFrom:
            secretKeyRef:
              name: lookbook-api-secrets
              key: database-password
        - name: JWT_SECRET_KEY
          valueFrom:
            secretKeyRef:
              name: lookbook-api-secrets
              key: jwt-secret
        resources:
          limits:
            cpu: "1"
            memory: "1Gi"
          requests:
            cpu: "500m"
            memory: "512Mi"
        readinessProbe:
          httpGet:
            path: /api/v1/actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        livenessProbe:
          httpGet:
            path: /api/v1/actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
```

```yaml
# k8s/service.yaml
apiVersion: v1
kind: Service
metadata:
  name: lookbook-api
spec:
  selector:
    app: lookbook-api
  ports:
    - port: 80
      targetPort: 8080
  type: ClusterIP
```

Apply the Kubernetes manifests:

```bash
kubectl apply -f k8s/
```

## Monitoring

The application includes Spring Boot Actuator endpoints for monitoring:

- Health check: `/api/v1/actuator/health`
- Metrics: `/api/v1/actuator/metrics`
- Info: `/api/v1/actuator/info`

Configure Prometheus and Grafana for comprehensive monitoring.

## Scaling Considerations

### Horizontal Scaling

The application is stateless and can be horizontally scaled by:
1. Increasing the number of replicas in Kubernetes
2. Using auto-scaling based on CPU/memory metrics

### Database Scaling

For database scaling:
1. Use connection pooling (HikariCP is configured by default)
2. Consider read replicas for PostgreSQL
3. Implement caching for frequently accessed data

## Backup and Recovery

### Database Backup

Set up regular PostgreSQL backups:

```bash
# Daily backup script
pg_dump -h hostname -U username -d lookbook_prod -F c -f /backups/lookbook_$(date +%Y%m%d).dump
```

### Application Configuration Backup

Store configuration files in version control and treat them as code.

## Security Hardening

### Production Security Checklist

1. ✅ Use HTTPS only
2. ✅ Set secure HTTP headers (configured in SecurityConfig)
3. ✅ Keep dependencies updated
4. ✅ Use strong JWT secret key
5. ✅ Set short JWT expiration time
6. ✅ Store secrets in environment variables or secret management system
7. ✅ Implement rate limiting
8. ✅ Enable audit logging

## Troubleshooting

### Common Issues

1. **Database Connection Issues**
   - Check database credentials
   - Verify network connectivity
   - Ensure database is running

2. **JWT Authentication Failures**
   - Verify JWT secret key is correctly set
   - Check token expiration times
   - Ensure clocks are synchronized

3. **Performance Issues**
   - Check database query performance
   - Monitor API response times
   - Review application logs for warnings

### Logs

Access application logs:

```bash
# Docker
docker logs lookbook-api

# Kubernetes
kubectl logs deployment/lookbook-api

# Traditional deployment
tail -f /path/to/logs/application.log
``` 