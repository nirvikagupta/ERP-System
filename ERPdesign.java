package com.example.ErpManagement;

import java.util.*;

import static com.example.ErpManagement.Admin.Admin_Menu;
import static com.example.ErpManagement.Prof.*;
import static com.example.ErpManagement.Student.Menu_For_Students;
import static com.example.ErpManagement.TA.Menu_For_TAs;

class InvalidLogin extends Exception {
    public InvalidLogin(String message) {
        super(message);
    }
}

public class ERPdesign {
    private static  String password_for_admin = "abc123";
    private static List<Student> Students = new ArrayList<>();
    private static List<TA> TAs = new ArrayList<>();
    private static List<Prof> Profs = new ArrayList<>();
    public static List<Prof> list_Of_Profs = new ArrayList<>();
    private static List<Complaint> List_Of_Complaints = new ArrayList<>();
    static List<ShowCourses> List_Of_Courses = new ArrayList<>();
    private static Admin admin = new Admin(password_for_admin);
    public static void ComplaintsImport(Complaint complaint) {
        List_Of_Complaints.add(complaint);
    }
    public static List<Complaint> getList_Of_Complaints() {
        return List_Of_Complaints;
    }


    private static void SignIn_For_Students(Scanner scanner) {
        System.out.println("\nEnter Your Email:");
        String email = scanner.nextLine();

        System.out.println("Enter Your Name:");
        String name = scanner.nextLine();

        if (StudentLocator(email) != null) {
            System.out.println("There is already an account with this Email!");
            return;
        }

        System.out.println("Enter a new password:");
        String password = scanner.nextLine();

        Student tempStudent = new Student(name, email, password);
        Students.add(tempStudent);
        System.out.println("Congrats! Your account has been created successfully!");
    }

    private static void SignIn_For_Professors(Scanner scanner) {

        System.out.println("\nEnter your email Prof:");
        String email = scanner.nextLine();
        System.out.println("Enter your name Prof:");
        String name = scanner.nextLine();

        if (ProfLocator(email) != null) {
            System.out.println("There already exists an account with this email, please try again Prof.");
            return;
        }
        System.out.println("Enter a password Prof:");
        String password = scanner.nextLine();
        Prof tempProf = new Prof(name, email, password);
        Profs.add(tempProf);
        System.out.println("Congratulations! your account has been successfully created Prof!");
    }


    private static void Login_For_Students(Scanner scanner) {
        System.out.println("\nEnter your Email for login:");
        String Email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        Student student = StudentLocator(Email);
        try
        {
            if (student != null && student.login(Email, password)) {
                Menu_For_Students(student, scanner);
            } else {
                throw new InvalidLogin("Invalid credentials.");            }
        }
        catch (InvalidLogin e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void Login_For_TA(Scanner scanner) {
        System.out.println("\nEnter your Email for login:");
        String Email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        TA ta = TALocator(Email);

        try
        {
            if (ta != null && ta.login(Email, password)) {
                Menu_For_TAs(ta, scanner);
            } else {
                throw new InvalidLogin("Invalid credentials.");            }
        }
        catch (InvalidLogin e)
        {
            System.out.println(e.getMessage());
        }
    }

    private static void Login_For_Prof(Scanner scanner) {
        System.out.println("\nEnter your email for login:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        Prof prof = ProfLocator(email);
        try
        {
            if (prof != null && prof.login(email, password)) {
                prof.Professors_Menu(prof, scanner);
            } else {
                throw new InvalidLogin("Invalid credentials.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void Login_For_Admin(Scanner scanner) {
        System.out.println("\nAdmin's Password:");
        String password = scanner.nextLine();
        try
        {
            if (password_for_admin.equals(password)) {
                Admin_Menu(scanner);
            } else {
                throw new InvalidLogin("Invalid credentials.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static ShowCourses FetchCourses(String code) {
        for (ShowCourses course : List_Of_Courses) {
            if (course.locate_course_code().equals(code)) {
                return course;
            }
        }
        return null;
    }

    private static void students_sample() {
        Students.add(new Student("Amy", "amy000", "amy"));
        Students.add(new Student("Lisa", "lisa000", "lisa"));
        Students.add(new Student("Joy", "joy000", "joy"));
    }

    public static void prof_sample() {
        list_Of_Profs.add(new Prof("Taylor", "taylor000", "taylor"));
        list_Of_Profs.add(new Prof("Julie", "julie000", "julie"));
    }
    
    private static void courses_sample() {
        ShowCourses cse101 = new ShowCourses("CSE101", "IP", 4, new ArrayList<>(), 1);
        ShowCourses ent101 = new ShowCourses("ENT101", "SOE", 4, new ArrayList<>(), 1);
        ShowCourses mth101 = new ShowCourses("MTH101", "LA", 4, new ArrayList<>(), 1);
        ShowCourses ssh101 = new ShowCourses("SSH101", "COMMS", 4, new ArrayList<>(), 1);
        ShowCourses csd101 = new ShowCourses("CSD101", "HCI", 4, new ArrayList<>(), 1);
        List_Of_Courses.add(cse101);
        List_Of_Courses.add(ent101);
        List_Of_Courses.add(mth101);
        List_Of_Courses.add(ssh101);
        List_Of_Courses.add(csd101);

    }

    public static void ta_sample() {
        TAs.add(new TA("John", "john000", "john", admin));
        TAs.add(new TA("Emily", "emily000", "emily", admin));
    }

    static Student StudentLocator(String email) {
        for (Student student : Students) {
            if (student.Email_Locate().equals(email)) {
                return student;
            }
        }
        return null;
    }

    static TA TALocator(String email) {
        for (TA ta : TAs) {
            if (ta.Email_Locate().equals(email)) {
                return ta;
            }
        }
        return null;
    }

    static ShowCourses Courses_fetch(String code_of_course, List<ShowCourses> list_of_courses) {
        for (ShowCourses course : List_Of_Courses) {
            if (course.locate_course_code().equals(code_of_course)) {
                return course;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        courses_sample();
        students_sample();
        prof_sample();
        ta_sample();


        String currentdate = "160824";
        Scanner scanner = new Scanner(System.in);
        boolean Executing = true;
        while (Executing) {
            System.out.println("========== Main Menu ==========");
            System.out.println("1 -> Login as Student");
            System.out.println("2 -> Login as Professor");
            System.out.println("3 -> Sign Up as Student");
            System.out.println("4 -> Sign Up as Professor");
            System.out.println("5 -> Login as Administrator");
            System.out.println("6 -> Exit");
            System.out.println("7 -> Login as TA");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" :
                    Login_For_Students(scanner);
                    break;
                case "2" :
                    Login_For_Prof(scanner);
                    break;
                case "3" :
                    SignIn_For_Students(scanner);
                    break;
                case "4" :
                    SignIn_For_Professors(scanner);
                    break;
                case "5" :
                    Login_For_Admin(scanner);
                    break;
                case "6" : {
                    Executing = false;
                    System.out.println("......Exiting application.....");
                    break;
                }

                case "7":{
                    Login_For_TA(scanner);
                    break;
                }
                default : System.out.println("Invalid choice.");
            }
        }
    }
}

