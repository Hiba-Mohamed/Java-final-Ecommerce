package EcommercePackage.user;
import EcommercePackage.database.DatabaseConnection;

import java.sql.*;

public class UserDAO{
    public static Connection connectToDataBase() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public void getAllUsers() throws SQLException{
        String sql = "SELECT * FROM users"; // SQL query to fetch all users

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // Process the results
            System.out.println("Connected to the database successfully!");
            System.out.println("Fetching users...");
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String password = rs.getString("password");

                // Print user details
                System.out.println("------------------------------------");
                System.out.println("User ID: " + userId);
                System.out.println("Username: " + username);
                System.out.println("Email: " + email);
                System.out.println("Password: " + password);
                System.out.println("------------------------------------");
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching users: " + e.getMessage());
        }

    }

}