//creation and initialization of util package
package com.hexaware.util;
/*Created by Sai Siddarth T S*/
//importing the in buily sql connection
import java.sql.Connection;
import java.sql.DriverManager;
//creation of class Database connection
public class DBConnection {
	//setting the connection vaule equal to null
	public static Connection connection = null;
	//constructor without fields
	public DBConnection() {
		super();
	}
	public static Connection getConnection()
	{
		//singleton design pattern
		if (connection == null)
		{
			try
			{
				connection = DriverManager.getConnection(
													PropertyUtil.get("db.url"),
													PropertyUtil.get("db.username"),
													PropertyUtil.get("db.password")
						);	
			}
		catch(Exception  e)
		{
			e.printStackTrace();
		}
		}
		return connection;
	}	
}
