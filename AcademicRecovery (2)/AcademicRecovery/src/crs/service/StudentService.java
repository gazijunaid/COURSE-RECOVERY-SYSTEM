package crs.service;

import crs.model.*;
import crs.util.FileManager;

import java.io.*;
import java.util.*;

public class StudentService {
    
    private static final String STUDENTS_FILE = "students.dat";
    private static final String STUDENT_COURSES_FILE = "student_courses.dat";
    
    private List<Student> students;
    private List<StudentCourse> studentCourses;
    
    private static StudentService instance;
    
    private StudentService() {
        loadStudents();
        loadStudentCourses();
    }
    
    public static StudentService getInstance() {
        if (instance == null) {
            instance = new StudentService();
        }
        return instance;
    }
    
    private void loadStudents() {
        students = FileManager.loadFromBinaryFile(STUDENTS_FILE);
        if (students == null) {
            students = new ArrayList<>();
        }
    }
    
    private void loadStudentCourses() {
        studentCourses = FileManager.loadFromBinaryFile(STUDENT_COURSES_FILE);
        if (studentCourses == null) {
            studentCourses = new ArrayList<>();
        }
    }
    
    public void saveStudents() {
        FileManager.saveToBinaryFile(STUDENTS_FILE, students);
    }
    
    public void saveStudentCourses() {
        FileManager.saveToBinaryFile(STUDENT_COURSES_FILE, studentCourses);
    }
    
    public void importStudentsFromCSV(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    Student student = new Student(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[5].trim(),
                        parts[3].trim(),
                        parts[4].trim()
                    );
                    
                    if (findById(student.getStudentId()) == null) {
                        students.add(student);
                    }
                }
            }
            saveStudents();
        } catch (IOException e) {
            System.err.println("Error importing students from CSV: " + e.getMessage());
        }
    }
    
    public Student addStudent(Student student) {
        if (findById(student.getStudentId()) != null) {
            throw new IllegalArgumentException("Student ID already exists");
        }
        students.add(student);
        saveStudents();
        return student;
    }
    
    public void updateStudent(Student student) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equals(student.getStudentId())) {
                students.set(i, student);
                saveStudents();
                return;
            }
        }
    }
    
    public Student findById(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
    
    public List<Student> searchStudents(String keyword) {
        List<Student> results = new ArrayList<>();
        String lower = keyword.toLowerCase();
        for (Student student : students) {
            if (student.getStudentId().toLowerCase().contains(lower) ||
                student.getFirstName().toLowerCase().contains(lower) ||
                student.getLastName().toLowerCase().contains(lower) ||
                student.getMajor().toLowerCase().contains(lower)) {
                results.add(student);
            }
        }
        return results;
    }
    
    public void enrollStudentInCourse(String studentId, Course course, String semester, int year) {
        StudentCourse sc = new StudentCourse(studentId, course, semester, year);
        studentCourses.add(sc);
        
        Student student = findById(studentId);
        if (student != null) {
            student.addCourse(sc);
            saveStudents();
        }
        saveStudentCourses();
    }
    
    public void updateStudentCourse(StudentCourse studentCourse) {
        for (int i = 0; i < studentCourses.size(); i++) {
            StudentCourse sc = studentCourses.get(i);
            if (sc.getStudentId().equals(studentCourse.getStudentId()) &&
                sc.getCourse().getCourseId().equals(studentCourse.getCourse().getCourseId())) {
                studentCourses.set(i, studentCourse);
                saveStudentCourses();
                return;
            }
        }
    }
    
    public List<StudentCourse> getStudentCourses(String studentId) {
        List<StudentCourse> courses = new ArrayList<>();
        for (StudentCourse sc : studentCourses) {
            if (sc.getStudentId().equals(studentId)) {
                courses.add(sc);
            }
        }
        return courses;
    }
    
    public List<StudentCourse> getStudentCoursesBySemester(String studentId, String semester, int year) {
        List<StudentCourse> courses = new ArrayList<>();
        for (StudentCourse sc : studentCourses) {
            if (sc.getStudentId().equals(studentId) &&
                sc.getSemester().equals(semester) &&
                sc.getYear() == year) {
                courses.add(sc);
            }
        }
        return courses;
    }
    
    public List<Student> getIneligibleStudents() {
        List<Student> ineligible = new ArrayList<>();
        for (Student student : students) {
            loadStudentCoursesForStudent(student);
            if (!student.isEligibleToProgress()) {
                ineligible.add(student);
            }
        }
        return ineligible;
    }
    
    public List<Student> getStudentsWithFailedCourses() {
        List<Student> failed = new ArrayList<>();
        for (Student student : students) {
            loadStudentCoursesForStudent(student);
            if (student.getFailedCoursesCount() > 0) {
                failed.add(student);
            }
        }
        return failed;
    }
    
    private void loadStudentCoursesForStudent(Student student) {
        student.getCourses().clear();
        for (StudentCourse sc : studentCourses) {
            if (sc.getStudentId().equals(student.getStudentId())) {
                student.addCourse(sc);
            }
        }
    }
    
    public double calculateCGPA(String studentId) {
        Student student = findById(studentId);
        if (student != null) {
            loadStudentCoursesForStudent(student);
            return student.calculateCGPA();
        }
        return 0.0;
    }
    
    public boolean isEligibleToProgress(String studentId) {
        Student student = findById(studentId);
        if (student != null) {
            loadStudentCoursesForStudent(student);
            return student.isEligibleToProgress();
        }
        return false;
    }
    
    public void confirmEnrolment(String studentId) {
        Student student = findById(studentId);
        if (student != null) {
            student.setEnrolled(true);
            saveStudents();
        }
    }
    
    public List<StudentCourse> getAllStudentCourses() {
        return new ArrayList<>(studentCourses);
    }
    
    public void assignGrade(String studentId, String courseId, String grade, double examScore, double assignmentScore) {
        for (StudentCourse sc : studentCourses) {
            if (sc.getStudentId().equals(studentId) && 
                sc.getCourse().getCourseId().equals(courseId)) {
                sc.setGrade(grade);
                sc.setExamScore(examScore);
                sc.setAssignmentScore(assignmentScore);
                saveStudentCourses();
                
                Student student = findById(studentId);
                if (student != null) {
                    loadStudentCoursesForStudent(student);
                    saveStudents();
                }
                return;
            }
        }
    }
    
    public void reloadData() {
        loadStudents();
        loadStudentCourses();
        for (Student student : students) {
            loadStudentCoursesForStudent(student);
        }
    }
}
