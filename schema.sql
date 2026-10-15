-- Smart City Management System Database Schema
-- Database: smart_city

CREATE DATABASE IF NOT EXISTS smart_city;
USE smart_city;

-- Drop child tables first to satisfy foreign key constraints
DROP TABLE IF EXISTS feedback;
DROP TABLE IF EXISTS service_requests;
DROP TABLE IF EXISTS env_readings;
DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS incidents;
DROP TABLE IF EXISTS traffic_zones;
DROP TABLE IF EXISTS resources;
DROP TABLE IF EXISTS users;

-- 1. Users Table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'PLANNER', 'RESIDENT') NOT NULL,
    zone VARCHAR(50)
);

-- 2. Resources Table
CREATE TABLE resources (
    resource_id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    zone VARCHAR(50) NOT NULL,
    allocated DOUBLE NOT NULL,
    consumed DOUBLE NOT NULL,
    unit VARCHAR(20) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. Traffic Zones Table
CREATE TABLE traffic_zones (
    zone_id INT AUTO_INCREMENT PRIMARY KEY,
    zone_name VARCHAR(50) NOT NULL UNIQUE,
    vehicle_count INT NOT NULL,
    avg_speed DOUBLE NOT NULL,
    congestion VARCHAR(20) NOT NULL
);

-- 4. Incidents Table
CREATE TABLE incidents (
    incident_id INT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    zone VARCHAR(50) NOT NULL,
    severity ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') NOT NULL,
    description TEXT,
    status ENUM('OPEN', 'IN_PROGRESS', 'RESOLVED') NOT NULL DEFAULT 'OPEN',
    reported_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Projects Table
CREATE TABLE projects (
    project_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    zone VARCHAR(50) NOT NULL,
    budget DECIMAL(14,2) NOT NULL,
    status ENUM('PROPOSED', 'IN_PROGRESS', 'COMPLETED', 'ON_HOLD') NOT NULL DEFAULT 'PROPOSED',
    start_date DATE,
    description TEXT
);

-- 6. Environmental Readings Table
CREATE TABLE env_readings (
    reading_id INT AUTO_INCREMENT PRIMARY KEY,
    zone VARCHAR(50) NOT NULL,
    aqi INT NOT NULL,
    noise_db DOUBLE NOT NULL,
    pm25 DOUBLE NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. Service Requests Table
CREATE TABLE service_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    resident_id INT NOT NULL,
    service_type VARCHAR(50) NOT NULL,
    details TEXT,
    status ENUM('PENDING', 'IN_PROGRESS', 'RESOLVED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (resident_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 8. Feedback Table
CREATE TABLE feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    resident_id INT NOT NULL,
    category VARCHAR(50) NOT NULL,
    message TEXT,
    submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (resident_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- ============================================================================
-- Seed Data
-- ============================================================================

-- Seed Users
INSERT INTO users (username, password, full_name, role, zone) VALUES
('admin', 'admin123', 'Aman Sharma', 'ADMIN', 'Central Hub'),
('planner', 'plan123', 'Priya Verma', 'PLANNER', 'Old Town'),
('resident', 'res123', 'Rohan Gupta', 'RESIDENT', 'Sector 12');

-- Seed Resources
INSERT INTO resources (type, zone, allocated, consumed, unit) VALUES
('Power', 'Sector 12', 50000.0, 42150.0, 'kWh'),
('Water', 'Sector 12', 120000.0, 98500.0, 'Litres'),
('Gas', 'Sector 12', 30000.0, 24100.0, 'm3'),
('Power', 'Old Town', 45000.0, 39800.0, 'kWh'),
('Water', 'Old Town', 95000.0, 88200.0, 'Litres'),
('Power', 'Industrial Belt', 180000.0, 165400.0, 'kWh'),
('Water', 'Industrial Belt', 250000.0, 210000.0, 'Litres'),
('Gas', 'Industrial Belt', 90000.0, 78300.0, 'm3');

-- Seed Traffic Zones
INSERT INTO traffic_zones (zone_name, vehicle_count, avg_speed, congestion) VALUES
('Sector 12', 450, 38.5, 'MODERATE'),
('Old Town', 820, 18.2, 'HEAVY'),
('Industrial Belt', 610, 42.0, 'LOW'),
('Central Hub', 1150, 12.4, 'CRITICAL');

-- Seed Incidents
INSERT INTO incidents (type, zone, severity, description, status) VALUES
('Power Outage', 'Sector 12', 'HIGH', 'Feeder line trip near Substation 4.', 'OPEN'),
('Water Pipeline Leak', 'Old Town', 'MEDIUM', 'Main distribution pipe leak causing low pressure.', 'IN_PROGRESS'),
('Chemical Fume Alert', 'Industrial Belt', 'CRITICAL', 'Elevated VOC sensor reading detected near Factory Unit 9.', 'OPEN'),
('Traffic Signal Failure', 'Central Hub', 'LOW', 'Blinking yellow signal at Crossroad 3.', 'RESOLVED');

-- Seed Projects
INSERT INTO projects (title, zone, budget, status, start_date, description) VALUES
('Solar Grid Expansion', 'Sector 12', 1500000.00, 'IN_PROGRESS', '2026-01-15', 'Installation of rooftop solar panels on municipal complexes.'),
('Heritage Corridor Revamp', 'Old Town', 2800000.00, 'PROPOSED', '2026-04-01', 'Pedestrianization and drainage restoration in market lanes.'),
('Heavy Vehicle Bypass Road', 'Industrial Belt', 5400000.00, 'IN_PROGRESS', '2025-11-10', 'Dedicated 4-lane bypass connecting Highway 8 to industrial sector.');

-- Seed Environment Readings
INSERT INTO env_readings (zone, aqi, noise_db, pm25) VALUES
('Sector 12', 88, 54.2, 32.1),
('Old Town', 142, 68.5, 58.4),
('Industrial Belt', 215, 76.1, 95.3),
('Central Hub', 165, 72.8, 67.9);

-- Seed Service Requests
INSERT INTO service_requests (resident_id, service_type, details, status) VALUES
(3, 'Garbage Clearance', 'Overflowing community bin outside Block C.', 'PENDING'),
(3, 'Streetlight Repair', 'Streetlight pole 14 non-functional for 3 days.', 'IN_PROGRESS');

-- Seed Feedback
INSERT INTO feedback (resident_id, category, message) VALUES
(3, 'Public Transport', 'Bus frequency on Route 12 is inadequate during evening peak hours.');
