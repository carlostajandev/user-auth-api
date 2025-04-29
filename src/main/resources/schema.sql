CREATE TABLE IF NOT EXISTS users (
    id BINARY(16) PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255),
    active BOOLEAN DEFAULT FALSE,
    activation_token VARCHAR(255),
    token_expiration TIMESTAMP
    );
