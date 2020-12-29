package de.icmmo.client;

import de.icmmo.shared.KeyPacket;

public class KeyInputThread extends Thread {

    private final Client client;

    public KeyInputThread(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        Reader inputReader;
        // Uses an input handler depending on the operating system
        if (System.getProperties().getProperty("os.name").startsWith("Windows")) {
            inputReader = new WindowsReader();
        } else {
            inputReader = new LinuxReader();
        }

        while (!this.isInterrupted()) {
            char c = inputReader.readNextChar();
            if (c == 'x') {
                break;
            }
            client.queue.add(new KeyPacket(c));
        }
        inputReader.end();
    }
}
