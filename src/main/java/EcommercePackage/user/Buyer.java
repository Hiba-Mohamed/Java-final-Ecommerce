package EcommercePackage.user;

public class Buyer extends  User{
    public Buyer(int user_id, String username, String email, String password) {
        super(user_id, username, email, password, Role.BUYER);
    }
}
