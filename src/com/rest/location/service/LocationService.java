package com.rest.location.service;


import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.rest.location.model.Location;
import com.rest.location.model.data.LocationData;
import com.rest.review.model.data.ReviewData;
import com.rest.utils.*;

@Path("/")  	
public class LocationService {
	private DBCon dbcn;
	private Statement st;
	private String success = "true";
	private String message;
	List<ReviewData> rd;
	LocationData ld;
	
	@POST                                
	@Path("/check-in")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response checkIn(@FormParam ("loginKey") String loginKey, @FormParam("venueId") String venueId, @FormParam("venueName") String venueName) throws SQLException {
		//sql queries
		//1 select userdata with the loginkey
		//2 insert into table locations user's data, venueid, and venue name
		//3 select other reviews for this venue id from reviews table
		//4 put resultset into reviewdata list
		
		//1 get the user
		dbcn = new DBCon();
		st = dbcn.getStatement();	// connects to db 
		ResultSet resUser = st.executeQuery("SELECT * FROM t5_users WHERE login_key = '" +loginKey+ "'");
		if (resUser.next()) {
			int userId = resUser.getInt("id");
			String email = resUser.getString("email");
			//2 check in
			int resCheckin = st.executeUpdate("INSERT INTO t5_users_reviews (users_id, users_email, venue_Id) VALUES ('" + userId + "', '" + email + "', '" + venueId +"')");
			if (resCheckin == 1) { // checked in
				message = "Checked in successfully";
				//3 retrieving other reviews
				ResultSet resReviewsCheck = st.executeQuery("SELECT * FROM t5_users_reviews WHERE venue_id = '" +venueId+ "' AND review_description <> ''");
				if (resReviewsCheck.next()) { // if at least 1 review exists
					rd = new ArrayList<ReviewData>();
					ResultSet resReviews = st.executeQuery("SELECT * FROM t5_users_reviews WHERE venue_id = '" +venueId+ "' AND review_description <> '' LIMIT 0, 10");
					//4 creating review data list
					while (resReviews.next()) {
						String userEmail = resReviews.getString("users_email");
						int  rating = resReviews.getInt("rating");
						String title = resReviews.getString("review_title");
						String review = resReviews.getString("review_description");
						rd.add(new ReviewData(userEmail, rating, title, review)); // adds reviews into reviews list 
					
					}					
					
					ld = new LocationData(venueId, venueName, rd);
					
				} else {
					// reviews not found
					rd=null;
					message = message + ". Nobody left a review yet";
					ld = new LocationData(venueId, venueName, rd);
				}
				
				
				
			} else {
				//failed to check in
				success = "false";
				message = "Failed to check in";
				ld = null;
			}
			

			
		} else {
			//loginkey is wrong
			success = "false";
			message = "Error occured. Please, log in again";
			ld = null;
		}
			
		
		
		return Response.ok(new Location(success, message, ld)).build();
	}
	


}
