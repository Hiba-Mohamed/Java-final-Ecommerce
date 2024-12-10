package EcommercePackage.user;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

import EcommercePackage.SQL;
import EcommercePackage.database.DatabaseConnection;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    // public int login(String username, String password) {
    //     try {
    //         return userDAO.login(username, password);
    //     } catch (SQLException e) {
    //         System.out.println("Error login: " + e.getMessage());
    //         return 0; // Return 0 for failed login
    //     }
    // }

    public String[] login(String username, String password) {
        String sql = "SELECT u.user_id, u.password, u.role_id, u.email FROM users u "
                + "WHERE u.username = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");  // Retrieve the hashed password
                String userId = resultSet.getString("user_id");  // Retrieve user_id as a String
                int roleId = resultSet.getInt("role_id");  // Retrieve role_id
                String email = resultSet.getString("email");  // Retrieve email

                // Compare the entered password with the stored hashed password using BCrypt
                if (BCrypt.checkpw(password, hashedPassword)) {
                    return new String[] { userId, String.valueOf(roleId), email };  // Return user_id, role_id, and email
                } else {
                    System.out.println("Invalid username or password.");
                    return new String[] { "null", "null", "null" };  // Indicate login failure
                }
            } else {
                System.out.println("Invalid username or password.");
                return new String[] { "null", "null", "null" };  // Username not found
            }
        } catch (SQLException e) {
            System.out.println("Error during login: " + e.getMessage());
            return new String[] { "null", "null", "null" };  // Indicate login failure due to an error
        }
    }
    public void getAllUsers() {
        try {
            userDAO.getAllUsers();
        } catch (SQLException e) {
            System.out.println("Error while retrieving all users: " + e.getMessage());
        }
    }

    public boolean addUser(User user) {
        if (user == null) {
            System.out.println("User is null");
            return false;
        }

        // Check if the username or email already exists
        if (userDAO.doesUserExist(user.getUsername(), user.getEmail())) {
            return false;  // Stop the registration process
        }

        try {
            userDAO.addUser(user); // Attempt to add the user to the database
            return true;  // Registration successful
        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
            return false;
        }
    }
    

    public boolean removeUser(String username) {
        if (username == null) {
            System.out.println("User is null");
            return false;
        }

        try {
            userDAO.removeUser(username);
            return true;
        } catch (SQLException e) {
            System.out.println("Error removing user: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUserPassword(String username, String oldPassword, String newPassword) {
        if (username == null || oldPassword == null || newPassword == null) {
            System.out.println("Error: All values required !");
            return false;
        }
        try {
            userDAO.updateUserPassword(username, oldPassword, newPassword);
            System.out.println("User password updated successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating user password: " + e.getMessage());
            return false;
        }
    }

    public boolean changeUserRole(String username, int newRoleId) {
        if (username == null || newRoleId < 1 || newRoleId > 3) {
            System.out.println("Error: username is required and role_id must be between 1 and 3 !");
            return false;
        }
        try {
            userDAO.changeUserRole(username, newRoleId);
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating user's role: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUserEmail(String username, String newEmail) {
        if (username == null || newEmail == null) {
            System.out.println("Error: username and email are required !");
            return false;
        }
        try {
            userDAO.updateUserEmail(username, newEmail);
            System.out.println("User password updated successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating user password: " + e.getMessage());
            return false;
        }
    }

    public int getRoleIdByUsername(String username) {
        if (username == null) {
            System.out.println("Error: username is required !");
            return 0;
        }
        try {
            int roleId = userDAO.getRoleIdByUsername(username);
            if (roleId != -1) {
                System.out.println("User roleId retrieved successfully: " + roleId);
            }
            return roleId;
        } catch (SQLException e) {
            System.out.println("Error updating user password: " + e.getMessage());
            return 0;
        }
    }

    // public boolean executeUserDatabaseSetUpOperations(){
    //     try {
    //         userDAO.executeUserDatabaseSetUpOperations();
    //         System.out.println("Sample data loaded successfully!");
    //         return true;
    //     } catch (SQLException e) {
    //         System.out.println("Error loading sample data : " + e.getMessage());
    //         return false;
    //     }
    // }

    public boolean executeUserDatabaseSetUpOperations() {
        try {
            SQL.executeUserDatabaseSetUpOperations();
            System.out.println("Sample data loaded successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Error loading sample data: " + e.getMessage());
            return false;
        }
    }

}