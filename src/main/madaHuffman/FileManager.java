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



        try(PrintWriter fileOutput = new PrintWriter(new FileOutputStream("encText.txt")))
        {
            fileOutput.print(sb);
            fileOutput.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }



        try (FileOutputStream fileOutput = new FileOutputStream("output.dat"))
        {
            for(int i = 0; i < sb.length() / 8; i++)
            {
                String sub = sb.substring(8*i, 8*i+8);
                int value = Integer.parseInt(sub, 2);


                fileOutput.write(value);
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

    public String readByteFile() {
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
        for (int i = 0; i < bFile.length; i++) {
            decodeByte = bFile[i];
            String code = String.format("%8s", Integer.toBinaryString(decodeByte % 0xFF)).replace(' ', '0');
            sb.append(code.substring(code.length() - 8));
        }

        //remove the last zeros
        while (sb.substring(sb.length() - 1).equals("0")) {
            sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        }
        //remove the last one
        if (sb.substring(sb.length() - 1).equals("1")) {
            sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        }

        return sb.toString();
    }

    public void decodeBitString(String codedText) {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fileInput = new FileInputStream("dec_tab.txt")){
            int r;
            while ((r = fileInput.read()) != -1) {
                sb.append((char)r);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] huffmanCode = sb.toString().split("-");
        String[] keyValue;
        int longest = 0;

        HashMap<Character, String> huffmanTable = new HashMap<>();
        for (int i=0; i<huffmanCode.length; i++) {
            keyValue = huffmanCode[i].split(":");
            int ascii = Integer.parseInt(keyValue[0]);
            huffmanTable.put((char)ascii, keyValue[1]);

            if(longest < keyValue[1].length()) {
                longest = keyValue[1].length();
            }
        }

        StringBuilder decodedText = new StringBuilder();
        Iterator it = huffmanTable.entrySet().iterator();
        while(longest != 0) {
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if(pair.getValue().toString().length() == longest) {
                    codedText = codedText.replace(pair.getValue().toString(), pair.getKey().toString());
                    decodedText = new StringBuilder(codedText);
                }
            }
            longest--;
        }

        try (PrintWriter fileOutput = new PrintWriter(new FileOutputStream("decompress.txt"))) {
            //fileOutput.print(decodedText);
            fileOutput.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
