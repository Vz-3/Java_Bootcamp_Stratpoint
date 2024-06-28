package com.Von.Service;

import com.Von.Model.Enums.updatePreference;
import com.Von.Model.Catalog.Product;
import com.Von.Model.Enums.builderPreference;
import com.Von.Model.Enums.searchPreference;

import java.util.List;
import java.util.Map;

public interface CatalogService {

    boolean isSerialRegistered(String serialNo, Map<String, Product> records);
    boolean addProduct(String serialNumberKey, Product newProduct, Map<String, Product> records);
    Product buildProduct(builderPreference builder, String name, Double price, String description, String seller);
    boolean removeProduct(String serialNumberKey, Map<String, Product> records);
    void updateProduct(updatePreference preference, Product product);
    List<Product> searchCatalog(searchPreference preference, String query, Map<String, Product> records);
    void viewCatalog(Map<String, Product> records);
}
