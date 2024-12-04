package EcommercePackage.user;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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


    public static void createSQLtables() throws SQLException {
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

    public void executeUserDatabaseSetUpOperations() throws SQLException{
        try {
            createSQLtables();
            insertTestData();
            System.out.println("All database operations completed successfully.");
        } catch (SQLException e) {
            // Handle any SQL exceptions that occur during any of the operations
            System.out.println("Error during database operations: " + e.getMessage());
        }
    }

    private static void insertTestData() throws SQLException{
        String checkDataSql = "SELECT COUNT(*) AS user_count FROM users;";
        String insertRolesSql =
                "INSERT INTO roles (role) VALUES ('ADMIN'), ('SELLER'), ('BUYER');";
        String checkProductDataSql = "SELECT COUNT(*) AS product_count FROM products;";

        try (Connection connection = connectToDataBase();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(checkDataSql)) {

            if (resultSet.next() && resultSet.getInt("user_count") > 0) {
                System.out.println("Sample data already exists. Skipping insertion.");
            } else {
                // Insert roles first
                statement.execute(insertRolesSql);

                // Insert users with hashed passwords and role IDs
                String insertUsersSql = generateInsertUsersSql();
                statement.execute(insertUsersSql);

                System.out.println("Sample data inserted successfully.");
            }

            // Insert products data
                try (ResultSet productResultSet = statement.executeQuery(checkProductDataSql)) {
                if (productResultSet.next() && productResultSet.getInt("product_count") > 0) {
                    System.out.println("Sample product data already exists. Skipping insertion.");
                } else {
                    String insertProductsSql = generateInsertProductsSql();
                    statement.execute(insertProductsSql);
                    System.out.println("Sample product data inserted successfully.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while inserting sample data: " + e.getMessage());
        }
    }

    private static String generateInsertUsersSql() throws SQLException {
        String[][] sampleUsers = {
                { "john_doe", "john@example.com", "secret123", "1" }, // 1 = ADMIN
                { "jane_doe", "jane@example.com", "password456", "3" }, // 3 = BUYER
                { "mark_smith", "mark@example.com", "12345", "2" }, // 2 = SELLER
                { "emma_brown", "emma@example.com", "emmapassword", "3" },
                { "lucas_white", "lucas@example.com", "lucaspassword", "2" },
                { "mia_black", "mia@example.com", "miapassword", "1" },
                { "olivia_green", "olivia@example.com", "oliviapassword", "2" },
                { "liam_blue", "liam@example.com", "liampassword", "3" },
                { "sophia_grey", "sophia@example.com", "sophiapassword", "2" },
                { "noah_purple", "noah@example.com", "noahpassword", "1" }
        };

        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO users (username, email, password, role_id) VALUES ");
        for (int i = 0; i < sampleUsers.length; i++) {
            String username = sampleUsers[i][0];
            String email = sampleUsers[i][1];
            String passwordHash = org.mindrot.jbcrypt.BCrypt.hashpw(sampleUsers[i][2],
                    org.mindrot.jbcrypt.BCrypt.gensalt());
            String roleId = sampleUsers[i][3];

            sqlBuilder.append(String.format("('%s', '%s', '%s', %s)", username, email, passwordHash, roleId));

            // Add a comma unless it's the last entry
            if (i < sampleUsers.length - 1) {
                sqlBuilder.append(", ");
            } else {
                sqlBuilder.append(";");
            }
        }

        return sqlBuilder.toString();
    }
    
    private static String generateInsertProductsSql() {
        String[][] sampleProducts = {
            {"Apple iPhone 14", "999.99", "100", "2"},  // seller_id = 2 (SELLER)
            {"Samsung Galaxy S21", "799.99", "150", "2"},
            {"Sony WH-1000XM4", "349.99", "200", "2"},
            {"Apple MacBook Air", "1299.99", "50", "2"},
            {"Dell XPS 13", "1099.99", "75", "2"},
            {"Bose SoundLink Speaker", "249.99", "120", "2"},
            {"HP Spectre x360", "1399.99", "40", "2"},
            {"Google Pixel 7", "599.99", "100", "2"},
            {"Canon EOS R10", "1099.00", "30", "2"},
            {"Microsoft Surface Pro 9", "999.00", "60", "2"}
        };

        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO products (productName, productPrice, productQuantity, productSellerId) VALUES ");

        for (int i = 0; i < sampleProducts.length; i++) {
            String name = sampleProducts[i][0];
            String price = sampleProducts[i][1];
            String quantity = sampleProducts[i][2];
            String sellerId = sampleProducts[i][3];

            sqlBuilder.append(String.format("('%s', %s, %s, %s)", name, price, quantity, sellerId));

            // Add a comma unless it's the last product
            if (i < sampleProducts.length - 1) {
                sqlBuilder.append(", ");
            } else {
                sqlBuilder.append(";");
            }
        }

        return sqlBuilder.toString();
    }


    private static void executeSqlQueries(Connection connection) throws SQLException{
        // Define the SQL queries as a string
        String sql =
                "CREATE TABLE IF NOT EXISTS roles (" +
                        "    id SERIAL PRIMARY KEY," +
                        "    role TEXT NOT NULL UNIQUE" +
                        ");" +
                        "CREATE TABLE IF NOT EXISTS users (" +
                        "    user_id SERIAL PRIMARY KEY," +
                        "    username VARCHAR(50) NOT NULL UNIQUE," +
                        "    email VARCHAR(100) NOT NULL UNIQUE," +
                        "    password TEXT NOT NULL," +
                        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "    role_id INT," +
                        "    FOREIGN KEY (role_id) REFERENCES roles(id)" +
                        ");" +
                        "CREATE TABLE IF NOT EXISTS products (" +
                        "    product_id SERIAL PRIMARY KEY," +
                        "    productName VARCHAR(100) NOT NULL," +
                        "    productPrice DECIMAL(10, 2) NOT NULL," +
                        "    productQuantity INT NOT NULL," +
                        "    productSellerId INT," +
                        "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
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