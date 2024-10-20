package Assignment_1_trial;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class UserDataManager {
	private static final String DATA_DIRECTORY = "data";
    private static final String USER_DATA_FILE = "users.json";
    private final Path filePath;
    private Map<String, User> userMap;

    public UserDataManager() throws IOException {
        try {
            Files.createDirectories(Paths.get(DATA_DIRECTORY));
            this.filePath = Paths.get(DATA_DIRECTORY, USER_DATA_FILE);
            this.userMap = new HashMap<>();
            loadUserData();
        } catch (IOException e) {
            System.err.println("Error initializing UserDataManager: " + e.getMessage());
            throw e;
        }
    }

    private void loadUserData() throws IOException {
        if (!Files.exists(filePath)) {
            createInitialUserData();
            return;
        }

        try (FileReader reader = new FileReader(filePath.toFile())) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONArray userArray = new JSONArray(tokener);

            for (int i = 0; i < userArray.length(); i++) {
                JSONObject userObj = userArray.getJSONObject(i);
                User user = createUserFromJSON(userObj);
                if (user != null) {
                    userMap.put(user.getEmail(), user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading user data: " + e.getMessage());
            throw e;
        }
    }

    private void createInitialUserData() {
        JSONArray initialUsers = new JSONArray();
        
        // Add sample users
        initialUsers.put(createJSONUser("Student", "John Doe", "john@example.com", "password123"));
        initialUsers.put(createJSONUser("Professor", "Jane Smith", "jane@example.com", "prof456"));
        initialUsers.put(createJSONUser("Administrator", "Admin", "admin@example.com", "admin123"));

        try (FileWriter writer = new FileWriter(USER_DATA_FILE)) {
            writer.write(initialUsers.toString(2));
        } catch (IOException e) {
            throw new RuntimeException("Error creating initial user data: " + e.getMessage(), e);
        }
    }

    private JSONObject createJSONUser(String role, String name, String email, String password) {
        JSONObject user = new JSONObject();
        user.put("role", role);
        user.put("name", name);
        user.put("email", email);
        user.put("password", password);
        return user;
    }

    private User createUserFromJSON(JSONObject userObj) {
        String role = userObj.getString("role");
        String name = userObj.getString("name");
        String email = userObj.getString("email");
        String password = userObj.getString("password");

        switch (role) {
            case "Student":
                return new Student(name, email, password);
            case "Professor":
                return new Professor(name, email, password);
            case "Administrator":
                return new Administrator(name, email, password);
            default:
                return null;
        }
    }

    public User authenticateUser(String email, String password, int role) {
        User user = userMap.get(email);
        if (user != null && user.getPassword().equals(password)) {
            boolean roleMatches = (role == 1 && user instanceof Student) ||
                                (role == 2 && user instanceof Professor) ||
                                (role == 3 && user instanceof Administrator);
            return roleMatches ? user : null;
        }
        return null;
    }

    public void addUser(User user) throws IOException {
        userMap.put(user.getEmail(), user);
        saveUserData();
    }

    public void updateUser(String email, User updatedUser) throws IOException {
        userMap.put(email, updatedUser);
        saveUserData();
    }

    private void saveUserData() {
        try {
            JSONArray userArray = new JSONArray();
            for (User user : userMap.values()) {
                JSONObject userObj = new JSONObject();
                userObj.put("role", getUserRole(user));
                userObj.put("name", user.getName());
                userObj.put("email", user.getEmail());
                userObj.put("password", user.getPassword());
                userArray.put(userObj);
            }

            try (FileWriter writer = new FileWriter(USER_DATA_FILE)) {
                writer.write(userArray.toString(2));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving user data: " + e.getMessage(), e);
        }
    }

    private String getUserRole(User user) {
        if (user instanceof Student) return "Student";
        if (user instanceof Professor) return "Professor";
        if (user instanceof Administrator) return "Administrator";
        return "Unknown";
    }

    public Collection<User> getAllUsers() {
        return userMap.values();
    }
}