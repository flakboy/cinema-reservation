package org.bd;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
    private final int port;
//    private String webroot = "";

    ServerSocket serverSocket;

    public ServerThread(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(this.port);
    }

    public void run() {
        System.out.println("Nasłuchiwanie na porcie " + this.port);
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                //.accept() oczekuje na połączenie; przy połączeniu z klientem zwróci socket
                Socket socket = serverSocket.accept();
                ConnectionWorkerThread worker = new ConnectionWorkerThread(socket);
                worker.run();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {}
            }
        }
    }
}
