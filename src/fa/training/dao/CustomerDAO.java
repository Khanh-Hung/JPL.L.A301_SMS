/*
 * CustomerDAO.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.dao;

import fa.training.entities.Customer;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Customer entity operations.
 * Defines persistence contracts to manage Customer data in database.
 * 
 * @author Fresher Academy
 */
public interface CustomerDAO {

    /**
     * Requirement 1: List all customers consist of customer_id, customer_name in the database.
     * 
     * @return List of Customer entities.
     * @throws SQLException If database access error occurs.
     */
    List<Customer> getAllCustomer() throws SQLException;

    /**
     * Finds a customer by customer ID.
     * 
     * @param customerId Customer ID.
     * @return Customer entity or null if not found.
     * @throws SQLException If database access error occurs.
     */
    Customer getCustomerById(int customerId) throws SQLException;

    /**
     * Requirement 5: Add a customer into the database using a Stored Procedure.
     * 
     * @param customer Customer entity to insert.
     * @return true if added successfully, false otherwise.
     * @throws SQLException If database access error occurs.
     */
    boolean addCustomer(Customer customer) throws SQLException;

    /**
     * Requirement 6: Delete a customer from the database using a Stored Procedure,
     * ensuring cascade deletion of related orders and ordered products.
     * 
     * @param customerId ID of customer to delete.
     * @return true if deleted successfully, false otherwise.
     * @throws SQLException If database access error occurs.
     */
    boolean deleteCustomer(int customerId) throws SQLException;

    /**
     * Requirement 7: Update a customer in the database using a Stored Procedure.
     * 
     * @param customer Customer entity with updated details.
     * @return true if updated successfully, false otherwise.
     * @throws SQLException If database access error occurs.
     */
    boolean updateCustomer(Customer customer) throws SQLException;
}
