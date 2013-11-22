package com.rest.location.service;


import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.sql.*;
import com.rest.location.model.Location;
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
    public Response checkIn(@FormParam ("loginKey") String loginKey, @FormParam("venueId") String venueId) throws SQLException, ArgumentMissingException, InvalidKeyException {
		
		dbAccess = new DatabaseAccess();
		long timeStamp = System.currentTimeMillis()/1000L;
		Location location = dbAccess.checkIn(loginKey, venueId, ""+timeStamp);			
		
		
		return Response.ok(location).build();
	}
	
	@POST                                
	@Path("/reviews")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response sendReview(@FormParam ("loginKey") String loginKey, @FormParam("venueId") String venueId,  @FormParam("rating") int rating, @FormParam("reviewTitle") String reviewTitle, @FormParam("reviewDescription") String reviewDescription, @FormParam("imageUri") String imageUri) throws SQLException, ArgumentMissingException, InvalidKeyException {
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
	@Path("{venueId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response getReviews(@PathParam ("venueId") String venueId) throws SQLException, ArgumentMissingException, InvalidKeyException {
		dbAccess = new DatabaseAccess();
		Location location = dbAccess.getReviews(venueId, false);
		return Response.ok(location).build();
	}



}
