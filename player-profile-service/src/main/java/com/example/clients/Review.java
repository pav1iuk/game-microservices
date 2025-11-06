package com.example.clients;

public class Review {
    public Long id;
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

    public Review(Long id, Long gameId, String username, int rating, String reviewText) {
        this.id = id;
        this.gameId = gameId;
        this.username = username;
        this.rating = rating;
        this.text = reviewText;
    }

}