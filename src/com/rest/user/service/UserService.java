package com.rest.user.service;

import it.sauronsoftware.base64.Base64;

import javax.ws.rs.*;

import java.sql.*;


import com.rest.user.model.*;
import com.rest.user.model.data.UserData;
import com.rest.utils.*;
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.EmailAlreadyExistsException;
import com.rest.utils.exceptions.InputTooLongException;
import com.rest.utils.exceptions.InvalidKeyException;
import com.rest.utils.exceptions.PasswordWrongException;
import com.rest.utils.exceptions.UserNotFoundException;
import com.rest.utils.exceptions.WrongEmailFormatException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.servlet.http.HttpServletRequest;

import java.io.ByteArrayInputStream;



	@Path("/")  						//defines that HTTP responses to "...hostname/user" are handled in the following class
	public class UserService {
		
		public UserService() {
			
		}

		
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
	        	String loginKey = userData.getLoginKey();

	           	User user = new User("true", loginKey, userData);

	       		return Response.ok(user).build();
	       	
	        } catch (UserNotFoundException e) {
	        	return  Response.ok(new User("false", "Email and/or password not found")).build();	        	
	        } catch (PasswordWrongException e) {
	        	return  Response.ok(new User("false", "Email and/or password not found")).build();	        	
        }
			
	    } 
		
		
		
		
		/****						user/register
		 * 
		 * 
		 * Following method handles registration process
		 * 
		 * 
		 * @param email user's email
		 * @param passwd user's password
		 * @param firstName user's first name
		 * @param lastName user's last name
		 * @return Http response object
		 * @throws SQLException
		 * @throws ArgumentMissingException 
		 * @throws InputTooLongException 
		 * @throws WrongEmailFormatException 
		 * @throws IOException 
		 */		
		@POST                                
		@Path("/register")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		@Produces(MediaType.APPLICATION_JSON)	
	    public Response register(@Context HttpServletRequest servletRequest, @FormParam ("email") String email, @FormParam("passwd") String passwd, @FormParam ("first_name") String firstName, @FormParam("last_name") String lastName, @FormParam("profile_image") String  encodedImage
	    		) throws SQLException, WrongEmailFormatException, InputTooLongException, ArgumentMissingException, IOException {	
						
			UserData userData;
					
			String hrefToFile = "";
				
			if (encodedImage != null) {
			String rootFolder = servletRequest.getSession().getServletContext().getRealPath("/");
			
			long timeStamp = System.currentTimeMillis();
			String imageName = SHA1.stringToSHA(email+timeStamp);
					
			InputStream encInpStr = new ByteArrayInputStream(encodedImage.getBytes());			
			OutputStream decOutStr = new FileOutputStream(rootFolder+"/img/"+imageName+".jpg");
			
			Base64.decode(encInpStr, decOutStr);
			
			encInpStr.close();
			decOutStr.close();
	
			hrefToFile = "http://"+servletRequest.getServerName()+":"+servletRequest.getServerPort()+"/JerseyServer/img/"+imageName+".jpg";
			}	
			try {
				dbAccess = new DatabaseAccess();
				userData = dbAccess.registerNewUser(email, passwd, firstName, lastName, hrefToFile);
				if (userData != null) {
				return Response.ok(new User("true", "Registration is complete")).build();
				}
				else {
					return Response.ok(new User("false", "Error occured")).build();
				}
			} catch (EmailAlreadyExistsException e) {
				return Response.ok(new User("false", "User with this email already exists")).build(); 				
			}
		}
		

		/**
		 * Following method lets a user log out
		 * @param loginKey user's login key
		 * @return Http response object
		 */
		@POST                                
		@Path("/logout")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		@Produces(MediaType.APPLICATION_JSON)
		public Response logout(@FormParam("key") String loginKey) {
			try {
				dbAccess = new DatabaseAccess();
				if (dbAccess.logoutUser(loginKey)) {
					return Response.ok(new User("true", "Logged out successfully")).build();
				} else {
					return Response.ok(new User("false", "Error occured")).build();
				}
			} catch (Exception e) {
				return Response.ok(new User("false", "Error occured")).build();
			}
		}
		
		/**
		 * This method updates user's settings
		 * @param loginKey login key of the user
		 * @param minDistance
		 * @param logoutSessionTime auto log out time; if the user is inactive for an amount of time specified in this parameter, he will be logged out automatically 
		 * @param geoPushInterval
		 * @return Http response object
		 */
		@POST                                
		@Path("/settings")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		@Produces(MediaType.APPLICATION_JSON)
		public Response updateSettings(@FormParam("key") String loginKey, @FormParam("min_distance") int minDistance, @FormParam("logout_session_time") int logoutSessionTime, @FormParam("geo_push_interval") int geoPushInterval) {
			try {
				dbAccess = new DatabaseAccess();
				if (dbAccess.updateSettings(loginKey, minDistance, logoutSessionTime, geoPushInterval)) {
					return Response.ok(new User("true", "Settings are updated successfully")).build();
				} else {
					return Response.ok(new User("false", "Failed to update settings")).build();
				}
			} catch (InvalidKeyException e) {
				return Response.ok(new User("false", "LoginKey is wrong")).build();
			}
		}
			
	   
		/**
		 * This method allows a user to follow another user
		 * @param key user's login key
		 * @param reviewerId id of the followed user 
		 * @return Http response object
		 */
		@POST                                
		@Path("/follow")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		@Produces(MediaType.APPLICATION_JSON)	

	    public Response follow(@FormParam ("key") String key, @FormParam("reviewer_id") int reviewerId) {

			
			dbAccess = new DatabaseAccess();
			boolean success = false;
			try {
				success = dbAccess.follow(key, reviewerId);
			} catch (InvalidKeyException e) {
				return Response.ok(new User("false", "LoginKey is wrong")).build();
			} catch (UserNotFoundException e) {
				return Response.ok(new User("false", "User not found")).build();
			}
			
			if(success) {
				return Response.ok(new User("true", "")).build();
			} else {
				return Response.ok(new User("false", "")).build();
			}
		}
		
		/**
		 * Following method returns user's profile information
		 * @param key user's login key
		 * @return Http response object
		 */
		@GET
        @Path("profile/{key}")
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        @Produces(MediaType.APPLICATION_JSON)
        public Response getUserProfile(@PathParam ("key") String key) {

            dbAccess = new DatabaseAccess();
            User u;
            try {
                u = dbAccess.getUserProfile(key);
            } catch (InvalidKeyException e) {
                u = new User("false", "LoginKey is wrong");
            }
            if(u == null) {
                u = new User("false", "");
            }

            return Response.ok(u).build();
        }
                               
		

		@GET
        @Path("/forgotpassword/{email}")
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        @Produces(MediaType.APPLICATION_JSON)
        public Response forgotPassword(@PathParam ("email") String email) {
			
			String success = "true";
			String message = "";
			
            dbAccess = new DatabaseAccess();
            boolean u;
            u = dbAccess.resetPassword(email);
            if(u == false) {
                success = "false";
            }

			return Response.ok(new User(success, message)).build();
        }
                               
		@GET
        @Path("/find-user/key/{key}/venue_id/{venueid}/timestamp/{timestamp}")
        @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
        @Produces(MediaType.APPLICATION_JSON)
        public Response findUser(@PathParam ("key") String loginKey, @PathParam ("venueid") String venueId, @PathParam ("timestamp") long timeStamp) {
			dbAccess= new DatabaseAccess();
			try {
				User user = dbAccess.findUser(loginKey, venueId, timeStamp);
				return Response.ok(user).build();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
				return Response.ok(new User("false", "LoginKey is wrong")).build();
			}
		}


		
		//this method is for checking some stuff at the server, not used in real app 
		@GET                                
		@Path("/info")
	//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	//	@Produces(MediaType.APPLICATION_JSON)
		public Response getInfo(@Context HttpServletRequest servletRequest) {
			
			String rootFolder = servletRequest.getSession().getServletContext().getRealPath("/");
			System.out.println(rootFolder);			

			return Response.ok("debug info <br />"+ "root folder: "+ rootFolder).build();
		}

	}

