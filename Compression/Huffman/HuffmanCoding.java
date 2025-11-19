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
    String[] table = new String[256];
    byte[] b;
    String newLine;
    byte[] sendBytes = new byte[4096];

    public HuffmanCoding(File file) throws IOException {
        this.file = file;
        try{
            fis = new FileInputStream(file);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        insertFile();
        makeHuffman();
        dfs(root,"");
        grantNum();
        encode();

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                System.out.println("'" + (char)i + "' = " + table[i]);
            }
        }
        System.out.println(newLine);
    }

    public void insertFile() throws IOException {
        b = fis.readAllBytes();
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

    public void dfs(Node node, String code){
        if(node==null){
            return;
        }
        if(node.left==null && node.right==null){
            table[node.value] = code;
            return;
        }
        dfs(node.left, code + "0");
        dfs(node.right, code + "1");
    }

    public void grantNum(){
        StringBuilder sb = new StringBuilder();
        for(int k: b){
            sb.append(table[k & 0xFF]);
        }
        while(sb.length() % 8 != 0){
            sb.append("0");
        }
        newLine = sb.toString();
    }

    public void encode(){
        String temp;
        int valuetemp;
        int index =0;
        for(int i = 0; i <newLine.length(); i +=8){
            temp = newLine.substring(i,i+8);
            valuetemp = Integer.parseInt(temp,2);
            sendBytes[index++] = (byte)valuetemp;
        }
    }


    public static void main(String[] args) throws IOException {
        HuffmanCoding hf = new HuffmanCoding(new File("Compression/Huffman/hello.txt"));
    }
}
