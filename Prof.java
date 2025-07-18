package com.example.ErpManagement;
import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

class Prof extends ERPdesign {
    private String name;
    private String email;
    private String password;
    private List<ShowCourses> courses = new ArrayList<>();


    public Prof(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public String NameFetch() {
        return name;
    }

    public String Email_Locate() {
        return email;
    }
    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }
    public void NewCourse(ShowCourses course) {
        courses.add(course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    public void CoursesMenu() {
        if (courses.isEmpty()) {
            System.out.println("\nNo courses assigned.");
        } else {
            System.out.println("\nYour Courses:");
            for (ShowCourses course : courses) {
                System.out.println("Code: " + course.locate_course_code() + ", Title: " + course.CoursenameFetch());
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Prof prof = (Prof) obj;
        return email.equals(prof.email);
    }

    public static Prof ProfLocator(String email) {
        for (Prof prof : list_Of_Profs) {
            if (prof.Email_Locate().equals(email)) {
                return prof;
            }
        }
        return null;
    }

    public void viewEnrolledStudents(String courseCode) {
        ShowCourses course = FetchCourses(courseCode);
        if (course != null) {
            System.out.println("Enrolled Students:");
            if (course.EnrolledStudentsFetch().isEmpty()) {
                System.out.println("No students are enrolled");
            } else {
                for (Student student : course.EnrolledStudentsFetch()) {
                    System.out.println(student.NameFetch() + " -> " + student.Email_Locate());
                }
            }
        } else {
            System.out.println("Course doesn't exist!");
        }
    }

    public static void CourseDetailsManagement(Prof prof, Scanner scanner) {
        System.out.println("\n=========== Manage Course Details ===========");
        System.out.println("Enter course code:");
        String courseCode = scanner.nextLine();
        ShowCourses course = ERPdesign.Courses_fetch(courseCode, ERPdesign.List_Of_Courses);
        if (course != null && course.ProfFetch() != null && course.ProfFetch().equals(prof)) {
            System.out.println("1 -> Update Class Timings");
            System.out.println("2 -> Update Syllabus");
            System.out.println("3 -> Update Enrollment Limits");
            System.out.println("4 -> Update Credits");
            System.out.println("5 -> Update Office Hours");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" :
                {
                    System.out.println("Enter class timings:");
                    String classTimings = scanner.nextLine();
                    course.ClasstimingsSet(classTimings);
                    System.out.println("Class timings updated!");
                    break;
                }
                case "2" :
                {

                    System.out.println("Enter syllabus:");
                    String syllabus = scanner.nextLine();
                    course.Syllabuscall(syllabus);
                    System.out.println("Syllabus updated!");
                    break;
                }
                case "3":
                {
                    System.out.println("Enter enrollment limit:");
                    int limit = Integer.parseInt(scanner.nextLine());
                    course.EnrollmentLimitSet(limit);
                    System.out.println("Enrollment limit updated!");
                    break;
                }
                case "4" :
                {

                    System.out.println("Enter credits");
                    int credits = Integer.parseInt(scanner.nextLine());
                    course.CreditsSet(credits);
                    System.out.println("Credits updated!");
                    break;
                }
                case "5" :
                {
                    System.out.println("Enter office hours:");
                    String officeHours = scanner.nextLine();
                    course.OfficeHoursSet(officeHours);
                    System.out.println("Office hours updated!");
                    break;
                }
                default : System.out.println("Invalid choice!");
            }
        } else {
            System.out.println("Course not found, or you're not assigned to this.");
        }
    }

    public void Professors_Menu(Prof prof, Scanner scanner) {
        boolean Executing = true;
        while (Executing) {
            System.out.println("\n========== Professor Menu ==========");
            System.out.println("1 -> View Courses");
            System.out.println("2 -> View Enrolled Students");
            System.out.println("3 -> Course Details Management");
            System.out.println("4 -> Logout");
            System.out.println("5 -> View Feedback");
            System.out.println("6 -> Assign TA");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" :
                        prof.CoursesMenu();
                        break;
                case "2" :
                        System.out.println("Enter the course code:");
                        String courseCode = scanner.nextLine();
                        prof.viewEnrolledStudents(courseCode);
                        break;

                case "3" : {
                        CourseDetailsManagement(prof, scanner);
                        break;
                }
                case "4" : {
                    Executing = false;
                    System.out.println(".....Logging out..... <3");
                    break;
                }

                case "5":{
                    System.out.println("Enter course code to view feedback:");
                    courseCode = scanner.nextLine();
                    ShowCourses course = ERPdesign.FetchCourses(courseCode);
                    if (course != null) {
                        prof.viewFeedback(course);
                    } else {
                        System.out.println("Course not found.");
                    }
                    break;
                }

                case "6":{
                    System.out.println("Enter the TA's email: ");
                    String TAemail = scanner.nextLine();
                    TA selectedTA = ERPdesign.TALocator(TAemail);
                    if (selectedTA == null) {
                        System.out.println("TA not found.");
                        break;
                    }

                    System.out.println("Enter course code for the assignment: ");
                    courseCode = scanner.nextLine();

                    ShowCourses selectedCourse = ERPdesign.Courses_fetch(courseCode, ERPdesign.List_Of_Courses);
                    if (selectedCourse == null) {
                        System.out.println("Course not found.");
                        break;
                    }
                    assignTA(selectedTA, selectedCourse);
                    break;
                }
                default : System.out.println("Invalid choice!");
            }
        }
    }


    public void viewFeedback(ShowCourses course)
    {
        if (!this.courses.contains(course)) {
            System.out.println("You can not view feedback for this course.");
            return;
        }
        System.out.println("==== Feedback for " + course.CoursenameFetch() + " ====");
        for (Feedback<?> feedback : course.getFeedbackList()) {
            System.out.println(feedback.toString());
        }
    }

    public void assignTA(TA ta, ShowCourses course) {
        if (this.courses.contains(course)) {
            ta.assignToCourse(course);
            System.out.println("TA  assigned");
        } else {
            System.out.println("You are not assigned to teach this course!");
        }
    }
}
