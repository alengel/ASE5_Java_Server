package com.rest.utils;

import java.sql.SQLException;
import java.util.ArrayList;

import com.rest.comment.model.data.CommentData;
import com.rest.location.model.Location;
import com.rest.user.model.User;
import com.rest.review.model.Review;
import com.rest.review.model.data.ReviewData;
import com.rest.user.model.data.UserData;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.EmailAlreadyExistsException;
import com.rest.utils.exceptions.InputTooLongException;
import com.rest.utils.exceptions.InvalidKeyException;
import com.rest.utils.exceptions.PasswordWrongException;
import com.rest.utils.exceptions.ReviewNotFoundException;
import com.rest.utils.exceptions.UserNotFoundException;
import com.rest.utils.exceptions.WrongEmailFormatException;

public interface DatabaseAccessInterface {

	/**
	 * delete everything in the database
	 */
	public void clearDatabase();

	/**
	 * log the user in
	 * @param email
	 * @param password
	 * @return the UserData of the logged in user, including his key. Null otherwise
	 * @throws ArgumentMissingException
	 * @throws UserNotFoundException
	 * @throws PasswordWrongException
	 */
	public UserData loginUser(String email, String password)
			throws ArgumentMissingException, UserNotFoundException,
			PasswordWrongException, SQLException;

	/**
	 * register a new User in the database if he does not exist yet
	 * 
	 * @param email
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param picture
	 * @return the new UserData if successful, null otherwise
	 * @throws WrongEmailFormatException
	 * @throws InputTooLongException
	 * @throws ArgumentMissingException
	 * @throws EmailAlreadyExistsException
	 */
	public UserData registerNewUser(String email, String password,
			String firstName, String lastName, String picture)
			throws WrongEmailFormatException, InputTooLongException,
			ArgumentMissingException, EmailAlreadyExistsException;

	/**
	 * will logout the user
	 * @param key the loginKey of the user
	 * @return true if logout successful, false otherwise
	 * @throws ArgumentMissingException
	 */
	public boolean logoutUser(String key) throws ArgumentMissingException;

	/**
	 * 
	 * @param user
	 * @param newPassword
	 * @return true if change was successful, false otherwise
	 * @throws UserNotFoundException
	 */
	public boolean changePassword(String userMail, String newPassword)
			throws UserNotFoundException;

	/**
	 * check a User in to a specific Location
	 * @param userKey
	 * @param venueId the location where the user wants to check in 
	 * @param timestamp the checkin time
	 * @return the Location where the User has checked in. Null if not possible
	 * @throws ArgumentMissingException
	 * @throws InvalidKeyException
	 */
	public Location checkIn(String userKey, String venueId, String timestamp)
			throws ArgumentMissingException, InvalidKeyException;

	/**
	 * 
	 * @param userKey
	 * @param minDistance
	 * @param maxLoginInterval
	 * @param geoPushInterval
	 * @return true if change successful, false otherwise
	 * @throws InvalidKeyException
	 */
	public boolean updateSettings(String key, int minDistance,
			int maxLoginInterval, int geoPushInterval)
			throws InvalidKeyException;

	/**
	 * store a new Review
	 * @param key
	 * @param venueId
	 * @param rating
	 * @param reviewTitle
	 * @param reviewDescription
	 * @param imageUri
	 * @return true if change successful, false otherwise
	 * @throws InvalidKeyException
	 */
	public boolean storeNewReview(String key, String venueId, int rating,
			String reviewTitle, String reviewDescription, String imageUri)
			throws InvalidKeyException;

	/**
	 * get reviews for one specific venue
	 * @param venueId the venueId for which reviews should be retrieved
	 * @return A review object containg the reviews for a venue
	 */
	public Review getReviews(String venueId);
	
	/**
	 * method fur up- or downvoting a review. 
	 * @param key the loginKey of the User
	 * @param reviewId the Review to be voted up or dowm
	 * @param vote 1 for upvoting, 0 for downvoting
	 * @return true if voting was successful, false otherwise
	 * @throws InvalidKeyException
	 * @throws ReviewNotFoundException
	 */
	public boolean vote(String key, int reviewId, int vote)
			throws InvalidKeyException, ReviewNotFoundException;
	
	/**
	 * if a user wants to follow another user
	 * @param key the key of the user who would like to follow another one
	 * @param reviewer_id id of the reviewer which the user likes to follow
	 * @return true if successful, false otherwise
	 * @throws InvalidKeyException
	 * @throws UserNotFoundException
	 */
	public boolean follow(String key, int reviewer_id)
			throws InvalidKeyException, UserNotFoundException;
	
	/**
	 * for storing comments on reviews
	 * @param key user that writes a comment
	 * @param reviewId review that gets commented
	 * @param comment the actual comment 
	 * @return true if succesful, false otherwise
	 * @throws ReviewNotFoundException
	 * @throws InvalidKeyException
	 */
	public boolean putComment(String key, int reviewId, String comment)
			throws ReviewNotFoundException, InvalidKeyException;
	
	/**
	 * 
	 * @param reviewId
	 * @return the Comments for a Review
	 * @throws ReviewNotFoundException
	 */
	public ArrayList<CommentData> getCommentsForReview(int reviewId)
			throws ReviewNotFoundException;
	
	/**
	 * 
	 * @param key
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param profileImage
	 * @return true if successful, false otherwise
	 * @throws InvalidKeyException
	 */
	public boolean updateUser(String key, String password,
			String firstName, String lastName, String profileImage)
			throws InvalidKeyException;

	public User getUserProfile(String key) throws InvalidKeyException;

	public ArrayList<ReviewData> getReviewsForUser(int userId)
			throws UserNotFoundException;
}
