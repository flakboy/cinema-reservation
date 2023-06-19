package org.bd.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class ReservationDetail {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationDetailId;

    @Column
    private int row;

    @Column
    private int seat;

    @JsonIgnore
    @JoinColumn(name = "reservationId")
    @ManyToOne(cascade = CascadeType.PERSIST)
    Reservation reservation;

    public ReservationDetail() {}

    public ReservationDetail(int row, int seat, Reservation reservation) {
        this.row = row;
        this.seat = seat;
        this.reservation = reservation;
    }

    public int getReservationDetailId() {
        return reservationDetailId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
