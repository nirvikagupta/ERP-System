package com.example.ErpManagement;

import java.util.List;
import java.util.Scanner;

class Complaint extends ERPdesign {
    private String status;
    private Student student;
    private static int slnoCounter = 1;
    private int Slno;
    private String complaintnote;

    public int FetchSlno() {
        return Slno;
    }
    public String DescriptionFetch() {
        return complaintnote;
    }
    public Student StudentFetch() {
        return student;
    }
    public String StatusFetch() {
        return status;
    }

    public void NewStatus(String status) {
        this.status = status;
    }

    public Complaint(String complaintnote, Student student) {
        this.Slno = slnoCounter++;
        this.complaintnote = complaintnote;
        this.student = student;
        this.status = "Pending";
    }

    private static void List_Of_Complaints() {
        List<Complaint> complaints = ERPdesign.getList_Of_Complaints();
        if (complaints.isEmpty()) {
            System.out.println("No complaints.");
        } else {
            for (Complaint complaint : complaints) {
                System.out.println(complaint);
            }
        }
    }

    private static void Updating_of_Complaints(Scanner scanner) {
        List<Complaint> complaints = ERPdesign.getList_Of_Complaints();
        if (complaints.isEmpty()) {
            System.out.println("No complaints");
            return;
        }
        System.out.println("Enter the complaint serial number: ");
        int ComplaintSlno = Integer.parseInt(scanner.nextLine());
        Complaint Complaint_chosen = complaints.stream()
                .filter(c -> c.FetchSlno() == ComplaintSlno)
                .findFirst()
                .orElse(null);

        for (Complaint complaint : complaints) {
            System.out.println("Serial Number: " +
                    complaint.FetchSlno() +
                    ", Complaint: " +
                    complaint.DescriptionFetch() +
                    ", Status: " +
                    complaint.StatusFetch() +
                    ", By Student: " +
                    complaint.StudentFetch().NameFetch());
        }

        if (Complaint_chosen != null) {
            System.out.println("Enter new status Resolved/Pending:");
            String newStatus = scanner.nextLine();
            Complaint_chosen.NewStatus(newStatus);
            System.out.println("Complaint status updated!");
        } else {
            System.out.println("Complaint not found!");
        }
    }

    static void Handling_of_Complaints(Scanner scanner) {
        boolean Executing = true;
        while (Executing) {
            System.out.println("\n========== Complaints Handling ==========");
            System.out.println("1 -> View Complaints");
            System.out.println("2 -> Update Status of Complaints");
            System.out.println("3 -> Back to Menu");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" :
                    List_Of_Complaints();
                    break;
                case "2" :
                    Updating_of_Complaints(scanner);
                    break;
                case "3" :
                    Executing = false;
                    break;
                default : System.out.println("Invalid choice.");
            }
        }
    }

    static void Complaints_listing(Student student, Scanner scanner) {
        boolean Executing = true;
        while (Executing) {
            System.out.println("\n========= Complaint Menu =========");
            System.out.println("1 -> Submit Complaint");
            System.out.println("2 -> View Complaints");
            System.out.println("3 -> Back to Main Menu");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" :
                {
                    System.out.println("Enter complaint:");
                    String complaintDescription = scanner.nextLine();
                    student.Complaint_filing(complaintDescription);
                    break;
                }
                case "2" :
                    student.checkComplaints();
                    break;
                case "3" :
                    Executing = false;
                    break;
                default : System.out.println("Invalid choice.");
            }
        }
    }

    @Override
    public String toString() {
        return "Complaint ID: " + Slno + ", Description: " + complaintnote + ", Status: " + status +
                ", Submitted by: " + student.NameFetch();
    }
}
