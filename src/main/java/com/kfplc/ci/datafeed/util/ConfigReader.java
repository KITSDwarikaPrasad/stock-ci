package com.kfplc.ci.datafeed.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This is the Utility class for reading configurations
 * <p> the configuration file path - /resources/config.properties </p>
 * <p> the secure configurations file path - user's home directory/doNotDelete/conf/stock-ci_config.properties</p>
 * @author prasad01
 * 
 */
public class ConfigReader {

	private static String configFileName = System.getProperty("user.dir").concat("/resources/config.properties");
	private static String secureConfigFileName = System.getProperty("user.home").concat("/doNotDelete/conf/stock-ci_config.properties");
	//private static InputStream inputStream = null;
	private static Properties props = new Properties();
	static {
		try (InputStream inputStream  = new FileInputStream(configFileName) ) {
			props.load(inputStream);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		try (InputStream secureInputStream  = new FileInputStream(secureConfigFileName)) {
			props.load(secureInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Util method to read the value in a key=value pair in config file
	 * @param key
	 * @return String value
	 * @throws IOException
	 */
	public static String getProperty(String key) {
		return props.getProperty(key);
	}

	/*public static void main(String[] args) {
		try {
			ConfigReader.getProperty("JDBC_DRIVER");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
