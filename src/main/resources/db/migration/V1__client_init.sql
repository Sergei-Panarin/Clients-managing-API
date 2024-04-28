CREATE TABLE IF NOT EXISTS client
(
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    job VARCHAR(255) DEFAULT 'unknown',
    position VARCHAR(255) DEFAULT 'unknown',
    gender VARCHAR(50) NOT NULL
);