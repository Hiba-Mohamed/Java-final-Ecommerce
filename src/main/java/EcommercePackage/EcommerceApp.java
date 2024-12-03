package EcommercePackage;
import EcommercePackage.database.DatabaseConnection;
import EcommercePackage.user.Buyer;
import EcommercePackage.user.Admin;
import EcommercePackage.user.Seller;
import EcommercePackage.user.User;
import EcommercePackage.user.UserDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;


public class EcommerceApp {
    private static final UserDAO userDAO = new UserDAO();

    public static void main(String[] args) {
        EcommerceApp app = new EcommerceApp();
        app.executeDatabaseOperations();
    };



    public void executeDatabaseOperations() {
        try {
            displayAllUsers();
            createSQLtables();
            insertTestData();
            addPredefinedUsers();
            removeUser();
            updateUserPassword();
            System.out.println("All database operations completed successfully.");
        } catch (SQLException e) {
            // Handle any SQL exceptions that occur during any of the operations
            System.out.println("Error during database operations: " + e.getMessage());
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

    public static Connection connectToDataBase() throws SQLException {
        return DatabaseConnection.getConnection();
    }


    private static void insertTestData() throws SQLException{
        String checkDataSql = "SELECT COUNT(*) AS user_count FROM users;";
        String insertRolesSql =
                "INSERT INTO roles (role) VALUES ('ADMIN'), ('SELLER'), ('BUYER');";

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
        } catch (SQLException e) {
            System.out.println("Error while inserting sample data: " + e.getMessage());
        }
    }

    private static String generateInsertUsersSql() throws SQLException{
        String[][] sampleUsers = {
                {"john_doe", "john@example.com", "secret123", "1"}, // 1 = ADMIN
                {"jane_doe", "jane@example.com", "password456", "3"}, // 3 = BUYER
                {"mark_smith", "mark@example.com", "markspassword", "2"}, // 2 = SELLER
                {"emma_brown", "emma@example.com", "emmapassword", "3"},
                {"lucas_white", "lucas@example.com", "lucaspassword", "2"},
                {"mia_black", "mia@example.com", "miapassword", "1"},
                {"olivia_green", "olivia@example.com", "oliviapassword", "2"},
                {"liam_blue", "liam@example.com", "liampassword", "3"},
                {"sophia_grey", "sophia@example.com", "sophiapassword", "2"},
                {"noah_purple", "noah@example.com", "noahpassword", "1"}
        };

        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO users (username, email, password, role_id) VALUES ");
        for (int i = 0; i < sampleUsers.length; i++) {
            String username = sampleUsers[i][0];
            String email = sampleUsers[i][1];
            String passwordHash = org.mindrot.jbcrypt.BCrypt.hashpw(sampleUsers[i][2], org.mindrot.jbcrypt.BCrypt.gensalt());
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

    private static void addPredefinedUsers() throws SQLException {
        // Create user instances
        User adminUser = new Admin("admin1", "admin@example.com", "adminpass");
        User sellerUser = new Seller("seller1", "seller@example.com", "sellerpass");
        User buyerUser = new Buyer("buyer1", "buyer@example.com", "buyerpass");

        // Add users to the database
        try {
            userDAO.addUser(adminUser);
            userDAO.addUser(sellerUser);
            userDAO.addUser(buyerUser);
        } catch (SQLException e) {
            System.out.println("Error while adding predefined users: " + e.getMessage());
        }
    }

    private static void removeUser() throws SQLException{
        try {
            userDAO.removeUser("buyer1");
        } catch (SQLException e) {
            System.out.println("Error while adding predefined users: " + e.getMessage());
        }
    }

    private static void updateUserPassword(){
        userDAO.updateUserPassword("admin1", "adminpass", "NewAdminPass" );
    }

    private static void displayAllUsers() throws SQLException {
        try{
            userDAO.getAllUsers();
        }
        catch (SQLException e){
            System.out.println("Error while getting all users: " + e.getMessage());

        }
    }
}