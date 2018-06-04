import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

/**
 *
 * @author joyce_c
 */
public class Manga{
  public static String download(String website)
    {
        String result = "";
        try {
            URL url = new URL(website);
            InputStream is = url.openStream();  // throws an IOException
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            while (line != null) {
                result += line + "\n";
                line = br.readLine();
            }
        } catch (IOException ioe) {
            result = ioe.toString();
        }
        
        return result;
    }
  public static void main(String[] args){
      String url = "";
      String html = download(url);
    }
}