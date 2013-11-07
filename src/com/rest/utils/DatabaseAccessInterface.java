package com.rest.utils;

import com.rest.user.model.User;

public interface DatabaseAccessInterface {
	
	/**
	 * 
	 * @param email
	 * @param password
	 * @return the User that has been retrieved from the DB. Null if the user is not found or the password is wrong
	 */
	public User loginUser(String email, String password);
		
	/**
	 * register a new User in the database if he does not exist yet
	 * @param email
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @return the new User that has been saved. Null if it was not possible
	 */
	public User registerNewUser(String email, String passwd, String firstName, String lastName);
	
	/**
	 * should remove the loginKey form the DB
	 * @param loginKey
	 * @return null
	 */
	public User logoutUser(String loginKey);
	
	
}
