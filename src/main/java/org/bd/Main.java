package org.bd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDate;
//import java.time.LocalTime;
//import java.time.Month;
//import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import org.bd.model.Show;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
public class Main {

    private static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public static Session getSession() throws HibernateException {
        return sessionFactory.openSession();
    }

    public static void main(String[] args) {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
            System.out.println("Nasłuchiwanie na porcie 8080");
            server.createContext("/", new GetFileHandler("src/main/resources/index.html", "text/html"));
            server.createContext("/style.css", new GetFileHandler("src/main/resources/style.css", "text/css"));

            server.createContext("/shows", (HttpExchange exchange) -> {
                final Session session = getSession();
                try {
                    String queryString = exchange.getRequestURI().getQuery();

                    if (queryString == null) {
                        throw new IllegalArgumentException();
                    }
                    String[] params = queryString.split("&");

                    Map<String, String> queryMap = new HashMap<>();

                    for (String param : params) {
                        String[] tokens = param.split("=");
                        String key = tokens[0];
                        String value = tokens[1];
                        queryMap.put(key, value);
                    }

                    if (!queryMap.containsKey("startDate") || !queryMap.containsKey("endDate")) {
                        throw new IllegalArgumentException();
                    }

                    ObjectMapper om = new ObjectMapper();
                    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                    List<Show> shows = (new DatabaseHelper()).getShowsInRange(
                            LocalDate.parse(queryMap.get("startDate")),
                            LocalDate.parse(queryMap.get("endDate"))
                    );
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("shows", shows);
                    String data = om.valueToTree(map).toString();
                    exchange.getResponseHeaders().set("Content-type", "application/json");

                    
                    //TODO: wysyłać dane po kawałku, a nie wszystko naraz
                    byte[] bytes = data.getBytes();
                    exchange.sendResponseHeaders(200, bytes.length);
                    exchange.getResponseBody().write(bytes);
                } catch (NullPointerException | IllegalArgumentException exception) {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("error", "Invalid query parameters");

                    ObjectMapper om = new ObjectMapper();
                    String responseBody = om.valueToTree(data).toString();
                    exchange.sendResponseHeaders(400, responseBody.getBytes().length);
                    exchange.getResponseBody().write(responseBody.getBytes());
                } finally {
                    //bez tej linijki serwer obsługuje jedynie co drugie połączenie
                    exchange.close();
                    session.close();
                }
            });
            server.createContext("/reserve", new GetFileHandler("src/main/resources/reserve.html", "text/html"));
            server.createContext("/reservation.js", new GetFileHandler("src/main/resources/reservation.js", "text/javascript"));
            server.createContext("/submit_reservation", new PostHandler(PostAction.SAVE_RESERVATION, sessionFactory));
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException e) {
            System.out.println("Nie udalo sie uruchomic serwera.");
        }

    }
}


