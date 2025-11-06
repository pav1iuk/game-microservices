package com.example.clients;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@RegisterRestClient
@Path("/reviews")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ReviewServiceClient {
    @GET
    @Path("/game/{gameId}")
    List<Review> getReviewsForGame(@PathParam("gameId") Long gameId);
    @GET
    @Path("/game/{gameId}/player")
    Response getPlayerReviewForGame(@PathParam("gameId") Long gameId, @QueryParam("username") String username);
    @POST
    Review addReview(Review review);
}