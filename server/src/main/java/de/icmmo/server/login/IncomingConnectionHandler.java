package de.icmmo.server.login;

import de.icmmo.server.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class IncomingConnectionHandler extends Thread{

    Server server;
    private ServerSocket incoming;

    public IncomingConnectionHandler(Server server, ServerSocket incoming) {
        this.server = server;
        this.incoming = incoming;
    }

    public void closeConnection() throws IOException {
        this.incoming.close();
    }

    @Override
    public void run() {

        while (!isInterrupted()){
            try {
                Socket incomingConnection = this.incoming.accept();
                if (incomingConnection != null){
                    LoginManager loginManager = new LoginManager(incomingConnection, server);
                    loginManager.setDaemon(true);
                    loginManager.start();
                }
            }catch (IOException ignored){
            }
        }
    }
}
