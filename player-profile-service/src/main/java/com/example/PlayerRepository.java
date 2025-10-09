package com.example;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class PlayerRepository {

    private final Map<Long, Player> players = new ConcurrentHashMap<>();
    private final Map<Long, OwnedGame> ownedGames = new ConcurrentHashMap<>();

    public PlayerRepository() {

        Player player1 = new Player(1L, "GamerPro123");
        Player player2 = new Player(2L, "Alice");
        players.put(player1.id, player1);
        players.put(player2.id, player2);

        ownedGames.put(1L, new OwnedGame(1L, 1L, 1L));
        ownedGames.put(2L, new OwnedGame(2L, 1L, 3L));
        ownedGames.put(3L, new OwnedGame(3L, 2L, 2L));
    }

    public Player findByUsername(String username) {
        return players.values().stream()
                .filter(player -> player.username.equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    public List<OwnedGame> findGamesForPlayer(Long playerId) {
        return ownedGames.values().stream()
                .filter(ownedGame -> ownedGame.playerId.equals(playerId))
                .collect(Collectors.toList());
    }
}