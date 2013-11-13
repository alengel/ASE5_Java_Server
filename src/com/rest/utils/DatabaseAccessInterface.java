package com.rest.utils;

import java.sql.SQLException;

import com.rest.location.model.Location;
import com.rest.user.model.data.UserData;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.EmailAlreadyExistsException;
import com.rest.utils.exceptions.InputTooLongException;
import com.rest.utils.exceptions.InvalidKeyException;
import com.rest.utils.exceptions.PasswordWrongException;
import com.rest.utils.exceptions.UserNotFoundException;
import com.rest.utils.exceptions.WrongEmailFormatException;

public interface DatabaseAccessInterface {
	
	/**
	 * delete everything in the database
	 */
	public void clearDatabase();
	
	
	/**
	 * 
	 * @param email 
	 * @param password
	 * @param loginKey 
	 * @return true if login was successful, false otherwise
	 * @throws ArgumentMissingException 
	 * @throws UserNotFoundException 
	 * @throws PasswordWrongException 
	 * @throws SQLException 
	 */
	public UserData loginUser(String email, String password) throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, SQLException;
		
	/**
	 * register a new User in the database if he does not exist yet
	 * @param email
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @return the new UserData that has been saved. Null if it was not possible (e. g. email is already registered to another user)
	 * @throws WrongEmailFormatException 
	 * @throws InputTooLongException 
	 * @throws ArgumentMissingException 
	 * @throws SQLException 
	 */
	public UserData registerNewUser(String email, String password, String firstName, String lastName) throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException;
	
	/**
	 * should remove the loginKey form the DB
	 * @param loginKey
	 * @return true, if logout was successful, false otherwise
	 * @throws ArgumentMissingException d
	 */
	public boolean logoutUser(String loginKey) throws ArgumentMissingException;
	
	/**
	 * 
	 * @param user
	 * @param newPassword
	 * @return true if change was successful, false otherwise
	 * @throws UserNotFoundException 
	 */
public boolean changePassword(String userMail, String newPassword) throws UserNotFoundException;
	
	/**
	 * 
	 * @param userKey
	 * @param venueId
	 * @param timestamp
	 * @return true if change successful, false otherwise
	 * @throws ArgumentMissingException 
	 * @throws InvalidKeyException 
	 */

	public Location checkIn(String userKey, String venueId, String timestamp) throws ArgumentMissingException, InvalidKeyException;

	
	/**
	 * 
	 * @param userKey
	 * @param minDistance
	 * @param maxLoginInterval
	 * @param geoPushInterval
	 * @return true if change successful, false otherwise
	 * @throws InvalidKeyException 
	 */
	public boolean updateSettings(String userKey, int minDistance, int maxLoginInterval, int geoPushInterval) throws InvalidKeyException;
	
	/**
	 * 
	 * @param key
	 * @param venueId
	 * @param rating
	 * @param reviewTitle
	 * @param reviewDescription
	 * @param imageUri
	 * @return true if change successful, false otherwise
	 * @throws InvalidKeyException 
	 */
	public boolean storeNewReview(String key, String venueId, int rating, String reviewTitle, String reviewDescription, String imageUri) throws InvalidKeyException;
	
}
