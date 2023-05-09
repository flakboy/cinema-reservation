package org.bd;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

class GetFileHandler implements HttpHandler {
    private final String resourcePath;
    private final String mimetype;

    public GetFileHandler(String resourcePath, String mimetype) {
        this.resourcePath = resourcePath;
        this.mimetype = mimetype;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        File file = new File(this.resourcePath);
        t.getResponseHeaders().set("Content-type", mimetype);
        t.getResponseHeaders().set("Content-length", Long.toString(file.length()));
        t.sendResponseHeaders(200, file.length());

        try (OutputStream outputStream = t.getResponseBody(); FileInputStream reader = new FileInputStream(file)) {
            int _byte = reader.read();
            while (_byte >= 0) {
                outputStream.write(_byte);
                _byte = reader.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}