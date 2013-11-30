package com.rest.utils;

/**
 * 
 * class which contains all necessary SQL Queries for DatabaseAccess
 *
 */
final class Queries {
	
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
}
