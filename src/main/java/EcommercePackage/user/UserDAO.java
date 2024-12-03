package EcommercePackage.user;
import EcommercePackage.database.DatabaseConnection;

import java.sql.*;

public class UserDAO{
//1 "ADMIN"
//2	"SELLER"
//3	"BUYER"
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

    public void addUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, email, password, role_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = connectToDataBase();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setInt(4, user.getRoleId());

            // Execute the query to insert the user
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("----------------------------");
                System.out.println("| User added successfully! |");
                System.out.println("----------------------------");

            } else {
                System.out.println("--------------------------");
                System.out.println("| User insertion failed. |");
                System.out.println("--------------------------");

            }
        } catch (SQLException e) {
            System.out.println("Error while adding user: " + e.getMessage());
        }
    }

    public void removeUser(String username) throws SQLException {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection connection = connectToDataBase();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Set the username parameter
            pstmt.setString(1, username);

            // Execute the query to delete the user
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("---------------------------------------------------");
                System.out.println("|  User '" + username + "' removed successfully.  |");
                System.out.println("---------------------------------------------------");
            } else {
                System.out.println("--------------------------------------------------------");
                System.out.println("|  No user found with the username '" + username + "'.  |");
                System.out.println("--------------------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error while removing user: " + e.getMessage());
        }
    }

}