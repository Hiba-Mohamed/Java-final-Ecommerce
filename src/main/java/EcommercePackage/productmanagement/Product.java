package EcommercePackage.productmanagement;

import java.sql.Timestamp;

public class Product {
  // Attributes
    private int product_id;
    private String name;
    private double price;
    private int quantity;
    private int sellerId;
    private Timestamp createdAt;

    // Constructor
    public Product(int product_id, String name, double price, int quantity, int sellerId, Timestamp createdAt) {
        this.product_id = product_id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.sellerId = sellerId;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getId() {
        return this.product_id;
    }

    public void setId(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String product_name) {
        this.name = product_name;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSellerId() {
        return this.sellerId;
    }

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // Utility methods
    public void updateStock(int newQuantity) {
        this.quantity = newQuantity;
    }

    @Override
    public String toString() {
        return "Product [ID=" + this.product_id + ", Name=" + this.name + ", Price=" + this.price +
               ", Quantity=" + this.quantity + ", Seller ID=" + this.sellerId + "CREATED AT=" + this.createdAt + "]";
    }
}
