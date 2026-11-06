/*
 * ProductDAO.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.dao;

import fa.training.entities.Product;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Product entity operations.
 * 
 * @author Fresher Academy
 */
public interface ProductDAO {

    /**
     * Retrieves all products from the database.
     * 
     * @return List of Product entities.
     * @throws SQLException If database access error occurs.
     */
    List<Product> getAllProducts() throws SQLException;

    /**
     * Finds a product by its unique product ID.
     * 
     * @param productId Product ID.
     * @return Product object or null if not found.
     * @throws SQLException If database access error occurs.
     */
    Product getProductById(int productId) throws SQLException;

    /**
     * Adds a new product to the database.
     * 
     * @param product Product entity to insert.
     * @return true if added successfully, false otherwise.
     * @throws SQLException If database access error occurs.
     */
    boolean addProduct(Product product) throws SQLException;

    /**
     * Updates an existing product's information.
     * 
     * @param product Product entity with updated details.
     * @return true if updated successfully, false otherwise.
     * @throws SQLException If database access error occurs.
     */
    boolean updateProduct(Product product) throws SQLException;

    /**
     * Deletes a product by product ID.
     * 
     * @param productId Product ID.
     * @return true if deleted successfully, false otherwise.
     * @throws SQLException If database access error occurs.
     */
    boolean deleteProduct(int productId) throws SQLException;
}
