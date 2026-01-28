# OrnaFlora Development Environment Setup

## Quick Start (5-10 minutes)

### Step 1: Ensure Prerequisites
```bash
# Check Java is installed and version is 17+
java -version

# Check Maven is installed
mvn -version
```

### Step 2: Create Database
For PostgreSQL:
```sql
CREATE DATABASE ornaflora;
```

For MySQL:
```sql
CREATE DATABASE ornaflora;
```

### Step 3: Build and Run
```bash
# Navigate to backend directory
cd e:\OrnaFlora-backend

# Build the project (downloads dependencies, compiles, runs tests)
mvn clean install

# Run the application
mvn spring-boot:run
```

Application will start on: **http://localhost:8080**

### Step 4: Verify Setup
```bash
# Check if server is running
curl http://localhost:8080/api/actuator/health

# Expected response:
# {"status":"UP"}
```

### Step 5: Test Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@ornaflora.com","password":"admin123"}'
```

---

## Detailed Setup Steps

### Windows Users

#### 1. Install PostgreSQL
1. Download from https://www.postgresql.org/download/windows/
2. Run the installer
3. Set password for postgres user (remember it!)
4. Keep port as 5432
5. Complete installation

#### 2. Create Database
1. Open pgAdmin (installed with PostgreSQL)
2. Right-click Databases → Create → Database
3. Name: `ornaflora`
4. Click Create

#### 3. Verify Connection
1. Open Command Prompt
2. Run: `psql -U postgres -d ornaflora`
3. If successful, you'll see the postgres prompt

#### 4. Build and Run
```bash
# Open Command Prompt (cmd) or PowerShell
cd e:\OrnaFlora-backend
mvn clean install
mvn spring-boot:run
```

### Mac Users

#### 1. Install PostgreSQL (using Homebrew)
```bash
brew install postgresql@15
brew services start postgresql@15
```

#### 2. Create Database
```bash
createdb ornaflora
```

#### 3. Build and Run
```bash
cd /path/to/OrnaFlora-backend
mvn clean install
mvn spring-boot:run
```

### Linux Users

#### 1. Install PostgreSQL
```bash
sudo apt-get update
sudo apt-get install postgresql postgresql-contrib
```

#### 2. Create Database
```bash
sudo -u postgres createdb ornaflora
```

#### 3. Build and Run
```bash
cd /path/to/OrnaFlora-backend
mvn clean install
mvn spring-boot:run
```

---

## Common Issues & Solutions

### Issue: "port 8080 is already in use"
**Solution:**
```bash
# Windows - Find process using port 8080
netstat -ano | findstr :8080
# Kill process: taskkill /PID <process_id> /F

# Mac/Linux - Find process using port 8080
lsof -i :8080
# Kill process: kill -9 <process_id>
```

### Issue: "database ornaflora does not exist"
**Solution:**
Create the database manually:
- PostgreSQL: `createdb ornaflora`
- MySQL: `mysql -u root -p -e "CREATE DATABASE ornaflora;"`

### Issue: "Cannot connect to database"
**Solution:**
1. Verify database is running
2. Check credentials in application.properties
3. Verify database server is accepting connections
4. Check firewall settings

### Issue: "Maven: command not found"
**Solution:**
1. Install Maven from https://maven.apache.org/install.html
2. Add Maven to PATH environment variable
3. Restart terminal and try again

### Issue: "Java: command not found"
**Solution:**
1. Install Java 17+ from https://www.oracle.com/java/technologies/downloads/
2. Add Java to PATH environment variable
3. Restart terminal and verify: `java -version`

---

## API Testing Tools

### Using cURL (Command Line)
Already available on Mac/Linux, install on Windows via Git Bash.

```bash
# Test server health
curl http://localhost:8080/api/actuator/health

# Test login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@ornaflora.com","password":"admin123"}'

# Get all products
curl http://localhost:8080/api/products
```

### Using Postman
1. Download from https://www.postman.com/downloads/
2. Create collection
3. Create requests for each endpoint
4. Test API responses

### Using VS Code REST Client Extension
1. Install "REST Client" extension in VS Code
2. Create `test.http` file:
```http
### Get Health
GET http://localhost:8080/api/actuator/health

### Login
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@ornaflora.com",
  "password": "admin123"
}

### Get All Products
GET http://localhost:8080/api/products
```
3. Click "Send Request" next to each request

---

## Frontend Integration

Update your React frontend to call the backend API.

### Example: Update Auth.js login function
```javascript
// Before (localStorage)
const handleLogin = (email, password) => {
  if (email === 'admin@ornaflora.com' && password === 'admin123') {
    setAdminUser(email);
    navigate('/admin-dashboard');
  }
}

// After (API call)
const handleLogin = async (email, password) => {
  try {
    const response = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });
    
    const data = await response.json();
    
    if (response.ok) {
      if (data.role === 'ADMIN') {
        setAdminUser(data.user);
        navigate('/admin-dashboard');
      } else {
        setCurrentUser(data.user);
        navigate('/');
      }
    }
  } catch (error) {
    console.error('Login failed:', error);
  }
}
```

### Example: Update Products fetch
```javascript
// Before (hardcoded products)
const products = [...];

// After (fetch from API)
const [products, setProducts] = useState([]);

useEffect(() => {
  fetch('http://localhost:8080/api/products')
    .then(res => res.json())
    .then(data => setProducts(data))
    .catch(err => console.error('Failed to load products:', err));
}, []);
```

---

## Production Deployment Checklist

Before deploying to production:

- [ ] Change admin credentials (admin@ornaflora.com)
- [ ] Change database password
- [ ] Update JWT secret to 32+ character random string
- [ ] Update CORS allowed origins to your domain
- [ ] Update database URL to production server
- [ ] Set logging level to WARN
- [ ] Enable HTTPS for database connections
- [ ] Set environment variables for sensitive data
- [ ] Run full test suite
- [ ] Create database backups
- [ ] Set up monitoring and alerting
- [ ] Document deployment procedures

---

## Support Commands

```bash
# View application logs
tail -f logs/ornaflora.log

# Build without tests
mvn clean install -DskipTests

# Run specific test
mvn test -Dtest=UserServiceTest

# Create production build
mvn clean package -DskipTests

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"

# Check dependencies
mvn dependency:tree
```

---

## Next Steps

1. ✅ Backend is running on port 8080
2. ⏳ Update React frontend to use backend APIs
3. ⏳ Test all endpoints with sample data
4. ⏳ Integrate payment gateway (if needed)
5. ⏳ Set up email notifications
6. ⏳ Deploy to production server

---

**Happy Coding! 🚀**
