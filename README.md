# Student Management System

A desktop-based Student Management System developed in Java using **Java Swing**, **JDBC**, and **MySQL**. It supports admin authentication, session tracking, form validation, and complete CRUD operations.

## Technologies and Tools Used
- **Frontend:** Java Swing (JFrame, JTable, JOptionPane)
- **Backend:** Core Java (JDK), JDBC
- **Database:** MySQL (XAMPP / phpMyAdmin)
- **Driver:** MySQL Connector/J 9.7.0
- **IDE:** Apache NetBeans

## Features and Functionalities
- **Admin Login & Session Management:** Secure login with session tracking (`SessionManager`) and logout functionality.
- **Input Validation:** Validates empty fields, email format, 10-digit phone numbers, `YYYY-MM-DD` dates, and numeric Course IDs.
- **Complete Student CRUD:**
  - **Create:** Add new student records into MySQL.
  - **Read:** View all student records in an interactive `JTable`.
  - **Update:** Click any table row to edit and update student details.
  - **Delete:** Delete selected student records with confirmation dialogs.

## Database Setup Instructions
1. Start **Apache** and **MySQL** in **XAMPP Control Panel**.
2. Open **phpMyAdmin** (`http://localhost/phpmyadmin`).
3. Create a database named `student_management`.
4. Click **Import**, select `student_management.sql`, and click **Go**.
5. Default credentials in `DBConnection.java`: URL `jdbc:mysql://localhost:3306/student_management`, Username `root`, Password `""`.

## Installation and Execution Instructions
1. Extract the project folder or clone this repository.
2. Open **Apache NetBeans** -> **File -> Open Project** -> select `studentmanagement`.
3. Ensure `mysql-connector-j-9.7.0.jar` is added under **Libraries**.
4. Right-click `NewJFrame.java` and click **Run File** (`Shift + F6`).
5. Log in with Username: `admin` | Password: `admin123`.