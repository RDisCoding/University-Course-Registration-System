package Assignment_1_trial;

import java.util.ArrayList;
import java.io.Console;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;
import java.util.InputMismatchException;
import java.io.IOException;
import java.util.NoSuchElementException;

public class CourseRegistrationSystem {
	private UserDataManager userManager;
	private CourseDataManager courseManager;
    private ComplaintDataManager complaintManager;
    private Scanner scanner;

    public CourseRegistrationSystem() {
    	try {
        userManager = new UserDataManager();
        courseManager = new CourseDataManager();
        complaintManager = new ComplaintDataManager();
        scanner = new Scanner(System.in);
    	} catch(Exception e) {
    		System.err.println("Error initializing the system: " + e.getMessage());
            throw new RuntimeException("System initialization failed", e);
    	}
    }

    public void start() {
        while (true) {
        	try {
            System.out.println("Welcome to the University Course Registration System");
            System.out.println("1. Enter the Application");
            System.out.println("2. Exit the Application");
            int choice = getIntInput("Enter your choice: ");

            if (choice == 1) {
                login();
            } else if (choice == 2) {
                System.out.println("Thank you for using the Course Registration System. Goodbye!");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        	} catch (Exception e) {
        		System.err.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again.");
        	}
        }
    }

    private void login() throws IllegalArgumentException {
    	try { 
        System.out.println("Login as:");
        System.out.println("1. Student");
        System.out.println("2. Professor");
        System.out.println("3. Administrator");
        int role = getIntInput("Enter your role: ");
        
        if (role < 1 || role > 3) {
            throw new IllegalArgumentException("Invalid role selected. Please choose 1, 2, or 3.");
        }

        Console console = System.console();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        
        String password;
        if (console != null) {
            char[] passwordArray = console.readPassword("Enter password: ");
            if (passwordArray == null || passwordArray.length == 0) {
                throw new IllegalArgumentException("Password cannot be empty.");
            }
            password = new String(passwordArray);
            java.util.Arrays.fill(passwordArray, ' ');
        } 
        else {
            System.out.println("Password will be visible when typing.");
            System.out.print("Enter password: ");
            password = scanner.nextLine();
            if (password == null || password.trim().isEmpty()) {
                throw new IllegalArgumentException("Password cannot be empty.");
            }
        }
        
        User user = authenticateUser(email, password, role);
        if (user != null) {
            handleUserMenu(user);
        } else {
            throw new IllegalArgumentException("Invalid credentials or user not found.");
        }
    } catch (NoSuchElementException e) {
        System.err.println("Error reading input: " + e.getMessage());
    } catch (IllegalArgumentException e) {
        System.err.println("Login error: " + e.getMessage());
    } 
    }

//    private User authenticateUser(String email, String password, int role) {
//    	try {
//            if (email == null || password == null) {
//                throw new IllegalArgumentException("Email and password cannot be null.");
//            }
//            
//            for (User user : users) {
//                if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
//                    if ((role == 1 && user instanceof Student) ||
//                        (role == 2 && user instanceof Professor) ||
//                        (role == 3 && user instanceof Administrator)) {
//                        return user;
//                    }
//                }
//            }
//            return null;
//        } catch (NullPointerException e) {
//            System.err.println("Error during authentication: " + e.getMessage());
//            return null;
//        }
//    }
    private User authenticateUser(String email, String password, int role) {
        try {
            return userManager.authenticateUser(email, password, role);
        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            return null;
        }
    }

    private void handleUserMenu(User user) {
        while (true) {
            user.displayMenu();
            int choice = getIntInput("Enter your choice: ");

            if (user instanceof Student) {
                if (handleStudentMenu((Student) user, choice)) break;
            } else if (user instanceof Professor) {
                if (handleProfessorMenu((Professor) user, choice)) break;
            } else if (user instanceof Administrator) {
                if (handleAdminMenu((Administrator) user, choice)) break;
            }
        }
    }

    private boolean handleStudentMenu(Student student, int choice) {
        switch (choice) {
            case 1:
                viewAvailableCourses();
                break;
            case 2:
                registerForCourses(student);
                break;
            case 3:
                viewSchedule(student);
                break;
            case 4:
                trackAcademicProgress(student);
                break;
            case 5:
                dropCourse(student);
                break;
            case 6:
                submitComplaint(student);
                break;
            case 7:
                System.out.println("Logging out...");
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    private boolean handleProfessorMenu(Professor professor, int choice) {
        switch (choice) {
            case 1:
                manageCourses(professor);
                break;
            case 2:
                viewEnrolledStudents(professor);
                break;
            case 3:
                System.out.println("Logging out...");
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    private boolean handleAdminMenu(Administrator admin, int choice) {
        switch (choice) {
            case 1:
                manageCourses(admin);
                break;
            case 2:
                manageStudentRecords();
                break;
            case 3:
                assignProfessorsToCourses();
                break;
            case 4:
                handleComplaints();
                break;
            case 5:
                System.out.println("Logging out...");
                return true;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        return false;
    }

    private void submitComplaint(Student student) {
        try {
            System.out.println("Submit a Complaint");
            System.out.print("Please describe your complaint: ");
            String complaintText = scanner.nextLine();
            
            Complaint newComplaint = new Complaint(student, complaintText);
            complaintManager.addComplaint(newComplaint);
            
            System.out.println("Your complaint has been submitted successfully.");
        } catch (IOException e) {
            System.err.println("Error submitting complaint: " + e.getMessage());
        }
    }
    
    private void handleComplaints() {
        try {
            List<Complaint> complaints = complaintManager.getAllComplaints();
            if (complaints.isEmpty()) {
                System.out.println("There are no complaints to handle at this time.");
                return;
            }

            System.out.println("Handling Complaints");
            Iterator<Complaint> iterator = complaints.iterator();
            while (iterator.hasNext()) {
                Complaint complaint = iterator.next();
                System.out.println("\nComplaint from " + complaint.getStudent().getName() + ":");
                System.out.println(complaint.getText());
                
                System.out.print("Mark this complaint as resolved? (y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                
                if (response.equals("y")) {
                    complaintManager.removeComplaint(complaint);
                    System.out.println("Complaint marked as resolved and removed from the list.");
                } else {
                    System.out.println("Complaint left unresolved.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error handling complaints: " + e.getMessage());
        }
    }

    private int getIntInput(String prompt) {
    	while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Input cannot be empty");
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.err.println("Invalid input. Please enter a valid number.");
            } catch (NoSuchElementException e) {
                System.err.println("Error reading input. Please try again.");
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void viewAvailableCourses() {
        System.out.println("Available Courses:");
        for (Course course : courseManager.getAllCourses()) {
            System.out.println(course.getCode() + " - " + course.getName() + " (" + course.getCredits() + " credits)");
        }
    }

    private void registerForCourses(Student student) {
    	try {
            if (student == null) {
                throw new IllegalArgumentException("Student cannot be null");
            }
            
            viewAvailableCourses();
            System.out.print("Enter the course code you want to register for: ");
            String courseCode = scanner.nextLine();
            
            if (courseCode == null || courseCode.trim().isEmpty()) {
                throw new IllegalArgumentException("Course code cannot be empty");
            }
            
            Course selectedCourse = findCourseByCode(courseCode);
            if (selectedCourse != null) {
                if (student.getEnrolledCourses().contains(selectedCourse)) {
                    throw new IllegalStateException("Already registered for this course");
                }
                student.registerForCourse(selectedCourse);
                System.out.println("Successfully registered for " + selectedCourse.getName());
            } else {
                throw new NoSuchElementException("Course not found: " + courseCode);
            }
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            System.err.println("Registration error: " + e.getMessage());
        }
    }

    private void viewSchedule(Student student) {
        List<Course> enrolledCourses = student.getEnrolledCourses();
        if (enrolledCourses.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
        } else {
            System.out.println("Your Schedule:");
            for (Course course : enrolledCourses) {
                System.out.println(course.getCode() + " - " + course.getName());
            }
        }
    }

    private void trackAcademicProgress(Student student) {
        System.out.println("Academic Progress:");
        System.out.println("Current Semester: " + student.getCurrentSemester());
        System.out.println("CGPA: " + student.getCgpa());
        System.out.println("Completed Courses:");
        for (Course course : student.getCompletedCourses()) {
            System.out.println(course.getCode() + " - " + course.getName());
        }
    }

    private void dropCourse(Student student) {
    	try {
            if (student == null) {
                throw new IllegalArgumentException("Student cannot be null");
            }
            
            viewSchedule(student);
            System.out.print("Enter the course code you want to drop: ");
            String courseCode = scanner.nextLine();
            
            if (courseCode == null || courseCode.trim().isEmpty()) {
                throw new IllegalArgumentException("Course code cannot be empty");
            }
            
            Course selectedCourse = findCourseByCode(courseCode);
            if (selectedCourse == null) {
                throw new NoSuchElementException("Course not found: " + courseCode);
            }
            
            if (!student.getEnrolledCourses().contains(selectedCourse)) {
                throw new IllegalStateException("Not enrolled in this course");
            }
            
            student.dropCourse(selectedCourse);
            System.out.println("Successfully dropped " + selectedCourse.getName());
        } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
            System.err.println("Course drop error: " + e.getMessage());
        }
    }
    
    private void viewTaughtCourses(Professor professor) {
        List<Course> taughtCourses = professor.getTaughtCourses();
        if (taughtCourses.isEmpty()) {
            System.out.println("You are not teaching any courses.");
        } else {
            System.out.println("Courses you are teaching:");
            for (Course course : taughtCourses) {
                System.out.println(course.getCode() + " - " + course.getName());
            }
        }
    }

    private void addNewCourse() {
        try {
            System.out.print("Enter course code: ");
            String code = scanner.nextLine();
            if (code == null || code.trim().isEmpty()) {
                throw new IllegalArgumentException("Course code cannot be empty");
            }
            
            if (courseManager.findCourseByCode(code) != null) {
                throw new IllegalArgumentException("Course code already exists");
            }
            
            System.out.print("Enter course name: ");
            String name = scanner.nextLine();
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Course name cannot be empty");
            }
            
            int credits = getIntInput("Enter course credits: ");
            if (credits <= 0) {
                throw new IllegalArgumentException("Credits must be greater than 0");
            }
            
            Course newCourse = new Course(code, name, credits);
            courseManager.addCourse(newCourse);
            System.out.println("Course added successfully.");
        } catch (IllegalArgumentException | IOException e) {
            System.err.println("Error adding course: " + e.getMessage());
        }
    }

    private void removeCourse() {
    	try {
    		viewAllCourses();
            System.out.print("Enter the code of the course to remove: ");
            String code = scanner.nextLine();
            if (code == null || code.trim().isEmpty()) {
                throw new IllegalArgumentException("Course code cannot be empty");
            }
            Course courseToRemove = findCourseByCode(code);
            
            if (courseToRemove != null) {
                courseManager.removeCourse(courseToRemove);
                System.out.println("Course removed successfully.");
            } else {
                System.out.println("Course not found.");
            }
    	} catch (IllegalArgumentException | IOException e) {
            System.err.println("Error removing course: " + e.getMessage());
        }
    }

    private void manageCourses(Professor professor) {
        System.out.println("1. View taught courses");
        System.out.println("2. Add a new course");
        System.out.println("3. Remove a course");
        int choice = getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                viewTaughtCourses(professor);
                break;
            case 2:
                addNewCourse();
                break;
            case 3:
                removeCourse();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void viewEnrolledStudents(Professor professor) {
        List<Course> taughtCourses = professor.getTaughtCourses();
        if (taughtCourses.isEmpty()) {
            System.out.println("You are not teaching any courses.");
        } else {
            for (Course course : taughtCourses) {
                System.out.println("Enrolled students for " + course.getName() + ":");
                for (Student student : course.getEnrolledStudents()) {
                    System.out.println("- " + student.getName());
                }
            }
        }
    }

    private void manageCourses(Administrator admin) {
        System.out.println("1. Add a new course");
        System.out.println("2. Remove a course");
        System.out.println("3. View all courses");
        int choice = getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                addNewCourse();
                break;
            case 2:
                removeCourse();
                break;
            case 3:
                viewAllCourses();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void manageStudentRecords() {
        System.out.println("1. View all students");
        System.out.println("2. Update student information");
        int choice = getIntInput("Enter your choice: ");

        switch (choice) {
            case 1:
                viewAllStudents();
                break;
            case 2:
                updateStudentInformation();
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    private void assignProfessorsToCourses() {
    	try {
            viewAllCourses();
            System.out.print("Enter the course code to assign a professor: ");
            String courseCode = scanner.nextLine();
            
            if (courseCode == null || courseCode.trim().isEmpty()) {
                throw new IllegalArgumentException("Course code cannot be empty");
            }
            
            Course selectedCourse = findCourseByCode(courseCode);
            if (selectedCourse == null) {
                throw new NoSuchElementException("Course not found: " + courseCode);
            }
            
            viewAllProfessors();
            System.out.print("Enter the email of the professor to assign: ");
            String profEmail = scanner.nextLine();
            
            if (profEmail == null || profEmail.trim().isEmpty()) {
                throw new IllegalArgumentException("Professor email cannot be empty");
            }
            
            Professor selectedProf = findProfessorByEmail(profEmail);
            if (selectedProf == null) {
                throw new NoSuchElementException("Professor not found: " + profEmail);
            }
            
            selectedCourse.assignProfessor(selectedProf);
            selectedProf.addCourse(selectedCourse);
            System.out.println("Professor successfully assigned to the course.");
        } catch (IllegalArgumentException | NoSuchElementException e) {
            System.err.println("Assignment error: " + e.getMessage());
        }
    }

    // Helper methods
    private Course findCourseByCode(String code) {
        for (Course course : courseManager.getAllCourses()) {
            if (course.getCode().equalsIgnoreCase(code)) {
                return course;
            }
        }
        return null;
    }

    private Professor findProfessorByEmail(String email) {
        for (User user : userManager.getAllUsers()) {
            if (user instanceof Professor && user.getEmail().equalsIgnoreCase(email)) {
                return (Professor) user;
            }
        }
        return null;
    }

    private void viewAllCourses() {
        System.out.println("All Courses:");
        for (Course course : courseManager.getAllCourses()) {
            System.out.println(course.getCode() + " - " + course.getName());
        }
    }

    private void viewAllStudents() {
        System.out.println("All Students:");
        for (User user : userManager.getAllUsers()) {
            if (user instanceof Student) {
                Student student = (Student) user;
                System.out.println(student.getName() + " - " + student.getEmail());
            }
        }
    }

    private void viewAllProfessors() {
        System.out.println("All Professors:");
        for (User user : userManager.getAllUsers()) {
            if (user instanceof Professor) {
                Professor professor = (Professor) user;
                System.out.println(professor.getName() + " - " + professor.getEmail());
            }
        }
    }

    private void updateStudentInformation() {
        try {
            viewAllStudents();
            System.out.print("Enter the email of the student to update: ");
            String email = scanner.nextLine();
            
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            
            Student student = findStudentByEmail(email);
            if (student == null) {
                throw new NoSuchElementException("Student not found: " + email);
            }
            
            System.out.print("Enter new name (or press enter to skip): ");
            String newName = scanner.nextLine();
            if (!newName.trim().isEmpty()) {
                student.setName(newName);
            }
            
            System.out.print("Enter new email (or press enter to skip): ");
            String newEmail = scanner.nextLine();
            if (!newEmail.trim().isEmpty()) {
                if (!isValidEmail(newEmail)) {
                    throw new IllegalArgumentException("Invalid email format");
                }
                student.setEmail(newEmail);
            }
            
            userManager.updateUser(email, student);
            System.out.println("Student information updated successfully.");
        } catch (Exception e) {
            System.err.println("Update error: " + e.getMessage());
        }
    }
    
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private Student findStudentByEmail(String email) {
        for (User user : userManager.getAllUsers()) {
            if (user instanceof Student && user.getEmail().equalsIgnoreCase(email)) {
                return (Student) user;
            }
        }
        return null;
    }

    public static void main(String[] args) {
    	try {
            CourseRegistrationSystem system = new CourseRegistrationSystem();
            system.start();
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close any resources if needed
        }
    }
}