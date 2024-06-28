package com.Von.Model;

import com.Von.Model.Catalog.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    private Product productComplete;
    private Product productBareMinimum;
    private final String initName = "Iphone X";
    private final Double initPrice = 499.0;
    private final String initDescription = "is a smartphone designed, developed, and marketed by Apple...";
    private final String initSeller = "Apple";

    @BeforeEach
    void setUp() {
        productComplete = new Product.ProductBuilder(initName, initPrice)
                .description(initDescription)
                .seller(initSeller)
                .build();

        assertNotNull(productComplete);

        productBareMinimum = new Product.ProductBuilder(initName, initPrice)
                .build();

        assertNotNull(productBareMinimum);
    }

    @Test
    void test_getProductName() {
        assertEquals(initName, productComplete.getProductName());
    }

    @Test
    void test_getProductPrice() {
        assertEquals(initPrice, productComplete.getProductPrice());
    }

    @Test
    void test_getProductDescription() {
        assertEquals(initDescription, productComplete.getProductDescription());

        assertNull(productBareMinimum.getProductDescription());
        assertNotEquals(initDescription, productBareMinimum.getProductDescription());
    }

    @Test
    void test_getProductSeller() {
        assertEquals(initSeller, productComplete.getProductSeller());

        assertNull(productBareMinimum.getProductSeller());
        assertNotEquals(initSeller, productBareMinimum.getProductSeller());
    }

    @Test
    void test_getAllFields() {
        String[] expectedComplete = new String[]{initName, String.valueOf(initPrice), initDescription, initSeller, "3.0"};
        String[] expectedBareMinimum = new String[]{initName, String.valueOf(initPrice), "", "", "3.0"};

        assertArrayEquals(expectedComplete, productComplete.getAllFields());
        assertArrayEquals(expectedBareMinimum, productBareMinimum.getAllFields());
    }
}