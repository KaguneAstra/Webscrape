package WebCrawler;


import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

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
      //String url = "";
      //String html = download(url);
      String picLink = "https://mangadex.org/images/manga/21944.jpg?1524581221";
        

      try
      {
            URL url = new URL(picLink);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream("\\\\SD36\\JOHN\\StudentHome\\Kelvin.lai\\Desktop\\try");
            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
            }

            is.close();
            os.close();
      }
      catch(IOException e)
      {
          System.out.println(e);
      }
    }
}
