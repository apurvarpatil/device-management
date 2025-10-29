# Device Management Application

This application provides a comprehensive solution for ***persisting and managing device resources***. 
It includes features for storing device information, retrieving device details, updating device configurations,
and deleting devices from the system.

## Features
- **CRUD Operations**: Create, Read, Update, and Delete device records.

## Prerequisites
- Docker Engine (or Docker Desktop)
    - Windows/Mac: Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)
    - Linux: Install [Docker Engine](https://docs.docker.com/engine/install/)
- Docker Compose
- Java 21+ (for development only)
- Gradle 8+ (for development only)

## Quick Start
1. Install Docker if you haven't already:
    - Windows/Mac: Download and install Docker Desktop
    - Linux: Run:
      ```bash
      curl -fsSL https://get.docker.com -o get-docker.sh
      sudo sh get-docker.sh
      ```

2. Clone and run:
   ```bash
   git clone https://github.com/apurvarpatil/device-management.git
   cd device-management
   docker-compose up -d
