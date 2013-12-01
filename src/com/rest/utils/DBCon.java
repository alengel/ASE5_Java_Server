package com.rest.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;



public class DBCon { 
	
	private Connection conn;
	private Statement st;
	private InitialContext ctx;
	private DataSource ds;
		

	private String url = "jdbc:mysql://localhost:3306/";
	private String dbName = "javatest";
	private String driver = "com.mysql.jdbc.Driver";
	private String userName = "root";
	private String password = "";
	
public DBCon()  {
	
	

//Tomcat connection configuration

	/*
=======
		/*
>>>>>>> 2585cbc99eecc084f8f7c586bbeb7f3a1a8e052f
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
		*/
 //--Tomcat
		
		
		
///// connection for jUnit
		
		try {
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url+dbName, userName, password);
		
			st = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
///--jUnit connection				
	
		
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


