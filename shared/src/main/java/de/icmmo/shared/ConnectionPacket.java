package de.icmmo.shared;

public class ConnectionPacket extends Packet{
    private Boolean success;
    private String requesttype;
    private String text;

    public ConnectionPacket(boolean success) {
        this.success = success;
    }

    public ConnectionPacket(Boolean success, String requesttype, String text) {
        this.success = success;
        this.requesttype = requesttype;
        this.text = text;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getRequesttype() {
        return requesttype;
    }

    public String getText() {
        return text;
    }

    @Override
    public PacketType getType() {
        return PacketType.CONNECTION;
    }
}
