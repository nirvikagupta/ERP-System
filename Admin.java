package com.example.ErpManagement;

import java.util.*;

import static com.example.ErpManagement.Complaint.Handling_of_Complaints;
import static com.example.ErpManagement.Prof.ProfLocator;

public class Admin extends ERPdesign {
    private String password;
    private static Map<String, Map<String, String>>courseGrades;


    public Admin(String password) {
        this.password = password;
        this.courseGrades = new HashMap<>();
    }

    public static void Managing_of_courses(Scanner scanner) {
        System.out.println("\n==========Course Management=========");
        System.out.println("1 -> Add a Course");
        System.out.println("2 -> Delete a Course");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
            {
                System.out.println("Enter the required course code:");
                String courseCode = scanner.nextLine();
                System.out.println("Enter the course name:");
                String courseName = scanner.nextLine();
                System.out.println("Enter which semester:");
                int semester = Integer.parseInt(scanner.nextLine());
                System.out.println("Enter the number of credits (2 or 4):");
                int credits = Integer.parseInt(scanner.nextLine());
                List<ShowCourses> prerequisites = new ArrayList<>();
                System.out.println("Does this course have prerequisites? (Y -> Yes, N -> No)");
                String Prereq_exists = scanner.nextLine();
                if (Prereq_exists.equalsIgnoreCase("Y")) {
                    System.out.println("Enter prerequisite course codes:");
                    String[] PrerequisiteCodes = scanner.nextLine().split(",");
                    for (String code : PrerequisiteCodes) {
                        ShowCourses PrerequisitesCourses = FetchCourses(code.trim());
                        if (PrerequisitesCourses != null) {
                            prerequisites.add(PrerequisitesCourses);
                        }

                        else {
                            System.out.println("Prerequisite course " + code.trim() + " not found.");
                        }
                    }
                }
                List_Of_Courses.add(new ShowCourses(courseCode, courseName, credits, prerequisites, semester));
                System.out.println("Course added!");
                break;
            }
            case "2" :
            {
                System.out.println("Enter course code to delete:");
                String codeToDelete = scanner.nextLine();
                ShowCourses courseToDelete = FetchCourses(codeToDelete);
                if (courseToDelete != null) {
                    List_Of_Courses.remove(courseToDelete);
                    System.out.println("Course removed!");
                } else {
                    System.out.println("Course not found.");
                }
                break;
            }
            default : System.out.println("Invalid choice.");
        }
    }

    static void Admin_Menu(Scanner scanner) {
        boolean Executing = true;
        while (Executing) {
            System.out.println("\n=========== Administrator Menu ==========");
            System.out.println("1 -> Manage Courses");
            System.out.println("2 -> Manage Student Records");
            System.out.println("3 -> Assign Professors to Courses");
            System.out.println("4 -> Handle Complaints");
            System.out.println("5 -> Logout");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" :
                    Managing_of_courses(scanner);
                    break;
                case "2" :
                    Manage_students_records(scanner);
                    break;
                case "3" :
                    Professors_assigned_to_courses(scanner);
                    break;
                case "4" :
                    Handling_of_Complaints(scanner);
                    break;
                case "5" : {
                    Executing = false;
                    System.out.println(".....Logging out.....");
                    break;
                }
                default : System.out.println("Invalid choice.");
            }
        }
    }


    static void Manage_students_records(Scanner scanner) {
        System.out.println("\n========== Student Records Management ===========");
        System.out.println("1 -> Assign Grades");
        System.out.println("2 -> Update Student Information");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" :
            {
                System.out.println("Enter student email:");
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
                        System.out.println("Grade assigned.");
                        if (studentToGrade.check_for_jumpnextsem()) {
                            studentToGrade.Jump_nextsem();
                        }
                    } else {
                        System.out.println("Error");
                    }
                } else {
                    System.out.println("Student not found.");
                }
                break;
            }
            case "2" :
            {
                System.out.println("Enter student email:");
                String email = scanner.nextLine();
                Student student = StudentLocator(email);
                if (student != null) {
                    System.out.println("Enter new name:");
                    String name = scanner.nextLine();
                    if (!name.isEmpty()) {
                        student.NewName(name);
                    }

                    System.out.println("Enter new password:");
                    String password = scanner.nextLine();
                    if (!password.isEmpty()) {
                        student.NewPassword(password);
                    }

                    System.out.println("Student information updated!");
                } else {
                    System.out.println("Student not found.");
                }
                break;
            }
            default : System.out.println("Invalid choice.");
        }
    }

    static void Professors_assigned_to_courses(Scanner scanner) {
        System.out.println("Email of the professor:");
        String professorEmail = scanner.nextLine();
        Prof prof = ProfLocator(professorEmail);
        if (prof == null) {
            System.out.println("Professor not found.");
            return;
        }
        System.out.println("Enter the course code to assign:");
        String courseCode = scanner.nextLine();
        ShowCourses course = ERPdesign.FetchCourses(courseCode);
        if (course != null) {
            prof.NewCourse(course);
            course.ProfSet(prof);
            System.out.println("Course has been assigned to Professor");
        } else {
            System.out.println("Course not found.");
        }
    }

    public static void assignGrade(String courseCode, String studentName, String grade) {
        courseGrades.putIfAbsent(courseCode, new HashMap<>());
        courseGrades.get(courseCode).put(studentName, grade);
    }

    public Map<String, Map<String, String>> getCourseGrades() {
        return courseGrades;
    }
}
