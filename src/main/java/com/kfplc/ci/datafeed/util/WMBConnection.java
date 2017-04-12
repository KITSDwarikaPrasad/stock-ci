package com.kfplc.ci.datafeed.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This is the Utility class for handle sql connections
 * @author prasad01
 *
 */
public class WMBConnection {
	
	/**
	 * Util Method to get the Connection to WMB Database
	 * @return Connection
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		Connection conn = null;
		Class.forName(ConfigReader.getProperty("JDBC_DRIVER"));
		conn = DriverManager.getConnection(ConfigReader.getProperty("DB_URL"),ConfigReader.getProperty("USERNAME"),ProtectedConfig.decrypt(ConfigReader.getProperty("PASSWORD")));
		System.out.println("Connection created..");
		return conn;
	}
	
	/**
	 * Method to test the connection with simple query
	 * @return Connection
	 * @throws SQLException
	 */
//	public static Connection testConnection() throws SQLException {
//
//		Connection conn = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		try{
//			conn = getConnection();
//			stmt = conn.createStatement();
//			rs = stmt.executeQuery("select * from MBODS.ARTICLE where EANCODE='5010214541300'");
//			while(rs!= null && rs.next()) {
//				System.out.println("rs[1]:"+rs.getString(1));
//			}
//
//		} catch(Exception e) {
//			e.printStackTrace();
//		} finally {
//			if(stmt != null) {
//				stmt.close();
//			}
//			closeConnection(conn);
//			
//		}
//		return null;
//
//	}
	
	/**
	 * Util Method to close the connection
	 * @param Connection
	 * @throws SQLException
	 */
	public static void closeConnection(Connection conn) throws SQLException {
		if(conn != null) {
			conn.close();
		}
		
	}


	/**
	 * Util Method to close Prepared statement
	 * @param preparedStatement
	 * @throws SQLException
	 */
	public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
		if(preparedStatement != null) {
			preparedStatement.close();
		}
		
	}

	/**
	 * Util Method to close the Resultset
	 * @param resultSet
	 * @throws SQLException
	 */
	public static void closeResultSet(ResultSet resultSet) throws SQLException {
		if(resultSet != null) {
			resultSet.close();
		}
		
	}

	
}
