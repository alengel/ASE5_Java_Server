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
		dbAccess.clearDatabase();
		
	}

	@After
	public void tearDown() throws Exception {
	}
	
	/*
	 * Testing the login method with correct name and password
	 */
	
	@Test
	public void testLoginUserNormal() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, EmailAlreadyExistsException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		dbAccess.registerNewUser(email, passwd, firstName, lastName, null);
		
		Response respResult = userService.login(email, passwd);
		User result = (User)respResult.getEntity();
		String loginKey = result.getLoginKey();
		List<UserData> ud = result.getUserData();
		UserData userData = ud.get(0);
		User expected = new User("true", loginKey, userData);
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing logging in with wrong email
	 */
	@Test
	public void testLoginUserWrongEmail() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		userService.register(null, email, passwd, firstName, lastName, null);
		
		Response resultResp = userService.login(email+"123qwe", passwd);
		User result = (User)resultResp.getEntity();

		User expected = new User("false", "Email and/or password not found");
		
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
		
		userService.register(null, email, passwd, firstName, lastName, null);
		
		Response resultResp = userService.login(email, passwd+ "123qwe");
		User result = (User)resultResp.getEntity();
		
		User expected = new User("false", "Email and/or password not found");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing  logging out using correct login key
	 */
	
	@Test
	public void testLogoutUserNormal() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException {
		

		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		userService.register(null, email, passwd, firstName, lastName, null);
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		Response resultResp = userService.logout(loginKey);
		User result = (User)resultResp.getEntity();
		
		User expected = new User("true", "Logged out successfully");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing logging out using wrong login key
	 */
	
	@Test
	public void testLogoutUserWrongKey() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException {
		

		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		userService.register(null, email, passwd, firstName, lastName, null);
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		Response resultResp = userService.logout(loginKey+"asd");
		User result = (User)resultResp.getEntity();
		
		User expected = new User("false", "Error occured");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing update settings mathod with correct loginkey
	 */
	@Test
	public void testUpdateSettings() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		userService.register(null, email, passwd, firstName, lastName, null);
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		Response resultResp = userService.updateSettings(loginKey, 100, 100, 100);
		User result = (User)resultResp.getEntity();
		User expected = new User("true", "Settings are updated successfully");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing update settings method with wrong loginkey
	 */
	@Test
	public void testUpdateSettingsWrongKey() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		userService.register(null, email, passwd, firstName, lastName, null);
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		Response resultResp = userService.updateSettings(loginKey+"asd", 100, 100, 100);
		User result = (User)resultResp.getEntity();
		User expected = new User("false", "LoginKey is wrong");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing follow user method with correct data
	 */
	@Test
	public void testFollow() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, EmailAlreadyExistsException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		userService.register(null, email, passwd, firstName, lastName, null);
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		//for new user whom we will follow
		String emailNewUser = "newuser@mai.com";
		String passwdNewUser = "asdfgh";		
		
		// register new user to make sure reviwer_id exists
		dbAccess.registerNewUser(emailNewUser, passwdNewUser, "Jon", "Smith", null );
		UserData userData1 = dbAccess.loginUser(emailNewUser, passwdNewUser);
		int reviewerId = Integer.parseInt(userData1.getId());
		
		Response resultResp = userService.follow(loginKey, reviewerId);
		User result = (User)resultResp.getEntity();
		User expected = new User("true", "");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing follow a user using a wrong key
	 */
	@Test
	public void testFollowWrongKey() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, EmailAlreadyExistsException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		userService.register(null, email, passwd, firstName, lastName, null);
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		//for new user whom we will follow
		String emailNewUser = "newuser@mai.com";
		String passwdNewUser = "asdfgh";		
		
		// register new user to make sure reviwer_id exists
		dbAccess.registerNewUser(emailNewUser, passwdNewUser, "Jon", "Smith", null );
		UserData userData1 = dbAccess.loginUser(emailNewUser, passwdNewUser);
		int reviewerId = Integer.parseInt(userData1.getId());
		
		Response resultResp = userService.follow(loginKey+"asd", reviewerId);
		User result = (User)resultResp.getEntity();
		User expected = new User("false", "LoginKey is wrong");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing follow a user method using a wrong reviewer id
	 */
	@Test
	public void testFollowWrongReviewerId() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, EmailAlreadyExistsException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		userService.register(null, email, passwd, firstName, lastName, null);
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
	
		Response resultResp = userService.follow(loginKey, 007);
		User result = (User)resultResp.getEntity();
		User expected = new User("false", "User not found");
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing get user profile method using a correct login key
	 */
	@Test
	public void testProfile() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, EmailAlreadyExistsException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		userService.register(null, email, passwd, firstName, lastName, null);
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
	
		Response resultResp = userService.getUserProfile(loginKey);
		
		User result = (User)resultResp.getEntity();
		List<UserData> ud = result.getUserData();
		UserData userData1 = ud.get(0);
		String loginKey1 = userData1.getLoginKey();
		User expected = new User("true", loginKey1, userData1);
		
		assertEquals(expected, result);
	}
	
	/*
	 * Testing get user profile method using a wrong login key
	 */
	@Test
	public void testProfileWrongKey() throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException, UserNotFoundException, PasswordWrongException, EmailAlreadyExistsException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		userService.register(null, email, passwd, firstName, lastName, null);
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
	
		Response resultResp = userService.getUserProfile(loginKey+"asd");
		User result = (User)resultResp.getEntity();
				
		User expected = new User("false", "LoginKey is wrong");
		
		assertEquals(expected, result);
	}
	
	
	
	
	

}
