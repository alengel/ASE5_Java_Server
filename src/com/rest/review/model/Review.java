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
   
 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((rd == null) ? 0 : rd.hashCode());
		result = prime * result + ((success == null) ? 0 : success.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Review other = (Review) obj;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (rd == null) {
			if (other.rd != null)
				return false;
		} else if (!rd.equals(other.rd))
			return false;
		if (success == null) {
			if (other.success != null)
				return false;
		} else if (!success.equals(other.success))
			return false;
		return true;
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
