/*
 * LineItem.java
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
 * Entity class representing an item line within an Order.
 * Associates an order with a product, along with purchased quantity and price.
 * 
 * @author Fresher Academy
 */
public class LineItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifier of the related order.
     */
    private int orderId;

    /**
     * Identifier of the purchased product.
     */
    private int productId;

    /**
     * Quantity of the product ordered.
     */
    private int quantity;

    /**
     * Unit price at the time of purchase.
     */
    private double price;

    /**
     * Default constructor to allow instantiating empty LineItem instances.
     */
    public LineItem() {
    }

    /**
     * Full-parameter constructor initializing all attributes.
     * 
     * @param orderId   Order ID.
     * @param productId Product ID.
     * @param quantity  Quantity purchased.
     * @param price     Unit price.
     */
    public LineItem(int orderId, int productId, int quantity, double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItem lineItem = (LineItem) o;
        return orderId == lineItem.orderId &&
                productId == lineItem.productId &&
                quantity == lineItem.quantity &&
                Double.compare(lineItem.price, price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, productId, quantity, price);
    }

    @Override
    public String toString() {
        return String.format("LineItem [orderId=%d, productId=%d, quantity=%d, price=%.2f]",
                orderId, productId, quantity, price);
    }
}
