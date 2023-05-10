package org.bd;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import org.bd.model.Movie;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
public class Main {

    private static final SessionFactory sessionFactory;


    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

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
                System.out.println(exchange.getRequestURI().getQuery());


                ObjectMapper om = new ObjectMapper();
                om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                List<Movie> result = new ArrayList<>();
                try {
                    String hql = "from org.bd.model.Movie";
                    Query<Movie> query = session.createQuery(hql, Movie.class);

                    result = query.getResultList();
                } finally {
                    session.close();
                }

                HashMap<String, Object> map = new HashMap<>();
                map.put("movies", result);
                String data = om.valueToTree(map).toString();
                exchange.getResponseHeaders().set("Content-type", "application/json");
                exchange.sendResponseHeaders(200, data.length());

                exchange.getResponseBody().write(data.getBytes());

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


