package de.icmmo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class IncomingConnectionHandler extends Thread{

    private Server server;
    ServerSocket incoming;

    public IncomingConnectionHandler(Server server, ServerSocket incoming) {
        this.server = server;
        this.incoming = incoming;
    }

    @Override
    public void run() {

        while (!isInterrupted()){
            try {
                Socket incomingConnection = this.incoming.accept();
                if (incomingConnection != null){
                    boolean success = server.addConnection(incomingConnection);
                    System.out.println(success ? "Connection successfull": "Connection refused");
                    if (!success)
                        incomingConnection.close();
                }
            }catch (IOException ignored){
            }
        }
    }
}
