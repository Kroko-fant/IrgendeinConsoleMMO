package de.icmmo.server.db;

import java.sql.*;

public class Database {

    private static final String fileName = "game.sqlite";

    private Connection connection;

    public Database() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e) {
            throw new SQLException("sqlite library not found");
        }
        connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
        createTables();
    }

    private void createTables() {
        try {
            Statement stmt = connection.createStatement();
            for (String query : DefaultQueries.tableCreateQueries) {
                stmt.executeUpdate(query);
            }
        } catch (SQLException e) {
            System.err.println("Error while creating tables: " + e.getMessage());
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error while closing database: " + e.getMessage());
            }
            connection = null;
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean validateLogin(String username, String password) throws SQLException, IndexOutOfBoundsException {
        PreparedStatement statement = connection.prepareStatement(DefaultQueries.searchUserPassword);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.getString(1).equals(password) ;
    }

    public boolean userNameTaken(String username) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DefaultQueries.checkUserName);
        statement.setString(1, username);
        return statement.executeQuery().getFetchSize() > 0;
    }

    public void insertUser(String username, String password) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DefaultQueries.insertUser);
        statement.setString(1, username);
        statement.setString(2, password);
        statement.executeUpdate();
    }
}
