package com.rest.location.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 
 * Specification of the API. Contains every method that can be called by a client
 *
 */
public interface LocationServiceInterface {	
		
	
	/** checks in
	 * 
	 * @param loginKey
	 * @param venueId
	 * @param venueName
	 * @return Response object with a User object with a param success: true/false
	 */
	@POST                                
	@Path("/checkin")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response checkIn(@FormParam ("loginKey") String loginKey, @FormParam("venueId") String venueId, @FormParam("venueName") String venueName);

	/**
	 * 
	 * @param venueId
	 * @param page
	 * @param loginKey
	 * @return a list of users and reviews and ratings for venueId
	 */
	@GET                                
	@Path("/reviews")                      
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)		
    public Response getReviews(@FormParam ("venueId") String venueId, @FormParam("page") int page, @FormParam ("loginKey") String loginKey);
	
	/**
	 * 
	 * @param venueId
	 * @param raiting
	 * @param review
	 * @param loginKey
	 * @return success: true/false
	 */	
	@POST                     
	@Path("/reviews")                      
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)		
    public Response addReview(@FormParam ("venueId") String venueId, @FormParam("rating") int raiting, @FormParam ("review") String review, @FormParam ("loginKey") String loginKey);
	
	
	@POST                                
	@Path("/vote")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response sendReview(@FormParam ("key") String key, @FormParam("reviewId") String reviewId,  @FormParam("vote") int vote);
	
	@POST                                
	@Path("/put-comment")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response sendReview(@FormParam ("key") String key, @FormParam("reviewId") String reviewId,  @FormParam("comment") String comment);

}

