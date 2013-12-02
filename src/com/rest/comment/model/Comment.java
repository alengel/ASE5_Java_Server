package com.rest.comment.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.rest.comment.model.data.*;

@XmlRootElement
public class Comment {

	private List<CommentData> cd = new ArrayList<CommentData>();
	private String success;
	private String message;
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cd == null) ? 0 : cd.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
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
		Comment other = (Comment) obj;
		if (cd == null) {
			if (other.cd != null)
				return false;
		} else if (!cd.equals(other.cd))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (success == null) {
			if (other.success != null)
				return false;
		} else if (!success.equals(other.success))
			return false;
		return true;
	}

	public Comment() {
    	
    }
    
    public Comment(String success, String message) {
    	this.success = success;
    	this.message = message;
    	this.cd = null;
    }
   
 
	public Comment(String success, String message, ArrayList<CommentData> cd) {
		
		this.success = success;
		this.message = message;
		this.cd = cd;
	}
	
	@XmlElement
	public List<CommentData> getCd() {
		return cd;
	}

	public void setCd(List<CommentData> cd) {
		this.cd = cd;
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
}
