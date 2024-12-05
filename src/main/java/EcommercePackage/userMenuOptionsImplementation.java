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
            System.out.println("1. Add a new product");
            System.out.println("2. Update an existing product");
            System.out.println("3. Delete a product");
            System.out.println("4. View my products");
            System.out.println("5. Log out");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    System.out.println("\nEnter product name:");
                    String productName = scanner.nextLine();
                    System.out.println("\nEnter product price:");
                    double productPrice = scanner.nextDouble();
                    System.out.println("\nEnter product Quantity:");
                    int productQuantity = scanner.nextInt();
                     scanner.nextLine();
                    System.out.println("\nEnter seller name:");
                    String sellerName = scanner.nextLine();
                    System.out.println("\nEnter seller email:");
                    String sellerEmail = scanner.nextLine();
                    Product productToAdd = new Product(productName, productPrice, productQuantity, productQuantity,
                            sellerName, sellerEmail);
                    productService.addProduct(productToAdd);
                    break;
                case 2:
                    System.out.println("Enter product Id you want to update:");
                    int product_id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter product name:");
                    String newProductName = scanner.nextLine();
                    System.out.println("Enter product price:");
                    double newPrice = scanner.nextDouble();
                    System.out.println("Enter product Quantity:");
                    int newQuantity = scanner.nextInt();
                    System.out.println("Enter product Seller Id:");
                    int newSellerId = scanner.nextInt();
                    System.out.println("Enter seller name:");
                    String sellerNewName = scanner.nextLine();
                    System.out.println("Enter seller email:");
                    String newEmail = scanner.nextLine();
                    Product newProductObjectToReplaceOldProduct = new Product(product_id, newProductName, newPrice,
                            newQuantity, newSellerId, sellerNewName, newEmail);
                    productService.updateProduct(newProductObjectToReplaceOldProduct);
                    showSellerMenu();
                    break;
                case 3:
                    System.out.println("Enter product Id you want deleted:");
                    int productToBeDeletedId = scanner.nextInt();
                    productService.deleteProduct(productToBeDeletedId);
                    break;
                case 4:
                    System.out.println("Enter product name you want to search for:");
                    productService.getAllProducts();
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

        System.out.println("Buyer Dashboard:");
        System.out.println("1. Browse products");
        System.out.println("2. Search for a specific product");
        System.out.println("3. View product details");
        System.out.println("4. Log out");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                productService.getAllProducts();
                break;
            case 2:
                System.out.println("Enter product name you want to look up:");
                String productName = scanner.nextLine();
                productService.viewProductsByName(productName);
                break;
            case 3:
                System.out.println("Enter product Id, to view product details:");
                int productID = scanner.nextInt();
                productService.viewProductsBySeller(productID);
                break;
            case 4:
                loggedInUser = null;
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid option, please try again.");
                break;

        }

    }
}