package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
        public static Connection getConnection() throws SQLException {
            String url = "jdbc:mysql://localhost:3306/mydatabase";
            String user = "root";
            String password = "password123";
            return DriverManager.getConnection(url, user, password);
        }
}

