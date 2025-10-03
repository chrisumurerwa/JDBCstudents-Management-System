package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String url = "jdbc:postgresql://localhost:5432/StudentManagementSystem";
        String user = "postgres";
        String password = "chris2020";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n== Student Management System ==");
                System.out.println("1. Add Student");
                System.out.println("2. Get All Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // clear buffer

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter First Name: ");
                        String firstName = scanner.nextLine();
                        System.out.print("Enter Last Name: ");
                        String lastName = scanner.nextLine();
                        System.out.print("Enter Email: ");
                        String email = scanner.nextLine();

                        String dob;
                        while (true) {
                            System.out.print("Enter Date of Birth (YYYY-MM-DD): ");
                            dob = scanner.nextLine();
                            if (dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
                                break; // valid format
                            } else {
                                System.out.println("Invalid format. Example: 2000-05-10");
                            }
                        }

                        StudentCRUD.addStudent(conn, new StudentCRUD.Student(firstName, lastName, email, dob));
                    }
                    case 2 -> {
                        System.out.println("\nStudents in database:");
                        for (StudentCRUD.Student s : StudentCRUD.getAllStudents(conn)) {
                            System.out.println(s.getId() + " | " + s.getName() + " | " + s.getLastName()
                                    + " | " + s.getEmail() + " | " + s.getDateOfBirth());
                        }
                    }
                    case 3 -> {
                        System.out.print("Enter Student ID to update: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter New Last Name: ");
                        String newLastName = scanner.nextLine();
                        System.out.print("Enter New Email: ");
                        String newEmail = scanner.nextLine();
                        StudentCRUD.updateStudent(conn, id, newLastName, newEmail);
                    }
                    case 4 -> {
                        System.out.print("Enter Student ID to delete: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        StudentCRUD.deleteStudent(conn, id);
                    }
                    case 5 -> {
                        System.out.println("Exiting program...");
                        return;
                    }
                    default -> System.out.println("Invalid option! Please try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
