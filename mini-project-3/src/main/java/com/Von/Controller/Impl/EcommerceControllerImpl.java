package com.Von.Controller.Impl;

import com.Von.Model.Catalog.Catalog;
import com.Von.Model.Catalog.Product;
import com.Von.Model.Enums.customEnums;
import com.Von.Service.Impl.CatalogServiceImpl;
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

public class EcommerceControllerImpl {
    private static final Scanner scn = new Scanner(System.in);
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    private static final String fileName = "src/main/resources/data.products.json";
    private final CatalogServiceImpl catalogService = new CatalogServiceImpl();
    private NumericInputValidator<BigDecimal> priceValidator = new NumericInputValidator<>(new BigDecimal("9999999.0"));

    Catalog catalog;

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

    public void start() {

    }

    public void createProduct() {
        try {
            String serialNumber;
            do {
                serialNumber = Utils.validateStringInput("serial number");
                if (catalog.getCatalog().isEmpty())
                    break;

                if (catalogService.isSerialRegistered(serialNumber, catalog.getCatalog()))
                    System.err.println("Serial number is already exists!");

            } while(catalogService.isSerialRegistered(serialNumber, catalog.getCatalog()));

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
                    start();
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
        }
    }

    public void readCatalog() {
        try {
            catalogService.viewCatalog(catalog.getCatalog());
        } catch (Exception e) {
            logger.error("Ecommerce.readCatalog error: ",e);
        }
    }

    public void updateProduct() {
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
                    start();
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
                    start();
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
        }
    }

    /**
     * A function that prompts to continue running the program,
     * accepts <code>y</code> or <code>n</code> to return back to
     * the {@link #start()} or exit the program.
     */
    private void continueWithProgram() {
        String input;
        do {
            try {
                System.out.println("""
                \
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
            start();
        else {
            if (!catalog.getCatalog().isEmpty()) {
                logger.info(Utils.storeData(catalog.getCatalog()) ? "Successfully stored data." : "Failed to store data.");
            }
            System.out.println("Closing program...");
        }
    }


}
