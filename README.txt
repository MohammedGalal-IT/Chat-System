# Chat System - JavaFX Client & Server

## 📌 Overview

A full-featured **Client-Server Chat Application** written in Java using JavaFX and Java Sockets.  
The system supports real-time text messaging, file sharing, and user authentication.

> 🔒 Built with clean architecture and layered packages for maintainability and scalability.

---

## 🛠️ Technologies Used

- Java 22
- JavaFX 22
- Java Sockets (TCP)
- MySQL
- Maven
- GSON Library
- Bcrypt Library

---

## 🧰 Features

- Real-time chat via Sockets  
- GUI using JavaFX  
- Supports text, image, video, audio, and file messaging  
- Secure login with hashed credentials  
- Multi-threaded server  
- Designed using layered architecture  
- Built with Maven for dependency management  
- Modular clean architecture  
- Transferring Data as JSON  

---

## ⚙️ Prerequisites

Server-Side Requirements:
- JDK 17
- MySQL Server
- Maven
- XAMPP/WAMP
- Internet connection (first run only)

Client-Side Requirements:
- JDK 22
- JavaFX SDK
- Maven
- Internet connection (first run only)

---

## 🗃️ Database Setup

1. Open: ChatServer/src/main/resources/database_schema.sql  
2. Execute it in your MySQL environment.

---

## 🚀 Getting Started

Server:
- Run MySQL
- Execute SQL Schema
- Start ServerMain.java

Client:
- Start after server
- Run ClientMain.java
- Login/Register and start chatting

---

## 📌 Notes

- Messaging works only between online users.
- Client and server are independent.
- Uses port 8000.
- JavaFX must be configured properly.
- Internet is needed only the first time for Maven.

---


## 📁 Project Structure

ChatSystem/
|
├── ChatServer/
│   ├── src/main/java/com/chatsystem/server/
│   │   ├── config/                - Configuration classes
│   │   ├── controller/            - Server-side controllers
│   │   ├── dataAccess/            - Database access layer
│   │   ├── model/                 - Entity classes
│   │   ├── network/               - Socket logic
│   │   ├── services/              - Business logic
│   │   ├── util/                  - Utility functions
│   │   └── ServerMain.java        - Entry point
│   ├── src/main/resources/
│   │   └── database_schema.sql    - SQL schema to initialize the database
│   ├── uploads/                   - Directory to store received files
│   └── pom.xml                    - Maven config

├── ChatClient/
│   ├── src/main/java/com/chatsystem/client/
│   │   ├── controller/            - Client-side controllers
│   │   ├── model/                 - DTOs and local models
│   │   ├── network/               - Networking with server
│   │   ├── services/              - Client logic and helpers
│   │   ├── util/                  - Shared utilities
│   │   └── ClientMain.java        - Entry point
│   ├── src/main/com/chatsystem/client/resources/
│   │   └── assets/                - App icons and media
│   │   └── view/                  - JavaFX Views
│   └── pom.xml                    - Maven config

---


## 🏃 Running

Server:
- cd ChatServer
- java -jar ChatServer.jar
- or run com.chatsystem.server.ServerMain

Client:
- cd ChatClient
- java --module-path "path_to_javafx_lib" --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.graphics -jar ChatClient.jar
- or run com.chatsystem.client.ClientMain

---

## 👨‍💻 Project Team

Name: Mohammed Galal — System Architect, Back-End Developer, Database Designer  
Name: Ahmed Al-Samadi — Front-End Developer, UI/UX Designer

---

Repository:
https://github.com/MohammedGalal-IT/Chat-System

Developers:
- Mohammed Galal: https://github.com/MohammedGalal-IT
- Ahmed Al-Samadi: https://github.com/Al-Samadi

---

## 📞 Contact

Mohammed Galal: MohammedGalal7777@hotmail.com  
Ahmed Al-Samadi: ahmedalsamadi.dev@gmail.com

---

## License

MIT License