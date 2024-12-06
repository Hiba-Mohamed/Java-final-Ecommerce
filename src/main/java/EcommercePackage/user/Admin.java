package EcommercePackage.user;

public class Admin extends User {
    public Admin(int user_id, String username, String email, String password) {
        super(user_id, username, email, password, "ADMIN", 1);
    }
    public Admin( String username, String email, String password) {
        super( username, email, password, "ADMIN", 1);
    }
}