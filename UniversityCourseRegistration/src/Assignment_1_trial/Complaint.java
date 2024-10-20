package Assignment_1_trial;

public class Complaint {
    private Student student;
    private String text;
    private boolean resolved;

    public Complaint(Student student, String text) {
        this.student = student;
        this.text = text;
        this.resolved = false;
    }

    public Student getStudent() {
        return student;
    }

    public String getText() {
        return text;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }
}