package Compression.Huffman;

public class Node implements Comparable<Node> {
    Node left,right;
    int ferq;
    int value;
    public Node(int value, int ferq) {
        this.ferq = ferq;
        this.value = value;
    }

    public Node(int ferq, Node left, Node right) {
        this.ferq = ferq;
        this.left = left;
        this.right = right;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public int getFerq() {
        return ferq;
    }


    @Override
    public int compareTo(Node o) {
        return this.ferq - o.ferq;
    }
}
