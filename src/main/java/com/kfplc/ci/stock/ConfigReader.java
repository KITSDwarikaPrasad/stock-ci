package com.kfplc.ci.stock;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author prasad01
 * This is the Utility class for reading configurations
 */
public class ConfigReader {
	
	//private static String configFileName = System.getProperty("user.dir").concat("/resources/config.properties");
	private static String configFileName = System.getProperty("user.home").concat("/doNotDelete/conf/stock-ci_config.properties");
	private static InputStream inputStream = null;
	private static Properties props = new Properties();
	/*static {
		try {
			inputStream = new FileInputStream(configFileName);
			props.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	/**
	 * Util method to read the value in a key=value pair in config file
	 * @param key
	 * @return String value
	 * @throws IOException
	 */
	public static String getProperty(String key) throws IOException {
		System.out.println("configFileName"+configFileName);
		return props.getProperty(key);
	}

	public static void main(String[] args) {
		try {
			ConfigReader.getProperty("JDBC_DRIVER");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
