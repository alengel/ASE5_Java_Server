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
	public void testRegisterNewUserInputTooLong2() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
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
	public void testRegisterNewUserInputTooLong3() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
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
	public void testRegisterNewUserInputTooLong4() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
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
	
	@Test
	public void testUserAlreadyExists() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
	
		String email = "test@web.com";
		String firstName = "Karol";
		String lastName = "Bral";
		String password = "Hello";
	
		//adding user to database 1st time
		dbAccess.registerNewUser(email, password, firstName, lastName);
		
		//adding user to database 2nd time 
		UserData result = dbAccess.registerNewUser(email, password, firstName, lastName);
		UserData expected = new UserData(null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(expected, result);
	}
	/*
	* Testing UserLogin
	*/
		
	@Test
	public void testUserLoginSuccesful() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
		String email = "testUserLogin@web.com";		
		String password = "Hello";
		dbAccess.registerNewUser(email, password, null, null);
	//If a user with this email and password is found true is returned, else  - false.
		UserData result = dbAccess.loginUser(email, password);
		UserData expected = new UserData(email,password,null,null,null,null,null,null,null,null,null);
		assertEquals(expected,result);
	}
	
	@Test
	//User not registered
	public void testUserLoginUnsuccesful1() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
		String email = "testUserLogin2@web.com";
		String password = "Hello";
		
	//Made the assumption that an unsuccessful login will return UserData of null	
		UserData result = dbAccess.loginUser(email, password);
		UserData expected = new UserData (null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(expected, result);
	}
	
	@Test
	//Incorrect Password
	public void testUserLoginUnsuccessful2() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
	//Registering the user
		String email = "testUserLogin3@web.com";
		String password = "Heyyy";
		String wrongpassword = "What?";
		
		dbAccess.registerNewUser(email, password, null, null);
	//Made the assumption that an unsuccessful login will return UserDate of null
		UserData result = dbAccess.loginUser(email, wrongpassword);
		UserData expected = new UserData (null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(expected, result);
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
	* Testing ForgottenPassword
	*/
	//public void testForgottenPassword() {
	//Need to include in DatabaseAccessInterface
		//String email = "test@web.com"
		
		//serData result = dbAccess.
	//}
	
	/*
	* Testing UpdateSettings
	*/
	
	@Test
	public void testUpdateSettingsSuccessful(){
		int minDistance = 10;
		int maxLoginInterval = 70;
		int geoPushInterval = 25;
		int geoCheckInterval = 30;
		String userKey = "UserKey";
		
		boolean result = dbAccess.updateSettings(userKey, minDistance, maxLoginInterval, geoPushInterval, geoCheckInterval);
		boolean expected = true;
		assertEquals(expected, result);
	}
		
	@Test
	public void testUpdateSettingsUnsuccessful() {
		int minDistance = 10;
		int maxLoginInterval = 70;
		int geoPushInterval = 25;
		int geoCheckInterval = 30;
		String userKey = "UserKey";
		
		boolean result = dbAccess.updateSettings(userKey, minDistance, maxLoginInterval, geoPushInterval, geoCheckInterval);
		boolean expected = false;
		assertEquals(expected, result);
	}
	/*
	* Testing StoreNewCheckIn
	*/
	
	@Test
	public void testCheckInSuccessful(){
		String key = "key";
		String venueId = "venueID";
		String timestamp = "timestamp";
		
		boolean result = dbAccess.checkIn(key, venueId, timestamp);
		boolean expected = true;
		assertEquals (expected, result);
	}
	
	@Test
	public void testCheckInUnsuccessful(){
		String key = "key";
		String venueId = "venueID";
		String timestamp = "timestamp";
		
		boolean result = dbAccess.checkIn(key, venueId, timestamp);
		boolean expected = false;
		assertEquals (expected, result);
	}

	
}
