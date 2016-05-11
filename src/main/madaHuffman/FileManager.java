package madaHuffman;

import java.io.*;
import java.util.*;

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

        byte[] b = new byte[sb.length() / 8];

        try (FileOutputStream fileOutput = new FileOutputStream("output.dat"))
        {
            for(int i = 0; i < b.length; i++)
            {
                String sub = sb.substring(8*i, 8*i+8);
                fileOutput.write(Integer.parseInt(sub));
            }
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

    public void readByteFile() {
        File file = new File("output.dat");
        byte[] bFile = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(bFile);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        int decodeByte;
        for (int i=0; i<bFile.length; i++) {
            //sb.append(String.format("%8s", Integer.toBinaryString(bFile[i] % 0xFF)).replace(' ', '0'));
            decodeByte = bFile[i];
            sb.append(Integer.toBinaryString(decodeByte));
        }

        //remove the last zeros
        while (sb.substring(sb.length()-1).equals("0")) {
            sb.substring(0, sb.length() - 1);
        }
        //remove the last one
        if (sb.substring(sb.length()-1).equals("1")) {
            sb.substring(0, sb.length() - 1);
        }

        //Bitstring dekotieren

        try (FileOutputStream fileOutput = new FileOutputStream("decompress.txt")) {
            //fileOutput.write(sb);
            fileOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();

    }
}
