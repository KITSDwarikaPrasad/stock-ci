import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class ReplaceSample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String validBQCode = "25694956";
		Path path = Paths.get("C:/Users/prasad01/work/JUnitCI/ZBQSTOCK_LEVELS_MERGED.TXT");
		Path copyPath = Paths.get("C:/Users/prasad01/work/JUnitCI/ZBQSTOCK_LEVELS_MERGED_1.TXT");
		
		try {
			if(Files.notExists(copyPath)) {
				Files.createFile(copyPath);
			}
			long startTime = System.currentTimeMillis();
			Files.lines(path)
//			  .map(line-> new StringBuilder(line).replace(15, 23, validBQCode).toString()).parallel()
			.map(line -> line.substring(0, 15).concat(validBQCode).concat(line.substring(23))).parallel()

			  .forEach(line -> {
				try {
					Files.write(copyPath, line.concat("\n").getBytes(),StandardOpenOption.APPEND);
					System.out.print("#");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			
			System.out.println("Time taken1: "+ (System.currentTimeMillis() - startTime));
			
			startTime = System.currentTimeMillis();
			Files.lines(path)
			  .map(line-> new StringBuilder(line).replace(15, 23, validBQCode).toString()).parallel()
//			.map(line -> line.substring(0, 15).concat(validBQCode).concat(line.substring(23))).parallel()

			  .forEach(line -> {
				try {
					Files.write(copyPath, line.concat("\n").getBytes(),StandardOpenOption.APPEND);
					System.out.print("#");
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			System.out.println("Time taken2: "+ (System.currentTimeMillis() - startTime));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
