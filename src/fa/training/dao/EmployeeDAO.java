/*
 * EmployeeDAO.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.dao;

import fa.training.entities.Employee;
import java.sql.SQLException;
import java.util.List;

/**
 * Data Access Object interface for Employee entity operations.
 * 
 * @author Fresher Academy
 */
public interface EmployeeDAO {

    /**
     * Retrieves all employees from the database.
     * 
     * @return List of Employee entities.
     * @throws SQLException If database access error occurs.
     */
    List<Employee> getAllEmployees() throws SQLException;

    /**
     * Finds an employee by ID.
     * 
     * @param employeeId Employee ID.
     * @return Employee entity or null if not found.
     * @throws SQLException If database access error occurs.
     */
    Employee getEmployeeById(int employeeId) throws SQLException;

    /**
     * Adds an employee to the database.
     * 
     * @param employee Employee entity to insert.
     * @return true if added successfully, false otherwise.
     * @throws SQLException If database access error occurs.
     */
    boolean addEmployee(Employee employee) throws SQLException;

    /**
     * Updates an employee in the database.
     * 
     * @param employee Employee entity with updated values.
     * @return true if updated successfully, false otherwise.
     * @throws SQLException If database access error occurs.
     */
    boolean updateEmployee(Employee employee) throws SQLException;

    /**
     * Deletes an employee by ID.
     * 
     * @param employeeId Employee ID.
     * @return true if deleted successfully, false otherwise.
     * @throws SQLException If database access error occurs.
     */
    boolean deleteEmployee(int employeeId) throws SQLException;
}
