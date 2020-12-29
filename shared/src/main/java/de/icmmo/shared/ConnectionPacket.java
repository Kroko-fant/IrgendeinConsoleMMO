package de.icmmo.shared;

public class ConnectionPacket extends Packet{
    boolean success;

    public ConnectionPacket(boolean success) {
        this.success = success;
    }

    @Override
    PacketType getType() {
        return null;
    }
}
