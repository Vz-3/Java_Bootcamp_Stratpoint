package com.Von.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    private Product productComplete;
    private Product productBareMinimum;
    private final String initName = "Iphone X";
    private final String initSerialNo = "2103-071-150-0456";
    private final Double initPrice = 499.0;
    private final String initDescription = "is a smartphone designed, developed, and marketed by Apple...";
    private final String initSeller = "Apple";

    @BeforeEach
    void setUp() {
        productComplete = new Product.ProductBuilder(initName, initSerialNo, initPrice)
                .description(initDescription)
                .seller(initSeller)
                .build();

        assertNotNull(productComplete);

        productBareMinimum = new Product.ProductBuilder(initName, initSerialNo, initPrice)
                .build();

        assertNotNull(productBareMinimum);
    }

    @Test
    void test_getProductName() {
        assertEquals(initName, productComplete.getProductName());
    }

    @Test
    void test_getProductSN() {
        assertEquals(initSerialNo, productComplete.getProductSerialNo());
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
}