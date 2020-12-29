package de.icmmo.client.ui;

import de.icmmo.client.Client;

public class MainWindow extends BorderedWindow {

    public static final int WIDTH = 60, HEIGHT = 20;

    public MainWindow(Client client) {
        super(new Rectangle(0, 0, WIDTH, HEIGHT));
        //Testing
        BorderedWindow win = new BorderedWindow(new Rectangle(4, 3, 20, 10));
        children.add(win);
        win.children.add(new MultiChoiceWindow(new Rectangle(1, 1, 18, 7), client, new String[]{"Test1blaaaaaaaaaaaa", "Test2", "Test2", "Test2", "Test2", "Test2", "Exit"}, c -> {}));
    }

    @Override
    protected void initWindow() {
        super.initWindow();
    }

    public void draw() {
        repaint(0, 0, dimensions.getWidth(), dimensions.getHeight(), drawnImage);

        // print window
        for (int i = 0; i < dimensions.getHeight(); ++i) {
            System.out.println(drawnImage[i]);
        }
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow(null);
        window.draw();
    }

}
