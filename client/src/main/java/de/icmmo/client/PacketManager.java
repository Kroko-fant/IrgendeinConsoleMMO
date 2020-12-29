package de.icmmo.client;

public class PacketManager extends Thread{

    private Client client;
    public PacketManager(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (!isInterrupted()){
            client.handle( client.queue.poll());
        }
    }
}
