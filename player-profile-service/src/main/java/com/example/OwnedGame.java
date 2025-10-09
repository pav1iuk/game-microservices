package com.example;

public class OwnedGame {
    public Long id;
    public Long playerId;
    public Long gameId;

    public OwnedGame(Long id, Long playerId, Long gameId) {
        this.id = id;
        this.playerId = playerId;
        this.gameId = gameId;
    }
}