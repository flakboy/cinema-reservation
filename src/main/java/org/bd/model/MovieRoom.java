package org.bd.model;

import jakarta.persistence.*;

@Entity
public class MovieRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movieRoomId;

    @Column
    private int rows;

    @Column
    private int seats;

    public MovieRoom() {}

    public MovieRoom(int rows, int seats) {
        this.rows = rows;
        this.seats = seats;
    }

    public int getMovieRoomId() {
        return movieRoomId;
    }
    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }
}
