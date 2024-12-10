package EcommercePackage;
import java.util.List;
import java.util.Scanner;

import EcommercePackage.productmanagement.Product;
import EcommercePackage.productmanagement.ProductServices;
import EcommercePackage.user.Buyer;
import EcommercePackage.user.Seller;
import EcommercePackage.user.User;
import EcommercePackage.user.UserService;

public class userMenuOptionsImplementation {
    private static final UserService userService = new UserService();
    private static final ProductServices productService = new ProductServices();
    private static final Scanner scanner = new Scanner(System.in);
    private static String loggedInUsername = null;
    private static int loggedInId;
    private static int loggedInRoleId;
    private static String loggedInEmail;


    public static void main(String[] args) {
        userService.executeUserDatabaseSetUpOperations();
        displayLoginRegisterMenu(userService);
    }
    
    public static void displayLoginRegisterMenu(UserService userService) {
        while (true) {
            System.out.println("\nUser Dashboard:");
            System.out.println("1. Login");
            System.out.println("2. Register as a new user");
            System.out.println("3. Quit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear the newline character
            String loggedInUser = null;
            loggedInId = -1; // Add this to track seller ID

            if (choice == 1) {
                // Login logic
                System.out.println("\nEnter username:");
                String username = scanner.nextLine();
                System.out.println("\nEnter Password:");
                String password = scanner.nextLine();

                String[] loginResult = userService.login(username, password);
                if (loginResult != null && !loginResult[0].equals("null")) {
                    // Safely parse the userId and userRole as integers
                    int userId = Integer.parseInt(loginResult[0]); // Parsing userId to an integer
                    int userRole = Integer.parseInt(loginResult[1]); // Parsing userRole to an integer
                    String userEmail = loginResult[2]; // Email stays as a String
                    loggedInId = userId;
                    loggedInRoleId = userRole;
                    loggedInEmail = userEmail;
                    loggedInUsername = username;
                    // Further logic based on login success
                    System.out.println("User ID: " + userId);
                    System.out.println("Role ID: " + userRole);
                    System.out.println("Email: " + userEmail);
                } else {
                    // Handle login failure
                    System.out.println("");
                }

                if (loggedInId != -1) {

                    switch (loggedInRoleId) {
                        case 1:
                            System.out.println("\nLogin successful. Welcome, " + username);
                            showAdminMenu();
                            break;
                        case 2:
                            System.out.println("\nLogin successful. Welcome, " + username);
                            showSellerMenu(loggedInId); // Pass seller ID
                            break;
                        case 3:
                            System.out.println("\nLogin successful. Welcome, " + username);
                            showBuyerMenu();
                            break;
                        default:
                            System.out.println("\nLogin failed. Invalid role.");
                            break;
                    }
                } else {
                    System.out.println("\nLogin failed. Please check your credentials.");
                }
            } else if (choice == 2) {
                // Registration
                System.out.println("\nEnter username:");
                String username = scanner.nextLine();
                System.out.println("\nEnter email:");
                String email = scanner.nextLine();
                System.out.println("\nEnter Password:");
                String password = scanner.nextLine();

                // Create a new user object
                User newUserByDefaultIsBuyer = new Buyer(username, email, password); // Default role set as Buyer

                // Call the UserService's addUser method
                if (userService.addUser(newUserByDefaultIsBuyer)) {
                    loggedInUser = username;
                    showBuyerMenu();  // Only proceed to buyer's dashboard if user was successfully added
                } else {
                    System.out.println("The username or email is already in use. Please try with a different one.");
                }
            } else if (choice == 3) {
                // Quit logic
                System.out.println("Thank you for using the system. Goodbye!");
                System.exit(0);
            } else {
                System.out.println("Invalid option, please try again.");
            }
        }
    }


    private static void showAdminMenu() {
        while (true) {
            System.out.println("\nAdmin Dashboard:");
            System.out.println("1. View all users");
            System.out.println("2. Delete a user");
            System.out.println("3. View all products with Seller info");
            System.out.println("4. Change a user's role");
            System.out.println("5. Log out");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    userService.getAllUsers(); 
                    break;

                case 2:
                    System.out.print("\nAttention !!!! deleting a user, deletes their associated products also");
                    System.out.print("\nEnter username to delete: ");
                    String usernameToDelete = scanner.nextLine();
                    userService.removeUser(usernameToDelete);
                    break;

                case 3:
                System.out.print("\nEnter Seller ID: ");
                int sellerId = scanner.nextInt();
                scanner.nextLine();
                List<Product> products = productService.viewProductsBySeller(sellerId);
                if (products.isEmpty()) {
                    System.out.println("No products found for the given Seller ID.");
                } else {
                    for (Product product : products) {
                        System.out.println("\nProduct ID: " + product.getProductId());
                        System.out.println("Product Name: " + product.getProductName());
                        System.out.println("Product Price: $" + product.getProductPrice());
                        System.out.println("Product Quantity: " + product.getProductQuantity());
                        Seller seller = product.getSeller();
                        if (seller != null) {
                            System.out.println("Seller Name: " + seller.getUsername());
                            System.out.println("Seller Email: " + seller.getEmail());
                        }
                        System.out.println("------------------------------------");
                    }
                }
                break;
                case 4:
                    System.out.print("\nEnter username you want to change the role for: ");
                    String usernameTochangeRole = scanner.nextLine();
                    System.out.print("\nEnter the new role id (1 for ADMIN, 2 for SELLER, and 3 for BUYER)");
                    int newRoleId = scanner.nextInt();
                    scanner.nextLine();
                    userService.changeUserRole(usernameTochangeRole, newRoleId);
                    break;
                case 5:
                    loggedInUsername = null;
                    System.out.println("Logged out successfully.");
                    return; 

                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    // Seller menu options
    private static void showSellerMenu(int loggedInId) {
        while (true) {
            System.out.println("\nSeller Dashboard:");
            System.out.println("1. View my products");
            System.out.println("2. Update an existing product");
            System.out.println("3. Add a new product");
            System.out.println("4. Delete a product");
            System.out.println("5. Log out");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                List<Product> products = productService.viewProductsBySeller(loggedInId);

                    if (products.isEmpty()) {
                        System.out.println("No products found for your account.");
                    } else {
                        // Display product details
                        System.out.println("\nYour Products:");
                        for (Product product : products) {
                            System.out.println("\nProduct ID: " + product.getProductId());
                            System.out.println("Product Name: " + product.getProductName());
                            System.out.println("Product Price: $" + product.getProductPrice());
                            System.out.println("Product Quantity: " + product.getProductQuantity());
                            System.out.println("------------------------------------");
                        }
                    }
                    break;
                case 2:
                    System.out.println("\nEnter product Id you want to update:");
                    int productId = scanner.nextInt();
                    scanner.nextLine();

                    List<Product> sellerProducts = productService.viewProductsBySeller(loggedInId);
                    boolean isValidProduct = sellerProducts.stream()
                            .anyMatch(product -> product.getProductId() == productId);
                    
                    if (!isValidProduct) {
                        System.out.println("\nYou are not authorized to update this product.");
                        break;
                    }

                    System.out.println("\nEnter new product name:");
                    String newProductName = scanner.nextLine();
                    System.out.println("\nEnter new product price:");
                    double newPrice = scanner.nextDouble();
                    System.out.println("\nEnter new product quantity:");
                    int newQuantity = scanner.nextInt();
                    Product updatedProduct = new Product(productId, newProductName, newPrice, newQuantity,loggedInId);
                    productService.updateProduct(updatedProduct);
                    break;
                case 3:
                    System.out.println("\nEnter product name:");
                    String productName = scanner.nextLine();
                    System.out.println("\nEnter product price:");
                    double productPrice = scanner.nextDouble();
                    System.out.println("\nEnter product quantity:");
                    int productQuantity = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    Product newProduct = new Product(productName, productPrice, productQuantity, loggedInId);
                    productService.addProduct(newProduct);
                    break;
                case 4:
                    System.out.println("\nEnter product ID to delete:");
                    int productToDeleteId = scanner.nextInt();

                    boolean isDeleted = productService.deleteProduct(productToDeleteId, loggedInId);

                    if (isDeleted) {
                        System.out.println("\nProduct successfully deleted.");
                    } else {
                        System.out.println(
                                "\nFailed to delete the product. You can only delete products you own.");
                    }
                    break;
                case 5:
                    loggedInUsername = null;
                    System.out.println("Logged out successfully.");
                    return; 

                default:
                    System.out.println("Invalid option, please try again.");
            }
        }

    }

    // Buyer menu options
    private static void showBuyerMenu() {
    Scanner scanner = new Scanner(System.in);
    boolean continueSession = true; // Variable to control the session loop

    while (continueSession) {
        System.out.println("\nBuyer Dashboard:");
        System.out.println("1. Browse products");
        System.out.println("2. Search for a product by its name");
        System.out.println("3. Log out");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character after nextInt()

        switch (choice) {
            case 1:
                // Show all products
                List<Product> products = productService.getAllProducts();
                if (products.isEmpty()) {
                    System.out.println("No products available.");
                } else {
                    // Display all products
                    System.out.println("\nProduct List:");
                    for (Product product : products) {
                        Seller seller = product.getSeller(); // Get the seller from the Product object
                        System.out.println("\nProduct ID: " + product.getProductId() +
                                        ", Name: " + product.getProductName() +
                                        ", Price: " + product.getProductPrice() +
                                        ", Quantity: " + product.getProductQuantity() +
                                        ", Seller: " + (seller != null ? seller.getUsername() : "Unknown") +
                                        ", Email: " + (seller != null ? seller.getEmail() : "Unknown"));
                    }
                }
                break;

            case 2:
                System.out.println("\nEnter product name you want to look up:");
                String productName = scanner.nextLine();

                List<Product> searchResults = productService.viewProductsByName(productName);

                if (searchResults.isEmpty()) {
                    System.out.println("\nNo products found for the name: " + productName);
                } else {
                    // Display the matching products
                    System.out.println("\nSearch Results for '" + productName + "':");
                    for (Product product : searchResults) {
                        Seller seller = product.getSeller(); // Get the Seller object from the Product
                        System.out.println("Product ID: " + product.getProductId() +
                                        ", Name: " + product.getProductName() +
                                        ", Price: " + product.getProductPrice() +
                                        ", Quantity: " + product.getProductQuantity() +
                                        ", Seller: " + (seller != null ? seller.getUsername() : "Unknown") +
                                        ", Email: " + (seller != null ? seller.getEmail() : "Unknown"));
                    }
                }
                break;

            case 3:
                loggedInUsername = null;  // Log out the user
                System.out.println("Logged out successfully.");
                continueSession = false; // End the session loop
                break;
            default:
                System.out.println("Invalid option, please try again.");
                break;
        }

        // If not logged out, the loop will continue, showing the buyer menu again
        if (continueSession) {
            System.out.println("\nReturning to Buyer Dashboard...");
        }
    }
}

}