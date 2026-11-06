/*
 * LineItemDAOImpl.java
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
import fa.training.entities.LineItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of LineItemDAO using JDBC PreparedStatement.
 * 
 * @author Fresher Academy
 */
public class LineItemDAOImpl implements LineItemDAO {

    /**
     * Requirement 3: List all line items for an order.
     */
    @Override
    public List<LineItem> getAllItemsByOrderId(int orderId) throws SQLException {
        List<LineItem> items = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(Constants.SQL_GET_ALL_ITEMS_BY_ORDER_ID);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int oId = rs.getInt("order_id");
                int pId = rs.getInt("product_id");
                int qty = rs.getInt("quantity");
                double price = rs.getDouble("price");
                items.add(new LineItem(oId, pId, qty, price));
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(rs);
            ConnectionManager.closeResource(pstmt);
            ConnectionManager.closeConnection(conn);
        }

        return items;
    }

    /**
     * Requirement 9: Create a line item into the database.
     */
    @Override
    public boolean addLineItem(LineItem item) throws SQLException {
        if (item == null) {
            return false;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(Constants.SQL_ADD_LINE_ITEM);
            pstmt.setInt(1, item.getOrderId());
            pstmt.setInt(2, item.getProductId());
            pstmt.setInt(3, item.getQuantity());
            pstmt.setDouble(4, item.getPrice());

            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(pstmt);
            ConnectionManager.closeConnection(conn);
        }
    }

    @Override
    public boolean deleteLineItem(int orderId, int productId) throws SQLException {
        String sql = "DELETE FROM dbo.LineItem WHERE order_id = ? AND product_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, productId);

            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(pstmt);
            ConnectionManager.closeConnection(conn);
        }
    }

    @Override
    public Double getTotalPriceByOrderId(int orderId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(quantity * price), 0.0) AS total_price FROM dbo.LineItem WHERE order_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Double total = 0.0;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total_price");
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
}
