package org.bd;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bd.model.Client;
import org.bd.model.Reservation;
import org.bd.model.ReservationDetail;
import org.bd.model.Show;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

class PostHandler implements HttpHandler {
    private final PostAction action;
    private final SessionFactory sessionFactory;

    public PostHandler(PostAction action, SessionFactory sessionFactory) {
        this.action = action;
        this.sessionFactory = sessionFactory;
    }

    private void saveReservation(HttpExchange exchange) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = exchange.getRequestBody();
        int i;
        try {
            while ((i = inputStream.read()) != -1) {
                sb.append((char) i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Nie udalo sie odczytac body");
            try {
                exchange.sendResponseHeaders(400, 0);
            } catch (IOException ex) {
            } finally {
                exchange.close();
                return;
            }
        }
        String body = sb.toString();
        System.out.println(body);
        Map<String, String> params = null;
        int showId = 0;
        int userId = 0;
        try {
            params = new QueryParser().parse(body);
            if (!params.containsKey("userId")) {
                throw new IllegalArgumentException("Nie podano id użytkownika!");
            } else if (!params.containsKey("showId")) {
                throw new IllegalArgumentException("Nie podano id seansu!");
            }

            showId = Integer.parseInt(params.get("showId"));
            userId = Integer.parseInt(params.get("userId"));
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            try {
                exchange.sendResponseHeaders(400, 0);
            } catch (IOException e) {
                e.printStackTrace();
                exchange.close();
                return;
            }
        }


        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Show show = showId != 0 ? session.get(Show.class, showId) : null;
        Client client = userId != 0 ? session.get(Client.class, userId) : null;

        if (show != null && client != null) {

            Reservation reservation = new Reservation(show, client);

            for (String key : params.keySet()) {
                if (key.startsWith("seat-")) {
                    String[] coords = key.substring(5).split("x");
                    reservation.addDetail(new ReservationDetail(
                            Integer.parseInt(coords[0]),
                            Integer.parseInt(coords[1]),
                            reservation
                    ));
                }
            }

            session.persist(reservation);
            tx.commit();
        }

        try {
            exchange.getResponseHeaders().set("Location", "/submit_success.html");
            exchange.sendResponseHeaders(302, 0);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            exchange.close();
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