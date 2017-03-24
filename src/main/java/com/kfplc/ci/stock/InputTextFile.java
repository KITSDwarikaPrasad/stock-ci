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

public class InputTextFile {

	private static Connection connection;
	private static PreparedStatement preparedStatement;
	private static PreparedStatement preparedStatementBQ;
	private static ResultSet resultSet;
	private static ResultSet resultSetBQ;

	public static void main(String[] args) {

		try {
			createTextFile();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * To create the input te
	 * @throws SQLException
	 */
	private static void createTextFile() throws SQLException {
		
		String storeCd = null;
		String bQCd = null;
		//read store_code and BQCode from database
		try {
			
			String sqlQueryStoreCd = "select FULLSTORECODE from MBREPOS.MBSTRCD where ROWNUM =1";
			
			connection = WMBConnection.getConnection();
			preparedStatement = connection.prepareStatement(sqlQueryStoreCd);
			resultSet = preparedStatement.executeQuery();
			if(resultSet != null && resultSet.next()) {
				storeCd = resultSet.getString(1);
			}
			
			
			String sqlQueryBQCd = "select BQCODE from MBODS.EFFECTIVE_ARTICLE_DELTA_RES where ROWNUM =1";
			preparedStatementBQ = connection.prepareStatement(sqlQueryBQCd);
			resultSetBQ = preparedStatementBQ.executeQuery();
			if(resultSetBQ != null && resultSetBQ.next()) {
				bQCd = resultSetBQ.getString(1);
			}
			
			System.out.println("storeCd: "+storeCd +",bQCd: "+bQCd);
			
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
		
		Path path = Paths.get("C:/Users/prasad01/work/JUnitCI/Text on the fly/ZBQSTOCK_TEST_INPUT.txt");
		
		try(BufferedWriter writer = Files.newBufferedWriter(path)) {
			String inputRow = createRow(storeCd,bQCd);
			writer.write(inputRow);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * To create a row , sample row : 1FAE11102102016201158830000016.000000000000000000000000010000003102016
	 * @param storeCd
	 * @param bQCd
	 * @return String 
	 */
	private static String createRow(String storeCd, String bQCd) {
		
		StringBuilder rowStringBuilder = new StringBuilder("");
		rowStringBuilder.append("1")	//Record_Identifier	1
		.append(storeCd)	//Store_Code	6
		.append("ddmmyyyy")	//Stock_Date	8
		.append(bQCd)	//BQCode	8
		.append("000000001.9")	//Current_Stock_Quantity	11
		.append("00000000000")	//Optimum_Stock_Quantity	11
		.append("00000000000")	//On_Order_Quantity	11
		.append("1")	//Ranged_Flag	1
		.append("000")	//CDL	3
		.append("0")	//Out_Of_Stock	1
		.append("0")	//Stocked_Flag	1
		.append("ddmmyyyy");	//Creation_Date	8
		
		return rowStringBuilder.toString();
	}

	
	
}
