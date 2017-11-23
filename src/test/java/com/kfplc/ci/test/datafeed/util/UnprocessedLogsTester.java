package com.kfplc.ci.test.datafeed.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertFalse;
import static org.assertj.core.api.Assertions.assertThat;

import com.kfplc.ci.datafeed.UnprocessedLogs;
import com.kfplc.ci.datafeed.util.ConfigReader;

/**
 * @author prasad01
 *
 */
public class UnprocessedLogsTester {
	/**
	 * To assert if the Unprocessed logs file contains the inputRow
	 * @param inputRow 
	 * @throws IOException
	 */
	public static void assertIfContains(String inputRow) throws IOException {
		String logFilePath = ConfigReader.getProperty("UNPROCESSED_LOG_FILE_PATH");
		boolean found= false;
		//ArrayList<String> matchingLogLines= new ArrayList<String>();
		try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(logFilePath))) {
			String logLine = null;
			while( (logLine = bufferedReader.readLine() ) != null) {
				if(logLine.contains(inputRow)) {
					found = true;
					//matchingLogLines.add(logLine);
				}
					
					
			}
		}
		assertTrue("Input Data ["+ inputRow +"] not found in Unprocessed logs", found == true);
		UnprocessedLogs.printUnprocessedCause(inputRow);
	}

	/**
	 * To assert if the reason for Not Processed is same as reason
	 * @param inputRow
	 * @param property
	 * @throws IOException 
	 */
	public static void assertReason(String inputRow, String reason) throws IOException {
		String logFilePath = ConfigReader.getProperty("UNPROCESSED_LOG_FILE_PATH");
		boolean found= false;
		
		//ArrayList<String> matchingLogLines= new ArrayList<String>();
		try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(logFilePath))) {
			String logLine = null;
			while( (logLine = bufferedReader.readLine() ) != null) {
				if(logLine.contains(inputRow)) {
					found = true;
					assertThat(logLine).containsIgnoringCase(reason);
					//matchingLogLines.add(logLine);
				}
					
					
			}
		}
	}
	
}
