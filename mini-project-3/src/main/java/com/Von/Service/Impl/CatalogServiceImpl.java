package com.Von.Service.Impl;

import com.Von.Model.Catalog.Product;
import com.Von.Model.Enums.searchPreference;
import com.Von.Model.Enums.builderPreference;
import com.Von.Model.Enums.updatePreference;
import com.Von.Service.CatalogService;
import com.Von.Service.Utils.NumericInputValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.Von.Service.Utils.Utils.validateStringInput;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CatalogServiceImpl implements CatalogService {
    private static final Logger logger = LoggerFactory.getLogger(CatalogServiceImpl.class);

    NumericInputValidator<Double> priceValidator = new NumericInputValidator<>(9_999_999.0);

    public CatalogServiceImpl() {}

    public boolean isSerialRegistered(String serialNo, Map<String, Product> records) {
        return records.containsKey(serialNo);
    }

    public boolean addProduct(String serialNumberKey, Product newProduct, Map<String, Product> records) {
        records.put(serialNumberKey, newProduct);
        return isSerialRegistered(serialNumberKey, records);
    }

    public Product buildProduct(builderPreference builder, String name, Double price, String description, String seller) {
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

    public boolean removeProduct(String serialNumberKey, Map<String, Product> records) {
        records.remove(serialNumberKey);

        // Flips the boolean value so that if key is not found, returns true that the product is removed.
        return !isSerialRegistered(serialNumberKey, records);
    }

    public void updateProduct(updatePreference preference, Product product) {
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

    public List<Product> searchCatalog(searchPreference preference, String query, Map<String, Product> records) {
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

    public void viewCatalog(Map<String, Product> records) {
        try {
            records.forEach((serialNo, product) -> System.out.printf("""
                            === Product ===
                            SN: %s
                            Name: %s
                            Price: %.2f
                            Description: %s
                            Seller: %s
                            ==== ------ ====
                            """, serialNo,
                    product.getProductName(),
                    product.getProductPrice(),
                    product.getProductDescription(),
                    product.getProductSeller()));
        } catch (Exception e) {
            logger.error("CatalogServiceImpl.viewCatalog error: ",e);
        }
    }
}
