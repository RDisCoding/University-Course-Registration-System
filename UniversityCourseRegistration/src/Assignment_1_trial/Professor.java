package Assignment_1_trial;

import java.util.ArrayList;
import java.util.List;

public class Professor extends User {
    private List<Course> taughtCourses;

    public Professor(String name, String email, String password) {
        super(name, email, password);
        this.taughtCourses = new ArrayList<>();
    }

    @Override
    public void displayMenu() {
        System.out.println("1. Manage Courses");
        System.out.println("2. View Enrolled Students");
        System.out.println("3. Logout");
    }

    public void addCourse(Course course) {
        taughtCourses.add(course);
    }

    public void removeCourse(Course course) {
        taughtCourses.remove(course);
    }

    public List<Course> getTaughtCourses() {
        return taughtCourses;
    }
}