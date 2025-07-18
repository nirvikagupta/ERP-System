package com.example.ErpManagement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ShowCourses extends ERPdesign {
    private String courseCode;
    private String courseName;
    private Prof prof;
    private List<ShowCourses> prerequisites;
    private String syllabus;
    private String classTimings;
    private String officeHours;
    private int semester;
    private List<Student> enrolledStudents;
    private int credits;
    private int limit;
    private ArrayList feedbackList;
    private Map<String, String> studentGrades;
    private String dropDeadline;
    int maxEnrollment = 50;


    public ShowCourses(String courseCode, String courseName, int credits, List<ShowCourses> prerequisites, int semester) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.enrolledStudents = new ArrayList<>();
        this.prerequisites = prerequisites;
        this.semester = semester;
        this.feedbackList = new ArrayList<>();
        this.studentGrades = new HashMap<>();

    }

    public void assignGrade(String student_name, String grade) {
        studentGrades.put(student_name, grade);
        System.out.println(student_name + " for course " + courseCode);
    }

    public Map<String, String> getStudentGrades() {
        return new HashMap<>(studentGrades);
    }


    public String ClasstimingsFetch() {
        return classTimings;
    }

    public void ClasstimingsSet(String classTimings) {
        this.classTimings = classTimings;
    }

    public Prof ProfFetch() {
        return prof;
    }

    public void ProfSet(Prof prof) {
        this.prof = prof;
    }

    public void Syllabuscall(String syllabus) {
        this.syllabus = syllabus;
    }


    public String locate_course_code() {
        return this.courseCode;
    }

    public String CoursenameFetch() {
        return courseName;
    }

    public String getDropDeadline() {
        return dropDeadline;
    }

    public void setDropDeadline(String dropDeadline) {
        this.dropDeadline = dropDeadline;
    }

    public int CreditsFetch() {
        return credits;
    }

    public void CreditsSet(int credits) {
        this.credits = credits;
    }

    public List<Student> EnrolledStudentsFetch() {
        return enrolledStudents;
    }

    public void EnrollmentLimitSet(int limit) {
        this.limit = limit;
    }

    public List<ShowCourses> PrerequisitesFetch() {
        return prerequisites;
    }

    public int SemesterFetch() {
        return semester;
    }

    public void StudentDelete(Student student) {
        enrolledStudents.remove(student);
    }

    public void StudentNew(Student student) {
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
            System.out.println(student.NameFetch() + " has been enrolled in " + this.courseName);
        } else {
            System.out.println(student.NameFetch() + " is already enrolled in this course.");
        }
    }

    public void OfficeHoursSet(String officeHours) {
        this.officeHours = officeHours;
    }

    public void addFeedback(Feedback<?> feedback) {
        feedbackList.add(feedback);
    }

    public List<Feedback<?>> getFeedbackList() {
        return feedbackList;
    }

    public int getCurrentEnrollment() {
        return enrolledStudents.size();
    }
}
