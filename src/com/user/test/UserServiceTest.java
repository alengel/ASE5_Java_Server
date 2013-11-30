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
	

}
