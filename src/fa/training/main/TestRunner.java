/*
 * TestRunner.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.main;

import fa.training.common.ConnectionManager;
import fa.training.dao.CustomerDAO;
import fa.training.dao.CustomerDAOImpl;
import fa.training.dao.EmployeeDAO;
import fa.training.dao.EmployeeDAOImpl;
import fa.training.dao.LineItemDAO;
import fa.training.dao.LineItemDAOImpl;
import fa.training.dao.OrderDAO;
import fa.training.dao.OrderDAOImpl;
import fa.training.dao.ProductDAO;
import fa.training.dao.ProductDAOImpl;
import fa.training.entities.Customer;
import fa.training.entities.Employee;
import fa.training.entities.LineItem;
import fa.training.entities.Order;
import fa.training.entities.Product;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

/**
 * Automated test suite verifying all 10 functional requirements of JPL.L.A301_SMS.
 * Supports running against live database or embedded in-memory test database.
 * 
 * @author Fresher Academy
 */
public class TestRunner {

    // Stored procedure Java methods for H2 compatibility alias
    public static void spAddCustomer(Connection conn, int customerId, String customerName) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("INSERT INTO dbo.Customer (customer_id, customer_name) VALUES (?, ?)");
        ps.setInt(1, customerId);
        ps.setString(2, customerName);
        ps.executeUpdate();
        ps.close();
    }

    public static void spDeleteCustomer(Connection conn, int customerId) throws SQLException {
        PreparedStatement ps1 = conn.prepareStatement(
                "DELETE FROM dbo.LineItem WHERE order_id IN (SELECT order_id FROM dbo.Orders WHERE customer_id = ?)");
        ps1.setInt(1, customerId);
        ps1.executeUpdate();
        ps1.close();

        PreparedStatement ps2 = conn.prepareStatement("DELETE FROM dbo.Orders WHERE customer_id = ?");
        ps2.setInt(1, customerId);
        ps2.executeUpdate();
        ps2.close();

        PreparedStatement ps3 = conn.prepareStatement("DELETE FROM dbo.Customer WHERE customer_id = ?");
        ps3.setInt(1, customerId);
        ps3.executeUpdate();
        ps3.close();
    }

    public static void spUpdateCustomer(Connection conn, int customerId, String customerName) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("UPDATE dbo.Customer SET customer_name = ? WHERE customer_id = ?");
        ps.setString(1, customerName);
        ps.setInt(2, customerId);
        ps.executeUpdate();
        ps.close();
    }

    public static double fnComputeOrderTotal(Connection conn, int orderId) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT COALESCE(SUM(quantity * price), 0.0) AS total_val FROM dbo.LineItem WHERE order_id = ?");
        ps.setInt(1, orderId);
        ResultSet rs = ps.executeQuery();
        double total = 0.0;
        if (rs.next()) {
            total = rs.getDouble("total_val");
        }
        rs.close();
        ps.close();
        return total;
    }

    /**
     * Prepares in-memory schema with schema 'dbo' and aliases for automated standalone testing.
     */
    public static void setupInMemoryDatabase() throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:SMS;DB_CLOSE_DELAY=-1;MODE=MSSQLServer", "sa", "");
        Statement st = conn.createStatement();
        st.execute("CREATE SCHEMA IF NOT EXISTS dbo;");
        st.execute("SET SCHEMA dbo;");

        st.execute("CREATE TABLE IF NOT EXISTS dbo.Customer (customer_id INT PRIMARY KEY, customer_name VARCHAR(100) NOT NULL);");
        st.execute("CREATE TABLE IF NOT EXISTS dbo.Employee (employee_id INT PRIMARY KEY, employee_name VARCHAR(100) NOT NULL, salary DOUBLE, supervisor_id INT);");
        st.execute("CREATE TABLE IF NOT EXISTS dbo.Product (product_id INT PRIMARY KEY, product_name VARCHAR(100) NOT NULL, list_price DOUBLE);");
        st.execute("CREATE TABLE IF NOT EXISTS dbo.Orders (order_id INT PRIMARY KEY, order_date DATE, customer_id INT NOT NULL, employee_id INT, total DOUBLE DEFAULT 0.0);");
        st.execute("CREATE TABLE IF NOT EXISTS dbo.LineItem (order_id INT NOT NULL, product_id INT NOT NULL, quantity INT NOT NULL, price DOUBLE NOT NULL, PRIMARY KEY(order_id, product_id));");

        // Create H2 procedure aliases matching dbo schema
        st.execute("CREATE ALIAS IF NOT EXISTS dbo.sp_AddCustomer FOR \"fa.training.main.TestRunner.spAddCustomer\";");
        st.execute("CREATE ALIAS IF NOT EXISTS dbo.sp_DeleteCustomer FOR \"fa.training.main.TestRunner.spDeleteCustomer\";");
        st.execute("CREATE ALIAS IF NOT EXISTS dbo.sp_UpdateCustomer FOR \"fa.training.main.TestRunner.spUpdateCustomer\";");
        st.execute("CREATE ALIAS IF NOT EXISTS dbo.fn_ComputeOrderTotal FOR \"fa.training.main.TestRunner.fnComputeOrderTotal\";");

        // Insert seed records
        st.execute("DELETE FROM dbo.LineItem;");
        st.execute("DELETE FROM dbo.Orders;");
        st.execute("DELETE FROM dbo.Product;");
        st.execute("DELETE FROM dbo.Employee;");
        st.execute("DELETE FROM dbo.Customer;");

        st.execute("INSERT INTO dbo.Customer (customer_id, customer_name) VALUES (1, 'Nguyen Van A'), (2, 'Tran Thi B'), (3, 'Le Van C');");
        st.execute("INSERT INTO dbo.Employee (employee_id, employee_name, salary, supervisor_id) VALUES (101, 'Pham Van Truong', 2000.0, NULL), (102, 'Hoang Van Nam', 1200.0, 101);");
        st.execute("INSERT INTO dbo.Product (product_id, product_name, list_price) VALUES (201, 'Laptop Dell XPS', 1500.0), (202, 'Logitech Mouse', 25.0);");
        st.execute("INSERT INTO dbo.Orders (order_id, order_date, customer_id, employee_id, total) VALUES (1001, '2026-01-15', 1, 102, 1550.0);");
        st.execute("INSERT INTO dbo.LineItem (order_id, product_id, quantity, price) VALUES (1001, 201, 1, 1500.0), (1001, 202, 2, 25.0);");

        st.close();
        conn.close();
    }

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("     RUNNING AUTOMATED TESTS FOR JPL.L.A301_SMS   ");
        System.out.println("==================================================");

        int passed = 0;
        int total = 10;

        try {
            boolean useH2 = args.length > 0 && "h2".equalsIgnoreCase(args[0]);
            boolean liveDbOk = false;
            
            if (!useH2) {
                try {
                    DriverManager.setLoginTimeout(1);
                    Connection c = ConnectionManager.getConnection();
                    c.close();
                    liveDbOk = true;
                    System.out.println("[INFO] Successfully connected to configured live database.");
                } catch (Exception e) {
                    useH2 = true;
                }
            }

            if (useH2) {
                System.out.println("[INFO] Setting up in-memory H2 database with SMS schema & SP aliases...");
                java.lang.reflect.Field propField = fa.training.common.PropertyManager.class.getDeclaredField("properties");
                propField.setAccessible(true);
                java.util.Properties p = (java.util.Properties) propField.get(fa.training.common.PropertyManager.getInstance());
                p.setProperty("driver", "org.h2.Driver");
                p.setProperty("url", "jdbc:h2:mem:SMS;DB_CLOSE_DELAY=-1;MODE=MSSQLServer");
                p.setProperty("userName", "sa");
                p.setProperty("password", "");

                setupInMemoryDatabase();
                System.out.println("[INFO] In-memory database initialized successfully.");
            }

            CustomerDAO customerDAO = new CustomerDAOImpl();
            OrderDAO orderDAO = new OrderDAOImpl();
            LineItemDAO lineItemDAO = new LineItemDAOImpl();
            ProductDAO productDAO = new ProductDAOImpl();
            EmployeeDAO employeeDAO = new EmployeeDAOImpl();

            // Test 1: getAllCustomer()
            System.out.print("[TEST 1/10] getAllCustomer(): ");
            List<Customer> customers = customerDAO.getAllCustomer();
            if (!customers.isEmpty()) {
                System.out.println("PASSED (Found " + customers.size() + " customers)");
                passed++;
            } else {
                System.out.println("FAILED (Empty customer list)");
            }

            // Test 2: getAllOrdersByCustomerId()
            System.out.print("[TEST 2/10] getAllOrdersByCustomerId(1): ");
            List<Order> orders = orderDAO.getAllOrdersByCustomerId(1);
            if (!orders.isEmpty() && orders.get(0).getCustomerId() == 1) {
                System.out.println("PASSED (Found " + orders.size() + " orders for customer 1)");
                passed++;
            } else {
                System.out.println("FAILED (No orders found for customer 1)");
            }

            // Test 3: getAllItemsByOrderId()
            System.out.print("[TEST 3/10] getAllItemsByOrderId(1001): ");
            List<LineItem> items = lineItemDAO.getAllItemsByOrderId(1001);
            if (!items.isEmpty()) {
                System.out.println("PASSED (Found " + items.size() + " line items)");
                passed++;
            } else {
                System.out.println("FAILED (No line items found for order 1001)");
            }

            // Test 4: computeOrderTotal() using UDF
            System.out.print("[TEST 4/10] computeOrderTotal(1001): ");
            Double computedTotal = orderDAO.computeOrderTotal(1001);
            // 1 * 1500 + 2 * 25 = 1550.0
            if (computedTotal != null && Math.abs(computedTotal - 1550.0) < 0.01) {
                System.out.println("PASSED (Total = " + computedTotal + ")");
                passed++;
            } else {
                System.out.println("FAILED (Expected 1550.0 but got " + computedTotal + ")");
            }

            // Test 5: addCustomer() using Stored Procedure
            System.out.print("[TEST 5/10] addCustomer() via SP: ");
            Customer newCust = new Customer(99, "Test Customer 99");
            boolean addCustOk = customerDAO.addCustomer(newCust);
            Customer fetched = customerDAO.getCustomerById(99);
            if (addCustOk && fetched != null && "Test Customer 99".equals(fetched.getCustomerName())) {
                System.out.println("PASSED (Customer 99 added successfully)");
                passed++;
            } else {
                System.out.println("FAILED (Could not add or retrieve customer)");
            }

            // Test 6: updateCustomer() using Stored Procedure
            System.out.print("[TEST 6/10] updateCustomer() via SP: ");
            newCust.setCustomerName("Updated Customer 99");
            boolean updCustOk = customerDAO.updateCustomer(newCust);
            Customer updated = customerDAO.getCustomerById(99);
            if (updCustOk && updated != null && "Updated Customer 99".equals(updated.getCustomerName())) {
                System.out.println("PASSED (Customer 99 updated successfully)");
                passed++;
            } else {
                System.out.println("FAILED (Customer update failed)");
            }

            // Test 7: deleteCustomer() using Stored Procedure (cascading)
            System.out.print("[TEST 7/10] deleteCustomer() via SP: ");
            boolean delCustOk = customerDAO.deleteCustomer(99);
            Customer afterDel = customerDAO.getCustomerById(99);
            if (delCustOk && afterDel == null) {
                System.out.println("PASSED (Customer 99 deleted successfully)");
                passed++;
            } else {
                System.out.println("FAILED (Customer deletion failed)");
            }

            // Test 8: addOrder()
            System.out.print("[TEST 8/10] addOrder(): ");
            Order newOrder = new Order(2001, new Date(), 2, 102, 0.0);
            boolean addOrderOk = orderDAO.addOrder(newOrder);
            Order fetchedOrder = orderDAO.getOrderById(2001);
            if (addOrderOk && fetchedOrder != null) {
                System.out.println("PASSED (Order 2001 created successfully)");
                passed++;
            } else {
                System.out.println("FAILED (Could not add order)");
            }

            // Test 9: addLineItem()
            System.out.print("[TEST 9/10] addLineItem(): ");
            LineItem newItem1 = new LineItem(2001, 201, 2, 1500.0);
            LineItem newItem2 = new LineItem(2001, 202, 4, 25.0);
            boolean item1Ok = lineItemDAO.addLineItem(newItem1);
            boolean item2Ok = lineItemDAO.addLineItem(newItem2);
            List<LineItem> order2001Items = lineItemDAO.getAllItemsByOrderId(2001);
            if (item1Ok && item2Ok && order2001Items.size() == 2) {
                System.out.println("PASSED (2 LineItems added to order 2001)");
                passed++;
            } else {
                System.out.println("FAILED (LineItems insertion failed)");
            }

            // Test 10: updateOrderTotal()
            System.out.print("[TEST 10/10] updateOrderTotal(2001): ");
            // 2 * 1500 + 4 * 25 = 3100.0
            boolean updTotalOk = orderDAO.updateOrderTotal(2001);
            Order updatedOrder = orderDAO.getOrderById(2001);
            if (updTotalOk && updatedOrder != null && Math.abs(updatedOrder.getTotal() - 3100.0) < 0.01) {
                System.out.println("PASSED (Order total updated to 3100.0)");
                passed++;
            } else {
                System.out.println("FAILED (Expected total 3100.0, got " + (updatedOrder != null ? updatedOrder.getTotal() : "null") + ")");
            }

            // Extra checks on ProductDAO and EmployeeDAO
            System.out.print("[EXTRA] ProductDAO and EmployeeDAO: ");
            List<Product> prodList = productDAO.getAllProducts();
            List<Employee> empList = employeeDAO.getAllEmployees();
            if (!prodList.isEmpty() && !empList.isEmpty()) {
                System.out.println("PASSED (Products: " + prodList.size() + ", Employees: " + empList.size() + ")");
            } else {
                System.out.println("FAILED (Products or employees query failed)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("==================================================");
        System.out.printf("RESULT: %d/%d TESTS PASSED!%n", passed, total);
        System.out.println("==================================================");
    }
}
