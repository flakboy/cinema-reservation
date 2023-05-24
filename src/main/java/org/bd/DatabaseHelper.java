package org.bd;

import org.bd.model.Movie;
import org.bd.model.MovieRoom;
import org.bd.model.Show;
import org.bd.model.Client;
import org.bd.model.Reservation;
import org.bd.model.ReservationDetail;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;


public class DatabaseHelper {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    // funkcje zwracajace listy wszystkich obiektow z bazy danych
    public List<Movie> getAllMovies() {
        Session session = sessionFactory.openSession();
        List<Movie> result;
        Query<Movie> query = session.createQuery("from Movie", Movie.class);
        result = query.getResultList();
        session.close();
        return result;
    }

    public List<Client> getAllClients() {
        Session session = sessionFactory.openSession();
        List<Client> result;
        Query<Client> query = session.createQuery("from Client", Client.class);
        result = query.getResultList();
        session.close();
        return result;
    }

    public List<Reservation> getAllReservations() {
        Session session = sessionFactory.openSession();
        List<Reservation> result;
        Query<Reservation> query = session.createQuery("from Reservation", Reservation.class);
        result = query.getResultList();
        session.close();
        return result;
    }

    public List<ReservationDetail> getAllReservationDetails() {
        Session session = sessionFactory.openSession();
        List<ReservationDetail> result;
        Query<ReservationDetail> query = session.createQuery("from ReservationDetail", ReservationDetail.class);
        result = query.getResultList();
        session.close();
        return result;
    }

    public List<MovieRoom> getAllMovieRooms() {
        Session session = sessionFactory.openSession();
        List<MovieRoom> result;
        Query<MovieRoom> query = session.createQuery("from MovieRoom", MovieRoom.class);
        result = query.getResultList();
        session.close();
        return result;
    }

    public List<Show> getAllShows() {
        Session session = sessionFactory.openSession();
        List<Show> result;
        Query<Show> query = session.createQuery("from Show", Show.class);
        result = query.getResultList();
        session.close();
        return result;
    }

    // funkcje zwrazajace listy obiektow z bazy danych
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

    public List<Reservation> getReservationsByClient(Client client) {
        Session session = sessionFactory.openSession();
        List<Reservation> result;
        Query<Reservation> query = session.createQuery("from Reservation where client = :clientid", Reservation.class);
        query.setParameter("clientid", client); // nie wiem czy id czy obiekt

        result = query.getResultList();

        session.close();
        return result;
    }

    public List<ReservationDetail> getReservationDetailsByReservation(Reservation reservation) {
        Session session = sessionFactory.openSession();
        List<ReservationDetail> result;
        Query<ReservationDetail> query = session.createQuery("from ReservationDetail where reservation = :reservationid", ReservationDetail.class);
        query.setParameter("reservationid", reservation); // nie wiem czy id czy obiekt

        result = query.getResultList();

        session.close();
        return result;
    }

    public List<Reservation> getReservationsByShow(Show show) {
        Session session = sessionFactory.openSession();
        List<Reservation> result;
        Query<Reservation> query = session.createQuery("from Reservation where show = :showid", Reservation.class);
        query.setParameter("showid", show); // nie wiem czy id czy obiekt

        result = query.getResultList();

        session.close();
        return result;
    }


    // updateuje obiekt w bazie danych

    public void updateMovie(Movie movie) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.update(movie);

        tx.commit();
        session.close();
    }

    public void updateClient(Client client) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.update(client);

        tx.commit();
        session.close();
    }

    public void updateReservation(Reservation reservation) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.update(reservation);

        tx.commit();
        session.close();
    }

    public void updateReservationDetail(ReservationDetail reservationDetail) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.update(reservationDetail);

        tx.commit();
        session.close();
    }

    public void updateMovieRoom(MovieRoom movieRoom) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.update(movieRoom);

        tx.commit();
        session.close();
    }

    public void updateShow(Show show) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.update(show);

        tx.commit();
        session.close();
    }

    // funkcje zwracajace obiekty z bazy danych o podanym id
    public Movie getMovieById(int movie_id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        //może zwrócić null jeśli rekord o danym id nie istnieje
        Movie foundMovie = session.get(Movie.class, movie_id);

        tx.commit();
        session.close();

        return foundMovie;
    }

    public Client getClientById(int client_id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Client foundClient = session.get(Client.class, client_id);

        tx.commit();
        session.close();

        return foundClient;
    }

    public Reservation getReservationById(int reservation_id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Reservation foundReservation = session.get(Reservation.class, reservation_id);

        tx.commit();
        session.close();

        return foundReservation;
    }

    public ReservationDetail getReservationDetailById(int reservation_detail_id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        ReservationDetail foundReservationDetail = session.get(ReservationDetail.class, reservation_detail_id);

        tx.commit();
        session.close();

        return foundReservationDetail;
    }

    public MovieRoom getMovieRoomById(int movie_room_id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        MovieRoom foundMovieRoom = session.get(MovieRoom.class, movie_room_id);

        tx.commit();
        session.close();

        return foundMovieRoom;
    }

    public Show getShowById(int show_id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Show foundShow = session.get(Show.class, show_id);

        tx.commit();
        session.close();

        return foundShow;
    }

    // funkcje dodające obiekty do bazy danych
    public void addMovieRoom(int rows, int seats) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.save(new MovieRoom(rows, seats));

        tx.commit();
        session.close();
    }

    public void addShow(Show show) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.save(show);

        tx.commit();
        session.close();
    }

    public void addMovie(Movie movie) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.save(movie);

        tx.commit();
        session.close();
    }

    public void addClient(Client client) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.save(client);

        tx.commit();
        session.close();
    }

    public void addReservation(Reservation reservation) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.save(reservation);

        tx.commit();
        session.close();
    }

    public void addReservationDetail(ReservationDetail reservationDetail) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.save(reservationDetail);

        tx.commit();
        session.close();
    }

    // funkcje usuwające obiekty z bazy danych
    public void deleteReservation(Reservation reservation) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.delete(reservation);

        tx.commit();
        session.close();
    }

    public void deleteReservationDetail(ReservationDetail reservationDetail) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.delete(reservationDetail);

        tx.commit();
        session.close();
    }

    public void deleteClient(Client client) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.delete(client);

        tx.commit();
        session.close();
    }

    public void deleteMovie(Movie movie) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.delete(movie);

        tx.commit();
        session.close();
    }

    public void deleteShow(Show show) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.delete(show);

        tx.commit();
        session.close();
    }

    public void deleteMovieRoom(MovieRoom movieRoom) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        session.delete(movieRoom);

        tx.commit();
        session.close();
    }

}