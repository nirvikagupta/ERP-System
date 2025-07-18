package com.example.ErpManagement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.example.ErpManagement.Complaint.Complaints_listing;

class InvalidCourse extends Exception {
    public InvalidCourse(String message) {
        super(message);
    }
}

class PrerequisiteNotMet extends Exception {
    public PrerequisiteNotMet(String message) {
        super(message);
    }
}

class ExceededCourseLimit extends Exception {
    public ExceededCourseLimit(String message) {
        super(message);
    }
}

class DropDeadlinePassed extends Exception {
    public DropDeadlinePassed(String message) {
        super(message);
    }
}


class Student extends ERPdesign {
    String email;
    private String name;
    String password;
    private List<ShowCourses> Courses_registered = new ArrayList<>();
    private Map<ShowCourses, String> grades = new HashMap<>();
    private List<ShowCourses> Course_completed = new ArrayList<>();
    private int semester = 1; //default first sem
    private double SGPA_sem1 = 0.0;
    private double SGPA_sem2 = 0.0;


    public void NewName(String name) {
        this.name = name;
    }

    public String getName(Student student) {
        return this.name;
    }

    public void NewPassword(String password) {
        this.password = password;
    }
    public List<ShowCourses> Registered_courses_Fetch() {
        return Courses_registered;
    }
    public String NameFetch() {
        return name;
    }

    public void NewGrade(ShowCourses course, String grade) {
        grades.put(course, grade);
    }

    public Student(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String Email_Locate() {
        return email;
    }

    public boolean login(String email, String password) {
        return this.email.equals(email) && this.password.equals(password);
    }

    static void Menu_For_Students(Student student, Scanner scanner) {
        boolean Executing = true;
        while (Executing) {
            System.out.println("\n========== Student Menu ==========");
            System.out.println("1 -> View Available Courses");
            System.out.println("2 -> Register for Courses");
            System.out.println("3 -> Drop Courses");
            System.out.println("4 -> View Schedule");
            System.out.println("5 -> Track Academic Progress");
            System.out.println("6 -> Complaints");
            System.out.println("7 -> Logout");
            System.out.println("8 -> Feedback for courses");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    student.Current_courses_list(ERPdesign.List_Of_Courses);
                    break;
                case "2": {
                    System.out.println("Enter your semester:");
                    int semester = Integer.parseInt(scanner.nextLine());
                    student.Courses_Registered(semester, ERPdesign.List_Of_Courses);
                    break;
                }
                case "3": {
                    System.out.println("Enter course code:");
                    String courseCode = scanner.nextLine();
                    ShowCourses course = ERPdesign.FetchCourses(courseCode);
                    if (course != null) {
                        student.CourseDropping(course);
                    } else {
                        System.out.println("Course not found.");
                    }
                    break;
                }
                case "4": {
                    student.Schedule_display();
                    break;
                }

                case "5":
                    student.trackAcademicProgress();
                    break;
                case "6":
                    Complaints_listing(student, scanner);
                    break;
                case "7": {
                    Executing = false;
                    System.out.println("Logging out.");
                    break;
                }

                case "8":{
                    System.out.println("Enter course code for feedback:");
                    String courseCode = scanner.nextLine();
                    ShowCourses course = ERPdesign.FetchCourses(courseCode);
                    if (student.Registered_courses_Fetch().contains(course) || student.Course_completed.contains(course)){
                        student.giveFeedback(course, scanner);
                    }

                    else{
                        System.out.println("Course not found");
                    }
                    break;

                }
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public void Current_courses_list(List<ShowCourses> List_Courses) {
        System.out.println("\n=========== Available Courses ==========");
        for (ShowCourses course : List_Courses) {
            System.out.println("Code: " + course.locate_course_code() + ", Title: " + course.CoursenameFetch() +
                    ", Credits: " + course.CreditsFetch() + ", Prerequisites: " +
                    course.PrerequisitesFetch() + ", Semester: " + course.SemesterFetch());
        }
    }

    public void Courses_Registered(int semester, List<ShowCourses> courseCatalog) {
        if (this.semester != semester) {
            System.out.println("Wrong Semester");
        }

        System.out.println("\nRegister for Courses:");
        List<ShowCourses> CoursesEnlisted = new ArrayList<>();
        for (ShowCourses course : courseCatalog) {
            if (course.SemesterFetch() == semester) {
                CoursesEnlisted.add(course);
            }
        }

        System.out.println("=========== Available Courses ==========");
        for (ShowCourses course : CoursesEnlisted) {
            System.out.println("Code: " + course.locate_course_code() + ", Title: " + course.CoursenameFetch());
        }

        System.out.println("Enter course codes for registering:");
        String[] chosenCourseCodes = new Scanner(System.in).nextLine().split(",");

        List<ShowCourses> chosenCourses = new ArrayList<>();
        for (String code : chosenCourseCodes) {
            try {
                ShowCourses course = Courses_fetch(code.trim(), CoursesEnlisted);
                if (course != null) {
                    if (Courses_registered.contains(course)) {
                        throw new InvalidCourse("Already registered");
                    } else {
                        chosenCourses.add(course);
                    }
                } else {
                    throw new InvalidCourse("Course Not Found");
                }
            } catch (InvalidCourse e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            if (RegistrationChecking(chosenCourses, semester)) {
                for (ShowCourses course : chosenCourses) {
                    if (course.getCurrentEnrollment() < course.maxEnrollment){
                        if (Courses_registered.size() < 5 && semester==course.SemesterFetch()) {
                        Courses_registered.add(course);
                        course.StudentNew(this);
                    } }else {
                        throw new ExceededCourseLimit("Exceeded Course Limit or wrong sem.");
                    }
                }
                System.out.println("Registration completed");
            } else {
                throw new PrerequisiteNotMet("Error!");
            }
        }
        catch (PrerequisiteNotMet e)
        {
            System.out.println(e.getMessage());
        }
        catch (ExceededCourseLimit e) {
            System.out.println(e.getMessage());}

        }

    public void Jump_nextsem() {
        if (everygradesubmitted()) {
            if (semester == 1) {
                SGPA_sem1 = SGPA_calculation(1);
            } else if (semester == 2) {
                SGPA_sem2 = SGPA_calculation(2);
            }

            Courses_registered.clear();
            semester++;
        }
    }

    private boolean everygradesubmitted() {
        for (ShowCourses course : Courses_registered) {
            if (!grades.containsKey(course)) {
                return false;
            }
        }
        return true;
    }

    public boolean check_for_jumpnextsem() {
        for (ShowCourses course : Courses_registered) {
            if (course.SemesterFetch() == this.semester) {
                if (!grades.containsKey(course)) {
                    return false;
                }
            }
        }
        return true;
    }

    private double SGPA_calculation(int semester) {
        double AggPoints = 0;
        int AggCredits = 0;

        for (ShowCourses course : Courses_registered) {
            if (course.SemesterFetch() == semester && grades.containsKey(course)) {
                double gradePoints = FetchGrades(grades.get(course));
                AggPoints += gradePoints * course.CreditsFetch();
                AggCredits += course.CreditsFetch();
            }
        }

        return AggCredits > 0 ? AggPoints / AggCredits : 0;
    }

    private boolean RegistrationChecking(List<ShowCourses> selectedCourses, int semester) {
        int AggCredits = 0;

        for (ShowCourses course : selectedCourses) {
            AggCredits += course.CreditsFetch();


            for (ShowCourses prereq : course.PrerequisitesFetch()) {
                if (!Course_completed.contains(prereq)) {
                    System.out.println("Prerequisite not completed!");
                    return false;
                }
            }
        }

        if (AggCredits > 20) {
            return false;
        }

        return true;
    }

        public void CourseDropping(ShowCourses course) {
            LocalDate currentDate = LocalDate.now();
            int semester = course.SemesterFetch();
            LocalDate deadline;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            if (semester == 1) {
                deadline = LocalDate.of(2024, 8, 11);
            } else if (semester == 2) {
                deadline = LocalDate.of(2025, 1, 5);
            } else {
                System.out.println("Invalid semester");
                return;
            }

            try {
                if (currentDate.isBefore(deadline) || currentDate.isEqual(deadline)) {
                    if (Courses_registered.remove(course)) {
                        course.StudentDelete(this);
                        System.out.println("Course dropped: " + course.CoursenameFetch());
                    } else {
                        System.out.println("Course not found in registered courses.");
                    }
                } else {
                    throw new DropDeadlinePassed("Deadline Passed, Can't drop this course");
                }
            } catch (DropDeadlinePassed e) {
                System.out.println(e.getMessage());
            }
        }

    public void Schedule_display() {
        System.out.println("\n========== Your Schedule ===========");
        for (ShowCourses course : Courses_registered) {
            System.out.println("Course: " + course.locate_course_code() + ", Title: " + course.CoursenameFetch() +
                    ", Timings: " + course.ClasstimingsFetch() + ", Professor: " + (course.ProfFetch() != null ? course.ProfFetch().NameFetch() : "Not Assigned"));
        }
    }

    public void trackAcademicProgress() {
        if (semester == 1) {
            SGPA_sem1 = SGPA_calculation(1);
            System.out.println("SGPA: " + SGPA_sem1);
            System.out.println("CGPA: " + SGPA_sem1);
        } else if (semester == 2) {
            SGPA_sem2 = SGPA_calculation(2);
            double cgpa = (SGPA_sem1 + SGPA_sem2) / 2.0;
            System.out.println("SGPA 1: " + SGPA_sem1);
            System.out.println("SGPA 2: " + SGPA_sem2);
            System.out.println("CGPA: " + cgpa);
        } else {
            System.out.println("No progress.");
        }
    }

    private double FetchGrades(String grade) {
        return switch (grade.toUpperCase()) {
            default -> 0.0;
            case "F" -> 0.0;
            case "D" -> 4.0;
            case "C" -> 6.0;
            case "B" -> 8.0;
            case "A" -> 10.0;
        };
    }

    public void Complaint_filing(String description) {
        Complaint complaint = new Complaint(description, this);
        ERPdesign.ComplaintsImport(complaint);
        System.out.println("Complaint filed.");
    }

    public void checkComplaints() {
        System.out.println("\nYour filed Complaints:");
        for (Complaint complaint : ERPdesign.getList_Of_Complaints()) {
            if (complaint.StudentFetch().equals(this)) {
                System.out.println("Serial Number: " + complaint.FetchSlno() + ", Complaint: " + complaint.DescriptionFetch() + ", Status: " + complaint.StatusFetch());
            }
        }
    }

    public void giveFeedback(ShowCourses course, Scanner scanner) {
        if (!Course_completed.contains(course) || !Registered_courses_Fetch().contains(course)) {
            System.out.println("===Enter feedback type===" +  "\n"+ "1 for Numeric (1-5) Rating" + "\n"+ "2 for Textual Feedback");
            String feedbackType = scanner.nextLine();

            if (feedbackType.equals("1")) {
                System.out.println("Enter your rating (1-5):");
                int rating = Integer.parseInt(scanner.nextLine());
                Feedback<Integer> feedback = new Feedback<>(this, course, rating);
                course.addFeedback(feedback);
                System.out.println("Your rating has been submitted, Thank You~~");
            } else if (feedbackType.equals("2")) {
                System.out.println("Enter your feedback:");
                String fb = scanner.nextLine();
                Feedback<String> feedback = new Feedback<>(this, course, fb);
                course.addFeedback(feedback);
                System.out.println("Your rating has been submitted, Thank You~~");
            } else {
                System.out.println("Invalid feedback type.");
            }
        }

    }

}


