package com.rest.utils;

/**
 * 
 * class which contains all necessary SQL Queries for DatabaseAccess
 *
 */
final class QueriesGenerator {
	
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
	private final static String USER_LOGIN_TIME = "login_timestamp ";
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
	
	
	/*
	 * --------------------------------------------------------------
	 * Getter-methods for getting the table attributes without space
	 * --------------------------------------------------------------
	 */
	public static String getUserTable() {
		return USER_TABLE.trim();
	}

	public static String getUserId() {
		return USER_ID.trim();
	}

	public static String getUserEmail() {
		return USER_EMAIL.trim();
	}

	public static String getUserPassword() {
		return USER_PASSWORD.trim();
	}

	public static String getUserFirstname() {
		return USER_FIRSTNAME.trim();
	}

	public static String getUserLastname() {
		return USER_LASTNAME.trim();
	}

	public static String getUserLoginkey() {
		return USER_LOGINKEY.trim();
	}

	public static String getUserGeoPushInterval() {
		return USER_GEO_PUSH_INTERVAL.trim();
	}

	public static String getUserMinDistance() {
		return USER_MIN_DISTANCE.trim();
	}

	public static String getUserPicture() {
		return USER_PICTURE.trim();
	}

	public static String getUserLogoutTime() {
		return USER_LOGOUT_TIME.trim();
	}
	
	public static String getUserLoginTime() {
		return USER_LOGIN_TIME.trim();
	}

	public static String getLocationsTable() {
		return LOCATIONS_TABLE.trim();
	}

	public static String getLocationsId() {
		return LOCATIONS_ID.trim();
	}

	public static String getLocationsFsquareVenueId() {
		return LOCATIONS_FSQUARE_VENUE_ID.trim();
	}

	public static String getReviewsTable() {
		return REVIEWS_TABLE.trim();
	}

	public static String getReviewsId() {
		return REVIEWS_ID.trim();
	}

	public static String getReviewsUserId() {
		return REVIEWS_USER_ID.trim();
	}

	public static String getReviewsLocationId() {
		return REVIEWS_LOCATION_ID.trim();
	}

	public static String getReviewsRating() {
		return REVIEWS_RATING.trim();
	}

	public static String getReviewsReviewTitle() {
		return REVIEWS_REVIEW_TITLE.trim();
	}

	public static String getReviewsReviewDescription() {
		return REVIEWS_REVIEW_DESCRIPTION.trim();
	}

	public static String getReviewsReviewPicture() {
		return REVIEWS_REVIEW_PICTURE.trim();
	}

	public static String getReviewsTotalVoteUp() {
		return REVIEWS_TOTAL_VOTE_UP.trim();
	}

	public static String getReviewsTotalVoteDown() {
		return REVIEWS_TOTAL_VOTE_DOWN.trim();
	}

	public static String getReviewsSpams() {
		return REVIEWS_SPAMS.trim();
	}

	public static String getReviewsCommentsTable() {
		return REVIEWS_COMMENTS_TABLE.trim();
	}

	public static String getReviewsCommentsId() {
		return REVIEWS_COMMENTS_ID.trim();
	}

	public static String getReviewsCommentsUserId() {
		return REVIEWS_COMMENTS_USER_ID.trim();
	}

	public static String getReviewsCommentsUserReviewsId() {
		return REVIEWS_COMMENTS_USER_REVIEWS_ID.trim();
	}

	public static String getReviewsCommentsComment() {
		return REVIEWS_COMMENTS_COMMENT.trim();
	}

	public static String getCheckinTable() {
		return CHECKIN_TABLE.trim();
	}

	public static String getCheckinUserId() {
		return CHECKIN_USER_ID.trim();
	}

	public static String getCheckinLocationId() {
		return CHECKIN_LOCATION_ID.trim();
	}

	public static String getCheckinCheckinTimestamp() {
		return CHECKIN_CHECKIN_TIMESTAMP.trim();
	}

	public static String getConnectionsTable() {
		return CONNECTIONS_TABLE.trim();
	}

	public static String getConnectionsMyId() {
		return CONNECTIONS_MY_ID.trim();
	}

	public static String getConnectionsFriendsId() {
		return CONNECTIONS_FRIENDS_ID.trim();
	}

	
	
	/*
	 * --------------------------------------------------------------
	 * Query Strings
	 * --------------------------------------------------------------
	 */
	String existsEmailInDbQuery(String email) {
		return SELECT + ALL + FROM + USER_TABLE
				+ WHERE + USER_EMAIL + EQUALS + "'" + email + "'";
	}
	
	String insertNewUser(String email, String password,
			String firstName, String lastName, String picture) {
		return INSERT_INTO + USER_TABLE + "(" + USER_EMAIL
				+ ", " + USER_PASSWORD + ", " + USER_FIRSTNAME + ", "
				+ USER_LASTNAME + ", " + USER_PICTURE + ", " + USER_LOGOUT_TIME
				+ ", " + USER_GEO_PUSH_INTERVAL + ", " + USER_MIN_DISTANCE
				+ ") " + VALUES + "('" + email + "', '" + password + "', '"
				+ firstName + "', '" + lastName + "', '" + picture
				+ "', 60, 30, 100);";
	}
	
	String getUserByEmail(String email) {
		return SELECT + "* " + FROM + USER_TABLE + WHERE
				+ USER_EMAIL + "= '" + email + "';";
	}
	
	String loginUser(String key, long timeStamp, String email) {
		return UPDATE + USER_TABLE + SET + USER_LOGINKEY + "= '"
				+ key + "'," + USER_LOGIN_TIME + "= " + timeStamp + " "
				+ WHERE + USER_EMAIL + "= '" + email + "';";
	}
	
	public String getUserByKey(String key) {
		return SELECT + "* " + FROM + USER_TABLE + WHERE
				+ USER_LOGINKEY + "= '" + key + "';";
	}
	
	
	
	
	
	
	
	public String getLocationIdFromLocationsByVenueId(String venueId) {
		return SELECT + LOCATIONS_ID + FROM + LOCATIONS_TABLE
				+ WHERE + LOCATIONS_FSQUARE_VENUE_ID + "= '" + venueId
				+ "';";
	}
	
	public String getReviewsForVenue(String venueId) {
		return SELECT + "* " + FROM
				+ REVIEWS_TABLE + WHERE + REVIEWS_LOCATION_ID + "= (" + SELECT
				+ LOCATIONS_ID + FROM + LOCATIONS_TABLE + WHERE
				+ LOCATIONS_FSQUARE_VENUE_ID + "= '" + venueId + "') LIMIT 0, 10;";
	}
	
	public String getUserById(int userId) {
		return SELECT + "* " + FROM
				+ USER_TABLE + WHERE + "id = " + userId;
	}
	
	public String getReviewsById(int reviewId) {
		return SELECT + "* " + FROM + REVIEWS_TABLE + WHERE
				+ REVIEWS_ID + "= " + reviewId + ";";
	}
	
	public String getCommentsByReviewId(int reviewId) {
		return SELECT + "*" + FROM + REVIEWS_COMMENTS_TABLE
				+ WHERE + REVIEWS_COMMENTS_USER_REVIEWS_ID + "= '"
				+ reviewId + "';";
	}
	
	public String getFriendsForUser(String userId) {
		return SELECT + CONNECTIONS_FRIENDS_ID + FROM
				+ CONNECTIONS_TABLE + WHERE + CONNECTIONS_MY_ID + "= '"
				+ userId + "';";
	}
	
	public String getReviewsByUserId(int userId) {
		return SELECT + "*" + FROM + REVIEWS_TABLE + WHERE
				+ REVIEWS_USER_ID + "= " + userId + ";";
	}

	
	
	
	
	
	
	/*
	 * --------------------------------------------------------------
	 * Insert Strings
	 * --------------------------------------------------------------
	 */
	
	public String insertNewVenue(String venueId) {
		return INSERT_IGNORE_INTO + LOCATIONS_TABLE + "("
		+ LOCATIONS_FSQUARE_VENUE_ID + ") " + VALUES + "('"
		+ venueId + "');";
	}
	
	public String insertCheckin(String timestamp, String key, String venueId) {
		return INSERT_IGNORE_INTO + CHECKIN_TABLE + "("
		+ CHECKIN_LOCATION_ID + ", " + CHECKIN_USER_ID + ", "
		+ CHECKIN_CHECKIN_TIMESTAMP + ") " + "(" + SELECT
		+ DISTINCT + "t5_locations." + LOCATIONS_ID + ", t5_users."
		+ USER_ID + ", " + "'" + timestamp + "' " + FROM
		+ LOCATIONS_TABLE + ", " + USER_TABLE + WHERE
		+ USER_LOGINKEY + "= '" + key + "' " + AND
		+ LOCATIONS_FSQUARE_VENUE_ID + "= " + "'" + venueId + "'"
		+ ");";
	}
	
	
	
	
	
	
	/*
	 * --------------------------------------------------------------
	 * Update Strings
	 * --------------------------------------------------------------
	 */
	String logoutUser(String key) {
		return UPDATE + USER_TABLE + SET + USER_LOGINKEY + "= "
				+ "NULL " + WHERE + USER_LOGINKEY + "= '" + key + "';";
	}
	
	public String updateSettings(int minDistance, int logoutSessionTime,
			int geoPushInterval, String key) {
		return UPDATE + USER_TABLE + SET
				+ USER_MIN_DISTANCE + "= '" + minDistance
				+ "', logout_session_time  = '" + logoutSessionTime + "', "
				+ USER_GEO_PUSH_INTERVAL + "= '" + geoPushInterval + "' "
				+ WHERE + USER_LOGINKEY + "= '" + key + "';";
	}
	
	public String updateReviewsVoteDown(int reviewId, int newVote) {
		return UPDATE + REVIEWS_TABLE + SET
				+ REVIEWS_TOTAL_VOTE_DOWN + "= " + newVote + " "
				+ WHERE + REVIEWS_ID + "= " + reviewId + ";";
	}
	
	public String updateReviewsVoteUp(int reviewId, int newVote) {
		return UPDATE + REVIEWS_TABLE + SET
				+ REVIEWS_TOTAL_VOTE_UP + "= " + newVote + " " + WHERE
				+ REVIEWS_ID + "= " + reviewId + ";";
	}
	
	public String updateUser(String password, String firstName,
			String lastName, String profileImage) {
		return UPDATE + USER_TABLE + SET + USER_PASSWORD
				+ "= '" + password + "', " + USER_FIRSTNAME + "= '"
				+ firstName + "', " + USER_LASTNAME + "= '" + lastName
				+ "', " + USER_PICTURE + "= '" + profileImage + "';";
	}

	
	
	
	
	
	/*
	 * --------------------------------------------------------------
	 * Delete Strings
	 * --------------------------------------------------------------
	 */
	String deleteUserTable() {
		return DELETE + FROM + USER_TABLE;
	}
	
	String deleteLocationsTable() {
		return DELETE + FROM + LOCATIONS_TABLE;
	}
	
	String deleteReviewsTable() {
		return DELETE + FROM + REVIEWS_TABLE;
	}
	
	String deleteCheckinsTable() {
		return DELETE + FROM + CHECKIN_TABLE;
	}
	
	String deleteConnectionsTable() {
		return DELETE + FROM + CONNECTIONS_TABLE;
	}

	public String insertNewReview(String userId, String locationId, int rating,
			String reviewTitle, String reviewDescription, String imageUri) {
		return INSERT_IGNORE_INTO + REVIEWS_TABLE + "( "
		+ REVIEWS_USER_ID + ", " + REVIEWS_LOCATION_ID + ", "
		+ REVIEWS_RATING + ", " + REVIEWS_REVIEW_TITLE + ", "
		+ REVIEWS_REVIEW_DESCRIPTION + ", "
		+ REVIEWS_REVIEW_PICTURE + ", " + REVIEWS_TOTAL_VOTE_DOWN
		+ "," + REVIEWS_TOTAL_VOTE_UP + ", " + REVIEWS_SPAMS + ") "
		+ VALUES + "( " + "'" + userId + "', '" + locationId
		+ "', '" + rating + "', '" + reviewTitle + "', '"
		+ reviewDescription + "', '" + imageUri + "', '0', '0', '0"
		+ "');";
	}

	public String insertNewFollow(String my_id, int reviewer_id) {
		return INSERT_IGNORE_INTO + CONNECTIONS_TABLE + "( "
				+ CONNECTIONS_MY_ID + ", " + CONNECTIONS_FRIENDS_ID + ") "
				+ VALUES + "( '" + my_id + "', '" + reviewer_id + "');";
	}

	public String insertNewComment(String userId, int reviewId, String comment) {
		return INSERT_IGNORE_INTO + REVIEWS_COMMENTS_TABLE
				+ "( " + REVIEWS_COMMENTS_USER_ID + ", "
				+ REVIEWS_COMMENTS_USER_REVIEWS_ID + ", "
				+ REVIEWS_COMMENTS_COMMENT + ") " + VALUES + "( " + userId
				+ ", " + reviewId + ", '" + comment + "');";
	}

	

	

	

	

	
	

	

	

	
	

	

	

	

	
}
