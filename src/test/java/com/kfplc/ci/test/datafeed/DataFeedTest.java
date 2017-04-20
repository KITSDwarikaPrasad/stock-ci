package com.kfplc.ci.test.datafeed;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNull;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.contentOf;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.stream.Stream;

import org.junit.Test;

import com.kfplc.ci.datafeed.ExpecetdCSVFile;
import com.kfplc.ci.datafeed.ExpectedCSVRow;
import com.kfplc.ci.datafeed.InputTextFile;
import com.kfplc.ci.datafeed.InputTextRow;
import com.kfplc.ci.datafeed.TestHelper;
import com.kfplc.ci.datafeed.util.ConfigReader;

/**
 * Write the test cases in this file 
 *	<p>steps: </p>
 *  <p>InputTextRow - create row for Input to BODS job</p>
 *  <p>createExpectedCSVFile - create the expected CSV File w.r.t. InputTextRow.</p>
 *  <p>TestHelper.invokeBODSJob() - Invoke BODS Job remotely </p>
 *  <p>assert statement</p>
 * 
 * @author prasad01
 *
 */
public class DataFeedTest {

	String directory = ConfigReader.getProperty("TARGET_OUT_DIR");
	File actualFile = new File(directory + ConfigReader.getProperty("CSV_FILENAME") + "_Actual");
	File expectedFile = new File(directory + ConfigReader.getProperty("CSV_FILENAME") + "_Expected");

	File unprocessedLogLhs = new File(ConfigReader.getProperty("UNPROCESSED_LOG_LHS_PATH"));
	File unprocessedLogRhs = new File(ConfigReader.getProperty("UNPROCESSED_LOG_RHS_PATH"));



	/**
	 * If the Stock level is a non - Integer then it should be rounded down to nearest lower integer value
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	@Test
	public void testStockLevelRoundingNonInteger() throws IOException, InterruptedException, SQLException {
		System.out.println("To Test - If the Stock level is a non - Integer then it should be rounded down to nearest lower integer value.");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setCurrent_stock_quantity("1.999");
		
		InputTextFile.createInputTextFile(inputTextRow);
		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setStockLevel("1");
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);
		
		TestHelper.invokeBODSJob();
		
		assertThat(actualFile).hasSameContentAs(expectedFile);
		//TestHelper.cleanUpBuild();
	}

	/**
	 * If the Stock level is a Integer then it should be exported as same
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	@Test
	public void testStockLevelRoundingInteger() throws IOException, InterruptedException, SQLException {
		System.out.println("To Test - If the Stock level is a Integer then it should be exported as same");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setCurrent_stock_quantity("3.000");
		
		InputTextFile.createInputTextFile(inputTextRow);
		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setStockLevel("3");
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);
		
		TestHelper.invokeBODSJob();
		
		assertThat(actualFile).hasSameContentAs(expectedFile);
		//TestHelper.cleanUpBuild();
	}
	
	/**
	 * If the Stock level is a Zero then it should be exported as same
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	@Test
	public void testStockLevelRoundingZero() throws IOException, InterruptedException, SQLException {
		System.out.println("To Test - If the Stock level is a Integer then it should be exported as same");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setCurrent_stock_quantity("0.000");
		
		InputTextFile.createInputTextFile(inputTextRow);
		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setStockLevel("0");
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);
		
		TestHelper.invokeBODSJob();
		
		assertThat(actualFile).hasSameContentAs(expectedFile);
		//TestHelper.cleanUpBuild();
	}
	
	/**
	 * If the Stock level is a Large number then it should be exported as nearest lower integer
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	@Test
	public void testStockLevelRoundingLargeNumber() throws IOException, InterruptedException, SQLException {
		System.out.println("To Test - If the Stock level is a Integer then it should be exported as same");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setCurrent_stock_quantity("9999999.999");
		
		InputTextFile.createInputTextFile(inputTextRow);
		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setStockLevel("9999999");
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);
		
		TestHelper.invokeBODSJob();
		
		assertThat(actualFile).hasSameContentAs(expectedFile);
		//TestHelper.cleanUpBuild();
	}
	
	/**
	 * If the Stock level is a Not a Number then it should be exported as zero
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	@Test
	public void testStockLevelNaN() throws IOException, InterruptedException, SQLException {
		System.out.println("To Test - If the Stock level is a Integer then it should be exported as same");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setCurrent_stock_quantity("aaaaaaaaaaa");
		
		InputTextFile.createInputTextFile(inputTextRow);
		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setStockLevel("0");
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);
		
		TestHelper.invokeBODSJob();
		
		assertThat(actualFile).hasSameContentAs(expectedFile);
		//TestHelper.cleanUpBuild();
	}
	
	/**
	 * If the Stock level is a Negative Number then it should be exported as zero
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	@Test
	public void testStockLevelNegative() throws IOException, InterruptedException, SQLException {
		System.out.println("To Test - If the Stock level is a Integer then it should be exported as same");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setCurrent_stock_quantity("aaaaaaaaaaa");
		
		InputTextFile.createInputTextFile(inputTextRow);
		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setStockLevel("0");
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);
		
		TestHelper.invokeBODSJob();
		
		assertThat(actualFile).hasSameContentAs(expectedFile);
		//TestHelper.cleanUpBuild();
	}
	
	//	@Test
	public void parseUnprocessedLogs() throws IOException {

		//		UnprocessedLogs unprocessedLogs = new UnprocessedLogs();
		//		unprocessedLogs.parseLogFile();
		//		assertThat(unprocessedLogRhs).hasSameContentAs(unprocessedLogLhs);
		String logFilePath = ConfigReader.getProperty("UNPROCESSED_LOG_FILE_PATH");
		Stream<java.lang.String> lines = Files.lines(Paths.get(logFilePath));
		StringBuilder sb = new StringBuilder("");
		lines.forEach(line -> 
		sb.append(String.format("%s could not be processed due to error : %s %n", line.substring(0, line.length()-100),line.substring(line.length()-100).trim()))
				);
		lines.close();
		throw new AssertionError(sb.toString());
	}




}
