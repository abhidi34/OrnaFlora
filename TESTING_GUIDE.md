# OrnaFlora Backend - Complete Testing Guide

## 🧪 API Testing Tutorial

This guide shows you how to test all 39 API endpoints with real data.

---

## 🔧 Prerequisites

Choose one API testing tool:

### Option 1: cURL (Command Line)
- Already installed on Mac/Linux
- Install on Windows via Git Bash

### Option 2: Postman (Recommended)
- Download: https://www.postman.com/downloads/
- Free desktop application
- Beautiful UI for testing

### Option 3: VS Code REST Client
- Install "REST Client" extension
- Create `.http` files with requests
- Run requests directly in editor

---

## 📋 Test Scenarios

### Scenario 1: Admin Login & Product Management

#### 1.1 Admin Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@ornaflora.com",
    "password": "admin123"
  }'
```

**Expected Response:**
```json
{
  "message": "Login successful",
  "user": {
    "id": 1,
    "email": "admin@ornaflora.com",
    "name": "Admin User",
    "role": "ADMIN",
    "isActive": true
  },
  "role": "ADMIN",
  "userId": 1
}
```

#### 1.2 Get All Products
```bash
curl http://localhost:8080/api/products
```

**Expected:** Array of 6 sample products

#### 1.3 Get Single Product
```bash
curl http://localhost:8080/api/products/1
```

**Expected:** Rose Bouquet product details

#### 1.4 Search Products
```bash
curl http://localhost:8080/api/products/search/rose
```

**Expected:** Products with "rose" in name

#### 1.5 Get Categories
```bash
curl http://localhost:8080/api/products/categories/all
```

**Expected:**
```json
["Flowers", "Plants"]
```

#### 1.6 Get Products by Category
```bash
curl http://localhost:8080/api/products/category/Flowers
```

**Expected:** Rose, Sunflower, Tulip products

#### 1.7 Create New Product (Admin)
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Lily Arrangement",
    "description": "Beautiful white lilies",
    "category": "Flowers",
    "price": 49.99,
    "stock": 20,
    "imageUrls": ["https://example.com/lily.jpg"]
  }'
```

**Expected:** Product created with id (example: 7)

#### 1.8 Update Product Stock
```bash
curl -X PUT http://localhost:8080/api/products/1/stock \
  -H "Content-Type: application/json" \
  -d 'stock=100'
```

**Alternative with URL param:**
```bash
curl -X PUT "http://localhost:8080/api/products/1/stock?stock=100"
```

**Expected:** "Stock updated successfully"

#### 1.9 Update Product (Admin)
```bash
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Premium Rose Bouquet",
    "description": "Extra premium red roses",
    "category": "Flowers",
    "price": 59.99,
    "stock": 100,
    "imageUrls": ["https://example.com/rose1.jpg", "https://example.com/rose2.jpg"]
  }'
```

**Expected:** Updated product details

---

### Scenario 2: Customer Registration & Shopping

#### 2.1 Customer Signup
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123",
    "name": "John Doe",
    "phone": "+1234567890"
  }'
```

**Expected:** New user created with id (example: 2)
**Note:** Save the userId for next requests

#### 2.2 Customer Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

**Expected:**
```json
{
  "message": "Login successful",
  "user": {
    "id": 2,
    "email": "john@example.com",
    "role": "CUSTOMER"
  },
  "role": "CUSTOMER",
  "userId": 2
}
```

#### 2.3 Add Item to Cart
```bash
curl -X POST "http://localhost:8080/api/cart?userId=2" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 2,
    "selectedImageUrl": "https://example.com/rose.jpg"
  }'
```

**Expected:** Cart item created with id (example: 1)

#### 2.4 Add Another Item to Cart
```bash
curl -X POST "http://localhost:8080/api/cart?userId=2" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 3,
    "quantity": 1,
    "selectedImageUrl": "https://example.com/orchid.jpg"
  }'
```

**Expected:** Second cart item created (id: 2)

#### 2.5 View Cart
```bash
curl "http://localhost:8080/api/cart?userId=2"
```

**Expected:** Array with 2 cart items

#### 2.6 Update Cart Item Quantity
```bash
curl -X PUT http://localhost:8080/api/cart/1 \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 3,
    "selectedImageUrl": "https://example.com/rose.jpg"
  }'
```

**Expected:** Updated cart item with quantity 3

#### 2.7 Add Shipping Address
```bash
curl -X POST "http://localhost:8080/api/addresses?userId=2" \
  -H "Content-Type: application/json" \
  -d '{
    "street": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "postalCode": "10001",
    "country": "USA",
    "phone": "+1234567890",
    "isDefault": true
  }'
```

**Expected:** Address created with id (example: 1)
**Note:** Save addressId for order creation

#### 2.8 Get User Addresses
```bash
curl "http://localhost:8080/api/addresses/user/2"
```

**Expected:** Array with address(es)

#### 2.9 Create Order
```bash
curl -X POST "http://localhost:8080/api/orders?userId=2" \
  -H "Content-Type: application/json" \
  -d '{
    "addressId": 1,
    "paymentMethod": "CARD",
    "items": [
      {
        "productId": 1,
        "quantity": 3,
        "selectedImageUrl": "https://example.com/rose.jpg"
      },
      {
        "productId": 3,
        "quantity": 1,
        "selectedImageUrl": "https://example.com/orchid.jpg"
      }
    ]
  }'
```

**Expected:**
```json
{
  "id": 1,
  "userId": 2,
  "status": "CONFIRMED",
  "totalAmount": 234.97,
  "items": [...]
}
```

**Note:** Cart is automatically cleared after order creation

#### 2.10 View User Orders
```bash
curl "http://localhost:8080/api/orders/user/2"
```

**Expected:** Array with 1 order

#### 2.11 Get Order Details
```bash
curl http://localhost:8080/api/orders/1
```

**Expected:** Complete order with items and totals

---

### Scenario 3: Admin Order Management

#### 3.1 Get All Confirmed Orders
```bash
curl "http://localhost:8080/api/orders/status/CONFIRMED"
```

**Expected:** Array with orders in CONFIRMED status

#### 3.2 Update Order Status to Processing
```bash
curl -X PUT "http://localhost:8080/api/orders/1/status?status=PROCESSING"
```

**Expected:** Order updated with status PROCESSING

#### 3.3 Update Order Status to Shipped
```bash
curl -X PUT "http://localhost:8080/api/orders/1/status?status=SHIPPED"
```

**Expected:** Order status updated to SHIPPED

#### 3.4 Update Order Status to Delivered
```bash
curl -X PUT "http://localhost:8080/api/orders/1/status?status=DELIVERED"
```

**Expected:** Order status updated to DELIVERED

#### 3.5 Get Orders by Date Range
```bash
curl "http://localhost:8080/api/orders/daterange?startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59"
```

**Expected:** Array of orders within date range

---

### Scenario 4: User Management (Admin)

#### 4.1 Get All Users
```bash
curl http://localhost:8080/api/users
```

**Expected:** Array of active users

#### 4.2 Get All Admins
```bash
curl http://localhost:8080/api/users/admin/all
```

**Expected:** Array of admin users

#### 4.3 Get User by ID
```bash
curl http://localhost:8080/api/users/2
```

**Expected:** User John Doe details

#### 4.4 Get User by Email
```bash
curl http://localhost:8080/api/users/email/john@example.com
```

**Expected:** User John Doe details

#### 4.5 Update User Profile
```bash
curl -X PUT http://localhost:8080/api/users/2 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Smith",
    "phone": "+9876543210",
    "avatarUrl": "https://example.com/avatar.jpg"
  }'
```

**Expected:** Updated user details

#### 4.6 Change User Password
```bash
curl -X POST "http://localhost:8080/api/users/2/change-password?oldPassword=password123&newPassword=newpassword456"
```

**Expected:** "Password changed successfully"

---

### Scenario 5: Address Management

#### 5.1 Add Second Address
```bash
curl -X POST "http://localhost:8080/api/addresses?userId=2" \
  -H "Content-Type: application/json" \
  -d '{
    "street": "456 Park Avenue",
    "city": "Los Angeles",
    "state": "CA",
    "postalCode": "90001",
    "country": "USA",
    "phone": "+1987654321",
    "isDefault": false
  }'
```

**Expected:** Second address created (id: 2)

#### 5.2 Get Default Address
```bash
curl "http://localhost:8080/api/addresses/user/2/default"
```

**Expected:** First address (marked as default)

#### 5.3 Set Second Address as Default
```bash
curl -X PUT "http://localhost:8080/api/addresses/2/default?userId=2"
```

**Expected:** Second address now marked as default, first unmarked

#### 5.4 Get Updated Default Address
```bash
curl "http://localhost:8080/api/addresses/user/2/default"
```

**Expected:** Second address (now default)

#### 5.5 Update Address
```bash
curl -X PUT http://localhost:8080/api/addresses/1 \
  -H "Content-Type: application/json" \
  -d '{
    "street": "789 Fifth Avenue",
    "city": "New York",
    "state": "NY",
    "postalCode": "10002",
    "country": "USA",
    "phone": "+1111111111"
  }'
```

**Expected:** Updated address details

#### 5.6 Delete Address
```bash
curl -X DELETE http://localhost:8080/api/addresses/1
```

**Expected:** "Address deleted successfully"

---

### Scenario 6: Error Handling

#### 6.1 Invalid Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "invalid@example.com",
    "password": "wrongpassword"
  }'
```

**Expected Response (401 Unauthorized):**
```json
{
  "message": "Login failed: User not found"
}
```

#### 6.2 Non-existent Product
```bash
curl http://localhost:8080/api/products/999
```

**Expected Response (404 Not Found):**
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Internal Server Error",
  "message": "Product not found",
  "path": "/api/products/999"
}
```

#### 6.3 Insufficient Stock
```bash
curl -X POST "http://localhost:8080/api/orders?userId=2" \
  -H "Content-Type: application/json" \
  -d '{
    "addressId": 1,
    "paymentMethod": "CARD",
    "items": [{
      "productId": 1,
      "quantity": 999999,
      "selectedImageUrl": ""
    }]
  }'
```

**Expected Response (400 Bad Request):**
```json
{
  "message": "Cart is empty" / "Insufficient stock for product"
}
```

#### 6.4 Duplicate Email Registration
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@ornaflora.com",
    "password": "password123",
    "name": "Test User",
    "phone": "+1234567890"
  }'
```

**Expected Response (400 Bad Request):**
```json
{
  "message": "Email already registered"
}
```

---

## 🧪 Using Postman

### Step 1: Import Collection
1. Open Postman
2. Click "Import"
3. Create requests for each endpoint

### Step 2: Create Requests

**Example: Login Request**
- Method: POST
- URL: `http://localhost:8080/api/auth/login`
- Body (raw JSON):
```json
{
  "email": "admin@ornaflora.com",
  "password": "admin123"
}
```
- Click "Send"

### Step 3: Use Environment Variables
Create variables for reusable values:
- `{{base_url}}` = http://localhost:8080/api
- `{{admin_id}}` = 1
- `{{customer_id}}` = 2
- `{{product_id}}` = 1
- `{{address_id}}` = 1

### Step 4: Create Request Collections
Organize requests:
```
OrnaFlora API
├── Authentication
│   ├── Login (Admin)
│   ├── Login (Customer)
│   └── Signup
├── Products
│   ├── Get All
│   ├── Get by ID
│   ├── Create
│   ├── Update
│   └── Delete
├── Orders
│   ├── Create
│   ├── Get by ID
│   └── Update Status
├── Cart
│   ├── Add Item
│   ├── Get Items
│   └── Remove Item
└── Addresses
    ├── Add Address
    ├── Get Addresses
    └── Set Default
```

---

## 📊 Complete Test Coverage

| Feature | Endpoints | Tests |
|---------|-----------|-------|
| Authentication | 4 | 4 |
| Products | 10 | 10 |
| Orders | 7 | 7 |
| Cart | 6 | 6 |
| Addresses | 7 | 7 |
| Users | 7 | 5 |
| **TOTAL** | **39** | **39** |

---

## ✅ Test Checklist

### Basic Functionality
- [ ] Server starts without errors
- [ ] Health check passes
- [ ] Admin login works
- [ ] Customer signup works
- [ ] Products display correctly

### Product Management
- [ ] Create product works
- [ ] Update product works
- [ ] Delete product works
- [ ] Search products works
- [ ] Filter by category works

### Shopping & Orders
- [ ] Add to cart works
- [ ] Update cart item works
- [ ] Remove from cart works
- [ ] Create order works
- [ ] Order status updates work
- [ ] Stock decreases after order

### Address Management
- [ ] Add address works
- [ ] Set default address works
- [ ] Multiple addresses per user works
- [ ] Update address works
- [ ] Delete address works

### Error Handling
- [ ] Invalid login returns error
- [ ] Duplicate email rejected
- [ ] Non-existent product returns 404
- [ ] Insufficient stock returns error
- [ ] Invalid data returns validation error

### Data Integrity
- [ ] Database transactions work
- [ ] Foreign keys enforced
- [ ] Stock restored on order cancel
- [ ] Timestamps updated correctly
- [ ] Default values set correctly

---

## 🎯 Performance Testing

### Load Test Query
```bash
# Test 100 requests to get all products
for i in {1..100}; do
  curl -s http://localhost:8080/api/products > /dev/null
  echo "Request $i completed"
done
```

**Expected:** All requests complete within reasonable time

---

## 🔍 Debugging Tips

### View Application Logs
```bash
tail -f logs/ornaflora.log
```

### Check Database
```bash
# PostgreSQL
psql -U postgres -d ornaflora
SELECT * FROM users;
SELECT * FROM products;

# MySQL
mysql -u root -p -e "USE ornaflora; SELECT * FROM users;"
```

### Test with Verbose Output
```bash
curl -v http://localhost:8080/api/products
```

---

## 📚 Resources

- Full API Documentation: BACKEND_SETUP_GUIDE.md
- Architecture Overview: BACKEND_INDEX.md
- Setup Instructions: QUICK_START.md

---

**Happy Testing! 🧪**

**Next Steps:**
1. ✅ Run through all test scenarios
2. ✅ Verify database data is created correctly
3. ✅ Test error scenarios
4. ✅ Integrate with React frontend
5. ✅ Deploy to production

