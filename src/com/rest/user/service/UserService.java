package com.rest.user.service;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.sql.*;

import com.rest.user.model.*;
import com.rest.user.model.data.UserData;
import com.rest.utils.*;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.EmailAlreadyExistsException;
import com.rest.utils.exceptions.InputTooLongException;
import com.rest.utils.exceptions.PasswordWrongException;
import com.rest.utils.exceptions.UserNotFoundException;
import com.rest.utils.exceptions.WrongEmailFormatException;

	@Path("/")  						//defines that HTTP responses to "...hostname/user" are handled in the following class
	public class UserService /*TODO: uncomment!   implements UserServiceInterface  */ {

		
		private DBCon dbcn;
		private Statement st;		//creates a DB statement object
		private DatabaseAccess dbAccess;
		
		/******											user/login
		 * 
		 * 
		 * 
		 * This method handles login requests.
		 * Annotations preceding the method are used by the server to understand when the method should be called. 
		 * In this case: 
		 * "@POST" defines the method used for the HTTP request(GET, POST, PUT etc). 
		 * "@Path" defines the target URL of the request. It is added to the end of previous Path annotation at class level "@Path("/user"), and the 
		 * 		result is "hostname/user/login".
		 * "@Consumes" defines how the data is stored within the request(XML, JSON, plain text, web form, etc..) In this case it's a web form
		 * "@Produces" defines the format of the data to be returned within a response to the request.
		 * To sum up, these annotations tell the server to call the following method when the server receives a POST request 
		 * 		containing some data in a WEB FORM to the "hostname/user/login" address. The method produces a response in JSON format and returns it.     
		 * @param email
		 * @param passwd
		 * @return a Response object containing a User object inside. The user object is automatically converted into a desirable
		 *  	output format (JSON in this case) and put into an HTTP response body.
		 * @throws SQLException
		 * @throws PasswordWrongException 
		 * @throws UserNotFoundException 
		 * @throws ArgumentMissingException 
		 */

		@POST                                
		@Path("/login")                      
	    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		@Produces(MediaType.APPLICATION_JSON)		
	    public Response login(@FormParam ("email") String email, @FormParam("passwd") String passwd) throws SQLException, ArgumentMissingException, UserNotFoundException, PasswordWrongException
	    {      	       
	        UserData userData;
	        dbAccess = new DatabaseAccess();
	        
	        try {
	        	userData = dbAccess.loginUser(email, passwd);
	           	User user = new User("true", userData);
	       		return Response.ok(user).build();
	       	
	        } catch (UserNotFoundException e) {
	        	return  Response.ok(new User("false", "Email not found")).build();	        	
	        } catch (PasswordWrongException e) {
	        	return  Response.ok(new User("false", "Password is wrong for this user")).build();	        	
        }
			
	    } 
		
		
		/****						user/register
		 * 
		 * 
		 * Following method handles registration process
		 * 
		 * 
		 * @param email
		 * @param passwd
		 * @param firstName
		 * @param lastName
		 * @return
		 * @throws SQLException
		 * @throws ArgumentMissingException 
		 * @throws InputTooLongException 
		 * @throws WrongEmailFormatException 
		 */
		
		@POST                                
		@Path("/register")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		@Produces(MediaType.APPLICATION_JSON)	
	    public Response register(@FormParam ("email") String email, @FormParam("passwd") String passwd, @FormParam ("firstName") String firstName, @FormParam("lastName") String lastName) throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException
	    {
	           
			UserData userData;
			try {
				dbAccess = new DatabaseAccess();
				userData = dbAccess.registerNewUser(email, passwd, firstName, lastName);
				return Response.ok(new User("true", "Registration is complete")).build();
			} catch (EmailAlreadyExistsException e) {
				return Response.ok(new User("false", "User with this email already exists")).build(); 				
			}
			
			
	    } 
		
		@POST                                
		@Path("/logout")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		@Produces(MediaType.APPLICATION_JSON)
		public Response logout(@FormParam("loginKey") String loginKey) {
			try {
				dbAccess = new DatabaseAccess();
				if (dbAccess.logoutUser(loginKey)) {
					return Response.ok(new User("true", "Logged out successfully")).build();
				} else {
					return Response.ok(new User("false", "Error occured. Wrong login key")).build();
				}
			} catch (Exception e) {
				return Response.ok(new User("false", "Error occured")).build();
			}
		}
		
			
	   

	}

