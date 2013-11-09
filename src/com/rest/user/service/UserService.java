package com.rest.user.service;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.*;
import com.rest.user.model.*;
import com.rest.user.model.data.UserData;
import com.rest.utils.*;

	@Path("/user")  						//defines that HTTP responses to "...hostname/user" are handled in the following class
	public class UserService /*TODO: uncomment!   implements UserServiceInterface  */ {

		
		private DBCon dbcn;
		private Statement st;		//creates a DB statement object
		
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
		 */

		@POST                                
		@Path("/login")                      
	    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		@Produces(MediaType.APPLICATION_JSON)		
	    public Response login(@FormParam ("email") String email, @FormParam("passwd") String passwd) throws SQLException
	    {      	       
	       	dbcn = new DBCon();
			st = dbcn.getStatement();	// connects to db	
			ResultSet resEmail = st.executeQuery("SELECT * FROM t5_users WHERE email = '" +email+ "'");
			if (resEmail.next()) {     // if email exists
				ResultSet resEmailPasswd = st.executeQuery("SELECT * FROM t5_users WHERE email = '" +email+ "' AND passwd = '" +passwd+ "'");
				if (resEmailPasswd.next()) {  // if passwd is correct
					String firstName = resEmailPasswd.getString("first_name");
					String lastName = resEmailPasswd.getString("last_name");
					long timeStamp = System.currentTimeMillis()/1000L;
					String loginKey = email+timeStamp;
					loginKey = SHA1.stringToSHA(loginKey);				
					st.executeUpdate("UPDATE t5_users SET login_key = '" + loginKey + "' WHERE email = '" + email +"'");
					// TODO
					// insert other fields
					///
					User user = new User("true", new UserData (null, null, firstName, lastName, loginKey, null, null, null, null, null, null)); // creates new User object with userdata which will be converted into json and returned to client
					dbcn.closeConn();
					return Response.ok(user).build();
				}
				else {
					dbcn.closeConn();
					return Response.ok(new User("false", "Password is wrong for this user")).build();
				}
				
			}
			else {
			dbcn.closeConn();
			return Response.ok(new User("false", "Email not found")).build();
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
		 */
		
		@POST                                
		@Path("/register")
		@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
		@Produces(MediaType.APPLICATION_JSON)	
	    public Response register(@FormParam ("email") String email, @FormParam("passwd") String passwd, @FormParam ("email") String firstName, @FormParam("passwd") String lastName) throws SQLException
	    {
	           
			dbcn = new DBCon();
			st = dbcn.getStatement();
	//////TODO
			//generate and insert all the other data into the query
	/////////	
			ResultSet resEmail = st.executeQuery("SELECT * FROM t5_users WHERE email = '" +email+ "'");
			if (!resEmail.next()) { // if email is unique
				int resReg = st.executeUpdate("INSERT INTO t5_users (id, email, passwd, first_name, last_name) VALUES  (NULL, '" + email+ "', '" + passwd + "', '" +firstName + "', '" +lastName +"')");
				if (resReg==1) {  // if 1 row was created successfully
					dbcn.closeConn();
					return Response.ok(new User("true", "Registration is complete")).build(); // then return success: true
				} else {
					dbcn.closeConn();
					return Response.ok(new User("false", "Registration failed")).build(); // 
				}
			} else { // if email is not unique
				dbcn.closeConn();
				return Response.ok(new User("false", "User with this email already exists")).build(); 				
			}
			
	    } 
		
		
	
			
	   

	}

