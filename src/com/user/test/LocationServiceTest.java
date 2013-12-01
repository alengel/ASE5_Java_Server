package com.user.test;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rest.location.model.Location;
import com.rest.location.service.LocationService;
import com.rest.review.model.Review;
import com.rest.review.model.data.ReviewData;
import com.rest.user.model.User;
import com.rest.user.model.data.UserData;
import com.rest.user.service.UserService;
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
	
	
	@Before
	public void setUp() throws Exception {
		locationService = new LocationService();
		dbAccess = new DatabaseAccess();
		
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/*
	 * Testing checking in with a correct login key
	 */
	@Test
	public void testCheckInNormal() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, InvalidKeyException, EmailAlreadyExistsException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		Response result = locationService.checkIn(loginKey, "new_venueId");
		Response expected = Response.ok(new Location("true", "Checked in successfully")).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing checking in using a wrong login key
	 */
	@Test
	public void testCheckInWrongKey() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, InvalidKeyException, EmailAlreadyExistsException {
		
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		Response result = locationService.checkIn(loginKey+"asd", "new_venueId");
		Response expected = Response.ok(new Location("false", "LoginKey is wrong")).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing adding reviews with correct key
	 */
	@Test
	public void testAddReviewNormal() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, InvalidKeyException, EmailAlreadyExistsException {
		
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		String venueId = "test_venue";
		String reviewTitle = "Good place";
		String reviewDescription = "Review description...";
		
		Response result = locationService.sendReview(loginKey, venueId, 5, reviewTitle, reviewDescription, null);
		Response expected = Response.ok(new Location("true", "Review sent successfully")).build();
		
		assertEquals(expected, result);
	}
	
	
	/*
	 * Testing adding review with wrong login key
	 */
	@Test
	public void testAddReviewWrongKey() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, InvalidKeyException, EmailAlreadyExistsException {
		
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		String venueId = "test_venue";
		String reviewTitle = "Good place";
		String reviewDescription = "Review description...";
		
		Response result = locationService.sendReview(loginKey+"asd", venueId, 5, reviewTitle, reviewDescription, null);
		Response expected = Response.ok(new Location("false", "LoginKey is wrong")).build();
		
		assertEquals(expected, result);
	}
	
	
	/*
	 * Testing getting reviews by venue id
	 */
	@Test
	public void testGetReviewsByVenue() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, InvalidKeyException, EmailAlreadyExistsException {
		

		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		String venueId = "test_venue";
		String reviewTitle = "Good place";
		String reviewDescription = "Review description...";
		
		dbAccess.storeNewReview(loginKey, venueId, 5, reviewTitle, reviewDescription, null);
		
		
		Response result = locationService.getReviews(venueId);
		List<ReviewData> rd = (List<ReviewData>)result.getEntity();
		Response expected = Response.ok(new Review("true", "List of reviews for this place", rd)).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing getting reviews by venue id, in case no reviews left
	 */
	@Test
	public void testGetReviewsByVenueNoReviews() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, InvalidKeyException, EmailAlreadyExistsException {
		
		String venueId = "new_venueId";

		Response result = locationService.getReviews(venueId);
		Response expected = Response.ok(new Review("true", "Nobody left a review yet", null)).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing voting for existing review
	 */
	@Test
	public void testVote() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, InvalidKeyException, EmailAlreadyExistsException {
		

		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		String venueId = "test_venue";
		String reviewTitle = "Good place";
		String reviewDescription = "Review description...";
		
		dbAccess.storeNewReview(loginKey, venueId, 5, reviewTitle, reviewDescription, null);
		
		Response result = locationService.vote(loginKey, 1, 5);
		Response expected = Response.ok(new Location("true", "Voted successfully")).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing voting for non-existing review
	 */
	@Test
	public void testVoteWWrongReview() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, InvalidKeyException, EmailAlreadyExistsException {
		

		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();

		Response result = locationService.vote(loginKey, 007, 5);
		Response expected = Response.ok(new Location("false", "Review not found")).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing voting with wrong login key
	 */
	@Test
	public void testVoteWrongKey() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, InvalidKeyException, EmailAlreadyExistsException {
		

		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		String venueId = "test_venue";
		String reviewTitle = "Good place";
		String reviewDescription = "Review description...";
		
		dbAccess.storeNewReview(loginKey, venueId, 5, reviewTitle, reviewDescription, null);
		
		Response result = locationService.vote(loginKey+"asd", 1, 5);
		Response expected = Response.ok(new Location("false", "Invalid key")).build();
		
		assertEquals(expected, result);
	}

	

}
