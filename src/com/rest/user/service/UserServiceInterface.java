package com.rest.user.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * 
 * Specification of the API. Contains every method that can be called by a client
 *
 */
public interface UserServiceInterface {
	
	/**
	 * method for a user to login
	 * @param email
	 * @param passwd
	 * @return 
	 */
	@POST                                
	@Path("/login")                      
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)		
    public Response login(@FormParam ("email") String email, @FormParam("passwd") String passwd);
	
	
	@POST                                
	@Path("/register")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response register(@FormParam ("email") String email, @FormParam("passwd") String passwd, @FormParam ("email") String firstName, @FormParam("passwd") String lastName);
	
	@POST                                
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(@FormParam("loginKey") String loginKey);
	
	@POST                                
	@Path("/settings")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSettings(@FormParam("loginKey") String loginKey, @FormParam("minDistance") int minDistance, @FormParam("logoutSessionTime") int logoutSessionTime, @FormParam("geoPushInterval") int geoPushInterval);
	
	
	@POST                                
	@Path("/account")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAccount(@FormParam("loginKey") String loginKey, @FormParam("password") String password, @FormParam("firstName") String firstName, @FormParam("lastName") String lastName, @FormParam("profileImage") String profileImage);
}
