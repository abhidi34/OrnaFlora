# OrnaFlora Spring Boot Backend

This is a production-ready Spring Boot backend application for the OrnaFlora e-commerce platform.

## Technologies Used

- **Java 17**
- **Spring Boot 3.2.1**
- **Spring Data JPA (Hibernate)**
- **PostgreSQL** (Primary) / **MySQL** (Alternative)
- **Flyway** for database migrations
- **Lombok** for boilerplate reduction
- **Maven** for build management

## Project Structure

```
src/
├── main/
│   ├── java/com/ornaflora/
│   │   ├── OrnaFloraApplication.java    # Main Spring Boot class
│   │   ├── config/                       # Configuration classes
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   ├── JacksonConfig.java
│   │   │   └── CorsConfig.java (already in main class)
│   │   ├── controller/                   # REST Controllers
│   │   │   ├── AuthController.java
│   │   │   ├── ProductController.java
│   │   │   ├── OrderController.java
│   │   │   ├── CartController.java
│   │   │   ├── AddressController.java
│   │   │   └── UserController.java
│   │   ├── service/                      # Business Logic
│   │   │   ├── UserService.java
│   │   │   ├── ProductService.java
│   │   │   ├── OrderService.java
│   │   │   ├── CartService.java
│   │   │   └── AddressService.java
│   │   ├── repository/                   # Data Access Layer
│   │   │   ├── UserRepository.java
│   │   │   ├── ProductRepository.java
│   │   │   ├── OrderRepository.java
│   │   │   ├── OrderItemRepository.java
│   │   │   ├── CartItemRepository.java
│   │   │   └── AddressRepository.java
│   │   ├── model/                        # JPA Entities
│   │   │   ├── User.java
│   │   │   ├── Product.java
│   │   │   ├── Order.java
│   │   │   ├── OrderItem.java
│   │   │   ├── CartItem.java
│   │   │   └── Address.java
│   │   └── dto/                          # Data Transfer Objects
│   │       ├── LoginRequest.java
│   │       ├── SignupRequest.java
│   │       ├── UserDTO.java
│   │       ├── ProductDTO.java
│   │       ├── OrderDTO.java
│   │       ├── CartItemDTO.java
│   │       ├── AddressDTO.java
│   │       └── *Request.java (request DTOs)
│   └── resources/
│       ├── application.properties         # Main configuration
│       └── db/migration/
│           └── V1__Initial_schema.sql    # Database schema
├── test/
└── pom.xml                               # Maven build configuration
```

## Prerequisites

### Required
- **Java 17** or higher installed
- **Maven 3.8+** installed
- **PostgreSQL 12+** OR **MySQL 8.0+**

### Optional
- Git (for version control)
- Postman (for API testing)

## Database Setup

### Option 1: PostgreSQL (Recommended)

1. **Install PostgreSQL** (if not already installed)
   - Download from https://www.postgresql.org/download/
   - Follow the installation guide for your OS

2. **Create Database**
   ```sql
   CREATE DATABASE ornaflora;
   ```

3. **Verify Connection** (optional)
   ```bash
   psql -U postgres -d ornaflora
   ```

4. **Application Configuration** (already set in application.properties)
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/ornaflora
   spring.datasource.username=postgres
   spring.datasource.password=password
   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
   ```

### Option 2: MySQL

1. **Install MySQL** (if not already installed)
   - Download from https://dev.mysql.com/downloads/mysql/
   - Follow the installation guide for your OS

2. **Create Database**
   ```sql
   CREATE DATABASE ornaflora;
   ```

3. **Switch to MySQL in application.properties**
   - Comment out PostgreSQL configuration
   - Uncomment MySQL configuration:
   ```properties
   # spring.datasource.url=jdbc:mysql://localhost:3306/ornaflora
   # spring.datasource.username=root
   # spring.datasource.password=password
   # spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
   ```

## Installation & Setup

### 1. Clone/Navigate to Backend Directory
```bash
cd e:\OrnaFlora-backend
```

### 2. Build the Project
```bash
mvn clean install
```
This command:
- Cleans previous builds
- Downloads all dependencies
- Compiles Java code
- Runs tests (if any)
- Creates JAR file in `target/` directory

### 3. Run the Application

#### Option A: Using Maven
```bash
mvn spring-boot:run
```

#### Option B: Using Java (after build)
```bash
java -jar target/ornaflora-backend-1.0.0.jar
```

#### Option C: Using IDE (IntelliJ IDEA / Eclipse)
1. Open the project in your IDE
2. Right-click on `OrnaFloraApplication.java`
3. Select "Run" or "Debug"

### 4. Verify Server is Running

Open your browser or use curl:
```bash
curl http://localhost:8080/api/actuator/health
```

Expected response:
```json
{
  "status": "UP"
}
```

## API Endpoints

### Base URL: `http://localhost:8080/api`

### Authentication
- **POST** `/auth/login` - Login user
- **POST** `/auth/signup` - Register new user
- **POST** `/auth/logout` - Logout user
- **GET** `/auth/profile/{userId}` - Get user profile

### Products
- **GET** `/products` - Get all products
- **GET** `/products/{id}` - Get product by ID
- **GET** `/products/category/{category}` - Get products by category
- **GET** `/products/search/{searchTerm}` - Search products
- **GET** `/products/categories/all` - Get all categories
- **GET** `/products/available/all` - Get available products
- **POST** `/products` - Create product (admin)
- **PUT** `/products/{id}` - Update product (admin)
- **DELETE** `/products/{id}` - Delete product (admin)
- **PUT** `/products/{id}/stock` - Update stock (admin)

### Orders
- **POST** `/orders?userId={userId}` - Create order
- **GET** `/orders/{id}` - Get order by ID
- **GET** `/orders/user/{userId}` - Get user's orders
- **GET** `/orders/status/{status}` - Get orders by status
- **GET** `/orders/daterange?startDate=...&endDate=...` - Get orders by date range
- **PUT** `/orders/{id}/status?status={status}` - Update order status
- **PUT** `/orders/{id}/cancel` - Cancel order

### Cart
- **POST** `/cart?userId={userId}` - Add to cart
- **GET** `/cart?userId={userId}` - Get cart items
- **PUT** `/cart/{itemId}` - Update cart item
- **DELETE** `/cart/{itemId}` - Remove from cart
- **DELETE** `/cart?userId={userId}` - Clear cart
- **DELETE** `/cart/product?userId={userId}&productId={productId}` - Remove product

### Addresses
- **POST** `/addresses?userId={userId}` - Save address
- **GET** `/addresses/{id}` - Get address by ID
- **GET** `/addresses/user/{userId}` - Get user addresses
- **GET** `/addresses/user/{userId}/default` - Get default address
- **PUT** `/addresses/{id}` - Update address
- **DELETE** `/addresses/{id}` - Delete address
- **PUT** `/addresses/{addressId}/default?userId={userId}` - Set default address

### Users (Admin)
- **GET** `/users/{id}` - Get user by ID
- **GET** `/users/email/{email}` - Get user by email
- **GET** `/users` - Get all users
- **GET** `/users/admin/all` - Get all admins
- **PUT** `/users/{id}` - Update user
- **DELETE** `/users/{id}` - Delete user
- **POST** `/users/{id}/change-password?oldPassword=...&newPassword=...` - Change password

## Testing the API

### Using Postman

1. **Download Postman** from https://www.postman.com/downloads/

2. **Create a new request:**
   - Set method to `POST`
   - Enter URL: `http://localhost:8080/api/auth/login`
   - Go to Body tab, select `raw` and `JSON`
   - Enter:
   ```json
   {
     "email": "admin@ornaflora.com",
     "password": "admin123"
   }
   ```
   - Click Send

3. **Expected Response:**
   ```json
   {
     "message": "Login successful",
     "user": {
       "id": 1,
       "email": "admin@ornaflora.com",
       "name": "Admin User",
       "role": "ADMIN",
       ...
     },
     "role": "ADMIN",
     "userId": 1
   }
   ```

### Using cURL

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@ornaflora.com","password":"admin123"}'

# Get all products
curl http://localhost:8080/api/products

# Get product by ID
curl http://localhost:8080/api/products/1

# Get all users
curl http://localhost:8080/api/users
```

## Database Schema

The application automatically creates the following tables via Flyway:

1. **users** - User accounts (admin/customer)
2. **products** - Product inventory
3. **orders** - Customer orders
4. **order_items** - Items within orders
5. **addresses** - Shipping/billing addresses
6. **cart_items** - Shopping cart items

## Configuration Files

### application.properties
- **Server Configuration**: Port 8080, context-path /api
- **Database**: PostgreSQL by default (MySQL alternative commented)
- **JPA/Hibernate**: Validation mode, batch processing
- **Flyway**: Automatic migration on startup
- **Logging**: File-based with rotation on 10MB
- **CORS**: Configured for localhost:3000
- **File Upload**: Max 5MB, allowed formats

## Important Notes

### Default Admin Credentials
```
Email: admin@ornaflora.com
Password: admin123
```
⚠️ Change these in production!

### Database Credentials
```
PostgreSQL:
  Host: localhost
  Port: 5432
  Database: ornaflora
  Username: postgres
  Password: password

MySQL:
  Host: localhost
  Port: 3306
  Database: ornaflora
  Username: root
  Password: password
```
⚠️ Change these in production!

### CORS Configuration
Currently allows:
- `http://localhost:3000` (React frontend)
- `http://localhost:3001`
- `127.0.0.1:3000`

Update in `OrnaFloraApplication.java` for production domains.

## Connecting Frontend to Backend

In your React application, update API calls:

```javascript
// Before (using localStorage)
const loginUser = (email, password) => {
  // localStorage logic
}

// After (using backend API)
const loginUser = async (email, password) => {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  return response.json();
}
```

## Troubleshooting

### Application fails to start
1. Check Java version: `java -version`
2. Check Maven: `mvn -version`
3. Check database is running: `psql -U postgres` (PostgreSQL)
4. Check port 8080 is not in use: `lsof -i :8080` (Mac/Linux)

### Database connection errors
1. Verify database is running and accessible
2. Check credentials in `application.properties`
3. Ensure database `ornaflora` exists
4. Check firewall settings

### CORS errors in frontend
1. Verify `http://localhost:3000` is in allowed origins
2. Check browser console for CORS error details
3. Update `OrnaFloraApplication.java` CORS configuration

### Flyway migration errors
1. Check SQL syntax in `V1__Initial_schema.sql`
2. Ensure database user has CREATE TABLE permissions
3. Check `logs/ornaflora.log` for detailed errors

## Building for Production

### Create JAR for Deployment
```bash
mvn clean package -DskipTests
```

### Use Production Configuration
Create `application-prod.properties`:
```properties
spring.datasource.url=jdbc:postgresql://prod-db-host:5432/ornaflora
spring.datasource.username=prod_user
spring.datasource.password=prod_password
spring.jpa.hibernate.ddl-auto=validate
server.port=8080
logging.level.root=WARN
```

### Run with Production Profile
```bash
java -jar target/ornaflora-backend-1.0.0.jar --spring.profiles.active=prod
```

## Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Guide](https://spring.io/projects/spring-data-jpa)
- [Flyway Migration Guide](https://flywaydb.org/documentation/getstarted)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [MySQL Documentation](https://dev.mysql.com/doc/)

## Support

For issues, questions, or contributions, please check the project documentation or contact the development team.

---

**Last Updated**: 2024
**Version**: 1.0.0
