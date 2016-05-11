package madaHuffman;

import java.io.*;
import java.util.*;

/**
 * Created by Tobias on 28.04.2016.
 * Developers: Janis Angst, Tobias Bollinger, Tom Ohme
 */
public class FileManager {
    private String inputFile;
    private String outputFile;
    private String decTabFile;
    private char[] array;

    /**
     * Constructor
     * @param fileName
     * @param outputFile
     * @param decTabFile
     */
    public FileManager(String fileName, String outputFile, String decTabFile) {
        this.inputFile = fileName;
        this.outputFile = outputFile;
        this.decTabFile = decTabFile;
    }

    /**
     * Read the text from the inputfile
     * @return content of file as char-array
     */
    public char[] readFile() {

        StringBuilder sb = new StringBuilder();

        try (FileInputStream fileInput = new FileInputStream(inputFile)) {
            int r;
            while ((r = fileInput.read()) != -1) {
                sb.append((char) r);
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

    /**
     * Create huffman table
     * @param huffmanTable
     */
    public void decodeTableFile(HashMap<Character, String> huffmanTable) {
        StringBuilder sb = new StringBuilder();

        Iterator it = huffmanTable.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            char c = (char) pair.getKey();
            int ascii = (int) c;

            sb.append(ascii + ":" + pair.getValue() + "-");
        }

        sb.setLength(sb.length() - 1);

        try (PrintWriter fileOutput = new PrintWriter(new FileOutputStream(this.decTabFile))) {
            fileOutput.print(sb);
            fileOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Encode the text and generate the file with the bytecode
     * @param huffmanTable
     */
    public void generateOutput(HashMap<Character, String> huffmanTable) {
        StringBuilder sb = new StringBuilder();

        for (char c : array) {
            Iterator it = huffmanTable.entrySet().iterator();

            // Search the character (key) in the HashMap and append the value of the key to the String
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                char key = (char) pair.getKey();
                if (key == c) {
                    sb.append(huffmanTable.get(key));
                    break;
                }
            }
        }

        // Add a 1 if the lenght of the String isnt modulo 8
        if (sb.length() % 8 != 0) {
            sb.append("1");

            // Add as many 0 until the String length is modulo 8
            while (sb.length() % 8 != 0) {
                sb.append("0");
            }
        }

        // Generate the byte from 8 characters
        try (FileOutputStream fileOutput = new FileOutputStream(this.outputFile)) {
            for (int i = 0; i < sb.length() / 8; i++) {
                String sub = sb.substring(8 * i, 8 * i + 8);
                int value = Integer.parseInt(sub, 2);
                fileOutput.write(value);
            }
            fileOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read the bytefile and return the encoded text
     * @return encoded text
     */
    public String readByteFile() {
        File file = new File(this.outputFile);
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
        // Transform the bytecode to the encoded text
        for (int i = 0; i < bFile.length; i++) {
            decodeByte = bFile[i];
            // Force leading zeros if smaller than 8
            String code = String.format("%8s", Integer.toBinaryString(decodeByte % 0xFF)).replace(' ', '0');
            // Get only the last 8 character. Needed if MSB is 1
            sb.append(code.substring(code.length() - 8));
        }

        // Remove the zeros from the tail
        while (sb.substring(sb.length() - 1).equals("0")) {
            sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        }

        // Remove the last one
        if (sb.substring(sb.length() - 1).equals("1")) {
            sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        }

        return sb.toString();
    }

    /**
     * decode the encoded text and save it into a file
     * @param codedText
     */
    public void decodeBitString(String codedText) {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fileInput = new FileInputStream(this.decTabFile)) {
            int r;
            while ((r = fileInput.read()) != -1) {
                sb.append((char) r);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] huffmanCode = sb.toString().split("-");
        // keyValue[0] = ascii code, keyValue[1] = Huffman code
        String[] keyValue;

        HashMap<Character, String> huffmanTable = new HashMap<>();
        for (int i = 0; i < huffmanCode.length; i++) {
            keyValue = huffmanCode[i].split(":");
            int ascii = Integer.parseInt(keyValue[0]);
            huffmanTable.put((char) ascii, keyValue[1]);
        }

        StringBuilder decodedText = new StringBuilder();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < codedText.length(); i++) {
            // Create huffman code
            code.append(codedText.charAt(i));
            Iterator it = huffmanTable.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                // Check if the created huffman code is in the huffman table
                if (pair.getValue().toString().equals(code.toString())) {
                    decodedText.append(pair.getKey());
                    code = new StringBuilder();
                }
            }
        }

        try (PrintWriter fileOutput = new PrintWriter(new FileOutputStream("decompress.txt"))) {
            fileOutput.print(decodedText);
            fileOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
