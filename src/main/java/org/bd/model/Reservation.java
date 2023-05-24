package org.bd.model;

import jakarta.persistence.*;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationId;

    @JoinColumn(name = "showId")
    @ManyToOne(cascade = CascadeType.PERSIST)
    Show show;

    @JoinColumn(name = "clientId")
    @ManyToOne(cascade = CascadeType.PERSIST)
    Client client;

    public Reservation() {}

    public Reservation(Show show, Client client) {
        this.show = show;
        this.client = client;
    }

    public int getReservationId() {
        return reservationId;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
