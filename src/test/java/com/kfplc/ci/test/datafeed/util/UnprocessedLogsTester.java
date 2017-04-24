package com.kfplc.ci.test.datafeed.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.kfplc.ci.datafeed.util.ConfigReader;

public class UnprocessedLogsTester {
	public static void assertIfContains(String inputRow) throws IOException {
		String logFilePath = ConfigReader.getProperty("UNPROCESSED_LOG_FILE_PATH");
		boolean found= false;
		ArrayList<String> matchingLogLines= new ArrayList<String>();
		try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(logFilePath))) {
			String logLine = null;
			while( (logLine = bufferedReader.readLine() ) != null) {
				if(logLine.contains(inputRow)) {
					found = true;
					matchingLogLines.add(logLine);
				}
					
					
			}
		}
		assertTrue("Input Data ["+ inputRow +"] not found in Unprocessed logs"+ matchingLogLines, found == true);
		
	}
}
