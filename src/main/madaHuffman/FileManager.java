package madaHuffman;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Tobias on 28.04.2016.
 */
public class FileManager {

    public String fileName;

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

        char[] array = new char[sb.length()];
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

    }
}
