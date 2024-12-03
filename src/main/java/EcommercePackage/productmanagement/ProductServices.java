package EcommercePackage.productmanagement;

import java.sql.SQLException;
import java.util.List;

public class ProductServices {

    private ProductDAO productDAO;

    // Constructor
    public ProductServices() {
        this.productDAO = new ProductDAO();
    }

    // Get all products
    public List<Product> getAllProducts() {
        try {
            return productDAO.getAllProducts();
        } catch (SQLException error) {
            System.out.println("Error in ProductServices: " + error.getMessage());
            return null;
        }
    }

    // Get products by seller ID
    public List<Product> viewProductsBySeller(int sellerId) {
        try {
            return productDAO.viewProductsBySeller(sellerId);
        } catch (SQLException error) {
            System.out.println("Error in ProductServices: " + error.getMessage());
            return null;
        }
    }

    // Add a new product
    public boolean addProduct(Product product) {
        try {
            if (product.getPrice() <= 0) {
                System.out.println("Price must be greater than zero.");
                return false;
            }
            if (product.getQuantity() <= 0) {
                System.out.println("Quantity must be greater than zero.");
                return false;
            }

            productDAO.addProduct(product);
            return true;
        } catch (SQLException error) {
            System.out.println("Error in ProductServices: " + error.getMessage());
            return false;
        }
    }

    // Update an existing product
    public boolean updateProduct(Product product) {
        try {
            return productDAO.updateProduct(product);
        } catch (SQLException error) {
            System.out.println("Error in ProductServices: " + error.getMessage());
            return false;
        }
    }

    // Delete a product by product ID
    public boolean deleteProduct(int productId) {
        try {
            return productDAO.deleteProduct(productId);
        } catch (SQLException error) {
            System.out.println("Error in ProductServices: " + error.getMessage());
            return false;
        }
    }
}
