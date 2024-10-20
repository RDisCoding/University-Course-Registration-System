package Assignment_1_trial;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CourseDataManager {
    private static final String DATA_DIRECTORY = "data";
    private static final String COURSE_DATA_FILE = "courses.json";
    private final Path filePath;
    private List<Course> courses;

    public CourseDataManager() throws IOException {
        try {
            Files.createDirectories(Paths.get(DATA_DIRECTORY));
            this.filePath = Paths.get(DATA_DIRECTORY, COURSE_DATA_FILE);
            this.courses = new ArrayList<>();
            loadCourseData();
        } catch (IOException e) {
            System.err.println("Error initializing CourseDataManager: " + e.getMessage());
            throw e;
        }
    }

    private void loadCourseData() throws IOException {
        if (!Files.exists(filePath)) {
            createInitialCourseData();
            return;
        }

        try (FileReader reader = new FileReader(filePath.toFile())) {
            JSONTokener tokener = new JSONTokener(reader);
            JSONArray courseArray = new JSONArray(tokener);

            for (int i = 0; i < courseArray.length(); i++) {
                JSONObject courseObj = courseArray.getJSONObject(i);
                Course course = createCourseFromJSON(courseObj);
                courses.add(course);
            }
        } catch (IOException e) {
            System.err.println("Error loading course data: " + e.getMessage());
            throw e;
        }
    }

    private void createInitialCourseData() throws IOException {
        JSONArray initialCourses = new JSONArray();
        initialCourses.put(createJSONCourse("CS101", "Introduction to Programming", 3));
        initialCourses.put(createJSONCourse("CS201", "Data Structures", 4));

        try {
            Files.createDirectories(filePath.getParent());
            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                writer.write(initialCourses.toString(2));
            }
            System.out.println("Created courses.json file at: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error creating initial course data: " + e.getMessage());
            throw e;
        }
    }

    private JSONObject createJSONCourse(String code, String name, int credits) {
        JSONObject course = new JSONObject();
        course.put("code", code);
        course.put("name", name);
        course.put("credits", credits);
        return course;
    }

    private Course createCourseFromJSON(JSONObject courseObj) {
        return new Course(
            courseObj.getString("code"),
            courseObj.getString("name"),
            courseObj.getInt("credits")
        );
    }

    public void saveCourseData() throws IOException {
        JSONArray courseArray = new JSONArray();
        for (Course course : courses) {
            JSONObject courseObj = new JSONObject();
            courseObj.put("code", course.getCode());
            courseObj.put("name", course.getName());
            courseObj.put("credits", course.getCredits());
            courseArray.put(courseObj);
        }

        try (FileWriter writer = new FileWriter(filePath.toFile())) {
            writer.write(courseArray.toString(2));
            System.out.println("Saved course data to: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error saving course data: " + e.getMessage());
            throw e;
        }
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }

    public void addCourse(Course course) throws IOException {
        courses.add(course);
        saveCourseData();
    }

    public void removeCourse(Course course) throws IOException {
        courses.remove(course);
        saveCourseData();
    }

    public Course findCourseByCode(String code) {
        return courses.stream()
            .filter(c -> c.getCode().equalsIgnoreCase(code))
            .findFirst()
            .orElse(null);
    }
}