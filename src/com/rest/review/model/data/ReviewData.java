package com.rest.review.model.data;

public class ReviewData {
	private int userId;
	private int rating;
	private String title;
	private String review;
	private String picture;
	private String userFirstName;
	private String userLastName;
	private String userEmail;
	
	public ReviewData() {
		
	}

	public ReviewData(int userId, String userFirstName, String userLastName, String userEmail, int rating, String title, String review, String picture) {
		this.userId = userId;
		this.rating = rating;
		this.title = title;
		this.review = review;
		this.picture = picture;
		this.userFirstName = userFirstName;
		this.userLastName = userLastName;
		this. userEmail = userEmail;
	}
	
	
	

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

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

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	
	
	
	
	
	
}
