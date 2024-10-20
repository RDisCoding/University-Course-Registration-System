package Assignment_1_trial;

import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private int currentSemester;
    private List<Course> enrolledCourses;
    private List<Course> completedCourses;
    private double cgpa;

    public Student(String name, String email, String password) {
        super(name, email, password);
        this.currentSemester = 1;
        this.enrolledCourses = new ArrayList<>();
        this.completedCourses = new ArrayList<>();
        this.cgpa = 0.0;
    }

    @Override
    public void displayMenu() {
        System.out.println("1. View Available Courses");
        System.out.println("2. Register for Courses");
        System.out.println("3. View Schedule");
        System.out.println("4. Track Academic Progress");
        System.out.println("5. Drop Course");
        System.out.println("6. Submit Complaint");
        System.out.println("7. Logout");
    }

    public void registerForCourse(Course course) {
        enrolledCourses.add(course);
    }

    public void dropCourse(Course course) {
        enrolledCourses.remove(course);
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public List<Course> getCompletedCourses() {
        return completedCourses;
    }

    public double getCgpa() {
        return cgpa;
    }

    public void setCgpa(double cgpa) {
        this.cgpa = cgpa;
    }

    public int getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(int currentSemester) {
        this.currentSemester = currentSemester;
    }
}