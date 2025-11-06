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

        List<GameDetails> userGames = playerProfileClient.getPlayerLibrary(username);

        return dashboard
                .data("username", username)
                .data("games", userGames);
    }
}