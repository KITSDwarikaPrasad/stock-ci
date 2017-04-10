package com.kfplc.ci.stock.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.swing.filechooser.FileFilter;

import com.kfplc.ci.stock.ConfigReader;

public class TestHelper {

	static String directory = ConfigReader.getProperty("TARGET_OUT_DIR");
	static String fileName = ConfigReader.getProperty("FILENAME");
	static String userDir = System.getProperty("user.dir");
	

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */

	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		
//		System.out.println("PATH:"+System.getenv().get("Path"));
//		String[] path = {"C:\\Users\\prasad01\\tools\\python\\WinPython-64bit-3.6.0.1\\scripts"};
		//	CommandRunner.runShellCommand("cmd /c dir", path, "C:\\Users\\prasad01\\tools\\");
//			Map<String, String> envVarMap = System.getenv();
//			System.out.println("envVarMap :"+ envVarMap);
			//for(Map.Entry<String, String> entry ; )
			//envVarMap.put("Path", envVarMap.get("Path").concat("C:\\Users\\prasad01\\tools\\python\\WinPython-64bit-3.6.0.1\\scripts"));
//		String command = "cmd /c dir /Q";
		//String[] commandArr = {"cmd","/c","dir","/Q"};	
//		String[] commandArr = command.split(" ");
//		List<String> list = new ArrayList<String>();
//		//Collections.addAll(list, Arrays.stream(ints).boxed().toArray(Integer[]::new));
//		
//			CommandRunner.runShellCommandPB( "C:\\Users\\prasad01\\", command );
//			//CommandRunner.runShellCommand("",  null);
//
		Path dir = Paths.get(directory);
//		ArrayList<String> paths = new ArrayList<String>();
		Optional<Path> lastFilePath = 
				Files.list(dir)
				 .filter(p -> Files.isDirectory(p) == false)
				 .filter(p -> p.toString().endsWith(".zip"))
				 .max((p1, p2) -> (int)(p1.toFile().lastModified() - p2.toFile().lastModified()) );
				// .sorted((Path p1, Path p2) -> p2.toFile().lastModified - p1.toFile().lastModified());
//				 .forEach( p -> {
//					 System.out.println(p.toUri());
//				 });
				 
		
		System.out.println(lastFilePath.get());
//				 .forEach( p -> paths.add(p.toUri().toString()));
		
//		File dir = new File(directory);
//		File theNewestFile = null;
//		if(!dir.exists()) System.out.println(dir + " Directory doesn't exists");
//		File[] files = dir.listFiles(new FilenameFilter() {
//			
//
//			@Override
//			public boolean accept(File file, String name) {
//				// TODO Auto-generated method stub
//				
//				return name.endsWith(".zip");
//			}
//			
//			
//		});
//		for (File file : files) {
//			System.out.println(file.getName());
//		}
//		if (files.length > 0) {
//	        /** The newest file comes first **/
//	        Arrays.sort(files, (f1, f2) -> (int)(f2.lastModified() - f1.lastModified()));
//	        theNewestFile = files[0];
//	    }
//		System.out.println("theNewestFile" + theNewestFile);
	}

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
		
//		CommandRunner.runShellCommandPB( userDir.concat("/script/shell"), "/bin/sh invokeBodsJob.sh");
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
			
//			System.out.println("newZipFileName :"+ newZipFileName);
//			boolean newZipFound = false;
//			if( null != newZipFileName && newZipFileName.length() != 0 ) {
//				if( null !=oldLastModZipFile && oldLastModZipFile.length() != 0 ) {
//					FileTime oldFileTs = (FileTime) Files.getAttribute(Paths.get(oldLastModZipFile), "creationTime");
//					FileTime newFileTs = (FileTime) Files.getAttribute(Paths.get(newZipFileName), "creationTime");
//					if( newFileTs.compareTo(oldFileTs)  > 0 ) { 
//						newZipFound = true;
//					}
//				} else {
//					newZipFound = true; //old file not  found
//				}
//			}


//			if( newZipFound == true ) {
				System.out.println("Got a new zip file.");
				//sort the content of new csv file
				System.out.println("starting sorting of csv file -- StartTime: "+new Date());
				//sh 'echo $(date +"%x %r %Z")'
				System.out.println( "sort -t \',\' "+ csvFilePath +" -o "+ csvFilePath +"_sorted" );
				
				CommandRunner.runShellCommand(null, "sort -t \',\' "+ csvFilePath +" -o "+ csvFilePath +"_sorted" );
				//sort the content of old csv file
				CommandRunner.runShellCommand(null, "sort -t \',\' "+ csvFilePath +"_bkp -o " +  csvFilePath + "_bkp_sorted" );
				System.out.println( "Sorting finished..-- EndTime: "+new Date() );
//			} else {
//				System.out.println("New Zip file not found, So the process will discontinue here.");
//				//throw new Exception("New Zip file not found");
//			}
		}
	}

	

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
	
	private static void cleanUpBuild() throws IOException {
		if(Files.exists(Paths.get(directory, fileName + ".csv_bkp"))) {
			Files.delete(Paths.get(directory, fileName + ".csv_bkp"));
		}
		if(Files.exists(Paths.get(directory, fileName + ".csv_sorted"))) {
			Files.delete(Paths.get(directory, fileName + ".csv_sorted"));
		}
		if(Files.exists(Paths.get(directory, fileName + ".csv_bkp_sorted"))) {
			Files.delete(Paths.get(directory, fileName + ".csv_bkp_sorted"));
		}
	}

}
