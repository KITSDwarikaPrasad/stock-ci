
//from  w ww . j a  v a2  s  .  c o  m

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class AppendSample {

    public static void main(String[] args) {

        Path wiki_path = Paths.get("C:/Users/prasad01/tmp", "ExecutionTimesLog.txt");

        Charset charset = Charset.forName("UTF-8");
        String text = "2\n";
        try (BufferedWriter writer = Files.newBufferedWriter(wiki_path, charset, StandardOpenOption.APPEND)) {
            writer.write(text);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}