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
	 * First Test case
	 * 	steps:
	 * 		InputTextRow - create row for Input to BODS job
	 *		TestHelper.preUnitTest() - Invoke preJunit which internally invokes the BODS job remotely
	 *		assert statement
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	@Test
	public void testStockLevelRounding() throws IOException, InterruptedException, SQLException {
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setCurrent_stock_quantity("1.9");
		InputTextFile.createInputTextFile(inputTextRow);
		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setStockLevel("2");
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);
		
		TestHelper.preUnitTest();
		
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
