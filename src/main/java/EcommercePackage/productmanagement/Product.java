package EcommercePackage.productmanagement;

public class Product {
    // Attributes
    private int product_id;
    private String productName;
    private double productPrice;
    private int productQuantity;
    private int productSellerId;
    private String sellerName;
    private String sellerEmail;

    // Constructors
    public Product(int product_id, String productName, double productPrice, int productQuantity, int productSellerId, String sellerName, String sellerEmail) {
        this.product_id = product_id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSellerId = productSellerId;
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
    }

    public Product(String productName, double productPrice, int productQuantity, int productSellerId, String sellerName, String sellerEmail) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSellerId = productSellerId;
        this.sellerName = sellerName;
        this.sellerEmail = sellerEmail;
    }

    // Getters and Setters
    public int getProductId() {
        return product_id;
    }

    public void setProductId(int product_id) {
        this.product_id = product_id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getProductSellerId() {
        return productSellerId;
    }

    public void setProductSellerId(int productSellerId) {
        this.productSellerId = productSellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    // Utility methods
    public void updateProductStock(int newProductQuantity) {
        this.productQuantity = newProductQuantity;
    }

    @Override
    public String toString() {
        return "Product [ID=" + product_id + ", Name=" + productName + ", Price=" + productPrice +
               ", Quantity=" + productQuantity + ", Seller ID=" + productSellerId +
               ", Seller Name=" + sellerName + ", Seller Email=" + sellerEmail + "]";
    }
}