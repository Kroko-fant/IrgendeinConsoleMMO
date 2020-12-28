package de.icmmo.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private Thread receiver, sender;
    private Reader inputReader;

    public Client(String ip, int port) throws IOException {
        //this.socket = new Socket(ip, port);
        this.receiver = new Receiver();
        if (System.getProperties().getProperty("os").startsWith("Windows")){
            this.inputReader = new WindowsReader();
        } else {
            this.inputReader = new LinuxReader();
        }
    }

    protected void runClient() {
        receiver.setDaemon(true);
        receiver.start();

        Scanner scanner = new Scanner(new InputStreamReader(System.in));
        scanner.useDelimiter("[A-Z]");


        while (receiver.isAlive()){
            char c = inputReader.readNextChar();
            System.out.println(c);
            if (c == '^'){
                return;
            }
        }
    }


    public static void main(String[] args) throws IOException, InterruptedException {
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
