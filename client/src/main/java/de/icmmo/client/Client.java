package de.icmmo.client;

import de.icmmo.client.observer.Observable;
import de.icmmo.shared.KeyPacket;
import de.icmmo.shared.Packet;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Client extends Observable<Packet> {

    private final Socket socket;
    private final Thread receiver;
    private final Reader inputReader;
    private final PacketManager packetManager;
    protected final LinkedBlockingQueue<Packet> queue;

    public Client(String ip, int port) throws IOException {
        this.socket = new Socket(ip, port);

        this.receiver = new Receiver(socket, this);
        if (System.getProperties().getProperty("os.name").startsWith("Windows")){
            this.inputReader = new WindowsReader();
        } else {
            this.inputReader = new LinuxReader();
        }
        // Manages Packages in the queue
        packetManager = new PacketManager(this);
        packetManager.setDaemon(true);
        this.queue = new LinkedBlockingQueue<>();
        packetManager.start();
    }

    protected void runClient() {
        receiver.setDaemon(true);
        receiver.start();
        while (receiver.isAlive()){
            char c = inputReader.readNextChar();
            if (c == 'x'){
                break;
            }
            queue.add(new KeyPacket(c));
        }
        inputReader.end();
    }


    public static void main(String[] args) {
        //TODO: Remove this
        args = new String[]{"localhost", "6969"};

        System.out.println("Connecting...");
        if (args.length > 2){
            System.err.println("Invalid Args! Args should be length 2");
            return;
        }
        String ip = args[0];
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (IllegalArgumentException e){
            System.err.println("Invalid Port!");
            return;
        }
        Client client;
        try{
            client = new Client(ip, port);
        } catch (IOException e) {
            System.err.println("Ungültige IP/Port!");
            return;
        }
        client.runClient();
    }
}
