package com.Von.Model.Catalog;

import java.math.BigDecimal;

public class Product {
    private String productName;
    private BigDecimal productPrice;
    private String productDescription; //optional
    private String productSeller; //optional

    private BigDecimal productCompoundedRatings = BigDecimal.valueOf(3); //optional

    private Product(ProductBuilder builder) {
        this.productName = builder.productName;
        this.productPrice = builder.productPrice;
        this.productDescription = builder.productDescription;
        this.productSeller = builder.productSeller;
    }

    public static class ProductBuilder {
        private final String productName;
        private final BigDecimal productPrice;
        private String productDescription; //optional
        private String productSeller; //optional
        private BigDecimal productCompoundedRatings = BigDecimal.valueOf(3);

        // constructor for required fields.
        public ProductBuilder(String _name, BigDecimal _price ) {
            this.productName = _name;
            this.productPrice = _price;
        }

        public ProductBuilder description(String _description) {
            this.productDescription = _description;
            return this;
        }

        public ProductBuilder seller(String _seller) {
            this.productSeller = _seller;
            return this;
        }

        public ProductBuilder rating(BigDecimal _rating) {
            this.productCompoundedRatings = _rating;
            return this;
        }

        public Product build() {
            return new Product(this);
        }
    }

    // Getters & Setters

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public void setProductSeller(String productSeller) {
        this.productSeller = productSeller;
    }

    public void setProductCompoundedRatings(BigDecimal productRating) {this.productCompoundedRatings = productRating;}

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductSeller() {
        return productSeller;
    }

    public BigDecimal getProductCompoundedRatings() {
        return productCompoundedRatings;
    }

    public String[] getAllFields() {
        String[] productFields = new String[5];

        productFields[0] = getProductName();
        productFields[1] = String.valueOf(getProductPrice());
        productFields[2] = getProductDescription()==null ? "" : getProductDescription();
        productFields[3] = getProductSeller()==null ? "" : getProductSeller();
        productFields[4] = String.valueOf(getProductCompoundedRatings());

        return productFields;
    }
}
