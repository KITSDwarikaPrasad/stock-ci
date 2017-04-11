package com.kfplc.ci.stock;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Optional;

import com.kfplc.ci.stock.util.CommandRunner;
import com.kfplc.ci.stock.util.ConfigReader;

/**
 * The class with methods and flows to help in one test case
 * @author prasad01
 *
 */
public class TestHelper {

	static String directory = ConfigReader.getProperty("TARGET_OUT_DIR");
	static String fileName = ConfigReader.getProperty("FILENAME");
	static String userDir = System.getProperty("user.dir");

	/**
	 * The method to be invoked before running the assert statements, all the operation to be done before the actual test
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void preUnitTest() throws IOException, InterruptedException {
		String csvFilePath = directory + fileName + ".csv";
		Optional<Integer> oldLastModZipTs = null;
		System.out.println("userDir: "+ userDir);


		cleanUpBuild();

		if( Files.exists( Paths.get(directory, fileName + ".csv")) ) {
			//System.out.println(ConfigReader.getProperty("ACTUAL_CSV_FILE_PATH"));
			//Create backup csv file	
			Files.copy(Paths.get(directory, fileName + ".csv"), Paths.get(directory, fileName + ".csv_bkp"));
			Files.delete(Paths.get(directory, fileName + ".csv"));
			//CommandRunner.sh("mv $filePath $filePath" + "_bkp");
			System.out.println("Backup file created :SAPR3toStockAPI.csv_bkp");
		} else {
			System.out.println( "old files not found" );
		}
		///hold the zip file - find out the latest zip file
		//oldLastModZipFileName = CommandRunner.runShellCommand(null, "ls -Art "+ directory + fileName + "*.zip | head -n 1");
		oldLastModZipTs = getLastModifiedZipFile();
		if(oldLastModZipTs.isPresent()) {
			System.out.println("oldLastModZipFileTs :"+ oldLastModZipTs);
		}

		System.out.println("invoking BODS Job ");

		CommandRunner.runShellCommandPB( userDir.concat("/script/shell"), "/bin/sh invokeBodsJob.sh");
		//polling for the new csv file
		pollTheFile(csvFilePath);
		//		CommandRunner.runShellCommand("sh poll_the_file1.sh --path "+ directory +" --file "+ fileName +".csv --interval 60 --duration 1800");

		if( Files.exists(Paths.get(directory, fileName +".csv")) ) {
			System.out.println(fileName+".csv file found");
			//check if the new zip file is newer
			//			String newZipFileName = CommandRunner.runShellCommand(directory, "ls -Art "+ fileName + "*.zip | tail -n 1");
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
				//sh 'echo $(date +"%x %r %Z")'
				CommandRunner.runShellCommandPB( userDir.concat("/script/shell"), "/bin/sh csvsort.sh --source "+ csvFilePath +" --dest "+ csvFilePath +"_sorted -w "+ ConfigReader.getProperty("SORT_WAIT_TIME_SEC"));
				CommandRunner.runShellCommandPB( userDir.concat("/script/shell"), "/bin/sh csvsort.sh --source "+ csvFilePath +"_bkp --dest "+ csvFilePath +"_bkp_sorted -w "+ ConfigReader.getProperty("SORT_WAIT_TIME_SEC"));

				//				CommandRunner.runShellCommandPB(null, "/bin/sort -t',' "+ csvFilePath +" -o "+ csvFilePath +"_sorted" );
				//				//sort the content of old csv file
				//				CommandRunner.runShellCommand(null, "/bin/sort -tzz',' "+ csvFilePath +"_bkp -o " +  csvFilePath + "_bkp_sorted" );
				System.out.println( "Sorting finished..-- EndTime: "+new Date() );
			} else {
				System.out.println("New Zip file not found, So the process will discontinue here.");
				//throw new Exception("New Zip file not found");
			}
		}
	}



	/**
	 * Method to identify the most recent zip file created by BODS job
	 * @return Optional Integer - depending on the existing old zip file in the directory
	 * @throws IOException
	 */
	private static Optional<Integer> getLastModifiedZipFile() throws IOException {
		//		File dir = new File(directory);
		//		File[] files = dir.listFiles(FilenameFilter.accept());

		Path dir = Paths.get(directory);
		Optional<Integer> lastFilePath = Files.list(dir)
				// .filter(f -> Files.isDirectory(f) == false)
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
		long pollingDuration = Long.valueOf( ConfigReader.getProperty("POLLING_DURATION_SECONDS") ) * 1000;
		long pollingInterval = Long.valueOf( ConfigReader.getProperty("POLLING_INTERVAL_SECONDS") ) * 1000;
		System.out.println("Started Polling for the csv file with polling interval(in milliSeconds) ="+ pollingInterval);
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
	public static void cleanUpBuild() throws IOException {
		if(Files.exists(Paths.get(directory, fileName + ".csv_bkp"))) {
			if(Files.exists(Paths.get(directory, fileName + ".csv"))) {
				Files.delete(Paths.get(directory, fileName + ".csv"));
			} else {
				Files.copy(Paths.get(directory, fileName + ".csv_bkp"), Paths.get(directory, fileName + ".csv"));
			}
		}
		if(Files.exists(Paths.get(directory, fileName + ".csv_sorted"))) {
			Files.delete(Paths.get(directory, fileName + ".csv_sorted"));
		}
		if(Files.exists(Paths.get(directory, fileName + ".csv_bkp_sorted"))) {
			Files.delete(Paths.get(directory, fileName + ".csv_bkp_sorted"));
		}
	}

}
