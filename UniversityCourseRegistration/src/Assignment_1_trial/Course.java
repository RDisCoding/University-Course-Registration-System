package Assignment_1_trial;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String code;
    private String name;
    private int credits;
    private Professor assignedProfessor;
    private List<Student> enrolledStudents;

    public Course(String code, String name, int credits) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.enrolledStudents = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public Professor getAssignedProfessor() {
        return assignedProfessor;
    }

    public void assignProfessor(Professor professor) {
        this.assignedProfessor = professor;
    }

    public List<Student> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void enrollStudent(Student student) {
        enrolledStudents.add(student);
    }

    public void unenrollStudent(Student student) {
        enrolledStudents.remove(student);
    }
}