package de.icmmo.client;

import de.icmmo.client.ui.MainWindow;

public class DispatchThread extends Thread{

    private final Client client;
    private final MainWindow mainWindow;

    public DispatchThread(Client client) {
        this.client = client;
        this.mainWindow = new MainWindow(client);
    }

    @Override
    public void run() {
        mainWindow.draw();
        while (!isInterrupted()){
            boolean update;
            try {
                update = client.handle( client.queue.take());
            } catch (InterruptedException e) {
                return;
            }
            if (update) {
                mainWindow.draw();
            }
        }
    }
}
