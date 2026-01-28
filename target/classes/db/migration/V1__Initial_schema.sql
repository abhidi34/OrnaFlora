-- V1__Initial_schema.sql
-- Initial schema creation for OrnaFlora database

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    avatar_url VARCHAR(1000),
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role)
);

-- Create products table
CREATE TABLE IF NOT EXISTS products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    price DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0,
    image_urls LONGTEXT,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    INDEX idx_category (category),
    INDEX idx_name (name),
    INDEX idx_created_at (created_at)
);

-- Create addresses table
CREATE TABLE IF NOT EXISTS addresses (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    street VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    postal_code VARCHAR(20),
    country VARCHAR(100),
    phone VARCHAR(15),
    is_default BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_is_default (is_default)
);

-- Create orders table
CREATE TABLE IF NOT EXISTS orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    address_id BIGINT,
    status VARCHAR(50) NOT NULL DEFAULT 'CONFIRMED',
    total_amount DECIMAL(10, 2) NOT NULL,
    delivery_charge DECIMAL(10, 2),
    payment_method VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES addresses(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
);

-- Create order_items table
CREATE TABLE IF NOT EXISTS order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price_at_purchase DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    selected_image_url VARCHAR(1000),
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT,
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
);

-- Create cart_items table
CREATE TABLE IF NOT EXISTS cart_items (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT DEFAULT 1,
    selected_image_url VARCHAR(1000),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id)
);

-- Insert sample admin user
INSERT INTO users (email, password, name, phone, role, is_active, created_at, updated_at)
VALUES ('admin@ornaflora.com', 'admin123', 'Admin User', '+1234567890', 'ADMIN', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- Insert sample products
INSERT INTO products (name, description, category, price, stock, image_urls, is_active, created_at, updated_at)
VALUES 
('Rose Bouquet', 'Beautiful red roses perfect for any occasion', 'Flowers', 45.99, 50, '["https://via.placeholder.com/400x300?text=Rose+Bouquet"]', true, NOW(), NOW()),
('Sunflower Arrangement', 'Bright and cheerful sunflowers', 'Flowers', 35.99, 40, '["https://via.placeholder.com/400x300?text=Sunflower"]', true, NOW(), NOW()),
('Orchid Plant', 'Elegant purple orchids in a decorative pot', 'Plants', 55.99, 30, '["https://via.placeholder.com/400x300?text=Orchid"]', true, NOW(), NOW()),
('Succulent Mix', 'Low maintenance succulent collection', 'Plants', 25.99, 60, '["https://via.placeholder.com/400x300?text=Succulents"]', true, NOW(), NOW()),
('Potted Fern', 'Green fern plant for indoor decoration', 'Plants', 29.99, 45, '["https://via.placeholder.com/400x300?text=Fern"]', true, NOW(), NOW()),
('Tulip Bunch', 'Colorful spring tulips', 'Flowers', 39.99, 35, '["https://via.placeholder.com/400x300?text=Tulips"]', true, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();
