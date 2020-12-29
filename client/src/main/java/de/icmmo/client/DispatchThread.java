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
            boolean update = client.handle( client.queue.poll());
            if (update) {
                mainWindow.draw();
            }
        }
    }
}
