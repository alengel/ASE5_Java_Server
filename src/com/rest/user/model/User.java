package com.rest.user.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

import com.rest.user.model.data.LocationData;
import com.rest.user.model.data.UserData;

@XmlRootElement
public class User {
	
	private List<UserData> ud = new ArrayList<UserData>();
	private List<LocationData> ld = new ArrayList<LocationData>();
	private String success;
	private String message;
	
    public User() {
    	
    }
    
    public User(String success, String message) {
    	this.success = success;
    	this.message = message;
    	this.ud = null;
    	this.ld = null;
    }
   
 
	public User(String success, UserData ud, LocationData ld) {
		this.success = success;
		this.ud.add(0, ud);
		this.ld.add(0, ld);
    
		
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
	
	@XmlElement
	public List<LocationData> getLocationData() {
		return ld;
	}
	
	public void setLocationData(List<LocationData> ld) {
		this.ld = ld;
	}
	


	
}
