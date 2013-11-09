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
	 * method for a user to log in
	 * @param email
	 * @param passwd
	 * @return Response object with a User object and a UserData object inside. For example, 
	 *  	Response.ok(a User object(success:"true", a UserData object(data)).build() if succeeded;
	 *  	Response.ok(a User object(success:"false", error message ), null).build() if user not found
	 */	
	@POST                                
	@Path("/login")                      
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)		
    public Response login(@FormParam ("email") String email, @FormParam("passwd") String passwd);
	

	
	/**
	 * method for registration
	 * @param email
	 * @param passwd
	 * @param firstName
	 * @param lastName
	 * @return Response object with a User object inside.
	 * 		e.g. Response.ok(a User object(success:true/false, message)).build()
	 */
	@POST                                
	@Path("/register")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response register(@FormParam ("email") String email, @FormParam("passwd") String passwd, @FormParam ("email") String firstName, @FormParam("passwd") String lastName);

	
	/** checks in
	 * 
	 * @param loginKey
	 * @param venueId
	 * @return Response object with a User object with a param success: true/false
	 */
	@POST                                
	@Path("/checkin")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response checkIn(@FormParam ("loginKey") String loginKey, @FormParam("venueId") String venueId);

	/** logs out
	 * 
	 * @param loginKey
	 * @return Response object with a user object a param success true/false.
	 */
	@POST                                
	@Path("/logout")                      
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)		
    public Response logout(@FormParam ("loginKey") String loginKey);
	
	/** sets settings
	 * 
	 * @param request
	 * @param loginKey
	 * @param settings
	 * @return Response object with a User object with settings string
	 */
	@POST                                
	@Path("/settings")                      
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)		
    public Response setSettings(@FormParam ("request") String request, @FormParam ("loginKey") String loginKey, @FormParam("settings") String settings);
	
	
	/*
	@POST                                
	@Path("/change-password")                      
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)		
    public Response changePassword(@FormParam ("email") String email); 
    */
	



}

