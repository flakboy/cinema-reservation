package org.bd.model;

import jakarta.persistence.*;

@Entity
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ShowId;

    private String title;

    private String description;

    private int length;

    public Show() {}

    public Show(String name, String description) {
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


}
