/*
 * OrderDAOImpl.java
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
import fa.training.entities.Order;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of OrderDAO utilizing JDBC PreparedStatements, UDF invocation,
 * and database operations.
 * 
 * @author Fresher Academy
 */
public class OrderDAOImpl implements OrderDAO {

    @Override
    public List<Order> getAllOrders() throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT order_id, order_date, customer_id, employee_id, total FROM dbo.Orders";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(rs);
            ConnectionManager.closeResource(stmt);
            ConnectionManager.closeConnection(conn);
        }

        return orders;
    }

    /**
     * Requirement 2: List all orders consist of order id, order date, customer id,
     * employee id, total for a given customer.
     */
    @Override
    public List<Order> getAllOrdersByCustomerId(int customerId) throws SQLException {
        List<Order> orders = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(Constants.SQL_GET_ALL_ORDERS_BY_CUSTOMER_ID);
            pstmt.setInt(1, customerId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                orders.add(extractOrderFromResultSet(rs));
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(rs);
            ConnectionManager.closeResource(pstmt);
            ConnectionManager.closeConnection(conn);
        }

        return orders;
    }

    @Override
    public Order getOrderById(int orderId) throws SQLException {
        String sql = "SELECT order_id, order_date, customer_id, employee_id, total FROM dbo.Orders WHERE order_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractOrderFromResultSet(rs);
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
     * Requirement 4: Compute order total using User Defined Function (UDF).
     * Returns a list with a row containing the computed order total from line items (named total_price).
     */
    @Override
    public Double computeOrderTotal(int orderId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Double total = 0.0;

        try {
            conn = ConnectionManager.getConnection();
            // Try calling UDF fn_ComputeOrderTotal first
            try {
                pstmt = conn.prepareStatement(Constants.SQL_SELECT_UDF_TOTAL);
                pstmt.setInt(1, orderId);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    total = rs.getDouble("total_price");
                    return total;
                }
            } catch (SQLException udfEx) {
                // If direct UDF query fails, attempt callable statement or fallback calculation
                ConnectionManager.closeResource(rs);
                ConnectionManager.closeResource(pstmt);

                CallableStatement cstmt = null;
                try {
                    cstmt = conn.prepareCall(Constants.FN_COMPUTE_ORDER_TOTAL);
                    cstmt.registerOutParameter(1, Types.DOUBLE);
                    cstmt.setInt(2, orderId);
                    cstmt.execute();
                    total = cstmt.getDouble(1);
                    return total;
                } catch (SQLException csEx) {
                    // Fallback to direct calculation from line items
                    String fallbackSql = "SELECT COALESCE(SUM(quantity * price), 0.0) AS total_price FROM dbo.LineItem WHERE order_id = ?";
                    pstmt = conn.prepareStatement(fallbackSql);
                    pstmt.setInt(1, orderId);
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        total = rs.getDouble("total_price");
                        return total;
                    }
                } finally {
                    ConnectionManager.closeResource(cstmt);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(rs);
            ConnectionManager.closeResource(pstmt);
            ConnectionManager.closeConnection(conn);
        }

        return total;
    }

    /**
     * Requirement 8: Create an order into the database.
     */
    @Override
    public boolean addOrder(Order order) throws SQLException {
        if (order == null) {
            return false;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(Constants.SQL_ADD_ORDER);
            pstmt.setInt(1, order.getOrderId());
            if (order.getOrderDate() != null) {
                pstmt.setDate(2, new Date(order.getOrderDate().getTime()));
            } else {
                pstmt.setDate(2, new Date(System.currentTimeMillis()));
            }
            pstmt.setInt(3, order.getCustomerId());
            pstmt.setInt(4, order.getEmployeeId());
            pstmt.setDouble(5, order.getTotal());

            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(pstmt);
            ConnectionManager.closeConnection(conn);
        }
    }

    /**
     * Requirement 10: Update an order total into the database.
     */
    @Override
    public boolean updateOrderTotal(int orderId) throws SQLException {
        Double total = computeOrderTotal(orderId);
        if (total == null) {
            total = 0.0;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(Constants.SQL_UPDATE_ORDER_TOTAL);
            pstmt.setDouble(1, total);
            pstmt.setInt(2, orderId);

            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(pstmt);
            ConnectionManager.closeConnection(conn);
        }
    }

    private Order extractOrderFromResultSet(ResultSet rs) throws SQLException {
        int orderId = rs.getInt("order_id");
        Date orderDate = rs.getDate("order_date");
        int customerId = rs.getInt("customer_id");
        int employeeId = rs.getInt("employee_id");
        double total = rs.getDouble("total");
        return new Order(orderId, orderDate, customerId, employeeId, total);
    }
}
