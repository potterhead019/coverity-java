package com.demo;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * Sample Java Application for Coverity Analysis
 * Contains intentional security vulnerabilities for demonstration
 */
public class Application {

    public static void main(String[] args) {
        Application app = new Application();
        
        System.out.println("=== Coverity Java Demo ===");
        
        // Test various methods
        app.processUserInput("test_user");
        app.readFile("/tmp/test.txt");
        
        System.out.println("Application completed.");
    }

    /**
     * SQL Injection vulnerability - Coverity will detect this
     */
    public void queryDatabase(String userId) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/db");
            Statement stmt = conn.createStatement();
            
            // VULNERABILITY: SQL Injection - user input directly in query
            String query = "SELECT * FROM users WHERE id = '" + userId + "'";
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                System.out.println("User: " + rs.getString("name"));
            }
            
            // VULNERABILITY: Resource leak - connection not closed in finally block
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Command Injection vulnerability - Coverity will detect this
     */
    public void processUserInput(String userInput) {
        try {
            // VULNERABILITY: Command Injection - user input in runtime exec
            Runtime.getRuntime().exec("echo " + userInput);
            
            System.out.println("Processed: " + userInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Path Traversal vulnerability - Coverity will detect this
     */
    public String readFile(String filename) {
        StringBuilder content = new StringBuilder();
        
        try {
            // VULNERABILITY: Path Traversal - unvalidated file path
            File file = new File("/data/" + filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            
            // VULNERABILITY: Resource leak - reader not closed
        } catch (IOException e) {
            // VULNERABILITY: Information exposure - stack trace printed
            e.printStackTrace();
        }
        
        return content.toString();
    }

    /**
     * Null pointer dereference - Coverity will detect this
     */
    public void processData(String data) {
        // VULNERABILITY: Potential null pointer dereference
        if (data.equals("test")) {  // Should check data != null first
            System.out.println("Processing: " + data.toUpperCase());
        }
    }

    /**
     * Hardcoded credentials - Coverity will detect this
     */
    public Connection getConnection() {
        try {
            // VULNERABILITY: Hardcoded credentials
            String password = "admin123";
            return DriverManager.getConnection(
                "jdbc:mysql://localhost/db", 
                "admin", 
                password
            );
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Insecure random - Coverity may detect this
     */
    public String generateToken() {
        // VULNERABILITY: Using insecure Random instead of SecureRandom
        Random random = new Random();
        return String.valueOf(random.nextLong());
    }
}
