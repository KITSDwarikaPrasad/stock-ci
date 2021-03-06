package com.kfplc.ci.datafeed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Stream;

import com.kfplc.ci.datafeed.util.ConfigReader;


/**
 * The class having methods to parse the Unprocessed logs
 * @author prasad01
 *
 */
public class UnprocessedLogs {
	
	
	private static final Function String = null;

	/*public static void main(String[] args) {
		UnprocessedLogs unprocessedLogs = new UnprocessedLogs();
		System.out.println("main invoked");
		try {
			System.out.println("before paselogs");

			unprocessedLogs.parseLogFile();
			System.out.println("after paselogs");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}*/

	/**
	 * This method splits the Unprocessed log file content into two parts and writes to separate files as LHS_PAth and RHS_Path
	 * @throws IOException
	 */
	public static void parseLogFile() throws IOException {
		BufferedWriter leftBw = Files.newBufferedWriter(Paths.get(ConfigReader.getProperty("UNPROCESSED_LOG_LHS_PATH")));
		BufferedWriter rightBw = Files.newBufferedWriter(Paths.get(ConfigReader.getProperty("UNPROCESSED_LOG_RHS_PATH")));
		String logFilePath = ConfigReader.getProperty("UNPROCESSED_LOG_FILE_PATH");
		Stream<java.lang.String> lines = Files.lines(Paths.get(logFilePath));
		lines.forEach(line -> writeToFiles(leftBw,rightBw,line));
		leftBw.close();
		rightBw.close();
		lines.close();
	}

	/**
	 * The methods to be invoked internally only, to write the unprocessed log's content to the LHS and RHS files
	 * @param leftBw
	 * @param rightBw
	 * @param line
	 */
	private static void writeToFiles(BufferedWriter leftBw, BufferedWriter rightBw, java.lang.String line) {
		
		try {
			//leftfw.write(line.substring(0, line.length()-100));
			leftBw.write(line.substring(0, line.length()-100) + "\r\n");
			//leftBw.newLine();
			leftBw.flush();
			rightBw.write(line.substring(line.length()-100).trim()  + "\r\n");
			//rightBw.newLine();
			rightBw.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void printUnprocessedCause(String inputRow) throws IOException {
		// TODO Auto-generated method stub
		String logFilePath = ConfigReader.getProperty("UNPROCESSED_LOG_FILE_PATH");
		Stream<java.lang.String> lines = Files.lines(Paths.get(logFilePath));
		lines.forEach(line -> {
			if(line.contains(inputRow)) {
				System.out.println("Input Row:"+ inputRow + ", Unprocessed due to Error: "+ line.substring(100, line.length()).trim());
			}
			
		});
	}





	/*private  void parseLogFile() throws IOException {
		// TODO Auto-generated method stub
		String logFilePath = ConfigReader.getProperty("UNPROCESSED_LOG_FILE_PATH");
		try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(logFilePath))) {
			String logLine = null;
			while( (logLine = bufferedReader.readLine() ) != null) {
				writeToExpectedFile(logLine.substring(0, logLine.length()-100));
				writeToActualFile(logLine.substring(100));
			}
		}
	}

	private void writeToActualFile(String rSubstring) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ConfigReader.getProperty("UNPROCESSED_LOG_RHS_PATH")))) {
			writer.write(rSubstring.trim());
		}
	}

	private void writeToExpectedFile(String lSubstring) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(ConfigReader.getProperty("UNPROCESSED_LOG_LHS_PATH")))) {
			writer.write(lSubstring.trim());
		}
	}*/
	

}
