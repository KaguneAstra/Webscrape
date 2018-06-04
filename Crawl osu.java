import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class WebCrawler {
    public String CURRENTID;
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
    
    public static void main(String[] args) {
        String beatmaps = download("https://osu.ppy.sh/beatmapsets");
        crawlOsu(beatmaps);
               
    }
    public static void crawlOsu(String html)
    {
        String header = "<script id=\"json-beatmaps\" type=\"application/json\">";
        int start = html.indexOf(header);
        int end = html.indexOf("</script>", start);
        String info = html.substring(start + header.length(), end);
        String[] beatmaps = info.split("title");
        int counter = 1;
        for(String i: beatmaps)
        {
            if(i.length()>20)
            {
                System.out.println(counter + ". " + manipulate(i));
                counter++;
            }
        }
        System.out.println("\n\n\n\nTo find out more about the standard difficulties in a mapset, type in a corresponding number");
        Scanner in = new Scanner(System.in);
        while(in.hasNext())
        {
            String i = in.next();
            if(i.equals("exit"))
            {
                break;
            }
            else if(Integer.valueOf(i)>=1&&Integer.valueOf(i)<51)
            {
                int temp = Integer.valueOf(i);
                System.out.println("\n\n ___________________________________");
                System.out.println(manipulate(beatmaps[temp]));
                System.out.println(sourceCreator(beatmaps[temp]));            
                System.out.println(moreInfo(beatmaps, temp));
            }
            else
            {
                System.out.println("Please input a valid number (1-50) \n\n\n\n ___________________________________");
            }
        }       
                
    }
    public static List<String> difficulties(String html)//the java script
    {
        String head = "\"title\":\"";
        String tail = "\"converts\":";
        String temp = html.substring(html.indexOf(head)+head.length(), html.indexOf(tail));
        String[] diff = temp.split("\"difficulty_rating\"");
        List<String> toReturn = new ArrayList();
        List<String> other = new ArrayList();
        for(int i=1;i<diff.length;i++)//skip one because nothing needed before first appearance of difficulty
        {
            temp = "Stars: " + get(":", ",\"passcount\":", diff[i]).replace("\"", " ");
            temp = temp.replace("version", "Difficulty name");
            toReturn.add(temp.replace("_", " "));
        }
        java.util.Collections.sort(toReturn);
        return toReturn;
    }
    
    public static String get(String head, String tail, String html)
    {
        return html.substring(html.indexOf(head)+head.length(), html.indexOf(tail));
    }
    
    public static String moreInfo(String[] beatmaps, int index)
    {
        String html = beatmaps[index];
        String info = download(getURL(html));
        int num = numberOfBeatmaps(info);
        List<String> difficulties = difficulties(info);
        String diffNum = "";
        if(num!=1)
            diffNum += "There are " + String.valueOf(num) + " standard difficulties in this mapset.";
        else
            diffNum += "There is " + String.valueOf(num) + " standard difficulty in this mapset.";
        for(int i=0;i<difficulties.size();i++)
        {
            diffNum += "\n" + difficulties.get(i);
        }
        return diffNum;
        
    }
    public static String sourceCreator(String html)
    {
        String start = "\"creator\":\"";
        String end = "\"covers\"";
        String temp = get(start, end, html);
        String creator = "Map by: " + temp.substring(0, temp.indexOf("\""));
        start = "\"bpm\":";
        end = ",\"source\"";
        String bpm = get(start, end, temp);
        start = "\"source\":\"";
        int f = temp.indexOf(start);
        String source = temp.substring(temp.indexOf(start)+start.length(), temp.indexOf("\"", f + start.length()));
        source = fixUnicode(source);
        if(source.length()<1)
        {
            source = "There is no source.";
        }
        else
        {
            source = "The source is: " + source;
        }
        return creator + "\n" + source + "\n" + bpm;
    }
    public static String fixUnicode(String uni)
    {
        int foundAt = uni.indexOf("\\u");
        while(foundAt >= 0)
        {
            String unicode = uni.substring(foundAt+2, foundAt+6);            
            String s = Character.toString((char)(Integer.parseInt(unicode, 16)));
            uni = uni.substring(0, foundAt) + s + uni.substring(foundAt+6);
            foundAt = uni.indexOf("\\u", foundAt+1);
        }
        return uni;
        
    }
    public static String manipulate(String info)
    //takes main beatmap page individually split html
    {
        String[] bits = info.split("\".{1,2}\"");
        String title = "The song name is: " + bits[1];
        String artist = " by " + bits[3];
        String coverID = ":{\"cover\":\"";
        int start = info.indexOf(coverID);
        String url  = info.substring(start + coverID.length(), info.indexOf("\",\"", start)); 
        url = url.substring(0, url.indexOf("\\/covers"));
        url = url.replace("assets", "osu");
        url = url.replace("beatmaps", "beatmapsets");
        url = url.replace("\\", "");
        return title + artist + "\nLink: " + url;
    }
    public static String getURL(String info)
    //takes main beatmap page split html
    {
        String coverID = ":{\"cover\":\"";
        int start = info.indexOf(coverID);
        String temp  = info.substring(start + coverID.length(), info.indexOf("\",\"", start)); 
        temp = temp.substring(0, temp.indexOf("\\/covers"));
        temp = temp.replace("assets", "osu");
        temp = temp.replace("beatmaps", "beatmapsets");
        temp = temp.replace("\\", "");     
        return temp;
    }
    
    public static int numberOfBeatmaps(String html)
    //takes the actual beatmap page
    {
        String regex = "\"mode\":\"osu\"";
        String[] temp = html.split(regex);
        return temp.length -1;
        // minus one is to account for the first bit at the beginning.
    }
}
