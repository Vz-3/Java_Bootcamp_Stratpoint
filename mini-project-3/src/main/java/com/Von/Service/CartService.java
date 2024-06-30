package com.Von.Service;

import com.Von.Model.Cart.Cart;
import com.Von.Model.Catalog.Product;

import java.math.BigDecimal;

public interface CartService {
    Boolean itemInCartExists(String sn, Cart userCart);
    Boolean addCartItem(Cart userCart, String sn, Product product);
    Boolean removeCartItem(String sn, Cart userCart);
    void updateQuantity(String sn, Cart userCart);
    void viewCartItems(Cart userCart);
    Boolean removeAllItems(Cart userCart);
    BigDecimal getTotal(Cart userCart);
}
