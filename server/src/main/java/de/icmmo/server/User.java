package de.icmmo.server;

import de.icmmo.shared.Packet;

import java.io.IOException;
import java.net.Socket;

public class User {
    Socket userSocket;

    public User(Socket userSocket) {
        this.userSocket = userSocket;
    }

    public void write(Packet p) throws IOException{

    }
}
