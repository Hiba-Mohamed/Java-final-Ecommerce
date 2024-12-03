package EcommercePackage.user;
import java.sql.SQLException;

public class UserService {
    private final UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
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

        try {
            userDAO.addUser(user);
            System.out.println("User added!");
            return true;
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
            System.out.println("User removed successfully!");
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

    public boolean changeUserRole(String username, int newRoleId){
        if (username == null || newRoleId <1 || newRoleId >3){
            System.out.println("Error: username is required and role_id must be between 1 and 3 !");
            return false;
        }
        try {
            userDAO.changeUserRole(username, newRoleId);
            System.out.println("User password updated successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating user password: " + e.getMessage());
            return false;
        }
    }


    public boolean updateUserEmail(String username, String newEmail){
        if (username == null || newEmail == null){
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

}