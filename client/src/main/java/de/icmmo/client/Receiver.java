package de.icmmo.client;

import de.icmmo.shared.Packet;

import java.io.*;
import java.net.Socket;

public class Receiver extends Thread {

    private final Socket socket;
    private final Client client;

    public Receiver(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream(); ObjectInputStream input = new ObjectInputStream(inputStream)) {
            while (!this.isInterrupted()) {
                try {
                    Packet packet = (Packet) input.readObject();
                    if (packet == null)
                        continue;
                    client.queue.add(packet);
                } catch (IOException | ClassNotFoundException ignored) {
                    System.out.println("Error receiving a package " +ignored.getMessage());
                }
            }
        } catch (IOException ignored) {
            System.err.println("IO-Exception! Connection closed");

        }
    }


}
