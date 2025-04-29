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
