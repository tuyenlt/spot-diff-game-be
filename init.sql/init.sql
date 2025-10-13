CREATE DATABASE IF NOT EXISTS image_diff_game;
USE image_diff_game;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    score INT DEFAULT 0,
    elo INT DEFAULT 1000,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Grant all privileges to appuser for connections from any host
GRANT ALL PRIVILEGES ON image_diff_game.* TO 'appuser'@'%';
FLUSH PRIVILEGES;
