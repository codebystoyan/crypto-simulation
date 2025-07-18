CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    balance DECIMAL(13,2) DEFAULT 10000.00
);

CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    crypto_symbol VARCHAR(10) NOT NULL,
    amount DECIMAL(18,8) NOT NULL,
    fiat_amount DECIMAL(13,2) NOT NULL,
    type ENUM('BUY', 'SELL'),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,
    INDEX idx_user_id (user_id)
);

CREATE TABLE IF NOT EXISTS holdings (
    user_id INT NOT NULL,
    crypto_symbol VARCHAR(10) NOT NULL,
    amount DECIMAL(18,8) NOT NULL DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, crypto_symbol),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);