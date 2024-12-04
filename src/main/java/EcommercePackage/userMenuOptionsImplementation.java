package EcommercePackage;
import java.util.Scanner;
import EcommercePackage.user.Buyer;
import EcommercePackage.user.User;
import EcommercePackage.user.UserService;

public class userMenuOptionsImplementation {
    public static void main(String[] args) {
        UserService userService = new UserService();
        userService.executeUserDatabaseSetUpOperations();
        displayLoginRegisterMenu(userService);
    }

    public static void displayLoginRegisterMenu(UserService userService) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("User Dashboard:");
        System.out.println("1. Login");
        System.out.println("2. Register as a new user");
        int choice = scanner.nextInt();
        scanner.nextLine();
        String loggedInUser = null;

        if (choice == 1) {
            // Login logic
            System.out.println("Enter username:");
            String username = scanner.nextLine();
            System.out.println("Enter Password:");
            String password = scanner.nextLine();

            int userRole = userService.login(username, password);  // login method assumes a return value representing role

            switch (userRole){
                case 1:
                    System.out.println("Login successful. Welcome, " + username);
                    loggedInUser = username;
                    showAdminMenu();
                    break;
                case 2:
                    System.out.println("Login successful. Welcome, " + username);
                    loggedInUser = username;
                    showSellerMenu();
                    break;
                case 3:
                    System.out.println("Login successful. Welcome, " + username);
                    loggedInUser = username;
                    showBuyerMenu();
                    break;
                default:
                    System.out.println("Login failed. Please check your credentials.");
                    break;
            }
        } else if (choice == 2) {
            // Registration
            System.out.println("Enter username:");
            String username = scanner.nextLine();
            System.out.println("Enter Password:");
            String password = scanner.nextLine();
            System.out.println("Enter email:");
            String email = scanner.nextLine();

            // In real application, add logic to store and validate new user info
            User newUserByDefaultIsBuyer = new Buyer(username, email, password);  // Default role set as Buyer
            userService.addUser(newUserByDefaultIsBuyer);
            System.out.println("Registration successful. Welcome, " + username);
            loggedInUser = username;
            showBuyerMenu();
        } else {
            System.out.println("Invalid option, please try again.");
        }
    }

    // Admin menu options
    private static void showAdminMenu() {
        System.out.println("Admin Dashboard:");
        System.out.println("1. View all users");
        System.out.println("2. Delete a user");
        System.out.println("3. View all products with Seller info");
        System.out.println("4. Log out");
    }

    // Seller menu options
    private static void showSellerMenu() {
        System.out.println("Seller Dashboard:");
        System.out.println("1. Add a new product");
        System.out.println("2. Update an existing product");
        System.out.println("3. Delete a product");
        System.out.println("4. View my products");
        System.out.println("5. Log out");
    }

    // Buyer menu options
    private static void showBuyerMenu() {
        System.out.println("Buyer Dashboard:");
        System.out.println("1. Browse products");
        System.out.println("2. Search for a specific product");
        System.out.println("3. View product details");
        System.out.println("4. Log out");
    }
}