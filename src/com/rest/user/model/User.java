package com.rest.user.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

import com.rest.user.model.data.LocationData;
import com.rest.user.model.data.UserData;

@XmlRootElement
public class User {
	
	private List<UserData> ud = new ArrayList<UserData>();
	private String success;
	private String message;
	
    public User() {
    	
    }
    
    public User(String success, String message) {
    	this.success = success;
    	this.message = message;
    	this.ud = null;
    }
   
 
	public User(String success, UserData ud) {
		this.success = success;
		this.ud.add(0, ud);

    
		
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
	
	@XmlElement
	public List<UserData> getUserData() {
		return ud;
	}
	
	public void setUserData(List<UserData> ud) {
		this.ud = ud;
	}
	



	
}
