# JPL.L.A301_SMS - Sales Management System (JDBC)

## 📌 Introduction
Project **JPL.L.A301_SMS** (Long Assignment 3 – Option 1: Building Database Application with JDBC) simulates a Sales Management System (SMS) using Java Standard Edition and Microsoft SQL Server. The project adheres to a 3-tier architecture, complete DAO patterns, stored procedures, and UDFs.

## 🏗️ Architecture & Packages
- **`fa.training.common`**:
  - `Constants`: Centralized definitions for queries, Stored Procedure names, and parameter keys.
  - `PropertyManager`: Singleton loader reading database credentials and URLs from `database.properties`.
  - `ConnectionManager`: Lifecycle manager for JDBC `Connection` and database resources.
- **`fa.training.entities`**:
  - `Customer` (`customerId`, `customerName`)
  - `Employee` (`employeeId`, `employeeName`, `salary`, `spvrId`)
  - `Product` (`productId`, `productName`, `listPrice`)
  - `Order` (`orderId`, `orderDate`, `customerId`, `employeeId`, `total`)
  - `LineItem` (`orderId`, `productId`, `quantity`, `price`)
- **`fa.training.dao`**:
  - Interfaces & implementations for all 5 entities (`CustomerDAO`, `OrderDAO`, `LineItemDAO`, `ProductDAO`, `EmployeeDAO`).
- **`fa.training.main`**:
  - `SaleManagement`: Interactive console user interface testing all 10 requirements.
  - `TestRunner`: Automated test suite asserting correctness of all 10 requirements.

## 📋 Functional Requirements (10/10)
1. `List<Customer> getAllCustomer()`
2. `List<Order> getAllOrdersByCustomerId(int customerId)`
3. `List<LineItem> getAllItemsByOrderId(int orderId)`
4. `Double computeOrderTotal(int orderId)` (via UDF `dbo.fn_ComputeOrderTotal`)
5. `boolean addCustomer(Customer customer)` (via Stored Procedure `dbo.sp_AddCustomer`)
6. `boolean deleteCustomer(int customerId)` (via Stored Procedure `dbo.sp_DeleteCustomer` with cascading delete)
7. `boolean updateCustomer(Customer customer)` (via Stored Procedure `dbo.sp_UpdateCustomer`)
8. `boolean addOrder(Order order)`
9. `boolean addLineItem(LineItem item)`
10. `boolean updateOrderTotal(int orderId)`

## 🗄️ Database Setup
- Run `sql/SMS_Database.sql` in SQL Server Management Studio (SSMS) or Azure Data Studio.
- Verify or update credentials in `database.properties`.

## 🚀 How to Run
### 1. Compile:
```powershell
javac -cp "lib/*;." -d bin (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName })
```

### 2. Run Automated Verification:
```powershell
java -cp "bin;lib/*;." fa.training.main.TestRunner
```

### 3. Run Interactive Console Menu:
```powershell
java -cp "bin;lib/*;." fa.training.main.SaleManagement
```
