/*
 * Constants.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.common;

/**
 * Global constant definitions used across the SMS application.
 * Centralizes SQL queries, stored procedure calls, and configuration keys
 * to eliminate magic strings and ease maintenance.
 * 
 * @author Fresher Academy
 */
public final class Constants {

    private Constants() {
        // Prevent instantiation
    }

    public static final String CONFIG_FILE_NAME = "database.properties";

    public static final String KEY_DRIVER = "driver";
    public static final String KEY_URL = "url";
    public static final String KEY_USERNAME = "userName";
    public static final String KEY_PASSWORD = "password";

    // Stored Procedures
    public static final String SP_ADD_CUSTOMER = "{call dbo.sp_AddCustomer(?, ?)}";
    public static final String SP_DELETE_CUSTOMER = "{call dbo.sp_DeleteCustomer(?)}";
    public static final String SP_UPDATE_CUSTOMER = "{call dbo.sp_UpdateCustomer(?, ?)}";

    // User Defined Function (UDF)
    public static final String FN_COMPUTE_ORDER_TOTAL = "{? = call dbo.fn_ComputeOrderTotal(?)}";
    public static final String SQL_SELECT_UDF_TOTAL = "SELECT dbo.fn_ComputeOrderTotal(?) AS total_price";

    // SQL Statements
    public static final String SQL_GET_ALL_CUSTOMERS = "SELECT customer_id, customer_name FROM dbo.Customer";
    public static final String SQL_GET_ALL_ORDERS_BY_CUSTOMER_ID = 
            "SELECT order_id, order_date, customer_id, employee_id, total FROM dbo.Orders WHERE customer_id = ?";
    public static final String SQL_GET_ALL_ITEMS_BY_ORDER_ID = 
            "SELECT order_id, product_id, quantity, price FROM dbo.LineItem WHERE order_id = ?";
    public static final String SQL_ADD_ORDER = 
            "INSERT INTO dbo.Orders (order_id, order_date, customer_id, employee_id, total) VALUES (?, ?, ?, ?, ?)";
    public static final String SQL_ADD_LINE_ITEM = 
            "INSERT INTO dbo.LineItem (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
    public static final String SQL_UPDATE_ORDER_TOTAL = 
            "UPDATE dbo.Orders SET total = ? WHERE order_id = ?";
}
