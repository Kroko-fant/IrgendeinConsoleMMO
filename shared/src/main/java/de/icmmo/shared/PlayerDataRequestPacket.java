package de.icmmo.shared;

public class PlayerDataRequestPacket extends Packet {
    @Override
    public PacketType getType() {
        return PacketType.PLAYER_DATA;
    }
}
