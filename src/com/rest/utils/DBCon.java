package com.rest.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.*;
import javax.sql.DataSource;



public class DBCon { 
	private DataSource ds;
	private Context ctx;
	private Connection conn;
	private Statement st;
	
public DBCon()  {
	

	//Connection configuration
		
		try {
			ctx = new InitialContext();
			ds = (DataSource)ctx.lookup("java:comp/env/jdbc/javatest");
		} catch (NamingException e) {			
			e.printStackTrace();
		}
		
		try {
			conn = ds.getConnection();
			st = conn.createStatement();
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
		
 //
	}
public Statement getStatement() {
	return st;
}
public void closeConn() {
	try {
		this.conn.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
}
}


