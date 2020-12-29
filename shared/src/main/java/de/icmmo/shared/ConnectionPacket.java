package de.icmmo.shared;

public class ConnectionPacket extends Packet{
    boolean success;

    public ConnectionPacket(boolean success) {
        this.success = success;
    }

    @Override
    public PacketType getType() {
        return PacketType.CONNECTION;
    }
}
