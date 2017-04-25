package com.kfplc.ci.datafeed;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import com.kfplc.ci.datafeed.util.CommandRunner;
import com.kfplc.ci.datafeed.util.ConfigReader;

/**
 * The class with methods and flows to help in one test case
 * @author prasad01
 *
 */
public class TestHelper {

	static String directory = ConfigReader.getProperty("TARGET_OUT_DIR");
	static String fileName = ConfigReader.getProperty("CSV_FILENAME");
	static String userDir = System.getProperty("user.dir");
	static String unprocessedLogPath = ConfigReader.getProperty("UNPROCESSED_LOG_FILE_PATH");

	/**
	 * The method to be invoked before running the assert statements, all the operation to be done before the actual test
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void invokeBODSJob() throws IOException, InterruptedException {
		String csvFilePath = directory + fileName ;
		Optional<Integer> oldLastModZipTs = null;
		System.out.println("userDir: "+ userDir);

		if( Files.exists( Paths.get(directory, fileName)) ) {
			//Create backup csv file	
			Files.copy(Paths.get(directory, fileName), Paths.get(directory, fileName + "_bkp"));
			Files.delete(Paths.get(directory, fileName));
			System.out.println("Backup file created :SAPR3toStockAPI.csv_bkp");
		} else {
			System.out.println( "old files not found" );
		}
		///hold the zip file - find out the latest zip file
		oldLastModZipTs = getLastModifiedZipFileTs();

		System.out.println("-----------> invoking BODS Job ");
		CommandRunner.runShellCommandPB( userDir.concat("/script/shell"), "/bin/sh invokeBodsJob.sh");
		//polling for the new csv file
		pollTheFile(csvFilePath);

		if( Files.exists(Paths.get(directory, fileName)) ) {
			System.out.println(fileName + " file found");
			//check if the new zip file is newer
			boolean newZipFound = true;
			Optional<Integer> newZipFileTs = getLastModifiedZipFileTs();
			System.out.println("newZipFileTs :"+ newZipFileTs.get());
			if(newZipFileTs.isPresent()) {
				if(oldLastModZipTs.isPresent() && newZipFileTs.get() == oldLastModZipTs.get()) {
					newZipFound = false;
				}
			}

			if( newZipFound == true ) {
				System.out.println("------------> Got a new zip file.");

				//sort the content of new csv file
				//CommandRunner.runShellCommand( userDir.concat("/script/shell"), "/bin/sh csvsort.sh --source "+ csvFilePath +" --dest "+ csvFilePath +"_Actual");
				//pollTheFile(csvFilePath +"_Actual");
				//CommandRunner.runShellCommandPB( userDir.concat("/script/shell"), "/bin/sh csvsort.sh --source "+ csvFilePath +"_Expected.0 --dest "+ csvFilePath +"_Expected");
				//pollTheFile(csvFilePath +"_Expected");
				ExpecetdCSVFile.sortData(csvFilePath, csvFilePath + "_Actual");
				ExpecetdCSVFile.sortData(csvFilePath + "_Expected.0", csvFilePath + "_Expected");
				Files.delete(Paths.get(directory, fileName + "_Expected.0"));
			} else {
				System.out.println("-------->New Zip file not found, So the process will discontinue here.");
			}
			Thread.sleep(40000);
		}
	}



	/**
	 * Method to identify the most recent zip file created by BODS job
	 * @return Optional Integer - depending on the existing old zip file in the directory
	 * @throws IOException
	 */
	private static Optional<Integer> getLastModifiedZipFileTs() throws IOException {

		Path dir = Paths.get(directory);
		Optional<Integer> lastFilePath = Files.list(dir)
				.filter(p -> p.toString().endsWith(".zip"))
				.map(p -> (int)p.toFile().lastModified())
				.max((t1, t2) -> (t1 - t2)) ;
		return lastFilePath;
	}

	/**
	 * Method to poll the target directory for the new csv file
	 * @param csvFilePath
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	private static void pollTheFile(String strFilePath) throws InterruptedException, IOException {
		boolean fileArrived = false;
		long pollingDuration = Long.parseLong( ConfigReader.getProperty("POLLING_DURATION_SECONDS") ) * 1000;
		long pollingInterval = Long.parseLong( ConfigReader.getProperty("POLLING_INTERVAL_SECONDS") ) * 1000;
		System.out.println("----------> Started Polling for the csv file "+strFilePath +" , with polling interval(in milliSeconds) ="+ pollingInterval);
		long endTimeSeconds= System.currentTimeMillis() + pollingDuration;
		Path filePath = Paths.get(strFilePath);
		long fileSize = 0L;
		while (System.currentTimeMillis() < endTimeSeconds) {
			System.out.println("checking for the file..");
			if(Files.exists(filePath)) {

//				long newFileSize = Files.size(filePath);
//				if( newFileSize == fileSize) {
//					fileArrived = true;
//					System.out.println("--------> File has arrived completely..");
//					break;
//				} else {
//					fileSize = newFileSize;
//				}
				if(isCompletelyWritten(strFilePath)){
					fileArrived = true;
					break;
				}
			}
			Thread.sleep(pollingInterval);
		}

		if (!fileArrived) {
			System.out.println("-----------> File did not arrive.");
			throw new AssertionError("Waiting for the file "+ strFilePath + " , but the file dod not arrive.");
//			System.exit(1);
		}
	}
	
	
	private static boolean isCompletelyWritten(String  filePath) {
	    RandomAccessFile stream = null;
	    try {
	        stream = new RandomAccessFile(filePath, "rw");
	        return true;
	    } catch (Exception e) {
	    	System.out.println("Skipping file " + filePath + " for this iteration due it's not completely written");
	    } finally {
	        if (stream != null) {
	            try {
	                stream.close();
	            } catch (IOException e) {
	            	System.out.println("Exception during closing file " + filePath);
	            }
	        }
	    }
	    return false;
	}

	/**
	 * Method to be called at start of each test case to delete the files created by previous test case
	 * @throws IOException
	 */
	public static void preJUnitCleanUp() throws IOException {
		if(Files.exists(Paths.get(directory, fileName + "_bkp"))) {
			Files.delete(Paths.get(directory, fileName + "_bkp"));
			System.out.println("----> Deleted existin bkp file");
		}
		if(Files.exists(Paths.get(directory, fileName))) {
			Files.copy(Paths.get(directory, fileName), Paths.get(directory, fileName + "_bkp"));
			Files.delete(Paths.get(directory, fileName));
			System.out.println("-------> Backup file created :"+fileName + "_bkp");
		}
		if(Files.exists(Paths.get(directory, fileName + "_Actual"))) {
			Files.delete(Paths.get(directory, fileName + "_Actual"));
		}
		if(Files.exists(Paths.get(directory, fileName + "_Expected"))) {
			Files.delete(Paths.get(directory, fileName + "_Expected"));
		}
		//		if(Files.exists(Paths.get(unprocessedLogPath))) {
		//			Files.delete(Paths.get(unprocessedLogPath));
		//		}
	}


	/**
	 * @param testName
	 * @param scenario
	 */
	public static void logWhatToTest(String testName, String scenario) {
		System.out.println("################ Test Case Name:"+testName+" , Scenario :- " + scenario);
	}


}
