package EcommercePackage.user;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import EcommercePackage.database.DatabaseConnection;

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

    public int login(String username, String password)  throws SQLException{
        String selectSql = "SELECT password, role_id FROM users WHERE username = ?";

        try (Connection connection = connectToDataBase();
             PreparedStatement selectStmt = connection.prepareStatement(selectSql)) {

            // Retrieve the current hashed password and role_id
            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String currentHashedPassword = rs.getString("password");
                int roleId = rs.getInt("role_id");

                // Compare the provided password with the hashed password
                if (!BCrypt.checkpw(password, currentHashedPassword)) {
                    System.out.println("------------------------------------");
                    System.out.println("| Password is incorrect.           |");
                    System.out.println("------------------------------------");
                    return 0; // Login failed, return 0 (or other failure indicator)
                }

                // Login successful
                System.out.println("------------------------------------");
                System.out.println("| Login successful.                |");
                System.out.println("------------------------------------");
                return roleId; // Return role_id for further use (e.g., role-based access)

            } else {
                System.out.println("------------------------------------");
                System.out.println("| Username not found.              |");
                System.out.println("------------------------------------");
                return 0; // Username not found, return 0
            }
        } catch (SQLException e) {
            System.out.println("Error while logging in: " + e.getMessage());
            return 0; // Return 0 if an exception occurs
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

    public void updateUserPassword(String username, String oldPassword, String newPassword)  throws SQLException{
        String selectSql = "SELECT password FROM users WHERE username = ?";
        String updateSql = "UPDATE users SET password = ? WHERE username = ?";

        try (Connection connection = connectToDataBase();
             PreparedStatement selectStmt = connection.prepareStatement(selectSql);
             PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {

            // Retrieve the current hashed password
            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String currentHashedPassword = rs.getString("password");

                // Compare the provided oldPassword with the hashed password
                if (!BCrypt.checkpw(oldPassword, currentHashedPassword)) {
                    System.out.println("------------------------------------");
                    System.out.println("| Old password is incorrect.       |");
                    System.out.println("------------------------------------");
                    return;
                }

                // Hash the new password
                String newHashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

                // Update the password
                updateStmt.setString(1, newHashedPassword);
                updateStmt.setString(2, username);
                int rowsAffected = updateStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("------------------------------------");
                    System.out.println("| Password updated successfully!   |");
                    System.out.println("------------------------------------");
                } else {
                    System.out.println("------------------------------------");
                    System.out.println("| Password update failed.          |");
                    System.out.println("------------------------------------");
                }
            } else {
                System.out.println("------------------------------------");
                System.out.println("| Username not found.              |");
                System.out.println("------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error while updating password: " + e.getMessage());
        }
    }

    public void changeUserRole(String username, int newRoleId) throws SQLException{
        if (newRoleId < 1 || newRoleId > 3) {
            System.out.println("Invalid role ID. 1 for 'ADMIN', 2 for 'SELLER', and 3 for 'BUYER'");
            return;
        }

        String updateRoleSql = "UPDATE users SET role_id = ? WHERE username = ?";

        try (Connection connection = connectToDataBase();
             PreparedStatement statement = connection.prepareStatement(updateRoleSql)) {

            statement.setInt(1, newRoleId);
            statement.setString(2, username);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("------------------------------------");
                System.out.println("|  User role updated successfully.  |");
                System.out.println("------------------------------------");
            } else {
                System.out.println("------------------------------------------------------");
                System.out.println("|  No user found with the username: " + username);
                System.out.println("------------------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error while changing user role: " + e.getMessage());
        }
    }

    public void updateUserEmail(String username, String newEmail) throws SQLException {
        String updateRoleSql = "UPDATE users SET email = ? WHERE username = ?";

        try (Connection connection = connectToDataBase();
             PreparedStatement statement = connection.prepareStatement(updateRoleSql)) {

            statement.setString(1, newEmail);
            statement.setString(2, username);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("-------------------------------------");
                System.out.println("|  User Email updated successfully.  |");
                System.out.println("-------------------------------------");
            } else {
                System.out.println("------------------------------------------------------");
                System.out.println("|  No user found with the username: " + username);
                System.out.println("------------------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error while changing user email: " + e.getMessage());
        }
    }

    public int getRoleIdByUsername(String username) throws SQLException {
        String getRoleSql = "SELECT role_id FROM users WHERE username = ?";

        try (Connection connection = connectToDataBase();
             PreparedStatement statement = connection.prepareStatement(getRoleSql)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("role_id");
            } else {
                System.out.println("No user found with username: " + username);
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving roleId: " + e.getMessage());
            throw e;
        }
    }

}