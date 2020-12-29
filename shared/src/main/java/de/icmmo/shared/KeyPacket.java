package de.icmmo.shared;

public class KeyPacket extends Packet {

    private final char key;

    public KeyPacket(char key) {
        this.key = key;
    }

    public char getKey() {
        return key;
    }

    @Override
    public PacketType getType() {
        return PacketType.KEY_INPUT;
    }
}
