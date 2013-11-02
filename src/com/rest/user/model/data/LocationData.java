package com.rest.user.model.data;

public class LocationData {
	
	private int id;
	private int userId;
	private String longtitude;
	private String latitude;
	private String dated;
	
	public LocationData() {
		
	}
	
	public LocationData(String latitude, String longtitude, String dated) {
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.dated = dated;
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getDated() {
		return dated;
	}

	public void setDated(String dated) {
		this.dated = dated;
	}

	
}
