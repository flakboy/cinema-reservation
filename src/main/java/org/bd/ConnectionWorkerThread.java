package org.bd;

//import org.bd.http.HttpResponseBuilder;

import org.bd.http.HttpRequest;
import org.bd.http.HttpRequestReader;
import org.bd.http.HttpResponseBuilder;
import org.bd.http.Router;

import java.io.*;
import java.net.Socket;

public class ConnectionWorkerThread extends Thread {
    private final Socket socket;

    public ConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            HttpRequestReader httpRequestReader = new HttpRequestReader();
            HttpRequest request = httpRequestReader.read(inputStream);


            Router router = new Router();
            switch (request.getMethod()) {
                case "GET":
                    router.get(outputStream, request);
                    break;
                case "POST":
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {}
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
    }
}
