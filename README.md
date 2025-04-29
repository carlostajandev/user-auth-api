
# 🛡️ User Authentication API

A Spring Boot-based authentication system with features like user registration, email-based account activation, JWT login, and password reset. This application integrates with MySQL and uses Mailjet to send transactional emails.

## 📦 Features

- User Registration with Email Verification
- JWT-Based Authentication
- Password Reset via Email
- Account Activation Workflow
- Token Expiry & Validation Handling
- Swagger-UI API Documentation

---

## 🚀 Requirements

- Java 17+
- Maven 3.6+
- MySQL 8+
- Mailjet Account (SMTP credentials)
- IDE (IntelliJ, VSCode, etc.)

---

## ⚙️ Configuration

### 1. MySQL Setup

Create a database:

```sql
CREATE DATABASE userauthdb;
```

Update your credentials in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/userauthdb?serverTimezone=UTC
    username: root
    password: your_password
```

### 2. Mailjet SMTP Configuration

Configure your Mailjet SMTP credentials:

```yaml
spring:
  mail:
    host: in-v3.mailjet.com
    port: 587
    username: YOUR_MAILJET_SMTP_USERNAME
    password: YOUR_MAILJET_SMTP_PASSWORD
    properties:
      mail:
        from: your_email@example.com
```

### 3. JWT Configuration

Ensure your JWT secret is secure:

```yaml
jwt:
  secret: "YourSuperSecureAndLongJWTSecretKey123456"
  expiration: 86400
```

---

## 🧱 Database Schema

Initialize the `users` table manually (if not using `ddl-auto: update`):

```sql
-- Users table to store account details
CREATE TABLE IF NOT EXISTS users (
                                     id BINARY(16) PRIMARY KEY,                            -- UUID as binary for compact storage
    full_name VARCHAR(255) NOT NULL,                      -- Full name of the user
    email VARCHAR(255) NOT NULL UNIQUE,                   -- Unique email for login and identification
    password VARCHAR(255),                                -- Hashed password
    active BOOLEAN DEFAULT FALSE,                         -- Whether the user has activated their account
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,       -- Timestamp when the user was created
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP        -- Timestamp when the user was last updated
    ON UPDATE CURRENT_TIMESTAMP
    );

-- Activation tokens table for email-based account activation
CREATE TABLE IF NOT EXISTS activation_tokens (
                                                 id BINARY(16) PRIMARY KEY,                            -- UUID as binary
    user_id BINARY(16) NOT NULL,                          -- References the user
    token VARCHAR(255) NOT NULL UNIQUE,                   -- Activation token
    used BOOLEAN DEFAULT FALSE,                           -- Whether the token has already been used
    expiration TIMESTAMP NOT NULL,                        -- Expiry time of the token
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,       -- Timestamp when the token was generated
    FOREIGN KEY (user_id) REFERENCES users(id)            -- Foreign key to users table
    ON DELETE CASCADE
    );

-- Password reset tokens for "forgot password" flow
CREATE TABLE IF NOT EXISTS password_reset_tokens (
                                                     id BINARY(16) PRIMARY KEY,                            -- UUID as binary
    user_id BINARY(16) NOT NULL,                          -- References the user
    token VARCHAR(255) NOT NULL UNIQUE,                   -- Unique password reset token
    used BOOLEAN DEFAULT FALSE,                           -- Whether the token was already used
    expiration TIMESTAMP NOT NULL,                        -- Token expiration timestamp
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,       -- Token creation time
    FOREIGN KEY (user_id) REFERENCES users(id)            -- Foreign key to users table
    ON DELETE CASCADE
    );

```

Other entities (activation/reset tokens) are handled via JPA.

---

## ▶️ Running the Application

Run the application using Maven:

```bash
./mvnw spring-boot:run
```

The app will start at:

```
http://localhost:8080
```

---

## 📬 Email Features

- Upon registration, an activation email is sent.
- Forgot password flow sends a reset link (valid for 2 hours).
- Tokens are one-time-use and time-bound.

---

## 🔐 Authentication Flow

| Endpoint                        | Method | Description                                  |
|--------------------------------|--------|----------------------------------------------|
| `/api/auth/register`           | POST   | Register a new user                          |
| `/api/auth/activate`           | GET    | Validate account activation token            |
| `/api/auth/activate-pass`      | POST   | Set password and activate user               |
| `/api/auth/login`              | POST   | Authenticate and receive JWT                 |
| `/api/auth/forgot`             | POST   | Send password reset email                    |
| `/api/auth/reset-validate`     | GET    | Validate password reset token                |
| `/api/auth/reset-pass`         | POST   | Reset password with valid token              |

---

## 📘 Swagger API Documentation

You can access the full Swagger-UI interface here:

```
http://localhost:8080/swagger-ui.html
```

This contains detailed documentation for every endpoint under `/api/auth`.

---

## 🧪 Example cURL Requests

**Register User**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"John Doe","email":"john@example.com"}'
```

**Login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","password":"your_password"}'
```

---

## 🛠 Tech Stack

- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- JWT (jjwt)
- Mailjet SMTP
- Swagger / OpenAPI 3

---

## 📄 License

This project is licensed under the MIT License.
