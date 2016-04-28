package madaHuffman;

/**
 * Created by Tobias on 28.04.2016.
 */
public class Main
{
    public static void main(String[] args) {
        FileManager fm = new FileManager("test.txt");
//        for(char c : fm.readFile())
//        {
//            System.out.println(c);
//        }

        Huffman huffman = new Huffman(fm.readFile());
    }
}
