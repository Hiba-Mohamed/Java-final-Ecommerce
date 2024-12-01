package EcommercePackage.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException, SQLException {
        Dotenv dotenv = Dotenv.load();

        // Get the environment variables
        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");
        return DriverManager.getConnection(url, user, password);
    }


}