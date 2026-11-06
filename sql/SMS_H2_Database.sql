-- ============================================================================
-- Database Script for H2 In-Memory / Embedded Testing
-- Assignment: JPL.L.A301_SMS
-- ============================================================================

DROP TABLE IF EXISTS LineItem;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Product;
DROP TABLE IF EXISTS Employee;
DROP TABLE IF EXISTS Customer;

CREATE TABLE Customer (
    customer_id INT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL
);

CREATE TABLE Employee (
    employee_id INT PRIMARY KEY,
    employee_name VARCHAR(100) NOT NULL,
    salary DOUBLE,
    supervisor_id INT,
    FOREIGN KEY (supervisor_id) REFERENCES Employee(employee_id)
);

CREATE TABLE Product (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    list_price DOUBLE
);

CREATE TABLE Orders (
    order_id INT PRIMARY KEY,
    order_date DATE,
    customer_id INT NOT NULL,
    employee_id INT,
    total DOUBLE DEFAULT 0.0,
    FOREIGN KEY (customer_id) REFERENCES Customer(customer_id),
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id)
);

CREATE TABLE LineItem (
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

-- Seed Data
INSERT INTO Customer (customer_id, customer_name) VALUES
(1, 'Nguyen Van A'),
(2, 'Tran Thi B'),
(3, 'Le Van C');

INSERT INTO Employee (employee_id, employee_name, salary, supervisor_id) VALUES
(101, 'Pham Van Truong', 2000.0, NULL),
(102, 'Hoang Van Nam', 1200.0, 101),
(103, 'Do Thi Mai', 1000.0, 101);

INSERT INTO Product (product_id, product_name, list_price) VALUES
(201, 'Laptop Dell XPS', 1500.0),
(202, 'Logitech Wireless Mouse', 25.0),
(203, 'Mechanical Keyboard', 80.0),
(204, 'Dell 27 Inch Monitor', 250.0);

INSERT INTO Orders (order_id, order_date, customer_id, employee_id, total) VALUES
(1001, '2026-01-15', 1, 102, 1550.0),
(1002, '2026-02-20', 1, 103, 80.0),
(1003, '2026-03-10', 2, 102, 275.0);

INSERT INTO LineItem (order_id, product_id, quantity, price) VALUES
(1001, 201, 1, 1500.0),
(1001, 202, 2, 25.0),
(1002, 203, 1, 80.0),
(1003, 202, 1, 25.0),
(1003, 204, 1, 250.0);
