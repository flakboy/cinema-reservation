package org.bd;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import org.bd.model.Show;

import org.hibernate.*;
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
                ObjectMapper om = new ObjectMapper();
                //wymagane do parsowania LocalDate/LocalTime na format JSON
                om.registerModule(new JavaTimeModule());
                om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                SimpleFilterProvider filterProvider = new SimpleFilterProvider();
                filterProvider.addFilter("showFilter", SimpleBeanPropertyFilter.serializeAllExcept("reservations"));
                om.setFilterProvider(filterProvider);
                Session session = getSession();

                //przewidujemy że odpowiedź na to żądanie zawsze będzie JSON-em
                exchange.getResponseHeaders().set("Content-type", "application/json");
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

                    //wymagane, ponieważ domyślnie pole movie jest ładowane leniwie
                    for (Show show: shows) {
                        Hibernate.initialize(show.getMovie());
                    }


                    HashMap<String, Object> map = new HashMap<>();
                    map.put("shows", shows);
                    String data = om.valueToTree(map).toString();


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
                    //kod bardzo podobny do powyższego, można by to przenieść do jakiejś nowej klasy która
                    //wysyłałaby generic odpowiedzi
                    HashMap<String, String> data = new HashMap<>();
                    data.put("error", "Internal server error");

                    String responseBody = om.valueToTree(data).toString();
                    exchange.sendResponseHeaders(500, responseBody.getBytes().length);
                    exchange.getResponseBody().write(responseBody.getBytes());

                    exception.printStackTrace();
                } finally {
                    exchange.close(); //bez tej linijki serwer obsługuje jedynie co drugie połączenie
                    session.close();
                }
            });
            server.createContext("/select_seats", new GetFileHandler("src/main/resources/select_seats.html", "text/html"));
            server.createContext("/submit_success", new GetFileHandler("src/main/resources/submit_success.html", "text/html"));
            server.createContext("/reservation.js", new GetFileHandler("src/main/resources/reservation.js", "text/javascript"));
            server.createContext("/submit_reservation", new PostHandler(PostAction.SAVE_RESERVATION, sessionFactory));
            server.createContext("/get_show_data", (HttpExchange exchange) -> {
                Session session = getSession();

                ObjectMapper om = new ObjectMapper();
                om.registerModule(new JavaTimeModule());
                SimpleFilterProvider provider = new SimpleFilterProvider();
                provider.addFilter("showFilter", SimpleBeanPropertyFilter.serializeAll());
                om.setFilterProvider(provider);
                exchange.getResponseHeaders().set("Content-type", "application/json");
                try {
                    String queryString = exchange.getRequestURI().getQuery();
                    if (queryString == null) {
                        throw new IllegalArgumentException();
                    }

                    Map<String, String> queryMap = new QueryParser().parse(queryString);
                    if (!queryMap.containsKey("showId")) {
                        throw new IllegalArgumentException();
                    }

                    int id = Integer.parseInt(queryMap.get("showId"));
                    Show show = session.get(Show.class, id);
                    if (show == null) {
                        //redirect
                        return;
                    }
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("data", show);
                    String data = om.valueToTree(map).toString();

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
                    //kod bardzo podobny do powyższego, można by to przenieść do jakiejś nowej klasy która
                    //wysyłałaby generic odpowiedzi
                    HashMap<String, String> data = new HashMap<>();
                    data.put("error", "Internal server error");

                    String responseBody = om.valueToTree(data).toString();
                    exchange.sendResponseHeaders(500, responseBody.getBytes().length);
                    exchange.getResponseBody().write(responseBody.getBytes());

                    exception.printStackTrace();
                } finally {
                    exchange.close(); //bez tej linijki serwer obsługuje jedynie co drugie połączenie
                    session.close();
                }
            });
            server.setExecutor(null); // creates a default executor
            server.start();
        } catch (IOException e) {
            System.out.println("Nie udalo sie uruchomic serwera.");
            e.printStackTrace();
        }

    }
}


