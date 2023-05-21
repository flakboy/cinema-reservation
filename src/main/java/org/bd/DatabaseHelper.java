package org.bd;

import org.bd.model.Movie;
import org.bd.model.Show;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;


public class DatabaseHelper {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    public List<Show> getShowsInRange(LocalDate beginDate, LocalDate endDate) {
        Session session = sessionFactory.openSession();
        List<Show> result;
        Query<Show> query = session.createQuery("from Show where date >= :start_date and date <= :end_date", Show.class);
        query.setParameter("start_date", beginDate);
        query.setParameter("end_date", endDate);

        result = query.getResultList();

        session.close();
        return result;
    }

    public Movie getMovieById(int movie_id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        //może zwrócić null jeśli rekord o danym id nie istnieje
        Movie foundMovie = session.get(Movie.class, movie_id);

        tx.commit();
        session.close();

        return foundMovie;
    }
}