package com.Von.Model.Catalog;

import java.util.HashMap;
import java.util.Map;

public class Catalog {
    private static Catalog singleInstance = null;
    private final Map<String, Product> catalogData;

    private Catalog() {
        catalogData = new HashMap<>();
    }

    private Catalog(Map<String, Product> initialData) {
        catalogData = new HashMap<>(initialData);
    }

    public static synchronized Catalog getInstance() {
        if (singleInstance == null)
            singleInstance = new Catalog();

        return singleInstance;
    }

    public static synchronized Catalog getInstance(Map<String, Product> initialData) {
        if (singleInstance == null)
            singleInstance = new Catalog(initialData);

        return singleInstance;
    }

    public Map<String, Product> getCatalog() {
        return this.catalogData;
    }

}
