package com.rest.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.rest.comment.model.data.CommentData;
import com.rest.location.model.Location;
import com.rest.review.model.Review;
import com.rest.review.model.data.ReviewData;
import com.rest.user.model.User;
import com.rest.user.model.data.UserData;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.EmailAlreadyExistsException;
import com.rest.utils.exceptions.InputTooLongException;
import com.rest.utils.exceptions.InvalidKeyException;
import com.rest.utils.exceptions.PasswordWrongException;
import com.rest.utils.exceptions.ReviewNotFoundException;
import com.rest.utils.exceptions.UserNotFoundException;
import com.rest.utils.exceptions.WrongEmailFormatException;

public class DatabaseAccess implements DatabaseAccessInterface {
	
	//for getting SQL statements
	private QueriesGenerator queriesGenerator;
	
	// maximum allowed inputLenght according to the database ddl
	private final static int MAX_INPUT_LENGTH = 200;

	// static Strings for SQL Queries
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
	
	/*
	 * Static Strings from the DDL
	 */
	// static Strings for the table t5_user
	private final static String USER_TABLE = "t5_users ";
	private final static String USER_ID = "id ";
	private final static String USER_EMAIL = "email ";
	private final static String USER_PASSWORD = "passwd ";
	private final static String USER_FIRSTNAME = "first_name ";
	private final static String USER_LASTNAME = "last_name ";
	private final static String USER_LOGINKEY = "login_key ";
	private final static String USER_GEO_PUSH_INTERVAL = "geo_push_interval ";
	private final static String USER_MIN_DISTANCE = "min_distance ";
	private final static String USER_PICTURE = "picture ";
	private final static String USER_LOGOUT_TIME = "logout_session_time ";
	// static Strings for the table t5_locations
	private final static String LOCATIONS_TABLE = "t5_locations ";
	private final static String LOCATIONS_ID = "id ";
	private final static String LOCATIONS_FSQUARE_VENUE_ID = "foursquare_venue_id ";
	// static Stings for the table t5_users_reviews
	private static final String REVIEWS_TABLE = "t5_users_reviews ";
	private static final String REVIEWS_ID = "id";
	private static final String REVIEWS_USER_ID = "users_id ";
	private static final String REVIEWS_LOCATION_ID = "locations_id ";
	private static final String REVIEWS_RATING = "rating ";
	private static final String REVIEWS_REVIEW_TITLE = "review_title ";
	private static final String REVIEWS_REVIEW_DESCRIPTION = "review_description ";
	private static final String REVIEWS_REVIEW_PICTURE = "review_picture ";
	private static final String REVIEWS_TOTAL_VOTE_UP = "total_vote_up ";
	private static final String REVIEWS_TOTAL_VOTE_DOWN = "total_vote_down ";
	private static final String REVIEWS_SPAMS = "spams ";
	// static Strings for t5_users_reviews_comments
	private static final String REVIEWS_COMMENTS_TABLE = "t5_users_reviews_comments ";
	private static final String REVIEWS_COMMENTS_ID = "id ";
	private static final String REVIEWS_COMMENTS_USER_ID = "user_id ";
	private static final String REVIEWS_COMMENTS_USER_REVIEWS_ID = "user_reviews_id ";
	private static final String REVIEWS_COMMENTS_COMMENT = "comment ";
	// static Strings for the table t5_checkins
	private static final String CHECKIN_TABLE = "t5_checkins ";
	private static final String CHECKIN_USER_ID = "users_id ";
	private static final String CHECKIN_LOCATION_ID = "locations_id ";
	private static final String CHECKIN_CHECKIN_TIMESTAMP = "checkin_timestamp ";
	// static Strings for the table t5_connections
	private static final String CONNECTIONS_TABLE = "t5_connections ";
	private static final String CONNECTIONS_MY_ID = "my_id ";
	private static final String CONNECTIONS_FRIENDS_ID = "friends_id ";
	
	public DatabaseAccess() {
		queriesGenerator = new QueriesGenerator();
	}

	@Override
	public UserData registerNewUser(String email, String password,
			String firstName, String lastName, String picture)
			throws WrongEmailFormatException, InputTooLongException,
			ArgumentMissingException, EmailAlreadyExistsException {

		if (email == null || password == null || email.isEmpty()
				|| password.isEmpty()) {
			throw new ArgumentMissingException();
		}

		boolean inputNotTooLong = checkInputLength(email)
				&& checkInputLength(password) && checkInputLength(firstName)
				&& checkInputLength(lastName);
		if (!inputNotTooLong) {
			throw new InputTooLongException();
		}

		boolean rightEmail = checkEmailFormat(email);
		if (!rightEmail) {
			throw new WrongEmailFormatException();
		}

		// get access to the database
		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		// check if another user with email already exists in the database
		ResultSet resultEmailAlreadyExists;
		try {
			resultEmailAlreadyExists = statement
					.executeQuery(queriesGenerator.existsEmailInDbQuery(email));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		try {
			if (resultEmailAlreadyExists.next()) {
				throw new EmailAlreadyExistsException();
			}
			
			int success = statement.executeUpdate(queriesGenerator.insertNewUser(email, password,
					firstName, lastName, picture));
			
			if (success != 1) {
				return null;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		dbConnection.closeConn();

		return new UserData(email, password, firstName, lastName, picture,
				null, null, null, null, null, null, null);
	}

	/**
	 * 
	 * @param input
	 * @return false, if input.lenght() > MAX_INPUT_LENGTH, true otherwise
	 */
	private boolean checkInputLength(String input) {

		if (input == null) {
			return true;
		} else if (input.length() <= MAX_INPUT_LENGTH) {
			return true;
		}
		return false;
	}

	private boolean checkEmailFormat(String email) {

		if (email.startsWith("@"))
			return false;
		if (email.endsWith("@"))
			return false;
		if (!email.contains("@"))
			return false;
		if (!email.contains("."))
			return false;
		if (email.startsWith("."))
			return false;
		if (email.endsWith("."))
			return false;
		if (email.contains(" "))
			return false;

		return true;
	}

	@Override
	public UserData loginUser(String email, String password)
			throws ArgumentMissingException, UserNotFoundException,
			PasswordWrongException {

		if (email == null || password == null || email.isEmpty()
				|| password.isEmpty()) {
			throw new ArgumentMissingException();
		}

		// get access to the database
		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		// check if user exists and if the password is correct
		String firstName;
		String lastName;
		String loginKey;
		String picture;
		int logoutSessionTime;
		int geoPushInterval;
		int minDistance;
		long timeStamp;

		try {
			ResultSet userFromDb = statement.executeQuery(queriesGenerator.getUserByEmail(email));
			
			if (!userFromDb.next()) {
				throw new UserNotFoundException();
			}
			
			String passwordInDb = userFromDb.getString(QueriesGenerator.getUserPassword());
			if (!passwordInDb.equals(password)) {
				throw new PasswordWrongException();
			}

			// log in the user
			timeStamp = System.currentTimeMillis(); //create the login Key
			loginKey = email + timeStamp;
			loginKey = SHA1.stringToSHA(loginKey);
			
			firstName = userFromDb.getString(QueriesGenerator.getUserFirstname());
			lastName = userFromDb.getString(QueriesGenerator.getUserLastname());
			picture = userFromDb.getString(QueriesGenerator.getUserPicture());
			logoutSessionTime = userFromDb.getInt(QueriesGenerator.getUserLogoutTime());
			geoPushInterval = userFromDb.getInt(QueriesGenerator.getUserGeoPushInterval());
			minDistance = userFromDb.getInt(QueriesGenerator.getUserMinDistance());
			
			int success = statement.executeUpdate(queriesGenerator.loginUser(loginKey, timeStamp, email));
			if (success != 1) {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		

		dbConnection.closeConn();
		return new UserData(email, password, firstName, lastName, picture,
				loginKey, null, "" + timeStamp, null, "" + logoutSessionTime,
				"" + geoPushInterval, "" + minDistance);
	}

	@Override
	public boolean logoutUser(String key) throws ArgumentMissingException {

		if (key == null || key.isEmpty()) {
			throw new ArgumentMissingException();
		}

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		try {
			int success = statement.executeUpdate(queriesGenerator.logoutUser(key));
			if (success != 1) {
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
	public boolean changePassword(String userMail, String newPassword)
			throws UserNotFoundException {

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		// check if user exists and if the password is correct
		String getUserFromDb = SELECT + "* " + FROM + USER_TABLE + WHERE
				+ USER_EMAIL + "= '" + userMail + "';";
		ResultSet userFromDb;
		try {

			userFromDb = statement.executeQuery(getUserFromDb);
			if (!userFromDb.next()) {
				throw new UserNotFoundException();
			}

			String setNewPassword = UPDATE + USER_TABLE + SET + USER_PASSWORD
					+ "= '" + newPassword + "' " + WHERE + USER_EMAIL + "= '"
					+ userMail + "';";
			int success = statement.executeUpdate(setNewPassword);
			if (success != 1) {
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
	public Location checkIn(String key, String venueId, String timestamp)
			throws ArgumentMissingException, InvalidKeyException {

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		if (key == null || venueId == null || timestamp == null
				|| key.isEmpty() || venueId.isEmpty() || timestamp.isEmpty()) {
			throw new ArgumentMissingException();
		}

		// check if key is valid
		String getKeyFromDb = queriesGenerator.getUserByKey(key);
		ResultSet keyFromDb;

		String message;
		try {

			keyFromDb = statement.executeQuery(getKeyFromDb);
			if (!keyFromDb.next()) {
				throw new InvalidKeyException();
			}

			// maybe insert the venue
			statement.executeUpdate(queriesGenerator.insertNewVenue(venueId));

			// insert the checkIn
			int resCheckIn = statement.executeUpdate(queriesGenerator.insertCheckin(timestamp, key, venueId));
			dbConnection.closeConn();
			if (resCheckIn == 1) { // if checked in

				return new Location("true", "Checked in successfully");

			} else {
				// failed to check in
				message = "Failed to check in";
				return new Location("false", message);
			}
			
			

		} catch (SQLException e) {
			message = "Failed to check in";
			return new Location("false", message);
		}

	}

	public Review getReviews(String venueId) {

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();
		
		List<ReviewData> reviewData = null;
		String message;
		
		try {
			//get reviews from Database
			ResultSet resReviews = statement.executeQuery(queriesGenerator.getReviewsForVenue(venueId));
			if (resReviews.next()) { // if at least one review exists
				
				reviewData = new ArrayList<ReviewData>();
				
				resReviews.beforeFirst();
				while (resReviews.next()) {

					int userId = resReviews.getInt(QueriesGenerator.getReviewsUserId());
					int rating = resReviews.getInt(QueriesGenerator.getReviewsRating());
					String title = resReviews.getString(QueriesGenerator.getReviewsReviewTitle());
					String review = resReviews.getString(QueriesGenerator.getReviewsReviewDescription());
					String picture = resReviews.getString(QueriesGenerator.getReviewsReviewPicture());
					
					DBCon dbConnection1 = new DBCon();
					Statement statement1 = dbConnection1.getStatement();
					
					ResultSet resUserById = null;
					resUserById = statement1.executeQuery(queriesGenerator.getUserById(userId));
					
					resUserById.next();
					String userFirstName = resUserById.getString(QueriesGenerator.getUserId());
					String userLastName = resUserById.getString(QueriesGenerator.getUserLastname());
					String userEmail = resUserById.getString(QueriesGenerator.getUserEmail());
					String profilePicture = resUserById.getString(QueriesGenerator.getUserPicture());
					
					if (profilePicture == null) {
						profilePicture = "";
					}
					dbConnection1.closeConn();
					reviewData.add(new ReviewData(userId, userFirstName, userLastName,
							userEmail, profilePicture, rating, title, review,
							picture)); // adds reviews into reviews list

				}
				dbConnection.closeConn();

				message = "List of reviews for this place";

				return new Review("true", message, reviewData); // returns list of
														// reviews

			} else {
				// no reviews left

				message = "Nobody left a review yet";

				dbConnection.closeConn();
				return new Review("true", message, reviewData); // returns empty list of
														// reviews
			}
		} catch (SQLException e) {
			message = "Error";

			dbConnection.closeConn();
			return new Review("false", message, reviewData);
		}

	}

	@Override
	public boolean updateSettings(String key, int minDistance,
			int logoutSessionTime, int geoPushInterval)
			throws InvalidKeyException {

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		// check if key is valid
		String getKeyFromDb = queriesGenerator.getUserByKey(key);
		ResultSet keyFromDb;
		try {

			keyFromDb = statement.executeQuery(getKeyFromDb);
			if (!keyFromDb.next()) {
				throw new InvalidKeyException();

			}

			statement.executeUpdate(queriesGenerator.updateSettings(minDistance, logoutSessionTime, geoPushInterval, key));

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		dbConnection.closeConn();

		return true;
	}

	@Override
	public boolean storeNewReview(String key, String venueId, int rating,
			String reviewTitle, String reviewDescription, String imageUri)
			throws InvalidKeyException {

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		// check if key is valid
		String getKeyFromDb = queriesGenerator.getUserByKey(key);
		ResultSet keyFromDb;
		try {

			keyFromDb = statement.executeQuery(getKeyFromDb);
			if (!keyFromDb.next()) {
				throw new InvalidKeyException();
			}

			String userId = keyFromDb.getString(QueriesGenerator.getUserId());

			// insert venueId if neccessary
			statement.executeUpdate(queriesGenerator.insertNewVenue(venueId));

			// get locationId
			ResultSet getLocationId = statement.executeQuery(queriesGenerator.getLocationIdFromLocationsByVenueId(venueId));
			getLocationId.next();
			String locationId = getLocationId.getString(0);

			// insert review
			statement.executeUpdate(queriesGenerator.insertNewReview(userId, locationId, rating, reviewTitle, reviewDescription, imageUri));

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
		String deleteUser = queriesGenerator.deleteUserTable();
		String deleteLocations = queriesGenerator.deleteLocationsTable();
		String deleteReviews = queriesGenerator.deleteReviewsTable();
		String deleteCheckIns = queriesGenerator.deleteCheckinsTable();
		String deleteConnections = queriesGenerator.deleteConnectionsTable();
		try {
			s.executeUpdate(deleteUser);
			s.executeUpdate(deleteLocations);
			s.executeUpdate(deleteReviews);
			s.executeUpdate(deleteCheckIns);
			s.executeUpdate(deleteConnections);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean vote(String key, String reviewId, int vote)
			throws InvalidKeyException, ReviewNotFoundException {

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		// check if key is valid
		String getKeyFromDb = SELECT + "* " + FROM + USER_TABLE + WHERE
				+ USER_LOGINKEY + "= '" + key + "';";
		ResultSet keyFromDb;
		try {

			keyFromDb = statement.executeQuery(getKeyFromDb);
			if (!keyFromDb.next()) {
				throw new InvalidKeyException();
			}

			keyFromDb.getString("id");

			// check if reviewId is valid
			String getReviewId = SELECT + "* " + FROM + REVIEWS_TABLE + WHERE
					+ REVIEWS_ID + "= '" + reviewId + "';";
			ResultSet reviewIdFromDb = statement.executeQuery(getReviewId);

			if (!reviewIdFromDb.next()) {
				throw new ReviewNotFoundException();
			}

			// update the review
			String updateReview;
			int newVote;
			if (vote == 0) {
				newVote = reviewIdFromDb.getInt(REVIEWS_TOTAL_VOTE_DOWN) + 1;
				updateReview = UPDATE + REVIEWS_TABLE + SET
						+ REVIEWS_TOTAL_VOTE_DOWN + "= " + newVote + " "
						+ WHERE + REVIEWS_ID + "= '" + reviewId + "';";
			} else {
				newVote = reviewIdFromDb.getInt(REVIEWS_TOTAL_VOTE_UP) + 1;
				updateReview = UPDATE + REVIEWS_TABLE + SET
						+ REVIEWS_TOTAL_VOTE_UP + "= " + newVote + " " + WHERE
						+ REVIEWS_ID + "= '" + reviewId + "';";
			}
			statement.executeUpdate(updateReview);

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		dbConnection.closeConn();

		return true;
	}

	public boolean follow(String key, String reviewer_id)
			throws InvalidKeyException, UserNotFoundException {

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		// check if key is valid
		String getKeyFromDb = SELECT + "* " + FROM + USER_TABLE + WHERE
				+ USER_LOGINKEY + "= '" + key + "';";
		ResultSet keyFromDb;
		try {

			keyFromDb = statement.executeQuery(getKeyFromDb);
			if (!keyFromDb.next()) {
				throw new InvalidKeyException();
			}

			String my_id = keyFromDb.getString(USER_ID);

			// check if reviewer_id exists
			String getId = SELECT + "* " + FROM + USER_TABLE + WHERE + USER_ID
					+ "= '" + reviewer_id + "';";
			statement.executeQuery(getId);

			if (!keyFromDb.next()) {
				throw new UserNotFoundException();
			}

			String setNewFollow = INSERT_IGNORE_INTO + CONNECTIONS_TABLE + "( "
					+ CONNECTIONS_MY_ID + ", " + CONNECTIONS_FRIENDS_ID + ") "
					+ VALUES + "( '" + my_id + "', '" + reviewer_id + "');";
			statement.executeUpdate(setNewFollow);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		dbConnection.closeConn();

		return true;
	}

	public boolean putComment(String key, String reviewId, String comment)
			throws ReviewNotFoundException, InvalidKeyException {

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		// check if key is valid
		String getKeyFromDb = SELECT + "* " + FROM + USER_TABLE + WHERE
				+ USER_LOGINKEY + "= '" + key + "';";
		ResultSet keyFromDb;
		try {

			keyFromDb = statement.executeQuery(getKeyFromDb);
			if (!keyFromDb.next()) {
				throw new InvalidKeyException();
			}

			String userId = keyFromDb.getString("id");

			// check if reviewId is valid
			String getReviewId = SELECT + "* " + FROM + REVIEWS_TABLE + WHERE
					+ REVIEWS_ID + "= '" + reviewId + "';";
			ResultSet reviewIdFromDb = statement.executeQuery(getReviewId);

			if (!reviewIdFromDb.next()) {
				throw new ReviewNotFoundException();
			}

			// insert the comment
			String insertComment = INSERT_IGNORE_INTO + REVIEWS_COMMENTS_TABLE
					+ "( " + REVIEWS_COMMENTS_USER_ID + ", "
					+ REVIEWS_COMMENTS_USER_REVIEWS_ID + ", "
					+ REVIEWS_COMMENTS_COMMENT + ") " + VALUES + "( '" + userId
					+ "', '" + reviewId + "', '" + comment + "');";
			statement.executeUpdate(insertComment);

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		dbConnection.closeConn();

		return true;
	}

	public ArrayList<CommentData> getCommentsForReview(String reviewId)
			throws ReviewNotFoundException {

		ArrayList<CommentData> result = new ArrayList<CommentData>();

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		// check if reviewId is valid
		String getReviewIdFromDb = SELECT + "* " + FROM + REVIEWS_TABLE + WHERE
				+ REVIEWS_ID + "= '" + reviewId + "';";
		ResultSet reviewIdFromDb;
		try {

			reviewIdFromDb = statement.executeQuery(getReviewIdFromDb);
			if (!reviewIdFromDb.next()) {
				throw new ReviewNotFoundException();
			}

			String getComments = SELECT + "*" + FROM + REVIEWS_COMMENTS_TABLE
					+ WHERE + REVIEWS_COMMENTS_USER_REVIEWS_ID + "= '"
					+ reviewId + "';";
			ResultSet commentsFromDb = statement.executeQuery(getComments);

			while (commentsFromDb.next()) {
				result.add(new CommentData(commentsFromDb
						.getString(REVIEWS_COMMENTS_ID), commentsFromDb
						.getString(REVIEWS_COMMENTS_USER_ID), commentsFromDb
						.getString(REVIEWS_COMMENTS_USER_REVIEWS_ID),
						commentsFromDb.getString(REVIEWS_COMMENTS_COMMENT)));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return result;
		}

		dbConnection.closeConn();

		return result;

	}

	public boolean updateUser(String key, String password,
			String firstName, String lastName, String profileImage)
			throws InvalidKeyException {

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		// check if key is valid
		String getKeyFromDb = SELECT + "* " + FROM + USER_TABLE + WHERE
				+ USER_LOGINKEY + "= '" + key + "';";
		ResultSet keyFromDb;
		try {

			keyFromDb = statement.executeQuery(getKeyFromDb);
			if (!keyFromDb.next()) {
				throw new InvalidKeyException();
			}

			String updateUser = UPDATE + USER_TABLE + SET + USER_PASSWORD
					+ "= '" + password + "', " + USER_FIRSTNAME + "= '"
					+ firstName + "', " + USER_LASTNAME + "= '" + lastName
					+ "', " + USER_PICTURE + "= '" + profileImage + "';";
			int success = statement.executeUpdate(updateUser);
			if (success != 1) {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		dbConnection.closeConn();

		return true;
	}

	public User getUserProfile(String key) throws InvalidKeyException {

		String firstName = "";
		String lastName = "";
		String email = "";
		String picture = "";
		List<UserData> friends = new ArrayList<UserData>();

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		// check if key is valid
		String getUser = SELECT + "* " + FROM + USER_TABLE + WHERE
				+ USER_LOGINKEY + "= '" + key + "';";
		ResultSet userFromDb;
		try {

			userFromDb = statement.executeQuery(getUser);
			if (!userFromDb.next()) {
				throw new InvalidKeyException();
			}

			// set the user data
			firstName = userFromDb.getString(USER_FIRSTNAME);
			lastName = userFromDb.getString(USER_LASTNAME);
			email = userFromDb.getString(USER_EMAIL);
			picture = userFromDb.getString(USER_PICTURE);

			// get the friends of the user
			String getFriends = SELECT + CONNECTIONS_FRIENDS_ID + FROM
					+ CONNECTIONS_TABLE + WHERE + CONNECTIONS_MY_ID + "= '"
					+ userFromDb.getString(USER_ID) + "';";
			ResultSet friendsIds = statement.executeQuery(getFriends);

			while (friendsIds.next()) {

				String id = friendsIds.getString(CONNECTIONS_FRIENDS_ID);

				String getUserDetails = SELECT + "* " + FROM + USER_TABLE
						+ WHERE + USER_ID + "= '" + id + "';";
				ResultSet friendFromDb = statement.executeQuery(getUserDetails);

				UserData friend = new UserData(
						friendFromDb.getString(USER_EMAIL), "",
						friendFromDb.getString(USER_FIRSTNAME),
						friendFromDb.getString(USER_LASTNAME),
						friendFromDb.getString(USER_PICTURE), "", "", "", "",
						"", "", "");
				friends.add(friend);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		dbConnection.closeConn();

		return new User("true", "", new UserData(email, "", firstName,
				lastName, picture, "", "", "", "", "", "", ""));
	}

	public ArrayList<ReviewData> getReviewsForUser(int userId)
			throws UserNotFoundException {

		ArrayList<ReviewData> result = new ArrayList<ReviewData>();

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		// check if userId is valid
		String getUser = SELECT + "* " + FROM + USER_TABLE + WHERE + USER_ID
				+ "= " + userId + ";";
		ResultSet userFromDb;
		try {

			userFromDb = statement.executeQuery(getUser);
			if (!userFromDb.next()) {
				throw new UserNotFoundException();
			}

			String userFirstName = userFromDb.getString(USER_FIRSTNAME);
			String userLastName = userFromDb.getString(USER_LASTNAME);
			String userEMail = userFromDb.getString(USER_EMAIL);
			String userPicture = userFromDb.getString(USER_PICTURE);

			// get the reviews of this user
			String getReviews = SELECT + "*" + FROM + REVIEWS_TABLE + WHERE
					+ REVIEWS_USER_ID + "= " + userId + ";";
			ResultSet reviewsFromDb = statement.executeQuery(getReviews);

			while (reviewsFromDb.next()) {

				String locationId = reviewsFromDb
						.getString(REVIEWS_LOCATION_ID);
				String title = reviewsFromDb.getString(REVIEWS_REVIEW_TITLE);
				String review = reviewsFromDb
						.getString(REVIEWS_REVIEW_DESCRIPTION);
				String locPicture = reviewsFromDb
						.getString(REVIEWS_REVIEW_PICTURE);
				int rating = reviewsFromDb.getInt(REVIEWS_RATING);

				result.add(new ReviewData(userId, userFirstName, userLastName,
						userEMail, userPicture, rating, title, review,
						locPicture, locationId));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		dbConnection.closeConn();

		return result;
	}
}
