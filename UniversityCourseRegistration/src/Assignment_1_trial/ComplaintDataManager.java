package Assignment_1_trial;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ComplaintDataManager {
    private static final String DATA_DIRECTORY = "data";
    private static final String COMPLAINT_DATA_FILE = "complaints.json";
    private final Path filePath;
    private List<Complaint> complaints;

    public ComplaintDataManager() throws IOException {
        try {
            Files.createDirectories(Paths.get(DATA_DIRECTORY));
            this.filePath = Paths.get(DATA_DIRECTORY, COMPLAINT_DATA_FILE);
            this.complaints = new ArrayList<>();
            loadComplaintData();
        } catch (IOException e) {
            System.err.println("Error initializing ComplaintDataManager: " + e.getMessage());
            throw e;
        }
    }

    private void loadComplaintData() throws IOException {
        if (!Files.exists(filePath)) {
            createInitialComplaintData();
            return;
        }

        try (FileReader reader = new FileReader(filePath.toFile())) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONArray complaintArray = new JSONArray(tokener);

            for (int i = 0; i < complaintArray.length(); i++) {
                JSONObject complaintObj = complaintArray.getJSONObject(i);
                Complaint complaint = createComplaintFromJSON(complaintObj);
                if (complaint != null) {
                    complaints.add(complaint);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading complaint data: " + e.getMessage());
            throw e;
        }
    }

    private void createInitialComplaintData() throws IOException {
        JSONArray initialComplaints = new JSONArray();
        try {
            Files.createDirectories(filePath.getParent());
            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                writer.write(initialComplaints.toString(2));
            }
            System.out.println("Created complaints.json file at: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error creating initial complaint data: " + e.getMessage());
            throw e;
        }
    }

    private Complaint createComplaintFromJSON(JSONObject complaintObj) {
        // Assuming Student information is stored in the complaint
        JSONObject studentObj = complaintObj.getJSONObject("student");
        Student student = new Student(
            studentObj.getString("name"),
            studentObj.getString("email"),
            studentObj.getString("password")
        );
        return new Complaint(student, complaintObj.getString("text"));
    }

    public void saveComplaintData() throws IOException {
        JSONArray complaintArray = new JSONArray();
        for (Complaint complaint : complaints) {
            JSONObject complaintObj = new JSONObject();
            
            // Store student information
            JSONObject studentObj = new JSONObject();
            Student student = complaint.getStudent();
            studentObj.put("name", student.getName());
            studentObj.put("email", student.getEmail());
            studentObj.put("password", student.getPassword());
            
            complaintObj.put("student", studentObj);
            complaintObj.put("text", complaint.getText());
            complaintArray.put(complaintObj);
        }

        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            writer.write(complaintArray.toString(2));
            System.out.println("Saved complaint data to: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving complaint data: " + e.getMessage());
            throw e;
        }
    }

    public void addComplaint(Complaint complaint) throws IOException {
        complaints.add(complaint);
        saveComplaintData();
    }

    public void removeComplaint(Complaint complaint) throws IOException {
        complaints.remove(complaint);
        saveComplaintData();
    }

    public List<Complaint> getAllComplaints() {
        return new ArrayList<>(complaints);
    }
}