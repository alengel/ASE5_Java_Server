package com.rest.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.rest.user.model.data.UserData;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.InputTooLongException;
import com.rest.utils.exceptions.WrongEmailFormatException;

public class DatabaseAccess implements DatabaseAccessInterface {
	
	private final static int MAX_INPUT_LENGTH = 200;
	
	//static Strings for SQL Queries
	private final static String SELECT = "Select ";
	private final static String DELETE = "Delete ";
	private final static String FROM = "from ";
	private final static String WHERE = "where ";
	private final static String DISTINCT = "distinct ";
	private final static String INSERT_INTO = "Insert into ";
	private final static String ALL = "* ";
	private final static String EQUALS = " = ";
	private final static String VALUES = "values ";
	
	
	//static Strings for the table t5_user
	private final static String USER_TABLE = "t5_users ";
	private final static String USER_ID = "id ";
	private final static String USER_EMAIL = "email ";
	private final static String USER_PASSWORD = "passwd ";
	private final static String USER_FIRSTNAME = "first_name ";
	private final static String USER_LASTNAME = "last_name ";
	private final static String USER_LOGINKEY = "login_key ";
	private final static String USER_LOGIN_TIMESTAMP = "login_timestamp ";
	private final static String USER_LAST_LOGIN = "last_login ";
	private final static String USER_LAST_REQUEST = "last_request ";
	private final static String USER_LOGOUT_SESSION_TIME = "logout_session_time ";
	private final static String USER_GPS_PUSH_TIME = "gps_push_time ";
	private final static String USER_DATED = "dated ";
	
	//static Strings for the table t5_locations
	private final static String LOCATIONS_TABLE = "t5_locations ";
	
	
	@Override
	public UserData registerNewUser(String email, String password, 
			String firstName, String lastName) throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException {
		
		if(email == null || password == null || email.isEmpty() || password.isEmpty()) {
			throw new ArgumentMissingException();
		}
		
		boolean inputNotTooLong = checkInputLength(email) && checkInputLength(password) && checkInputLength(firstName) && checkInputLength(lastName);
		if(!inputNotTooLong) {
			throw new InputTooLongException();
		}
		
		boolean rightEmail = checkEmailFormat(email);
		if(!rightEmail) {
			throw new WrongEmailFormatException();
		}
		
		
		//get access to the database
		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();
		
		//check if another user with email already exists in the database
		String queryEmailAlreadyExists = SELECT + ALL + FROM + USER_TABLE + WHERE + USER_EMAIL + EQUALS + email;
		ResultSet resultEmailAlreadyExists;
		try {
			resultEmailAlreadyExists = statement.executeQuery(queryEmailAlreadyExists);
		} catch (SQLException e) {
			return new UserData(null, null, null, null, null, null, null, null, null, null, null);
		}
		try {
			if(resultEmailAlreadyExists.isAfterLast()) {
				return new UserData(null, null, null, null, null, null, null, null, null, null, null);
			}
		} catch (SQLException e) {
			return new UserData(null, null, null, null, null, null, null, null, null, null, null);
		}
		
		String insertStatement = INSERT_INTO + USER_TABLE +
				"(" + USER_EMAIL + "," + USER_PASSWORD + "," + USER_FIRSTNAME + "," + USER_LASTNAME + ")"
				+ VALUES + "(" + email + "," + password + "," + firstName + "," + lastName + ")";
		boolean success;
		try {
			success = statement.execute(insertStatement);
		} catch (SQLException e) {
			return new UserData(null, null, null, null, null, null, null, null, null, null, null);
		}
						
		if(success == false) {
			return new UserData(null, null, null, null, null, null, null, null, null, null, null);
		}
		
		dbConnection.closeConn();
		
		return new UserData(email, password, firstName, lastName, null, null, null, null, null, null, null);
	}
	
	/**
	 * 
	 * @param input
	 * @return false, if input.lenght() > MAX_INPUT_LENGTH, true otherwise
	 */
	private boolean checkInputLength(String input) {
		
		if(input == null) {
			return true;
		} else if(input.length() <= MAX_INPUT_LENGTH) {
			return true;
		}
		return false;
	}

	private boolean checkEmailFormat(String email) {
		
		if(email.startsWith("@")) return false;
		if(email.endsWith("@")) return false;
		if(!email.contains("@")) return false;
		if(!email.contains(".")) return false;
		if(email.startsWith(".")) return false;
		if(email.endsWith(".")) return false;
		//TODO: add more!
		
		return true;
	}

	@Override
	public UserData loginUser(String email, String Password) {
		//TODO 
		
		return null;
	}
	
	
	
	@Override
	public boolean logoutUser(String loginKey) {
		//TODO 
		
		return false;
	}

	public void clearDatabase() {
		
		DBCon dbConnection = new DBCon();
		Statement s = dbConnection.getStatement();
		String deleteUser = DELETE + FROM + USER_TABLE;
		String deleteLocations = DELETE + FROM + LOCATIONS_TABLE;
		try {
			s.executeUpdate(deleteUser);
			s.executeUpdate(deleteLocations);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

}
