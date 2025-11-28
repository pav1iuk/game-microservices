package com.example;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class OwnedGame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long gameId; // ID гри з каталогу

    @ManyToOne
    @JoinColumn(name = "player_id")
    @JsonIgnore // Щоб уникнути циклічності при серіалізації в JSON
    public Player player;

    public OwnedGame() {}

    public OwnedGame(Long gameId, Player player) {
        this.gameId = gameId;
        this.player = player;
    }
}