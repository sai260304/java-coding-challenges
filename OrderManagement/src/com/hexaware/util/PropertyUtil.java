//initialization of package util
package com.hexaware.util;
/*Created by Sai Siddarth T S*/
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
//creation of class property util
public class PropertyUtil {
	//Two static variables created
	private static final String File_Path ="resources/db.properties";
	private static Properties properties;
	static {
		// try - catch -- exception handling   
		//try is where your code is which may throw an exception and catch is to catch the exception
		try {
			// For reading the file
			FileInputStream fis = new FileInputStream(File_Path);
			properties = new Properties();
            // to load the file which is property file so we are loading with the help of object of Properties class
			properties.load(fis);
		}
		catch(IOException e)
		{
			e.printStackTrace();  // jvm
		}	
	}
		public static String get(String key) {
		return properties.getProperty(key);
	}
}


