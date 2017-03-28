package com.kfplc.ci.stock;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Stream;

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

	public void parseLogFile() throws IOException {
		BufferedWriter leftBw = Files.newBufferedWriter(Paths.get(ConfigReader.getProperty("UNPROCESSED_LOG_LHS_PATH")));
		BufferedWriter rightBw = Files.newBufferedWriter(Paths.get(ConfigReader.getProperty("UNPROCESSED_LOG_RHS_PATH")));
		String logFilePath = ConfigReader.getProperty("UNPROCESSED_LOG_FILE_PATH");
		Stream<java.lang.String> lines = Files.lines(Paths.get(logFilePath));
		lines.forEach(line -> writeToFiles(leftBw,rightBw,line));
		leftBw.close();
		rightBw.close();
		lines.close();
	}

	private void writeToFiles(BufferedWriter leftBw, BufferedWriter rightBw, java.lang.String line) {
		
		try {
			//leftfw.write(line.substring(0, line.length()-100));
			leftBw.write(line.substring(0, line.length()-100));
			leftBw.newLine();
			leftBw.flush();
			rightBw.write(line.substring(line.length()-100).trim());
			rightBw.newLine();
			rightBw.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
