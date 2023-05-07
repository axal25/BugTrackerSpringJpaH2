package axal25.oles.jacek.jdbc;

import axal25.oles.jacek.constant.Constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseUtils {
    public static Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        properties.put("user", Constants.Database.userName);
        properties.put("password", Constants.Database.password);
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:" + Constants.Database.databaseName, properties);
        connection.setAutoCommit(false);
        return connection;
    }
}
