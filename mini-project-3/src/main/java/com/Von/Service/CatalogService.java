package com.Von.Service;

import com.Von.Model.Enums.customEnums;
import com.Von.Model.Catalog.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CatalogService {

    boolean isSerialRegistered(String serialNo, Map<String, Product> records);
    boolean addProduct(String serialNumberKey, Product newProduct, Map<String, Product> records);
    Product buildProduct(customEnums.builderPreference builder, String name, BigDecimal price, String description, String seller);
    boolean removeProduct(String serialNumberKey, Map<String, Product> records);
    void updateProduct(customEnums.updatePreference preference, Product product);
    List<Product> searchCatalog(customEnums.searchPreference preference, String query, Map<String, Product> records);
    void viewCatalogVerbose(Map<String, Product> records);
}
