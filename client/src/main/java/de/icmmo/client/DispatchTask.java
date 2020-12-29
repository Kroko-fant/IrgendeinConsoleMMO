package de.icmmo.client;

import de.icmmo.client.ui.MainWindow;

public class DispatchTask {

    private final Client client;
    private final MainWindow mainWindow;

    public DispatchTask(Client client) {
        this.client = client;
        this.mainWindow = new MainWindow(client);
    }

    public void run() {
        mainWindow.draw();
        while (true) {
            boolean update;
            try {
                update = client.handle(client.queue.take());
            } catch (InterruptedException e) {
                return;
            }
            if (update) {
                if (!mainWindow.isEnabled()) break;
                mainWindow.draw();
            }
        }
    }
}
