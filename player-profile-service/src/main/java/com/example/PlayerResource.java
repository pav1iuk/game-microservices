package com.example;

import com.example.grpc.GameCatalog;
import com.example.grpc.GetGameRequest;
import io.quarkus.grpc.GrpcClient;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlayerResource {

    @Inject
    PlayerRepository playerRepository;

    @GrpcClient("catalog")
    GameCatalog gameCatalog;

    @GET
    @Path("/{username}/library")
    public List<GameDetails> getPlayerLibrary(@PathParam("username") String username) {
        Player player = playerRepository.findByUsername(username);

        if (player == null) {
            return List.of();
        }

        return player.library.stream()
                .map(ownedGame -> {
                    // 3. Для кожної гри робимо gRPC запит за деталями
                    var gameResponse = gameCatalog
                            .getGameById(GetGameRequest.newBuilder().setId(ownedGame.gameId).build())
                            .await().indefinitely();
                    return new GameDetails(gameResponse.getTitle(), gameResponse.getGenre());
                })
                .collect(Collectors.toList());
    }

    @POST
    @Transactional
    public void createPlayer(Player player) {
        playerRepository.persist(player);
    }

    @POST
    @Path("/{username}/buy/{gameId}")
    @Transactional
    public void buyGame(@PathParam("username") String username, @PathParam("gameId") Long gameId) {
        Player player = playerRepository.findByUsername(username);
        if (player != null) {
            OwnedGame newGame = new OwnedGame(gameId, player);
            player.library.add(newGame);
            playerRepository.persist(player);
        }
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