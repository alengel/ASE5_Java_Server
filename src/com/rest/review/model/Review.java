package com.rest.review.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;
import com.rest.review.model.data.ReviewData;

@XmlRootElement
public class Review {
	
	private List<ReviewData> rd = new ArrayList<ReviewData>();
	private String success;
	private String message;
	
    public Review() {
    	
    }
    
    public Review(String success, String message) {
    	this.success = success;
    	this.message = message;
    	this.rd = null;
    }
   
 
	public Review(String success, String message, List<ReviewData> rd) {
		
		this.success = success;
		this.message = message;
		this.rd = rd;
    
		
	} 
	 @XmlElement
	public String getSuccess() {
		return success;
	}
	
	public void setSuccess(String success) {
		this.success = success;
	}
	
	 @XmlElement
	public String getMessage() {
		return message;
	}
		
	public void setMessage(String message) {
		this.message = message;
	}
	
	@XmlElement (name = "data")
	public List<ReviewData> getReviewData() {
		return rd;
	}
	
	public void setReviewData(List<ReviewData> rd) {
		this.rd = rd;
	}
	
	


	
}
