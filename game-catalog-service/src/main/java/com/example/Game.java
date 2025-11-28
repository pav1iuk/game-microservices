package com.example;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Game extends PanacheEntity {

    public String title;
    public String genre;
    public String developer;

    public Game() {
    }

    public Game(String title, String genre, String developer) {
        this.title = title;
        this.genre = genre;
        this.developer = developer;
    }
}