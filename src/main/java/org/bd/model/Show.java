package org.bd.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Date;
import java.sql.Time;

public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int showId;

    @Column
    private Date date;

    @Column
    private Time time;

    public Show() {}
}
