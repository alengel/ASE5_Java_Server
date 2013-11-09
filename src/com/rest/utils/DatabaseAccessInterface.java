package com.rest.utils;

import com.rest.user.model.data.UserData;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.InputTooLongException;
import com.rest.utils.exceptions.WrongEmailFormatException;

public interface DatabaseAccessInterface {
	
	/**
	 * 
	 * @param email 
	 * @param password
	 * @return the UserData that has been retrieved from the DB + his key. Null if the user is not found or the password is wrong
	 */
	public UserData loginUser(String email, String password);
		
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
	 */
	public boolean logoutUser(String loginKey);
	
	/**
	 * 
	 * @param user
	 * @param newPassword
	 * @return true if change was successful, false otherwise
	 */
	public boolean changePassword(String userMail, String newPassword);
	
	/**
	 * 
	 * @param key
	 * @param venueId
	 * @param timestamp
	 * @return true if change successful, false otherwise
	 */
	public boolean storeNewCheckin(String key, String venueId, String timestamp);
	
	public boolean logout(String key);
	
	public boolean updateSettings(String key, int minDistance, int maxLoginInterval, int geoPushInterval, int geoCheckInterval);
	
}
