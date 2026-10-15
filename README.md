# Smart City Management System

A Jakarta EE 10 web application for municipal operations, urban planning, citizen services, and simulated IoT sensor network telemetry.

## Prerequisites
- Java 17+
- Apache Tomcat 10+
- MySQL 8.0+

## Setup
Run the deployment script matching your operating system:
- **macOS / Linux:** `./deploy.sh`
- **Windows:** `deploy.bat`

## Default Logins
- **City Admin:** `admin` / `admin123`
- **Urban Planner:** `planner` / `plan123`
- **Resident:** `resident` / `res123`

## Architecture Note
The presentation layer was migrated from Java Swing desktop frames to Jakarta EE servlets and JSPs targeting Apache Tomcat 10 for the web application deployment track; all underlying business entities, generic JDBC persistence layers, multi-threaded IoT sensor simulation daemons, and synchronized memory data stores in the `model`, `dao`, and `service` packages remain identical and completely preserved.
