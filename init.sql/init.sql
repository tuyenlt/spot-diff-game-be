CREATE DATABASE IF NOT EXISTS image_diff_game;
USE image_diff_game;

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    is_online BOOLEAN DEFAULT FALSE,
    is_in_game BOOLEAN DEFAULT FALSE,
    score INT DEFAULT 0,
    elo INT DEFAULT 1000,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create index for faster queries
CREATE INDEX idx_username ON users(username);
CREATE INDEX idx_elo ON users(elo DESC);
CREATE INDEX idx_online_status ON users(is_online);

-- Grant all privileges to appuser for connections from any host
GRANT ALL PRIVILEGES ON image_diff_game.* TO 'appuser'@'%';
FLUSH PRIVILEGES; 
