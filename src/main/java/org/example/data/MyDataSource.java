package org.example.data;

import                                                                                                                                                                                                                                                                                                                                                                                                                                                  java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataSource {

    private static final String URL = "jdbc:mysql://localhost:3306/todoit?autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Berlin";
    private static final String USER = "root";
    private static final String PASSWORD = "8603";



    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


}
