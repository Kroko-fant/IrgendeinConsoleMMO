package de.icmmo.server.login;

import de.icmmo.server.Server;
import de.icmmo.shared.ConnectionPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;

public class LoginManager extends Thread {
    private final ObjectOutputStream outputChannel;
    private final ObjectInputStream inputChannel;
    private final Socket incomingConnection;
    private final Server server;

    public LoginManager(Socket incomingConnection, Server server) throws IOException {
        this.incomingConnection = incomingConnection;
        this.server = server;
        this.outputChannel = new ObjectOutputStream(incomingConnection.getOutputStream());
        this.inputChannel = new ObjectInputStream(incomingConnection.getInputStream());
    }

    @Override
    public void run() {
        // Ask for a package
        try {
            ConnectionPacket start = (ConnectionPacket) inputChannel.readObject();
            if (!start.getRequesttype().equals("start")) {
                System.out.println("Connection with client failed!");
                return;
            }
        } catch (IOException | ClassNotFoundException | NullPointerException i) {
            System.out.println("Connection with client failed!");
            return;
        }

        //Choose Login Method
        String userName;
        while (true) {
            try {
                outputChannel.writeObject(new ConnectionPacket(
                        null, "auth",
                        "Type register to register\nType login to login into an existing account"));
                ConnectionPacket request = (ConnectionPacket) inputChannel.readObject();
                if (request.getRequesttype().startsWith("login")) {
                    userName = establishLogin();
                    break;
                } else if (request.getRequesttype().startsWith("register") ){
                    userName= register();
                    break;
                }
            } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException ignored) {
            }
        }

        boolean success = server.addConnection(userName, incomingConnection, outputChannel, inputChannel);
        System.out.println(success ? "Connection successfull" : "Connection refused");
        if (!success) {
            try {
                incomingConnection.close();
            } catch (IOException ignored) {
            }
        }
    }

    private String register() throws NoSuchAlgorithmException {
        MessageDigest hasingObject = MessageDigest.getInstance("SHA-256");
        String username = "";
        ConnectionPacket password = null;
        while (true) {
            try {
                do {
                    while (username.length() < 3) {
                        outputChannel.writeObject(
                                new ConnectionPacket(null, "username",
                                        "[Register] Please provide a username!"));
                        username = ((ConnectionPacket) inputChannel.readObject()).getText();
                    }
                } while (server.getDb().userNameTaken(username));
            } catch (IOException | ClassNotFoundException | SQLException ignored) {

            }
            try {
                outputChannel.writeObject(new ConnectionPacket(null, "password",
                        "[Register] Please provide a password!"));
                password = (ConnectionPacket) inputChannel.readObject();
                server.getDb().insertUser(username,
                        new String(hasingObject.digest(password.getText().getBytes(StandardCharsets.UTF_8))));
                break;
            } catch (IOException | ClassNotFoundException | SQLException ignored) {
                System.out.println("Exception while inserting a new User");
            }
        }
        return username;
    }

    private String establishLogin() throws NoSuchAlgorithmException {
        MessageDigest hasingObject = MessageDigest.getInstance("SHA-256");
        // Establish login
        String username = "";
        ConnectionPacket password;
        boolean success = false;
        int exceptioncounter = 0;
        while (!success) {
            try {
                //Ask for Username
                outputChannel.writeObject(
                        new ConnectionPacket(null, "username",
                                "[Login] Please provide a username!"));
                username = ((ConnectionPacket) inputChannel.readObject()).getText();
                // Ask for Password
                outputChannel.writeObject(new ConnectionPacket(null, "password",
                        "[Login] Please provide a password!"));
                password = (ConnectionPacket) inputChannel.readObject();
                success = server.getDb().validateLogin(username,
                        new String(hasingObject.digest(password.getText().getBytes(StandardCharsets.UTF_8))));
            } catch (IOException | ClassNotFoundException | SQLException | NullPointerException |
                    IndexOutOfBoundsException e) {
                exceptioncounter++;
                System.out.println("Exceptioncounter while login: " + exceptioncounter);
                System.out.println(e.getClass().toString());
                System.out.println(Arrays.toString(e.getStackTrace()));
                System.out.println(e.getMessage());
            }
        }
        return username;
    }
}
