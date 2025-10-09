package com.example.clients;

public class Review {
    public Long id;
    public Long gameId;
    public String username;
    public int rating;
    public String reviewText;

    public Review() {
    }

    public Review(Long id, Long gameId, String username, int rating, String reviewText) {
        this.id = id;
        this.gameId = gameId;
        this.username = username;
        this.rating = rating;
        this.reviewText = reviewText;
    }
}