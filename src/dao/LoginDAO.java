/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import model.User;

public class LoginDAO {
    private Connection conn;

    public LoginDAO(Connection conn) {
        this.conn = conn;
    }

    public User validate(String username, String password) throws SQLException {
        String sql = "SELECT id, username, password FROM users WHERE username = ?";
        
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    
                    // Compare the stored hashed password with the hashed input password
                    if (checkPassword(password, storedHash)) {
                        // Create a User object and return it
                        int id = rs.getInt("id");
                        return new User(id, username, storedHash);
                    }
                }
                return null; // User not found or password mismatch
            }
        }
    }


    private boolean checkPassword(String password, String storedHash) {
        // Implement your hashing and comparison logic here
        String hashedInputPassword = hashPassword(password);
         System.out.println("Hasil hash password: " + hashedInputPassword); // Mencetak hasil hash

        return hashedInputPassword.equals(storedHash);
    }

    private String hashPassword(String password) {
        // Example using SHA-256 (consider using bcrypt or another stronger algorithm)
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception (log it, rethrow it, etc.)
            throw new RuntimeException("Hashing algorithm not found", e);
        }
    }
}
