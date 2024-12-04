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
    public int getProductId() {
        return this.product_id;
    }

    public void setProductId(int product_id) {
        this.product_id = product_id;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return this.productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return this.productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getProductSellerId() {
        return this.productSellerId;
    }

    public void setProductSellerId(int productSellerId) {
        this.productSellerId = productSellerId;
    }

    // Utility methods
    public void updateProductStock(int newProductQuantity) {
        this.productQuantity = newProductQuantity;
    }

    @Override
    public String toString() {
        return "Product [ID=" + this.product_id + ", Name=" + this.productName + ", Price=" + this.productPrice +
               ", Quantity=" + this.productQuantity + ", Seller ID=" + this.productSellerId + "]";
    }
}
