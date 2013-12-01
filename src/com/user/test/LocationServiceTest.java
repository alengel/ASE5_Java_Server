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
		
		Response result = locationService.checkIn(loginKey, "any_venueId");
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
		
		Response result = locationService.checkIn(loginKey+"asd", "random_venueId");
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
		
		Response result = locationService.sendReview(loginKey, "random_venueId", 5, "Good place", "Review description...", null);
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
		
		Response result = locationService.sendReview(loginKey+"asd", "random_venueId", 5, "Good place", "Review description...", null);
		Response expected = Response.ok(new Location("false", "LoginKey is wrong")).build();
		
		assertEquals(expected, result);
	}

	

}
