package EcommercePackage.user;
//1 "ADMIN"
//2	"SELLER"
//3	"BUYER"
public class Seller extends User{
    public Seller(int user_id, String username, String email, String password) {
        super(user_id, username, email, password, "SELLER", 2);
    }
    public Seller(String username, String email, String password) {
        super( username, email, password, "SELLER", 2);
    }
}