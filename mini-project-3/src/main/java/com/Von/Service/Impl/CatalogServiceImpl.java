package com.Von.Service.Impl;

import com.Von.Model.Catalog.Product;
import com.Von.Model.Enums.customEnums;
import com.Von.Service.CatalogService;
import com.Von.Service.Utils.NumericInputValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.Von.Service.Utils.Utils.validateStringInput;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CatalogServiceImpl implements CatalogService {
    private static final Logger logger = LoggerFactory.getLogger(CatalogServiceImpl.class);

    NumericInputValidator<BigDecimal> priceValidator = new NumericInputValidator<>(new BigDecimal("9999999.0") );

    public CatalogServiceImpl() {}

    /**
     * Checks whether a product with the specified serial number exists in the catalog.
     *
     * @param serialNo The serial number to check.
     * @param records The map of serial numbers to corresponding products.
     * @return <code>true</code> if the serial number is registered, otherwise <code>false</code>.
     */
    public boolean isSerialRegistered(String serialNo, Map<String, Product> records) {
        return records.containsKey(serialNo);
    }

    /**
     * Adds a new product to the catalog.
     *
     * @param serialNumberKey The serial number key for the new product.
     * @param newProduct The product to add.
     * @param records The map of serial numbers to corresponding products.
     * @return <code>true</code> if the product is successfully added, otherwise <code>false</code>.
     */
    public boolean addProduct(String serialNumberKey, Product newProduct, Map<String, Product> records) {
        records.put(serialNumberKey, newProduct);
        return isSerialRegistered(serialNumberKey, records);
    }

    /**
     * Builds a new <code>Product</code> based on the specified details.
     *
     * @param builder The preference for building the product (minimal, with description, or with seller).
     * @param name The product name.
     * @param price The product price.
     * @param description The product description (optional).
     * @param seller The product seller (optional).
     * @return The constructed <code>Product</code>.
     */
    public Product buildProduct(customEnums.builderPreference builder, String name, BigDecimal price, String description, String seller) {
        Product.ProductBuilder productBuilder = new Product.ProductBuilder(name, price);
        switch (builder) {
            case withDescription:
                productBuilder.description(description);
                break;
            case withSeller:
                productBuilder.seller(seller);
                break;
            case allDetails:
                productBuilder.description(description).seller(seller);
                break;
            // For minimum, no additional steps
        }
        return productBuilder.build();
    }

    /**
     * Removes a product from the catalog based on the provided serial number.
     *
     * @param serialNumberKey The serial number key of the product to remove.
     * @param records The map of serial numbers to corresponding products.
     * @return <code>true</code> if the product is successfully removed, otherwise <code>false</code>.
     */
    public boolean removeProduct(String serialNumberKey, Map<String, Product> records) {
        records.remove(serialNumberKey);

        // Flips the boolean value so that if key is not found, returns true that the product is removed.
        return !isSerialRegistered(serialNumberKey, records);
    }

    /**
     * Updates specific details of a product based on the preference.
     *
     * @param preference The type of update (name, price, description, or seller).
     * @param product The product to update.
     */
    public void updateProduct(customEnums.updatePreference preference, Product product) {
        switch (preference) {
            case updateName -> product.
                    setProductName
                            (validateStringInput("Set product name"));
            case updatePrice -> product.
                    setProductPrice
                            (priceValidator.validateInput("Set price"));
            case updateDescription -> product.
                    setProductDescription
                            (validateStringInput("Set product description", 250));
            case updateSeller -> product.
                    setProductSeller
                            (validateStringInput("Set product seller", 25));
        }
    }

    /**
     * Searches the catalog for products based on the specified preference and query.
     *
     * @param preference The search preference (by name, seller, or serial number).
     * @param query The search query (e.g., product name, seller name, or serial number).
     * @param records The map of serial numbers to corresponding products.
     * @return A list of products matching the search criteria.
     */
    public List<Product> searchCatalog(customEnums.searchPreference preference, String query, Map<String, Product> records) {
        return switch (preference) {
            case byName -> records.
                    values().stream().
                    filter
                            (product -> product.getProductName().contains(query)).
                    collect
                            (Collectors.toList());
            case bySeller -> records.
                    values().stream().
                    filter
                            (product -> product.getProductSeller().contains(query)).
                    collect
                            (Collectors.toList());
            case bySN -> isSerialRegistered(query, records) ?
                    Collections.singletonList(records.get(query)) : null;
        };
    }

    /**
     * Displays detailed information about each product in the catalog.
     *
     * @param records The map of serial numbers to corresponding products.
     */
    public void viewCatalog(Map<String, Product> records) {
        try {
            System.out.println("=== Product ===");
            records.forEach((serialNo, product) -> System.out.printf("""
                            SN: %s
                            Name: %s
                            Price: %.2f
                            Description: %s
                            Seller: %s
                            ====+======+====
                            """, serialNo,
                    product.getProductName(),
                    product.getProductPrice(),
                    product.getProductDescription(),
                    product.getProductSeller()));
        } catch (Exception e) {
            logger.error("CatalogServiceImpl.viewCatalog error: ",e);
        }
    }

    /**
     * Displays a concise view of the catalog, showing only serial numbers, product names, and prices.
     *
     * @param records The map of serial numbers to corresponding products.
     */
    public void viewCatalogVerbose(Map<String, Product> records) {
        try {
            System.out.println("""
                +--------------------+--------------------+----------+
                |Serial Number       |Product Name        |Price(USD)|
                +--------------------+--------------------+----------+
                """);
            records.forEach((serialNo, product) -> System.out.printf("""
                |%-20s |%-20s | %7.2f|
                """, serialNo, product.getProductName(), product.getProductPrice()));
            System.out.println("+--------------------+--------------------+----------+");
        } catch (Exception e) {
            logger.error("CatalogServiceImpl.viewCatalogVerbose error: ", e);
        }
    }
}
