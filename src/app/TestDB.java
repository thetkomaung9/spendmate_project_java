package app;

import app.dao.DBConnection;
import app.dao.UserDAO;
import app.model.User;
import java.sql.*;

public class TestDB {
    public static void main(String[] args) {
        System.out.println("Testing database connection and user registration...\n");
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Check if users table exists
            ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE 'SpendMate_users'");
            if (rs.next()) {
                System.out.println("✓ Users table exists: " + rs.getString(1));
            } else {
                System.out.println("✗ Users table does NOT exist!");
                return;
            }
            
            // Show table structure
            rs = stmt.executeQuery("DESCRIBE SpendMate_users");
            System.out.println("\nTable structure:");
            while (rs.next()) {
                System.out.println("  " + rs.getString(1) + " - " + rs.getString(2));
            }
            
            // Test user registration
            System.out.println("\nTesting user registration...");
            UserDAO userDAO = new UserDAO();
            
            // Create test user
            String testUsername = "testuser_" + System.currentTimeMillis();
            User testUser = new User(testUsername, "password123", "test@example.com");
            
            boolean result = userDAO.registerUser(testUser);
            System.out.println("Registration result: " + (result ? "SUCCESS" : "FAILED"));
            
            if (result) {
                System.out.println("User ID: " + testUser.getId());
                
                // Test authentication
                User authUser = userDAO.authenticate(testUsername, "password123");
                System.out.println("Authentication: " + (authUser != null ? "SUCCESS" : "FAILED"));
                
                // Cleanup - delete test user
                stmt.executeUpdate("DELETE FROM SpendMate_users WHERE username = '" + testUsername + "'");
                System.out.println("Test user cleaned up.");
            }
            
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
