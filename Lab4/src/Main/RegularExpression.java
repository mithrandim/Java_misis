package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegularExpression
{
    private static Pattern clientVersion = Pattern.compile(".+(\\d\\.\\d\\.\\d{3})\\s\\[\\d{4}\\]");
    private static Pattern enterTimeName = Pattern.compile("(\\d{4})\\/(\\d{2})\\/(\\d{2}) (\\d{2}\\:\\d{2}\\:\\d{2}).+:\\s(\\S+)@.+");
    private static Pattern serverVer = Pattern.compile(".+agent ver:(\\d.\\d.\\d{3})");
    private static Matcher matcher;

    public static String clientVersion(String line)
    {
        StringBuilder result = new StringBuilder();
        matcher = clientVersion.matcher(line);
        if (matcher.matches())
        {
            for (int i=1; i<=matcher.groupCount(); ++i)
            {
                result.append(matcher.group(i));
            }
        }
        return result.toString();
    }

    public static String enterTimeName(String line)
    {
        StringBuilder result = new StringBuilder();
        matcher = enterTimeName.matcher(line);
        if (matcher.matches())
        {
            for (int i=1; i<=matcher.groupCount(); ++i)
            {
                result.append(matcher.group(i));
                result.append("\t");
            }
        }
        return result.toString();
    }

    public static String serverVer(String line)
    {
        StringBuilder result = new StringBuilder();
        matcher = serverVer.matcher(line);
        if (matcher.matches())
        {
            for (int i=1; i<=matcher.groupCount(); ++i)
            {
                result.append(matcher.group(i));
            }
        }
        return result.toString();
    }

}

class Main
{
    public static void main(String args[]) throws Exception
    {
        BufferedReader br = new BufferedReader( new FileReader( "sample.log" ));
        String line;
        while ((line = br.readLine()) != null)
        {
            if(!RegularExpression.clientVersion(line).isEmpty())
                System.out.println("Client version: " + RegularExpression.clientVersion(line));
            else if (!RegularExpression.serverVer(line).isEmpty())
                System.out.println("Server version: " + RegularExpression.serverVer(line));
            else if (!RegularExpression.enterTimeName(line).isEmpty())
                System.out.println("Enter time, date and username: " + RegularExpression.enterTimeName(line));
            else
                continue;

        }
    }
}
