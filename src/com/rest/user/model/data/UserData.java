package com.rest.user.model.data;

import java.sql.ResultSet;

import javax.xml.bind.annotation.XmlElement;

import com.rest.utils.DBCon;

public class UserData {
	
	private String id;
	private String email;
	private String passwd;
	private String firstName;
	private String lastName;
	private String loginTimestamp;
	private String lastLogin;
	private String loginKey;
	private String lastRequest;
	private String logoutSessiontime;
	private String gpsPushtime;
	private String minDistance;
	private String picture;
		
	public UserData() {
		
		
	}


	public UserData(String email, String passwd, String firstName, String lastName, String picture, String loginKey, String loginTimestamp, String lastLogin, String lastRequest, String logoutSessiontime, String gpsPushtime, String minDistance) {
		this.email = email;
		this.passwd = passwd;
		this.firstName = firstName;
		this.lastName = lastName;
		this.loginTimestamp = loginTimestamp;
		this.lastLogin = lastLogin;
		this.lastRequest = lastRequest;
		this.logoutSessiontime = logoutSessiontime;
		this.gpsPushtime = gpsPushtime;
		this.minDistance = minDistance;
		this.picture = picture;
		this.loginKey = loginKey;
		
		
		try {
			DBCon dbcn = new DBCon();		
			ResultSet idRes = dbcn.getStatement().executeQuery("SELECT * FROM t5_users WHERE email = '" + email+"';");
			if(idRes.next()) {
				this.id = idRes.getInt("id")+"";
			}
			dbcn.closeConn();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	@XmlElement(name = "first_name")	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@XmlElement(name = "last_name")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@XmlElement(name = "profile_image")
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	@XmlElement(name = "key")
	public String getLoginKey() {
		return loginKey;
	}

	public void setLoginKey(String loginKey) {
		this.loginKey = loginKey;
	}
	
	public String getLoginTimestamp() {
		return loginTimestamp;
	}

	public void setLoginTimestamp(String loginTimestamp) {
		this.loginTimestamp = loginTimestamp;
	}

	@XmlElement(name = "last_login")
	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	@XmlElement(name = "last_request")
	public String getLastRequest() {
		return lastRequest;
	}

	public void setLastRequest(String lastRequest) {
		this.lastRequest = lastRequest;
	}

	@XmlElement(name = "max_login_interval")
	public String getLogoutSessiontime() {
		return logoutSessiontime;
	}

	public void setLogoutSessiontime(String logoutSessiontime) {
		this.logoutSessiontime = logoutSessiontime;
	}

	@XmlElement(name = "gps_push_interval")
	public String getGpsPushtime() {
		return gpsPushtime;
	}

	public void setGpsPushtime(String gpsPushtime) {
		this.gpsPushtime = gpsPushtime;
	}

	@XmlElement(name = "min_distance")
	public String getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(String minDistance) {
		this.minDistance = minDistance;
	}
	
	
	
}
	
	