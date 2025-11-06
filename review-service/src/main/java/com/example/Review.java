package com.example;

public class Review {
    public Long id;
    public Long gameId;
    public String username;
    public int rating;
    public String text;

    public Review() {
    }

    public Review(Long id, Long gameId, String username, int rating, String text) {
        this.id = id;
        this.gameId = gameId;
        this.username = username;
        this.rating = rating;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", gameId=" + gameId +
                ", username='" + username + '\'' +
                ", rating=" + rating +
                ", text='" + text + '\'' +
                '}';
    }
}