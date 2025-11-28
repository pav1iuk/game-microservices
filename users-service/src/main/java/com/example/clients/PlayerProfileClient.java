package com.example.clients;

import io.quarkus.oidc.token.propagation.AccessToken;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "player-profile")
@AccessToken
@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PlayerProfileClient {
    @POST
    @Path("/{username}/games/{gameId}/reviews")
    Review addPlayerReview(@PathParam("username") String username,
                           @PathParam("gameId") Long gameId,
                           ReviewInput reviewInput);
    @GET
    @Path("/{username}/library/{gameId}")
    GameWithReviewDetails getGameWithReview(@PathParam("username") String username,
                                            @PathParam("gameId") Long gameId);
    @GET
    @Path("/{username}/library")
    List<GameDetails> getPlayerLibrary(@PathParam("username") String username);

    // Отримати всі ігри для магазину
    @GET
    @Path("/available-games")
    List<GameDetails> getAllGames();

    // Купити гру
    @POST
    @Path("/{username}/buy/{gameId}")
    void buyGame(@PathParam("username") String username, @PathParam("gameId") Long gameId);
}