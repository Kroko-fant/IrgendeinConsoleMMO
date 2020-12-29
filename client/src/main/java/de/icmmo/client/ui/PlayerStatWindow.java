package de.icmmo.client.ui;

import data.LevelHelper;
import data.PlayerData;
import de.icmmo.client.Client;
import de.icmmo.shared.Packet;
import de.icmmo.shared.PacketType;
import de.icmmo.shared.PlayerDataPacket;
import de.icmmo.shared.PlayerDataRequestPacket;

public class PlayerStatWindow extends Window {

    private final MultiChoiceWindow selection;

    protected PlayerStatWindow(Rectangle dimensions, Client client) {
        super(dimensions);
        client.addObserver(this::receive);
        String[] choices = new String[10];
        choices[0] = "Back";
        this.selection = new MultiChoiceWindow(new Rectangle(0, 10, 20, choices.length), client, choices);
        selection.setCallback(this::selected);
        children.add(selection);

        client.sendPacket(new PlayerDataRequestPacket());
    }

    private void drawPlayerData(PlayerData data) {
        writeText(0, 1, data.getName());
        writeText(0, 2, String.format("Level: %3.3s %s/%s", data.getLevel(), data.getXp(), LevelHelper.getXpToNextLevelUp(data.getLevel())));
    }

    private void selected(int choice) {
        if (choice == 0) this.close();
    }

    private boolean receive(Packet p) {
        if (p.getType() == PacketType.PLAYER_DATA) {
            drawPlayerData(((PlayerDataPacket) p).getPlayerData());
            return true;
        }
        return false;
    }
}
