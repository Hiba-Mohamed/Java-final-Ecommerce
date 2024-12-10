package EcommercePackage.productmanagement;
import EcommercePackage.user.Seller;

public class Product {
    // Attributes
    private int product_id;
    private String productName;
    private double productPrice;
    private int productQuantity;
    private int productSellerID;
    private Seller seller;


    // Constructors
    public Product(int product_id, String productName, double productPrice, int productQuantity, int productSellerId) {
        this.product_id = product_id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSellerID = productSellerId;
    }

    public Product(int productId, String productName, double productPrice, int productQuantity, Seller seller) {
        this.product_id = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.seller = seller; 
    }


    public Product(String productName, double productPrice, int productQuantity, int productSellerId) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productSellerID = productSellerId;
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
        return productSellerID;
    }
    public void setProductSellerId(int productSellerID) {
        this.productSellerID = productSellerID;
    }

    public Seller getSeller() {
        return seller; // Get the associated Seller object
    }

    public void setSeller(Seller seller) {
        this.seller = seller; // Set the associated Seller object
    }

    // Utility methods
    public void updateProductStock(int newProductQuantity) {
        this.productQuantity = newProductQuantity;
    }

    @Override
    public String toString() {
        return "Product [ID=" + product_id + ", Name=" + productName + ", Price=" + productPrice +
                ", Quantity=" + productQuantity + ", Seller ID=" + getProductSellerId();
    }
}
