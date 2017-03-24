package com.kfplc.ci.stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UnprocessedLogs {
	
	
	
	public static void main(String[] args) {
		UnprocessedLogs unprocessedLogs = new UnprocessedLogs();
		try {
			unprocessedLogs.parseLogFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private  void parseLogFile() throws IOException {
		// TODO Auto-generated method stub
		Map<String, String> unprocessedLogsMap = new HashMap<String, String>();
		BufferedReader br = null;
		String logFilePath = ConfigReader.getProperty("UNPROCESSED_LOG_FILE_PATH");
		br = new BufferedReader(new FileReader(logFilePath));
		String logLine = null;
		while( (logLine = br.readLine() ) != null) {
			
		}
		
	}

}
