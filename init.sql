-- Initial database setup
CREATE DATABASE IF NOT EXISTS dealerdb;

-- Create tables (optional, as JPA will handle this with ddl-auto=update)
-- This is just for reference and initial setup

-- Dealers table
CREATE TABLE IF NOT EXISTS dealers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    subscription_type VARCHAR(20) NOT NULL CHECK (subscription_type IN ('BASIC', 'PREMIUM'))
);

-- Vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    id BIGSERIAL PRIMARY KEY,
    dealer_id BIGINT NOT NULL,
    model VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('AVAILABLE', 'SOLD')),
    FOREIGN KEY (dealer_id) REFERENCES dealers(id) ON DELETE CASCADE
);

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    dealer_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    method VARCHAR(20) NOT NULL CHECK (method IN ('UPI', 'CARD', 'NET_BANKING')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDING', 'SUCCESS', 'FAILED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (dealer_id) REFERENCES dealers(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_vehicles_dealer_id ON vehicles(dealer_id);
CREATE INDEX idx_payments_dealer_id ON payments(dealer_id);
CREATE INDEX idx_dealers_subscription_type ON dealers(subscription_type);