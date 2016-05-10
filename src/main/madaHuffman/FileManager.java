package madaHuffman;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Tobias on 28.04.2016.
 */
public class FileManager {

    public String fileName;
    private char[] array;

    public FileManager(String fileName){
        this.fileName = fileName;
    }

    public char[] readFile(){

        StringBuilder sb = new StringBuilder();

        try(FileInputStream fileInput = new FileInputStream(fileName)){

            int r;
            while ((r = fileInput.read()) != -1) {
                sb.append((char)r);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        array = new char[sb.length()];
        sb.getChars(0, array.length, array, 0);

        return array;
    }

    public void decodeTableFile(HashMap<Character, String> huffmanTable)
    {
        StringBuilder sb = new StringBuilder();

        Iterator it = huffmanTable.entrySet().iterator();

        while(it.hasNext())
        {
            Map.Entry pair = (Map.Entry)it.next();

            char c = (char)pair.getKey();
            int ascii = (int)c;

            sb.append(ascii + ":" + pair.getValue() + "-");
        }

        sb.setLength(sb.length()-1);

        try(PrintWriter fileOutput = new PrintWriter(new FileOutputStream("dec_tab.txt")))
        {
            fileOutput.print(sb);
            fileOutput.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void generateOutput(HashMap<Character, String> huffmanTable)
    {
        StringBuilder sb = new StringBuilder();

        for(char c : array)
        {
            Iterator it = huffmanTable.entrySet().iterator();

            while(it.hasNext())
            {
                Map.Entry pair = (Map.Entry) it.next();
                char key = (char)pair.getKey();
                if(key == c)
                {
                    sb.append(huffmanTable.get(key));
                    break;
                }
            }
        }

        if(sb.length() % 8 != 0)
        {
            sb.append("1");

            while( sb.length() % 8 != 0)
            {
                sb.append("0");
            }
        }

      //  byte[] b = new byte[sb.length() / 8];
        byte[] b = new byte[sb.length()];
/*
        for(int i = 0; i < b.length; i++)
        {
            String sub = sb.substring(8*i, 8*i+8);
            b = sub.getBytes(StandardCharsets.UTF_8);
        }
*/
        b = sb.toString().getBytes(StandardCharsets.UTF_8);

        try (FileOutputStream fileOutput = new FileOutputStream("output.dat"))
        {
       //     for(int i = 0; i < b.length; i++)
         //   {
                fileOutput.write(b);
           // }
            fileOutput.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
