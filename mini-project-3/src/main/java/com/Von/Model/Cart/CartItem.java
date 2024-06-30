package com.Von.Model.Cart;

import java.math.BigDecimal;

public class CartItem {
    private final String name;
    private final BigDecimal price;
    private final String serialNumber;
    private Integer quantity = 1;
    private BigDecimal subtotalPrice;

    public CartItem(String _name, BigDecimal _price, String _serialNumber) {
        this.name = _name;
        this.price = _price;
        this.serialNumber = _serialNumber;
        this.subtotalPrice = price.multiply(BigDecimal.valueOf(quantity));
    }

    public CartItem(String _name, BigDecimal _price, String _serialNumber,Integer _amount) {
        this.name = _name;
        this.price = _price;
        this.serialNumber = _serialNumber;
        this.quantity = _amount;
        this.subtotalPrice = price.multiply(BigDecimal.valueOf(quantity));
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getSN() {
        return serialNumber;
    }

    public BigDecimal getSubtotalPrice() {
        return subtotalPrice;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        recalibrateSubtotal();
    }

    private void recalibrateSubtotal() {
        this.subtotalPrice = price.multiply(BigDecimal.valueOf(quantity));
    }
}
