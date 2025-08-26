# 🎵 Hertz Music Server

<div align="center">

**A robust TCP server for the Hertz music streaming application**

[![Java](https://img.shields.io/badge/Java-16+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red.svg)](https://maven.apache.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-4.4+-green.svg)](https://www.mongodb.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

</div>

---

## 🔗 Repositories

- **Frontend (Flutter app)**: [github.com/ArshaFrn/MusicPlayer](https://github.com/ArshaFrn/MusicPlayer)
- **Backend (this repo)**: [github.com/ShayanDZ/MusicPlayerServer](https://github.com/ShayanDZ/MusicPlayerServer)

---

## 🚀 Overview

The Hertz Music Server is a high-performance TCP server built in Java that powers the Hertz music streaming application. It provides real-time communication, secure user authentication, music file management, and comprehensive data persistence using MongoDB.

### 🌟 Key Features

- **Real-time TCP Communication** with JSON-based protocol
- **Secure User Authentication** with bcrypt password hashing
- **Music File Management** with Base64 encoding/decoding
- **MongoDB Integration** for persistent data storage
- **Email Services** for password reset functionality
- **Admin System** with role-based access control
- **Multi-threaded Architecture** for concurrent connections
- **Comprehensive Error Handling** and logging

---

## 🏗️ Architecture

### System Components

```
Server/
├── src/main/java/com/hertz/
│   ├── ServerApplication.java      # Main server entry point
│   ├── handler/                    # Request handlers
│   │   ├── ClientHandler.java      # Main client request processor
│   │   └── ForgotPasswordHandler.java
│   ├── model/                      # Data models
│   │   ├── User.java
│   │   ├── Music.java
│   │   ├── Playlist.java
│   │   ├── Admin.java
│   │   └── Response.java
│   ├── repository/                 # Data access layer
│   │   ├── UserRepository.java
│   │   ├── MusicRepository.java
│   │   ├── AdminRepository.java
│   │   └── ResetCodeRepository.java
│   └── utils/                      # Utility classes
│       ├── DatabaseConnection.java
│       ├── PasswordUtils.java
│       ├── EmailUtils.java
│       └── ResponseUtils.java
├── src/main/resources/
│   ├── application.properties      # Configuration
│   └── data/music.json            # Sample data
└── pom.xml                        # Maven configuration
```

---

## 🛠️ Technology Stack

### Core Technologies
- **Java 16+** - Primary programming language
- **Maven** - Build and dependency management
- **MongoDB** - NoSQL database
- **GSON** - JSON parsing and serialization
- **JavaMail** - Email functionality
- **JUnit 5** - Unit testing framework

### Dependencies

```xml
<dependencies>
    <!-- JSON Processing -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
    
    <!-- Database -->
    <dependency>
        <groupId>org.mongodb</groupId>
        <artifactId>mongo-java-driver</artifactId>
        <version>3.12.10</version>
    </dependency>
    
    <!-- Email Services -->
    <dependency>
        <groupId>javax.mail</groupId>
        <artifactId>javax.mail-api</artifactId>
        <version>1.6.2</version>
    </dependency>
    
    <!-- Password Hashing -->
    <dependency>
        <groupId>de.svenkubiak</groupId>
        <artifactId>jBCrypt</artifactId>
        <version>0.4</version>
    </dependency>
    
    <!-- Testing -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.8.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## 🚀 Getting Started

### Prerequisites

- **Java JDK 16** or higher
- **Maven 3.8** or higher
- **MongoDB 4.4** or higher
- **Git** for version control

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/ShayanDZ/MusicPlayerServer.git
   cd MusicPlayerServer
   ```

2. **Configure MongoDB**
   - Install and start MongoDB service
   - Create database: `hertz_music`
   - Configure connection in `application.properties`

3. **Configure Email Settings** (for password reset)
   ```properties
   # Email configuration
   email.host=smtp.gmail.com
   email.port=587
   email.username=your-email@gmail.com
   email.password=your-app-password
   ```

4. **Build the project**
   ```bash
   mvn clean compile
   ```

5. **Run the server**
   ```bash
   mvn exec:java -Dexec.mainClass="com.hertz.ServerApplication"
   ```

### Development Setup

**Compile and Run:**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.hertz.ServerApplication"
```

**Run Tests:**
```bash
mvn test
```

**Package JAR:**
```bash
mvn clean package
```

---

## 📡 API Protocol

### Communication Format

The server uses a JSON-based protocol over TCP connections:

```json
{
  "Request": "requestType",
  "Payload": {
    // Request-specific data
  }
}
```

### Response Format

```json
{
  "status": "success/error",
  "message": "Response message",
  "Payload": {
    // Response data
  }
}
```

---

## 🔐 Authentication System

### User Registration
- **Username validation** (minimum 8 characters, alphanumeric + underscore)
- **Email validation** and uniqueness check
- **Password hashing** using bcrypt
- **Registration date** tracking

### User Login
- **Secure password verification** with bcrypt
- **Session management** with user data return
- **Error handling** for invalid credentials

### Password Reset
- **Email-based reset** with verification codes
- **Temporary code generation** and validation
- **Secure password update** process

---

## 🎵 Music Management

### File Operations
- **Base64 encoding/decoding** for file transfer
- **Metadata extraction** and storage
- **File validation** and format checking
- **Storage optimization** with compression

### Music Features
- **Public/Private** music sharing
- **Like/Dislike** functionality
- **Playlist management** with CRUD operations
- **Recently played** tracking
- **Search and filtering** capabilities

### Data Models

**Music Model:**
```java
public class Music {
    private int id;
    private String title;
    private Artist artist;
    private String genre;
    private int durationInSeconds;
    private LocalDateTime releaseDate;
    private String filePath;
    private String extension;
    private int likeCount;
    private boolean isPublic;
    // ... getters and setters
}
```

**User Model:**
```java
public class User {
    private int id;
    private String username;
    private String email;
    private String hashedPassword;
    private String fullName;
    private LocalDateTime registrationDate;
    private List<Integer> tracks;
    private List<Integer> likedSongs;
    private List<Playlist> playlists;
    // ... getters and setters
}
```

---

## 👨‍💼 Admin System

### Admin Types
- **Super Admin** - Full system access
- **Full Admin** - User and music management
- **Limited Admin** - Basic monitoring

### Admin Capabilities
- **User management** (view, delete users)
- **Music management** (view, delete music)
- **System monitoring** and statistics
- **Content moderation** and approval

---

## 🗄️ Database Schema

### Collections

**Users Collection:**
```json
{
  "_id": "ObjectId",
  "username": "string",
  "email": "string",
  "hashedPassword": "string",
  "fullName": "string",
  "registrationDate": "ISODate",
  "profileImageBase64": "string",
  "tracks": ["musicId"],
  "likedSongs": ["musicId"],
  "playlists": ["playlistId"]
}
```

**Music Collection:**
```json
{
  "_id": "ObjectId",
  "title": "string",
  "artist": "string",
  "genre": "string",
  "durationInSeconds": "number",
  "releaseDate": "ISODate",
  "filePath": "string",
  "extension": "string",
  "likeCount": "number",
  "isPublic": "boolean",
  "uploadedBy": "userId"
}
```

**Admins Collection:**
```json
{
  "_id": "ObjectId",
  "username": "string",
  "hashedPassword": "string",
  "capability": "enum",
  "createdDate": "ISODate"
}
```

---

## 🔧 Configuration

### Application Properties

```properties
# Server Configuration
server.port=12345
server.host=localhost

# Database Configuration
mongodb.uri=mongodb://localhost:27017
mongodb.database=hertz_music

# Email Configuration
email.host=smtp.gmail.com
email.port=587
email.username=your-email@gmail.com
email.password=your-app-password
email.enabled=true

# Security Configuration
bcrypt.rounds=12
session.timeout=3600
```

### Environment Variables

```bash
export MONGODB_URI=mongodb://localhost:27017
export EMAIL_HOST=smtp.gmail.com
export EMAIL_USERNAME=your-email@gmail.com
export EMAIL_PASSWORD=your-app-password
```

---

## 🧪 Testing

### Unit Tests

Run all tests:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=PasswordUtilsTest
```

### Test Coverage

The project includes comprehensive tests for:
- **Password utilities** (hashing, verification)
- **Email utilities** (sending, validation)
- **Date parsing** utilities
- **Response formatting** utilities

---

## 📊 Performance & Scalability

### Optimization Features
- **Connection pooling** for database operations
- **Multi-threading** for concurrent client handling
- **Efficient JSON parsing** with GSON
- **Memory management** for large file transfers
- **Caching strategies** for frequently accessed data

### Monitoring
- **Connection logging** and statistics
- **Error tracking** and reporting
- **Performance metrics** collection
- **Resource usage** monitoring

---

## 🔒 Security Features

### Data Protection
- **Password hashing** with bcrypt
- **Input validation** and sanitization
- **SQL injection prevention** (MongoDB)
- **File upload validation**
- **Session management**

### Network Security
- **TCP connection encryption** (configurable)
- **Request validation** and rate limiting
- **Error message sanitization**
- **Access control** and authorization

---

## 🐛 Troubleshooting

### Common Issues

**1. MongoDB Connection Failed**
```bash
# Check MongoDB service
sudo systemctl status mongod

# Verify connection string
mongo mongodb://localhost:27017
```

**2. Email Service Issues**
- Verify SMTP credentials
- Check firewall settings
- Enable "Less secure app access" for Gmail
- Use a safe VPN if not availbe in your country :)

**3. Port Already in Use**
```bash
# Find process using port
netstat -tulpn | grep 12345

# Kill process
kill -9 <PID>
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- **MongoDB Team** for the excellent database
- **Google GSON** for JSON processing
- **BCrypt** for secure password hashing
- **JavaMail** for email functionality

---

<div align="center">

**Built with ❤️ and Java**

[![Java](https://img.shields.io/badge/Java-16+-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8+-red.svg)](https://maven.apache.org/)

</div>
