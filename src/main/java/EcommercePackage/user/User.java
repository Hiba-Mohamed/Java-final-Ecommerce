package EcommercePackage.user;
import org.mindrot.jbcrypt.BCrypt;
public abstract class User {
    private int user_id;
    private String username;
    private String email;
    private String password;
    private String role;
    private int role_id;


    public User(int id, String username, String email, String password, String role, int role_id){
        this.user_id = id;
        this.username = username;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.role = role;
        this.role_id = role_id;
    }
    public User(String username, String email, String password, String role, int role_id) {
        this.username = username;
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.role = role;
        this.role_id = role_id;

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

    public String getRoleDescription(){
        return "Role ID: " + this.role_id + ", Role Name: " + this.role;
    }

    public int getRoleId(){
        return this.role_id;
    }

    public void setRole(int role_id, String role){
        this.role= role;
        this.role_id = role_id;
    }
}