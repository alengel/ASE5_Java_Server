package com.user.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.core.Response;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rest.user.model.User;
import com.rest.user.model.data.UserData;
import com.rest.user.service.UserService;
import com.rest.utils.DatabaseAccess;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.EmailAlreadyExistsException;
import com.rest.utils.exceptions.InputTooLongException;
import com.rest.utils.exceptions.PasswordWrongException;
import com.rest.utils.exceptions.UserNotFoundException;
import com.rest.utils.exceptions.WrongEmailFormatException;

public class UserServiceTest {
	
	UserService userService;
	DatabaseAccess dbAccess;
	
	
	@Before
	public void setUp() throws Exception {
		userService = new UserService();
		dbAccess = new DatabaseAccess();
		
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/*
	 * Testing user login normal
	 */
	
	@Test
	public void testLoginUserNormal() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		userService.register(null, email, passwd, firstName, lastName, null);
		
		Response result = userService.login(email, passwd);
		User user = (User)result.getEntity();
		String loginKey = user.getLoginKey();
		List<UserData> ud = user.getUserData();
		UserData userData = ud.get(0);
		Response expected = Response.ok(new User("true", loginKey, userData)).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing logging in with wrong emil
	 */
	@Test
	public void testLoginUserWrongEmail() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		userService.register(null, email, passwd, firstName, lastName, null);
		
		Response result = userService.login(email+"123qwe", passwd);

		Response expected = Response.ok(new User("false", "Email and/or password not found")).build();
		
		assertEquals(expected, result);
	}
	
	
	/*
	 * Testing logging in with wrong password
	 */
	@Test
	public void testLoginUserWrongPassword() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		
		Response result = userService.login(email, passwd+ "123qwe");

		Response expected = Response.ok(new User("false", "Email and/or password not found")).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing user logout normal
	 */
	
	@Test
	public void testLogoutUserNormal() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException {
		

		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		Response result = userService.logout(loginKey);
		Response expected = Response.ok(new User("true", "Logged out successfully")).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing logout with wrong key
	 */
	
	@Test
	public void testLogoutUserWrongKey() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException {
		

		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		Response result = userService.logout(loginKey+"asd");
		Response expected = Response.ok(new User("false", "Error occured")).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing update settings normal
	 */
	@Test
	public void testUpdateSettings() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException {
		
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		Response result = userService.updateSettings(loginKey, 100, 100, 100);
		Response expected = Response.ok(new User("true", "Settings are updated successfully")).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing update settings wrong loginkey
	 */
	@Test
	public void testUpdateSettingsWrongKey() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException {
		
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		Response result = userService.updateSettings(loginKey+"asd", 100, 100, 100);
		Response expected = Response.ok(new User("false", "LoginKey is wrong")).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing follow user
	 */
	@Test
	public void testFollow() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, EmailAlreadyExistsException {
		
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		//for new user whom we will follow
		String emailNewUser = "newuser@mai.com";
		String passwdNewUser = "asdfgh";		
		
		// register new user to make sure reviwer_id exists
		dbAccess.registerNewUser(emailNewUser, passwdNewUser, "Jon", "Smith", null );
		UserData userData1 = dbAccess.loginUser(emailNewUser, passwdNewUser);
		int reviewerId = Integer.parseInt(userData1.getId());
		
		Response result = userService.follow(loginKey, reviewerId);
		Response expected = Response.ok(new User("true", "")).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing follow user with wrong key
	 */
	@Test
	public void testFollowWrongKey() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, EmailAlreadyExistsException {
		
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		//for new user whom we will follow
		String emailNewUser = "newuser@mai.com";
		String passwdNewUser = "asdfgh";		
		
		// register new user to make sure reviwer_id exists
		dbAccess.registerNewUser(emailNewUser, passwdNewUser, "Jon", "Smith", null );
		UserData userData1 = dbAccess.loginUser(emailNewUser, passwdNewUser);
		int reviewerId = Integer.parseInt(userData1.getId());
		
		Response result = userService.follow(loginKey+"asd", reviewerId);
		Response expected = Response.ok(new User("false", "LoginKey is wrong")).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing follow user wrong reviewer id
	 */
	@Test
	public void testFollowWrongReviewerId() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, EmailAlreadyExistsException {
		
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
	
		Response result = userService.follow(loginKey, 007);
		Response expected = Response.ok(new User("false", "User not found")).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing get user profile normal
	 */
	@Test
	public void testProfile() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, EmailAlreadyExistsException {
		
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
	
		Response result = userService.getUserProfile(loginKey);
		
		User user = (User)result.getEntity();
		
		Response expected = Response.ok(user).build();
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing get user profile wrong login key
	 */
	@Test
	public void testProfileWrongKey() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, EmailAlreadyExistsException {
		
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
	
		Response result = userService.getUserProfile(loginKey+"asd");
				
		Response expected = Response.ok(new User("false", "Key not found")).build();
		
		assertEquals(expected, result);
	}
	
	
	
	
	

}
