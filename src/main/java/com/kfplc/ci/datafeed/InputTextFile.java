package com.kfplc.ci.datafeed;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;

import com.kfplc.ci.datafeed.util.ConfigReader;
import com.kfplc.ci.datafeed.util.WMBConnection;

/**
 * This class has the methods to create new Input data and write to the Input file for BODS job 
 * @author prasad01
 */
public class InputTextFile {

	private static Connection connection;
	private static PreparedStatement preparedStatement;
	private static PreparedStatement preparedStatementBQ;
	private static ResultSet resultSet;
	private static ResultSet resultSetBQ;


	/**
	 * Method to fetch the Store code and  BQCode from Database, Then set these values in the InputTextRow
	 * @param inputTextRow 
	 * @throws SQLException
	 */
	public static InputTextRow fillBQStoreCd(InputTextRow inputTextRow) throws SQLException {
		
		//read store_code and BQCode from database
		try {
			
			String sqlQueryStoreCd = "select FULLSTORECODE from MBREPOS.MBSTRCD where ROWNUM =1";
			
			connection = WMBConnection.getConnection();
			preparedStatement = connection.prepareStatement(sqlQueryStoreCd);
			resultSet = preparedStatement.executeQuery();
			if(resultSet != null && resultSet.next()) {
				inputTextRow.setFull_store_code(resultSet.getString(1));
			}
			
			
			String sqlQueryBQCd = "select BQCODE from MBODS."+ ConfigReader.getProperty("TBL_EFFECTIVE_ARTICLE")+" where ROWNUM =1";
			preparedStatementBQ = connection.prepareStatement(sqlQueryBQCd);
			resultSetBQ = preparedStatementBQ.executeQuery();
			if(resultSetBQ != null && resultSetBQ.next()) {
				inputTextRow.setBqcode(resultSetBQ.getString(1));
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch blockh
			e.printStackTrace();
		} finally {
			WMBConnection.closeResultSet(resultSet);
			WMBConnection.closePreparedStatement(preparedStatement);
			WMBConnection.closeResultSet(resultSetBQ);
			WMBConnection.closePreparedStatement(preparedStatementBQ);
			WMBConnection.closeConnection(connection);
		}
		
		// File writter
		
		
		return inputTextRow;
	}
	
	

	/**The method to write the input data to Input Text file
	 * @param inputTextRow - The object holding the different sections of the input row for BODS job
	 * @throws SQLException
	 */
	public static void createInputTextFile(InputTextRow inputTextRow) throws SQLException {

		fillBQStoreCd(inputTextRow);

		Path path = Paths.get(ConfigReader.getProperty("INPUT_FILE_PATH"));

		try(BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(inputTextRow.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
}
