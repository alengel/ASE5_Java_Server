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
	
	/**
	 * This methods allows a user to check in the closest venue
	 * @param loginKey login key of the user (generated when he logs in)
	 * @param venueId foursquare venue id
	 * @return Http response object
	 * @throws SQLException
	 * @throws ArgumentMissingException
	 * @throws InvalidKeyException
	 */
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
	
	/**
	 * This method allows a user to leave a review about a place he visited
	 * @param loginKey login key of the user
	 * @param venueId foursquare venue id
	 * @param rating user rating of the place (1 to 5)
	 * @param reviewTitle
	 * @param reviewDescription
	 * @param imageUri venue picture
	 * @return Http response object
	 * @throws SQLException
	 * @throws ArgumentMissingException
	 * @throws InvalidKeyException
	 */
	@POST                                
	@Path("/reviews")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response sendReview(@FormParam ("key") String loginKey, @FormParam("venue_id") String venueId,  @FormParam("rating") int rating, @FormParam("review_title") String reviewTitle, @FormParam("review_description") String reviewDescription, @FormParam("location_image") String imageUri) throws SQLException, ArgumentMissingException, InvalidKeyException {
		dbAccess = new DatabaseAccess();
		String success;
		String message;
		try {
			if (dbAccess.storeNewReview(loginKey, venueId, rating, reviewTitle, reviewDescription, imageUri)) {
		
			success = "true";
			message = "Review sent successfully";
		} else {
			success = "false";
			message = "Failed to send the review";
		}
		return Response.ok(new Location(success, message)).build();
		} catch (InvalidKeyException e) {
			return Response.ok(new Location("false", "LoginKey is wrong")).build();
		}
		
	}
	
	/**
	 * This method returns all reviews for a chosen venue
	 * @param venueId foursquare venue id
	 * @return Http response object
	 * @throws SQLException
	 * @throws ArgumentMissingException
	 * @throws InvalidKeyException
	 */
	@GET                                
	@Path("venue/{venue_id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response getReviews(@PathParam ("venue_id") String venueId) throws SQLException, ArgumentMissingException, InvalidKeyException {
		dbAccess = new DatabaseAccess();
		Review review = dbAccess.getReviews(venueId);
		return Response.ok(review).build();
	}


	/**
	 * This method allows a user to vote for a review of another user
	 * @param key login key of the user
	 * @param reviewId id of the voted review
	 * @param vote vote(rating) of the review from 1 to 5
	 * @return Http response object
	 */
	@POST                                
	@Path("/vote")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response vote(@FormParam ("key") String key, @FormParam("review_id") int reviewId,  @FormParam("vote") int vote) {
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
			message = "LoginKey is wrong";
			e.printStackTrace();
		} catch (ReviewNotFoundException e) {
			success = "false";
			message = "Review not found";
		}
		return Response.ok(new Location(success, message)).build();
		
	}
	
	/**
	 * This method sends comments on reviews of other users
	 * @param key login key of the user
	 * @param reviewId id of commented review
	 * @param comment
	 * @return Http response object
	 */
	@POST                                
	@Path("/put-comment")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response putComment(@FormParam ("key") String key, @FormParam("review_id") int reviewId,  @FormParam("comment") String comment) {
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
			message = "LoginKey is wrong";
		} catch (ReviewNotFoundException e) {
			success = "false";
			message = "Review not found";
		}
		return Response.ok(new Location(success, message)).build();
		
	}
	
	
	/**
	 * This method returns comments for a given review
	 * @param reviewId
	 * @return Http response object
	 */
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
			message = "Review not found";
		}
		
		Comment comment = new Comment(success, message, comments);
		return Response.ok(comment).build();
	}
	
	
	/**
	 * This method returns all reviews a particular user left 
	 * @param userId id of the user whose reviews will be returned
	 * @return Http response object
	 */
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
