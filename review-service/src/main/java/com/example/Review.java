package com.example;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class Review extends PanacheEntity {

    public Long gameId;
    public String username;
    public int rating;
    public String text;

    public Review() {
    }

    public Review(Long gameId, String username, int rating, String text) {
        this.gameId = gameId;
        this.username = username;
        this.rating = rating;
        this.text = text;
    }
}