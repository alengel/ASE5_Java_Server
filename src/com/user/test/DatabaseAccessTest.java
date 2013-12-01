package com.user.test;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysql.jdbc.ResultSetImpl;
import com.rest.location.model.Location;
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
		
		UserData result = dbAccess.registerNewUser(email, password, firstName, lastName, null);
		UserData expected = new UserData(email, password, firstName, lastName, null, null, null, null, null, null, null, null);
		assertEquals(email, result.getEmail());
		assertEquals(expected.getPasswd(), result.getPasswd());
		assertEquals(expected.getFirstName(), result.getFirstName());
		assertEquals(expected.getLastName(), result.getLastName());
		
		dbAccess.clearDatabase();
		
		result  = dbAccess.registerNewUser(email, password, null, lastName, null);
		expected = new UserData(email, password, null, lastName, null, null, null, null, null, null, null, null);
		assertEquals(expected.getEmail(), result.getEmail());
		assertEquals(expected.getPasswd(), result.getPasswd());
		assertNull(result.getFirstName());
		assertEquals(expected.getLastName(), result.getLastName());
		
		dbAccess.clearDatabase();
		
		result  = dbAccess.registerNewUser(email, password, firstName, null, null);
		expected = new UserData(email, password, firstName, null, null, null, null, null, null, null, null, null);
		assertEquals(expected.getEmail(), result.getEmail());
		assertEquals(expected.getPasswd(), result.getPasswd());
		assertEquals(expected.getFirstName(), result.getFirstName());
		assertNull(result.getLastName());
		
		dbAccess.clearDatabase();
		
		result  = dbAccess.registerNewUser(email, password, null, null, null);
		expected = new UserData(email, password, null, null, null, null, null, null, null, null, null, null);
		assertEquals(expected.getEmail(), result.getEmail());
		assertEquals(expected.getPasswd(), result.getPasswd());
		assertNull(result.getFirstName());
		assertNull(result.getLastName());
	}
	
	@Test(expected = ArgumentMissingException.class)
	public void testRegisterNewUserNoEmail() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(null, password, firstName, lastName, null);
	}
	
	@Test(expected = ArgumentMissingException.class)
	public void testRegisterNewUserEmailEmpty() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser("", password, firstName, lastName, null);
	}
	
	@Test(expected = ArgumentMissingException.class)
	public void testRegisterNewUserNoPassword() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		
		dbAccess.registerNewUser(email, null, firstName, lastName, null);
	}
	
	@Test(expected = ArgumentMissingException.class)
	public void testRegisterNewUserPasswordEmpty() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@web.de";
		
		dbAccess.registerNewUser(email, "", firstName, lastName, null);
	}
	
	@Test(expected = WrongEmailFormatException.class)
	public void testRegisterNewUserWrongEmailFormat() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName, null);
	}
	
	@Test(expected = WrongEmailFormatException.class)
	public void testRegisterNewUserWrongEmailFormat2() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		
		String firstName = "Karolina";
		String lastName = "Schliski";
		String email = "test@ web.de";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName, null);
	}
	
	@Test(expected = InputTooLongException.class)
	public void testRegisterNewUserInputTooLong() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		//firstName too long
		String email = "test@web.de";
		String firstName = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec.";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		UserData result = dbAccess.registerNewUser(email, password, firstName.replaceAll(" ", ""), lastName, null);
		UserData expected = new UserData(null, null, null, null, null, null,  null, null, null, null, null, null);
		assertEquals(expected, result);
	}
	
	@Test(expected = InputTooLongException.class)
	public void testRegisterNewUserInputTooLong2() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		//lastName too long
		String email = "test@web.de";
		String lastName = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec.";
		String firstName = "Schliski";
		String password = "Hi98786";
		
		UserData result = dbAccess.registerNewUser(email, password, firstName.replaceAll(" ", ""), lastName.replaceAll(" ", ""), null);
		UserData expected = new UserData(null, null, null, null,  null, null, null, null, null, null, null, null);
		assertEquals(expected, result);
	}

	@Test (expected = InputTooLongException.class)
	public void testRegisterNewUserInputTooLong3() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		//E-mail too long
		String email = "test@LoremdipsumddolordsitdametddconsectetueddadipiscingdelitddAeneandcommododliguladegetddolorddAeneandmassaddCumdsociisdnatoquedpenatibusdetdmagnisddisdparturientdmontesddnasceturdridiculusdmusddDonecdquamdfelisddultriciesdnecddpellentesquedeuddpretiumdquisddsemddNulladconsequatdmassadquisdenimddDonecdwebdde.com";
		String lastName = "Schliski";
		String firstName = "Karolina";
		String password = "Hi98786";
		
		UserData result = dbAccess.registerNewUser(email, password, firstName, lastName, null);
		UserData expected = new UserData(null, null, null, null, null, null, null, null, null, null, null, null);
		assertEquals(expected, result);
	}
	
	@Test(expected = InputTooLongException.class)
	public void testRegisterNewUserInputTooLong4() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		//Password too long
		String email = "test@web.de";
		String lastName = "Schliski";
		String firstName = "Karolina";
		String password = "Hi98786LoremdipsumddolordsitdametddconsectetueddadipiscingdelitddAeneandcommododliguladegetddolorddAeneandmassaddCumdsociisdnatoquedpenatibusdetdmagnisddisdparturientdmontesddnasceturdridiculusdmusddDonecdquamdfelisddultriciesdnecddpellentesquedeuddpretiumdquisddsemddNulladconsequatdmassadquisdenimddDonecdwebddecom";
		
		UserData result = dbAccess.registerNewUser(email, password, firstName, lastName, null);
		UserData expected = new UserData(null, null, null, null, null, null, null, null, null, null, null, null);
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
		dbAccess.registerNewUser(email, password, firstName, lastName, null);
		
		dbAccess.registerNewUser(email, password, firstName, lastName, null);
		
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
		
		dbAccess.registerNewUser(email, password, firstName, lastName, null);
		
		UserData userLoggedIn = dbAccess.loginUser(email, password);
		
		String loginKey = userLoggedIn.getLoginKey();
		
		UserData result = dbAccess.loginUser(email, password);
		UserData expected = new UserData (email, password, firstName, lastName, loginKey, null, null, null, null, null, null, null);
		assertEquals(expected,result);
	}
	
	@Test(expected = UserNotFoundException.class)
	public void loginUserUnsuccessful() throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, UserNotFoundException, PasswordWrongException {
		String email = "test@web.de";
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName, null);
		
		UserData userLoggedIn = dbAccess.loginUser(email, password);
		
		userLoggedIn.getLoginKey();
		
		UserData result = dbAccess.loginUser("ttt@web.de", password);
	}
	
	/*
	* Testing UserLogout
	*/
	
	@Test
	public void testUserLogoutSuccessful() throws ArgumentMissingException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException, UserNotFoundException, PasswordWrongException {
		String email = "test@web.de";
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName, null);
		
		UserData userLoggedIn = dbAccess.loginUser(email, password);
		
		String key = userLoggedIn.getLoginKey();
		
		boolean result = dbAccess.logoutUser(key);
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
		
		dbAccess.registerNewUser(email, password, firstName, lastName, null);
		
		UserData userLoggedIn = dbAccess.loginUser(email, password);
		
		String loginKey = userLoggedIn.getLoginKey();
		
		int minDistance = 10;
		int maxLoginInterval = 70;
		int geoPushInterval = 25;
				
		boolean settings = dbAccess.updateSettings(loginKey, minDistance, maxLoginInterval, geoPushInterval);
		boolean expected = true;
		assertEquals(expected, settings);
	}
		
	@Test(expected = InvalidKeyException.class)
	public void testUpdateSettingsUnsuccessful() throws InvalidKeyException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException, UserNotFoundException, PasswordWrongException {
		String email = "test@web.de";
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName, null);
		
		UserData userLoggedIn = dbAccess.loginUser(email, password);
		
		String loginKey = userLoggedIn.getLoginKey();
		
		int minDistance = 10;
		int maxLoginInterval = 70;
		int geoPushInterval = 25;
				
		dbAccess.updateSettings("-1", minDistance, maxLoginInterval, geoPushInterval);
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
	
	@Test(expected = UserNotFoundException.class)
	public void testChangePasswordUnsuccessful() throws UserNotFoundException {
		
		String userMail = "user@webb.com";
		String newPassword = "newpasswd";
	
		dbAccess.changePassword(userMail, newPassword);
		
	}
	
	/*
	* Testing StoreNewCheckIn
	*/
	
	@Test
	public void testCheckInSuccessful() throws ArgumentMissingException, InvalidKeyException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException, SQLException, UserNotFoundException, PasswordWrongException{
		String email = "test@web.de";
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName, null);
		
		UserData userLoggedIn = dbAccess.loginUser(email, password);
		
		String loginKey = userLoggedIn.getLoginKey();
		
		String venueId = "venueID";
		String timestamp = "timestamp";
		
		Location result = dbAccess.checkIn(loginKey, venueId, timestamp);
		assertEquals ("true", result.getSuccess());
	}
	
	@Test(expected = InvalidKeyException.class)
	public void testCheckInUnsuccessful() throws ArgumentMissingException, InvalidKeyException, WrongEmailFormatException, InputTooLongException, EmailAlreadyExistsException, SQLException, UserNotFoundException, PasswordWrongException{
		String email = "test@web.de";
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		dbAccess.registerNewUser(email, password, firstName, lastName, null);
		
		UserData userLoggedIn = dbAccess.loginUser(email, password);
		
		String loginKey = userLoggedIn.getLoginKey();
		
		String venueId = "venueID";
		String timestamp = "timestamp";
		
		Location result = dbAccess.checkIn("-1", venueId, timestamp);
		assertEquals ("true", result.getSuccess());
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
		
		dbAccess.registerNewUser(email, password, firstName, lastName, null);
		
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
		
		dbAccess.registerNewUser(email, password, firstName, lastName, null);
		
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
	
	@Test
	public void testGetReviewsSuccessful() {
		
		String email = "test@web.de";
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		try {
			dbAccess.registerNewUser(email, password, firstName, lastName, null);
			
			UserData userLoggedIn = dbAccess.loginUser(email, password);
			
			String loginKey = userLoggedIn.getLoginKey();
			
			String venueId = "venueID";
			String timestamp = "timestamp";
			
			dbAccess.checkIn(loginKey, venueId, timestamp);
			
			int rating = 1;
			String reviewTitle = "Horrible place";
			String reviewDescription = "Disgusting food, will never go there again";
			String imageUri = "http://whatever";
		
			dbAccess.storeNewReview(loginKey, venueId, rating, reviewTitle, reviewDescription, imageUri);
			
			Review result = dbAccess.getReviews(venueId);
			ReviewData resultReviewData = result.getReviewData().get(0);
			assertEquals(rating, resultReviewData.getRating());
			assertEquals(reviewTitle, resultReviewData.getTitle());
			assertEquals(reviewDescription, resultReviewData.getReview());
			assertEquals(imageUri, resultReviewData.getPicture());
			assertEquals(email, resultReviewData.getUserEmail());
			
		} catch (WrongEmailFormatException | InputTooLongException
				| ArgumentMissingException | EmailAlreadyExistsException | InvalidKeyException | UserNotFoundException | PasswordWrongException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	@Test
	public void testGetReviewsUnSuccessful() {
			
		Review result = dbAccess.getReviews("123");
		assertEquals("Nobody left a review yet", result.getMessage());
		
		
	}
	
	@Test
	public void getReviewsForUserSuccessful() {
		
		String email = "test@web.de";
		String firstName = "Karolina";
		String lastName = "Schliski";
		String password = "Hi98786";
		
		try {
			dbAccess.registerNewUser(email, password, firstName, lastName, null);
			
			UserData userLoggedIn = dbAccess.loginUser(email, password);
			
			String loginKey = userLoggedIn.getLoginKey();
			
			String venueId = "venueID";
			String timestamp = "timestamp";
			
			dbAccess.checkIn(loginKey, venueId, timestamp);
			
			int rating = 1;
			String reviewTitle = "Horrible place";
			String reviewDescription = "Disgusting food, will never go there again";
			String imageUri = "http://whatever";
		
			dbAccess.storeNewReview(loginKey, venueId, rating, reviewTitle, reviewDescription, imageUri);
			
			List<ReviewData> result = dbAccess.getReviewsForUser(0);
			ReviewData resultReviewData = result.get(0);
			assertEquals(rating, resultReviewData.getRating());
			assertEquals(reviewTitle, resultReviewData.getTitle());
			assertEquals(reviewDescription, resultReviewData.getReview());
			assertEquals(imageUri, resultReviewData.getPicture());
			assertEquals(email, resultReviewData.getUserEmail());
			
		} catch (WrongEmailFormatException | InputTooLongException
				| ArgumentMissingException | EmailAlreadyExistsException | InvalidKeyException | UserNotFoundException | PasswordWrongException e) {
			e.printStackTrace();
		}
	}
	
	@Test(expected = UserNotFoundException.class)
	public void testGetReviewsForUserUnSuccessful() throws UserNotFoundException {
		
		List<ReviewData> result = null;

		result = dbAccess.getReviewsForUser(99);
		
	}
}
