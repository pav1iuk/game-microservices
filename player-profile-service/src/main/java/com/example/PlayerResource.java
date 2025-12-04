package com.example;

import com.example.clients.Review;
import com.example.clients.ReviewInput; // Переконайтесь, що цей клас існує (код нижче)
import com.example.clients.ReviewServiceClient;
import com.example.grpc.GameCatalog;
import com.example.grpc.GetAllGamesRequest;
import com.example.grpc.GetGameRequest;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.grpc.GrpcClient;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import com.example.events.GamePurchasedEvent;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Iterator;

@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlayerResource {

    @Inject
    PlayerRepository playerRepository;

    @Inject
    @RestClient
    ReviewServiceClient reviewClient;

    @GrpcClient("catalog")
    GameCatalog gameCatalog;

    @GET
    @Path("/{username}/library")
    public List<GameDetails> getPlayerLibrary(@PathParam("username") String username) {
        Player player = playerRepository.findByUsername(username);
        if (player == null) return List.of();

        return player.library.stream()
                .map(ownedGame -> {
                    var gameInfo = gameCatalog
                            .getGameById(GetGameRequest.newBuilder().setId(ownedGame.gameId).build())
                            .await().indefinitely();

                    Review myReview = null;
                    try {
                        var reviews = reviewClient.getReviewsForGame(ownedGame.gameId);
                        myReview = reviews.stream()
                                .filter(r -> r.username.equals(username))
                                .findFirst()
                                .orElse(null);
                    } catch (Exception e) {
                        System.out.println("Review service unavailable for game " + ownedGame.gameId);
                    }

                    // 3. Збираємо все разом
                    return new GameDetails(
                            gameInfo.getId(),
                            gameInfo.getTitle(),
                            gameInfo.getGenre(),
                            myReview
                    );
                })
                .collect(Collectors.toList());
    }

    @GET
    @Path("/available-games")
    public List<GameDetails> getAllAvailableGames() {
        var response = gameCatalog.getAllGames(GetAllGamesRequest.newBuilder().build())
                .await().indefinitely();

        return response.getGamesList().stream()
                .map(g -> new GameDetails(g.getId(), g.getTitle(), g.getGenre(), null))
                .collect(Collectors.toList());
    }

    @POST
    @Transactional
    public void createPlayer(Player player) {
        playerRepository.persist(player);
    }
    @Inject
    @Channel("game-purchases") // Назва каналу
    Emitter<GamePurchasedEvent> purchaseEmitter;


    @POST
    @Path("/{username}/buy/{gameId}")
    @Transactional
    public void buyGame(@PathParam("username") String username, @PathParam("gameId") Long gameId) {
        Player player = playerRepository.findByUsername(username);
        if (player != null) {
            boolean alreadyOwned = player.library.stream().anyMatch(g -> g.gameId.equals(gameId));

            if (!alreadyOwned) {
                // 1. Зберігаємо в БД (Синхронно)
                OwnedGame newGame = new OwnedGame(gameId, player);
                player.library.add(newGame);
                playerRepository.persist(player);

                // 2. Відправляємо повідомлення в Kafka (Асинхронно)
                GamePurchasedEvent event = new GamePurchasedEvent(
                        username,
                        gameId,
                        LocalDateTime.now().toString()
                );
                purchaseEmitter.send(event);
                System.out.println("Event sent to Kafka: " + event.username);
            }
        }
    }
    @POST
    @Path("/{username}/games/{gameId}/reviews")
    @Transactional
    public Review addPlayerReview(@PathParam("username") String username,
                                  @PathParam("gameId") Long gameId,
                                  ReviewInput reviewInput) {
        Review review = new Review();
        review.gameId = gameId;
        review.username = username;
        review.rating = reviewInput.rating;
        review.text = reviewInput.text;

        return reviewClient.addReview(review);
    }
    @DELETE
    @Path("/{username}/library/{gameId}")
    @Transactional
    public void removeGame(@PathParam("username") String username, @PathParam("gameId") Long gameId) {
        Player player = playerRepository.findByUsername(username);

        if (player != null) {
            // Використовуємо ітератор для безпечного видалення елемента під час перебору
            Iterator<OwnedGame> iterator = player.library.iterator();
            while (iterator.hasNext()) {
                OwnedGame game = iterator.next();
                if (game.gameId.equals(gameId)) {
                    iterator.remove(); // Видаляє зі списку
                    break;
                }
            }
            // Hibernate автоматично виконає SQL DELETE при завершенні транзакції
            // завдяки orphanRemoval=true у класі Player
            playerRepository.persist(player);
        }
    }
}

class GameDetails {
    public Long id;
    public String title;
    public String genre;
    public Review review;

    public GameDetails(Long id, String title, String genre, Review review) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.review = review;
    }
    public GameDetails() {
    }
}