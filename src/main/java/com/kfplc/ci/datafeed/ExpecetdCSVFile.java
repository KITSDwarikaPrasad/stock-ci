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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.kfplc.ci.datafeed.util.ConfigReader;
import com.kfplc.ci.datafeed.util.WMBConnection;

public class ExpecetdCSVFile {
	
	private static Connection connection;
	private static PreparedStatement preparedStatementEan;
	private static PreparedStatement preparedStatementStoreCd;
	private static ResultSet resultSetEan;
	private static ResultSet resultSetStoreCd;


	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		//System.out.println(fetchDataFromDB("PBI964", "1.9", "27345337"));
	}

	
	/**
	 * This method looks into Database to fetch EAN, OPCO and NUMSTORECODE based on fullStorecode and BQcode in Input
	 * @param fullStoreCode
	 * @param expecetdCSVRow
	 * @param bQCd - to get corresponding EAN from Database
	 * @return List of ExpectedCSVRow objects
	 * @throws SQLException
	 */
	public static List<ExpectedCSVRow> fetchDataFromDB(String fullStoreCode, ExpectedCSVRow expecetdCSVRow, String bQCd) throws SQLException {
		
		List<ExpectedCSVRow> expectedCSVRowList = new ArrayList<ExpectedCSVRow>();
		//read store_code and BQCode from database
		try {
			String sqlQueryStoreCd = "select distinct NUMSTORECODE, OPCO from MBREPOS.MBSTRCD where FULLSTORECODE = ?";
			String sqlQueryEan = "select EAN from MBODS."+ ConfigReader.getProperty("TBL_EFFECTIVE_ARTICLE")+" where BQCODE = ?";
			String ean = "";
			
			connection = WMBConnection.getConnection();
			
			preparedStatementEan = connection.prepareStatement(sqlQueryEan);
			preparedStatementEan.setString(1, bQCd);
			resultSetEan = preparedStatementEan.executeQuery();
			if( resultSetEan != null &&  resultSetEan.next() ) {
				ean = resultSetEan.getString(1);
			}
			
			preparedStatementStoreCd = connection.prepareStatement(sqlQueryStoreCd);
			preparedStatementStoreCd.setString(1, fullStoreCode);
			resultSetStoreCd = preparedStatementStoreCd.executeQuery();
			while( resultSetStoreCd != null &&  resultSetStoreCd.next() ) {
				ExpectedCSVRow expectedCSVRow= new  ExpectedCSVRow();
				expectedCSVRow.setStoreCode(resultSetStoreCd.getString(1));
				expectedCSVRow.setEan(ean);
				expectedCSVRow.setStockLevel(expecetdCSVRow.getStockLevel());
				//Below- take OPCO from Database only if it is not set in Test case
				if( expecetdCSVRow.getOpco() == null) {
					expectedCSVRow.setOpco(resultSetStoreCd.getString(2));
				}
				expectedCSVRowList.add(expectedCSVRow);
			}
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch blockh
			e.printStackTrace();
		} finally {
			WMBConnection.closeResultSet(resultSetEan);
			WMBConnection.closePreparedStatement(preparedStatementEan);
			WMBConnection.closeResultSet(resultSetStoreCd);
			WMBConnection.closePreparedStatement(preparedStatementStoreCd);
			WMBConnection.closeConnection(connection);
		}
		
		// File writter
		
		
		return expectedCSVRowList;
	}
	
	

	/**The method to create a new Expected CSV file by looking into Database
	 * @param inputTextRow - The object holding the different sections of the input row for BODS job
	 * @param expecetdCSVRow 
	 * @throws SQLException
	 * @throws IOException 
	 */
	public static int createExpectedCSVFile(InputTextRow inputTextRow, ExpectedCSVRow expecetdCSVRow) throws SQLException, IOException {
		int rowsWrittenCount = 0;
		Path path = Paths.get(ConfigReader.getProperty("TARGET_OUT_DIR") + ConfigReader.getProperty("CSV_FILENAME") + "_Expected.0");
		
		if(!expecetdCSVRow.isNoOutputFlag()) {
			List<ExpectedCSVRow> expectedCSVRowList =  fetchDataFromDB(inputTextRow.getFull_store_code(), expecetdCSVRow, inputTextRow.getBqcode());
			rowsWrittenCount = expectedCSVRowList.size();
			//System.out.println("Creating Expected CSV File : " + path );
			try(BufferedWriter writer = Files.newBufferedWriter(path)) {
				if(expecetdCSVRow.isHasHeaderFlag()) {
					writer.write(ConfigReader.getProperty("EXPECTED_FILE_HEADER"));
				}
				String outputRow = null;
				for (ExpectedCSVRow expectedCSVRow : expectedCSVRowList) {
					outputRow = expectedCSVRow.formatAsRow();
					System.out.println("------------> Expected Row : "+ outputRow);
					writer.write("\r\n" + outputRow);
				}
			} 
			
		} else {
			Files.createFile(path);
			if(expecetdCSVRow.isHasHeaderFlag()) {
				try(BufferedWriter writer = Files.newBufferedWriter(path)) {
					writer.write(ConfigReader.getProperty("EXPECTED_FILE_HEADER"));
					System.out.println("EXPECTED_FILE_HEADER : "+ ConfigReader.getProperty("EXPECTED_FILE_HEADER"));
				}
			}
		}
		return rowsWrittenCount;
	}


	/**
	 * Sorts the data of the small file
	 * @param source
	 * @throws IOException 
	 */
	public static void sortData(String source, String destination) throws IOException {
//		System.out.println("------> starting sorting of csv file -- StartTime: "+new Date());
		System.out.println("-------->To sort "+ source +" to destination "+ destination);
		Stream<java.lang.String> linesStream = Files.lines(Paths.get(source));
		List<String> sortedLinesList = linesStream.sorted().collect(Collectors.toList());
		try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(destination))) {
			for (String line : sortedLinesList) {
				System.out.println("%%%%%%%%%%%%%%%%%%% : "+ line);
				writer.write(line + "\r\n");
			}
		} catch (IOException ioe) {
			System.out.println("sortData exception: "+ ioe.getMessage());
		}
		linesStream.close();
//		System.out.println( "---------> Sorting finished..-- EndTime: "+new Date() );

	}
	
/*	*//**
	 * @param inputTextRowList
	 * @param expecetdCSVRowList
	 * @throws SQLException
	 * @throws IOException
	 *//*
	public static void createExpectedCSVFile(List<InputTextRow> inputTextRowList, List<ExpectedCSVRow> expecetdCSVRowList) throws SQLException, IOException {
		Path path = Paths.get(ConfigReader.getProperty("TARGET_OUT_DIR") + ConfigReader.getProperty("CSV_FILENAME") + "_Expected.0");
		
		for (int rowNumber = 0; rowNumber < inputTextRowList.size(); rowNumber++ ) {
			if(!expecetdCSVRowList.get(rowNumber).isNoOutputFlag()) {
				List<ExpectedCSVRow> expectedCSVRowListDB =  fetchDataFromDB(inputTextRowList.get(rowNumber).getFull_store_code(), expecetdCSVRowList.get(rowNumber).getStockLevel(), inputTextRowList.get(rowNumber).getBqcode());
				
				//System.out.println("Creating Expected CSV File : " + path );
				try(BufferedWriter writer = Files.newBufferedWriter(path)) {
					writer.write(ConfigReader.getProperty("EXPECTED_FILE_HEADER"));
					String outputRow = null;
					for (ExpectedCSVRow expectedCSVRow : expectedCSVRowListDB) {
						outputRow = expectedCSVRow.formatAsRow();
						System.out.println("------------> Expected Row : "+ outputRow);
						writer.write("\r\n" + outputRow);
					}
				} 
				
			} else {
				if(Files.notExists(path)) {
					Files.createFile(path);
				}
			}
			
		}
	}*/

}
