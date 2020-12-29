package de.icmmo.client;

import de.icmmo.shared.KeyPacket;

public class KeyInputThread extends Thread {

    private final Client client;
    private final Reader inputReader;

    public KeyInputThread(Client client) {
        this.client = client;
        if (System.getProperties().getProperty("os.name").startsWith("Windows")) {
            inputReader = new WindowsReader();
        } else {
            inputReader = new LinuxReader();
        }
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            char c = inputReader.readNextChar();
            client.queue.add(new KeyPacket(c));
        }
        inputReader.end();
    }

    public void stopRead() {
        inputReader.end();
    }
}
