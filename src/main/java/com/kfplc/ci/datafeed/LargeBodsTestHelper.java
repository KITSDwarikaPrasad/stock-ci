package com.kfplc.ci.datafeed;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.kfplc.ci.datafeed.util.CommandRunner;
import com.kfplc.ci.datafeed.util.ConfigReader;
import com.kfplc.ci.datafeed.util.TestCasePosition;
import com.kfplc.ci.datafeed.util.WMBConnection;

/**
 * The class with methods and flows to help in Large BODS test case
 * @author prasad01
 *
 */
public class LargeBodsTestHelper {
	
	static String directory = ConfigReader.getProperty("TARGET_OUT_DIR");
	static String fileName = ConfigReader.getProperty("CSV_FILENAME");
	static String userDir = System.getProperty("user.dir");
	static String unprocessedLogPath = ConfigReader.getProperty("UNPROCESSED_LOG_FILE_PATH");
	
	static int count = 0;
	/**
	 * The method to be invoked before running the assert statements  for large BODS Job test, all the operation to be done before the actual test
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static long invokeLargeBODSJob() throws IOException, InterruptedException {
		System.out.println("Executing large BODS Job.");
		String csvFilePath = directory + fileName ;
		System.out.println("-----------> invoking BODS Job ");
		long startTime = System.currentTimeMillis();
		CommandRunner.runShellCommandPB( userDir.concat("/script/shell"), "/bin/sh invokeBodsJob.sh");
		pollTheFileLargeJob(csvFilePath);
		long executionTimeInMinutes = (System.currentTimeMillis() - startTime) / 60000;
		logExecutionTime(executionTimeInMinutes);
		return executionTimeInMinutes;
		
	}

	/**
	 * Method to poll the target directory for the new csv file
	 * @param csvFilePath
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	private static void pollTheFileLargeJob(String strFilePath) throws IOException, InterruptedException {

		boolean fileArrived = false;
		long pollingDuration = Long.parseLong( ConfigReader.getProperty("POLLING_DURATION_SECONDS_LARGE") ) * 1000;
		long pollingInterval = Long.parseLong( ConfigReader.getProperty("POLLING_INTERVAL_SECONDS_LARGE") ) * 1000;
		System.out.println("----------> Started Polling for the csv file "+strFilePath +" , with polling interval(in milliSeconds) ="+ pollingInterval);
		long endTimeSeconds= System.currentTimeMillis() + pollingDuration;
		Path filePath = Paths.get(strFilePath);
		long fileSize = 0L;
		while (System.currentTimeMillis() < endTimeSeconds) {
			System.out.println("checking for the file..");
			if(Files.exists(filePath)) {
				System.out.println("File exists..");
				if(TestHelper.isCompletelyWritten(strFilePath)){
					long newFileSize = Files.size(filePath);
					System.out.println("newFileSize: "+ newFileSize);
					if(newFileSize > 0  && newFileSize == fileSize) {
						fileArrived = true;
						break;
					} else {
						fileSize = newFileSize;
					}
				}
			}
			Thread.sleep(pollingInterval);
		}

		if (!fileArrived) {
			System.out.println("-----------> File did not arrive.");
			throw new AssertionError("Waiting for the file "+ strFilePath + " , but the file dod not arrive.");
		}
			
	}

	/**
	 * To log the execution time for large bods job test
	 * @param timeTakenInMinutes
	 */
	private static void logExecutionTime(long timeTakenInMinutes) {
		Path path = Paths.get(ConfigReader.getProperty("EXECUTION_TIME_LOG_PATH"));
		//System.out.println("Creating input File : " + path );
		 Charset charset = Charset.forName("UTF-8");
		try(BufferedWriter writer = Files.newBufferedWriter(path,charset,StandardOpenOption.APPEND)) {
			System.out.println("--------------> Large Job execution time in Minutes: "+timeTakenInMinutes);
			writer.write(String.valueOf(timeTakenInMinutes) + "\n" );
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Asser statement for large BODS Job test.
	 * @param executionTime
	 * @throws IOException
	 */
	public static void assertExecutionTimeInLimit(long executionTime) throws IOException {
		// TODO Auto-generated method stub
		int upperLimit = calculateUpperLimit();
		if(executionTime > upperLimit) {
			throw new AssertionError("Execution took more time than normal");
		}
		
	}
	
	/**
	 * To calculate the upper limit of execution time
	 * @return
	 * @throws IOException
	 */
	private static int calculateUpperLimit() throws IOException {
		
		Map<Integer, Integer> freqMap = new HashMap<Integer, Integer>();
		Files.lines(Paths.get(ConfigReader.getProperty("EXECUTION_TIME_LOG_PATH")))
		.mapToInt( line -> Integer.parseInt(line))
		.forEach( t -> {
			if(freqMap.containsKey(t)) {
				freqMap.put(t, freqMap.get(t).intValue() + 1);
			} else {
				freqMap.put(t,  1);
			}
		});
		
		int nr = 0;
		int dr = 0;
		for(Map.Entry<Integer, Integer> entry : freqMap.entrySet()) {
			nr += entry.getKey() * entry.getValue();
			dr += entry.getValue();
		}
		
		float mean = nr / dr;
		int varriationPerc = Integer.parseInt(ConfigReader.getProperty("EXECUTION_TIME_VARRIATION%"));
		System.out.println("----------> Mean: "+ mean);
		float upperLimit = mean * (1 + varriationPerc / 100);
		return (int)upperLimit;
	}
	
	public static void prepareLargeInputFile() throws SQLException, IOException {
		System.out.println("-----------> Preparing input data for large datafeed job test from production like file.");
		Connection connection = null;
		PreparedStatement preparedStatementBQ = null;
		ResultSet resultSetBQ = null;
		String validBQCode = "25694956";

		Path path = Paths.get(ConfigReader.getProperty("INPUT_FILE_PATH"));
		Path bkpPath = Paths.get(ConfigReader.getProperty("INPUT_FILE_PATH") + "BKP");
		Files.move(path, bkpPath);
		if (Files.notExists(path)) {
			Files.createFile(path);
		}
		try {
			String sqlQueryBQCd = "select BQCODE from MBODS." + ConfigReader.getProperty("TBL_EFFECTIVE_ARTICLE")
					+ " where ROWNUM =1";
			connection = WMBConnection.getConnection();
			preparedStatementBQ = connection.prepareStatement(sqlQueryBQCd);
			resultSetBQ = preparedStatementBQ.executeQuery();
			// final String validBQCode = null;
			// if(resultSetBQ != null &&) {
			resultSetBQ.next();
			validBQCode = (resultSetBQ != null && resultSetBQ.next()) ? resultSetBQ.getString(1) : "25694956";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			WMBConnection.closeResultSet(resultSetBQ);
			WMBConnection.closePreparedStatement(preparedStatementBQ);
			WMBConnection.closeConnection(connection);
		}
		final String validBQCode1 = validBQCode;
		
		Files.lines(bkpPath).map(line -> new StringBuilder(line).replace(15, 23, validBQCode1).toString()).parallel()
				.forEach(line -> {
					try {
						updateCount();
						Files.write(path, line.concat("\n").getBytes(), StandardOpenOption.APPEND);
						if(count % 1000 == 0){
							System.out.print(".");
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				});

	}

	private static void updateCount() {
		count++;
	}

	public static void postJUnitCleanUp(TestCasePosition last) throws IOException {
		System.out.println("Reverting input files.");
		Path inputPath = Paths.get(ConfigReader.getProperty("INPUT_FILE_PATH"));
		Files.delete(inputPath);
		Files.move(Paths.get(ConfigReader.getProperty("INPUT_FILE_PATH") + "BKP"), inputPath);
		
	}
}
