package madaHuffman;

/**
 * Created by Tobias on 28.04.2016.
 * Developers: Janis, Tobias, Tom
 */
public class Node {
    public Node leftNode;
    public Node rightNode;
    public char content;
    public int weight;

    /**
     * Empty Constructor
     */
    public Node() {
    }

    /**
     * Constructor with content and weight
     * @param content
     * @param weight
     */
    public Node(char content, int weight) {
        this.content = content;
        this.weight = weight;
    }

    /**
     * Node in the huffman-Tree
     * @param leftNode
     * @param rightNode
     * @param weight
     */
    public Node(Node leftNode, Node rightNode, int weight) {
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.weight = weight;
    }

    /**
     *
     * @return
     */
    public String toString() {
        if (content == '\u0000') {
            return "[NODE weight: " + weight + ", \nleftnote: " + leftNode + "\nrightnode: " + rightNode + "]";
        } else {
            return "[NODE_" + content + " weight: " + weight + "\nleftnote:" + leftNode + "\nrightnode: " + rightNode + "]";
        }
    }
}
