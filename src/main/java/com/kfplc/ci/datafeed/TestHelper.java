package com.kfplc.ci.datafeed;

import java.io.IOException;
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

	/**
	 * The method to be invoked before running the assert statements, all the operation to be done before the actual test
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void preUnitTest() throws IOException, InterruptedException {
		String csvFilePath = directory + fileName ;
		Optional<Integer> oldLastModZipTs = null;
		System.out.println("userDir: "+ userDir);

		preJUnitCleanUp();

		if( Files.exists( Paths.get(directory, fileName + ".csv")) ) {
			//Create backup csv file	
			Files.copy(Paths.get(directory, fileName + ".csv"), Paths.get(directory, fileName + ".csv_bkp"));
			Files.delete(Paths.get(directory, fileName + ".csv"));
			System.out.println("Backup file created :SAPR3toStockAPI.csv_bkp");
		} else {
			System.out.println( "old files not found" );
		}
		///hold the zip file - find out the latest zip file
		oldLastModZipTs = getLastModifiedZipFile();
		if(oldLastModZipTs.isPresent()) {
			System.out.println("oldLastModZipFileTs :"+ oldLastModZipTs);
		}

		System.out.println("invoking BODS Job ");

		CommandRunner.runShellCommandPB( userDir.concat("/script/shell"), "/bin/sh invokeBodsJob.sh");
		//polling for the new csv file
		pollTheFile(csvFilePath);

		if( Files.exists(Paths.get(directory, fileName +".csv")) ) {
			System.out.println(fileName+".csv file found");
			//check if the new zip file is newer
			boolean newZipFound = true;
			Optional<Integer> newZipFileTs = getLastModifiedZipFile();
			System.out.println("newZipFile :"+ newZipFileTs.get());
			if(newZipFileTs.isPresent()) {
				if(oldLastModZipTs.isPresent() && newZipFileTs.get() == oldLastModZipTs.get()) {
					newZipFound = false;
				}
			}

			if( newZipFound == true ) {
				System.out.println("Got a new zip file.");

				//sort the content of new csv file
				System.out.println("starting sorting of csv file -- StartTime: "+new Date());
				CommandRunner.runShellCommandPB( userDir.concat("/script/shell"), "/bin/sh csvsort.sh --source "+ csvFilePath +" --dest "+ csvFilePath +"_Actual");
				CommandRunner.runShellCommandPB( userDir.concat("/script/shell"), "/bin/sh csvsort.sh --source "+ csvFilePath +"_Expected");
				pollTheFile(csvFilePath +"_Actual");
				pollTheFile(csvFilePath +"_Expected");
				System.out.println( "Sorting finished..-- EndTime: "+new Date() );
			} else {
				System.out.println("New Zip file not found, So the process will discontinue here.");
			}
		}
	}



	/**
	 * Method to identify the most recent zip file created by BODS job
	 * @return Optional Integer - depending on the existing old zip file in the directory
	 * @throws IOException
	 */
	private static Optional<Integer> getLastModifiedZipFile() throws IOException {

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
	 */
	private static void pollTheFile(String csvFilePath) throws InterruptedException {
		boolean fileArrived = false;
		long pollingDuration = Long.parseLong( ConfigReader.getProperty("POLLING_DURATION_SECONDS") ) * 1000;
		long pollingInterval = Long.parseLong( ConfigReader.getProperty("POLLING_INTERVAL_SECONDS") ) * 1000;
		System.out.println("Started Polling for the csv file "+csvFilePath +" , with polling interval(in milliSeconds) ="+ pollingInterval);
		long endTimeSeconds= System.currentTimeMillis() + pollingDuration;
		while (System.currentTimeMillis() < endTimeSeconds) {
			System.out.println("checking for the file..");
			if (Files.exists(Paths.get(csvFilePath))) {
				System.out.println("File has arrived..");
				fileArrived = true;
				break;
			}
			Thread.sleep(pollingInterval);
		}

		if (!fileArrived) {
			System.out.println("File did not arrive.");
			System.exit(1);
		}
	}

	/**
	 * Method to be called at start of each test case to delete the files created by previous test case
	 * @throws IOException
	 */
	public static void preJUnitCleanUp() throws IOException {
		if(Files.exists(Paths.get(directory, fileName + "_bkp"))) {
			Files.delete(Paths.get(directory, fileName + "_bkp"));
		}
		if(Files.exists(Paths.get(directory, fileName))) {
			if(Files.exists(Paths.get(directory, fileName + "_bkp"))) {
				Files.delete(Paths.get(directory, fileName + "_bkp"));
			} 
			Files.copy(Paths.get(directory, fileName), Paths.get(directory, fileName + "_bkp"));
		}
		if(Files.exists(Paths.get(directory, fileName + "_Actual"))) {
			Files.delete(Paths.get(directory, fileName + "_Actual"));
		}
		if(Files.exists(Paths.get(directory, fileName + "_Expected"))) {
			Files.delete(Paths.get(directory, fileName + "_Expected"));
		}
	}

}