package EcommercePackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import EcommercePackage.database.DatabaseConnection;

public class SQL {
    public static Connection connectToDataBase() throws SQLException {
        return DatabaseConnection.getConnection();
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

    public static void executeUserDatabaseSetUpOperations() throws SQLException{
        try {
            createSQLtables();
            insertTestData();
            System.out.println("All database operations completed successfully.");
        } catch (SQLException e) {
            // Handle any SQL exceptions that occur during any of the operations
            System.out.println("Error during database operations: " + e.getMessage());
        }
    }

    private static void insertTestData() throws SQLException {
        String checkDataSql = "SELECT COUNT(*) AS user_count FROM users;";
        String insertRolesSql = "INSERT INTO roles (role) VALUES ('ADMIN'), ('SELLER'), ('BUYER');";
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
                    String insertProductsSql = generateInsertProductsSql(connection);
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

    private static String generateInsertProductsSql(Connection connection) throws SQLException {
        String[][] sampleProducts = {
                { "Apple iPhone 14", "999.99", "100", "mark_smith" },
                { "Samsung Galaxy S21", "799.99", "150", "lucas_white" },
                { "Sony WH-1000XM4", "349.99", "200", "olivia_green" },
                { "Apple MacBook Air", "1299.99", "50", "sophia_grey" },
                { "Dell XPS 13", "1099.99", "75", "mark_smith" },
                { "Bose SoundLink Speaker", "249.99", "120", "mark_smith" },
                { "HP Spectre x360", "1399.99", "40", "sophia_grey" },
                { "Google Pixel 7", "599.99", "100", "mark_smith" },
                { "Canon EOS R10", "1099.00", "30", "lucas_white" },
                { "Microsoft Surface Pro 9", "999.00", "60", "olivia_green" }
        };

        StringBuilder sqlBuilder = new StringBuilder(
                "INSERT INTO products (productName, productPrice, productQuantity, productSellerId, sellerName, sellerEmail) VALUES ");

        for (int i = 0; i < sampleProducts.length; i++) {
            String name = sampleProducts[i][0];
            String price = sampleProducts[i][1];
            String quantity = sampleProducts[i][2];
            String sellerName = sampleProducts[i][3];

            int sellerId = getSellerIdByName(connection, sellerName);
            if (sellerId == -1) {
            System.out.println("Seller not found: " + sellerName);
            continue;
        }

            
            String sellerEmail = getSellerEmailById(connection, sellerId);
        
            sqlBuilder.append(String.format("('%s', %s, %s, %s, '%s', '%s')",
                name, price, quantity, sellerId, sellerName, sellerEmail));

            // Add a comma unless it's the last product
            if (i < sampleProducts.length - 1) {
                sqlBuilder.append(", ");
            } else {
                sqlBuilder.append(";");
            }
        }

        return sqlBuilder.toString();
    }

    private static int getSellerIdByName(Connection connection, String sellerName) throws SQLException {
        String query = "SELECT user_id FROM users WHERE username = ? AND role_id = 2"; // role_id = 2 for SELLER
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, sellerName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id"); 
            }
        }
        return -1; 
    }

    private static String getSellerEmailById(Connection connection, int sellerId) throws SQLException {
        String query = "SELECT email FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, sellerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
        }
        return null; 
    }

    private static void executeSqlQueries(Connection connection) throws SQLException {
        // Define the SQL queries as a string
        String sql = "CREATE TABLE IF NOT EXISTS roles (" +
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
                "    sellerName VARCHAR(50)," +
                "    sellerEmail VARCHAR(100)," +
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
