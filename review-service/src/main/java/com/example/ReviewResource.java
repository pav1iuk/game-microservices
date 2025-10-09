package com.example;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

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

    @POST
    public Review addReview(Review review) {
        return repository.save(review);
    }
}