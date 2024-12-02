package EcommercePackage.productmanagement;

public class Product {
  // Attributes
    private int id;
    private String name;
    private double price;
    private int quantity;
    private int sellerId;

    // Constructor
    public Product(int id, String name, double price, int quantity, int sellerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.sellerId = sellerId;
    }

    // Getters and Setters
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    // Utility methods
    public void updateStock(int newQuantity) {
        this.quantity = newQuantity;
    }

    @Override
    public String toString() {
        return "Product [ID=" + this.id + ", Name=" + this.name + ", Price=" + this.price +
               ", Quantity=" + this.quantity + ", Seller ID=" + this.sellerId + "]";
    }
}
