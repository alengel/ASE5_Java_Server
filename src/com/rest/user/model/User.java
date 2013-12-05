package com.rest.user.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.*;

import com.rest.user.model.data.UserData;

@XmlRootElement
public class User {
	
	private List<UserData> ud = new ArrayList<UserData>();
	private String success;
	private String message;
	private String loginKey;
	

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((loginKey == null) ? 0 : loginKey.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((success == null) ? 0 : success.hashCode());
		result = prime * result + ((ud == null) ? 0 : ud.hashCode());
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
		User other = (User) obj;
		if (loginKey == null) {
			if (other.loginKey != null)
				return false;
		} else if (!loginKey.equals(other.loginKey))
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
		if (ud == null) {
			if (other.ud != null)
				return false;
		} else if (!ud.equals(other.ud))
			return false;
		return true;
	}

	public User() {
            
    }
    
    public User(String success, String message) {
            this.success = success;
            this.message = message;
            this.ud = null;
    }
   
 

	public User(String success, String loginKey, UserData ud) {
		this.success = success;
		this.loginKey = loginKey;
		this.ud.add(0, ud);
	
		

    
		
	} 
	
	public User(String success, String loginKey, List<UserData> ud) {
		this.success = success;
		this.loginKey = loginKey;
		this.ud = ud;
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
	
	@XmlElement(name = "key")
	public String getLoginKey() {
		return loginKey;
	}

	public void setLoginKey(String loginKey) {
		this.loginKey = loginKey;
	}

	
	@XmlElement (name = "data")
	public List<UserData> getUserData() {
		return ud;
	}
	
	public void setUserData(List<UserData> ud) {
		this.ud = ud;
	}
	



	
}
