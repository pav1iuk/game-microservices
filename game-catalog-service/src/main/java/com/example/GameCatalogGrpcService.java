package com.example;

import com.example.grpc.*;
import io.quarkus.grpc.GrpcService;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class GameCatalogGrpcService implements GameCatalog {

    @Override
    @Blocking
    public Uni<GameResponse> getGameById(GetGameRequest request) {
        Game game = Game.findById(request.getId());

        if (game == null) {
            return Uni.createFrom().failure(new RuntimeException("Game not found"));
        }

        return Uni.createFrom().item(GameResponse.newBuilder()
                .setId(game.id)
                .setTitle(game.title)
                .setGenre(game.genre)
                .setDeveloper(game.developer)
                .build());
    }

    @Override
    @Blocking
    public Uni<GetAllGamesResponse> getAllGames(GetAllGamesRequest request) {
        List<Game> games = Game.listAll();

        List<GameResponse> gameResponses = games.stream()
                .map(game -> GameResponse.newBuilder()
                        .setId(game.id)
                        .setTitle(game.title)
                        .setGenre(game.genre)
                        .setDeveloper(game.developer)
                        .build())
                .collect(Collectors.toList());

        return Uni.createFrom().item(GetAllGamesResponse.newBuilder()
                .addAllGames(gameResponses)
                .build());
    }
}