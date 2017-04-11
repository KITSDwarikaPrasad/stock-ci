package com.kfplc.ci.stock;

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

import com.kfplc.ci.stock.util.ConfigReader;
import com.kfplc.ci.stock.util.WMBConnection;

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
				inputTextRow.setStore_code(resultSet.getString(1));
			}
			
			
			String sqlQueryBQCd = "select BQCODE from MBODS.EFFECTIVE_ARTICLE_DELTA_RES where ROWNUM =1";
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
			WMBConnection.closeResultSet(resultSetBQ);
			WMBConnection.closePreparedStatement(preparedStatement);
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
	public static void createRow(InputTextRow inputTextRow) throws SQLException {

		fillBQStoreCd(inputTextRow);

		Path path = Paths.get(ConfigReader.getProperty("INPUT_FILE_PATH"));

		try(BufferedWriter writer = Files.newBufferedWriter(path)) {
			writer.write(inputTextRow.join());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//		rowStringBuilder.append("1")	//Record_Identifier	1
		//		.append(storeCd)	//Store_Code	6
		//		.append("ddmmyyyy")	//Stock_Date	8
		//		.append(bQCd)	//BQCode	8
		//		.append("000000001.9")	//Current_Stock_Quantity	11
		//		.append("00000000000")	//Optimum_Stock_Quantity	11
		//		.append("00000000000")	//On_Order_Quantity	11
		//		.append("1")	//Ranged_Flag	1
		//		.append("000")	//CDL	3
		//		.append("0")	//Out_Of_Stock	1
		//		.append("0")	//Stocked_Flag	1
		//		.append("ddmmyyyy");	//Creation_Date	8
		//		
		//		return rowStringBuilder.toString();
	}

	
	
}
