package de.icmmo.client;

import de.icmmo.client.observer.Observable;
import de.icmmo.shared.ConnectionPacket;
import de.icmmo.shared.KeyPacket;
import de.icmmo.shared.Packet;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

public class Client extends Observable<Packet> {

    private final Socket socket;
    private final ObjectOutputStream outputChannel;
    private final Thread receiver;
    private final Reader inputReader;
    private final DispatchThread dispatchThread;
    protected final LinkedBlockingQueue<Packet> queue;

    public Client(String ip, int port) throws IOException, LoginException {
        // Initialising socket and outputchannel
        this.socket = new Socket(ip, port);
        this.outputChannel = new ObjectOutputStream(socket.getOutputStream());

        // Starts a receiver that puts packages in the queue
        this.receiver = new Receiver(socket, this);
        receiver.setDaemon(true);
        receiver.start();
        this.queue = new LinkedBlockingQueue<>();

        if (!login())
            throw new LoginException();



        // Uses an input handler depending on the operating system
        if (System.getProperties().getProperty("os.name").startsWith("Windows")) {
            this.inputReader = new WindowsReader();
        } else {
            this.inputReader = new LinuxReader();
        }
        // Manages Packages in the queue
        dispatchThread = new DispatchThread(this);
        dispatchThread.setDaemon(true);
        dispatchThread.start();
    }

    private boolean login() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        ConnectionPacket packet = null;
        do {
            try {
                packet = (ConnectionPacket) queue.take();
                System.out.println("Package Client!");
            } catch (InterruptedException e) {
                continue;
            }
            switch (Objects.requireNonNull(packet).getRequesttype()) {
                case "username" -> outputChannel.writeObject(new ConnectionPacket(null, packet.getRequesttype(),
                        br.readLine()));
                // thats not secure
                case "password" -> outputChannel.writeObject(new ConnectionPacket(null, packet.getRequesttype(),
                        br.readLine()));
            }
            System.out.println("Package client?");

        } while (Objects.requireNonNull(packet).getSuccess() != null || !packet.getSuccess());
        return true;
    }

    protected void runClient() {
        // receives Packages
        while (receiver.isAlive()) {
            char c = inputReader.readNextChar();
            if (c == 'x') {
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
        if (args.length > 2) {
            System.err.println("Invalid Args! Args should be length 2");
            return;
        }
        String ip = args[0];
        int port;
        try {
            port = Integer.parseInt(args[1]);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid Port!");
            return;
        }
        Client client;
        try {
            client = new Client(ip, port);
        } catch (IOException e) {
            System.err.println("Ungültige IP/Port!");
            return;
        } catch (LoginException ignored) {
            System.err.println("Failed to log in!");
            return;
        }
        client.runClient();
    }
}
