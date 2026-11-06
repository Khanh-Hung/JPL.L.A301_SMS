/*
 * Employee.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.entities;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entity class representing an Employee in the Sales Management System.
 * Stores employee identifier, employee name, salary, and supervisor identifier.
 * 
 * @author Fresher Academy
 */
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier of the employee.
     */
    private int employeeId;

    /**
     * Full name of the employee.
     */
    private String employeeName;

    /**
     * Salary amount of the employee.
     */
    private double salary;

    /**
     * Identifier of the employee's supervisor.
     */
    private int spvrId;

    /**
     * Default constructor to allow instantiating empty Employee instances.
     */
    public Employee() {
    }

    /**
     * Full-parameter constructor to initialize all attributes.
     * 
     * @param employeeId   Unique employee ID.
     * @param employeeName Employee full name.
     * @param salary       Salary amount.
     * @param spvrId       Supervisor ID.
     */
    public Employee(int employeeId, String employeeName, double salary, int spvrId) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.salary = salary;
        this.spvrId = spvrId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getSpvrId() {
        return spvrId;
    }

    public void setSpvrId(int spvrId) {
        this.spvrId = spvrId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return employeeId == employee.employeeId &&
                Double.compare(employee.salary, salary) == 0 &&
                spvrId == employee.spvrId &&
                Objects.equals(employeeName, employee.employeeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, employeeName, salary, spvrId);
    }

    @Override
    public String toString() {
        return String.format("Employee [employeeId=%d, employeeName=%s, salary=%.2f, spvrId=%d]",
                employeeId, employeeName, salary, spvrId);
    }
}
