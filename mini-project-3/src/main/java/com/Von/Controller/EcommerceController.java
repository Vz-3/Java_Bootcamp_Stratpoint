package com.Von.Controller;

import com.Von.Model.Enums.customEnums;

public interface EcommerceController {
    void viewProducts();
    void readCatalog();
    void createProduct();
    void updateProduct();
    void deleteProduct();
    void searchProduct();

    void addToCart();
    void removeFromCart();
    void clearCart();
    void updateItemAmount();
    void viewCart();
    void showCartTotal();

    void start(customEnums.Role appState);
}
