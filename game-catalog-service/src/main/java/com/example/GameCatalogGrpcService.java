package com.example;

import com.example.grpc.*;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class GameCatalogGrpcService implements GameCatalog {

    @Inject
    GameRepository repository;

    @Override
    public Uni<GameResponse> getGameById(GetGameRequest request) {
        Game game = repository.findById(request.getId());
        return Uni.createFrom().item(GameResponse.newBuilder()
                .setId(game.id)
                .setTitle(game.title)
                .setGenre(game.genre)
                .setDeveloper(game.developer)
                .build());
    }

    @Override
    public Uni<GetAllGamesResponse> getAllGames(GetAllGamesRequest request) {
        List<GameResponse> gameResponses = repository.findAll().stream()
                .map(game -> GameResponse.newBuilder() /* ... mapping ... */ .build())
                .collect(Collectors.toList());

        return Uni.createFrom().item(GetAllGamesResponse.newBuilder()
                .addAllGames(gameResponses)
                .build());
    }
}
