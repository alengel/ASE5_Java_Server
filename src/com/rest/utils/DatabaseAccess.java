package com.rest.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.rest.comment.model.data.CommentData;
import com.rest.location.model.Location;
import com.rest.location.model.data.LocationData;
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
	
	// maximum allowed inputLength according to the database ddl
	private final static int MAX_INPUT_LENGTH = 200;

	// static Strings for SQL Queries
	private final static String SELECT = "Select ";
	private final static String UPDATE = "Update ";
	private final static String SET = "Set ";
	private final static String FROM = "from ";
	private final static String WHERE = "where ";
	
	/*
	 * Static Strings from the DDL
	 */
	// static Strings for the table t5_user
	private final static String USER_TABLE = "t5_users ";
	private final static String USER_EMAIL = "email ";
	private final static String USER_PASSWORD = "passwd ";
	private final static String USER_LOGINKEY = "login_key ";
	private final static String USER_PASSWORD_KEY = "password_key ";
	
	
	
	
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
			e.printStackTrace();
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
			String locationId = getLocationId.getString(1);

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

	public boolean vote(String key, int reviewId, int vote)
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

			// check if reviewId is valid
			String getReviewId = queriesGenerator.getReviewsById(reviewId);
			ResultSet reviewIdFromDb = statement.executeQuery(getReviewId);

			if (!reviewIdFromDb.next()) {
				throw new ReviewNotFoundException();
			}

			// update the review
			String updateReview;
			int newVote;
			if (vote == 0) {
				newVote = reviewIdFromDb.getInt(QueriesGenerator.getReviewsTotalVoteDown()) + 1;
				updateReview = queriesGenerator.updateReviewsVoteDown(reviewId, newVote);
			} else {
				newVote = reviewIdFromDb.getInt(QueriesGenerator.getReviewsTotalVoteUp()) + 1;
				updateReview = queriesGenerator.updateReviewsVoteUp(reviewId, newVote);
			}
			statement.executeUpdate(updateReview);

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		dbConnection.closeConn();

		return true;
	}

	public boolean follow(String key, int reviewer_id)
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

			String my_id = keyFromDb.getString(QueriesGenerator.getUserId());

			// check if reviewer_id exists
			String getId = queriesGenerator.getUserById(reviewer_id);
			ResultSet resReviwerId = statement.executeQuery(getId);

			if (!resReviwerId.next()) {
				throw new UserNotFoundException();
			}

			String setNewFollow = queriesGenerator.insertNewFollow(my_id, reviewer_id);
			statement.executeUpdate(setNewFollow);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		dbConnection.closeConn();

		return true;
	}

	public boolean putComment(String key, int reviewId, String comment)
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

			String userId = keyFromDb.getString(QueriesGenerator.getUserId());

			// check if reviewId is valid
			ResultSet reviewIdFromDb = statement.executeQuery(queriesGenerator.getReviewsById(reviewId));

			if (!reviewIdFromDb.next()) {
				throw new ReviewNotFoundException();
			}

			// insert the comment
			statement.executeUpdate(queriesGenerator.insertNewComment(userId, reviewId, comment));

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		dbConnection.closeConn();

		return true;
	}

	public ArrayList<CommentData> getCommentsForReview(int reviewId)
			throws ReviewNotFoundException {

		ArrayList<CommentData> result = new ArrayList<CommentData>();

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		// check if reviewId is valid
		ResultSet reviewIdFromDb;
		try {

			reviewIdFromDb = statement.executeQuery(queriesGenerator.getReviewsById(reviewId));
			if (!reviewIdFromDb.next()) {
				throw new ReviewNotFoundException();
			}

			ResultSet commentsFromDb = statement.executeQuery(queriesGenerator.getCommentsByReviewId(reviewId));

			while (commentsFromDb.next()) {
				result.add(new CommentData(commentsFromDb
						.getString(QueriesGenerator.getReviewsCommentsId()), commentsFromDb
						.getString(QueriesGenerator.getReviewsCommentsUserId()), commentsFromDb
						.getString(QueriesGenerator.getReviewsCommentsUserReviewsId()),
						commentsFromDb.getString(QueriesGenerator.getReviewsCommentsComment())));
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

			int success = statement.executeUpdate(queriesGenerator.updateUser(password, firstName, lastName, profileImage));
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

	public boolean resetPassword(String email) {

		DBCon dbConnection = new DBCon();
		Statement statement = dbConnection.getStatement();

		String to = email;
		String from = "support@t5team.com";
		String host = "localhost";

		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);

		Session session = Session.getDefaultInstance(properties);
		long timeStamp = System.currentTimeMillis();

		ResultSet keyFromDb;
		try {

			String updateSettings = UPDATE + USER_TABLE + SET
					+ USER_PASSWORD_KEY + "= '" + timeStamp + "' " + WHERE
					+ USER_EMAIL + "= '" + email + "';";
			statement.executeUpdate(updateSettings);

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		dbConnection.closeConn();

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					to));

			message.setSubject("Please reset your Password.");
			message.setContent("Hi, <bR> Please click below link to reset your password.<br><a href='http://www.switchcodes.in/sandbox/projectpackets/t5/user/reset-password/x/"
					+ timeStamp + "'>Click here to reset.</a><br><br>Thanks",
                    "text/html" );
			
			// Send message
			Transport.send(message);
			
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
		
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
		String getUser = queriesGenerator.getUserByKey(key);
		ResultSet userFromDb;
		try {

			userFromDb = statement.executeQuery(getUser);
			if (!userFromDb.next()) {
				throw new InvalidKeyException();
			}

			// set the user data
			firstName = userFromDb.getString(QueriesGenerator.getUserFirstname());
			lastName = userFromDb.getString(QueriesGenerator.getUserLastname());
			email = userFromDb.getString(QueriesGenerator.getUserEmail());
			picture = userFromDb.getString(QueriesGenerator.getUserPicture());

			// get the friends of the user
			String userId = userFromDb.getString(QueriesGenerator.getUserId());
			ResultSet friendsIds = statement.executeQuery(queriesGenerator.getFriendsForUser(userId));

			while (friendsIds.next()) {

				int id = friendsIds.getInt(QueriesGenerator.getConnectionsFriendsId());

				String getUserDetails = queriesGenerator.getUserById(id);
				ResultSet friendFromDb = statement.executeQuery(getUserDetails);

				UserData friend = new UserData(
						friendFromDb.getString(QueriesGenerator.getUserEmail()), "",
						friendFromDb.getString(QueriesGenerator.getUserFirstname()),
						friendFromDb.getString(QueriesGenerator.getUserLastname()),
						friendFromDb.getString(QueriesGenerator.getUserPicture()), "", "", "", "",
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
		String getUser = queriesGenerator.getUserById(userId);
		ResultSet userFromDb;
		try {

			userFromDb = statement.executeQuery(getUser);
			if (!userFromDb.next()) {
				throw new UserNotFoundException();
			}

			String userFirstName = userFromDb.getString(QueriesGenerator.getUserFirstname());
			String userLastName = userFromDb.getString(QueriesGenerator.getUserLastname());
			String userEMail = userFromDb.getString(QueriesGenerator.getUserEmail());
			String userPicture = userFromDb.getString(QueriesGenerator.getUserPicture());

			// get the reviews of this user
			ResultSet reviewsFromDb = statement.executeQuery(queriesGenerator.getReviewsByUserId(userId));

			while (reviewsFromDb.next()) {

				String locationId = reviewsFromDb
						.getString(QueriesGenerator.getReviewsLocationId());
				String title = reviewsFromDb.getString(QueriesGenerator.getReviewsReviewTitle());
				String review = reviewsFromDb
						.getString(QueriesGenerator.getReviewsReviewDescription());
				String locPicture = reviewsFromDb
						.getString(QueriesGenerator.getReviewsReviewPicture());
				int rating = reviewsFromDb.getInt(QueriesGenerator.getReviewsRating());

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
