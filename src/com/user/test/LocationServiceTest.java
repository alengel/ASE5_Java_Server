package com.user.test;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rest.comment.model.Comment;
import com.rest.comment.model.data.CommentData;
import com.rest.location.model.Location;
import com.rest.location.service.LocationService;
import com.rest.review.model.Review;
import com.rest.review.model.data.ReviewData;
import com.rest.user.model.data.UserData;
import com.rest.utils.DatabaseAccess;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.EmailAlreadyExistsException;
import com.rest.utils.exceptions.InputTooLongException;
import com.rest.utils.exceptions.InvalidKeyException;
import com.rest.utils.exceptions.PasswordWrongException;
import com.rest.utils.exceptions.UserNotFoundException;
import com.rest.utils.exceptions.WrongEmailFormatException;

public class LocationServiceTest {
	
	
	LocationService locationService;
	DatabaseAccess dbAccess;
	DatabaseAccess dbAccess1;
	
	
	@Before
	public void setUp() throws Exception {
		locationService = new LocationService();
		dbAccess = new DatabaseAccess();
		dbAccess1 = new DatabaseAccess();
		dbAccess.clearDatabase();
		
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/*
	 * Testing checking in with a correct login key
	 */
	@Test
	public void testCheckInNormal() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, UserNotFoundException, PasswordWrongException, SQLException, InvalidKeyException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		Response resultResp = locationService.checkIn(loginKey, "new_venueId");
		Location result = (Location)resultResp.getEntity();
		Location expected = new Location("true", "Checked in successfully");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing checking in using a wrong login key
	 */
	@Test
	public void testCheckInWrongKey() throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, SQLException, InvalidKeyException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		Response resultResp = locationService.checkIn(loginKey+"asd", "new_venueId");
		Location result = (Location)resultResp.getEntity();
		Location expected = new Location("false", "LoginKey is wrong");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing adding reviews with correct key
	 */
	@Test
	public void testAddReviewNormal() throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, SQLException, InvalidKeyException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException  {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		String venueId = "test_venue";
		String reviewTitle = "Good place";
		String reviewDescription = "Review description...";
		
		Response resultResp = locationService.sendReview(loginKey, venueId, 5, reviewTitle, reviewDescription, null);
		Location result = (Location)resultResp.getEntity();
		Location expected = new Location("true", "Review sent successfully");
		
		assertEquals(expected, result);
	}
	
	
	/*
	 * Testing adding review with wrong login key
	 */
	@Test
	public void testAddReviewWrongKey() throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, SQLException, InvalidKeyException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException  {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		String venueId = "test_venue";
		String reviewTitle = "Good place";
		String reviewDescription = "Review description...";
		
		Response resultResp = locationService.sendReview(loginKey+"asd", venueId, 5, reviewTitle, reviewDescription, null);
		Location result = (Location)resultResp.getEntity();
		Location expected = new Location("false", "LoginKey is wrong");
		
		assertEquals(expected, result);
	}
	
	
	/*
	 * Testing getting reviews by venue id
	 */
	@Test
	public void testGetReviewsByVenue() throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, InvalidKeyException, SQLException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException {
		

		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		String venueId = "test_venue";
		String reviewTitle = "Good place";
		String reviewDescription = "Review description...";
		
		dbAccess.storeNewReview(loginKey, venueId, 5, reviewTitle, reviewDescription, null);
		
		Response resultResp = locationService.getReviews(venueId);
		

		
		Review result = (Review)resultResp.getEntity();
		ArrayList<ReviewData> cd = (ArrayList<ReviewData>)result.getReviewData();
		Review expected = new Review("true", "List of reviews for this place", cd);
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing getting reviews by venue id, in case no reviews left
	 */
	@Test
	public void testGetReviewsByVenueNoReviews() throws SQLException, ArgumentMissingException, InvalidKeyException{
		
		String venueId = "new_venueId";

		Response resultResp = locationService.getReviews(venueId);
		
		Review result = (Review)resultResp.getEntity();
		Review expected = new Review("true", "Nobody left a review yet", null);
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing voting for existing review
	 */
	@Test
	public void testVote() throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, InvalidKeyException, SQLException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException {
		

		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		String venueId = "test_venue";
		String reviewTitle = "Good place";
		String reviewDescription = "Review description...";
		
		dbAccess.storeNewReview(loginKey, venueId, 5, reviewTitle, reviewDescription, null);
		
		Response resultResp = locationService.vote(loginKey, 1, 5);
		Location result = (Location)resultResp.getEntity();
		Location expected = new Location("true", "Voted successfully");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing voting for non-existing review
	 */
	@Test
	public void testVoteWrongReview() throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, SQLException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();

		Response resultResp = locationService.vote(loginKey, 007, 5);
		Location result = (Location)resultResp.getEntity();
		Location expected = new Location("false", "Review not found");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing voting with wrong login key
	 */
	@Test
	public void testVoteWrongKey() throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, InvalidKeyException, SQLException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException {
		

		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		String venueId = "test_venue";
		String reviewTitle = "Good place";
		String reviewDescription = "Review description...";
		
		dbAccess.storeNewReview(loginKey, venueId, 5, reviewTitle, reviewDescription, null);
		
		Response resultResp = locationService.vote(loginKey+"asd", 1, 5);
		Location result = (Location)resultResp.getEntity();
		Location expected = new Location("false", "LoginKey is wrong");
		
		assertEquals(expected, result);
	}

	/*
	 * Testing adding comments
	 */
	@Test
	public void testComments() throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, InvalidKeyException, SQLException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException {
		

		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		String venueId = "test_venue";
		String reviewTitle = "Good place";
		String reviewDescription = "Review description...";
		
		dbAccess.storeNewReview(loginKey, venueId, 5, reviewTitle, reviewDescription, null);
		
		Response resultResp = locationService.putComment(loginKey, 1, "comment...");
		Location result = (Location)resultResp.getEntity();
		Location expected = new Location("true", "Comment successful");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing adding comments for non-existing review
	 */
	@Test
	public void testCommentsWrongReview() throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, SQLException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException {
		

		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
	
		Response resultResp = locationService.putComment(loginKey, 007, "comment...");
		Location result = (Location)resultResp.getEntity();
		Location expected = new Location("false", "Review not found");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing adding comments using wrong login key
	 */
	@Test
	public void testCommentsWrongKey() throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, InvalidKeyException, SQLException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException {
		

		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		String venueId = "test_venue";
		String reviewTitle = "Good place";
		String reviewDescription = "Review description...";
		
		dbAccess.storeNewReview(loginKey, venueId, 5, reviewTitle, reviewDescription, null);
		
		Response resultResp = locationService.putComment(loginKey+"asd", 1, "comment...");
		Location result = (Location)resultResp.getEntity();
		Location expected = new Location("false", "LoginKey is wrong");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing getting comments for existing review
	 */
	@Test
	public void testGetComments() throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, InvalidKeyException, SQLException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException {
		

		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		String venueId = "test_venue";
		String reviewTitle = "Good place";
		String reviewDescription = "Review description...";
		
		dbAccess.storeNewReview(loginKey, venueId, 5, reviewTitle, reviewDescription, null);
		
		Response resultResp = locationService.getCommentsForReview(1);
		Comment result = (Comment)resultResp.getEntity();
		ArrayList<CommentData> cd = (ArrayList<CommentData>)result.getCd();
		Comment expected = new Comment("true", "", cd);
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing getting comments for non-existing review
	 */
	@Test
	public void testGetCommentsWrongReview() {
		
		Response resultResp = locationService.getCommentsForReview(007);
		Comment result = (Comment)resultResp.getEntity();
		ArrayList<CommentData> cd = (ArrayList<CommentData>)result.getCd();
		Comment expected = new Comment("false", "Review not found", cd);
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing getting reviews for an existing user
	 */
	@Test
	public void testGetReviewsByUser() throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, InvalidKeyException, SQLException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		String venueId = "test_venue";
		String reviewTitle = "Good place";
		String reviewDescription = "Review description...";
		
		dbAccess.storeNewReview(loginKey, venueId, 5, reviewTitle, reviewDescription, null);
	
		
		int id = Integer.parseInt(userData.getId());
		Response resultResp = locationService.getReviewsForUser(id);
		
		Review result = (Review)resultResp.getEntity();
		ArrayList<ReviewData> cd = (ArrayList<ReviewData>)result.getReviewData();
		Review expected = new Review("true", "", cd);
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing getting reviews for non-existing user
	 */
	@Test
	public void testGetReviewsByUserWrongId() {
	
		Response resultResp = locationService.getReviewsForUser(007);
		Review result = (Review)resultResp.getEntity();
		
		Review expected = new Review("false", "User not found", null);
		
		assertEquals(expected, result);
	}
	
	

}
