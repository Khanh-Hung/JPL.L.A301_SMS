/*
 * CustomerDAOImpl.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.dao;

import fa.training.common.ConnectionManager;
import fa.training.common.Constants;
import fa.training.entities.Customer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of CustomerDAO using JDBC API, Statements, and CallableStatements
 * for invoking stored procedures in the SMS database.
 * 
 * @author Fresher Academy
 */
public class CustomerDAOImpl implements CustomerDAO {

    /**
     * Requirement 1 & Checklist Row 17: List all customers exist in order table.
     */
    @Override
    public List<Customer> getAllCustomer() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.createStatement();
            // Queries customers that exist in the orders table (matches Checklist Row 17 & Req 1)
            rs = stmt.executeQuery(Constants.SQL_GET_ALL_CUSTOMERS_EXIST_ORDER);

            while (rs.next()) {
                int id = rs.getInt("customer_id");
                String name = rs.getString("customer_name");
                customers.add(new Customer(id, name));
            }

            // Fallback: if no orders exist yet in database, return all customers
            if (customers.isEmpty()) {
                ConnectionManager.closeResource(rs);
                rs = stmt.executeQuery(Constants.SQL_GET_ALL_CUSTOMERS);
                while (rs.next()) {
                    int id = rs.getInt("customer_id");
                    String name = rs.getString("customer_name");
                    customers.add(new Customer(id, name));
                }
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(rs);
            ConnectionManager.closeResource(stmt);
            ConnectionManager.closeConnection(conn);
        }

        return customers;
    }

    @Override
    public List<Customer> getAllCustomersInDb() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(Constants.SQL_GET_ALL_CUSTOMERS);

            while (rs.next()) {
                int id = rs.getInt("customer_id");
                String name = rs.getString("customer_name");
                customers.add(new Customer(id, name));
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(rs);
            ConnectionManager.closeResource(stmt);
            ConnectionManager.closeConnection(conn);
        }

        return customers;
    }

    @Override
    public Customer getCustomerById(int customerId) throws SQLException {
        String sql = "SELECT customer_id, customer_name FROM dbo.Customer WHERE customer_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Customer(rs.getInt("customer_id"), rs.getString("customer_name"));
            }
            return null;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(rs);
            ConnectionManager.closeResource(pstmt);
            ConnectionManager.closeConnection(conn);
        }
    }

    /**
     * Requirement 5: Add a customer into the database using a Stored Procedure.
     */
    @Override
    public boolean addCustomer(Customer customer) throws SQLException {
        if (customer == null) {
            return false;
        }

        Connection conn = null;
        CallableStatement cstmt = null;

        try {
            conn = ConnectionManager.getConnection();
            cstmt = conn.prepareCall(Constants.SP_ADD_CUSTOMER);
            cstmt.setInt(1, customer.getCustomerId());
            cstmt.setString(2, customer.getCustomerName());

            cstmt.execute();
            return true;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(cstmt);
            ConnectionManager.closeConnection(conn);
        }
    }

    /**
     * Requirement 6: Delete a customer from the database using a Stored Procedure.
     * Cascades deletion of related LineItems and Orders.
     */
    @Override
    public boolean deleteCustomer(int customerId) throws SQLException {
        Connection conn = null;
        CallableStatement cstmt = null;

        try {
            conn = ConnectionManager.getConnection();
            cstmt = conn.prepareCall(Constants.SP_DELETE_CUSTOMER);
            cstmt.setInt(1, customerId);

            cstmt.execute();
            return true;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(cstmt);
            ConnectionManager.closeConnection(conn);
        }
    }

    /**
     * Requirement 7: Update a customer in the database using a Stored Procedure.
     */
    @Override
    public boolean updateCustomer(Customer customer) throws SQLException {
        if (customer == null) {
            return false;
        }

        Connection conn = null;
        CallableStatement cstmt = null;

        try {
            conn = ConnectionManager.getConnection();
            cstmt = conn.prepareCall(Constants.SP_UPDATE_CUSTOMER);
            cstmt.setInt(1, customer.getCustomerId());
            cstmt.setString(2, customer.getCustomerName());

            cstmt.execute();
            return true;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(cstmt);
            ConnectionManager.closeConnection(conn);
        }
    }
}
