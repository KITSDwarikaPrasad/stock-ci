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
		
	}
}
