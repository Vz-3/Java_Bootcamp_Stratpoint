package com.Von.Service.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import com.Von.Model.Catalog.Product;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
    private static final Scanner scn = new Scanner(System.in);
    private static final Integer defaultMinLength = 4;
    private static final Integer defaultMaxLength = 20;
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    private static final String fileName = "src/main/resources/data.products.json";
    private Utils() {}

    /**
     * Validates user input for a specified field.
     *
     * @param fieldName The name of the field being validated.
     * @param minLength The minimum allowed length of the input.
     * @param maxLength The maximum allowed length of the input.
     * @return The valid input string.
     */
    private static String validator(String fieldName, int minLength, int maxLength) {
        String validString;
        boolean firstTime = true;
        final Pattern invalidChars = Pattern.compile("[^a-zA-Z0-9&.,()\\s-]"); // Updated regex

        System.out.printf("%s:", fieldName);
        do {
            try {
                validString = scn.nextLine();
                Matcher matcher = invalidChars.matcher(validString);

                if (validString.isBlank()
                        || (validString.trim().length() < minLength || validString.trim().length() > maxLength)
                        || matcher.find()) {
                    if (firstTime) {
                        System.err.printf("""
                                ===== Invalid input =====
                                Either:
                                \t Blank input
                                \t Less than %d characters
                                \t More than %d characters
                                \t Invalid character/symbol
                                Please try again:""", minLength, maxLength);
                        firstTime = false;
                    } else {
                        System.out.printf("%s:", fieldName);
                    }
                } else {
                    return validString;
                }
            } catch (NoSuchElementException e) {
                System.err.println("Error reading input. Please try again.");
                validString = ""; // Reset the input.
            }
        } while (true);
    }

    public static String validateStringInput(String fieldName) {
        return validator(fieldName, defaultMinLength, defaultMaxLength);
    }

    public static String validateStringInput(String fieldName, Integer maxLength) {
        return validator(fieldName, defaultMinLength, maxLength);
    }

    public static String validateStringInput(String fieldName, Integer minLength, Integer maxLength) {
        return validator(fieldName, minLength, maxLength);
    }

    /**
     * Imports product data from a JSON file and populates a catalog map.
     *
     * @return A map of serial numbers to corresponding products.
     */
    public static Map<String, Product> importData() {
        Map<String, Product> existingCatalog = new HashMap<>();

        try {
            String jsonContent = FileUtils.readFileToString(new File(fileName), StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(jsonContent);

            jsonArray.forEach(jsonObj -> {
                JSONObject jsonObject = (JSONObject) jsonObj;
                String sn = jsonObject.getString("sn");
                String name = jsonObject.getString("product_name");
                BigDecimal price = jsonObject.getBigDecimal("product_price");
                String seller = jsonObject.getString("product_seller");
                String description = jsonObject.getString("product_description");
                BigDecimal rating = jsonObject.getBigDecimal("product_rating");

                Product existingProduct = new Product.ProductBuilder(name, price)
                        .seller(seller).description(description).rating(rating)
                        .build();

                existingCatalog.put(sn, existingProduct);
            });
        } catch (IOException | ClassCastException e) {
            logger.error("Utils.importData error: ", e);
        }
        return existingCatalog;
    }

    /**
     * Stores the catalog data (products) in a JSON file.
     *
     * @param catalog The map of serial numbers to corresponding products.
     * @return <code>true</code> if the data is successfully stored, otherwise <code>false</code>.
     */
    public static boolean storeData(Map<String, Product> catalog) {
        try (FileWriter writer = new FileWriter(fileName)) {
            StringBuilder json = new StringBuilder("[\n");
            if (!catalog.isEmpty()) {
                catalog.forEach((key, product) -> json.append(String.format("""
                {
                    "sn": "%s",
                    "product_name": "%s",
                    "product_price": "%s",
                    "product_seller": "%s",
                    "product_description": "%s",
                    "product_rating": "%s"
                },
            """,
                        key,
                        product.getProductName(),
                        product.getProductPrice(),
                        product.getProductSeller() == null ? "" : product.getProductSeller(),
                        product.getProductDescription() == null ? "" : product.getProductDescription(),
                        product.getProductCompoundedRatings())));
                json.deleteCharAt(json.length() - 2); // Remove the trailing comma and newline
            }
            json.append("]");

            writer.write(json.toString());
            System.out.println("Data written to " + fileName);
        } catch (IOException e) {
            logger.error("Utils.storeData error: ", e);
            return false;
        }
        return true;
    }

}
