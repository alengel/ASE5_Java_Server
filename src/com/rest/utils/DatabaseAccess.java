package com.rest.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rest.location.model.Location;
import com.rest.location.model.data.LocationData;
import com.rest.review.model.data.ReviewData;
import com.rest.user.model.data.UserData;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.EmailAlreadyExistsException;
import com.rest.utils.exceptions.InputTooLongException;
import com.rest.utils.exceptions.InvalidKeyException;
import com.rest.utils.exceptions.PasswordWrongException;
import com.rest.utils.exceptions.UserNotFoundException;
import com.rest.utils.exceptions.WrongEmailFormatException;

public class DatabaseAccess implements DatabaseAccessInterface {
	
	//maximum allowed inputLenght according to the database ddl

		private final static int MAX_INPUT_LENGTH = 200;
		
		//static Strings for SQL Queries
		private final static String SELECT = "Select ";
		private final static String DELETE = "Delete ";
		private final static String UPDATE = "Update ";
		private final static String SET = "Set ";
		private final static String FROM = "from ";
		private final static String WHERE = "where ";
		private final static String AND = "and ";
		private final static String DISTINCT = "Distinct ";
		private final static String INSERT_INTO = "Insert into ";
		private final static String INSERT_IGNORE_INTO = "Insert Ignore into ";
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
		private final static String USER_GEO_PUSH_INTERVAL = "geo_push_interval ";
		private final static String USER_MIN_DISTANCE = "min_distance ";
		//static Strings for the table t5_locations
		private final static String LOCATIONS_TABLE = "t5_locations ";
		private final static String LOCATIONS_ID = "id ";
		private final static String LOCATIONS_FSQUARE_VENUE_ID = "foursquare_venue_id ";
		//static Stings for the table t5_users_reviews
		private static final String REVIEWS_TABLE = "t5_reviews ";
		private static final String REVIEWS_USER_ID = "users_id ";
		private static final String REVIEWS_LOCATION_ID = "locations_id ";
		private static final String REVIEWS_RATING = "rating ";
		private static final String REVIEWS_REVIEW_TITLE = "review_title ";
		private static final String REVIEWS_REVIEW_DESCRIPTION = "review_description ";
		private static final String REVIEWS_REVIEW_PICTURE = "review_picture ";
		private static final String REVIEWS_TOTAL_VOTE_UP = "total_vote_up";
		private static final String REVIEWS_TOTAL_VOTE_DOWN = "total_vote_down";
		private static final String REVIEWS_SPAMS = "spams";
		//static Strings for t5_users_reviews_comments
		private static final String REVIEWS_COMMENTS_TABLE = "t5_users_reviews_comments";
		private static final String REVIEWS_COMMENTS_ID = "id";
		private static final String REVIEWS_COMMENTS_USER_ID = "user_id";
		private static final String REVIEWS_COMMENTS_USER_REVIEWS_ID = "user_reviews_id";
		private static final String REVIEWS_COMMENTS_COMMENT = "comment";
		//static Strings for t5_users_votes
		private static final String USERS_VOTES_TABLE = "t5_users_votes";
		private static final String USERS_VOTES_ID = "id";
		private static final String USERS_VOTES_USER_ID = "user_id";
		private static final String USERS_VOTES_USER_REVIEWS_ID = "user_reviews_id";
		private static final String USERS_VOTES_VOTE_FLAG = "vote_flag";
		private static final String USERS_VOTES_DATED = "dated";
		//static Strings for the table t5_checkins
		private static final String CHECKIN_TABLE = "t5_checkins ";
		private static final String CHECKIN_USER_ID = "users_id ";
		private static final String CHECKIN_LOCATION_ID = "locations_id ";
		private static final String CHECKIN_CHECKIN_TIMESTAMP = "checkin_timestamp ";
		

	
	@Override
	public UserData registerNewUser(String email, String password, 
			String firstName, String lastName, String picture) throws WrongEmailFormatException, InputTooLongException, ArgumentMissingException, EmailAlreadyExistsException, SQLException {
		
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
		String queryEmailAlreadyExists = SELECT + ALL + FROM + USER_TABLE + WHERE + USER_EMAIL + EQUALS + "'"+email+"'";
		ResultSet resultEmailAlreadyExists;
		try {
			resultEmailAlreadyExists = statement.executeQuery(queryEmailAlreadyExists);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		try {
			if(resultEmailAlreadyExists.next()) {
				throw new EmailAlreadyExistsException();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		String insertStatement = INSERT_INTO + USER_TABLE +
				"(" + USER_EMAIL + ", " + USER_PASSWORD + ", " + USER_FIRSTNAME + ", " + USER_LASTNAME + ", picture, logout_session_time, geo_push_interval, min_distance) "
				+ VALUES + "('" + email + "', '" + password + "', '" + firstName + "', '" + lastName + "', '" + picture + "', 60, 30, 100);";
	
		int success;
		try {
			success = statement.executeUpdate(insertStatement);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
						
		if(success != 1) {
			return null;
		}
		
		dbConnection.closeConn();
		
		return new UserData(email, password, firstName, lastName, picture, null, null, null, null, null, null, null);
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
		if(email.contains(" ")) return false;
		
		return true;
	}

	
	
	@Override
	public UserData loginUser(String email, String password) throws ArgumentMissingException, UserNotFoundException, PasswordWrongException, SQLException {
		
		if(email == null || password == null || email.isEmpty() || password.isEmpty()) {
			throw new ArgumentMissingException();
		}
		
		//get access to the database
		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();
		
		//check if user exists and if the password is correct
		String getUserFromDb = SELECT + "* " + FROM + USER_TABLE + WHERE + USER_EMAIL + "= '" + email +"';";
		ResultSet userFromDb;
		String firstName;
		String lastName;
		String loginKey;
		String picture;
		int logoutSessionTime;
		int geoPushInterval;
		int minDistance;

			userFromDb = statement.executeQuery(getUserFromDb);
			if(!userFromDb.next()) {
				throw new UserNotFoundException();
			}
			String passwordInDb = userFromDb.getString("passwd");
			if(!passwordInDb.equals(password)) {
				throw new PasswordWrongException();
			}
			
			//log in the user			
			long timeStamp = System.currentTimeMillis()/1000L;
			loginKey = email+timeStamp;
			loginKey = SHA1.stringToSHA(loginKey);	
			firstName = userFromDb.getString("first_name");
			lastName = userFromDb.getString("last_name");
			picture = userFromDb.getString("picture");
			logoutSessionTime = userFromDb.getInt("logout_session_time");
			geoPushInterval = userFromDb.getInt("geo_push_interval");
			minDistance = userFromDb.getInt("min_distance");
			String loginUser = UPDATE + USER_TABLE + SET + USER_LOGINKEY + "= '" + loginKey + "', login_timestamp = '" + timeStamp + "' " + WHERE + USER_EMAIL + "= '" + email + "';";
			int success = statement.executeUpdate(loginUser);
			if(success != 1) {
				return null;
			}
			
		
				
		
		
		dbConnection.closeConn();
		return new UserData(email, password, firstName, lastName, picture, loginKey, ""+timeStamp, null, null, ""+logoutSessionTime, ""+geoPushInterval, ""+minDistance);
	}
	
	
	
	@Override
	public boolean logoutUser(String loginKey) throws ArgumentMissingException {
		
		if(loginKey == null || loginKey.isEmpty()) {
			throw new ArgumentMissingException();
		}
		
		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();
		
		String logoutUser = UPDATE + USER_TABLE + SET + USER_LOGINKEY + "= " + "NULL " + WHERE + USER_LOGINKEY + "= '" + loginKey + "';";
		try {
			int success = statement.executeUpdate(logoutUser);
			if(success != 1) {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		dbConnection.closeConn();
		
		return true;
	}

	@Override
	public boolean changePassword(String userMail, String newPassword) throws UserNotFoundException {
		
		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();
		
		//check if user exists and if the password is correct
		String getUserFromDb = SELECT + "* " + FROM + USER_TABLE + WHERE + USER_EMAIL + "= '" + userMail +"';";
		ResultSet userFromDb;
		try {
					
			userFromDb = statement.executeQuery(getUserFromDb);
			if(userFromDb.isAfterLast()) {
				throw new UserNotFoundException();
			}
			
			String setNewPassword = UPDATE + USER_TABLE + SET + USER_PASSWORD + "= '" + newPassword + "' " + WHERE + USER_EMAIL + "= '" + userMail + "';";
			int success = statement.executeUpdate(setNewPassword);
			if(success != 1) {
				return false;
			}
					
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		dbConnection.closeConn();
		
		return true;
	}

	@Override

	public Location checkIn(String key, String venueId, String timestamp) throws ArgumentMissingException, InvalidKeyException {

		
		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();
		
		if(key == null || venueId == null || timestamp == null || key.isEmpty() || venueId.isEmpty() || timestamp.isEmpty()) {
			throw new ArgumentMissingException();
		}
		
		//check if key is valid
		String getKeyFromDb = SELECT + "* " + FROM + USER_TABLE + WHERE + USER_LOGINKEY + "= '" + key +"';";
		ResultSet keyFromDb;

		String message;
		List<ReviewData> rd = null;

		try {
							
			keyFromDb = statement.executeQuery(getKeyFromDb);
			if(keyFromDb.isAfterLast()) {
				throw new InvalidKeyException();
			}
			
			//maybe insert the venue
			String venueInsert = INSERT_IGNORE_INTO + LOCATIONS_TABLE + "(" + LOCATIONS_FSQUARE_VENUE_ID + ") " + VALUES + "('" + venueId +"');";
			statement.executeUpdate(venueInsert);
			
			//insert the checkIn

			String insertCheckin = INSERT_IGNORE_INTO + CHECKIN_TABLE + "(" + CHECKIN_LOCATION_ID + ", " + CHECKIN_USER_ID + ", " + CHECKIN_CHECKIN_TIMESTAMP + ") " +
									 "(" + 
									SELECT + DISTINCT + "t5_locations." + LOCATIONS_ID + ", t5_users." + USER_ID + ", " + "'" + timestamp + "' " +
									FROM + LOCATIONS_TABLE + ", " + USER_TABLE +
									WHERE + USER_LOGINKEY + "= '" + key + "' " + AND + LOCATIONS_FSQUARE_VENUE_ID + "= " + "'" + venueId + "'"
									+ ");";
		
			int resCheckIn = statement.executeUpdate(insertCheckin);
			if (resCheckIn == 1) { // if checked in
				
				return this.getReviews(venueId, true);
				
		/*	ResultSet resReviewsCheck;
				resReviewsCheck = statement.executeQuery(SELECT + "* " + FROM + REVIEWS_TABLE + WHERE + "locations_id = (" + SELECT + "id " +
														FROM + LOCATIONS_TABLE + WHERE + LOCATIONS_FSQUARE_VENUE_ID +"= '" + venueId + "');");
				if (resReviewsCheck.next()) { // if at least one review exists
					rd = new ArrayList<ReviewData>();
			
					ResultSet resReviews = statement.executeQuery(SELECT + "* " + FROM + REVIEWS_TABLE + WHERE + "locations_id = (" + SELECT + "id " +
							FROM + LOCATIONS_TABLE + WHERE + LOCATIONS_FSQUARE_VENUE_ID +"= '" + venueId + "') LIMIT 0, 10;");
					while (resReviews.next()) {
						int userId = resReviews.getInt("users_id");
						int  rating = resReviews.getInt("rating");
						String title = resReviews.getString("review_title");
						String review = resReviews.getString("review_description");
						String picture = resReviews.getString("review_picture");
						DBCon dbConnection1 = new DBCon();
						Statement statement1 = dbConnection1.getStatement();
						ResultSet resUserById = null;
						resUserById = statement1.executeQuery(SELECT + "* " + FROM + USER_TABLE + WHERE + "id = " + userId);
						resUserById.next();
						String userFirstName = resUserById.getString("first_name");
						String userLastName = resUserById.getString("last_name");
						String userEmail = resUserById.getString("email");
						dbConnection1.closeConn();
						rd.add(new ReviewData(userId, userFirstName, userLastName, userEmail, rating, title, review, picture)); // adds reviews into reviews list 

					}
					dbConnection.closeConn();
					return new Location("true", message, new LocationData(venueId, rd)); // returns list of reviews
					
				} else {					
					// no reviews left
					message = message + ". No reviews left for this place";
					dbConnection.closeConn();
					return new Location("true", message, new LocationData(venueId, rd)); // returns empty list of reviews
				}
				
				
				*/
			} else {
				// failed to check in
				message = "Failed to check in";
				dbConnection.closeConn();
				return new Location("false", message, new LocationData(venueId, rd)); //returns false and empty list of reviews
			}
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		

	}
	
	public Location getReviews(String venueId, boolean checkedIn) throws SQLException {
		
		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();
		ResultSet resReviewsCheck;
		List<ReviewData> rd = null;
		String message;
	try {	resReviewsCheck = statement.executeQuery(SELECT + "* " + FROM + REVIEWS_TABLE + WHERE + "locations_id = (" + SELECT + "id " +
												FROM + LOCATIONS_TABLE + WHERE + LOCATIONS_FSQUARE_VENUE_ID +"= '" + venueId + "');");
		if (resReviewsCheck.next()) { // if at least one review exists
			rd = new ArrayList<ReviewData>();
	
			ResultSet resReviews = statement.executeQuery(SELECT + "* " + FROM + REVIEWS_TABLE + WHERE + "locations_id = (" + SELECT + "id " +
					FROM + LOCATIONS_TABLE + WHERE + LOCATIONS_FSQUARE_VENUE_ID +"= '" + venueId + "') LIMIT 0, 10;");
			while (resReviews.next()) {
				int userId = resReviews.getInt("users_id");
				int  rating = resReviews.getInt("rating");
				String title = resReviews.getString("review_title");
				String review = resReviews.getString("review_description");
				String picture = resReviews.getString("review_picture");
				DBCon dbConnection1 = new DBCon();
				Statement statement1 = dbConnection1.getStatement();
				ResultSet resUserById = null;
				resUserById = statement1.executeQuery(SELECT + "* " + FROM + USER_TABLE + WHERE + "id = " + userId);
				resUserById.next();
				String userFirstName = resUserById.getString("first_name");
				String userLastName = resUserById.getString("last_name");
				String userEmail = resUserById.getString("email");
				dbConnection1.closeConn();
				rd.add(new ReviewData(userId, userFirstName, userLastName, userEmail, rating, title, review, picture)); // adds reviews into reviews list 

			}
			dbConnection.closeConn();
			if (checkedIn) {
				message = "Checked in successfully";
			} else {
			message = "List of reviews for this place";
			}
			return new Location("true", message, new LocationData(venueId, rd)); // returns list of reviews
			
		} else {					
			// no reviews left
			if (checkedIn) {
				message = "Checked in successfully. Nobody left a review yet";
			} else {
			message = "Nobody left a review yet";
			}
			dbConnection.closeConn();
			return new Location("true", message, new LocationData(venueId, rd)); // returns empty list of reviews
		}
	} catch (SQLException e) {
		e.printStackTrace();
		return null;
	}

	}


	@Override
	public boolean updateSettings(String key, int minDistance,
			int logoutSessionTime, int geoPushInterval) throws InvalidKeyException {
		
		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();
		
		//check if key is valid
		String getKeyFromDb = SELECT + "* " + FROM + USER_TABLE + WHERE + USER_LOGINKEY + "= '" + key +"';";
		ResultSet keyFromDb;
		try {
							
			keyFromDb = statement.executeQuery(getKeyFromDb);
			if(!keyFromDb.next()) {
				throw new InvalidKeyException();
				
			}
			
			String updateSettings = UPDATE + USER_TABLE + 
					SET + USER_MIN_DISTANCE + "= '" + minDistance +"', logout_session_time  = '" + logoutSessionTime +"', " + USER_GEO_PUSH_INTERVAL + "= '" + geoPushInterval + "' " +
					WHERE + USER_LOGINKEY + "= '" + key + "';";
			statement.executeUpdate(updateSettings);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		dbConnection.closeConn();
		
		return true;
	}
	
	@Override
	public boolean storeNewReview(String key, String venueId, int rating, String reviewTitle, String reviewDescription, String imageUri) throws InvalidKeyException {
		
		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();
		
		//check if key is valid
		String getKeyFromDb = SELECT + "* " + FROM + USER_TABLE + WHERE + USER_LOGINKEY + "= '" + key +"';";
		ResultSet keyFromDb;
		try {
							
			keyFromDb = statement.executeQuery(getKeyFromDb);
			if(!keyFromDb.next()) {
				throw new InvalidKeyException();
			}
			
			String userId = keyFromDb.getString("id");
			
			//insert venueId if neccessary
			String venueInsert = INSERT_IGNORE_INTO + LOCATIONS_TABLE + "(" + LOCATIONS_FSQUARE_VENUE_ID + ") " + VALUES + "('" + venueId +"');";
			statement.executeUpdate(venueInsert);
			
			//get locationId
			String getVenueId = SELECT + LOCATIONS_ID + FROM + LOCATIONS_TABLE + WHERE + LOCATIONS_FSQUARE_VENUE_ID + "= '" + venueId + "';";
			ResultSet getVenueIdResult = statement.executeQuery(getVenueId);
			getVenueIdResult.next();
			String locationId = getVenueIdResult.getString("id");
			
			//insert review
			String insertReview = INSERT_IGNORE_INTO + REVIEWS_TABLE + "( " + 
					REVIEWS_USER_ID + ", " + REVIEWS_LOCATION_ID + ", " + REVIEWS_RATING + ", " + REVIEWS_REVIEW_TITLE + ", " + REVIEWS_REVIEW_DESCRIPTION + ", " + REVIEWS_REVIEW_PICTURE + ") " +
					VALUES + "( " + "'" + userId + "', '" + locationId + "', '" + rating + "', '" + reviewTitle + "', '" + reviewDescription + "', '" + imageUri + "');";
			statement.executeUpdate(insertReview);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		dbConnection.closeConn();

		
		return true;
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
