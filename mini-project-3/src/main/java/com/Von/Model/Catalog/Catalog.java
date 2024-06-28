package com.Von.Model.Catalog;

import java.util.HashMap;
import java.util.Map;

public class Catalog<K, V> {
    private static Catalog<?, ?> singleInstance = null;
    private final Map<K, V> productCatalog;

    private Catalog() {
        productCatalog = new HashMap<>();
    }

    public static synchronized Catalog<?, ?> getInstance() {
        if (singleInstance == null)
            singleInstance = new Catalog<>();

        return singleInstance;
    }

    public Map<K, V> getProductCatalog() {
        return this.productCatalog;
    }
}
