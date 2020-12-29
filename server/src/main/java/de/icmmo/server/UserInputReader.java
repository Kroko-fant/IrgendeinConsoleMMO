package de.icmmo.server;

import data.PlayerData;
import de.icmmo.shared.Packet;
import de.icmmo.shared.PlayerDataPacket;

import java.io.IOException;
import java.io.ObjectInputStream;

public class UserInputReader extends Thread {

    private User user;
    private ObjectInputStream inputStream;

    public UserInputReader(User user, ObjectInputStream inputStream) {
        this.user = user;
        this.inputStream = inputStream;
    }

    private void providePlayerData() throws IOException {
        user.write(
                new PlayerDataPacket(new PlayerData(
                        "Simon",
                        106,
                        1112312,
                        100,
                        43211234,
                        42134,
                        54362,
                        2314,
                        3456,
                        3456,
                        6453)
                )
        );
    }

    // Run Method that takes a packet of the client and responds to the client via an other package
    @Override
    public void run() {
        while (!isInterrupted()) {
            Packet packet;
            try {
                packet = (Packet) inputStream.readObject();

                switch (packet.getType()) {
                    case PLAYER_DATA -> providePlayerData();
                    case CONNECTION -> this.isInterrupted();
                }


            } catch (IOException | ClassNotFoundException ignored) {
                continue;
            }

        }
    }
}
