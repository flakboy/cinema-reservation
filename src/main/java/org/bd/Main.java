package org.bd;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.*;

public class Main {

    public static void main(String[] args) {
//        SessionFactory sessionFactory = new Configuration().configure(
//                "jdbc:mysql://server:port/db_name?user=username&password=passwd"
//        );


        try {
            ServerThread serverThread = new ServerThread(8080);
            serverThread.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}