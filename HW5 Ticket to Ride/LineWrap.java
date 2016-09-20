import java.util.Scanner;
import java.io.*;

public class LineWrap
{
    public static void main(String[] args)
    {
        Scanner in = null;
        try
        {
            in = new Scanner(new File("input.txt"));
        }
        catch(Exception e)
        {
            System.out.println("File not found");
        }
        int lineNum = 0;
        String line = "";
        while(in.hasNextLine())
        {
            lineNum++;
            line = in.nextLine();
            if(line.length() > 70)
            {
                System.out.println("Line " + lineNum + " is over 70 characters" + ": " + line);
            }
        }
   }
}