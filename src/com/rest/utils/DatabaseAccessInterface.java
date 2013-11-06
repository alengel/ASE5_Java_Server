package com.rest.utils;

import com.rest.user.model.User;

public interface DatabaseAccessInterface {
	
	/**
	 * register a new User in the database if he does not exist yet
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param password
	 * @return the new User that has been saved. Null if it was not possible
	 */
	public User registerNewUser(String firstName, String lastName, String email, String password);
	
}
