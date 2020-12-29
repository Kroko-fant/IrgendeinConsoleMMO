package de.icmmo.shared;

import data.PlayerData;

import java.io.Serializable;

public class PlayerDataPacket extends Packet implements Serializable {

    private final PlayerData playerData;

    public PlayerDataPacket(PlayerData playerData) {
        this.playerData = playerData;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    @Override
    public PacketType getType() {
        return PacketType.PLAYER_DATA;
    }
}
