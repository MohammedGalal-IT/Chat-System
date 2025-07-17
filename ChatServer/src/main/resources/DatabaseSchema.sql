DROP DATABASE IF EXISTS chat_system;

CREATE DATABASE chat_system;

Use chat_system;

CREATE TABLE `users` (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    is_online BOOLEAN DEFAULT FALSE,
    profile_picture VARCHAR(255),  -- مسار الصورة الشخصية
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE messages (
    message_id INT AUTO_INCREMENT PRIMARY KEY,
    sender_id INT NOT NULL,
    receiver_id INT NOT NULL,
    message_type ENUM('TEXT', 'IMAGE', 'VIDEO', 'AUDIO', 'FILE', 'EMOJI') NOT NULL,
    content TEXT,
    file_format ENUM('jpg', 'png', 'mp4', 'mp3', 'pdf', 'docx', 'xlsx', 'zip', 'rar', 'txt'),
    file_length BIGINT UNSIGNED,
    file_path_server VARCHAR(200), 
    file_path_client VARCHAR(200),
    is_read BOOLEAN DEFAULT FALSE,
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,

    FOREIGN KEY (sender_id) REFERENCES users(user_id),
    FOREIGN KEY (receiver_id) REFERENCES users(user_id)
);




