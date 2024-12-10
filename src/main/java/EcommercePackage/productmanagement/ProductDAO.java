package EcommercePackage.productmanagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import EcommercePackage.database.DatabaseConnection;
import EcommercePackage.user.Seller;

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

            while (rs.next()) {
                // Fetch product details
                int product_id = rs.getInt("product_id");
                String productName = rs.getString("productName");
                double productPrice = rs.getDouble("productPrice");
                int productQuantity = rs.getInt("productQuantity");
                int productSellerId = rs.getInt("productSellerId");

                // Fetch seller details
                Seller seller = null;
                String sellerQuery = "SELECT user_id, username, email FROM users WHERE user_id = ? AND role_id = 2";
                try (PreparedStatement sellerStmt = connection.prepareStatement(sellerQuery)) {
                    sellerStmt.setInt(1, productSellerId);
                    try (ResultSet sellerRs = sellerStmt.executeQuery()) {
                        if (sellerRs.next()) {
                            // Create Seller object
                            int sellerId = sellerRs.getInt("user_id");
                            String sellerName = sellerRs.getString("username");
                            String sellerEmail = sellerRs.getString("email");

                            seller = new Seller(sellerId, sellerName, sellerEmail); // Create the Seller object
                        }
                    }
                }

                // Create Product object with Seller
                if (seller != null) {
                    Product product = new Product(product_id, productName, productPrice, productQuantity, seller);
                    products.add(product);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error while fetching products: " + e.getMessage());
            throw e;
        }

        return products;
}


    // Get all products by seller id
    public List<Product> viewProductsBySeller(int sellerId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE productSellerId = ?"; // Query to get products
        String sqlUser = "SELECT username, email FROM users WHERE user_id = ?"; // Query to get seller info

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

                    // Fetch the seller details using the sellerId
                    Seller seller = null;
                    try (PreparedStatement sellerStmt = connection.prepareStatement(sqlUser)) {
                        sellerStmt.setInt(1, productSellerId);
                        try (ResultSet sellerRs = sellerStmt.executeQuery()) {
                            if (sellerRs.next()) {
                                String sellerName = sellerRs.getString("username");
                                String sellerEmail = sellerRs.getString("email");

                                // Create the Seller object
                                seller = new Seller(productSellerId, sellerName, sellerEmail);
                            }
                        }
                    }

                    // Create the Product object and add it to the list
                    if (seller != null) {
                        Product product = new Product(productId, productName, productPrice, productQuantity, seller);
                        product.setSeller(seller);
                        products.add(product);
                    }
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
                    // Fetch product details
                    int productId = resultSet.getInt("product_id");
                    String productName1 = resultSet.getString("productName");
                    double productPrice = resultSet.getDouble("productPrice");
                    int productQuantity = resultSet.getInt("productQuantity");
                    int sellerId = resultSet.getInt("productsellerid");

                    // Fetch seller details
                    Seller seller = null;
                    String sellerQuery = "SELECT user_id, username, email FROM users WHERE user_id = ? AND role_id = 2";
                    try (PreparedStatement sellerStmt = connection.prepareStatement(sellerQuery)) {
                        sellerStmt.setInt(1, sellerId);
                        try (ResultSet sellerRs = sellerStmt.executeQuery()) {
                            if (sellerRs.next()) {
                                String sellerName = sellerRs.getString("username");
                                String sellerEmail = sellerRs.getString("email");
                                seller = new Seller(sellerId, sellerName, sellerEmail);
                            }
                        }
                    }

                    // Create Product object with Seller
                    if (seller != null) {
                        Product product = new Product(productId, productName1, productPrice, productQuantity, seller);
                        products.add(product);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching products by product name: " + e.getMessage());
            throw e;
        }

        return products;
    }


    // Add a new product
    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (productName, productPrice, productQuantity, productSellerId) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setDouble(2, product.getProductPrice());
            preparedStatement.setInt(3, product.getProductQuantity());
            preparedStatement.setInt(4, product.getProductSellerId());

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
        String validateSql = "SELECT productSellerId FROM products WHERE product_id = ?";
        String updateSql = "UPDATE products SET productName = ?, productPrice = ?, productQuantity = ? WHERE product_id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Step 1: Validate ownership of the product
            try (PreparedStatement validateStmt = connection.prepareStatement(validateSql)) {
                validateStmt.setInt(1, product.getProductId());
            }
            // Step 2: Proceed with the update if validation passes
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                updateStmt.setString(1, product.getProductName());
                updateStmt.setDouble(2, product.getProductPrice());
                updateStmt.setInt(3, product.getProductQuantity());
                updateStmt.setInt(4, product.getProductId());

                int rowsAffected = updateStmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("\nProduct updated successfully.");
                    return true;
                } else {
                    System.out.println("\nFailed to update product.");
                    return false;
                }
            }
        } catch (SQLException error) {
            System.out.println("Error updating product: " + error.getMessage());
            throw error;
        }
    }

    // Deleet a product
    public boolean deleteProduct(int productId, int sellerId) throws SQLException {
        String checkSellerSql = "SELECT productSellerId FROM products WHERE product_id = ?";
        String deleteSql = "DELETE FROM products WHERE product_id = ? AND productSellerId = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Step 1: Validate ownership of the product
            try (PreparedStatement checkStatement = connection.prepareStatement(checkSellerSql)) {
                checkStatement.setInt(1, productId);

                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int actualProductSellerId = resultSet.getInt("productSellerId");

                        if (actualProductSellerId != sellerId) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }

            // Step 2: Proceed to delete the product
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setInt(1, productId);
                deleteStatement.setInt(2, sellerId);

                int rowsAffected = deleteStatement.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
            throw e;
        }
    }
}

