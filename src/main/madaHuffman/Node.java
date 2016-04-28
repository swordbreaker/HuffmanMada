package madaHuffman;

/**
 * Created by Tobias on 28.04.2016.
 */
public class Node{
    public Node leftNode;
    public Node rightNode;
    public char content;
    public int weight;

    public Node(){}

    public Node(char content, int weight) {
        this.content = content;
        this.weight = weight;
    }

    public Node(Node leftNode, Node rightNode, int weight) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.weight = weight;
    }

    public String toString(){
        if(content == '\u0000'){
            return "[NODE weight: " + weight + ", \nleftnote: " + leftNode + "\nrightnode: " + rightNode + "]";
        }
        else{
            return "[NODE_"+ content +" weight: " + weight + "\nleftnote:" + leftNode + "\nrightnode: " + rightNode + "]";
        }
    }
}
