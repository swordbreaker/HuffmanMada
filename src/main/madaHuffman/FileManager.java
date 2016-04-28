package madaHuffman;

import java.io.*;

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

}
