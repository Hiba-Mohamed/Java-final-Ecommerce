package EcommercePackage.productmanagement;

public class Product {
  // Attributes
    private int product_id;
    private String product_name;
    private double price;
    private int quantity;
    private int sellerId;

    // Constructor
    public Product(int product_id, String product_name, double price, int quantity, int sellerId) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.price = price;
        this.quantity = quantity;
        this.sellerId = sellerId;
    }

    // Getters and Setters
    public int getId() {
        return this.product_id;
    }

    public void setId(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return this.product_name;
    }

    public void setName(String product_name) {
        this.product_name = product_name;
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
        return "Product [ID=" + this.product_id + ", Name=" + this.product_name + ", Price=" + this.price +
               ", Quantity=" + this.quantity + ", Seller ID=" + this.sellerId + "]";
    }
}
