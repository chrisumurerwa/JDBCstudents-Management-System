package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentCRUD {

    // Student model
    public static class Student {
        int id;
        String name;
        String lastName;
        String email;
        String dateOfBirth;

        // Constructor without id (for new students)
        public Student(String name, String lastName, String email, String dateOfBirth) {
            this.name = name;
            this.lastName = lastName;
            this.email = email;
            this.dateOfBirth = dateOfBirth;
        }

        // Constructor with id (for updates/retrieval)
        public Student(int id, String name, String lastName, String email, String dateOfBirth) {
            this.id = id;
            this.name = name;
            this.lastName = lastName;
            this.email = email;
            this.dateOfBirth = dateOfBirth;
        }

        // Getters
        public int getId() { return id; }
        public String getName() { return name; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getDateOfBirth() { return dateOfBirth; }
    }

    // CREATE - Add Student (auto-generated ID)
    public static void addStudent(Connection conn, Student student) throws SQLException {
        String sql = "INSERT INTO students (first_name, last_name, email, date_of_birth) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getEmail());
            try {
                pstmt.setDate(4, Date.valueOf(student.getDateOfBirth())); // must be yyyy-MM-dd
            } catch (IllegalArgumentException e) {
                System.out.println("⚠ Invalid date format. Please use YYYY-MM-DD.");
                return;
            }
            pstmt.executeUpdate();
            System.out.println("Student added successfully!");
        }
    }

    // READ - Get all Students
    public static List<Student> getAllStudents(Connection conn) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getDate("date_of_birth").toString()
                ));
            }
        }
        return students;
    }

    // UPDATE - Update last name and email
    public static void updateStudent(Connection conn, int id, String newLastName, String newEmail) throws SQLException {
        String sql = "UPDATE students SET last_name=?, email=? WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newLastName);
            pstmt.setString(2, newEmail);
            pstmt.setInt(3, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println(" Student updated successfully!");
            } else {
                System.out.println(" No student found with ID " + id);
            }
        }
    }

    // DELETE - Delete Student
    public static void deleteStudent(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("🗑 Student deleted successfully!");
            } else {
                System.out.println(" No student found with ID " + id);
            }
        }
    }
}
