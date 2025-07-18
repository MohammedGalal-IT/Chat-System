# Chat System - JavaFX Client & Server

## 📌 Overview

A full-featured **Client-Server Chat Application** written in Java using JavaFX and Java Sockets.  
The system supports real-time text messaging, file sharing, and user authentication.

> 🔒 Built with clean architecture and layered packages for maintainability and scalability.

---

## 👨‍💻 Project Team

| Name                  | Role                           |
|-----------------------|--------------------------------|
| **Mohammed Galal** | System Architect, Back-End Developer, Database Designer |
| **Ahmed Al-Samadi** | Front-End Developer, UI/UX Designer |

---

## 📁 Project Structure

```
ChatSystem/
│
├── ChatServer/
│   ├── src/main/java/com/chatsystem/server/
│   │   ├── config/                # Configuration classes
│   │   ├── controller/            # Server-side controllers
│   │   ├── dataAccess/            # Database access layer
│   │   ├── model/                 # Entity classes
│   │   ├── network/               # Socket logic
│   │   ├── services/              # Business logic
│   │   ├── util/                  # Utility functions
│   │   └── ServerMain.java        # Entry point
│   ├── src/main/resources/
│   │   └── database_schema.sql    # SQL schema to initialize the database
│   ├── uploads/                   # Directory to store received files
│   └── pom.xml                    # Maven config
│
├── ChatClient/
│   ├── src/main/java/com/chatsystem/client/
│   │   ├── controller/            # Client-side controllers
│   │   ├── model/                 # DTOs and local models
│   │   ├── network/               # Networking with server
│   │   ├── services/              # Client logic and helpers
│   │   ├── util/                  # Shared utilities
│   │   └── ClientMain.java        # Entry point
│   ├── src/main/com/chatsystem/client/resources/
│   │   └── assets/                # App Assets
│   │   └── view/                  # Javafx FXML files
│   └── pom.xml                    # Maven config
```

---

## 🛠️ Technologies Used

- **Java 22**
- **JavaFX 22**
- **Java Sockets (TCP)**
- **MySQL**
- **Maven**
- **GSON Library**
- **Bcrypt Library**

---

## 🧰 Features

- ✅ Real-time chat via Sockets
- ✅ GUI using JavaFX
- ✅ Supports text, image, video, audio, and file messaging
- ✅ Secure login with hashed credentials
- ✅ Multi-threaded server
- ✅ Designed using layered architecture (Controller, Service, DAO, Network, etc.)
- ✅ Built with Maven for dependency management
- ✅ Modular clean architecture
- ✅ Transferring Data as json.

---

## ⚙️ Prerequisites

Before running the system, make sure the following tools are installed:

### Server-Side Requirements

- **JDK 17**
- **MySQL Server**
- **Maven**
- **XAMPP** or any other MySQL-compatible tool (e.g., WAMP)
- Internet connection (only required on the first run for Maven dependency resolution)

### Client-Side Requirements

- **JDK 22**
- **JavaFX SDK**
- **Maven**
- Internet connection (only required on the first run for Maven dependency resolution)

## 🗃️ Database Setup

1. Open the file:  
   `ChatServer/src/main/resources/database_schema.sql`

2. Execute the script in your MySQL environment.

---

## 🚀 Getting Started

### `Server Setup`

1. **Create the Database**  
   - Use the SQL schema provided in the `ChatServer/src/main/resources/database_schema.sql` folder inside the server module.

2. **Run MySQL Server**  
   - Ensure your MySQL service is running via XAMPP/WAMP or another tool.

3. **Start the Server Application**  
   - Run `ServerMain.java` located in:  
     `src/main/java/server/ServerMain.java`


### `Client Setup`

1. **Ensure the Server is Running**  
   - Start the server before launching the client.

2. **Start the Client Application**  
   - Run `ClientMain.java` located in:  
     `src/main/java/client/ClientMain.java`

3. **Login or Register**  
   - Use existing credentials to log in, or create a new account.

4. **Start Chatting**  
   - Select an online user from the contacts list and start messaging.

---

## 📌 Important Notes

- Messaging works **only between online users**.
- The **client and server are independent modules** but are placed in the same repository for simplicity.
- The system uses **port 8000 by default**. Ensure this port is available on your machine.
- JavaFX libraries must be configured correctly for the client application to run.
- The internet is required on the first run only for Maven to download dependencies.


## 🚀 Running the Application

### 🖥 Server

```bash
cd ChatServer
java -jar ChatServer.jar
```

Alternatively, run the class:
```
com.chatsystem.server.ServerMain
```

### 💻 Client

```bash
cd ChatClient
java --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" --add-modules javafx controls,javafx.fxml,javafx.web,javafx.graphics -jar ChatClient.jar
```

Or run:
```
com.chatsystem.client.ClientMain
```

---

## 🎯 Contributing

We welcome contributions!

- Fork the repository
- Create a new branch (`feature/your-feature`)
- Commit your changes
- Submit a Pull Request

Please follow clean code practices and write descriptive commit messages.

---

## 📞 Contact

For questions, suggestions, or collaboration opportunities:

- 📧 **Mohammed Galal Ahmed** — [MohammedGalal7777@hotmail.com](mailto:MohammedGalal7777@hotmail.com)  
- 📧 **Ahmed Al-Samadi** — [ahmedalsamadi.dev@gmail.com](mailto:ahmedalsamadi.dev@gmail.com)

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).
