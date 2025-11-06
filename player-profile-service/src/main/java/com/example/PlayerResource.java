package com.example;

import com.example.grpc.GameCatalog;
import com.example.grpc.GetGameRequest;
import io.quarkus.grpc.GrpcClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;
import com.example.clients.Review;
import com.example.clients.ReviewServiceClient;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.ws.rs.core.Response;

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlayerResource {

    @Inject
    PlayerRepository playerRepository;

    @GrpcClient("catalog")
    GameCatalog gameCatalog;

    @Inject
    @RestClient
    ReviewServiceClient reviewServiceClient;

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

    @POST
    @Path("/{username}/games/{gameId}/reviews")
    public Review addPlayerReview(@PathParam("username") String username,
                                  @PathParam("gameId") Long gameId,
                                  ReviewInput reviewInput) {

        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            throw new NotFoundException("Player not found: " + username);
        }

        try {
            gameCatalog.getGameById(GetGameRequest.newBuilder().setId(gameId).build())
                    .await().indefinitely();
        } catch (Exception e) {
            throw new NotFoundException("Game not found: " + gameId);
        }

        Review reviewToSend = new Review(gameId, username, reviewInput.rating, reviewInput.text);

        return reviewServiceClient.addReview(reviewToSend);
    }
    @GET
    @Path("/{username}/library/{gameId}")
    public GameWithReviewDetails getGameWithReview(@PathParam("username") String username,
                                                   @PathParam("gameId") Long gameId) {

        Player player = playerRepository.findByUsername(username);
        var gameResponse = gameCatalog
                .getGameById(GetGameRequest.newBuilder().setId(gameId).build())
                .await().indefinitely();

        Review playerReview = null;
        try (Response reviewResponse = reviewServiceClient.getPlayerReviewForGame(gameId, username)) {
            if (reviewResponse.getStatus() == Response.Status.OK.getStatusCode()) {
                playerReview = reviewResponse.readEntity(Review.class);
            }
        } catch (Exception e) {
            System.err.println("Error fetching review: " + e.getMessage());
        }

        return new GameWithReviewDetails(
                gameResponse.getId(),
                gameResponse.getTitle(),
                gameResponse.getGenre(),
                gameResponse.getDeveloper(),
                playerReview
        );
    }
}
class GameWithReviewDetails {
    public Long gameId;
    public String title;
    public String genre;
    public String developer;
    public Review review;

    public GameWithReviewDetails(Long gameId, String title, String genre, String developer, Review review) {
        this.gameId = gameId;
        this.title = title;
        this.genre = genre;
        this.developer = developer;
        this.review = review;
    }
}
class ReviewInput {
    public int rating;
    public String text;
}
class GameDetails {
    public String title;
    public String genre;
    public GameDetails(String title, String genre) {
        this.title = title;
        this.genre = genre;
    }
}