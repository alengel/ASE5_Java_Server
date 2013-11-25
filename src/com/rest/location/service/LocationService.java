package com.rest.location.service;


import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.sql.*;

import com.rest.location.model.Location;
import com.rest.review.model.Review;
import com.rest.utils.*;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.InvalidKeyException;

@Path("/")  	
public class LocationService {
	private DatabaseAccess dbAccess;
	
	@POST                                
	@Path("/check-in")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response checkIn(@FormParam ("key") String loginKey, @FormParam("venue_id") String venueId) throws SQLException, ArgumentMissingException, InvalidKeyException {
		
		dbAccess = new DatabaseAccess();
		long timeStamp = System.currentTimeMillis()/1000L;
		Location location = dbAccess.checkIn(loginKey, venueId, ""+timeStamp);			
		
		
		return Response.ok(location).build();
	}
	
	@POST                                
	@Path("/reviews")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response sendReview(@FormParam ("key") String loginKey, @FormParam("venue_id") String venueId,  @FormParam("rating") int rating, @FormParam("review_title") String reviewTitle, @FormParam("review_description") String reviewDescription, @FormParam("location_image") String imageUri) throws SQLException, ArgumentMissingException, InvalidKeyException {
		dbAccess = new DatabaseAccess();
		String success;
		String message;
		if (dbAccess.storeNewReview(loginKey, venueId, rating, reviewTitle, reviewDescription, imageUri)) {
			success = "true";
			message = "Review sent successfully";
		} else {
			success = "false";
			message = "Failed to send the review";
		}
		return Response.ok(new Location(success, message)).build();
		
	}
	
	@GET                                
	@Path("venue/{venue_id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response getReviews(@PathParam ("venue_id") String venueId) throws SQLException, ArgumentMissingException, InvalidKeyException {
		dbAccess = new DatabaseAccess();
		Review review = dbAccess.getReviews(venueId);
		return Response.ok(review).build();
	}



}
