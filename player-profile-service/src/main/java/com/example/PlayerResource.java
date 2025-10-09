package com.example;

import com.example.grpc.GameCatalog;
import com.example.grpc.GetGameRequest;
import io.quarkus.grpc.GrpcClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {

    @Inject
    PlayerRepository playerRepository;

    @GrpcClient("catalog")
    GameCatalog gameCatalog;

    @GET
    @Path("/{username}/library")
    public List<GameDetails> getPlayerLibrary(@PathParam("username") String username) {
        Player player = playerRepository.findByUsername(username);
        List<OwnedGame> ownedGames = playerRepository.findGamesForPlayer(player.id);

        return ownedGames.stream()
                .map(ownedGame -> {
                    var gameResponse = gameCatalog
                            .getGameById(GetGameRequest.newBuilder().setId(ownedGame.gameId).build())
                            .await().indefinitely();
                    return new GameDetails(gameResponse.getTitle(), gameResponse.getGenre());
                })
                .collect(Collectors.toList());
    }
}

class GameDetails {
    public String title;
    public String genre;
    public GameDetails(String title, String genre) {
        this.title = title;
        this.genre = genre;
    }
}