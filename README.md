# 📚 University Course Registration System

A robust Java-based course registration system implementing Object-Oriented Programming principles to manage university course registration processes.

## 🌟 Features

- Multi-user authentication system (Students, Professors, Administrators)
- Course management and registration
- Academic progress tracking
- Complaint management system
- Schedule viewing and management
- Professor-course assignment system

## 🏗️ Object-Oriented Programming Concepts Used

### 1. Encapsulation
- **Data Hiding**: Private fields in all classes with public getter/setter methods
- **Information Hiding**: Internal implementations are hidden from external classes
- **Examples**:
  ```java
  private UserDataManager userManager;
  private CourseDataManager courseManager;
  private ComplaintDataManager complaintManager;
  ```

### 2. Inheritance
- **User Hierarchy**:
  - Base class: `User`
  - Derived classes: `Student`, `Professor`, `Administrator`
- **Inherited Properties**: Common attributes like name, email, and password
- **Benefits**: Code reuse and hierarchical organization

### 3. Polymorphism
- **Method Overriding**: 
  - Different implementations of `displayMenu()` for each user type
  - Specialized handling in `handleUserMenu()` for different user types
- **Runtime Polymorphism**: 
  ```java
  User user = authenticateUser(email, password, role);
  user.displayMenu(); // Calls appropriate version based on user type
  ```

### 4. Abstraction
- **High-level Operations**: Complex operations are abstracted into simple method calls
- **Data Managers**: 
  - `UserDataManager`
  - `CourseDataManager`
  - `ComplaintDataManager`
- **Example**:
  ```java
  courseManager.addCourse(newCourse);
  userManager.updateUser(email, student);
  ```

### 5. Composition
- **System Components**: Main system composed of various manager classes
- **Relationships**: Courses contain lists of enrolled students
- **Benefits**: Modular design and better maintainability

## 🛡️ Exception Handling

- Comprehensive error handling using try-catch blocks
- Custom exception messages for different scenarios
- Input validation and error prevention
- Example:
  ```java
  try {
      // Operation code
  } catch (IllegalArgumentException e) {
      System.err.println("Error: " + e.getMessage());
  }
  ```

## 📝 Design Patterns

1. **Singleton Pattern**: Used for data managers
2. **Factory Pattern**: User creation based on role
3. **MVC Pattern**: Separation of data, logic, and presentation

## 🔄 System Flow

1. **Authentication**:
   - User login with role selection
   - Credential verification
   - Role-based menu presentation

2. **Operations**:
   - Course registration/dropping
   - Schedule management
   - Academic progress tracking
   - Complaint submission and handling
   - Course and user management

## 🛠️ Technical Requirements

- Java 8 or higher
- Console-based interface
- File system for data persistence

## 🚀 Getting Started

1. Clone the repository
```bash
git clone https://github.com/yourusername/course-registration-system.git
```

2. Compile the project
```bash
javac CourseRegistrationSystem.java
```

3. Run the application
```bash
java CourseRegistrationSystem
```

## 👥 User Types and Permissions

### Student
- View available courses
- Register for courses
- View schedule
- Track academic progress
- Drop courses
- Submit complaints

### Professor
- Manage taught courses
- View enrolled students
- Add/remove courses

### Administrator
- Manage all courses
- Manage student records
- Assign professors to courses
- Handle complaints

## 📊 Project Structure

```
src/
├── main/
│   ├── CourseRegistrationSystem.java
│   ├── models/
│   │   ├── User.java
│   │   ├── Student.java
│   │   ├── Professor.java
│   │   ├── Administrator.java
│   │   ├── Course.java
│   │   └── Complaint.java
│   └── managers/
│       ├── UserDataManager.java
│       ├── CourseDataManager.java
│       └── ComplaintDataManager.java
```
