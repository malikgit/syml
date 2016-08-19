package com.biz.rest;

import java.io.IOException;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import play.Logger;
import controllers.Application;

public class PostgressConnection {
	
	private static org.slf4j.Logger logger = Logger.underlying();
	public static Connection getPostGressConnection() throws SQLException, IOException{
		logger.info("inside for gettting postgress con{}");
		
		Connection con1=null;
		

		try {
			Class.forName("org.postgresql.Driver");
			
			
			Properties prop = new Properties();
			ArrayList<URI> nodes = new ArrayList<URI>();
			// getting connection parameter
			prop.load(Application.class.getClassLoader()
					.getResourceAsStream("underwrite.properties"));

			String url =prop.getProperty("postgresURL");
			String userName=prop.getProperty("postgresUserName");	
			String userPassword=prop.getProperty("postgresPassword");
			logger.info("url : " +url+ "usermame"+  userName + "passs: "+ userPassword);
			try{
					
		
			// work fine with just one connection.
			 con1 = DriverManager.getConnection(url, userName,
					userPassword);
		
			}catch(SQLException e){
				try{
					 url =prop.getProperty("postgresURL1");
					 userName=prop.getProperty("postgresUserName1");	
				 userPassword=prop.getProperty("postgresPassword1");
				 con1 = DriverManager.getConnection(url, userName,
							userPassword);
				}catch(SQLException e1){
					logger.error(""+e1.getMessage());
					
				}
			}
			
		}catch(ClassNotFoundException e){
			logger.error("Sqle "+e.getMessage());
		}
		
		return con1;
	}

}
