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
import com.kfplc.ci.datafeed.LargeBodsTestHelper;
import com.kfplc.ci.datafeed.TestHelper;
import com.kfplc.ci.datafeed.util.CommandRunner;
import com.kfplc.ci.datafeed.util.ConfigReader;
import com.kfplc.ci.datafeed.util.TestCasePosition;
import com.kfplc.ci.test.datafeed.util.UnprocessedLogsTester;

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

	String testName = "";
	/**
	 * If the Stock level is a non - Integer then it should be rounded down to nearest lower integer value
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	static {
		CommandRunner.authenticateKerberos();
	}
	
		//@Test
	public void testStockLevelRoundingNonInteger() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();
		TestHelper.logWhatToTest(testName,"If the Stock level is a non - Integer then it should be rounded down to nearest lower integer value.");
		//Cleanup
		TestHelper.preJUnitCleanUp(TestCasePosition.FIRST);
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
		TestHelper.postJUnitCleanUp(testName);
	}

	/**
	 * If the Stock level is a Integer then it should be exported as same
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testStockLevelRoundingInteger() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();
		TestHelper.logWhatToTest(testName,"If the Stock level is an Integer then it should be exported as same");
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
		TestHelper.postJUnitCleanUp(testName);
	}

	/**
	 * If the Stock level is a Zero then it should be exported as same
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
		//@Test
	public void testStockLevelRoundingZero() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName," If the Stock level is 0.000 then it should be exported as zero");
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
		TestHelper.postJUnitCleanUp(testName);
	}

	/**
	 * If the Stock level is a Large number then it should be exported as nearest lower integer
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testStockLevelRoundingLargeNumber() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName, "If the Stock level is a large number then it should be exported as nearest lower integer");
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
		TestHelper.postJUnitCleanUp(testName);
	}

	/**
	 * If the Stock level is a Not a Number then it should be exported as zero
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testStockLevelNaN() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName, " If the Stock level is a Not a Number then it should be exported as zero");
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
		TestHelper.postJUnitCleanUp(testName);
	}

	/**
	 * If the Stock level is a Negative Number then it should be exported as zero
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testStockLevelNegative() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName," If the Stock level is a Negative Number then it should be exported as zero");
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
		TestHelper.postJUnitCleanUp(testName);
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
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName, " If the 'destination' BQCode in EFFECTIVE_ARTICLE is not present then no line is created in the output"
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
		assertTrue("unprocessedLogFile not found!",unprocessedLogFile.exists());
		UnprocessedLogsTester.assertIfContains(inputTextRow.formatAsRow());
		UnprocessedLogsTester.assertReason(inputTextRow.formatAsRow(), ConfigReader.getProperty("BQCODE_NULL_INVALID"));

		TestHelper.postJUnitCleanUp(testName);
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
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName, " If no Store Code exists for the 'destination' BQCode in EFFECTIVE_ARTICLE "
				+ "then no line is created in the output file, Log this row to the 'Not Processed' file, with a reason");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setBqcode("27345337");
		inputTextRow.setFull_store_code("RFI141");

		InputTextFile.createInputTextFile(inputTextRow);
		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setNoOutputFlag(true);
		//expecetdCSVRow.setHasHeaderFlag(false);
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);

		TestHelper.invokeBODSJob();

		assertThat(actualFile).hasSameContentAs(expectedFile);
		assertTrue("unprocessedLogFile not found!",unprocessedLogFile.exists());
		UnprocessedLogsTester.assertIfContains(inputTextRow.formatAsRow());
		UnprocessedLogsTester.assertReason(inputTextRow.formatAsRow(), ConfigReader.getProperty("STORECODE_NULL_INVALID"));
		TestHelper.postJUnitCleanUp(testName);
	}	

	/**
	 *  If no OPCO exists for the 'destination' BQCode in EFFECTIVE_ARTICLE then no line is created in the output file.
	 *	Log this row to the 'Not Processed' file, with a reason
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
//	//@Test
	public void testOPCONotFound() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName, " If no OPCO exists for the 'destination' BQCode in EFFECTIVE_ARTICLE then"
				+ " no line is created in the output file,  Log this row to the 'Not Processed' file, with a reason");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setBqcode("27345337");

		InputTextFile.createInputTextFile(inputTextRow);
		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setNoOutputFlag(true);
		expecetdCSVRow.setHasHeaderFlag(false);
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);

		TestHelper.invokeBODSJob();

		assertThat(actualFile).hasSameContentAs(expectedFile);
		assertTrue("unprocessedLogFile not found!",unprocessedLogFile.exists());
		UnprocessedLogsTester.assertIfContains(inputTextRow.formatAsRow());
		UnprocessedLogsTester.assertReason(inputTextRow.formatAsRow(), ConfigReader.getProperty("OPCO_NULL_INVALID"));
		TestHelper.postJUnitCleanUp(testName);
	}	

	/**
	 *  If the 'ranged' flag is not 0 or 1 then no line is created in the output file.
	 *	Log this row to the 'Not Processed' file, with a reason
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testRangedFlagNot01() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName, " If the 'ranged' flag is not 0 or 1 then no line is created in the output file,"
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
		//expecetdCSVRow.setHasHeaderFlag(false);
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);

		TestHelper.invokeBODSJob();

		assertThat(actualFile).hasSameContentAs(expectedFile);
		assertTrue("unprocessedLogFile not found!",unprocessedLogFile.exists());
		UnprocessedLogsTester.assertIfContains(inputTextRow.formatAsRow());
		UnprocessedLogsTester.assertReason(inputTextRow.formatAsRow(), ConfigReader.getProperty("RANGED_FLAG_NOT_0_1"));
		TestHelper.postJUnitCleanUp(testName);
	}

	/**
	 *  If the 'ranged' flag is not 0 or 1 but if it is 'a' then no line is created in the output file.
	 *	Log this row to the 'Not Processed' file, with a reason
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testRangedFlagNaN() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName, " If the 'ranged' flag is not 0 or 1 then no line is created in the output file,"
				+ "  Log this row to the 'Not Processed' file, with a reason");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File

		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setRanged_flag("a");
		InputTextFile.createInputTextFile(inputTextRow);

		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setNoOutputFlag(true);
		//expecetdCSVRow.setHasHeaderFlag(false);
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);

		TestHelper.invokeBODSJob();

		assertThat(actualFile).hasSameContentAs(expectedFile);
		assertTrue("unprocessedLogFile not found!",unprocessedLogFile.exists());
		UnprocessedLogsTester.assertIfContains(inputTextRow.formatAsRow());
		UnprocessedLogsTester.assertReason(inputTextRow.formatAsRow(), ConfigReader.getProperty("RANGED_FLAG_NOT_INTEGER"));
		TestHelper.postJUnitCleanUp(testName);
	}
	
	/**
	 *  Large BODS job Test - Test the time the BODS job takes and fail it if it takes > x % longer than usual
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testLargeBODSJob() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName, " Test the time the BODS job takes and fail it if it takes > x % longer than usual");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File

//		InputTextRow inputTextRow = new InputTextRow();
//		inputTextRow.setRanged_flag("0");
//		inputTextRow.setCurrent_stock_quantity("1.999");
//		InputTextFile.createInputTextFile(inputTextRow);
//
//		//Create Expeccted CSV File
//		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
//		expecetdCSVRow.setStockLevel("1");
//		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);
//
//		TestHelper.invokeBODSJob();
//
//		assertThat(actualFile).hasSameContentAs(expectedFile);
//		TestHelper.postJUnitCleanUp(testName);
	}
	
	/**
	 *  If the 'ranged' flag is 0 then csv file should be created.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testRangedFlagIs1() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName, " If the 'ranged' flag is 0 then csv file should be created.");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File

		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setRanged_flag("1");
		inputTextRow.setCurrent_stock_quantity("1.999");
		InputTextFile.createInputTextFile(inputTextRow);

		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setStockLevel("1");
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);

		TestHelper.invokeBODSJob();

		assertThat(actualFile).hasSameContentAs(expectedFile);
		TestHelper.postJUnitCleanUp(testName);
	}
	
	/**
	 *  If multiple EANs are found for the same BQCode then these additional lines will be written to the output file
	 * @throws IOException
	 * @throws InterruptedException	
	 * @throws SQLException
	 */
	//@Test
	public void testMultipleEANs() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName, " If multiple EANs are found for the same BQCode then these additional lines will be written to the output file");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File

		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setCurrent_stock_quantity("4.00");;
		InputTextFile.createInputTextFile(inputTextRow);

		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setStockLevel("4");
		int rowsWrittenCount = ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);

		TestHelper.invokeBODSJob();
		long lineCount = Files.lines(Paths.get(actualFile.getPath())).count();
		assertTrue("Expected multiple lines but got only "+ lineCount, lineCount> 2);
		assertThat(actualFile).hasSameContentAs(expectedFile);
		TestHelper.postJUnitCleanUp(testName);
		
		
	}
	
	/**
	 *  If the 'ranged' flag is 0 then csv file should be created.
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testRangedFlagIs0() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName, " If the 'ranged' flag is 0 then csv file should be created.");
		//Cleanup
		TestHelper.preJUnitCleanUp();
		//Create Input Text File

		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setRanged_flag("0");
		inputTextRow.setCurrent_stock_quantity("1.999");
		InputTextFile.createInputTextFile(inputTextRow);

		//Create Expeccted CSV File
		ExpectedCSVRow expecetdCSVRow = new ExpectedCSVRow();
		expecetdCSVRow.setStockLevel("1");
		ExpecetdCSVFile.createExpectedCSVFile(inputTextRow, expecetdCSVRow);

		TestHelper.invokeBODSJob();

		assertThat(actualFile).hasSameContentAs(expectedFile);
		TestHelper.postJUnitCleanUp(testName);
	}
	
	/**
	 * Test the execution time of a large BODS Job
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	@Test
	public void testLargeDatafeed() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName, "  Test the execution time of a large BODS Job");
		//Cleanup
		TestHelper.preJUnitCleanUp(TestCasePosition.LAST);
		//Create Input Text File
		LargeBodsTestHelper.prepareLargeInputFile();
		long executionTime = LargeBodsTestHelper.invokeLargeBODSJob();
		LargeBodsTestHelper.postJUnitCleanUp(TestCasePosition.LAST); //call this in conjugation with prepareLargeInputFile()
		LargeBodsTestHelper.assertExecutionTimeInLimit(executionTime);
		
	}
	
	/**
	 * Test the job with custom input data
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	//@Test
	public void testCustomInput() throws IOException, InterruptedException, SQLException {
		testName = Thread.currentThread().getStackTrace()[1].getMethodName();

		TestHelper.logWhatToTest(testName, "  Test the job with custom input data");
		//Cleanup
		TestHelper.preJUnitCleanUp(TestCasePosition.LAST);
		//Create Input Text File
		//LargeBodsTestHelper.prepareLargeInputFile();
		LargeBodsTestHelper.prapareInputWithCustomData("1FAE11101052017256949560000051.000000000000000000000000010000002052017");
		long executionTime = LargeBodsTestHelper.invokeLargeBODSJob();
		LargeBodsTestHelper.postJUnitCleanUp(TestCasePosition.LAST); //call this in conjugation with createInputWithCustomData()
		LargeBodsTestHelper.assertCSVFileHasData();
		
	}
	

}
