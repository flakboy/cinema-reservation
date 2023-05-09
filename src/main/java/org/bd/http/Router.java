package org.bd.http;

import java.io.IOException;
import java.io.OutputStream;

public class Router {

    public void get(OutputStream stream, HttpRequest request) {

        String path = request.getPath();

        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
        try {
            switch (path) {
                case "/":
                    responseBuilder.serveFile(stream, "src/main/resources/index.html", "text/html");
                    break;
                case "/style.css":
                    responseBuilder.serveFile(stream, "src/main/resources/style.css", "text/css");
                    break;
//                case "/shows.css":
//                    responseBuilder.serveFile(stream, "src/main/resources/style.css", "application/json");
//                    break;
                default:
                    responseBuilder.serve404(stream);
                    break;
            }
        } catch (IOException e) {}
    }
}
