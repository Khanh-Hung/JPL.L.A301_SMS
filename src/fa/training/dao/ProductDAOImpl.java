/*
 * ProductDAOImpl.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.dao;

import fa.training.common.ConnectionManager;
import fa.training.entities.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of ProductDAO using standard JDBC PreparedStatements.
 * 
 * @author Fresher Academy
 */
public class ProductDAOImpl implements ProductDAO {

    @Override
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT product_id, product_name, list_price FROM dbo.Product";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("product_name");
                double price = rs.getDouble("list_price");
                products.add(new Product(id, name, price));
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(rs);
            ConnectionManager.closeResource(stmt);
            ConnectionManager.closeConnection(conn);
        }

        return products;
    }

    @Override
    public Product getProductById(int productId) throws SQLException {
        String sql = "SELECT product_id, product_name, list_price FROM dbo.Product WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("product_name");
                double price = rs.getDouble("list_price");
                return new Product(id, name, price);
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

    @Override
    public boolean addProduct(Product product) throws SQLException {
        if (product == null) {
            return false;
        }

        String sql = "INSERT INTO dbo.Product (product_id, product_name, list_price) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, product.getProductId());
            pstmt.setString(2, product.getProductName());
            pstmt.setDouble(3, product.getListPrice());

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
    public boolean updateProduct(Product product) throws SQLException {
        if (product == null) {
            return false;
        }

        String sql = "UPDATE dbo.Product SET product_name = ?, list_price = ? WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getProductName());
            pstmt.setDouble(2, product.getListPrice());
            pstmt.setInt(3, product.getProductId());

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
    public boolean deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM dbo.Product WHERE product_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, productId);

            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(pstmt);
            ConnectionManager.closeConnection(conn);
        }
    }
}
