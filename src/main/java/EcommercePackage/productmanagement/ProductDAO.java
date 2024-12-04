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
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                int sellerId = rs.getInt("seller_id");

                // Add product to the list
                Product product = new Product(product_id, name, price, quantity, sellerId);
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
        String sql = "SELECT * FROM products WHERE seller_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, sellerId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {

                    int productId = resultSet.getInt("product_id");
                    String name = resultSet.getString("name");
                    double price = resultSet.getDouble("price");
                    int quantity = resultSet.getInt("quantity");
                    int sellerIdFromDB = resultSet.getInt("seller_id");

                    // Create a Product object and add it to the list
                    Product product = new Product(productId, name, price, quantity, sellerIdFromDB);
                    products.add(product);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching products by seller: " + e.getMessage());
        }

        return products;
    }

    // Add a new product
    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, price, quantity, seller_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setInt(4, product.getSellerId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product added successfully.");
            } else {
                System.out.println("No product added.");
            }
        } catch (SQLException error) {
            System.out.println("Error adding product: " + error.getMessage());
            throw error;
        }
    }

    // Update a product
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, price = ?, quantity = ? WHERE product_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setInt(4, product.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product updated successfully.");
                return true;
            } else {
                System.out.println("No product found with the given ID.");
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
                System.out.println("Product deleted successfully.");
                return true;
            } else {
                System.out.println("No product found with the given ID.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
            throw e; // Rethrow the exception to be handled by the caller
        }
    }
}
