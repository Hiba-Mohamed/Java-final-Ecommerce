package EcommercePackage.user;

public class Seller extends User{
    public Seller(int user_id, String username, String email, String password) {
        super(user_id, username, email, password, Role.SELLER);
    }
}
