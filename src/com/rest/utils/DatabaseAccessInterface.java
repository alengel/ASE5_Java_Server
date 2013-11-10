package com.rest.utils;

import com.rest.user.model.data.UserData;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.InputTooLongException;
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
	 */
	public boolean loginUser(String email, String password, String loginKey) throws ArgumentMissingException, UserNotFoundException, PasswordWrongException;
		
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
	 */
	public UserData registerNewUser(String email, String password, String firstName, String lastName) throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException;
	
	/**
	 * should remove the loginKey form the DB
	 * @param loginKey
	 * @return true, if logout was successful, false otherwise
	 * @throws ArgumentMissingException 
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
	 */
	public boolean checkIn(String userKey, String venueId, String timestamp);
	
	/**
	 * 
	 * @param userKey
	 * @param minDistance
	 * @param maxLoginInterval
	 * @param geoPushInterval
	 * @param geoCheckInterval
	 * @return true if change successful, false otherwise
	 */
	public boolean updateSettings(String userKey, int minDistance, int maxLoginInterval, int geoPushInterval, int geoCheckInterval);
	
	/**
	 * 
	 * @param key
	 * @param venueId
	 * @param rating
	 * @param reviewTitle
	 * @param reviewDescription
	 * @param imageUri
	 * @return true if change successful, false otherwise
	 */
	public boolean storeNewReview(String key, String venueId, int rating, String reviewTitle, String reviewDescription, String imageUri);
	
}
