package de.icmmo.shared;

import java.io.Serializable;

public abstract class Packet implements Serializable {
    private static final long serialVersionUID = 10000L;

    public abstract PacketType getType();

}
