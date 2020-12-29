package de.icmmo.server.login;

import de.icmmo.server.Server;
import de.icmmo.shared.ConnectionPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

public class LoginManager extends Thread{
    private final ObjectOutputStream outputChannel;
    private final ObjectInputStream inputChannel;
    private Socket incomingConnection;
    private Server server;

    public LoginManager(Socket incomingConnection, Server server) throws IOException {
        this.incomingConnection = incomingConnection;
        this.server = server;
        this.outputChannel = new ObjectOutputStream(incomingConnection.getOutputStream());
        this.inputChannel = new ObjectInputStream(incomingConnection.getInputStream());
    }

    @Override
    public void run() {
        ConnectionPacket username = null;
        ConnectionPacket password = null;
        boolean success = false;
        int exceptioncounter = 0;
        do {
            try {
                //Ask for Username
                outputChannel.writeObject(
                        new ConnectionPacket(null, "username", "Please provide a username!"));
                System.out.println("Package sent");
                username = (ConnectionPacket) inputChannel.readObject();
                // Ask for Password
                outputChannel.writeObject(new ConnectionPacket(null, "password", "Please provide a password!"));
                System.out.println("Package sent2");
                password = (ConnectionPacket) inputChannel.readObject();
                success = server.getDb().validateLogin(username.getText(), password.getText());
            } catch (IOException | ClassNotFoundException | SQLException | NullPointerException e) {
                exceptioncounter++;
            }
        } while (!success);


       success = server.addConnection(incomingConnection);
        System.out.println(success ? "Connection successfull": "Connection refused");
        if (!success) {
            try {
                incomingConnection.close();
            } catch (IOException ioException) {
            }
        }
    }
}
