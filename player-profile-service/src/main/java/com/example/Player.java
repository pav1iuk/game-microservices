package com.example;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String username;
    public String email;

    // Зв'язок з іграми, якими володіє гравець
    // fetch = EAGER для спрощення, щоб список завантажувався одразу
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    public List<OwnedGame> library = new ArrayList<>();

    public Player() {}

    public Player(String username, String email) {
        this.username = username;
        this.email = email;
    }
}