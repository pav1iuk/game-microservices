package com.example.clients;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@RegisterRestClient
@Path("/reviews")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ReviewServiceClient {
    @GET
    @Path("/game/{gameId}")
    List<Review> getReviewsForGame(@PathParam("gameId") Long gameId);
    @POST
    Review addReview(Review review);
}