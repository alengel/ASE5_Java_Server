package com.rest.user.model.data;

public class UserData {
	
	private String id;
	private String email;
	private String passwd;
	private String firstName;
	private String lastName;
	private String loginKey;
	private String loginTimestamp;
	private String lastLogin;
	private String lastRequest;
	private String logoutSessiontime;
	private String gpsPushtime;
	private String dated;
		
	public UserData() {
		
		
	}
	
	public UserData(String email, String passwd, String firstName, String lastName, String loginKey, String loginTimestamp, String lastLogin, String lastRequest, String logoutSessiontime, String gpsPushtime, String dated) {
		this.email = email;
		this.passwd = passwd;
		this.firstName = firstName;
		this.lastName = lastName;
		this.loginKey = loginKey;
		this.loginTimestamp = loginTimestamp;
		this.lastLogin = lastLogin;
		this.lastRequest = lastRequest;
		this.logoutSessiontime = logoutSessiontime;
		this.gpsPushtime = gpsPushtime;
		this.dated = dated;
		
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

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

	public String getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getLastRequest() {
		return lastRequest;
	}

	public void setLastRequest(String lastRequest) {
		this.lastRequest = lastRequest;
	}

	public String getLogoutSessiontime() {
		return logoutSessiontime;
	}

	public void setLogoutSessiontime(String logoutSessiontime) {
		this.logoutSessiontime = logoutSessiontime;
	}

	public String getGpsPushtime() {
		return gpsPushtime;
	}

	public void setGpsPushtime(String gpsPushtime) {
		this.gpsPushtime = gpsPushtime;
	}

	public String getDated() {
		return dated;
	}

	public void setDated(String dated) {
		this.dated = dated;
	}
	
	
	
}
	
	