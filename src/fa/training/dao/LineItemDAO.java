/*
 * LineItemDAO.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.dao;

import fa.training.entities.LineItem;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for LineItem entity operations.
 * Defines persistence operations for individual order items.
 * 
 * @author Fresher Academy
 */
public interface LineItemDAO {

    /**
     * Requirement 3: List all line items for an order.
     * 
     * @param orderId Order ID.
     * @return List of LineItem instances belonging to the specified order.
     * @throws SQLException If database access error occurs.
     */
    List<LineItem> getAllItemsByOrderId(int orderId) throws SQLException;

    /**
     * Requirement 9: Create a line item into the database.
     * 
     * @param item LineItem to insert.
     * @return true if added successfully, false otherwise.
     * @throws SQLException If database access error occurs.
     */
    boolean addLineItem(LineItem item) throws SQLException;

    /**
     * Deletes a specific line item in an order.
     * 
     * @param orderId   Order ID.
     * @param productId Product ID.
     * @return true if deleted successfully, false otherwise.
     * @throws SQLException If database access error occurs.
     */
    boolean deleteLineItem(int orderId, int productId) throws SQLException;
}
