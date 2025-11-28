package com.example;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/reviews")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReviewResource {

    @GET
    @Path("/game/{gameId}")
    public List<Review> getReviewsForGame(@PathParam("gameId") Long gameId) {
        return Review.list("gameId", gameId);
    }

    @POST
    @Transactional
    public Review addReview(Review review) {
        review.persist();
        return review;
    }
}