package com.example;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped

public class GameRepository {
    private final Map<Long, Game> games = new ConcurrentHashMap<>();

    public GameRepository() {
        games.put(1L, new Game(1L, "Cyberpunk 2077", "RPG", "CD Projekt Red"));
        games.put(2L, new Game(2L, "The Witcher 3", "RPG", "CD Projekt Red"));
        games.put(3L, new Game(3L, "Elden Ring", "Action RPG", "FromSoftware"));
    }

    public List<Game> findAll() {
        return List.copyOf(games.values());
    }

    public Game findById(Long id) {
        return games.get(id);
    }
}
