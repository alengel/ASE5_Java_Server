package com.rest.location.service;


import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.sql.*;
import java.util.ArrayList;

import com.rest.comment.model.Comment;
import com.rest.comment.model.data.CommentData;
import com.rest.location.model.Location;


import com.rest.review.model.Review;
import com.rest.review.model.data.ReviewData;
import com.rest.utils.*;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.InvalidKeyException;
import com.rest.utils.exceptions.ReviewNotFoundException;
import com.rest.utils.exceptions.UserNotFoundException;

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
		try {
			Location location = dbAccess.checkIn(loginKey, venueId, ""+timeStamp);
			return Response.ok(location).build();
		} catch (InvalidKeyException e) {
			return Response.ok(new Location("false", "LoginKey is wrong")).build();
		}
		
		
		
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


	
	@POST                                
	@Path("/vote")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response sendReview(@FormParam ("key") String key, @FormParam("review_id") int reviewId,  @FormParam("vote") int vote) {
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
    public Response sendReview(@FormParam ("key") String key, @FormParam("review_id") int reviewId,  @FormParam("comment") String comment) {
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
	@Path("reviews/{reviewId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response getCommentsForReview(@PathParam ("reviewId") int reviewId) {
		
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
	
	
	
	@GET                                
	@Path("reviews-for-user/{userId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response getReviewsForUser(@PathParam ("userId") int userId) {
		
		String success = "false";
		String message = "";
		
		dbAccess = new DatabaseAccess();
		ArrayList<ReviewData> reviews = null;
		
		try {
			reviews = dbAccess.getReviewsForUser(userId);
			success = "true";
		} catch (UserNotFoundException e) {
			message = "User not found";
		}
		
		
		Review review = new Review(success, message, reviews);
		
		return Response.ok(review).build();
	}
}
