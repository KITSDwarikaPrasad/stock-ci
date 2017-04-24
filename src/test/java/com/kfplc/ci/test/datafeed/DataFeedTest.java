package com.kfplc.ci.test.datafeed;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNull;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.contentOf;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import org.junit.Test;

import com.kfplc.ci.datafeed.ExpecetdCSVFile;
import com.kfplc.ci.datafeed.ExpectedCSVRow;
import com.kfplc.ci.datafeed.InputTextFile;
import com.kfplc.ci.datafeed.InputTextRow;
import com.kfplc.ci.datafeed.TestHelper;
import com.kfplc.ci.datafeed.UnprocessedLogs;
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

//	File unprocessedLogLhs = new File(ConfigReader.getProperty("UNPROCESSED_LOG_LHS_PATH"));
//	File unprocessedLogRhs = new File(ConfigReader.getProperty("UNPROCESSED_LOG_RHS_PATH"));
	File unprocessedLogFile = new File(ConfigReader.getProperty("UNPROCESSED_LOG_FILE_PATH"));
	
	/**
	 * If the Stock level is a non - Integer then it should be rounded down to nearest lower integer value
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testStockLevelRoundingNonInteger() throws IOException, InterruptedException, SQLException {
		TestHelper.logWhatToTest("testStockLevelRoundingNonInteger","If the Stock level is a non - Integer then it should be rounded down to nearest lower integer value.");
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
	//@Test
	public void testStockLevelRoundingInteger() throws IOException, InterruptedException, SQLException {
		TestHelper.logWhatToTest("testStockLevelRoundingInteger","If the Stock level is an Integer then it should be exported as same");
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
		TestHelper.logWhatToTest("testStockLevelRoundingZero"," If the Stock level is zero then it should be exported as zero");
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
	//@Test
	public void testStockLevelRoundingLargeNumber() throws IOException, InterruptedException, SQLException {
		TestHelper.logWhatToTest("testStockLevelRoundingZero", "If the Stock level is a large number then it should be exported as nearest lower integer");
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
	//@Test
	public void testStockLevelNaN() throws IOException, InterruptedException, SQLException {
		TestHelper.logWhatToTest("testStockLevelNaN", " If the Stock level is a Not a Number then it should be exported as zero");
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
	//@Test
	public void testStockLevelNegative() throws IOException, InterruptedException, SQLException {
		TestHelper.logWhatToTest("testStockLevelNegative"," If the Stock level is a Negative Number then it should be exported as zero");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setCurrent_stock_quantity("-2.9");
		
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
	 * If the 'destination' BQCode in EFFECTIVE_ARTICLE is not present then no line is created in the output file
	 *	Log this row to the 'Not Processed' file, with a reason
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testBQCodeNotFound() throws IOException, InterruptedException, SQLException {
		TestHelper.logWhatToTest("testBQCodeNotFound", " If the 'destination' BQCode in EFFECTIVE_ARTICLE is not present then no line is created in the output"
				+ " file Log this row to the 'Not Processed' file, with a reason");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setBqcode("27345338");
		
		InputTextFile.createInputTextFile(inputTextRow);
		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setNoOutputFlag(true);
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);
		
		TestHelper.invokeBODSJob();
		
		assertThat(actualFile).hasSameContentAs(expectedFile);
		assertTrue(unprocessedLogFile.exists());
		assertThat(unprocessedLogFile).hasContent(inputTextRow.formatAsRow());
		UnprocessedLogs.printUnprocessedCause(inputTextRow.formatAsRow());
		//TestHelper.cleanUpBuild();
	}
	
	/**
	 * If no Store Code exists for the 'destination' BQCode in EFFECTIVE_ARTICLE then no line is created in the output file
	 *	Log this row to the 'Not Processed' file, with a reason
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testStoreCodeNotFound() throws IOException, InterruptedException, SQLException {
		TestHelper.logWhatToTest("testStoreCodeNotFound", " If no Store Code exists for the 'destination' BQCode in EFFECTIVE_ARTICLE "
				+ "then no line is created in the output file, Log this row to the 'Not Processed' file, with a reason");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setBqcode("27345338");
		
		InputTextFile.createInputTextFile(inputTextRow);
		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setNoOutputFlag(true);
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);
		
		TestHelper.invokeBODSJob();
		
		assertThat(actualFile).hasSameContentAs(expectedFile);
		assertTrue(unprocessedLogFile.exists());
		assertThat(unprocessedLogFile).hasContent(inputTextRow.formatAsRow());
		UnprocessedLogs.printUnprocessedCause(inputTextRow.formatAsRow());
		//TestHelper.cleanUpBuild();
	}	
	
	/**
	 *  If no OPCO exists for the 'destination' BQCode in EFFECTIVE_ARTICLE then no line is created in the output file.
	 *	Log this row to the 'Not Processed' file, with a reason
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testOPCONotFound() throws IOException, InterruptedException, SQLException {
		TestHelper.logWhatToTest("testStoreCodeNotFound", " If no OPCO exists for the 'destination' BQCode in EFFECTIVE_ARTICLE then"
				+ " no line is created in the output file,  Log this row to the 'Not Processed' file, with a reason");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setBqcode("27345338");
		
		InputTextFile.createInputTextFile(inputTextRow);
		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setNoOutputFlag(true);
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);
		
		TestHelper.invokeBODSJob();
		
		assertThat(actualFile).hasSameContentAs(expectedFile);
		assertTrue(unprocessedLogFile.exists());
		assertThat(unprocessedLogFile).hasContent(inputTextRow.formatAsRow());
		UnprocessedLogs.printUnprocessedCause(inputTextRow.formatAsRow());
		//TestHelper.cleanUpBuild();
	}	
	
	/**
	 *  If the 'ranged' flag is not 0 or 1 then no line is created in the output file.
	 *	Log this row to the 'Not Processed' file, with a reason
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testRangedFlag() throws IOException, InterruptedException, SQLException {
		TestHelper.logWhatToTest("testRangedFlag", " If the 'ranged' flag is not 0 or 1 then no line is created in the output file,"
				+ "  Log this row to the 'Not Processed' file, with a reason");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setRanged_flag("2");
		InputTextFile.createInputTextFile(inputTextRow);
		
		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setNoOutputFlag(true);
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);
		
		TestHelper.invokeBODSJob();
		
		assertThat(actualFile).hasSameContentAs(expectedFile);
		assertTrue(unprocessedLogFile.exists());
		assertThat(unprocessedLogFile).hasContent(inputTextRow.formatAsRow());
		UnprocessedLogs.printUnprocessedCause(inputTextRow.formatAsRow());
		//TestHelper.cleanUpBuild();
	}
	
	/**
	 *  If multiple EANs are found for the same BQCode then these additional lines will be written to the output file
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testMultipleEANs() throws IOException, InterruptedException, SQLException {
		TestHelper.logWhatToTest("testMultipleEANs", " If multiple EANs are found for the same BQCode then these additional lines will be written to the output file");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setCurrent_stock_quantity("4");;
		InputTextFile.createInputTextFile(inputTextRow);
		
		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setStockLevel("4");
		int rowsWrittenCount = ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);
		
		TestHelper.invokeBODSJob();
		assertTrue(Files.lines(Paths.get(actualFile.getPath())).count() > 1);
		assertThat(actualFile).hasSameContentAs(expectedFile);
		//TestHelper.cleanUpBuild();
	}
	/*//	@Test
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
	}*/




}
