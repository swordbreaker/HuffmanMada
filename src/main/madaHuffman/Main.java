package madaHuffman;

/**
 * Created by Tobias on 28.04.2016.
 */
public class Main
{
    public static void main(String[] args)
    {
        FileManager fm = new FileManager("test.txt");

        Huffman huffman = new Huffman(fm.readFile());
        fm.decodeTableFile(huffman.huffmanTable);
        fm.generateOutput(huffman.huffmanTable);
    }
}
