package com.rest.comment.model.data;

public class CommentData {
	
	private String id;
	private String userId;
	private String ReviewUserId;
	private String comment;
	
	public CommentData() {
		
	}

	public CommentData(String id, String userId, String reviewUserId,
			String comment) {
		
		this.id = id;
		this.userId = userId;
		ReviewUserId = reviewUserId;
		this.comment = comment;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReviewUserId() {
		return ReviewUserId;
	}

	public void setReviewUserId(String reviewUserId) {
		ReviewUserId = reviewUserId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
