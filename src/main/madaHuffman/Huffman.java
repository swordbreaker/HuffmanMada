package madaHuffman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Tobias on 28.04.2016.
 */
public class Huffman {

    // char | count
    int[][] usedChars = new int[128][2];
    public char[] text;
    public Node rootNode;
    public HashMap<Character, String> huffmanTable = new HashMap<Character, String>();

    public Huffman(char[] text)
    {
        this.text = text;

        countChars();
        generateTree();
        generateTable(rootNode, "");
    }

    private void countChars(){
        int[] countCharsArray = new int[128];
        int diffrentChars = 0;
        for(int i = 0; i < text.length; i++){
            countCharsArray[(int)text[i]]++;
            if(countCharsArray[(int)text[i]] == 1){
                diffrentChars++;
            }
        }

        usedChars = new int[diffrentChars][2];

        int j = 0;
        for(int i = 0; i < countCharsArray.length; i++){
            if(countCharsArray[i] > 0){
                usedChars[j][0] = i;
                usedChars[j][1] = countCharsArray[i];
                j++;
            }
        }
    }

    private void generateTree(){
        Arrays.sort(usedChars, (o1, o2) -> o2[1] - o1[1]);

        ArrayList<Node> nodelist = new ArrayList<>();

        int i = usedChars.length - 1;
        while (i >= 0){

            Node centerNode = new Node();

            nodelist.sort((o1, o2) -> o1.weight - o2.weight);

            if(nodelist.size() > 0 && i == 0) {
                centerNode.leftNode = nodelist.get(0);
                nodelist.remove(0);
            }

            while (nodelist.size() > 0 && nodelist.get(0).weight <= usedChars[i][1] && centerNode.rightNode == null){
                if(centerNode.leftNode == null)
                    centerNode.leftNode = nodelist.get(0);
                else
                    centerNode.rightNode = nodelist.get(0);
                nodelist.remove(0);
            }

            if(centerNode.leftNode == null){
                Node leftNode = new Node((char)usedChars[i][0], usedChars[i][1]);
                centerNode.leftNode = leftNode;
                i--;
            }

            if(i >= 0 && centerNode.rightNode == null){
                Node rightNode = new Node((char)usedChars[i][0], usedChars[i][1]);
                centerNode.rightNode = rightNode;
                i--;
            }

            int leftWeight = centerNode.leftNode.weight;
            int rightWeight = (centerNode.rightNode != null ? centerNode.rightNode.weight : 0);
            centerNode.weight = leftWeight + rightWeight;
            nodelist.add(centerNode);
        }

        nodelist.sort((o1, o2) -> o2.weight - o1.weight);

        int j = nodelist.size() - 1;
        while (nodelist.size() > 1){
            Node centerNode = new Node();
            centerNode.leftNode = nodelist.get(j);
            nodelist.remove(j);
            j--;
            centerNode.rightNode = nodelist.get(j);
            nodelist.remove(j);
            int leftWeight = centerNode.leftNode.weight;
            int rightWeight = (centerNode.rightNode != null ? centerNode.rightNode.weight : 0);
            centerNode.weight = leftWeight + rightWeight;
            nodelist.add(centerNode);
        }
      //  System.out.println(nodelist.get(nodelist.size() - 1));

        rootNode = nodelist.get(nodelist.size() - 1);
    }

    private void generateTable(Node node, String huffCode)
    {
        if(node.leftNode != null)
        {
            huffCode += "0";
            generateTable(node.leftNode, huffCode);
            huffCode = huffCode.substring(0, huffCode.length() -1);
        }

        if(node.rightNode != null)
        {
            huffCode += "1";
            generateTable(node.rightNode, huffCode);
            huffCode = huffCode.substring(0, huffCode.length() -1);
        }

        if(node.content !='\u0000')
        {
            huffmanTable.put(node.content, huffCode);
        }
    }
}
