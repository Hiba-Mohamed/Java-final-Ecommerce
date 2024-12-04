package EcommercePackage.productmanagement;

public class Product {
  // Attributes
    private int product_id;
    private String productName;
    private double productPrice;
    private int productQuantity;
    private int productSellerId;

    // Constructor
    public Product(int product_id, String productName, double productPrice, int productQuantity, int productSellerId) {
        this.product_id = product_id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSellerId = productSellerId;
    }

    public Product(String productName, double productPrice, int productQuantity, int productSellerId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSellerId = productSellerId;
    }

    // Getters and Setters
    public int getId() {
        return this.product_id;
    }

    public void setId(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return this.productName;
    }

    public void setName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return this.productPrice;
    }

    public void setPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return this.productQuantity;
    }

    public void setQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getSellerId() {
        return this.productSellerId;
    }

    public void setSellerId(int productSellerId) {
        this.productSellerId = productSellerId;
    }

    // Utility methods
    public void updateStock(int newQuantity) {
        this.productQuantity = newQuantity;
    }

    @Override
    public String toString() {
        return "Product [ID=" + this.product_id + ", Name=" + this.productName + ", Price=" + this.productPrice +
               ", Quantity=" + this.productQuantity + ", Seller ID=" + this.productSellerId + "]";
    }
}
