-- ============================================================================
-- Database: SMS (Sales Management System)
-- Schema: dbo
-- Assignment: JPL.L.A301_SMS
-- ============================================================================

USE master;
GO

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'SMS')
BEGIN
    CREATE DATABASE SMS;
END
GO

USE SMS;
GO

-- Drop tables if exists (in reverse dependency order)
IF OBJECT_ID('dbo.LineItem', 'U') IS NOT NULL DROP TABLE dbo.LineItem;
IF OBJECT_ID('dbo.Orders', 'U') IS NOT NULL DROP TABLE dbo.Orders;
IF OBJECT_ID('dbo.Product', 'U') IS NOT NULL DROP TABLE dbo.Product;
IF OBJECT_ID('dbo.Employee', 'U') IS NOT NULL DROP TABLE dbo.Employee;
IF OBJECT_ID('dbo.Customer', 'U') IS NOT NULL DROP TABLE dbo.Customer;
GO

-- Drop procedures and functions if exists
IF OBJECT_ID('dbo.sp_AddCustomer', 'P') IS NOT NULL DROP PROCEDURE dbo.sp_AddCustomer;
IF OBJECT_ID('dbo.sp_DeleteCustomer', 'P') IS NOT NULL DROP PROCEDURE dbo.sp_DeleteCustomer;
IF OBJECT_ID('dbo.sp_UpdateCustomer', 'P') IS NOT NULL DROP PROCEDURE dbo.sp_UpdateCustomer;
IF OBJECT_ID('dbo.fn_ComputeOrderTotal', 'FN') IS NOT NULL DROP FUNCTION dbo.fn_ComputeOrderTotal;
GO

-- 1. Create Customer Table
CREATE TABLE dbo.Customer (
    customer_id INT PRIMARY KEY,
    customer_name NVARCHAR(100) NOT NULL
);
GO

-- 2. Create Employee Table
CREATE TABLE dbo.Employee (
    employee_id INT PRIMARY KEY,
    employee_name NVARCHAR(100) NOT NULL,
    salary FLOAT,
    supervisor_id INT,
    CONSTRAINT FK_Employee_Supervisor FOREIGN KEY (supervisor_id) REFERENCES dbo.Employee(employee_id)
);
GO

-- 3. Create Product Table
CREATE TABLE dbo.Product (
    product_id INT PRIMARY KEY,
    product_name NVARCHAR(100) NOT NULL,
    list_price FLOAT
);
GO

-- 4. Create Orders Table
CREATE TABLE dbo.Orders (
    order_id INT PRIMARY KEY,
    order_date DATE,
    customer_id INT NOT NULL,
    employee_id INT,
    total FLOAT DEFAULT 0,
    CONSTRAINT FK_Orders_Customer FOREIGN KEY (customer_id) REFERENCES dbo.Customer(customer_id),
    CONSTRAINT FK_Orders_Employee FOREIGN KEY (employee_id) REFERENCES dbo.Employee(employee_id)
);
GO

-- 5. Create LineItem Table
CREATE TABLE dbo.LineItem (
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price FLOAT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT FK_LineItem_Orders FOREIGN KEY (order_id) REFERENCES dbo.Orders(order_id),
    CONSTRAINT FK_LineItem_Product FOREIGN KEY (product_id) REFERENCES dbo.Product(product_id)
);
GO

-- ============================================================================
-- STORED PROCEDURES
-- ============================================================================

-- Stored Procedure: sp_AddCustomer
CREATE PROCEDURE dbo.sp_AddCustomer
    @customerId INT,
    @customerName NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;
    INSERT INTO dbo.Customer (customer_id, customer_name)
    VALUES (@customerId, @customerName);
END;
GO

-- Stored Procedure: sp_DeleteCustomer
-- Cascades deletion of LineItem and Orders for the deleted customer
CREATE PROCEDURE dbo.sp_DeleteCustomer
    @customerId INT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRANSACTION;
    BEGIN TRY
        -- Delete LineItems associated with any orders belonging to this customer
        DELETE FROM dbo.LineItem
        WHERE order_id IN (SELECT order_id FROM dbo.Orders WHERE customer_id = @customerId);

        -- Delete Orders belonging to this customer
        DELETE FROM dbo.Orders
        WHERE customer_id = @customerId;

        -- Delete Customer
        DELETE FROM dbo.Customer
        WHERE customer_id = @customerId;

        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        ROLLBACK TRANSACTION;
        THROW;
    END CATCH
END;
GO

-- Stored Procedure: sp_UpdateCustomer
CREATE PROCEDURE dbo.sp_UpdateCustomer
    @customerId INT,
    @customerName NVARCHAR(100)
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE dbo.Customer
    SET customer_name = @customerName
    WHERE customer_id = @customerId;
END;
GO

-- ============================================================================
-- USER DEFINED FUNCTION: fn_ComputeOrderTotal
-- ============================================================================
CREATE FUNCTION dbo.fn_ComputeOrderTotal (@orderId INT)
RETURNS FLOAT
AS
BEGIN
    DECLARE @total FLOAT;
    SELECT @total = ISNULL(SUM(quantity * price), 0.0)
    FROM dbo.LineItem
    WHERE order_id = @orderId;
    RETURN @total;
END;
GO

-- ============================================================================
-- SAMPLE SEED DATA
-- ============================================================================
INSERT INTO dbo.Customer (customer_id, customer_name) VALUES
(1, N'Nguyen Van A'),
(2, N'Tran Thi B'),
(3, N'Le Van C');

INSERT INTO dbo.Employee (employee_id, employee_name, salary, supervisor_id) VALUES
(101, N'Pham Van Truong', 2000.0, NULL),
(102, N'Hoang Van Nam', 1200.0, 101),
(103, N'Do Thi Mai', 1000.0, 101);

INSERT INTO dbo.Product (product_id, product_name, list_price) VALUES
(201, N'Laptop Dell XPS', 1500.0),
(202, N'Logitech Wireless Mouse', 25.0),
(203, N'Mechanical Keyboard', 80.0),
(204, N'Dell 27 Inch Monitor', 250.0);

INSERT INTO dbo.Orders (order_id, order_date, customer_id, employee_id, total) VALUES
(1001, '2026-01-15', 1, 102, 1550.0),
(1002, '2026-02-20', 1, 103, 80.0),
(1003, '2026-03-10', 2, 102, 275.0);

INSERT INTO dbo.LineItem (order_id, product_id, quantity, price) VALUES
(1001, 201, 1, 1500.0),
(1001, 202, 2, 25.0),
(1002, 203, 1, 80.0),
(1003, 202, 1, 25.0),
(1003, 204, 1, 250.0);
GO
