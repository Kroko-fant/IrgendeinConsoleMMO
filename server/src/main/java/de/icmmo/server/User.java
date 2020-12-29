package de.icmmo.server;

import de.icmmo.shared.Packet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;

public class User {
    String userName;
    Socket userSocket;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;

    public User(String userName, Socket userSocket, ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException {
        this.userName = userName;
        this.userSocket = userSocket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;

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
