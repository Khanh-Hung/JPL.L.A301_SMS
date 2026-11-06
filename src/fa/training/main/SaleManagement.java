/*
 * SaleManagement.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.main;

import fa.training.dao.CustomerDAO;
import fa.training.dao.CustomerDAOImpl;
import fa.training.dao.LineItemDAO;
import fa.training.dao.LineItemDAOImpl;
import fa.training.dao.OrderDAO;
import fa.training.dao.OrderDAOImpl;
import fa.training.entities.Customer;
import fa.training.entities.LineItem;
import fa.training.entities.Order;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Main application console user interface for Sales Management System (SMS).
 * Implements interactive menu allowing users to execute and test all 10
 * functional requirements.
 * 
 * @author Fresher Academy
 */
public class SaleManagement {

    private static final CustomerDAO customerDAO = new CustomerDAOImpl();
    private static final OrderDAO orderDAO = new OrderDAOImpl();
    private static final LineItemDAO lineItemDAO = new LineItemDAOImpl();
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        int choice = -1;
        do {
            printMenu();
            choice = readInt("Enter your choice (1-11): ");
            try {
                switch (choice) {
                    case 1:
                        handleGetAllCustomers();
                        break;
                    case 2:
                        handleGetAllOrdersByCustomerId();
                        break;
                    case 3:
                        handleGetAllItemsByOrderId();
                        break;
                    case 4:
                        handleComputeOrderTotal();
                        break;
                    case 5:
                        handleAddCustomer();
                        break;
                    case 6:
                        handleDeleteCustomer();
                        break;
                    case 7:
                        handleUpdateCustomer();
                        break;
                    case 8:
                        handleAddOrder();
                        break;
                    case 9:
                        handleAddLineItem();
                        break;
                    case 10:
                        handleUpdateOrderTotal();
                        break;
                    case 11:
                        System.out.println("Thank you for using SMS. Exiting...");
                        break;
                    default:
                        System.out.println("Invalid selection! Please enter a number between 1 and 11.");
                }
            } catch (SQLException e) {
                System.err.println("Database operation failed: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }
            System.out.println();
        } while (choice != 11);
    }

    private static void printMenu() {
        System.out.println("==================================================");
        System.out.println("         SALES MANAGEMENT SYSTEM (SMS)");
        System.out.println("==================================================");
        System.out.println("1.  List all customers (getAllCustomer)");
        System.out.println("2.  List all orders for a customer (getAllOrdersByCustomerId)");
        System.out.println("3.  List all line items for an order (getAllItemsByOrderId)");
        System.out.println("4.  Compute order total via UDF (computeOrderTotal)");
        System.out.println("5.  Add a customer via Stored Procedure (addCustomer)");
        System.out.println("6.  Delete a customer via Stored Procedure (deleteCustomer)");
        System.out.println("7.  Update a customer via Stored Procedure (updateCustomer)");
        System.out.println("8.  Create an order (addOrder)");
        System.out.println("9.  Create a line item (addLineItem)");
        System.out.println("10. Update an order total into database (updateOrderTotal)");
        System.out.println("11. Exit");
        System.out.println("==================================================");
    }

    /**
     * Requirement 1: List all customers
     */
    private static void handleGetAllCustomers() throws SQLException {
        System.out.println("--- [1] ALL CUSTOMERS ---");
        List<Customer> list = customerDAO.getAllCustomer();
        if (list.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }
        System.out.printf("%-15s %-30s%n", "Customer ID", "Customer Name");
        System.out.println("----------------------------------------------");
        for (Customer c : list) {
            System.out.printf("%-15d %-30s%n", c.getCustomerId(), c.getCustomerName());
        }
    }

    /**
     * Requirement 2: List all orders for a customer
     */
    private static void handleGetAllOrdersByCustomerId() throws SQLException {
        System.out.println("--- [2] ALL ORDERS BY CUSTOMER ID ---");
        int customerId = readInt("Enter Customer ID: ");
        List<Order> list = orderDAO.getAllOrdersByCustomerId(customerId);
        if (list.isEmpty()) {
            System.out.println("No orders found for customer ID: " + customerId);
            return;
        }
        System.out.printf("%-10s %-15s %-15s %-15s %-10s%n",
                "Order ID", "Order Date", "Customer ID", "Employee ID", "Total");
        System.out.println("-----------------------------------------------------------------");
        for (Order o : list) {
            String dateStr = o.getOrderDate() != null ? dateFormat.format(o.getOrderDate()) : "N/A";
            System.out.printf("%-10d %-15s %-15d %-15d %-10.2f%n",
                    o.getOrderId(), dateStr, o.getCustomerId(), o.getEmployeeId(), o.getTotal());
        }
    }

    /**
     * Requirement 3: List all line items for an order
     */
    private static void handleGetAllItemsByOrderId() throws SQLException {
        System.out.println("--- [3] ALL LINE ITEMS BY ORDER ID ---");
        int orderId = readInt("Enter Order ID: ");
        List<LineItem> list = lineItemDAO.getAllItemsByOrderId(orderId);
        if (list.isEmpty()) {
            System.out.println("No line items found for order ID: " + orderId);
            return;
        }
        System.out.printf("%-10s %-12s %-10s %-12s%n", "Order ID", "Product ID", "Quantity", "Price");
        System.out.println("------------------------------------------------");
        for (LineItem item : list) {
            System.out.printf("%-10d %-12d %-10d %-12.2f%n",
                    item.getOrderId(), item.getProductId(), item.getQuantity(), item.getPrice());
        }
    }

    /**
     * Requirement 4: Compute order total using UDF
     */
    private static void handleComputeOrderTotal() throws SQLException {
        System.out.println("--- [4] COMPUTE ORDER TOTAL (UDF) ---");
        int orderId = readInt("Enter Order ID: ");
        Double total = orderDAO.computeOrderTotal(orderId);
        System.out.printf("Computed order total (total_price) for order #%d is: %.2f%n", orderId, total);
    }

    /**
     * Requirement 5: Add customer using Stored Procedure
     */
    private static void handleAddCustomer() throws SQLException {
        System.out.println("--- [5] ADD CUSTOMER (STORED PROCEDURE) ---");
        int id = readInt("Enter new Customer ID: ");
        String name = readString("Enter Customer Name: ");
        Customer c = new Customer(id, name);
        boolean success = customerDAO.addCustomer(c);
        if (success) {
            System.out.println("Customer added successfully via Stored Procedure!");
        } else {
            System.out.println("Failed to add customer.");
        }
    }

    /**
     * Requirement 6: Delete customer using Stored Procedure
     */
    private static void handleDeleteCustomer() throws SQLException {
        System.out.println("--- [6] DELETE CUSTOMER (STORED PROCEDURE) ---");
        int id = readInt("Enter Customer ID to delete: ");
        boolean success = customerDAO.deleteCustomer(id);
        if (success) {
            System.out.println("Customer and associated orders/line items deleted successfully via Stored Procedure!");
        } else {
            System.out.println("Customer ID not found or deletion failed.");
        }
    }

    /**
     * Requirement 7: Update customer using Stored Procedure
     */
    private static void handleUpdateCustomer() throws SQLException {
        System.out.println("--- [7] UPDATE CUSTOMER (STORED PROCEDURE) ---");
        int id = readInt("Enter existing Customer ID: ");
        String name = readString("Enter new Customer Name: ");
        Customer c = new Customer(id, name);
        boolean success = customerDAO.updateCustomer(c);
        if (success) {
            System.out.println("Customer updated successfully via Stored Procedure!");
        } else {
            System.out.println("Customer ID not found or update failed.");
        }
    }

    /**
     * Requirement 8: Add Order
     */
    private static void handleAddOrder() throws SQLException {
        System.out.println("--- [8] CREATE ORDER ---");
        int orderId = readInt("Enter Order ID: ");
        String dateInput = readString("Enter Order Date (yyyy-MM-dd, empty for today): ");
        Date orderDate = new Date();
        if (!dateInput.trim().isEmpty()) {
            try {
                orderDate = dateFormat.parse(dateInput.trim());
            } catch (ParseException e) {
                System.out.println("Invalid date format, using today's date.");
            }
        }
        int customerId = readInt("Enter Customer ID: ");
        int employeeId = readInt("Enter Employee ID: ");
        double total = readDouble("Enter initial Total (or 0.0): ");

        Order order = new Order(orderId, orderDate, customerId, employeeId, total);
        boolean success = orderDAO.addOrder(order);
        if (success) {
            System.out.println("Order created successfully!");
        } else {
            System.out.println("Failed to create order.");
        }
    }

    /**
     * Requirement 9: Add LineItem
     */
    private static void handleAddLineItem() throws SQLException {
        System.out.println("--- [9] CREATE LINE ITEM ---");
        int orderId = readInt("Enter Order ID: ");
        int productId = readInt("Enter Product ID: ");
        int quantity = readInt("Enter Quantity: ");
        double price = readDouble("Enter Price: ");

        LineItem item = new LineItem(orderId, productId, quantity, price);
        boolean success = lineItemDAO.addLineItem(item);
        if (success) {
            System.out.println("Line item added successfully!");
        } else {
            System.out.println("Failed to add line item.");
        }
    }

    /**
     * Requirement 10: Update Order Total
     */
    private static void handleUpdateOrderTotal() throws SQLException {
        System.out.println("--- [10] UPDATE ORDER TOTAL ---");
        int orderId = readInt("Enter Order ID: ");
        boolean success = orderDAO.updateOrderTotal(orderId);
        if (success) {
            Double updatedTotal = orderDAO.computeOrderTotal(orderId);
            System.out.printf("Order #%d total successfully updated to: %.2f%n", orderId, updatedTotal);
        } else {
            System.out.println("Failed to update order total. Please ensure Order ID exists.");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return 11; // Exit gracefully on stream end
            }
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) {
                return 0.0;
            }
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid decimal number.");
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return "";
        }
        return scanner.nextLine().trim();
    }
}
