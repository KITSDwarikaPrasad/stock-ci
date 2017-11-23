import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class ReplaceSample {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String validBQCode = "25694956";
		Path path = Paths.get("C:/Users/prasad01/work/JUnitCI/ZBQSTOCK_LEVELS_MERGED.TXT");
		Path copyPath = Paths.get("C:/Users/prasad01/work/JUnitCI/ZBQSTOCK_LEVELS_MERGED_1.TXT");
		
			if(Files.notExists(copyPath)) {
				Files.createFile(copyPath);
			}
			long startTime = System.currentTimeMillis();
//			Files.lines(path)
////			  .map(line-> new StringBuilder(line).replace(15, 23, validBQCode).toString()).parallel()
//			.map(line -> line.substring(0, 15).concat(validBQCode).concat(line.substring(23))).parallel()
//
//			  .forEach(line -> {
//				try {
//					Files.write(copyPath, line.concat("\n").getBytes(),StandardOpenOption.APPEND);
//					System.out.print("#");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			});
//			
//			System.out.println("Time taken1: "+ (System.currentTimeMillis() - startTime));
//			
//			startTime = System.currentTimeMillis();
//			Files.lines(path)
//			  .map(line-> new StringBuilder(line).replace(15, 23, validBQCode).toString()).parallel()
////			.map(line -> line.substring(0, 15).concat(validBQCode).concat(line.substring(23))).parallel()
//
//			  .forEach(line -> {
//				try {
//					Files.write(copyPath, line.concat("\n").getBytes(),StandardOpenOption.APPEND);
//					System.out.print("#");
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			});
//			System.out.println("Time taken2: "+ (System.currentTimeMillis() - startTime));
//			BufferedReader reader = Files.newBufferedReader(path);
			try(BufferedWriter writer = Files.newBufferedWriter(copyPath,  Charset.forName("UTF-8"), StandardOpenOption.APPEND);
					BufferedReader reader = Files.newBufferedReader(path) ) {
				int lineCount = 0;
				StringBuilder chunk = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					lineCount++;
					chunk = chunk.append(line).replace(15, 23, validBQCode).append("\n");
					if(lineCount == 2) {
						lineCount = 0;
						writer.write(chunk.toString());
						chunk = new StringBuilder();
						System.out.print(".");
						continue;
					}
					writer.write(chunk.toString());
				}
				System.out.println("Time taken1: "+ (System.currentTimeMillis() - startTime));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
