/*
 * Customer.java
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
 * Entity class representing a Customer in the Sales Management System (SMS).
 * Holds customer identifier and customer name.
 * 
 * @author Fresher Academy
 */
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier of the customer.
     */
    private int customerId;

    /**
     * Name of the customer.
     */
    private String customerName;

    /**
     * Default constructor to allow instantiating empty Customer instances.
     */
    public Customer() {
    }

    /**
     * Full-parameter constructor to initialize all attributes.
     * 
     * @param customerId   Unique customer ID.
     * @param customerName Customer name.
     */
    public Customer(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId == customer.customerId &&
                Objects.equals(customerName, customer.customerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, customerName);
    }

    @Override
    public String toString() {
        return String.format("Customer [customerId=%d, customerName=%s]", customerId, customerName);
    }
}
