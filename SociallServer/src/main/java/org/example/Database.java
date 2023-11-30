package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    protected String dbHost = "localhost";
    protected String dbPort = "3306";
    protected String dbUser = "root";
    protected String dbPass = "1234";
    protected String dbName = "socservices";
    //protected static User user = new User();
    //protected static Suggestion suggestion = new Suggestion();
    Connection dbConnection;

    {
        try {
            dbConnection = getDbConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    String request;

    /*Database(){
        try {
            dbConnection = getDbConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/

    public Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        System.out.println("Все заебок");
        return dbConnection;
    }

    public String CheckUser(String snils, String salt_password) {
        request = "SELECT * FROM user_profile WHERE snils = ? AND salt_password = ?";
        try {
            PreparedStatement prSt = dbConnection.prepareStatement(request);
            prSt.setString(1, snils);
            prSt.setString(2, salt_password);
            ResultSet resultSet = prSt.executeQuery();
            int schet = 0;
            String answerbd = null;
            while (resultSet.next()) {
                answerbd =  "OK";
                schet = 1;
            }
            if (schet == 0) {
                answerbd = "BADLY";
            }
            else if (schet > 1){
                answerbd = "Вы что ебланы как получилось 2 или больше одинаковых?";
            }
            return answerbd;

        } catch (SQLException e) {
            System.out.println("ошибка ");
            throw new RuntimeException(e);
        }
    }
}