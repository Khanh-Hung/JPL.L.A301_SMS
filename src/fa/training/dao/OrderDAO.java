/*
 * OrderDAO.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.dao;

import fa.training.entities.Order;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Order entity operations.
 * Defines methods for querying, creating, and updating orders and computing order totals.
 * 
 * @author Fresher Academy
 */
public interface OrderDAO {

    /**
     * Retrieves all orders in the database.
     * 
     * @return List of Order objects.
     * @throws SQLException If database access error occurs.
     */
    List<Order> getAllOrders() throws SQLException;

    /**
     * Requirement 2: List all orders consist of order id, order date, customer id,
     * employee id, total for a given customer.
     * 
     * @param customerId Customer ID.
     * @return List of Orders belonging to customer.
     * @throws SQLException If database access error occurs.
     */
    List<Order> getAllOrdersByCustomerId(int customerId) throws SQLException;

    /**
     * Finds an order by its unique ID.
     * 
     * @param orderId Order ID.
     * @return Order object or null if not found.
     * @throws SQLException If database access error occurs.
     */
    Order getOrderById(int orderId) throws SQLException;

    /**
     * Requirement 4: Compute order total using User Defined Function (UDF).
     * 
     * @param orderId Order ID.
     * @return Computed total price of the order.
     * @throws SQLException If database access error occurs.
     */
    Double computeOrderTotal(int orderId) throws SQLException;

    /**
     * Requirement 8: Create an order into the database.
     * 
     * @param order Order object to insert.
     * @return true if added successfully, false otherwise.
     * @throws SQLException If database access error occurs.
     */
    boolean addOrder(Order order) throws SQLException;

    /**
     * Requirement 10: Update an order total into the database.
     * Calculates total from line items and persists to Orders table.
     * 
     * @param orderId Order ID.
     * @return true if updated successfully, false otherwise.
     * @throws SQLException If database access error occurs.
     */
    boolean updateOrderTotal(int orderId) throws SQLException;
}
