package com.example.ErpManagement;
import java.util.*;

import static com.example.ErpManagement.Admin.assignGrade;

public class TA extends Student {
    private static List<ShowCourses> assignedCourse;
    private static Admin admin;


    public TA(String name, String email, String password,Admin admin) {
        super(name, email, password);
        this.admin = admin;
        this.assignedCourse = new ArrayList<>();

    }

    public void assignToCourse(ShowCourses course) {
        assignedCourse.add(course);
        System.out.println("TA has been assigned to this couse");
    }
    public static void viewStudentGrades() {
        if (assignedCourse.isEmpty()) {
            System.out.println("You are not assigned to any courses yet.");
        } else {
            Map<String, Map<String, String>> courseGrades = admin.getCourseGrades();
            System.out.println("Assigned courses:");
            Iterator var1 = assignedCourse.iterator();

            while(var1.hasNext()) {
                ShowCourses course = (ShowCourses)var1.next();
                String courseCode = course.locate_course_code();
                System.out.println("Course: " + courseCode);
                Map<String, String> grades = (Map)courseGrades.get(courseCode);
                if (grades != null) {
                    grades.forEach((studentName, grade) -> {
                        System.out.println("Student: " + studentName + ", Grade: " + grade);
                    });
                } else {
                    System.out.println("No grades available for this course.");
                }
            }

        }
    }
    public static void updateStudentGrade(Scanner scanner) {
        if (assignedCourse.isEmpty()) {
            System.out.println("You are not assigned to any courses yet.");
            return;
        }

        System.out.println("Assigned Courses:");
        for (int i = 0; i < assignedCourse.size(); i++) {
            System.out.println((i + 1) + ". " + assignedCourse.get(i).locate_course_code());
        }

        System.out.print("Select a course number to update the grade: ");
        int courseIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        if (courseIndex < 0 || courseIndex >= assignedCourse.size()) {
            System.out.println("Invalid course selection.");
            return;
        }

        ShowCourses selectedCourse = assignedCourse.get(courseIndex);

        System.out.print("Enter the student's email: ");
        String studentEmail = scanner.nextLine();

        Student studentToGrade = StudentLocator(studentEmail);
        if (studentToGrade != null) {
            System.out.println("Enter course code:");
            String courseCode = scanner.nextLine();
            ShowCourses courseToGrade = FetchCourses(courseCode);

            if (courseToGrade != null && studentToGrade.Registered_courses_Fetch().contains(courseToGrade)) {
                System.out.println("Enter grade:");
                String grade = scanner.nextLine();

                studentToGrade.NewGrade(courseToGrade, grade);
                assignGrade(courseCode, studentToGrade.getName(studentToGrade), grade);

                System.out.println("Grade assigned successfully.");

                if (studentToGrade.check_for_jumpnextsem()) {
                    studentToGrade.Jump_nextsem();
                    System.out.println("Student promoted to the next semester.");
                }
            } else {
                System.out.println("Error: Course not found or student not registered in the course.");
            }
        } else {
            System.out.println("Student not found.");
        }
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    static void Menu_For_TAs(TA ta, Scanner scanner) {
        boolean Executing = true;
        while (Executing) {
            System.out.println("\n========== TA ==========");
            System.out.println("1 -> View Assigned Course Grades");
            System.out.println("2 -> Manage Student Grades");
            System.out.println("3 -> Logout");


        String choice = scanner.nextLine();

        switch (choice) {
            case "1": {
                TA.viewStudentGrades();
                break;
            }
            case "2": {
                TA.updateStudentGrade(scanner);
                break;
            }
            case "3": {
                Executing = false;
                System.out.println("Logging out.");
                break;
            }

        }
    }
}

}