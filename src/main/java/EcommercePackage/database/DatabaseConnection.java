package EcommercePackage.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {

    public static Connection getConnection() throws SQLException{
        Dotenv dotenv = Dotenv.configure()
                .directory("src/main/resources")
                .load();
//        System.out.println(dotenv);
        // Get the environment variables
        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");
        return DriverManager.getConnection(url, user, password);
    }

}