~~# Device Management Application

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
   docker-compose up -d~~


## Future enhancements

- Authentication & Authorization (JWT + RBAC)
    - Roles: `ADMIN`, `OWNER`, `USER`.
    - Expected behavior:
        - `ADMIN` required to delete devices.
        - `OWNER` or `ADMIN` allowed to update a device.
        - `USER` can list and view devices.
    - Implementation options:
        - Add an `AuthorizationService` and inject it into `DeviceService` (check `canModifyDevice(...)` / `hasRole(...)`).
        - Integrate Spring Security + JWT and enforce method-level security (e.g., `@PreAuthorize("hasRole('ADMIN')")`).
    - Security notes:
        - Use `spring-boot-starter-security` and a JWT library (e.g., `com.auth0:java-jwt`).
        - Store `jwt.secret` securely (env var / vault); rotate regularly.
        - Prefer stateless sessions (JWT) for APIs.

- Swagger / OpenAPI
    - Recommended: migrate to `springdoc-openapi` for Spring Boot 3 / Java 21.
        - Add `implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:<version>'` to `build.gradle`.
        - Create a minimal `OpenApiConfig` and set `springdoc.swagger-ui.path` / `springdoc.api-docs.path` in `application.properties`.
        - Remove any `springfox` dependencies and config classes (conflicts are common).
    - If forced to keep old `springfox`, downgrade to Spring Boot 2\.7.x and add `io.springfox:springfox-boot-starter:3.0.0` with `spring.mvc.pathmatch.matching-strategy=ant_path_matcher`.

- Troubleshooting `/v3/api-docs` 500 errors
    - Inspect the server logs / stacktrace; enable debug: `logging.level.org.springdoc=DEBUG`.
    - Common causes & fixes:
        - Conflicting springfox + springdoc dependencies — remove one.
        - GroupedOpenApi scanning wrong package(s) — set `packagesToScan(...)` or `pathsToMatch("/**")`.
        - DTOs with circular refs / unsupported types cause model resolution failures — simplify or add annotations to break cycles.
        - Context path mismatch — verify `server.servlet.context-path` and request paths.
    - If 500 persists, capture the root cause line from logs and address the specific exception.

- Testing & migration notes
    - Authz unit tests exist as disabled placeholders: `src/test/java/com/app/device_management/service/DeviceServiceAuthFutureTest.java`.
    - To enable:
        1. Implement `AuthorizationService` (or configure Spring Security context).
        2. Mock/stub authorization checks in tests (`authorizationService.hasRole(...)` / `canModifyDevice(...)`).
        3. Update `DeviceService` to consult the authorization layer and throw `com.app.device_management.exception.AuthorizationException` (or `AccessDeniedException`) on denial.
    - Add integration tests to validate end-to-end auth once security is wired.

- Quick migration checklist
    1. Decide: upgrade to `springdoc` (preferred) or downgrade Spring Boot for `springfox`.
    2. Update `build.gradle` dependencies accordingly.
    3. Add `OpenApiConfig` + `SecurityConfig` + `JwtService` (if using JWT).
    4. Remove old/conflicting config classes and restart; verify `/v3/api-docs` and `/swagger-ui/index.html`.
