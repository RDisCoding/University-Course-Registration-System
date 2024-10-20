package Assignment_1_trial;
 
public class Administrator extends User {
    public Administrator(String name, String email, String password) {
        super(name, email, password);
    }

    @Override
    public void displayMenu() {
        System.out.println("1. Manage Course Catalog");
        System.out.println("2. Manage Student Records");
        System.out.println("3. Assign Professors to Courses");
        System.out.println("4. Handle Complaints");
        System.out.println("5. Logout");
    }
}