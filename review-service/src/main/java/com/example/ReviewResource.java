package com.example;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import java.util.Optional;

@Path("/reviews")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReviewResource {

    @Inject
    ReviewRepository repository;

    @GET
    @Path("/game/{gameId}")
    public List<Review> getReviewsForGame(@PathParam("gameId") Long gameId) {
        return repository.findByGameId(gameId);
    }
    @GET
    @Path("/game/{gameId}/player")
    public Response getPlayerReviewForGame(@PathParam("gameId") Long gameId, @QueryParam("username") String username) {
        Optional<Review> reviewOpt = repository.findByGameIdAndUsername(gameId, username);

        return reviewOpt
                .map(review -> Response.ok(review).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
    @POST
    public Review addReview(Review review) {
        return repository.save(review);
    }
}