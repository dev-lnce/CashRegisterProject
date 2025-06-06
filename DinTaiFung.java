import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class DinTaiFung {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // ArrayLists to store user credentials
        ArrayList<String> usernames = new ArrayList<>();
        ArrayList<String> passwords = new ArrayList<>();

        // Add a default admin user
        usernames.add("admin");
        passwords.add("Admin123");

        boolean exitProgram = false;

        while (!exitProgram) {
            // Authentication Menu
            System.out.println("\n======================================================");
            System.out.println("            DIN TAI FUNG AUTHENTICATION               ");
            System.out.println("======================================================");
            System.out.println("1) Login");
            System.out.println("2) Signup");
            System.out.println("3) Exit");
            System.out.println("------------------------------------------------------");
            System.out.print("Enter your choice (1-3): ");

            int authChoice = getValidIntegerInput(scanner, 3);

            switch (authChoice) {
                case 1:
                    String loggedInUser = login(scanner, usernames, passwords);
                    if (loggedInUser != null) {
                        System.out.println("\nLogin successful! Welcome to Din Tai Fung Cash Register System.");
                        runCashRegisterSystem(scanner, loggedInUser);
                    }
                    break;
                case 2:
                    signup(scanner, usernames, passwords);
                    break;
                case 3:
                    exitProgram = true;
                    System.out.println("Exiting system. Thank you!");
                    break;
            }
        }

        scanner.close();
    }

    // Modified login to return username on success, null on failure
    private static String login(Scanner scanner, ArrayList<String> usernames, ArrayList<String> passwords) {
        System.out.println("\n======================================================");
        System.out.println("                    USER LOGIN                        ");
        System.out.println("======================================================");

        int attempts = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            int userIndex = usernames.indexOf(username);

            if (userIndex != -1 && passwords.get(userIndex).equals(password)) {
                return username;
            } else {
                attempts++;
                System.out.println("Invalid username or password. " + (MAX_ATTEMPTS - attempts) + " attempts remaining.");
                if (attempts == MAX_ATTEMPTS) {
                    System.out.println("Maximum login attempts reached. Please try again later.");
                    return null;
                }
            }
        }

        return null;
    }

    private static void signup(Scanner scanner, ArrayList<String> usernames, ArrayList<String> passwords) {
        System.out.println("\n======================================================");
        System.out.println("                   USER SIGNUP                        ");
        System.out.println("======================================================");

        // Username validation - alphanumeric and 5-15 characters
        String usernameRegex = "^[a-zA-Z0-9]{5,15}$";
        Pattern usernamePattern = Pattern.compile(usernameRegex);

        // Password validation - at least one uppercase, one number, 8-20 characters
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,20}$";
        Pattern passwordPattern = Pattern.compile(passwordRegex);

        String username = "";
        boolean validUsername = false;

        while (!validUsername) {
            System.out.println("Username requirements:");
            System.out.println("- Must be alphanumeric (letters and numbers only)");
            System.out.println("- Must be 5-15 characters long");
            System.out.print("\nEnter username: ");
            username = scanner.nextLine().trim();

            Matcher usernameMatcher = usernamePattern.matcher(username);

            if (usernames.contains(username)) {
                System.out.println("Username already exists. Please choose a different username.");
            } else if (!usernameMatcher.matches()) {
                System.out.println("Invalid username format. Please try again.");
            } else {
                validUsername = true;
            }
        }

        String password = "";
        boolean validPassword = false;

        while (!validPassword) {
            System.out.println("\nPassword requirements:");
            System.out.println("- Must contain at least one uppercase letter");
            System.out.println("- Must contain at least one number");
            System.out.println("- Must be 8-20 characters long");
            System.out.print("\nEnter password: ");
            password = scanner.nextLine().trim();

            Matcher passwordMatcher = passwordPattern.matcher(password);

            if (!passwordMatcher.matches()) {
                System.out.println("Invalid password format. Please try again.");
            } else {
                validPassword = true;
            }
        }

        System.out.print("Confirm password: ");
        String confirmPassword = scanner.nextLine().trim();

        if (password.equals(confirmPassword)) {
            usernames.add(username);
            passwords.add(password);
            System.out.println("\nSignup successful! You can now login with your new credentials.");
        } else {
            System.out.println("\nPasswords do not match. Signup failed.");
        }

        pressEnterToContinue(scanner);
    }

    // Added loggedInUser parameter to receive username
    private static void runCashRegisterSystem(Scanner scanner, String loggedInUser) {
        ArrayList<String> productNames = new ArrayList<>(Arrays.asList(
                "Xiao Long Bao", "Pork Chop Fried Rice", "Shrimp Fried Rice",
                "Pork Wontons", "Shrimp & Pork Wontons",
                "Pork Potstickers", "Vegetable",
                "Pork Buns", "Vegetable Buns", "Red Bean Buns",
                "Chocolate Buns", "Hot & Sour Soup", "Jasmine Tea", "Mango Smoothie"));

        ArrayList<Double> productPrices = new ArrayList<>(Arrays.asList(
                280.00, 320.00, 340.00, 250.00, 270.00,
                250.00, 240.00, 220.00, 200.00, 180.00,
                190.00, 220.00, 120.00, 180.00));

        ArrayList<String> cartProductNames = new ArrayList<>();
        ArrayList<Integer> cartProductQuantities = new ArrayList<>();

        boolean continueTransaction = true;
        while (continueTransaction) {
            boolean inCart = true;
            while (inCart) {
                displayCart(cartProductNames, cartProductQuantities, productNames, productPrices);
                System.out.println("------------------------------------------------------");
                System.out.println("\nChoose an action:");
                System.out.println("1) Add Product to Cart");
                System.out.println("2) Modify Product in Cart");
                System.out.println("3) Remove Product from Cart");
                System.out.println("4) Manage Menu (Add/Remove Products)");
                System.out.println("5) Checkout");
                System.out.println("6) Logout");
                System.out.println("------------------------------------------------------");
                System.out.print("Enter your choice (1-6): ");

                int action = getValidIntegerInput(scanner, 6);
                switch (action) {
                    case 1:
                        addProduct(scanner, productNames, productPrices, cartProductNames, cartProductQuantities);
                        break;
                    case 2:
                        modifyProduct(scanner, productNames, productPrices, cartProductNames, cartProductQuantities);
                        break;
                    case 3:
                        removeProduct(scanner, cartProductNames, cartProductQuantities);
                        break;
                    case 4:
                        manageMenu(scanner, productNames, productPrices);
                        break;
                    case 5:
                        if (cartProductNames.isEmpty()) {
                            System.out.println("Cart is empty. Please add products before checkout.");
                            pressEnterToContinue(scanner);
                        } else {
                            inCart = false;
                        }
                        break;
                    case 6:
                        System.out.println("Logging out...");
                        return; // Exit the cash register function to go back to login
                }
            }

            double totalPrice = calculateTotal(cartProductNames, cartProductQuantities, productNames, productPrices);
            double discountedTotal = applyDiscount(scanner, totalPrice);
            processPaymentAndPrintReceipt(scanner, cartProductNames, cartProductQuantities, productNames, productPrices, totalPrice, discountedTotal);

            // Log the successful transaction
            logTransaction(loggedInUser, generateOrderNumber(), cartProductNames, cartProductQuantities, productNames, productPrices, discountedTotal);

            System.out.println("\nDo you want to perform another transaction? (Y/N)");
            String response = safeReadLine(scanner);

            continueTransaction = response.equalsIgnoreCase("Y");

            cartProductNames.clear();
            cartProductQuantities.clear();
        }

        System.out.println("Thank you for using Din Tai Fung Cash Register System!");
    }

    private static void logTransaction(String loggedinUser, String orderNumber, ArrayList<String> cartProductNames,
                                       ArrayList<Integer> cartProductQuantities, ArrayList<String> productNames,
                                       ArrayList<Double> productPrices, double totalAmount) {
        try {
            FileWriter fileWriter = new FileWriter("transactions.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateTimeStr = now.format(dateFormatter);

            // Write transaction header
            printWriter.println("======================================================");
            printWriter.println("TRANSACTION LOG");
            printWriter.println("======================================================");
            printWriter.println("Order Number: " + orderNumber);
            printWriter.println("Date/Time: " + dateTimeStr);
            printWriter.println("Cashier: " + loggedinUser);
            printWriter.println("------------------------------------------------------");
            printWriter.println("Items Purchased:");
            printWriter.printf("%-25s %-5s %-10s %-10s%n", "Item", "Qty", "Price", "Amount");
            printWriter.println("------------------------------------------------------");

            // Write each item
            for (int i = 0; i < cartProductNames.size(); i++) {
                String product = cartProductNames.get(i);
                int quantity = cartProductQuantities.get(i);
                int productIndexInFixed = findProductIndex(productNames, product);
                double itemPrice = productPrices.get(productIndexInFixed);
                double itemTotal = quantity * itemPrice;
                printWriter.printf("%-25s %-5d ₱%-10.2f ₱%-10.2f%n", product, quantity, itemPrice, itemTotal);
            }

            printWriter.println("------------------------------------------------------");
            printWriter.printf("Total Amount: ₱%.2f%n", totalAmount);
            printWriter.println("======================================================");
            printWriter.println(); // Empty line for separation

            printWriter.close();
            System.out.println("Transaction logged successfully to transactions.txt");

        } catch (IOException e) {
            System.out.println("Error logging transaction: " + e.getMessage());
        }
    }

    // Existing methods, unchanged except added parameter to runCashRegisterSystem calls
    private static double applyDiscount(Scanner scanner, double totalPrice) {
        System.out.println("\n======================================================");
        System.out.println("                 DISCOUNT OPTIONS                     ");
        System.out.println("======================================================");
        System.out.println("Select discount type:");
        System.out.println("1) Senior Citizen (20% discount)");
        System.out.println("2) Person with Disability (20% discount)");
        System.out.println("3) Employee Discount (15% discount)");
        System.out.println("4) No Discount");
        System.out.println("------------------------------------------------------");
        System.out.print("Enter your choice (1-4): ");

        int discountChoice = getValidIntegerInput(scanner, 4);
        double discountRate = 0.0;
        String discountType = "None";

        switch (discountChoice) {
            case 1:
                discountRate = 0.20;
                discountType = "Senior Citizen";
                break;
            case 2:
                discountRate = 0.20;
                discountType = "PWD";
                break;
            case 3:
                discountRate = 0.15;
                discountType = "Employee";
                break;
            case 4:
                discountRate = 0.0;
                discountType = "None";
                break;
        }

        if (discountRate > 0) {
            System.out.print("Enter ID number for verification: ");
            String idNumber = safeReadLine(scanner);

            double discountAmount = totalPrice * discountRate;
            double discountedTotal = totalPrice - discountAmount;

            System.out.println("\n------------------------------------------------------");
            System.out.println("Discount type: " + discountType + " (" + (discountRate * 100) + "%)");
            System.out.println("ID number: " + idNumber);
            System.out.printf("Original total: ₱%.2f%n", totalPrice);
            System.out.printf("Discount amount: ₱%.2f%n", discountAmount);
            System.out.printf("Discounted total: ₱%.2f%n", discountedTotal);

            return discountedTotal;
        }

        return totalPrice;
    }

    private static void manageMenu(Scanner scanner, ArrayList<String> productNames, ArrayList<Double> productPrices) {
        boolean managingMenu = true;

        while (managingMenu) {
            System.out.println("\n======================================================");
            System.out.println("                    MENU MANAGEMENT                   ");
            System.out.println("======================================================");
            System.out.println("Current Menu Items:");
            System.out.printf("%-5s %-25s %-10s%n", "No.", "Product", "Price (₱)");
            System.out.println("------------------------------------------------------");

            for (int i = 0; i < productNames.size(); i++) {
                System.out.printf("%-5d %-25s ₱%-10.2f%n", (i + 1), productNames.get(i), productPrices.get(i));
            }

            System.out.println("------------------------------------------------------");
            System.out.println("\nChoose an action:");
            System.out.println("1) Add New Product");
            System.out.println("2) Edit Existing Product");
            System.out.println("3) Remove Product from Menu");
            System.out.println("4) Return to Main Menu");
            System.out.println("------------------------------------------------------");
            System.out.print("Enter your choice (1-4): ");

            int choice = getValidIntegerInput(scanner, 4);

            switch (choice) {
                case 1:
                    addNewProduct(scanner, productNames, productPrices);
                    break;
                case 2:
                    editProduct(scanner, productNames, productPrices);
                    break;
                case 3:
                    removeMenuProduct(scanner, productNames, productPrices);
                    break;
                case 4:
                    managingMenu = false;
                    break;
            }
        }
    }
    private static void addNewProduct(Scanner scanner, ArrayList<String> productNames, ArrayList<Double> productPrices) {
        System.out.println("\n======================================================");
        System.out.println("                    ADD NEW PRODUCT                   ");
        System.out.println("======================================================");
        System.out.print("Enter new product name: ");
        String newName = safeReadLine(scanner).trim();

        // Check if product already exists
        if (productNames.contains(newName)) {
            System.out.println("Product already exists in the menu.");
            pressEnterToContinue(scanner);
            return;
        }

        System.out.print("Enter product price (₱): ");
        double newPrice = getValidDoubleInput(scanner);
        productNames.add(newName);
        productPrices.add(newPrice);

        System.out.println("New product added successfully: " + newName + " (₱" + newPrice + ")");
        pressEnterToContinue(scanner);
    }
    private static void editProduct(Scanner scanner, ArrayList<String> productNames, ArrayList<Double> productPrices) {
        System.out.println("\n======================================================");
        System.out.println("                   EDIT PRODUCT                       ");
        System.out.println("======================================================");
        System.out.printf("%-5s %-25s %-10s%n", "No.", "Product", "Price (₱)");
        System.out.println("------------------------------------------------------");

        for (int i = 0; i < productNames.size(); i++) {
            System.out.printf("%-5d %-25s ₱%-10.2f%n", (i + 1), productNames.get(i), productPrices.get(i));
        }

        System.out.print("\nEnter the number of the product to edit (1-" + productNames.size() + "): ");
        int productNum = getValidIntegerInput(scanner, productNames.size());
        int productIndex = productNum - 1;

        System.out.println("\nWhat would you like to edit?");
        System.out.println("1) Product Name");
        System.out.println("2) Product Price");
        System.out.println("3) Both Name and Price");
        System.out.println("------------------------------------------------------");
        System.out.print("Enter your choice (1-3): ");
        int editChoice = getValidIntegerInput(scanner, 3);

        if (editChoice == 1 || editChoice == 3) {
            System.out.print("Enter new name for '" + productNames.get(productIndex) + "': ");
            String newName = safeReadLine(scanner).trim();
            productNames.set(productIndex, newName);
        }

        if (editChoice == 2 || editChoice == 3) {
            System.out.print("Enter new price for '" + productNames.get(productIndex) + "' (₱): ");
            double newPrice = getValidDoubleInput(scanner);
            productPrices.set(productIndex, newPrice);
        }
        System.out.println("Product updated successfully!");
        pressEnterToContinue(scanner);
    }
    private static void removeMenuProduct(Scanner scanner, ArrayList<String> productNames, ArrayList<Double> productPrices) {
        if (productNames.size() <= 1) {
            System.out.println("Cannot remove product. Menu must have at least one product.");
            pressEnterToContinue(scanner);
            return;
        }

        System.out.println("\n======================================================");
        System.out.println("                 REMOVE MENU PRODUCT                  ");
        System.out.println("======================================================");
        System.out.printf("%-5s %-25s %-10s%n", "No.", "Product", "Price (₱)");
        System.out.println("------------------------------------------------------");

        for (int i = 0; i < productNames.size(); i++) {
            System.out.printf("%-5d %-25s ₱%-10.2f%n", (i + 1), productNames.get(i), productPrices.get(i));
        }

        System.out.print("\nEnter the number of the product to remove (1-" + productNames.size() + "): ");

        int productNum = getValidIntegerInput(scanner, productNames.size());
        int productIndex = productNum - 1;
        String removedProduct = productNames.get(productIndex);

        System.out.println("Are you sure you want to remove '" + removedProduct + "' from the menu? (Y/N)");
        String confirmation = safeReadLine(scanner);

        if (confirmation.equalsIgnoreCase("Y")) {
            productNames.remove(productIndex);
            productPrices.remove(productIndex);
            System.out.println(removedProduct + " has been removed from the menu.");
        } else {
            System.out.println("Removal cancelled.");
        }

        pressEnterToContinue(scanner);
    }
    private static void pressEnterToContinue(Scanner scanner) {
        System.out.println("\nPress Enter to continue...");
        safeReadLine(scanner);
    }
    private static void displayCart(ArrayList<String> cartProductNames, ArrayList<Integer> cartProductQuantities,
                                    ArrayList<String> productNames, ArrayList<Double> productPrices) {
        System.out.println("\n======================================================");
        System.out.println("             DIN TAI FUNG CASH REGISTER               ");
        System.out.println("======================================================");
        System.out.println("Current Cart Items:");

        if (cartProductNames.isEmpty()) {
            System.out.println("Cart is empty");
        } else {
            System.out.printf("%-25s %-10s %-10s%n", "Product", "Quantity", "Price (₱)");
            System.out.println("------------------------------------------------------");

            for (int i = 0; i < cartProductNames.size(); i++) {
                String product = cartProductNames.get(i);

                int quantity = cartProductQuantities.get(i);
                int productIndexInFixed = findProductIndex(productNames, product);
                double itemPrice = productPrices.get(productIndexInFixed);
                double itemTotal = quantity * itemPrice;

                System.out.printf("%-25s %-10d ₱%-10.2f%n", product, quantity, itemTotal);
            }
        }
    }
    private static int getValidIntegerInput(Scanner scanner, int max) {
        int input = -1;

        while (input < 1 || input > max) {
            try {
                String line = scanner.nextLine();
                input = Integer.parseInt(line.trim());

                if (input < 1 || input > max) {
                    System.out.println("Invalid input. Please enter a number between " + 1 + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between " + 1 + " and " + max + ".");
            }
        }
        return input;
    }
    private static void addProduct(Scanner scanner, ArrayList<String> productNames, ArrayList<Double> productPrices,
                                   ArrayList<String> cartProductNames, ArrayList<Integer> cartProductQuantities) {
        System.out.println("\n======================================================");
        System.out.println("                    MENU                             ");
        System.out.println("======================================================");
        System.out.printf("%-5s %-25s %-10s%n", "No.", "Product", "Price (₱)");
        System.out.println("------------------------------------------------------");

        for (int i = 0; i < productNames.size(); i++) {
            System.out.printf("%-5d %-25s ₱%-10.2f%n", (i + 1), productNames.get(i), productPrices.get(i));
        }

        System.out.println("------------------------------------------------------");
        System.out.print("Enter product number (1-" + productNames.size() + "): ");
        int productNum = getValidIntegerInput(scanner, productNames.size());
        int productIndex = productNum - 1;
        String selectedProduct = productNames.get(productIndex);

        System.out.print("Enter product quantity: ");
        int quantity = getValidQuantityInput(scanner);
        int existingIndex = cartProductNames.indexOf(selectedProduct);
        System.out.println("------------------------------------------------------");

        if (existingIndex != -1) {
            int newQuantity = cartProductQuantities.get(existingIndex) + quantity;
            cartProductQuantities.set(existingIndex, newQuantity);
            System.out.println(selectedProduct + " quantity updated to " + newQuantity);
        } else {
            cartProductNames.add(selectedProduct);
            cartProductQuantities.add(quantity);
            System.out.println(selectedProduct + " added to cart with quantity " + quantity);
        }

        pressEnterToContinue(scanner);
    }
    private static int getValidQuantityInput(Scanner scanner) {
        int quantity = -1;
        while (quantity < 1) {
            try {
                String line = scanner.nextLine();
                quantity = Integer.parseInt(line.trim());
                if (quantity < 1) {
                    System.out.println("Invalid input. Please enter a positive number for quantity.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a positive number for quantity.");
            }
        }
        return quantity;
    }
    private static void modifyProduct(Scanner scanner, ArrayList<String> productNames, ArrayList<Double> productPrices,
                                      ArrayList<String> cartProductNames, ArrayList<Integer> cartProductQuantities) {
        if (cartProductNames.isEmpty()) {
            System.out.println("Cart is empty. Nothing to modify.");
            pressEnterToContinue(scanner);
            return;
        }

        System.out.println("\n======================================================");
        System.out.println("                  MODIFY PRODUCT                      ");
        System.out.println("======================================================");
        System.out.printf("%-5s %-25s %-10s%n", "No.", "Product", "Quantity");
        System.out.println("------------------------------------------------------");

        for (int i = 0; i < cartProductNames.size(); i++) {
            System.out.printf("%-5d %-25s %-10d%n", (i + 1), cartProductNames.get(i), cartProductQuantities.get(i));
        }

        System.out.print("\nEnter the number of the product to modify (1-" + cartProductNames.size() + "): ");
        int productNum = getValidIntegerInput(scanner, cartProductNames.size());
        int productIndex = productNum - 1;
        String selectedProduct = cartProductNames.get(productIndex);

        System.out.println("\nWhat would you like to modify?");
        System.out.println("1) Change quantity only");
        System.out.println("2) Replace with a different product");
        System.out.print("Enter your choice (1-2): ");
        int modifyChoice = getValidIntegerInput(scanner, 2);

        switch (modifyChoice) {
            case 1:
                System.out.print("Enter new quantity for " + selectedProduct + ": ");
                int newQuantity = getValidQuantityInput(scanner);
                cartProductQuantities.set(productIndex, newQuantity);
                System.out.println(selectedProduct + " quantity updated to " + newQuantity);
                break;
            case 2:
                replaceProduct(scanner, productNames, productPrices, cartProductNames, cartProductQuantities, productIndex);
                break;
        }

        pressEnterToContinue(scanner);
    }
    private static void replaceProduct(Scanner scanner, ArrayList<String> productNames, ArrayList<Double> productPrices,
                                       ArrayList<String> cartProductNames, ArrayList<Integer> cartProductQuantities, int productIndex) {
        System.out.println("\n======================================================");
        System.out.println("                    MENU                             ");
        System.out.println("======================================================");
        System.out.printf("%-5s %-25s %-10s%n", "No.", "Product", "Price (₱)");
        System.out.println("------------------------------------------------------");

        for (int i = 0; i < productNames.size(); i++) {
            System.out.printf("%-5d %-25s ₱%-10.2f%n", (i + 1), productNames.get(i), productPrices.get(i));
        }

        System.out.print("\nEnter product number to replace with (1-" + productNames.size() + "): ");
        int newProductNum = getValidIntegerInput(scanner, productNames.size());
        String newProduct = productNames.get(newProductNum - 1);
        int existingIndex = cartProductNames.indexOf(newProduct);

        if (existingIndex != -1 && existingIndex != productIndex) {
            System.out.println(newProduct + " is already in your cart. Would you like to");
            System.out.println("1) Combine quantities");
            System.out.println("2) Replace anyway (separate entries)");
            System.out.print("Enter your choice (1-2): ");
            int combineChoice = getValidIntegerInput(scanner, 2);

            if (combineChoice == 1) {
                int combinedQuantity = cartProductQuantities.get(productIndex) + cartProductQuantities.get(existingIndex);
                cartProductQuantities.set(existingIndex, combinedQuantity);
                cartProductNames.remove(productIndex);
                cartProductQuantities.remove(productIndex);
                System.out.println("Products combined. " + newProduct + " quantity is now " + combinedQuantity);
                return;
            }
        }

        System.out.print("Enter quantity for " + newProduct + ": ");
        int quantity = getValidQuantityInput(scanner);
        cartProductNames.set(productIndex, newProduct);
        cartProductQuantities.set(productIndex, quantity);
        System.out.println("Product replaced with " + newProduct + " (quantity: " + quantity + ")");
    }
    private static void removeProduct(Scanner scanner, ArrayList<String> cartProductNames,
                                      ArrayList<Integer> cartProductQuantities) {
        if (cartProductNames.isEmpty()) {
            System.out.println("Cart is empty. Nothing to remove.");
            pressEnterToContinue(scanner);
            return;
        }

        System.out.println("\n======================================================");
        System.out.println("                  REMOVE PRODUCT                      ");
        System.out.println("======================================================");
        System.out.printf("%-5s %-25s %-10s%n", "No.", "Product", "Quantity");
        System.out.println("------------------------------------------------------");

        for (int i = 0; i < cartProductNames.size(); i++) {
            System.out.printf("%-5d %-25s %-10d%n", (i + 1), cartProductNames.get(i), cartProductQuantities.get(i));
        }

        System.out.print("\nEnter the number of the product to remove (1-" + cartProductNames.size() + "): ");
        int productNum = getValidIntegerInput(scanner, cartProductNames.size());
        int productIndex = productNum - 1;
        String removedProduct = cartProductNames.get(productIndex);
        cartProductNames.remove(productIndex);
        cartProductQuantities.remove(productIndex);

        System.out.println(removedProduct + " has been removed from your cart.");
        pressEnterToContinue(scanner);
    }
    private static double calculateTotal(ArrayList<String> cartProductNames, ArrayList<Integer> cartProductQuantities,
                                         ArrayList<String> productNames, ArrayList<Double> productPrices) {
        double totalPrice = 0.0;
        for (int i = 0; i < cartProductNames.size(); i++) {
            String product = cartProductNames.get(i);
            int quantity = cartProductQuantities.get(i);
            int productIndexInFixed = findProductIndex(productNames, product);
            double itemPrice = productPrices.get(productIndexInFixed);
            double itemTotal = quantity * itemPrice;
            totalPrice += itemTotal;
        }
        return totalPrice;
    }

    private static void processPaymentAndPrintReceipt(Scanner scanner, ArrayList<String> cartProductNames,
                                                      ArrayList<Integer> cartProductQuantities, ArrayList<String> productNames,
                                                      ArrayList<Double> productPrices, double originalTotal, double discountedTotal) {
        System.out.println("\n======================================================");
        System.out.println("                Payment Processing                    ");
        System.out.println("======================================================");

        // Get payment method
        String paymentMethod = getValidPaymentMethod(scanner);
        String paymentDetails = "";
        double payment = 0;

        if (paymentMethod.equalsIgnoreCase("Cash")) {
            System.out.printf("Total amount due: ₱%.2f%n", discountedTotal);
            payment = processCashPayment(scanner, discountedTotal);
        } else {
            payment = discountedTotal;
            paymentDetails = processElectronicPayment(scanner, paymentMethod);
        }

        double change; // Declare the variable

        if (paymentMethod.equalsIgnoreCase("Cash")) {
            change = payment - discountedTotal; // Calculate change if payment is cash
        } else {
            change = 0; // Set change to 0 if payment is not cash
        }

        double tax = discountedTotal * 0.12; // Calculate tax based on discounted total
        String orderNumber = generateOrderNumber();
        printReceipt(orderNumber, cartProductNames, cartProductQuantities, productNames, productPrices,
                originalTotal, discountedTotal, payment, change, paymentMethod, paymentDetails, tax);
    }

    private static String getValidPaymentMethod(Scanner scanner) {
        System.out.println("Select payment method:");
        System.out.println("1) Cash");
        System.out.println("2) Credit/Debit Card");
        System.out.println("3) QR Code Payment");
        System.out.println("4) Online Banking");
        System.out.println("------------------------------------------------------");
        System.out.print("Enter your choice (1-4): ");

        int paymentChoice = getValidIntegerInput(scanner, 4);
        String paymentMethod;

        switch (paymentChoice) {
            case 1:
                paymentMethod = "Cash";
                break;
            case 2:
                paymentMethod = "Card";
                break;
            case 3:
                paymentMethod = "QR Code";
                break;
            case 4:
                paymentMethod = "Online Banking";
                break;
            default:
                paymentMethod = "Cash";
                break;
        }

        return paymentMethod;
    }

    private static double processCashPayment(Scanner scanner, double totalPrice) {
        double payment = 0;
        boolean paymentValid = false;

        while (!paymentValid) {
            System.out.print("Enter payment amount (₱): ");
            payment = getValidDoubleInput(scanner);

            if (payment < totalPrice) {
                System.out.printf("Insufficient payment. Amount due: ₱%.2f%n", totalPrice);
                System.out.printf("Your payment: ₱%.2f%n", payment);
                System.out.printf("Still needed: ₱%.2f%n", (totalPrice - payment));
            } else {
                paymentValid = true;
            }
        }
        return payment;
    }

    private static String processElectronicPayment(Scanner scanner, String paymentMethod) {
        String details = "";

        if (paymentMethod.equalsIgnoreCase("Card")) {
            // Card payment processing logic (existing code)
            System.out.println("Processing card payment...");

            boolean validCard = false;
            String cardType = "";

            while (!validCard) {
                System.out.println("Select card type:");
                System.out.println("1) Visa");
                System.out.println("2) Mastercard");
                System.out.println("3) JCB");
                System.out.print("Enter your choice (1-3): ");

                int cardChoice = getValidIntegerInput(scanner, 3);

                switch (cardChoice) {
                    case 1:
                        cardType = "Visa";
                        break;
                    case 2:
                        cardType = "Mastercard";
                        break;
                    case 3:
                        cardType = "JCB";
                        break;
                }
                validCard = true;
            }

            boolean validCardNumber = false;
            String cardNumber = "";

            while (!validCardNumber) {
                System.out.print("Enter last 4 digits of card number: ");
                cardNumber = safeReadLine(scanner).trim();

                if (cardNumber.matches("\\d{4}")) {
                    validCardNumber = true;
                } else {
                    System.out.println("Invalid input. Please enter 4 digits.");
                }
            }

            details = cardType + " ****" + cardNumber;
            System.out.println("Card payment processed successfully.");

        } else if (paymentMethod.equalsIgnoreCase("QR Code")) {
            // QR code payment processing logic
            System.out.println("Processing QR code payment...");

            boolean validApp = false;
            String appName = "";

            while (!validApp) {
                System.out.println("Select payment app:");
                System.out.println("1) GCash");
                System.out.println("2) Maya");
                System.out.println("3) ShopeePay");
                System.out.print("Enter your choice (1-3): ");

                int appChoice = getValidIntegerInput(scanner, 3);

                switch (appChoice) {
                    case 1:
                        appName = "GCash";
                        break;
                    case 2:
                        appName = "Maya";
                        break;
                    case 3:
                        appName = "ShopeePay";
                        break;
                }
                validApp = true;
            }

            boolean validRefNumber = false;
            String refNumber = "";

            while (!validRefNumber) {
                System.out.print("Enter reference number: ");
                refNumber = safeReadLine(scanner).trim();

                if (refNumber.matches("[A-Za-z0-9]{6,12}")) {
                    validRefNumber = true;
                } else {
                    System.out.println("Invalid reference number. It should be 6-12 alphanumeric characters.");
                }
            }

            details = appName + " QR Ref#" + refNumber;
            System.out.println("QR code payment verified successfully.");

        } else if (paymentMethod.equalsIgnoreCase("Online Banking")) {
            // Online banking payment processing logic
            System.out.println("Processing online banking payment...");

            boolean validBank = false;
            String bankName = "";

            while (!validBank) {
                System.out.println("Select bank:");
                System.out.println("1) BDO");
                System.out.println("2) BPI");
                System.out.println("3) Metrobank");
                System.out.println("4) Unionbank");
                System.out.print("Enter your choice (1-4): ");

                int bankChoice = getValidIntegerInput(scanner, 4);

                switch (bankChoice) {
                    case 1:
                        bankName = "BDO";
                        break;
                    case 2:
                        bankName = "BPI";
                        break;
                    case 3:
                        bankName = "Metrobank";
                        break;
                    case 4:
                        bankName = "Unionbank";
                        break;
                }
                validBank = true;
            }

            boolean validRefNumber = false;
            String refNumber = "";

            while (!validRefNumber) {
                System.out.print("Enter reference number: ");
                refNumber = safeReadLine(scanner).trim();

                if (refNumber.matches("[A-Za-z0-9]{6,12}")) {
                    validRefNumber = true;
                } else {
                    System.out.println("Invalid reference number. It should be 6-12 alphanumeric characters.");
                }
            }

            details = bankName + " Ref#" + refNumber;
            System.out.println("Online banking payment verified successfully.");
        }

        return details;
    }

    private static double getValidDoubleInput(Scanner scanner) {
        double input = -1;
        while (input < 0) {
            try {
                String line = scanner.nextLine();
                input = Double.parseDouble(line.trim());

                if (input < 0) {
                    System.out.println("Invalid input. Please enter a positive number.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return input;
    }

    private static String generateOrderNumber() {
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyMMdd");
        String dateStr = now.format(dateFormatter);
        int randomNum = 1000 + random.nextInt(9000);
        return "DTF-" + dateStr + "-" + randomNum;
    }

    private static void printReceipt(String orderNumber, ArrayList<String> cartProductNames,
                                     ArrayList<Integer> cartProductQuantities, ArrayList<String> productNames,
                                     ArrayList<Double> productPrices, double originalTotal, double discountedTotal,
                                     double payment, double change, String paymentMethod, String paymentDetails, double tax) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTimeStr = now.format(dateFormatter);

        System.out.println("\n======================================================");
        System.out.println("                     DIN TAI FUNG                     ");
        System.out.println("              Authentic Taiwanese Cuisine             ");
        System.out.println("======================================================");
        System.out.println("              SM City Lipa, MCAF Center               ");
        System.out.println("                Lipa City, Philippines                ");
        System.out.println("            Contact No: (+63) 966-140-6125            ");
        System.out.println("======================================================");
        System.out.println("Order #: " + orderNumber);
        System.out.println("Date/Time: " + dateTimeStr);
        System.out.println("------------------------------------------------------");
        System.out.printf("%-25s %-5s %-10s %-10s%n", "Item", "Qty", "Price", "Amount");
        System.out.println("------------------------------------------------------");

        for (int i = 0; i < cartProductNames.size(); i++) {
            String product = cartProductNames.get(i);
            int quantity = cartProductQuantities.get(i);
            int productIndexInFixed = findProductIndex(productNames, product);
            double itemPrice = productPrices.get(productIndexInFixed);
            double itemTotal = quantity * itemPrice;
            System.out.printf("%-25s %-5d ₱%-10.2f ₱%-10.2f%n", product, quantity, itemPrice, itemTotal);
        }

        System.out.println("------------------------------------------------------");
        System.out.printf("%-31s ₱%-10.2f%n", "Subtotal:", originalTotal);

        if (originalTotal != discountedTotal) {
            double discountAmount = originalTotal - discountedTotal;
            System.out.printf("%-31s ₱%-10.2f%n", "Discount:", discountAmount);
        }

        System.out.printf("%-31s ₱%-9.2f%n", "VAT (12%):", tax);
        System.out.printf("%-31s ₱%-10.2f%n", "Total:", discountedTotal);
        System.out.println("------------------------------------------------------");
        System.out.printf("%-31s ₱%-10.2f%n", "Payment (" + paymentMethod + "):", payment);

        if (!paymentDetails.isEmpty()) {
            System.out.println("Payment Details: " + paymentDetails);
        }

        if (change > 0) {
            System.out.printf("%-31s ₱%-10.2f%n", "Change:", change);
        }

        System.out.println("======================================================");
        System.out.println("             Thank You for Dining With Us!            ");
        System.out.println("                  Please Come Again!                  ");
        System.out.println("======================================================");
    }

    private static int findProductIndex(ArrayList<String> productNames, String productName) {
        for (int i = 0; i < productNames.size(); i++) {
            if (productNames.get(i).equals(productName)) {
                return i;
            }
        }
        return -1; // Product not found
    }

    private static String safeReadLine(Scanner scanner) {
        String input = "";
        try {
            input = scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Error reading input, please try again.");
        }
        return input;
    }
}
