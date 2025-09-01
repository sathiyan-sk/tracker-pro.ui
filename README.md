# Tracker Pro - Role-Based Authentication System

A comprehensive Spring Boot application with role-based authentication, user management, and a modern responsive web interface.

## 🚀 Features

- **Role-Based Authentication**: Admin and User roles with different access levels
- **User Registration & Login**: Complete user registration workflow with validation
- **Responsive UI**: Modern, mobile-friendly interface using HTML5, CSS3, and JavaScript
- **Database Support**: H2 in-memory database (easily switchable to MySQL)
- **Security**: Spring Security integration with BCrypt password encryption
- **REST API**: Comprehensive API endpoints for authentication and user management
- **Real-time Validation**: Frontend validation with real-time feedback
- **Default Admin**: Pre-configured admin account for immediate access

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.2.0, Java 17
- **Security**: Spring Security 6.2.0
- **Database**: H2 (in-memory), JPA/Hibernate
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript
- **Build Tool**: Maven
- **Authentication**: BCrypt password encoding
- **Session Management**: HTTP Session-based authentication

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.8+
- Git

## 🚀 Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd tracker-pro
   ```

2. **Build the application**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Main Application: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
   - Database URL: `jdbc:h2:mem:trackerdb`
   - Username: `sa`
   - Password: `password`

## 🔐 Default Credentials

**Admin Account:**
- Email: `admin@trackerpro.com`
- Password: `admin123`
- Role: ADMIN

## 📱 User Interface

The application provides several pages:

1. **Landing Page** (`/`) - Main login page with student portal access
2. **Student Login** (`/login`) - Dedicated student login interface
3. **Registration** (`/register`) - User registration with comprehensive form validation
4. **Dashboard** (`/dashboard`) - Protected dashboard for authenticated users
5. **Success Pages** - Registration and login success confirmations

## 🔧 API Endpoints

### Authentication APIs

- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `POST /api/auth/logout` - User logout
- `GET /api/auth/check-email` - Check if email exists
- `GET /api/auth/check-empid` - Check if employee ID exists

### Example API Usage

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@trackerpro.com","password":"admin123"}'
```

**Register:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "confirmPassword": "password123",
    "department": "IT",
    "empId": "EMP001",
    "mobileNo": "+91-9876543210"
  }'
```

## 🗄️ Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    department VARCHAR(255) NOT NULL,
    emp_id VARCHAR(255) NOT NULL UNIQUE,
    mobile_no VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL CHECK (role IN ('ADMIN','USER')),
    enabled BOOLEAN DEFAULT TRUE
);
```

## 🔄 Switching to MySQL

To switch from H2 to MySQL:

1. **Update `application.properties`:**
   ```properties
   # Comment out H2 configuration
   # spring.datasource.url=jdbc:h2:mem:trackerdb
   # spring.datasource.driverClassName=org.h2.Driver
   # spring.h2.console.enabled=true
   
   # Enable MySQL configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/trackerdb
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
   ```

2. **Create MySQL database:**
   ```sql
   CREATE DATABASE trackerdb;
   ```

3. **Restart the application**

## 🏗️ Project Structure

```
tracker-pro/
├── src/
│   ├── main/
│   │   ├── java/com/trackerapp/
│   │   │   ├── TrackerProApplication.java
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/
│   │   │   │   ├── WebController.java
│   │   │   │   └── AuthController.java
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   └── RegistrationRequest.java
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   └── Role.java
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java
│   │   │   └── service/
│   │   │       └── UserService.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/images/
│   │       └── templates/
│   │           ├── index.html
│   │           ├── loginPage.html
│   │           ├── register.html
│   │           ├── dashboard.html
│   │           ├── success.html
│   │           ├── userlogin.html
│   │           └── forget.html
├── pom.xml
└── README.md
```

## 🧪 Testing

The application includes comprehensive validation:

- **Frontend Validation**: Real-time form validation with JavaScript
- **Backend Validation**: Server-side validation using Bean Validation
- **Database Constraints**: Unique constraints on email and employee ID
- **Security**: CSRF protection, XSS protection, and secure headers

## 🔒 Security Features

- **Password Encryption**: BCrypt password hashing
- **Session Management**: Secure session handling
- **CSRF Protection**: Built-in CSRF protection (disabled for API endpoints)
- **Role-Based Access**: Different access levels for Admin and User roles
- **Input Validation**: Comprehensive input validation and sanitization

## 🚦 User Workflow

1. **New Users**: 
   - Register → Email verification → Dashboard access
   
2. **Admin Users**: 
   - Direct login with default credentials → Dashboard access
   
3. **Regular Users**: 
   - Login with registered credentials → Dashboard access

## 📊 Monitoring

- **H2 Console**: Database monitoring and query execution
- **Application Logs**: Comprehensive logging for debugging
- **Security Events**: Authentication and authorization logging

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For support or questions, please open an issue in the repository.

---

**Built with ❤️ using Spring Boot and modern web technologies**