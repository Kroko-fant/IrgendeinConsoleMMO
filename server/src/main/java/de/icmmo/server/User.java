package de.icmmo.server;

import de.icmmo.shared.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;

public class User {
    private String userName;
    private Socket userSocket;
    private final ObjectOutputStream outputStream;
    private UserInputReader userInputReader;

    public User(String userName, Socket userSocket, ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException {
        this.userName = userName;
        this.userSocket = userSocket;
        this.outputStream = outputStream;
        this.userInputReader = new UserInputReader(this, inputStream);
        userInputReader.setDaemon(true);
        userInputReader.start();
    }

    public void disconnect(){
        try {
            outputStream.close();
            userInputReader.interrupt();
            userSocket.close();
        } catch (IOException ignored){
        }
    }

    public void write(Packet p) throws IOException {
        try {
            outputStream.writeObject(p);
        } catch (IOException e) {
            System.out.println("Error sending package");
            outputStream.writeObject(p);
        }
    }
}
