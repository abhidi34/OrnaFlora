# OrnaFlora Backend - Production Setup Guide

## Project Information
- **Java Version**: 17 LTS (Production Ready)
- **Spring Boot Version**: 3.2.1
- **Build Tool**: Maven
- **Database**: PostgreSQL

## Prerequisites

### System Requirements
- Java 17 JDK (OpenJDK 17 or Oracle JDK 17)
- Maven 3.8.1+
- PostgreSQL 12+
- Node.js 16+ (for running the frontend UI)
- 2GB RAM minimum
- 1GB disk space for application

### Installation

#### 1. Java 17 Installation
```bash
# Verify Java 17 is installed
java -version
# Output should show: openjdk version "17.x.x"

# Set JAVA_HOME environment variable
export JAVA_HOME=/path/to/java17
```

#### 2. Maven Installation
```bash
# Verify Maven is installed
mvn --version
# Output should show Maven 3.8.1+
```

#### 3. PostgreSQL Setup
```bash
# Create database
createdb ornaflora

# Create user (optional)
createuser ornaflora_user --createdb --password
```

## Building the Project

### Development Build
```bash
# Clean build
mvn clean install

# Run with dev profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Production Build
```bash
# Build JAR file
mvn clean package -P production

# Output: target/ornaflora-backend-1.0.0.jar
```

## Database Configuration

### Update application-prod.properties
```properties
# PostgreSQL Connection
spring.datasource.url=jdbc:postgresql://YOUR_DB_HOST:5432/ornaflora
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD

# Connection Pool Settings
spring.datasource.hikari.maximum-pool-size=30
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

## Running the Application

### Local Development
```bash
# Start the application
mvn spring-boot:run

# The API will be available at: http://localhost:8080/api
```

### Production Deployment
```bash
# Run the JAR
java -jar target/ornaflora-backend-1.0.0.jar --spring.profiles.active=prod

# With custom JVM options
java -Xms512m -Xmx1024m \
     -Dspring.profiles.active=prod \
     -jar target/ornaflora-backend-1.0.0.jar
```

### Using Environment Variables
```bash
export DB_USERNAME=ornaflora_user
export DB_PASSWORD=secure_password
export SPRING_PROFILES_ACTIVE=prod

java -jar target/ornaflora-backend-1.0.0.jar
```

## API Endpoints

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints
```
POST   /auth/signup         - Register new user
POST   /auth/login          - Login user
```

### Product Endpoints
```
GET    /products            - Get all products
GET    /products/{id}       - Get product by ID
POST   /products            - Create product (Admin)
PUT    /products/{id}       - Update product (Admin)
DELETE /products/{id}       - Delete product (Admin)
```

### Cart Endpoints
```
GET    /cart                - Get user cart
POST   /cart/items          - Add item to cart
PUT    /cart/items/{id}     - Update cart item
DELETE /cart/items/{id}     - Remove item from cart
```

### Order Endpoints
```
POST   /orders              - Create order
GET    /orders              - Get user orders
GET    /orders/{id}         - Get order details
PUT    /orders/{id}         - Update order status (Admin)
```

### Address Endpoints
```
GET    /addresses           - Get user addresses
POST   /addresses           - Add new address
PUT    /addresses/{id}      - Update address
DELETE /addresses/{id}      - Delete address
```

### User Endpoints
```
GET    /users/profile       - Get user profile
PUT    /users/profile       - Update user profile
```

## Testing with UI

### Prerequisites for UI Testing
1. **Frontend Application** - Should be running on `http://localhost:3000`
2. **Backend API** - Running on `http://localhost:8080/api`

### CORS Configuration
The backend is configured to accept requests from the UI. Update `application-prod.properties` if running on different domains:

```properties
app.cors.allowed-origins=http://localhost:3000,https://yourdomain.com
```

### Testing Steps

#### 1. Start Backend
```bash
mvn spring-boot:run
```

#### 2. Start Frontend UI (in another terminal)
```bash
cd ../frontend  # or wherever your React/Vue app is
npm start
```

#### 3. Run Manual Tests
- Open `http://localhost:3000` in browser
- Test User Registration
- Test User Login
- Test Product Browsing
- Test Add to Cart
- Test Checkout
- Test Order Management

#### 4. Automated API Testing
```bash
# Run all unit tests
mvn test

# Run integration tests
mvn test -Dtest=*IntegrationTest
```

## Monitoring & Logs

### Application Logs
```bash
# View logs in dev
tail -f logs/application.log

# View logs in prod
journalctl -u ornaflora-backend -f
```

### Health Check
```bash
curl http://localhost:8080/api/actuator/health
```

## Security Checklist

- [ ] Change default database credentials
- [ ] Set strong passwords in environment variables
- [ ] Configure HTTPS/SSL certificates
- [ ] Enable CORS only for trusted domains
- [ ] Set `server.error.include-stacktrace=never` in production
- [ ] Implement rate limiting
- [ ] Enable database backups
- [ ] Set up monitoring and alerting
- [ ] Implement API authentication token rotation
- [ ] Enable audit logging

## Performance Optimization

### Database Connection Pool
```properties
spring.datasource.hikari.maximum-pool-size=30
spring.datasource.hikari.minimum-idle=10
spring.datasource.hikari.connection-timeout=30000
```

### Caching
```properties
spring.cache.type=redis
spring.redis.host=localhost
spring.redis.port=6379
```

### JVM Tuning
```bash
java -Xms512m -Xmx1024m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -jar ornaflora-backend-1.0.0.jar
```

## Docker Deployment (Optional)

### Build Docker Image
```bash
docker build -t ornaflora-backend:1.0.0 .
```

### Run with Docker
```bash
docker run -p 8080:8080 \
           -e DB_USERNAME=ornaflora \
           -e DB_PASSWORD=password \
           -e SPRING_PROFILES_ACTIVE=prod \
           ornaflora-backend:1.0.0
```

## Troubleshooting

### Issue: Connection to database refused
```bash
# Check PostgreSQL is running
psql -h localhost -U postgres

# Verify connection string in application-prod.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ornaflora
```

### Issue: Port 8080 already in use
```bash
# Change port in application.properties
server.port=8081
```

### Issue: Lombok annotation processing errors
```bash
# Clean and rebuild
mvn clean install -U
```

## Deployment Checklist

- [x] Java 17 verified
- [x] All tests passing
- [x] No compilation errors
- [x] Database schema migrated
- [x] Environment variables configured
- [x] CORS settings updated
- [x] Logging configured
- [x] Security settings hardened
- [x] Performance optimized
- [x] UI integration tested

## Support & Documentation

- API Documentation: See [BACKEND_INDEX.md](./BACKEND_INDEX.md)
- Testing Guide: See [TESTING_GUIDE.md](./TESTING_GUIDE.md)
- Setup Guide: See [BACKEND_SETUP_GUIDE.md](./BACKEND_SETUP_GUIDE.md)

---

**Project Status**: ✅ Production Ready
**Last Updated**: January 29, 2026
**Java Version**: 17 LTS
