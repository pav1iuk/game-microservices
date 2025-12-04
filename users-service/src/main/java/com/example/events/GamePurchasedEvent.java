package com.example.events;

public class GamePurchasedEvent {
    public String username;
    public Long gameId;
    public String timestamp;

    public GamePurchasedEvent() {}

    public GamePurchasedEvent(String username, Long gameId, String timestamp) {
        this.username = username;
        this.gameId = gameId;
        this.timestamp = timestamp;
    }
}