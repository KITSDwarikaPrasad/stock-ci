package com.kfplc.ci.datafeed.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
//java.nio.charset.Charset;
//import java.nio.file.StandardOpenOption;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LargeInputToSmall {

	static int  count = 0;
	static Path path = Paths.get("C:\\Users\\prasad01\\work\\JUnitCI\\ZBQSTOCK_LEVELS_MERGED.TXT");
	static Path bkpPath = Paths.get("C:\\Users\\prasad01\\work\\JUnitCI\\ZBQSTOCK_LEVELS_MERGED.TXT"+ "BKP");
	public static void main(String[] args) throws IOException {
	//	Files.move(path, bkpPath);
		
		
		Stream<String> linesStream = Files.lines(path);
		BufferedWriter writer = Files.newBufferedWriter(bkpPath);
		
		linesStream.limit(1000l)
		.forEach(line -> {
				try {
					writer.write(line + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		});
		
//		linesStream.filter(line -> ++count  >= 1000).collect(Collectors.joining("\n"));
//		
//		Files.write(bkpPath, (Iterable<String>)linesStream::iterator);
//		linesStream.close();			
//		//System.out.println("Creating input File : " + path );
//		try (BufferedReader buffer = Files.newBufferedReader(path)) {
//             buffer.lines().filter(line -> checkCount()).collect(Collectors.joining("\n"));
////			buffer.lines().f
//        }
//		try(BufferedWriter writer = Files.newBufferedWriter(path)) {
////			writer.write(arg0);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	private static void writeToFile(String line, BufferedWriter writer) throws IOException {
		Files.createFile(bkpPath);
//		try(BufferedWriter writer = Files.newBufferedWriter(bkpPath,Charset.defaultCharset() ,StandardOpenOption.WRITE)) {
//		try( = ) {
		writer.write(line + "\n\r");
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	}

	private static boolean checkCount() {
		return ++count  >= 1000;
	}

}
