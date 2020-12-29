package de.icmmo.client;

import de.icmmo.client.observer.Observable;
import de.icmmo.shared.ConnectionPacket;
import de.icmmo.shared.Packet;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class Client extends Observable<Packet> {

    private final Socket socket;
    private final ObjectOutputStream outputChannel;
    private final Thread receiver;
    private final KeyInputThread inputThread;
    protected final LinkedBlockingQueue<Packet> queue;

    public Client(String ip, int port) throws IOException, LoginException, InterruptedException {
        // Initialising socket and outputchannel
        this.socket = new Socket(ip, port);
        this.outputChannel = new ObjectOutputStream(socket.getOutputStream());

        // Starts a receiver that puts packages in the queue
        this.queue = new LinkedBlockingQueue<>();
        this.receiver = new Receiver(socket, this);
        receiver.setDaemon(true);
        receiver.start();

        if (!login())
            throw new LoginException();

        // Reads input and adds in queue
        inputThread = new KeyInputThread(this);
        inputThread.setDaemon(true);
        inputThread.start();
    }

    private boolean login() throws IOException, InterruptedException {
        ConnectionPacket packet = null;

        // Initialise a inputreader and send a login request to the server
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        outputChannel.writeObject(new ConnectionPacket(true, "start", ""));

        // Checks for Login/Register
        packet = (ConnectionPacket) queue.take();

        while (packet.getRequesttype().equals("auth")) {
            System.out.println(packet.getText());

            //Answers the server if the client wants to register or login
            outputChannel.writeObject(new ConnectionPacket(null, br.readLine(), ""));
            packet = (ConnectionPacket) queue.take();
        }

        while (packet.getSuccess() == null || !packet.getSuccess()) {
            System.out.println(packet.getText());
            outputChannel.writeObject(new ConnectionPacket(null, packet.getRequesttype(),
                    br.readLine()));
            packet = (ConnectionPacket) queue.take();
            System.out.println(packet.getRequesttype());
        }

        System.out.println("Login");
        return true;
    }

    protected void runClient() {
        // Manages Packages in the queue and Ui
        new DispatchTask(this).run();

        inputThread.stopRead();
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
        } catch (InterruptedException ignored) {
            System.err.println("Client wurde unerwartet beendet");
            return;
        }
        client.runClient();
    }
}
