package com.rest.review.model.data;

import javax.xml.bind.annotation.XmlElement;

public class ReviewData {
	private int userId;
	private int rating;
	private String title;
	private String review;
	private String locPicture;
	private String userFirstName;
	private String userLastName;
	private String userEmail;
	private String userPicture;
	
	public ReviewData() {
		
	}

	public ReviewData(int userId, String userFirstName, String userLastName, String userEmail, String userPicture, int rating, String title, String review, String locPicture) {
		this.userId = userId;
		this.rating = rating;
		this.title = title;
		this.review = review;
		this.locPicture = locPicture;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this.userEmail = userEmail;
		this.userPicture = userPicture;
	}
	
	
	


	@XmlElement(name = "profile_image")
	public String getUserPicture() {
		return userPicture;
	}

	public void setUserPicture(String userPicture) {
		this.userPicture = userPicture;
	}
	@XmlElement(name = "users_id")
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@XmlElement(name = "location_image")
	public String getPicture() {
		return locPicture;
	}

	public void setPicture(String picture) {
		this.locPicture = picture;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@XmlElement(name = "review_title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	@XmlElement(name = "first_name")
	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	@XmlElement(name = "last_name")
	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	@XmlElement(name = "email")
	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	
	
	
	
	
	
}
