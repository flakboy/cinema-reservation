package org.bd.model;


import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;

//import java.sql.Date;
//import java.sql.Time;

@Entity
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int showId;


    //potrzebna przy mapowaniu dat (?)
    @Column
    private LocalDate date;

    @Column
    private LocalTime time;



    //TODO:
//    ScreeningRoom room;
    @JoinColumn(name = "movieId")
    @ManyToOne(cascade = CascadeType.PERSIST)
    Movie movie;


    public Show() {}

    public Show(Movie movie, LocalDate date, LocalTime time) {
        this.movie = movie;
        this.date = date;
        this.time = time;
    }


    public int getShowId() {
        return showId;
    }

    public Movie getMovie() {
        return movie;
    }

    public Movie setMovie(Movie movie) {
        return movie;
    }
}
