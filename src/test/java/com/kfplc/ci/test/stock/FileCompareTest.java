package com.kfplc.ci.test.stock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.contentOf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;
import com.kfplc.ci.stock.ConfigReader;
import com.kfplc.ci.stock.UnprocessedLogs;

public class FileCompareTest {
	
//	File actualFile = new File("/support/home/esradm/SAPR3toStockAPI_1000.csv");
//	File expectedFile = new File("/support/home/esradm/SAPR3toStockAPI_1010.csv");
//	File actualFile = new File("/BODSSHARE/UKBQ/DSOUT/StockService/New_folder/SAPR3toStockAPI_sorted.csv");
//	File expectedFile = new File("/BODSSHARE/UKBQ/DSOUT/StockService/New_folder/SAPR3toStockAPI.csv_bkp_sorted");
	File actualFile = new File(ConfigReader.getProperty("ACTUAL_CSV_FILE_PATH"));
	File expectedFile = new File(ConfigReader.getProperty("EXPECTED_CSV_FILE_PATH"));
	
	File unprocessedLogLhs = new File(ConfigReader.getProperty("UNPROCESSED_LOG_LHS_PATH"));
	File unprocessedLogRhs = new File(ConfigReader.getProperty("UNPROCESSED_LOG_RHS_PATH"));
	
	
	@Test
	 public void compareWithAssertJ() {

		 assertThat(actualFile).hasSameContentAs(expectedFile);
		
	}
	
	@Test
	 public void parseUnprocessedLogs() throws IOException {

		UnprocessedLogs unprocessedLogs = new UnprocessedLogs();
		unprocessedLogs.parseLogFile();
		assertThat(unprocessedLogRhs).hasSameContentAs(unprocessedLogLhs);
		
	}

	
	//@Test
	/* public void compareWithJUnit() throws IOException {
		BufferedReader actual = new BufferedReader(new FileReader(actualFile));
		BufferedReader expected = new BufferedReader(new FileReader(expectedFile));
		String expectedLine;
		String actualLine;
	    while ((expectedLine = expected.readLine()) != null && expectedLine.length()!=0) {
	    	actualLine = actual.readLine();
	    	if(actualLine != null && actualLine.length() != 0) {
		    	assertEquals(expectedLine, actualLine);

	    	} else {
	    		assertNull("Expected had more lines then the actual.", actualLine);
	    	}
	    }
	    
	    assertNull("Actual had more lines then the actual.", actual.readLine());
		
	}*/
	
	//@Test
	/*public void compareWithJUnitX() {
		junitx.framework.FileAssert.assertEquals(expectedFile, actualFile);
	}*/
	

}
