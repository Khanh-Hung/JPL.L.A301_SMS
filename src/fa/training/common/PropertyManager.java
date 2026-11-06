/*
 * PropertyManager.java
 *
 * Version: 1.0
 *
 * Date: 06-11-2026
 *
 * Copyright (c) FPT Software. All rights reserved.
 */
package fa.training.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Singleton / Utility class responsible for loading and retrieving configuration
 * properties from database.properties.
 * 
 * @author Fresher Academy
 */
public class PropertyManager {

    private static PropertyManager instance;
    private final Properties properties = new Properties();

    /**
     * Private constructor to enforce Singleton pattern.
     * Searches for database.properties in local file system first, then falls back to classpath.
     */
    private PropertyManager() {
        loadProperties();
    }

    /**
     * Retrieves the singleton instance of PropertyManager.
     * 
     * @return Single instance of PropertyManager.
     */
    public static synchronized PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    /**
     * Loads database configuration properties from file or classpath.
     */
    public void loadProperties() {
        InputStream input = null;
        try {
            File propFile = new File(Constants.CONFIG_FILE_NAME);
            if (propFile.exists()) {
                input = new FileInputStream(propFile);
            } else {
                input = getClass().getClassLoader().getResourceAsStream(Constants.CONFIG_FILE_NAME);
            }

            if (input != null) {
                properties.load(input);
            } else {
                System.err.println("Warning: " + Constants.CONFIG_FILE_NAME + " not found, using default configurations.");
            }
        } catch (IOException e) {
            System.err.println("Error reading " + Constants.CONFIG_FILE_NAME + ": " + e.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.err.println("Failed to close input stream: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Retrieves property value by key with optional fallback.
     * 
     * @param key Property key.
     * @return Property value, or null if not found.
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Retrieves property value with a default fallback if missing.
     * 
     * @param key          Property key.
     * @param defaultValue Default value if key is not present.
     * @return Property value or default.
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public String getDriver() {
        return getProperty(Constants.KEY_DRIVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
    }

    public String getUrl() {
        return getProperty(Constants.KEY_URL, "jdbc:sqlserver://localhost:1433;databaseName=SMS;encrypt=false;trustServerCertificate=true");
    }

    public String getUserName() {
        String val = getProperty(Constants.KEY_USERNAME);
        if (val == null) {
            val = getProperty("username", "sa");
        }
        return val;
    }

    public String getPassword() {
        return getProperty(Constants.KEY_PASSWORD, "");
    }
}
