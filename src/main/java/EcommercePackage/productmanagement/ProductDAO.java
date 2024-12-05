package EcommercePackage.productmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import EcommercePackage.database.DatabaseConnection;

public class ProductDAO {

    public static Connection connectToDataBase() throws SQLException {
        return DatabaseConnection.getConnection();
    }
    
    // Get all products
    public List<Product> getAllProducts() throws SQLException {
        String sql = "SELECT * FROM products"; // SQL query to fetch all products
        List<Product> products = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

            // Process the results
            while (rs.next()) {
                int product_id = rs.getInt("product_id");
                String productName = rs.getString("productName");
                double productPrice = rs.getDouble("productPrice");
                int productQuantity = rs.getInt("productQuantity");
                int productSellerId = rs.getInt("productSellerId");
                String sellerName = rs.getString("sellerName"); 
                String sellerEmail = rs.getString("sellerEmail"); 

                // Add product to the list
                Product product = new Product(product_id, productName, productPrice, productQuantity, productSellerId,
                        sellerName, sellerEmail);
                products.add(product);
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching products: " + e.getMessage());
        }

        return products;
    }


    // Get all products by seller id
    public List<Product> viewProductsBySeller(int sellerId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE productSellerId = ?";

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, sellerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {

                    int productId = resultSet.getInt("product_id");
                    String productName = resultSet.getString("productName");
                    double productPrice = resultSet.getDouble("productPrice");
                    int productQuantity = resultSet.getInt("productQuantity");
                    int productSellerId = resultSet.getInt("productSellerId");
                    String sellerName = resultSet.getString("sellerName"); 
                    String sellerEmail = resultSet.getString("sellerEmail"); 

                    // Create a Product object and add it to the list
                    Product product = new Product(productId, productName, productPrice, productQuantity, productSellerId,
                            sellerName, sellerEmail);
                    products.add(product);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching products by seller: " + e.getMessage());
        }

        return products;
    }


    // Get all products by product name
    public List<Product> viewProductsByName(String productName) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE LOWER(productName) = LOWER(?)";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, productName.toLowerCase());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {

                    int productId = resultSet.getInt("product_id");
                    String productName1 = resultSet.getString("productName");
                    double productPrice = resultSet.getDouble("productPrice");
                    int productQuantity = resultSet.getInt("productQuantity");
                    int productSellerId = resultSet.getInt("productSellerId");
                    String sellerName = resultSet.getString("sellerName"); // Fetch sellerName
                    String sellerEmail = resultSet.getString("sellerEmail"); // Fetch sellerEmail

                    // Create a Product object and add it to the list
                    Product product = new Product(productId, productName1, productPrice, productQuantity,
                            productSellerId, sellerName, sellerEmail);
                    products.add(product);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching products by product name: " + e.getMessage());
        }

        return products;
    }

    
    // Add a new product
    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (productName, productPrice, productQuantity, productSellerId, sellerName, sellerEmail) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setDouble(2, product.getProductPrice());
            preparedStatement.setInt(3, product.getProductQuantity());
            preparedStatement.setInt(4, product.getProductSellerId());
            preparedStatement.setString(5, product.getSellerName());
            preparedStatement.setString(6, product.getSellerEmail()); 

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("\nProduct added successfully.");
            } else {
                System.out.println("\nProduct insertion failed.");
            }
        } catch (SQLException error) {
            System.out.println("Error adding product: " + error.getMessage());
            throw error;
        }
    }


    // Update a product
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET productName = ?, productPrice = ?, productQuantity = ?, productSellerID = ?, sellerName = ?, sellerEmail = ?, where product_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setDouble(2, product.getProductPrice());
            preparedStatement.setInt(3, product.getProductQuantity());
            preparedStatement.setInt(4, product.getProductSellerId());
            preparedStatement.setString(5, product.getSellerName()); 
            preparedStatement.setString(6, product.getSellerEmail()); 
            preparedStatement.setInt(7, product.getProductId()); 

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("----------------------------");
                System.out.println("'" + product + "' is updated successfully.");
                System.out.println("----------------------------");
                return true;
            } else {
                System.out.println("----------------------------");
                System.out.println("No product found with the given ID.");
                System.out.println("----------------------------");
                return false;
            }
        } catch (SQLException error) {
            System.out.println("Error updating product: " + error.getMessage());
            throw error;
        }
    }

    // Delete a product
    public boolean deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // Set the product ID for the delete query
            preparedStatement.setInt(1, productId);

            // Execute the delete query
            int rowsAffected = preparedStatement.executeUpdate();

            // Provide feedback based on whether any rows were deleted
            if (rowsAffected > 0) {
                System.out.println("----------------------------");
                System.out.println("Product with Product ID: '" + productId + "' is deleted successfully.");
                return true;
            } else {
                System.out.println("No product found with the given ID.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
            throw e;
        }
    }

}
