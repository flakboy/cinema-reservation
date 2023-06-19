package org.bd.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationId;


    @JsonBackReference
    @JoinColumn(name = "showId")
    @ManyToOne(cascade = CascadeType.PERSIST)
    Show show;

    @JsonIgnore
    @JoinColumn(name = "clientId")
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    Client client;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="reservationId")
    private List<ReservationDetail> details = new ArrayList<>();

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

    public List<ReservationDetail> getDetails() {
        return new ArrayList<>(details);
    }

    public void addDetail(ReservationDetail detail) {
        details.add(detail);
    }
}
