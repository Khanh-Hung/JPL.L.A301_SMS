/*
 * EmployeeDAOImpl.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.dao;

import fa.training.common.ConnectionManager;
import fa.training.entities.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of EmployeeDAO using JDBC PreparedStatement.
 * 
 * @author Fresher Academy
 */
public class EmployeeDAOImpl implements EmployeeDAO {

    @Override
    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT employee_id, employee_name, salary, supervisor_id FROM dbo.Employee";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                list.add(extractEmployee(rs));
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(rs);
            ConnectionManager.closeResource(stmt);
            ConnectionManager.closeConnection(conn);
        }

        return list;
    }

    @Override
    public Employee getEmployeeById(int employeeId) throws SQLException {
        String sql = "SELECT employee_id, employee_name, salary, supervisor_id FROM dbo.Employee WHERE employee_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractEmployee(rs);
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
    public boolean addEmployee(Employee employee) throws SQLException {
        if (employee == null) {
            return false;
        }

        String sql = "INSERT INTO dbo.Employee (employee_id, employee_name, salary, supervisor_id) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employee.getEmployeeId());
            pstmt.setString(2, employee.getEmployeeName());
            pstmt.setDouble(3, employee.getSalary());
            if (employee.getSpvrId() > 0) {
                pstmt.setInt(4, employee.getSpvrId());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }

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
    public boolean updateEmployee(Employee employee) throws SQLException {
        if (employee == null) {
            return false;
        }

        String sql = "UPDATE dbo.Employee SET employee_name = ?, salary = ?, supervisor_id = ? WHERE employee_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, employee.getEmployeeName());
            pstmt.setDouble(2, employee.getSalary());
            if (employee.getSpvrId() > 0) {
                pstmt.setInt(3, employee.getSpvrId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setInt(4, employee.getEmployeeId());

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
    public boolean deleteEmployee(int employeeId) throws SQLException {
        String sql = "DELETE FROM dbo.Employee WHERE employee_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConnectionManager.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);

            int affected = pstmt.executeUpdate();
            return affected > 0;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found: " + e.getMessage(), e);
        } finally {
            ConnectionManager.closeResource(pstmt);
            ConnectionManager.closeConnection(conn);
        }
    }

    private Employee extractEmployee(ResultSet rs) throws SQLException {
        int id = rs.getInt("employee_id");
        String name = rs.getString("employee_name");
        double salary = rs.getDouble("salary");
        int spvrId = rs.getInt("supervisor_id");
        return new Employee(id, name, salary, spvrId);
    }
}
