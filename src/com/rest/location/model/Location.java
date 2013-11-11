package com.rest.location.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;
import com.rest.location.model.data.LocationData;

@XmlRootElement
public class Location {
	
	private List<LocationData> ld = new ArrayList<LocationData>();
	private String success;
	private String message;
	
    public Location() {
    	
    }
    
    public Location(String success, String message) {
    	this.success = success;
    	this.message = message;
    	this.ld = null;
    }
   
 
	public Location(String success, String message, LocationData ud) {
		
		this.success = success;
		this.message = message;
		this.ld.add(0, ud);
    
		
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
	public List<LocationData> getLocationData() {
		return ld;
	}
	
	public void setLocationData(List<LocationData> ld) {
		this.ld = ld;
	}
	
	


	
}
