/*
 * Order.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Entity class representing an Order placed by a customer and served by an employee.
 * Encapsulates order identifier, order date, customer identifier, employee identifier,
 * and order total amount.
 * 
 * @author Fresher Academy
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier of the order.
     */
    private int orderId;

    /**
     * Creation date of the order.
     */
    private Date orderDate;

    /**
     * Identifier of the customer who placed the order.
     */
    private int customerId;

    /**
     * Identifier of the employee who handled the order.
     */
    private int employeeId;

    /**
     * Total amount of the order.
     */
    private double total;

    /**
     * Default constructor to allow instantiating empty Order instances.
     */
    public Order() {
    }

    /**
     * Full-parameter constructor initializing all 5 attributes.
     * 
     * @param orderId    Unique order ID.
     * @param orderDate  Date of order.
     * @param customerId Customer ID.
     * @param employeeId Employee ID.
     * @param total      Order total.
     */
    public Order(int orderId, Date orderDate, int customerId, int employeeId, double total) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.total = total;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId &&
                customerId == order.customerId &&
                employeeId == order.employeeId &&
                Double.compare(order.total, total) == 0 &&
                Objects.equals(orderDate, order.orderDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderDate, customerId, employeeId, total);
    }

    @Override
    public String toString() {
        return String.format("Order [orderId=%d, orderDate=%s, customerId=%d, employeeId=%d, total=%.2f]",
                orderId, orderDate, customerId, employeeId, total);
    }
}
