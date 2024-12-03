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
                int productId = rs.getInt("product_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                int sellerId = rs.getInt("seller_id");

                // Add product to the list
                Product product = new Product(productId, name, price, quantity, sellerId);
                products.add(product);
            }

        } catch (SQLException e) {
        System.out.println("Error while fetching products: " + e.getMessage());
        }

        return products;

    }

    // Add a new product
    public void addProduct(Product product) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO products (name, price, quantity, seller_id) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setInt(4, product.getSellerId());
            preparedStatement.executeUpdate();
        }
    }

    // Get products by seller ID
    public List<Product> viewProductsBySeller(int sellerId) throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM products WHERE seller_id = ?")) {
            preparedStatement.setInt(1, sellerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(new Product(
                            resultSet.getInt("product_id"),
                            resultSet.getString("name"),
                            resultSet.getDouble("price"),
                            resultSet.getInt("quantity"),
                            resultSet.getInt("seller_id")
                    ));
                }
            }
        }
        return products;
    }

    // Update a product
    public boolean updateProduct(Product product) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE products SET name = ?, price = ?, quantity = ? WHERE product_id = ?")) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setInt(3, product.getQuantity());
            preparedStatement.setInt(4, product.getId());
            return preparedStatement.executeUpdate() > 0;
        }
    }

    // Delete a product
    public boolean deleteProduct(int productId) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM products WHERE product_id = ?")) {
            preparedStatement.setInt(1, productId);
            return preparedStatement.executeUpdate() > 0;
        }
    }
}
