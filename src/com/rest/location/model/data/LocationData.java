package com.rest.location.model.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.rest.review.model.data.*;


public class LocationData {
	
	private String venueId;
	private String dated;
	private List<ReviewData> rd = new ArrayList<ReviewData>(); 
	
	public LocationData() {
		
	}
	
	public LocationData(String venueId, List<ReviewData> rd) {
		this.venueId = venueId;
		this.rd = rd;
		
	}


	@XmlElement(name = "venue_id")
	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}


	public String getDated() {
		return dated;
	}

	public void setDated(String dated) {
		this.dated = dated;
	}
	
	public List<ReviewData> getReviewData() {
		return rd;
	}
	
	public void setReviewData(List<ReviewData> rd) {
		this.rd = rd;
	}

	
}
