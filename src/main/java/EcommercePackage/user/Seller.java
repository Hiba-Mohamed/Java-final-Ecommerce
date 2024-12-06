package EcommercePackage.user;

import EcommercePackage.productmanagement.Product;
import java.util.List;
import java.util.ArrayList;

public class Seller extends User {
    private List<Product> products;
    public Seller(int user_id, String username, String email, String password) {
        super(user_id, username, email, password, "SELLER", 2);
        this.products = new ArrayList<>();
    }

    public Seller(String username, String email, String password) {
        super(username, email, password, "SELLER", 2);
        this.products = new ArrayList<>();
    }

    // Constructor with user ID, username, and email (without password)
    public Seller(int user_id, String username, String email) {
        super(user_id, username, email, "SELLER", 2);
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

}