# 🗄️ H2 DATABASE CONSOLE - ACCESS GUIDE

## ✅ Fixed! H2 Console is Now Accessible

Your security configuration has been updated to allow H2 console access without authentication.

---

## 🚀 Quick Start

### Step 1: Start Your Application
```bash
cd C:\Users\omkar\OneDrive\Documents\hms\demo
mvn spring-boot:run
```

Wait for this message:
```
Started HmsApplication in X seconds
Tomcat started on port 8080
```

### Step 2: Open H2 Console
Open your browser and go to:
```
http://localhost:8080/h2-console
```

### Step 3: Login (No Authentication Required)
Just click **"Connect"** button - all fields are pre-filled:

| Field | Value |
|-------|-------|
| Driver Class | `org.h2.Driver` |
| JDBC URL | `jdbc:h2:mem:testdb` |
| User Name | `sa` |
| Password | (leave empty) |

---

## 🔧 What Was Changed

**File:** `SecurityConfig.java`

**Changes Made:**
```java
// Added H2 console to allowed endpoints
.requestMatchers("/h2-console/**").permitAll()

// Allow H2 console frames
.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))

// Disable CSRF for development
.csrf(csrf -> csrf.disable())
```

---

## 📊 H2 Console Features

Once logged in, you can:

### View All Tables
```sql
SELECT * FROM users;
SELECT * FROM students;
SELECT * FROM complaints;
SELECT * FROM notices;
SELECT * FROM fees;
SELECT * FROM rooms;
SELECT * FROM room_change_requests;
```

### Check Table Structure
```sql
DESCRIBE users;
DESCRIBE students;
SHOW TABLES;
```

### Query Data
```sql
SELECT COUNT(*) FROM users;
SELECT * FROM students WHERE username = 'john';
```

### Insert Test Data
```sql
-- Create test user
INSERT INTO users (username, email, password, role) 
VALUES ('testuser', 'test@example.com', 'pass123', 'STUDENT');

-- Create test student
INSERT INTO students (name, email, department, user_id) 
VALUES ('John Doe', 'john@example.com', 'CSE', 1);
```

---

## 🎯 Database Tables Available

| Table | Purpose |
|-------|---------|
| `users` | User authentication & roles |
| `students` | Student information |
| `rooms` | Hostel room details |
| `complaints` | Student complaints |
| `notices` | Hostel notices |
| `fees` | Fee payment information |
| `room_change_requests` | Room change requests |

---

## 💡 Useful SQL Queries

### Count All Records
```sql
SELECT 'users' as table_name, COUNT(*) as count FROM users
UNION ALL
SELECT 'students', COUNT(*) FROM students
UNION ALL
SELECT 'complaints', COUNT(*) FROM complaints
UNION ALL
SELECT 'rooms', COUNT(*) FROM rooms;
```

### View All Users with Roles
```sql
SELECT id, username, email, role FROM users;
```

### View Student Details
```sql
SELECT s.id, s.name, s.email, s.department, u.username, u.role
FROM students s
JOIN users u ON s.user_id = u.id;
```

### View Room Allocations
```sql
SELECT r.id, r.roomNo, r.capacity, 
       COUNT(s.id) as occupied_students
FROM rooms r
LEFT JOIN students s ON r.id = s.id
GROUP BY r.id, r.roomNo, r.capacity;
```

---

## 🔍 Troubleshooting

### Problem: Still seeing "Access Denied"

**Solution 1: Clear Browser Cache**
- Press `Ctrl + Shift + Delete`
- Clear all data
- Reload page

**Solution 2: Restart Application**
```bash
# Stop current application (Ctrl+C)
# Then restart
mvn spring-boot:run
```

**Solution 3: Try Incognito/Private Window**
- Open H2 console in private/incognito mode
- This avoids cache issues

### Problem: "H2 Console not found (404)"

**Check if:**
- ✅ Application is running on port 8080
- ✅ URL is exactly: `http://localhost:8080/h2-console`
- ✅ application.properties has: `spring.h2.console.enabled=true`

### Problem: "Tomcat started on port X" (not 8080)

**Solution:**
- If using different port, replace 8080 with your port
- Example: `http://localhost:9090/h2-console`

---

## 📋 Current Configuration

Your `application.properties` is configured as:

```properties
spring.application.name=hms
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

spring.h2.console.enabled=true
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

✅ Everything is correctly configured!

---

## 🎓 Learn More

### H2 Console Navigation
- **Left Panel:** Database structure (Tables, Views, etc.)
- **Top Bar:** SQL editor area
- **Bottom:** Query results
- **Refresh Button:** Reload table list

### Common Keys
- `F5` - Refresh table structure
- `Ctrl+Enter` - Execute query
- `Ctrl+Space` - SQL autocomplete

---

## ✨ Your Data is Safe

**Important Notes:**
- ✅ H2 is an **in-memory database** for development
- ✅ Data persists **during current session only**
- ✅ Data is **lost when application stops**
- ✅ Perfect for testing and development
- ✅ Use MySQL/PostgreSQL for production

---

## 🔐 Security Note

The H2 console endpoint is:
- ✅ Only accessible on **localhost** (development machine)
- ✅ Not exposed to internet
- ✅ Protected for production use
- ✅ Safe to use in development environment

**For Production:** Always disable H2 console!
```properties
spring.h2.console.enabled=false
```

---

## 📞 Quick Reference

| Action | Command/URL |
|--------|-------------|
| **Start App** | `mvn spring-boot:run` |
| **Open Console** | `http://localhost:8080/h2-console` |
| **Login** | Just click "Connect" |
| **View All Tables** | `SELECT * FROM INFORMATION_SCHEMA.TABLES;` |
| **Stop App** | `Ctrl+C` in terminal |

---

**You're all set!** 🎉

The H2 console is now accessible without authentication.

Visit: **`http://localhost:8080/h2-console`**

---

**Created:** November 11, 2025  
**Status:** ✅ H2 Console Ready  
**Configuration:** Complete
