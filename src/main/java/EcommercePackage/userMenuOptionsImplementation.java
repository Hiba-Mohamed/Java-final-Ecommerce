package EcommercePackage;
import java.util.List;
import java.util.Scanner;

import EcommercePackage.productmanagement.Product;
import EcommercePackage.productmanagement.ProductServices;
import EcommercePackage.user.Buyer;
import EcommercePackage.user.User;
import EcommercePackage.user.UserService;

public class userMenuOptionsImplementation {
    private static final UserService userService = new UserService();
    private static final ProductServices productService = new ProductServices();
    private static final Scanner scanner = new Scanner(System.in);
    private static String loggedInUser = null;
    private static int loggedInSellerId; 


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

            if (choice == 1) {
                // Login logic
                System.out.println("\nEnter username:");
                String username = scanner.nextLine();
                System.out.println("\nEnter Password:");
                String password = scanner.nextLine();

                int userRole = userService.login(username, password);

                switch (userRole) {
                    case 1:
                        System.out.println("\nLogin successful. Welcome, " + username);
                        loggedInUser = username;
                        showAdminMenu();
                        break;
                    case 2:
                        System.out.println("\nLogin successful. Welcome, " + username);
                        loggedInUser = username;
                        showSellerMenu();
                        break;
                    case 3:
                        System.out.println("\nLogin successful. Welcome, " + username);
                        loggedInUser = username;
                        showBuyerMenu();
                        break;
                    default:
                        System.out.println("\nLogin failed. Please check your credentials.");
                        break;
                }
            } else if (choice == 2) {
                // Registration
                System.out.println("\nEnter username:");
                String username = scanner.nextLine();
                System.out.println("\nEnter Password:");
                String password = scanner.nextLine();
                System.out.println("\nEnter email:");
                String email = scanner.nextLine();

                // In real application, add logic to store and validate new user info
                User newUserByDefaultIsBuyer = new Buyer(username, email, password); // Default role set as Buyer
                userService.addUser(newUserByDefaultIsBuyer);
                System.out.println("\nRegistration successful. Welcome, " + username);
                loggedInUser = username;
                showBuyerMenu();
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
            System.out.println("4. Log out");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); 

            switch (choice) {
                case 1:
                    userService.getAllUsers(); 
                    break;

                case 2:
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
                    // Display each product with seller information
                    for (Product product : products) {
                        System.out.println("\nProduct ID: " + product.getProductId());
                        System.out.println("Product Name: " + product.getProductName());
                        System.out.println("Product Price: $" + product.getProductPrice());
                        System.out.println("Product Quantity: " + product.getProductQuantity());
                        System.out.println("Seller Name: " + product.getSellerName());
                        System.out.println("Seller Email: " + product.getSellerEmail());
                        System.out.println("------------------------------------");
                    }
                }
                break;

                case 4:
                    loggedInUser = null; 
                    System.out.println("Logged out successfully.");
                    return; 

                default:
                    System.out.println("Invalid option, please try again.");
            }
        }
    }

    // Seller menu options
    private static void showSellerMenu() {
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
                //     System.out.println("Enter your ID to get the list of all your products:");
                //     int sellerId = scanner.nextInt();
                //     scanner.nextLine();  

                //     List<Product> products = productService.viewProductsBySeller(sellerId);

                //     if (products.isEmpty()) {
                //         System.out.println("No products found for the given seller ID.");
                //     } else {
                //         // Display product details
                //         for (Product product : products) {
                //             System.out.println("\nProduct ID: " + product.getProductId());
                //             System.out.println("Product Name: " + product.getProductName());
                //             System.out.println("Product Price: $" + product.getProductPrice());
                //             System.out.println("Product Quantity: " + product.getProductQuantity());
                //             System.out.println("Seller Name: " + product.getSellerName());
                //             System.out.println("Seller Email: " + product.getSellerEmail());
                //             System.out.println("------------------------------------");
                //         }
                //     }
                // break;
                List<Product> products = productService.viewProductsBySeller(loggedInSellerId);

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

                    System.out.println("\nEnter new product name:");
                    String newProductName = scanner.nextLine();
                    System.out.println("\nEnter new product price:");
                    double newPrice = scanner.nextDouble();
                    System.out.println("\nEnter new product quantity:");
                    int newQuantity = scanner.nextInt();

                    Product updatedProduct = new Product(productId, newProductName, newPrice, newQuantity, loggedInSellerId, null, null);
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

                    Product newProduct = new Product(productName, productPrice, productQuantity, loggedInSellerId, null, null);
                    productService.addProduct(newProduct);
                    break;
                case 4:
                    System.out.println("\nEnter product ID to delete:");
                    int productToDeleteId = scanner.nextInt();
                    boolean isDeleted = productService.deleteProduct(productToDeleteId, loggedInSellerId);
                    if (isDeleted) {
                        System.out.println("Product successfully deleted.");
                    } else {
                        System.out.println("Failed to delete the product. Ensure the product ID is correct.");
                    }
                    break;
                case 5:
                    loggedInUser = null;
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
                    break;
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
        System.out.println("3. View product details");
        System.out.println("4. Log out");
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
                        System.out.println("\nProduct ID: " + product.getProductId() +
                                           ", Name: " + product.getProductName() +
                                           ", Price: " + product.getProductPrice() +
                                           ", Quantity: " + product.getProductQuantity() +
                                           ", Seller: " + product.getSellerName() +
                                           ", Email: " + product.getSellerEmail());
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
                        System.out.println("Product ID: " + product.getProductId() +
                                           ", Name: " + product.getProductName() +
                                           ", Price: " + product.getProductPrice() +
                                           ", Quantity: " + product.getProductQuantity() +
                                           ", Seller: " + product.getSellerName() +
                                           ", Email: " + product.getSellerEmail());
                    }
                }
                break;

            case 3:
                System.out.println("Enter product Id, to view product details:");
                int productID = scanner.nextInt();
                productService.viewProductsBySeller(productID);
                break;
            case 4:
                loggedInUser = null;  // Log out the user
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