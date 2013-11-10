package com.user.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rest.user.model.data.UserData;
import com.rest.utils.DatabaseAccess;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.InputTooLongException;
import com.rest.utils.exceptions.WrongEmailFormatException;

public class DatabaseAccessTest {
	
	DatabaseAccess dbAccess; //the DatabaseAccess object to be tested
	
	@Before
	public void setUp() throws Exception {
		dbAccess = new DatabaseAccess();
		
		dbAccess.clearDatabase();
	}
	
	

	@After
	public void tearDown() throws Exception {
	}
	
	/*
	 * Testing registerNewUser
	 */
	@Test
	public void testRegisterNewUserNormal() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
		
		String email = "test@web.de";
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		UserData result = dbAccess.registerNewUser(email, password, firstName, lastName);
		UserData expected = new UserData(email, password, firstName, lastName, null, null, null, null, null, null, null);
		assertEquals(expected, result);
		
		dbAccess.clearDatabase();
		
		result  = dbAccess.registerNewUser(email, password, null, lastName);
		expected = new UserData(email, password, null, lastName, null, null, null, null, null, null, null);
		assertEquals(expected, result);
		
		dbAccess.clearDatabase();
		
		result  = dbAccess.registerNewUser(email, password, firstName, null);
		expected = new UserData(email, password, firstName, null, null, null, null, null, null, null, null);
		assertEquals(expected, result);
		
		dbAccess.clearDatabase();
		
		result  = dbAccess.registerNewUser(email, password, null, null);
		expected = new UserData(email, password, null, null, null, null, null, null, null, null, null);
		assertEquals(expected, result);
	}
	
	@Test(expected = ArgumentMissingException.class)
	public void testRegisterNewUserNoEmail() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(null, password, firstName, lastName);
	}
	
	@Test(expected = ArgumentMissingException.class)
	public void testRegisterNewUserEmailEmpty() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser("", password, firstName, lastName);
	}
	
	@Test(expected = ArgumentMissingException.class)
	public void testRegisterNewUserNoPassword() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		
		dbAccess.registerNewUser(email, null, firstName, lastName);
	}
	
	@Test(expected = ArgumentMissingException.class)
	public void testRegisterNewUserPasswordEmpty() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		
		dbAccess.registerNewUser(email, "", firstName, lastName);
	}
	
	@Test(expected = WrongEmailFormatException.class)
	public void testRegisterNewUserWrongEmailFormat() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName);
	}
	
	@Test(expected = WrongEmailFormatException.class)
	public void testRegisterNewUserWrongEmailFormat2() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@ web.de";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName);
	}
	
	@Test(expected = InputTooLongException.class)
	public void testRegisterNewUserInputTooLong() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
		
		String email = "test@web.de";
		String firstName = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec.";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		UserData result = dbAccess.registerNewUser(email, password, firstName.replaceAll(" ", ""), lastName);
		UserData expected = new UserData(null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(expected, result);
	}
	
	@Test(expected = InputTooLongException.class)
	public void testRegisterNewUserInputTooLong2() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
		
		String email = "test@web.de";
		String lastName = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec.";
		String firstName = "Schliski";
		String password = "Hi98786";
		
		UserData result = dbAccess.registerNewUser(email, password, firstName.replaceAll(" ", ""), lastName.replaceAll(" ", ""));
		UserData expected = new UserData(null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(expected, result);
	}

}
