package com.kfplc.ci.stock.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.Date;

import com.kfplc.ci.stock.ConfigReader;

public class TestHelper {

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void preUnitTest() throws IOException, InterruptedException {
		String directory = ConfigReader.getProperty("TARGET_OUT_DIR");
		String fileName = ConfigReader.getProperty("FILENAME");
		String userDir = System.getProperty("user.dir");
		String csvFilePath = directory + fileName + ".csv";
		String oldLastModZipFileName = null;
		System.out.println("userDir: "+ userDir);
		
		if( Files.exists( Paths.get(directory, fileName + ".csv")) ) {
			///hold the zip file - find out the latest zip file
			//System.out.println(ConfigReader.getProperty("ACTUAL_CSV_FILE_PATH"));
			oldLastModZipFileName = CommandRunner.runShellCommand("ls -Art "+directory + fileName + "*.zip | head -n 1");
			System.out.println("oldLastModZipFileName :"+ oldLastModZipFileName);
			//Create backup csv file	
			Files.copy(Paths.get(directory, fileName + ".csv"), Paths.get(directory, fileName + ".csv_bkp"));
			Files.delete(Paths.get(directory, fileName + ".csv"));
			//CommandRunner.sh("mv $filePath $filePath" + "_bkp");
			System.out.println("Backup file created :SAPR3toStockAPI.csv_bkp");
		} else {
			System.out.println( "old files not found" );
		}
		

		System.out.println("invoking BODS Job -- dummy");

		//polling for the new csv file
		 pollTheFile(csvFilePath);
//		CommandRunner.runShellCommand("tr '\\r' '\\n' < script/poll_the_file.sh > script/poll_the_file1.sh");
//		CommandRunner.runShellCommand("sh poll_the_file1.sh --path "+ directory +" --file "+ fileName +".csv --interval 60 --duration 1800");

		if( Files.exists(Paths.get(directory, fileName +".csv")) ) {
			System.out.println(fileName+".csv file found");
			//check if the new zip file is newer
			String newZipFileName = CommandRunner.runShellCommand("ls -Art "+directory + fileName + "*.zip | tail -n 1");
			System.out.println("newZipFileName :"+ newZipFileName);
			boolean newZipFound = false;
			if( null != newZipFileName && newZipFileName.length() != 0 ) {
				if( null !=oldLastModZipFileName && oldLastModZipFileName.length() != 0 ) {
					FileTime oldFileTs = (FileTime) Files.getAttribute(Paths.get(oldLastModZipFileName), "creationTime");
					FileTime newFileTs = (FileTime) Files.getAttribute(Paths.get(newZipFileName), "creationTime");
					if( newFileTs.compareTo(oldFileTs)  > 0 ) { 
						newZipFound = true;
					}
				} else {
					newZipFound = true; //old file not  found
				}
			}


			if( newZipFound == true ) {
				System.out.println("Got a new zip file.");
				//sort the content of new csv file
				System.out.println("starting sorting of csv file -- StartTime: "+new Date());
				//sh 'echo $(date +"%x %r %Z")'
				CommandRunner.runShellCommand( "sort -t \',\' "+ csvFilePath +" -o "+ csvFilePath +"_sorted" );
				//sort the content of old csv file
				CommandRunner.runShellCommand( "sort -t \',\' "+ csvFilePath +"_bkp -o " +  csvFilePath + "_bkp_sorted" );
				System.out.println( "Sorting finished..-- EndTime: "+new Date() );
			} else {
				System.out.println("New Zip file not found, So the process will discontinue here.");
				//throw new Exception("New Zip file not found");
			}
		}
	}

	private static void pollTheFile(String csvFilePath) throws InterruptedException {
		boolean fileArrived = false;
		long pollingDuration = Long.valueOf( ConfigReader.getProperty("POLLING_DURATION_SECONDS") ) * 1000;
		long pollingInterval = Long.valueOf( ConfigReader.getProperty("POLLING_INTERVAL_SECONDS") ) * 1000;
		System.out.println("Started Polling for the csv file with polling interval(in Seconds) ="+ pollingInterval);
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

}
