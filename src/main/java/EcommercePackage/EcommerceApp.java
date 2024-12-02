package EcommercePackage;
import java.sql.Connection;
import java.sql.SQLException;
import EcommercePackage.database.DatabaseConnection;

public class EcommerceApp {

    public static void main(String[] args) {
        // Call the connectToDataBase method and use try-with-resources
        try (Connection connection = connectToDataBase()) {
            if (connection != null) {
                System.out.println("Connected to the database successfully!");
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            // Handle any exceptions that might be thrown during the connection attempt
            System.out.println("Error while handling the connection: " + e.getMessage());
        }
    }

    // Define the connectToDataBase method
    public static Connection connectToDataBase() throws SQLException {
        // Return the connection obtained from DatabaseConnection
        return DatabaseConnection.getConnection();
    }
}