package Compression.Huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.PriorityQueue;

public class HuffmanCoding {
    private File file;
    public FileInputStream fis;
    private PriorityQueue<Node> pq = new PriorityQueue<>();
    Node root;

    public HuffmanCoding(File file) throws IOException {
        this.file = file;
        try{
            fis = new FileInputStream(file);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        insertFile();
    }

    public void insertFile() throws IOException {
        byte[] b = fis.readAllBytes();
        int[] freq = new int[256];
        for(int k: b){
            freq[k & 0xFF]++;
        }

        for(int i=0;i<256;i++){
            if(freq[i]>0){
                pq.add(new Node(i,freq[i]));
            }
        }
    }

    public void makeHuffman(){
        Node temp ;
        Node temp2;
        Node parent = null ;

        while(pq.size()>1){
            temp = pq.poll();
            temp2 = pq.poll();
            parent = new Node((temp.getFerq()+ temp2.getFerq()), temp, temp2);
            pq.add(parent);
        }

        root = parent;
    }

    public void grantBytenum(){

    }


    public static void main(String[] args) throws IOException {
        HuffmanCoding hf = new HuffmanCoding(new File("Compression/Huffman/hello.txt"));
    }
}
