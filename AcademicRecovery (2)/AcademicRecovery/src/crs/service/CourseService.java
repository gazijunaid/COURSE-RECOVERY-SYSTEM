package crs.service;

import crs.model.Course;
import crs.util.FileManager;

import java.io.*;
import java.util.*;

public class CourseService {
    
    private static final String COURSES_FILE = "courses.dat";
    
    private List<Course> courses;
    
    private static CourseService instance;
    
    private CourseService() {
        loadCourses();
    }
    
    public static CourseService getInstance() {
        if (instance == null) {
            instance = new CourseService();
        }
        return instance;
    }
    
    private void loadCourses() {
        courses = FileManager.loadFromBinaryFile(COURSES_FILE);
        if (courses == null) {
            courses = new ArrayList<>();
        }
    }
    
    public void saveCourses() {
        FileManager.saveToBinaryFile(COURSES_FILE, courses);
    }
    
    public void importCoursesFromCSV(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    Course course = new Course(
                        parts[0].trim(),
                        parts[1].trim(),
                        Integer.parseInt(parts[2].trim()),
                        parts[3].trim(),
                        parts[4].trim(),
                        Integer.parseInt(parts[5].trim()),
                        Integer.parseInt(parts[6].trim())
                    );
                    
                    if (findById(course.getCourseId()) == null) {
                        courses.add(course);
                    }
                }
            }
            saveCourses();
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error importing courses from CSV: " + e.getMessage());
        }
    }
    
    public Course addCourse(Course course) {
        if (findById(course.getCourseId()) != null) {
            throw new IllegalArgumentException("Course ID already exists");
        }
        courses.add(course);
        saveCourses();
        return course;
    }
    
    public void updateCourse(Course course) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getCourseId().equals(course.getCourseId())) {
                courses.set(i, course);
                saveCourses();
                return;
            }
        }
    }
    
    public void deleteCourse(String courseId) {
        courses.removeIf(c -> c.getCourseId().equals(courseId));
        saveCourses();
    }
    
    public Course findById(String courseId) {
        for (Course course : courses) {
            if (course.getCourseId().equals(courseId)) {
                return course;
            }
        }
        return null;
    }
    
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }
    
    public List<Course> searchCourses(String keyword) {
        List<Course> results = new ArrayList<>();
        String lower = keyword.toLowerCase();
        for (Course course : courses) {
            if (course.getCourseId().toLowerCase().contains(lower) ||
                course.getCourseName().toLowerCase().contains(lower) ||
                course.getInstructor().toLowerCase().contains(lower)) {
                results.add(course);
            }
        }
        return results;
    }
    
    public List<Course> getCoursesBySemester(String semester) {
        List<Course> results = new ArrayList<>();
        for (Course course : courses) {
            if (course.getSemester().equalsIgnoreCase(semester)) {
                results.add(course);
            }
        }
        return results;
    }
    
    public List<Course> getCoursesByInstructor(String instructor) {
        List<Course> results = new ArrayList<>();
        for (Course course : courses) {
            if (course.getInstructor().equalsIgnoreCase(instructor)) {
                results.add(course);
            }
        }
        return results;
    }
    
    public List<String> getAllSemesters() {
        Set<String> semesters = new HashSet<>();
        for (Course course : courses) {
            semesters.add(course.getSemester());
        }
        return new ArrayList<>(semesters);
    }
    
    public List<String> getAllInstructors() {
        Set<String> instructors = new HashSet<>();
        for (Course course : courses) {
            instructors.add(course.getInstructor());
        }
        return new ArrayList<>(instructors);
    }
}
