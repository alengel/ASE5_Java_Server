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
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ld == null) ? 0 : ld.hashCode());
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
		Location other = (Location) obj;
		if (ld == null) {
			if (other.ld != null)
				return false;
		} else if (!ld.equals(other.ld))
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
