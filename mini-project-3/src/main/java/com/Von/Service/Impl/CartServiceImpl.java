package com.Von.Service.Impl;

import com.Von.Model.Cart.Cart;
import com.Von.Model.Cart.CartItem;
import com.Von.Model.Catalog.Product;
import com.Von.Service.CartService;
import com.Von.Service.Utils.NumericInputValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class CartServiceImpl implements CartService {
    private final NumericInputValidator<BigDecimal> quantityValidator = new NumericInputValidator<>(new BigDecimal("999"));
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    /**
     * Checks whether an item with the specified serial number exists in the user's cart.
     *
     * @param sn The serial number to check.
     * @param userCart The user's cart containing cart items.
     * @return <code>true</code> if the item exists in the cart, otherwise <code>false</code>.
     */
    public Boolean itemInCartExists(String sn, Cart userCart) {
        if (userCart.getCart().isEmpty())
            return false;

        Boolean itemExists = false;
        for (CartItem cart: userCart.getCart()) {
            if (cart.getSN().equals(sn)) {
                itemExists = true;
                break;
            }
        }
        return itemExists;
    }

    /**
     * Adds a new item to the user's cart.
     *
     * @param userCart The user's cart.
     * @param sn The serial number of the product to add.
     * @param product The product details (name, price, etc.) to add.
     * @return <code>true</code> if the item is successfully added to the cart.
     */
    public Boolean addCartItem(Cart userCart, String sn, Product product) {
        CartItem newItem = new CartItem(product.getProductName(), product.getProductPrice(), sn, quantityValidator.validateInput("Set quantity").intValueExact());
        userCart.getCart().add(newItem);

        return itemInCartExists(sn, userCart);
    }

    /**
     * Removes an item from the user's cart based on the provided serial number.
     *
     * @param sn The serial number of the item to remove.
     * @param userCart The user's cart.
     * @return <code>true</code> if the item is successfully removed, otherwise <code>false</code>.
     */
    public Boolean removeCartItem(String sn, Cart userCart) {
        return userCart.getCart().removeIf(cartItem -> cartItem.getSN().equals(sn));
    }

    /**
     * Removes all items from the user's cart.
     *
     * @param userCart The user's cart.
     * @return <code>true</code> if all items are successfully removed, otherwise <code>false</code>.
     */
    public Boolean removeAllItems(Cart userCart) {
        userCart.getCart().clear();

        return userCart.getCart().isEmpty();
    }

    /**
     * Updates the quantity of an item in the user's cart.
     *
     * @param sn The serial number of the item to update.
     * @param userCart The user's cart.
     */
    public void updateQuantity(String sn, Cart userCart) {
        userCart.getCart().stream()
                .filter(cartItem -> cartItem.getSN().equals(sn))
                .findFirst()
                .ifPresent(cartItem -> cartItem.setQuantity(quantityValidator.validateInput("Set quantity").intValueExact()));
    }

    /**
     * Displays the items currently in the user's cart.
     *
     * @param userCart The user's cart containing cart items.
     */
    public void viewCartItems(Cart userCart) {
        try {
            System.out.println("""
                +--------------------+--------------------+----------+------+
                |Serial Number       |Product Name        |Price(USD)|   Qty|
                +--------------------+--------------------+----------+------+""");
            userCart.getCart().forEach(cartItem -> System.out.printf("""
                |%-19s |%-19s | %9.2f| %4d|
                """, cartItem.getSN(), cartItem.getName(), cartItem.getPrice(), cartItem.getQuantity()));
            System.out.println("+--------------------+--------------------+----------+------+");
            System.out.printf("""
            |Total: %1c%50.2f |
            +-----------------------------------------------------------+
            """, '$',getTotal(userCart));
        } catch (Exception e) {
            logger.error("CartServiceImpl.viewCartItems error: ", e);
        }
    }

    /**
     * Calculates the total cost of all items in the user's cart.
     *
     * @param userCart The user's cart containing cart items.
     * @return The total cost as a <code>BigDecimal</code>.
     */
    public BigDecimal getTotal(Cart userCart) {
        return userCart.getCart().stream()
                .map(CartItem::getSubtotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
