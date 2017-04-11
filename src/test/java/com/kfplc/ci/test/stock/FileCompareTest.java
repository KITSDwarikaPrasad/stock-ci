package com.kfplc.ci.test.stock;

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
import com.kfplc.ci.stock.ConfigReader;
import com.kfplc.ci.stock.InputTextFile;
import com.kfplc.ci.stock.InputTextRow;
import com.kfplc.ci.stock.util.TestHelper;

public class FileCompareTest {

	String directory = ConfigReader.getProperty("TARGET_OUT_DIR");
	File actualFile = new File(directory + ConfigReader.getProperty("ACTUAL_CSV_FILENAME"));
	File expectedFile = new File(directory + ConfigReader.getProperty("EXPECTED_CSV_FILENAME"));

	File unprocessedLogLhs = new File(ConfigReader.getProperty("UNPROCESSED_LOG_LHS_PATH"));
	File unprocessedLogRhs = new File(ConfigReader.getProperty("UNPROCESSED_LOG_RHS_PATH"));



	@Test
	public void compareWithAssertJ() throws IOException, InterruptedException, SQLException {
		InputTextRow inputTextRow = new InputTextRow();
		inputTextRow.setCurrent_stock_quantity("20");
		
		InputTextFile.createRow(inputTextRow);
		TestHelper.preUnitTest();
		assertThat(actualFile).hasSameContentAs(expectedFile);
		TestHelper.cleanUpBuild();
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
