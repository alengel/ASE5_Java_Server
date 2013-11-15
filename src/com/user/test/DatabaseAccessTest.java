package com.user.test;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rest.location.model.Location;
import com.rest.user.model.data.UserData;
import com.rest.utils.DatabaseAccess;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.EmailAlreadyExistsException;
import com.rest.utils.exceptions.InputTooLongException;
import com.rest.utils.exceptions.InvalidKeyException;
import com.rest.utils.exceptions.PasswordWrongException;
import com.rest.utils.exceptions.UserNotFoundException;
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
	public void testRegisterNewUserNormal() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		
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
	public void testRegisterNewUserNoEmail() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(null, password, firstName, lastName);
	}
	
	@Test(expected = ArgumentMissingException.class)
	public void testRegisterNewUserEmailEmpty() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser("", password, firstName, lastName);
	}
	
	@Test(expected = ArgumentMissingException.class)
	public void testRegisterNewUserNoPassword() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		
		dbAccess.registerNewUser(email, null, firstName, lastName);
	}
	
	@Test(expected = ArgumentMissingException.class)
	public void testRegisterNewUserPasswordEmpty() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		
		dbAccess.registerNewUser(email, "", firstName, lastName);
	}
	
	@Test(expected = WrongEmailFormatException.class)
	public void testRegisterNewUserWrongEmailFormat() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName);
	}
	
	@Test(expected = WrongEmailFormatException.class)
	public void testRegisterNewUserWrongEmailFormat2() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@ web.de";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName);
	}
	
	@Test(expected = InputTooLongException.class)
	public void testRegisterNewUserInputTooLong() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		//firstName too long
		String email = "test@web.de";
		String firstName = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec.";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		UserData result = dbAccess.registerNewUser(email, password, firstName.replaceAll(" ", ""), lastName);
		UserData expected = new UserData(null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(expected, result);
	}
	
	@Test(expected = InputTooLongException.class)
	public void testRegisterNewUserInputTooLong2() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		//lastName too long
		String email = "test@web.de";
		String lastName = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec.";
		String firstName = "Schliski";
		String password = "Hi98786";
		
		UserData result = dbAccess.registerNewUser(email, password, firstName.replaceAll(" ", ""), lastName.replaceAll(" ", ""));
		UserData expected = new UserData(null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(expected, result);
	}

	@Test (expected = InputTooLongException.class)
	public void testRegisterNewUserInputTooLong3() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		//E-mail too long
		String email = "test@LoremdipsumddolordsitdametddconsectetueddadipiscingdelitddAeneandcommododliguladegetddolorddAeneandmassaddCumdsociisdnatoquedpenatibusdetdmagnisddisdparturientdmontesddnasceturdridiculusdmusddDonecdquamdfelisddultriciesdnecddpellentesquedeuddpretiumdquisddsemddNulladconsequatdmassadquisdenimddDonecdwebdde.com";
		String lastName = "Schliski";
		String firstName = "Karolina";
		String password = "Hi98786";
		
		UserData result = dbAccess.registerNewUser(email, password, firstName, lastName);
		UserData expected = new UserData(null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(expected, result);
	}
	
	@Test(expected = InputTooLongException.class)
	public void testRegisterNewUserInputTooLong4() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		//Password too long
		String email = "test@web.de";
		String lastName = "Schliski";
		String firstName = "Karolina";
		String password = "Hi98786LoremdipsumddolordsitdametddconsectetueddadipiscingdelitddAeneandcommododliguladegetddolorddAeneandmassaddCumdsociisdnatoquedpenatibusdetdmagnisddisdparturientdmontesddnasceturdridiculusdmusddDonecdquamdfelisddultriciesdnecddpellentesquedeuddpretiumdquisddsemddNulladconsequatdmassadquisdenimddDonecdwebddecom";
		
		UserData result = dbAccess.registerNewUser(email, password, firstName, lastName);
		UserData expected = new UserData(null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(expected, result);
	}
	
	 /*
	 * Testing EmailAlreadyExists
	 */
	
	@Test(expected = EmailAlreadyExistsException.class)
	public void testUserAlreadyExists() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
	
		String email = "test@web.com";
		String firstName = "Karol";
		String lastName = "Bral";
		String password = "Hello";
	
		//adding user to database 1st time
		dbAccess.registerNewUser(email, password, firstName, lastName);
		
		//adding user to database 2nd time 
		UserData result = dbAccess.registerNewUser(email, password, firstName, lastName);
		
	}
	/*
	* Testing UserLogin
	*/
		
	@Test
	public void loginUserSuccessful() throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, SQLException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException {
		String email = "test@web.de";
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName);
		
		UserData userLoggedIn = dbAccess.loginUser(email, password);
		
		String loginKey = userLoggedIn.getLoginKey();
		
		UserData result = dbAccess.loginUser(email, password);
		UserData expected = new UserData (email, password, firstName, lastName, loginKey, null, null, null, null, null, null);
		assertEquals(expected,result);
	}
	
	@Test
	public void loginUserUnsuccessful() throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, SQLException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException {
		String email = "test@web.de";
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName);
		
		UserData userLoggedIn = dbAccess.loginUser(email, password);
		
		String loginKey = userLoggedIn.getLoginKey();
		
		UserData result = dbAccess.loginUser(email, password);
		UserData expected = new UserData (null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(expected,result);
	}
	
	/*
	* Testing UserLogout
	*/
	
	@Test
	public void testUserLogoutSuccessful() throws ArgumentMissingException {
	//Need proper loginKey
		String loginKey="whatever";
		
		boolean result = dbAccess.logoutUser(loginKey);
		boolean expected = true;
		assertEquals(result, expected);
		}
			
	@Test
	public void testUserLogoutUnsuccessful() throws ArgumentMissingException {
	//Need proper loginKey	
		String loginKey="whatever";
		
		boolean result = dbAccess.logoutUser(loginKey);
		boolean expected = false;
		assertEquals(result, expected);
	}

		
	/*
	* Testing UpdateSettings
	*/
	
	@Test
	public void testUpdateSettingsSuccessful() throws InvalidKeyException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException, UserNotFoundException, PasswordWrongException{
		
		String email = "test@web.de";
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName);
		
		UserData userLoggedIn = dbAccess.loginUser(email, password);
		
		String loginKey = userLoggedIn.getLoginKey();
		
		int minDistance = 10;
		int maxLoginInterval = 70;
		int geoPushInterval = 25;
				
		boolean settings = dbAccess.updateSettings(loginKey, minDistance, maxLoginInterval, geoPushInterval);
		boolean expected = true;
		assertEquals(expected, settings);
	}
		
	@Test
	public void testUpdateSettingsUnsuccessful() throws InvalidKeyException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException, UserNotFoundException, PasswordWrongException {
		String email = "test@web.de";
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName);
		
		UserData userLoggedIn = dbAccess.loginUser(email, password);
		
		String loginKey = userLoggedIn.getLoginKey();
		
		int minDistance = 10;
		int maxLoginInterval = 70;
		int geoPushInterval = 25;
				
		boolean result = dbAccess.updateSettings(loginKey, minDistance, maxLoginInterval, geoPushInterval);
		boolean expected = false;
		assertEquals(expected, result);
	}
	
	/*
	* Testing Change Password
	*/
	
	@Test
	public void testChangePasswordSuccessful() throws UserNotFoundException {
	
		String userMail = "user@web.com";
		String newPassword = "newpaswd";
	
		boolean result = dbAccess.changePassword(userMail, newPassword);
		boolean expected = true;
		assertEquals(expected, result);
	}
	
	@Test
	public void testChangePasswordUnsuccessful() throws UserNotFoundException {
		
		String userMail = "user@webb.com";
		String newPassword = "newpasswd";
	
		boolean result = dbAccess.changePassword(userMail, newPassword);
		boolean expected = false;
		assertEquals(expected, result);
	}
	
	/*
	* Testing StoreNewCheckIn
	*/
	
	@Test
	public void testCheckInSuccessful() throws ArgumentMissingException, InvalidKeyException{
		String key = "key";
		String venueId = "venueID";
		String timestamp = "timestamp";
		
		Location result = dbAccess.checkIn(key, venueId, timestamp);
		boolean expected = true;
		assertEquals (expected, result);
	}
	
	@Test
	public void testCheckInUnsuccessful() throws ArgumentMissingException, InvalidKeyException{
		String key = "key";
		String venueId = "venueID";
		String timestamp = "timestamp";
		
Location result = dbAccess.checkIn(key, venueId, timestamp);
		boolean expected = false;
		assertEquals (expected, result);
	}

	/*
	* Testing StoreNewReview
	*/
	
	@Test
	public void testStoreNewReviewSuccessful() throws InvalidKeyException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException, UserNotFoundException, PasswordWrongException {
	
		String email = "test@web.de";
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName);
		
		UserData userLoggedIn = dbAccess.loginUser(email, password);
		
		String loginKey = userLoggedIn.getLoginKey();
		
		String venueId = "venueID";
		int rating = 1;
		String reviewTitle = "Horrible place";
		String reviewDescription = "Disgusting food, will never go there again";
		String imageUri = "http://whatever";
	
	boolean result = dbAccess.storeNewReview(loginKey, venueId, rating, reviewTitle, reviewDescription, imageUri);
	boolean expected = true;
	assertEquals (expected, result);
	}	
	
	@Test
	public void testStoreNewReviewUnsuccessful() throws InvalidKeyException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException, UserNotFoundException, PasswordWrongException {
		String email = "test@web.de";
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName);
		
		UserData userLoggedIn = dbAccess.loginUser(email, password);
		
		String loginKey = userLoggedIn.getLoginKey();
		
		String venueId = "venueID";
		int rating = 1;
		String reviewTitle = "Horrible place";
		String reviewDescription = "Disgusting food, will never go there again";
		String imageUri = "http://whatever";
	
	boolean result = dbAccess.storeNewReview(loginKey, venueId, rating, reviewTitle, reviewDescription, imageUri);
	boolean expected = false;
	assertEquals (expected, result);
	}	
}
