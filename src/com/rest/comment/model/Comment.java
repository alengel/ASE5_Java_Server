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
