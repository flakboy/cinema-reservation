package org.bd;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

class PostHandler implements HttpHandler {
    private final PostAction action;
    private final SessionFactory sessionFactory;

    public PostHandler(PostAction action, SessionFactory sessionFactory) {
        this.action = action;
        this.sessionFactory = sessionFactory;
    }

    private void saveReservation(HttpExchange exchange) {
        Session session = sessionFactory.openSession();
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = exchange.getRequestBody();
        int i;
        try {
            while ((i = inputStream.read()) != -1) {
                sb.append((char) i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        String body = sb.toString();
        System.out.print(Arrays.toString(body.split("&")));
        try {
            exchange.sendResponseHeaders(200, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("bbb");
        } finally {
            session.close();
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {


        switch (action) {
            case SAVE_RESERVATION -> {
                saveReservation(exchange);
            }
            case UNKNOWN -> {
                System.out.print("Nieznany handler");
                exchange.sendResponseHeaders(404, 0);
            }

        }
    }
}