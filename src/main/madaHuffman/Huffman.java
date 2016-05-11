package madaHuffman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Tobias on 28.04.2016.
 * Developers: Janis Angst, Tobias Bollinger, Tom Ohme
 */
public class Huffman {
    // usedChar [ascii][char | count]
    int[][] usedChars = new int[128][2];
    public char[] text;
    public Node rootNode;
    public HashMap<Character, String> huffmanTable = new HashMap<Character, String>();

    /**
     * Constructor
     * @param text
     */
    public Huffman(char[] text) {
        this.text = text;

        countChars();
        generateTree();
        generateTable(rootNode, "");
    }

    /**
     * Count how often a Char appeared in the text
     */
    private void countChars() {
        int[] countCharsArray = new int[128];
        int diffrentChars = 0;

        //Count chars
        for (int i = 0; i < text.length; i++) {
            countCharsArray[(int) text[i]]++;
            if (countCharsArray[(int) text[i]] == 1) {
                diffrentChars++;
            }
        }

        usedChars = new int[diffrentChars][2];

        //Save the counted chars in a 2D array
        int j = 0;
        for (int i = 0; i < countCharsArray.length; i++) {
            if (countCharsArray[i] > 0) {
                usedChars[j][0] = i;
                usedChars[j][1] = countCharsArray[i];
                j++;
            }
        }
    }

    /**
     * Generate the huffman tree
     */
    private void generateTree() {
        Arrays.sort(usedChars, (o1, o2) -> o2[1] - o1[1]);
        ArrayList<Node> nodelist = new ArrayList<>();

        int i = usedChars.length - 1;
        while (i >= 0) {
            //Generate a Center Node to pair Nodes
            Node centerNode = new Node();

            //Sort the saved node by weight
            nodelist.sort((o1, o2) -> o1.weight - o2.weight);

            //When we are at the last char and we have node which are not connected to the Tree connect it to the left side
            if (nodelist.size() > 0 && i == 0) {
                centerNode.leftNode = nodelist.get(0);
                nodelist.remove(0);
            }

            //connect unconnected Nodes when their weight is less or equal than the actual char
            while (nodelist.size() > 0 && nodelist.get(0).weight <= usedChars[i][1] && centerNode.rightNode == null) {
                if (centerNode.leftNode == null)
                    centerNode.leftNode = nodelist.get(0);
                else
                    centerNode.rightNode = nodelist.get(0);
                nodelist.remove(0);
            }

            //Connect chars when there are free edges
            if (centerNode.leftNode == null) {
                Node leftNode = new Node((char) usedChars[i][0], usedChars[i][1]);
                centerNode.leftNode = leftNode;
                i--;
            }

            if (i >= 0 && centerNode.rightNode == null) {
                Node rightNode = new Node((char) usedChars[i][0], usedChars[i][1]);
                centerNode.rightNode = rightNode;
                i--;
            }

            //count the weight of the center node
            int leftWeight = centerNode.leftNode.weight;
            int rightWeight = (centerNode.rightNode != null ? centerNode.rightNode.weight : 0);
            centerNode.weight = leftWeight + rightWeight;

            //add the node to the node list. To queue it for connection to the tree.
            nodelist.add(centerNode);
        }

        //if there are nodes left unconnected after we went throw all the chars connect the node here together.
        nodelist.sort((o1, o2) -> o2.weight - o1.weight);

        int j = nodelist.size() - 1;
        while (nodelist.size() > 1) {
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

        //get the root node
        rootNode = nodelist.get(nodelist.size() - 1);
    }

    /**
     * Creates the huffman code for each character rekursiv
     * @param node
     * @param huffCode
     */
    private void generateTable(Node node, String huffCode) {
        if (node.leftNode != null) {
            huffCode += "0";
            generateTable(node.leftNode, huffCode);
            huffCode = huffCode.substring(0, huffCode.length() - 1);
        }

        if (node.rightNode != null) {
            huffCode += "1";
            generateTable(node.rightNode, huffCode);
            huffCode = huffCode.substring(0, huffCode.length() - 1);
        }

        // Check if node is rootnode
        if (node.content != '\u0000') {
            huffmanTable.put(node.content, huffCode);
        }
    }
}
