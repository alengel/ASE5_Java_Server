package com.rest.location.service;


import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.sql.*;
import java.util.ArrayList;

import com.rest.comment.model.Comment;
import com.rest.comment.model.data.CommentData;
import com.rest.location.model.Location;
import com.rest.utils.*;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.InvalidKeyException;
import com.rest.utils.exceptions.ReviewNotFoundException;

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


	
	@POST                                
	@Path("/vote")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response sendReview(@FormParam ("key") String key, @FormParam("reviewId") String reviewId,  @FormParam("vote") int vote) {
		dbAccess = new DatabaseAccess();
		String success;
		String message;
		try {
			if (dbAccess.vote(key, reviewId, vote)) {
				success = "true";
				message = "Voted successfully";
			} else {
				success = "false";
				message = "Failed to vote";
			}
		} catch (InvalidKeyException e) {
			success = "false";
			message = "Invalid key";
			e.printStackTrace();
		} catch (ReviewNotFoundException e) {
			success = "false";
			message = "Review not found";
		}
		return Response.ok(new Location(success, message)).build();
		
	}
	
	
	@POST                                
	@Path("/put-comment")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response sendReview(@FormParam ("key") String key, @FormParam("reviewId") String reviewId,  @FormParam("comment") String comment) {
		dbAccess = new DatabaseAccess();
		String success;
		String message;
		try {
			if (dbAccess.putComment(key, reviewId, comment)) {
				success = "true";
				message = "Comment successful";
			} else {
				success = "false";
				message = "Failed to comment";
			}
		} catch(InvalidKeyException e) {
			success = "false";
			message = "Key not found";
		} catch (ReviewNotFoundException e) {
			success = "false";
			message = "Review not found";
		}
		return Response.ok(new Location(success, message)).build();
		
	}
	
	
	
	@GET                                
	@Path("{reviewId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response getCommentsForReview(@PathParam ("reviewId") String reviewId) {
		
		String success = "false";
		String message = "";
		
		dbAccess = new DatabaseAccess();
		ArrayList<CommentData> comments = null;
		try {
			comments = dbAccess.getCommentsForReview(reviewId);
			success = "true";
		} catch (ReviewNotFoundException e) {
			message = "Review not found.";
		}
		
		Comment comment = new Comment(success, message, comments);
		return Response.ok(comment).build();
	}
}
