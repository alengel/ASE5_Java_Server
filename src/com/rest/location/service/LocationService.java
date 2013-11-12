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
import com.rest.utils.exceptions.ArgumentMissingException;
import com.rest.utils.exceptions.InvalidKeyException;

@Path("/")  	
public class LocationService {
	private DBCon dbcn;
	private Statement st;
	private String success = "true";
	private String message;
	private DatabaseAccess dbAccess;
	
	@POST                                
	@Path("/check-in")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)	
    public Response checkIn(@FormParam ("loginKey") String loginKey, @FormParam("venueId") String venueId) throws SQLException, ArgumentMissingException, InvalidKeyException {
		//sql queries
		//1 select userdata with the loginkey
		//2 insert into table locations user's data, venueid, and venue name
		//3 select other reviews for this venue id from reviews table
		//4 put resultset into reviewdata list
		
		//1 get the user
		dbAccess = new DatabaseAccess();
		long timeStamp = System.currentTimeMillis()/1000L;
		Location location = dbAccess.checkIn(loginKey, venueId, ""+timeStamp);			
		
		
		return Response.ok(location).build();
	}
	


}
