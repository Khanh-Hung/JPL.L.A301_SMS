/*
 * Product.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entity class representing a Product item sold in the store.
 * Encapsulates product identifier, product name, and unit list price.
 * 
 * @author Fresher Academy
 */
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier of the product.
     */
    private int productId;

    /**
     * Name of the product item.
     */
    private String productName;

    /**
     * Standard list price of the product.
     */
    private double listPrice;

    /**
     * Default constructor to allow instantiating empty Product instances.
     */
    public Product() {
    }

    /**
     * Full-parameter constructor to initialize all attributes.
     * 
     * @param productId   Unique product ID.
     * @param productName Name of the product.
     * @param listPrice   List price.
     */
    public Product(int productId, String productName, double listPrice) {
        this.productId = productId;
        this.productName = productName;
        this.listPrice = listPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getListPrice() {
        return listPrice;
    }

    public void setListPrice(double listPrice) {
        this.listPrice = listPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return productId == product.productId &&
                Double.compare(product.listPrice, listPrice) == 0 &&
                Objects.equals(productName, product.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, listPrice);
    }

    @Override
    public String toString() {
        return String.format("Product [productId=%d, productName=%s, listPrice=%.2f]",
                productId, productName, listPrice);
    }
}
