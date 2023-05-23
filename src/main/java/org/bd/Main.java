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
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import org.bd.model.QueryParser;
import org.bd.model.Show;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
                ObjectMapper om = new ObjectMapper();
                //wymagane do parsowania LocalDate/LocalTime na format JSON
                om.registerModule(new JavaTimeModule());
                om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                Session session = getSession();

                try {
                    String queryString = exchange.getRequestURI().getQuery();

                    if (queryString == null) {
                        throw new IllegalArgumentException();
                    }

                    Map<String, String> queryMap = new QueryParser().parse(queryString);

                    if (!queryMap.containsKey("startDate") || !queryMap.containsKey("endDate")) {
                        throw new IllegalArgumentException();
                    }

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
                } catch (IllegalArgumentException exception) {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("error", "Invalid query parameters");

                    String responseBody = om.valueToTree(data).toString();
                    exchange.sendResponseHeaders(400, responseBody.getBytes().length);
                    exchange.getResponseBody().write(responseBody.getBytes());

                    exception.printStackTrace();
                } catch (NullPointerException exception) {
                    HashMap<String, String> data = new HashMap<>();
                    data.put("error", "Internal server error");

                    String responseBody = om.valueToTree(data).toString();
                    exchange.sendResponseHeaders(500, responseBody.getBytes().length);
                    exchange.getResponseBody().write(responseBody.getBytes());

                    exception.printStackTrace();
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


