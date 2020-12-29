package de.icmmo.server.login;

import de.icmmo.server.Server;
import de.icmmo.shared.ConnectionPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
        while (true) {
            try {
                outputChannel.writeObject(new ConnectionPacket(
                        null, "auth",
                        "Type register to register\nType login to login into an existing account"));
                ConnectionPacket request = (ConnectionPacket) inputChannel.readObject();
                if (request.getRequesttype().startsWith("login")) {
                    establishLogin();
                    break;
                } else if (request.getRequesttype().startsWith("register") ){
                    register();
                    break;
                }
            } catch (IOException | ClassNotFoundException ignored) {
            }
        }

        boolean success = server.addConnection(incomingConnection);
        System.out.println(success ? "Connection successfull" : "Connection refused");
        if (!success) {
            try {
                incomingConnection.close();
            } catch (IOException ignored) {
            }
        }
    }

    private void register() {
        String username = "";
        String password = "";
        while (true) {
            try {
                do {
                    while (username.length() < 3) {
                        outputChannel.writeObject(
                                new ConnectionPacket(null, "username", "Please provide a username!"));
                        username = ((ConnectionPacket) inputChannel.readObject()).getText();
                    }
                } while (server.getDb().userNameTaken(username));
            } catch (IOException | ClassNotFoundException | SQLException ignored) {

            }
            try {
                outputChannel.writeObject(new ConnectionPacket(null, "password", "Please provide a password!"));
                password = ((ConnectionPacket) inputChannel.readObject()).getText();
                server.getDb().insertUser(username, password);
                break;
            } catch (IOException | ClassNotFoundException | SQLException ignored) {
            }
        }
    }

    private void establishLogin() {
        // Establish login
        ConnectionPacket username;
        ConnectionPacket password;
        boolean success = false;
        int exceptioncounter = 0;
        while (!success) {
            try {
                //Ask for Username
                outputChannel.writeObject(
                        new ConnectionPacket(null, "username", "Please provide a username!"));
                username = (ConnectionPacket) inputChannel.readObject();
                // Ask for Password
                outputChannel.writeObject(new ConnectionPacket(null, "password", "Please provide a password!"));
                password = (ConnectionPacket) inputChannel.readObject();
                System.out.println(username.getText() + password.getText());
                success = server.getDb().validateLogin(username.getText(), password.getText());
            } catch (IOException | ClassNotFoundException | SQLException | NullPointerException |
                    IndexOutOfBoundsException e) {
                exceptioncounter++;
                System.out.println("Exceptioncounter while login: " + exceptioncounter);
                System.out.println(e.getClass().toString());
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
