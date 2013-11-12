package com.rest.review.model.data;

public class ReviewData {
	private int userId;
	private int rating;
	private String title;
	private String review;
	private String picture;
	
	public ReviewData() {
		
	}

	public ReviewData(int userId, int rating, String title, String review, String picture) {
		this.userId = userId;
		this.rating = rating;
		this.title = title;
		this.review = review;
		this.picture = picture;
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
	
	
	
	
	
}
