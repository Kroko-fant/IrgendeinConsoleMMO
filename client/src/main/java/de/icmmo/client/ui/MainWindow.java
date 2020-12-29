package de.icmmo.client.ui;

import data.LevelHelper;
import data.PlayerData;
import de.icmmo.client.Client;
import de.icmmo.shared.Packet;
import de.icmmo.shared.PlayerDataPacket;

import java.util.Arrays;

public class MainWindow extends BorderedWindow {

    private static final int WIDTH = 60, HEIGHT = 20;
    private static final int CLEARLINES = 10;
    private final char[][] drawBuffer;

    public MainWindow(Client client) {
        super(new Rectangle(0, 0, WIDTH, HEIGHT));
        drawBuffer = new char[HEIGHT][WIDTH];
        client.addObserver(this::receivePacket);
        //Testing
        /*DialogChoiceWindow win = new DialogChoiceWindow(new Rectangle(1, 3, WIDTH - 2, HEIGHT - 4), client,
                new String[]{"Option1", "Option2", "Exit"},
                c -> switch(c) {
                    case 0 -> new BorderedWindow(new Rectangle(4, 3, 20, 10));
                    case 1 -> new BorderedWindow(new Rectangle(2, 5, 20, 10));
                    default -> null;
                });
        children.add(win);*/
        //new BorderedWindow(new Rectangle(24, 13, 4, 4));
        //children.add(win2);
        //win.children.add(new MultiChoiceWindow(new Rectangle(1, 1, 18, 7), client, new String[]{"Test1blaaaaaaaaaaaa", "Test2", "Test2", "Test2", "Test2", "Test2", "Exit"}, (c, w) -> win.enabled = false));
        initWindow();
        //writePlayerStats(new PlayerData("na1234aa9", 12, 99, 0, 0, 0, 0, 0, 0, 0, 0));
        // Add dialog
        DialogChoiceWindow win = new DialogChoiceWindow(new Rectangle(1, 3, WIDTH - 2, HEIGHT - 4), client,
                new String[]{"Stats", "Exit"},
                c -> switch(c) {
                    case 0 -> new BorderedWindow(new Rectangle(4, 3, 20, 10));
                    default -> null;
                });
        win.setCloseCallback(() -> setEnabled(false));
        children.add(win);
    }

    private void initWindow() {
        //draw sub borders for player
        final int borderLocation = 2;
        Arrays.fill(drawnImage[borderLocation], '\u2550');
        drawnImage[borderLocation][0] = '\u2560';
        drawnImage[borderLocation][dimensions.getWidth() - 1] = '\u2563';
    }

    private void writePlayerStats(PlayerData data) {

        final int location = 1;
        final String bar = "\u2588";

        final int requiredXp = LevelHelper.getXpToNextLevelUp(data.getLevel());
        int steps = 10;
        if (requiredXp != 0) steps = (data.getXp() * 10) / requiredXp;

        writeText(2, location, String.format("%-10.10s LvL.%-3.3s [%-10.10s] %10.10sg", data.getName(), data.getLevel(), bar.repeat(steps), data.getGold()));
    }

    private boolean receivePacket(Packet p) {
        switch (p.getType()) {
            case PLAYER_DATA -> writePlayerStats(((PlayerDataPacket) p).getPlayerData());
        }
        return false;
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
