package com.example;

import com.example.clients.GameDetails;
import com.example.clients.PlayerProfileClient;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import com.example.clients.ReviewInput;
import java.util.List;

@Path("/")
public class DashboardResource {

    @Inject
    Template dashboard;

    @Inject
    SecurityContext securityContext;

    @Inject
    @RestClient
    PlayerProfileClient playerProfileClient;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getDashboard() {
        String username = securityContext.getUserPrincipal().getName();

        // 1. Отримуємо ігри користувача
        List<GameDetails> myGames = playerProfileClient.getPlayerLibrary(username);

        // 2. Отримуємо всі ігри для магазину
        List<GameDetails> allGames = playerProfileClient.getAllGames();

        // 3. Передаємо обидва списки в шаблон
        return dashboard
                .data("username", username)
                .data("games", myGames)
                .data("store", allGames); // <-- Нові дані
    }

    // Обробка кнопки "Купити"
    @POST
    @Path("/buy")
    public Response buyGame(@FormParam("gameId") Long gameId) {
        String username = securityContext.getUserPrincipal().getName();

        // Викликаємо бекенд, щоб зберегти в базу
        playerProfileClient.buyGame(username, gameId);

        // Перезавантажуємо сторінку
        return Response.seeOther(URI.create("/")).build();
    }
    @POST
    @Path("/review")
    public Response addReview(@FormParam("gameId") Long gameId,
                              @FormParam("rating") int rating,
                              @FormParam("text") String text) {

        String username = securityContext.getUserPrincipal().getName();

        ReviewInput input = new ReviewInput(rating, text);

        playerProfileClient.addPlayerReview(username, gameId, input);

        return Response.seeOther(URI.create("/")).build();
    }

    @POST // HTML форми підтримують тільки GET і POST, тому використовуємо POST для виклику дії
    @Path("/remove")
    public Response removeGame(@FormParam("gameId") Long gameId) {
        String username = securityContext.getUserPrincipal().getName();

        // Викликаємо бекенд для видалення
        playerProfileClient.removeGame(username, gameId);

        // Перезавантажуємо сторінку
        return Response.seeOther(URI.create("/")).build();
    }

}