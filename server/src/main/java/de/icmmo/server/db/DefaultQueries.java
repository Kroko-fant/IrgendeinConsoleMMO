package de.icmmo.server.db;

public class DefaultQueries {

    public static final String[] tableCreateQueries = {
            "create table if not exists PLAYERS(ID INTEGER primary key autoincrement, USERNAME VARCHAR(255) not null, PASSWORD VARCHAR(255) not null, JOINED TIMESTAMP default CURRENT_TIMESTAMP);"
    };
    public static final String searchUserPassword = "SELECT PASSWORD FROM PLAYERS WHERE USERNAME == ?";
    public static final String checkUserName = "SELECT name FROM PLAYERS WHERE USERNAME == ?";
    public static final String insertUser = "INSERT INTO PLAYERS (username, password) VALUES(?,?)";
}
