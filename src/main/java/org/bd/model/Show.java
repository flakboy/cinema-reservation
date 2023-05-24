package org.bd.model;


import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    @Column
    private LocalTime time;




    @JoinColumn(name = "movieId")
    @ManyToOne(cascade = CascadeType.PERSIST)
    Movie movie;

    @JoinColumn(name = "movieRoomId")
    @ManyToOne(cascade = CascadeType.PERSIST)
    MovieRoom movieRoom;

    @Column(length = 50)
    private String type;

    public Show() {}

    public Show(Movie movie, LocalDate date, LocalTime time, MovieRoom movieRoom, String type) {
        this.movie = movie;
        this.date = date;
        this.time = time;
        this.movieRoom = movieRoom;
        this.type = type;
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

    @Override
    public String toString() {
        return time + " - " + date;
    }


    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return this.time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
    }

    public MovieRoom getMovieRoom() {return this.movieRoom;}
    public void setMovieRoom(MovieRoom movieRoom) {this.movieRoom = movieRoom;}

    public String getType() {return this.type;}
    public void setType(String type) {this.type = type;}
}
