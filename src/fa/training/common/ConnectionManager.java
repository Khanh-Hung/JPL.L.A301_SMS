/*
 * ConnectionManager.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages database connection lifecycle.
 * Loads JDBC driver dynamically and establishes connection based on
 * properties provided by PropertyManager.
 * 
 * @author Fresher Academy
 */
public class ConnectionManager {

    private static boolean driverLoaded = false;

    private ConnectionManager() {
        // Prevent instantiation
    }

    /**
     * Obtains a new JDBC Connection to the database.
     * 
     * @return Active Connection instance.
     * @throws SQLException If database access fails.
     * @throws ClassNotFoundException If JDBC driver class is missing.
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        PropertyManager pm = PropertyManager.getInstance();
        String driver = pm.getDriver();
        String url = pm.getUrl();
        String user = pm.getUserName();
        String password = pm.getPassword();

        if (!driverLoaded) {
            Class.forName(driver);
            driverLoaded = true;
        }

        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Safely closes a database connection.
     * 
     * @param connection Connection to close.
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Safely closes any AutoCloseable database resource (Statement, ResultSet, etc.).
     * 
     * @param resource Resource to close.
     */
    public static void closeResource(AutoCloseable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (Exception e) {
                System.err.println("Error closing resource: " + e.getMessage());
            }
        }
    }
}
