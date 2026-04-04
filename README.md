# Video Game Store (Spring Boot + Thymeleaf + HTMX + JWT REST API)

A web application for browsing and purchasing video games (catalog, filters, cart, orders) with PayPal plus a separate REST API protected with JWT authentication. The REST API also exposes an admin-only CSV export endpoint for authentication logs.

## Table of Contents
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Running the Application](#running-the-application)
- [REST API (JWT)](#rest-api-jwt)
  - [1) Login](#1-login)
  - [2) List games](#2-list-games)
  - [3) Export admin logs as CSV](#3-export-admin-logs-as-csv)
- [Security & Roles](#security--roles)
- [Authentication Logging](#authentication-logging)
---

## Tech Stack
- Java, Spring Boot
- Spring MVC + Thymeleaf (server-side rendering)
- HTMX (AJAX interactions without full page reloads: filtering, pagination)
- Spring Security
  - Web UI: form login (session-based)
  - REST API: stateless JWT (Bearer token)
- JPA/Hibernate (H2 or another DB depending on configuration)
- Bootstrap 5 (UI)

---

## Features

### Web Application
- Browse game catalog
- Filter by categories/genres (HTMX)
- Pagination (HTMX)
- Game details page
- Shopping cart
- Orders (authenticated users)
- Reviews:
  - create/update your review
  - delete your review

### REST API (separate from Web UI)
- `POST /api/login` — returns JWT access token (+ refresh token)
- `GET /api/games` — returns list of games (protected)
- `GET /api/admin/logs.csv` — exports authentication logs as CSV (ADMIN only)

---

## Running the Application
1. Clone the repository and open it in your IDE (IntelliJ / VS Code).
2. Start the Spring Boot application.
3. Open the web UI:
   - `http://localhost:8081/games`

> The port can be different depending on your `application.properties`.

---

## REST API (JWT)

### 1) Login
**Endpoint:** `POST /api/login`  
**Body:** JSON with username/password.

```bash
curl -s -X POST "http://localhost:8081/api/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"ADMIN_PASSWORD"}'
```

Example response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9....",
  "refreshToken": "..."
}
```

If you have `jq`, extract the token into a variable:

```bash
TOKEN=$(curl -s -X POST "http://localhost:8081/api/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"ADMIN_PASSWORD"}' | jq -r .accessToken)
```

---

### 2) List games
**Endpoint:** `GET /api/games`  
**Auth:** `Authorization: Bearer <accessToken>`

```bash
curl -s "http://localhost:8081/api/games" \
  -H "Authorization: Bearer $TOKEN"
```

---

### 3) Export admin logs as CSV
**Endpoint:** `GET /api/admin/logs.csv`  
**Auth:** `Authorization: Bearer <accessToken>`  
**Role required:** `ADMIN`

```bash
curl -L "http://localhost:8081/api/admin/logs.csv" \
  -H "Authorization: Bearer $TOKEN" \
  -o auth-logs.csv

head -n 5 auth-logs.csv
```

If you get a JSON error inside the `.csv` file, check the HTTP status without saving:

```bash
curl -i "http://localhost:8081/api/admin/logs.csv" \
  -H "Authorization: Bearer $TOKEN"
```

---

## Security & Roles
- Web UI uses Spring Security form login and sessions.
- REST API is stateless and uses JWT Bearer tokens.
- `/api/admin/**` endpoints require the `ADMIN` role.

---

## Authentication Logging
Authentication events are persisted as log entries (successful and failed logins).  
An admin can export these logs via the REST API CSV export endpoint.

> If you implement REST login manually (without Spring’s `AuthenticationManager`), remember to log API login attempts explicitly or integrate the API login into Spring Security to emit auth events automatically.
