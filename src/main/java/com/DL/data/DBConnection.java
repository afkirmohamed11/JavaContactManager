package com.DL.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class DBConnection {
    private Logger logger = Logger.getLogger(this.getClass());
    private static String dbUrl;
    private static String login;
    private static String password;
    private static String driver;
    private static Connection connection;

    private DBConnection() throws DataBaseException {
        try {
            Properties dbProperties = DbPropertiesLoader.loadPoperties("log4j.properties");
            dbUrl = dbProperties.getProperty("db.url");
            login = dbProperties.getProperty("db.login");
            password = dbProperties.getProperty("db.password");
            driver = dbProperties.getProperty("db.driver");
            Class.forName(driver);
            connection = DriverManager.getConnection(dbUrl, login, password);

        } catch (Exception var2) {
            this.logger.error(var2);
            throw new DataBaseException(var2);
        }
    }

    public static Connection getInstance() throws DataBaseException {
        if (connection == null) {
            new DBConnection();
        }
        return connection;
    }

}
