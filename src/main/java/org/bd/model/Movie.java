package org.bd.model;

import jakarta.persistence.*;

@Entity
public class Movie {
    @Id

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_seq")
    @SequenceGenerator(name = "movie_seq", sequenceName = "movie_seq")
    private Long movieId;

    private String title;

    private String description;

    public Movie() {}

    public Movie(String name, String description) {
        this.title = name;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getMovieId() {
        return movieId;
    }
}
