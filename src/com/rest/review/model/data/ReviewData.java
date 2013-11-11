package com.rest.review.model.data;

public class ReviewData {
	private String userEmail;
	private String venueId;
	private String venueName;
	private int rating;
	private String title;
	private String review;
	
	public ReviewData() {
		
	}

	public ReviewData(String userEmail, int rating, String title, String review) {
		this.userEmail = userEmail;
		this.rating = rating;
		this.title = title;
		this.review = review;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}

	public String getVenueName() {
		return venueName;
	}

	public void setVenueName(String venueName) {
		this.venueName = venueName;
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
