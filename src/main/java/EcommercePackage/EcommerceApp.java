package EcommercePackage;

import EcommercePackage.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class EcommerceApp {

    public static void main(String[] args) {
        // Call the method to create tables
        createSQLtables();
    }

    public static void createSQLtables() {
        try (Connection connection = connectToDataBase()) {
            if (connection != null) {
                System.out.println("Connected to the database successfully!");
                // Execute SQL queries directly from the string
                executeSqlQueries(connection);
            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error while handling the connection: " + e.getMessage());
        }
    }

    public static Connection connectToDataBase() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    private static void executeSqlQueries(Connection connection) {
        // Define the SQL queries as a string
        String sql =
                "CREATE TABLE IF NOT EXISTS roles (" +
                        "    id SERIAL PRIMARY KEY," +
                        "    role TEXT NOT NULL" +
                        ");" +
                        "CREATE TABLE IF NOT EXISTS users (" +
                        "    user_id SERIAL PRIMARY KEY," +
                        "    username VARCHAR(50) NOT NULL," +
                        "    email VARCHAR(100) NOT NULL UNIQUE," +
                        "    password TEXT NOT NULL," +
                        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "    role_id INT," +
                        "    FOREIGN KEY (role_id) REFERENCES roles(id)" +
                        ");" +
                        "CREATE TABLE IF NOT EXISTS products (" +
                        "    product_id SERIAL PRIMARY KEY," +
                        "    name VARCHAR(100) NOT NULL," +
                        "    price DECIMAL(10, 2) NOT NULL," +
                        "    quantity INT NOT NULL," +
                        "    seller_id INT," +
                        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "    FOREIGN KEY (seller_id) REFERENCES users(user_id)" +
                        ");";

        try (Statement statement = connection.createStatement()) {
            // Execute the SQL queries
            String[] sqlStatements = sql.split(";");
            for (String s : sqlStatements) {
                if (!s.trim().isEmpty()) {
                    statement.execute(s.trim());
                }
            }
            System.out.println("SQL queries executed successfully.");
        } catch (SQLException e) {
            System.out.println("Error while executing SQL queries: " + e.getMessage());
        }
    }
}