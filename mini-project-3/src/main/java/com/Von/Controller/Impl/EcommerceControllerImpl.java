package com.Von.Controller.Impl;

import com.Von.Controller.EcommerceController;
import com.Von.Model.Cart.Cart;
import com.Von.Model.Catalog.Catalog;
import com.Von.Model.Catalog.Product;
import com.Von.Model.Enums.customEnums;

import com.Von.Service.Impl.CatalogServiceImpl;
import com.Von.Service.Impl.CartServiceImpl;
import com.Von.Service.Utils.NumericInputValidator;
import com.Von.Service.Utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import static com.Von.Model.Enums.enumFactory.fromChoice;

public class EcommerceControllerImpl implements EcommerceController {
    private static final Scanner scn = new Scanner(System.in);
    private static final Logger logger = LoggerFactory.getLogger(EcommerceControllerImpl.class);
    private static final String fileName = "src/main/resources/data.products.json";
    private final CatalogServiceImpl catalogService = new CatalogServiceImpl();
    private final CartServiceImpl cartService = new CartServiceImpl();
    private final NumericInputValidator<BigDecimal> priceValidator = new NumericInputValidator<>(new BigDecimal("9999999.0"));

    Catalog catalog;
    Cart userCart;
    customEnums.Role appState = customEnums.Role.Admin;

    public EcommerceControllerImpl() {
        File data = new File(fileName);
        if (data.exists() && !data.isDirectory()) {
            catalog = Catalog.getInstance(Utils.importData());
            logger.info(String.format("Successfully loaded %s",fileName));
        }
        else {
            catalog = Catalog.getInstance();
            logger.warn(String.format("Failed to find %s",fileName));
        }

    }

    /**
     * Displays the administrative options menu and handles user choices
     * related to managing the product catalog.
     */
    private void adminOptions() {
        try {
            Integer eCommerceOption = -1;

            Boolean viewingOptions = true;
            System.out.println("""
                +======== E-buy Management ========+
                | [1] View All products            |
                | [2] View All product details     |
                | [3] Register a product           |
                | [4] Update a product             |
                | [5] Remove a product             |
                | [6] Search for a product         |
                | [7] Test user                    |
                | [exit] Quit application          |
                +============= ------ =============+
                """);

            do {
                System.out.println("Choice: ");
                String userInput = scn.nextLine();
                if (userInput.equalsIgnoreCase("exit"))
                    viewingOptions = false;
                else {
                    if (userInput.matches("[1-7]")) {
                        eCommerceOption = Integer.parseInt(userInput);
                        viewingOptions = false;
                    }
                }
            } while(viewingOptions);

            switch (eCommerceOption) {
                case 1 -> viewProducts();
                case 2 -> readCatalog();
                case 3 -> createProduct();
                case 4 -> updateProduct();
                case 5 -> deleteProduct();
                case 6 -> searchProduct();
                case 7 -> {
                    if (catalog.getCatalog().isEmpty()) {
                        System.out.println("Catalog is empty, can't test user & cart functionality!");
                        adminOptions();
                    }
                    else {
                        appState = customEnums.Role.User;
                        userCart = new Cart();
                        userOptions(); // Once called, can't go back to adminOptions until the application restarts.
                    }
                }
                default -> {
                    Utils.storeData(catalog.getCatalog());
                    System.out.println("Closing app...");
                    logger.info("Ecommerce.adminOption saving data!");
                }
            }
        } catch (Exception e) {
            logger.error("Ecommerce.adminOptions error: ",e);
        }

    }

    /**
     * Displays the user options menu and handles user choices
     * related to managing their shopping cart and viewing products.
     */
    private void userOptions() {
        try {
            Integer eCommerceOption = -1;

            Boolean viewingOptions = true;
            System.out.println("""
                +============== E-buy ==============+
                | [1] View All products             |
                | [2] View All product details      |
                | [3] Add item to cart              |
                | [4] Update item quantity          |
                | [5] View items in cart            |
                | [6] Remove item in cart           |
                | [7] Empty cart                    |
                | [8] View cart total               |
                | [exit] Quit application           |
                +============== ----- ==============+
                """);

            do {
                System.out.println("Choice: ");
                String userInput = scn.nextLine();
                if (userInput.equalsIgnoreCase("exit"))
                    viewingOptions = false;
                else {
                    if (userInput.matches("[1-8]")) {
                        eCommerceOption = Integer.parseInt(userInput);
                        viewingOptions = false;
                    }
                }
            } while(viewingOptions);

            switch (eCommerceOption) {
                case 1 -> viewProducts();
                case 2 -> readCatalog();
                case 3 -> addToCart();
                case 4 -> updateItemAmount();
                case 5 -> viewCart();
                case 6 -> removeFromCart();
                case 7 -> clearCart();
                case 8 -> showCartTotal();
                default -> {
                    Utils.storeData(catalog.getCatalog());
                    System.out.println("Closing app...");
                    logger.info("Ecommerce.userOptions saving data!");
                }
            }
        } catch (Exception e) {
            logger.error("Ecommerce.userOptions error: ",e);
        }

    }

    /**
     * Initiates the Ecommerce application by displaying the main menu
     * and handling user options for both administrators and regular users.
     */
    public void start(customEnums.Role appState) {
        // Redirect interface to options for specific role.
        if (appState == customEnums.Role.Admin)
            adminOptions();
        else
            userOptions();
    }

    // User
    public void addToCart() {
        try {
            // Makes sure that the serial number is not only valid but also not added to cart already!
            String serialNumber = null;

            if (!catalog.getCatalog().isEmpty() && userCart.getCart().isEmpty()) {
                serialNumber = validateSN(); // No redundancy since cart is empty
            }
            else if (!catalog.getCatalog().isEmpty() && !userCart.getCart().isEmpty()) {
                // Checking for product redundancy in cart
                Boolean registeredButNotAdded = false;
                do {
                    serialNumber = Utils.validateStringInput("serial number");
                    if (!catalogService.isSerialRegistered(serialNumber, catalog.getCatalog()))
                        System.err.printf("Serial number %s does not exist!%n", serialNumber);
                    else if (cartService.itemInCartExists(serialNumber, userCart))
                        System.err.printf("%s is already in the cart!", catalog.getCatalog().get(serialNumber).getProductName());
                    else
                        registeredButNotAdded = true; //Product should exist but not yet added to cart.
                } while (!registeredButNotAdded);
            }

            Product productRef = catalog.getCatalog().get(serialNumber);
            if (cartService.addCartItem(userCart, serialNumber, productRef))
                logger.info(String.format("Ecommerce.addToCart successfully added %s to cart!", productRef.getProductName()));
            else
                logger.info(String.format("Ecommerce.addToCart failed to add %s to cart!", productRef.getProductName()));

        } catch (Exception e) {
            logger.error("Ecommerce.addToCart error: ",e);
        } finally {
            continueWithProgram();
        }
    }

    public void removeFromCart() {
        try {
            if (userCart.getCart().isEmpty())
                System.out.println("Empty cart, there's nothing to remove!");
            else {
                String snQuery = Utils.validateStringInput("serial number"); // Does not require the sn to be existing.
                boolean result = cartService.removeCartItem(snQuery,userCart);
                if (result)
                    logger.info(String.format("Ecommerce.removeFromCart successfully removed %s - %s from cart!", snQuery, catalog.getCatalog().get(snQuery).getProductName()));
                else
                    logger.info(String.format("Ecommerce.removeFromCart failed to remove %s - %s from cart!", snQuery, catalog.getCatalog().get(snQuery).getProductName()));
            }
        } catch (Exception e) {
            logger.error("Ecommerce.removeFromCart error: ",e);
        } finally {
            continueWithProgram();
        }
    }

    public void clearCart() {
        try {
            if (userCart.getCart().isEmpty())
                System.out.println("The cart is empty already!");
            else {
                Boolean result = cartService.removeAllItems(userCart);
                if (result)
                    logger.info("Ecommerce.clearCart successfully removed all cart items!");
                else
                    logger.info("Ecommerce.clearCart failed to remove all cart items!");
            }
        } catch (Exception e) {
            logger.error("Ecommerce.clearCart error: ",e);
        } finally {
            continueWithProgram();
        }
    }

    public void updateItemAmount() {
        try {
            // Makes sure that the serial number is valid and inside the cart!
            String serialNumber = null;
            if (userCart.getCart().isEmpty())
                System.out.println("Cart is empty, no item to update!");
            else {
                    if (!catalog.getCatalog().isEmpty() && !userCart.getCart().isEmpty()) {
                    // Checking if product exists
                    do {
                        serialNumber = Utils.validateStringInput("serial number");
                        if (!catalogService.isSerialRegistered(serialNumber, catalog.getCatalog()))
                            System.out.printf("Can't find product with %s in the cart.%n", serialNumber);
                    } while (!catalogService.isSerialRegistered(serialNumber, catalog.getCatalog()));
                }
                cartService.updateQuantity(serialNumber, userCart);
                logger.info("Ecommerce.updateItemAmount successfully updated product quantity");
            }
        } catch (Exception e) {
            logger.error("Ecommerce.updateItemAmount error: ",e);
        } finally {
            continueWithProgram();
        }
    }

    public void viewCart() {
        try {
            cartService.viewCartItems(userCart);
        } catch (Exception e) {
            logger.error("Ecommerce.viewCart error: ",e);
        } finally {
            continueWithProgram();
        }
    }

    public void showCartTotal() {
        try {
            System.out.printf("""
            +-----------------------------------------------------------+
            |Total: $%.2f                                               |
            +-----------------------------------------------------------+
            """, cartService.getTotal(userCart));
        } catch (Exception e) {
            logger.error("Ecommerce.showCartTotal error: ",e);
        } finally {
            continueWithProgram();
        }
    }

    // Admin

    public void createProduct() {
        try {
            String serialNumber = validateSN();

            customEnums.builderPreference preference = customEnums.builderPreference.minimum;// default is bare minimum.
            boolean isInvalidChoice = true;
            String choice;

            System.out.println("""
                    +=== Select Details ===+
                    |0 - Name & Price only |
                    |1 - With Description  |
                    |2 - With Seller       |
                    |3 - Complete          |
                    |4 - Return to menu    |
                    +=== -------------- ===+
                    """);
            do {
                System.out.print("Choice: ");
                choice = scn.nextLine();
                if (choice.trim().equals("4")) {
                    start(customEnums.Role.Admin);
                }

                if (choice.trim().matches("[0123]")) {
                    isInvalidChoice = false;
                    preference = fromChoice(Integer.parseInt(choice), customEnums.builderPreference.class);
                }
            } while (isInvalidChoice);

            String name = Utils.validateStringInput("product name");
            BigDecimal price = priceValidator.validateInput("product price");
            String description = "";
            String seller = "";

            switch (preference) {
                case withDescription -> description = Utils.validateStringInput("product description", 250);
                case withSeller -> seller = Utils.validateStringInput("product seller", 25);
                case allDetails -> {
                    description = Utils.validateStringInput("product description", 250);
                    seller = Utils.validateStringInput("product seller", 25);
                }
            }

            Product newProduct = catalogService.buildProduct(preference, name, price, description, seller);

            if (catalogService.addProduct(serialNumber, newProduct, catalog.getCatalog()))
                logger.info(String.format("Ecommerce.createProduct successfully added %s with sn[%s]", name, serialNumber));
            else
                logger.info(String.format("Ecommerce.createProduct failed to add %s with sn[%s]", name, serialNumber));

        } catch (Exception e) {
            logger.error("Ecommerce.createProduct error: ",e);
        } finally {
            continueWithProgram();
        }
    }

    /**
     * Validates user input for a serial number.
     * Loops until a valid serial number exists (unless the catalog is empty).
     *
     * @return The valid serial number.
     */
    private String validateSN() {
        String serialNumber = null;
        // Loops until serial number exists unless if pseudo-database is empty.
        if (!catalog.getCatalog().isEmpty()) {
            do {
                serialNumber = Utils.validateStringInput("serial number");
                if (!catalogService.isSerialRegistered(serialNumber, catalog.getCatalog()))
                    System.err.printf("Serial number %s does not exist!%n", serialNumber);
            } while (!catalogService.isSerialRegistered(serialNumber, catalog.getCatalog()));
        }
        return serialNumber;
    }

    public void updateProduct() {
        try {
            String serialNumber = validateSN();

            customEnums.updatePreference preference = customEnums.updatePreference.updatePrice;// default is price.
            boolean isInvalidChoice = true;
            String choice;

            System.out.println("""
                    +=== Select Field ===+
                    |0 - Name            |
                    |1 - Price           |
                    |2 - Seller          |
                    |3 - Description     |
                    |4 - Return to menu  |
                    +=== ------------ ===+
                    """);
            do {
                System.out.print("Choice: ");
                choice = scn.nextLine();
                if (choice.trim().equals("4")) {
                    start(customEnums.Role.Admin);
                }

                if (choice.trim().matches("[0123]")) {
                    isInvalidChoice = false;
                    preference = fromChoice(Integer.parseInt(choice), customEnums.updatePreference.class);
                }
            } while (isInvalidChoice);

            catalogService.updateProduct(preference,catalog.getCatalog().get(serialNumber));
            logger.info(String.format("Ecommerce.updateProduct updated product sn[%s]", serialNumber));
        } catch (Exception e) {
            logger.error("Ecommerce.updateProduct error: ",e);
        } finally {
            continueWithProgram();
        }
    }

    public void deleteProduct() {
        try {
            String serialNumber = null;
            // Loops until serial number exists unless if pseudo-database is empty.
            if (!catalog.getCatalog().isEmpty()) {
                do {
                    serialNumber = Utils.validateStringInput("serial number");
                    if (!catalogService.isSerialRegistered(serialNumber, catalog.getCatalog()))
                        System.err.printf("Serial number %s does not exist!%n", serialNumber);
                } while (!catalogService.isSerialRegistered(serialNumber, catalog.getCatalog()));
            }

            if (catalogService.removeProduct(serialNumber, catalog.getCatalog()))
                logger.info(String.format("Ecommerce.deleteProduct successfully removed product sn[%s]", serialNumber));
            else
                logger.info(String.format("Ecommerce.deleteProduct failed to remove product sn[%s]", serialNumber));
        } catch (Exception e) {
            logger.error("Ecommerce.removeProduct error: ",e);
        } finally {
            continueWithProgram();
        }
    }

    public void searchProduct() {
        try {
            customEnums.searchPreference preference = customEnums.searchPreference.bySN;// default is serial number.
            boolean isInvalidChoice = true;
            String choice;

            System.out.println("""
                    +=== Select Search Field ===+
                    |0 - Name                   |
                    |1 - Serial Number          |
                    |2 - Seller                 |
                    |3 - Return to menu         |
                    +=== ------------------- ===+
                    """);
            do {
                System.out.print("Choice: ");
                choice = scn.nextLine();
                if (choice.trim().equals("3")) {
                    start(customEnums.Role.Admin);
                }

                if (choice.trim().matches("[012]")) {
                    isInvalidChoice = false;
                    preference = fromChoice(Integer.parseInt(choice), customEnums.searchPreference.class);
                }
            } while (isInvalidChoice);

            String query = Utils.validateStringInput("Search query");

            List<Product> results = catalogService.searchCatalog(preference, query, catalog.getCatalog());
            System.out.printf("Found %d results.",results.size());
            if (!results.isEmpty()) {
                System.out.println("View fields?");
                String input;
                do {
                    System.out.println("(y/n):");
                    input = scn.nextLine().toLowerCase();
                } while (!input.matches("[yn]"));
            }

            logger.info(String.format("Ecommerce.searchProduct performed search query: %s on preference: %s",query,preference.toString()));
        } catch (Exception e) {
            logger.error("Ecommerce.searchProduct error: ", e);
        } finally {
            continueWithProgram();
        }
    }

    // Both roles
    public void readCatalog() {
        try {
            catalogService.viewCatalog(catalog.getCatalog());
        } catch (Exception e) {
            logger.error("Ecommerce.readCatalog error: ",e);
        } finally {
            continueWithProgram();
        }
    }

    public void viewProducts() {
        try {
            catalogService.viewCatalogVerbose(catalog.getCatalog());
        } catch (Exception e) {
            logger.error("Ecommerce.viewCatalogVerbose error: ",e);
        } finally {
            continueWithProgram();
        }
    }

    /**
     * A function that prompts to continue running the program,
     * accepts <code>y</code> or <code>n</code> to return back to
     * the {@link #start(customEnums.Role)} or exit the program.
     */
    private void continueWithProgram() {
        String input;
        do {
            try {
                System.out.println("""
                
                +=============================+\
                | Continue using the program? |\
                +=============================+\
                (y/n):""");
                input = scn.nextLine().toLowerCase();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input, kindly try again.");
                input = "";
            }
        } while (!input.matches("[yn]"));
        if (input.equals("y"))
            start(appState);
        else {
            if (!catalog.getCatalog().isEmpty()) {
                logger.info(Utils.storeData(catalog.getCatalog()) ? "Successfully stored data." : "Failed to store data.");
            }
            System.out.println("Closing program...");
        }
    }


}
