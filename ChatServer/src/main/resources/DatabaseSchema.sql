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
    file_path VARCHAR(200),  -- إذا كانت الرسالة تحتوي مرفقًا
    is_read BOOLEAN DEFAULT FALSE,
    sent_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    is_deleted BOOLEAN DEFAULT FALSE,

    FOREIGN KEY (sender_id) REFERENCES users(user_id),
    FOREIGN KEY (receiver_id) REFERENCES users(user_id)
);


CREATE TABLE attachments (
    attachment_id INT AUTO_INCREMENT PRIMARY KEY,
    message_id INT NOT NULL,
    file_type VARCHAR(20),        -- مثال: mp3, mp4, png
    file_size INT,                -- بالكيلوبايت
    original_name VARCHAR(255),   -- الاسم الأصلي للملف
    server_path VARCHAR(255),     -- المسار الكامل في الخادم

    FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE ON UPDATE CASCADE
);




