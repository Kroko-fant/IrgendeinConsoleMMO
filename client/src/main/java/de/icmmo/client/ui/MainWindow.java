package de.icmmo.client.ui;

import de.icmmo.client.Client;

public class MainWindow extends BorderedWindow {

    private static final int WIDTH = 60, HEIGHT = 20;
    private static final int CLEARLINES = 10;
    private final char[][] drawBuffer;

    public MainWindow(Client client) {
        super(new Rectangle(0, 0, WIDTH, HEIGHT));
        drawBuffer = new char[HEIGHT][WIDTH];
        //Testing
        DialogChoiceWindow win = new DialogChoiceWindow(new Rectangle(1, 1, WIDTH - 2, HEIGHT - 2), client,
                new String[]{"Option1", "Option2", "Exit"},
                c -> switch(c) {
                    case 0 -> new BorderedWindow(new Rectangle(4, 3, 20, 10));
                    case 1 -> new BorderedWindow(new Rectangle(2, 5, 20, 10));
                    default -> null;
                });
        children.add(win);
        //new BorderedWindow(new Rectangle(24, 13, 4, 4));
        //children.add(win2);
        //win.children.add(new MultiChoiceWindow(new Rectangle(1, 1, 18, 7), client, new String[]{"Test1blaaaaaaaaaaaa", "Test2", "Test2", "Test2", "Test2", "Test2", "Exit"}, (c, w) -> win.enabled = false));
    }

    public void draw() {
        //create buffer
        repaint(0, 0, dimensions.getWidth(), dimensions.getHeight(), drawBuffer);
        for (int i = 0; i < CLEARLINES; ++i) {
            System.out.print("\n");
        }
        // print window
        for (int i = 0; i < dimensions.getHeight(); ++i) {
            System.out.println(drawBuffer[i]);
        }
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow(null);
        window.draw();
    }

}
