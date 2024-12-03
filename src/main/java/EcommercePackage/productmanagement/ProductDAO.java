package EcommercePackage.productmanagement;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
     private List<Product> products = new ArrayList<>();

    // Adding a new product
    public void addProduct(Product product) {
        products.add(product);
        System.out.println("Product added: " + product);
    }

    // Retrieving all products by a seller
    public List<Product> getProductsBySeller(int sellerId) {
        List<Product> sellerProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getSellerId() == sellerId) {
                sellerProducts.add(product);
            }
        }
        return sellerProducts;
    }

    // Updating a product's details
    public boolean updateProduct(int productId, Product updatedProduct) {
        for (Product product : products) {
            if (product.getId() == productId) {
                product.setName(updatedProduct.getName());
                product.setPrice(updatedProduct.getPrice());
                product.setQuantity(updatedProduct.getQuantity());
                System.out.println("Product updated: " + product);
                return true;
            }
        }
        return false;
    }

    // Deleting a product by ID
    public boolean deleteProduct(int productId) {
        return products.removeIf(product -> product.getId() == productId);
    }
}
