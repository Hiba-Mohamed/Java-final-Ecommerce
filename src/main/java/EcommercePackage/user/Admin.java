package EcommercePackage.user;

public class Admin extends User {
    public Admin(int user_id, String username, String email, String password) {
        super(user_id, username, email, password, Role.ADMIN);
    }
}
