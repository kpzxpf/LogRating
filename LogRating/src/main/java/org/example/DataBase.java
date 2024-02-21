package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
    private static final String BD_USERNAME = "postgres";
    private static final String BD_PASSWORD = "postgres";
    private static final String BD_URL = "jdbc:postgresql://localhost:5432/postgres";
    private Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");

    public DataBase() throws SQLException {
    }

    public void insertInBD(long chatId, String login, String password) throws SQLException {
        String insert = "INSERT INTO Users (chatId, login, password) VALUES (" + chatId + ", '" + login + "', '" + password + "')";
        Statement statement = this.connection.createStatement();
        statement.executeQuery(insert);
    }

    public boolean CheckChatId(long chatId) throws SQLException {
        String selectCount = "SELECT Count(id) AS COUNT FROM Users WHERE chatId = '" + chatId + "'";
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery(selectCount);

        int count;
        String str;
        for(str = null; result.next(); count = Integer.parseInt(str)) {
            str = result.getString("COUNT");
        }

        count = Integer.parseInt(str);
        return count != 0;
    }

    public String outputLogin(long chatId) throws SQLException {
        String selectCount = "SELECT login FROM Users WHERE chatId = '" + chatId + "'";
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery(selectCount);
        return result.next() ? result.getString("login") : null;
    }

    public String outputPassword(long chatId) throws SQLException {
        String selectCount = "SELECT password FROM Users WHERE chatId = '" + chatId + "'";
        Statement statement = this.connection.createStatement();
        ResultSet result = statement.executeQuery(selectCount);
        return result.next() ? result.getString("password") : null;
    }
    public void deleteData(long chatId) throws SQLException {
        String selectCount = "DELETE FROM Users WHERE chatId = '" + chatId + "'";
        Statement statement = this.connection.createStatement();
        statement.executeQuery(selectCount);
    }
}