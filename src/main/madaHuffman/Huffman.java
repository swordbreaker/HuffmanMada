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
    public HashMap<Character, Integer> huffmanTable;

    public Huffman(char[] text){
        this.text = text;

        countChars();
        encode();
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

    private void encode(){
        Arrays.sort(usedChars, (o1, o2) -> o2[1] - o1[1]);

        ArrayList<Node> nodelist = new ArrayList<>();

        int i = usedChars.length - 1;
        while (i >= 0 && usedChars[i][1] > 0){

            Node centerNode = new Node();

            int j = 0;
            while (nodelist.size() > 0 && nodelist.get(j).weight < usedChars[i][1] && centerNode.rightNode == null){
                if(centerNode.leftNode == null)
                    centerNode.leftNode = nodelist.get(j);
                else
                    centerNode.rightNode = nodelist.get(j);
                j++;
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

            centerNode.weight = centerNode.leftNode.weight + (centerNode.rightNode != null ? centerNode.rightNode.weight : 0);
            if(centerNode.leftNode != null) nodelist.remove(centerNode.leftNode);
            if(centerNode.rightNode != null) nodelist.remove(centerNode.leftNode);
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
            nodelist.add(centerNode);
        }

        System.out.println(nodelist.get(nodelist.size() - 1));

//        for(Node node : nodelist){
//            System.out.println(node);
//        }
    }
}
