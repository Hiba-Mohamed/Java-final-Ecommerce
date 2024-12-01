package EcommercePackage.user;
import org.mindrot.jbcrypt.BCrypt;
public abstract class User {
    private int user_id;
    private String username;
    private String email;
    private String password;
    private Role role;

    public enum Role {
        BUYER, SELLER, ADMIN
    }

    public User(int id, String username, String email, String password, Role role){
        this.user_id = id;
        this.username = username;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.role = role;
    }
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public Role getRole(){
        return this.role;
    }

    public void setRole(Role role){
        this.role= role;
    }
}
