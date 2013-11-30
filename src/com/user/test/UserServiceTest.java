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
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		String passwd = "Hi98786";
		
		UserData userData = dbAccess.loginUser(email, passwd);
		String loginKey = userData.getLoginKey();
		
		Response result = userService.logout(loginKey);
		Response expected = Response.ok(new User("true", "Logged out successfully")).build();
		
		assertEquals(expected, result);
	}
	
	

}
