package de.icmmo.server;

import de.icmmo.server.db.Database;
import de.icmmo.server.login.IncomingConnectionHandler;
import de.icmmo.shared.ConnectionPacket;
import de.icmmo.shared.Packet;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

    private LinkedBlockingQueue<Packet> packetQueue;
    private final User[] users;
    private final Object outputLock;
    private int userPointer;
    private final IncomingConnectionHandler connectionHandler;
    private final Database db;

    public Server(Database db, ServerSocket incoming, int size) {
        this.db = db;
        outputLock = new Object();
        synchronized (outputLock) {
            connectionHandler = new IncomingConnectionHandler(this, incoming);
            this.packetQueue = new LinkedBlockingQueue<>();
            users = new User[size];
            userPointer = 0;
            connectionHandler.setDaemon(true);
            connectionHandler.start();
        }
    }

    public Database getDb() {
        return db;
    }

    public void close() {
        db.close();
        synchronized (outputLock) {
            for (int i = 0; i < users.length;i++)
                if (users[i] != null) {
                    users[i].disconnect();
                    users[i] = null;
                }
        }
        try {
            connectionHandler.closeConnection();
        } catch (IOException ignored) {
        }
    }

    /**
     * @param incomingConnection a new Socket Connection representing a client
     * @return returns if the connection was successfull
     * **/
    public boolean addConnection(String userName, Socket incomingConnection, ObjectOutputStream outputStream,
                                 ObjectInputStream inputStream) {
        int temp = userPointer;
        synchronized (outputLock) {
            userPointer = (userPointer + 1) % users.length;
            while (userPointer != temp) {
                if (users[temp] == null) {
                    try {
                        users[temp] = new User(userName, incomingConnection, outputStream, inputStream);
                        System.out.println("Write");
                        users[temp].write(
                                new ConnectionPacket(true, "success", "Connection successful"));
                    } catch (IOException ignored) {
                        return false;
                    }
                    return true;
                }
                userPointer = (userPointer + 1) % users.length;
            }
        }
        try {
            PrintWriter pw = new PrintWriter(incomingConnection.getOutputStream());
            pw.print(new ConnectionPacket(false));
        } catch (IOException ignored) {
        }
        return false;
    }


    /**
     * @param command An input of a command that has to be executed
     * **/
    private static void evaluateCommand(String command) throws UnknownHostException {
        String[] splitted = command.split(" ");
        switch (splitted[0]) {
            case "sql" -> System.out.println(command.substring(4)); // TODO Execute command
            case "ip" -> System.out.println("Server is running on: " + InetAddress.getLocalHost());
        }
    }

    public static void main(String[] args) {
        ServerSocket incoming;
        Database db;
        // Nice port
        int port = 6969;
        int size = 64;

        System.out.println("Server started");
        //Handle IP/PORT
        try {
            System.out.println("Server is running on: " + InetAddress.getLocalHost() + ":" + port);
        } catch (UnknownHostException e) {
            System.err.println("No Internet :(");
            return;
        }
        try {
            incoming = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Failed to open a ServerSocket :(");
            return;
        }

        // Creating a database
        try {
             db = new Database();
        } catch (SQLException throwables) {
            System.err.println("Could not establish a database");
            return;
        }

        // Starting the Server
        Server server = new Server(db, incoming, size);
        System.out.println("Server initialized");

        // Reading in Command Input
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            for (String command = ""; !command.equals("exit"); command = reader.readLine()) {
                try {
                    evaluateCommand(command);
                } catch (Exception ignored){
                }
            }
        } catch (IOException e) {
            System.err.println("Server closed due to an console Error!");
        } finally {
            server.close();
        }
    }
}
