package com.kfplc.ci.stock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author prasad01
 *This is the Utility class for handle sql connections
 */
public class WMBConnection {
	
	/**
	 * Method to get the Connection to WMB Database
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
	public static Connection testConnection() throws SQLException {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try{
			conn = getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from MBODS.ARTICLE where EANCODE='5010214541300'");
			while(rs!= null && rs.next()) {
				System.out.println("rs[1]:"+rs.getString(1));
			}

		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(stmt != null) {
				stmt.close();
			}
			closeConnection(conn);
			
		}
		return null;

	}
	
	/**
	 * Method to close the connection
	 * @param Connection
	 * @throws SQLException
	 */
	static void closeConnection(Connection conn) throws SQLException {
		if(conn != null) {
			conn.close();
		}
		
	}

	public static void main(String[] args) {
		//WMBConnection wmbConnection = new WMBConnection();
		try {
			Connection conn = WMBConnection.getConnection();
			WMBConnection.testConnection();
			WMBConnection.closeConnection(conn);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void closePreparedStatement(PreparedStatement preparedStatement) throws SQLException {
		if(preparedStatement != null) {
			preparedStatement.close();
		}
		
	}

	public static void closeResultSet(ResultSet resultSetBQ) throws SQLException {
		if(resultSetBQ != null) {
			resultSetBQ.close();
		}
		
	}

	
}
