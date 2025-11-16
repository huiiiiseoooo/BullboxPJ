package Compression.Huffman;

import java.io.*;
import java.util.ArrayList;

public class MinHeap {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        ArrayList<Integer> list = new ArrayList<>();
        int n = Integer.parseInt(br.readLine());

        list.add(0);

        int k;
        for (int i = 0; i < n; i++) {
             k=Integer.parseInt(br.readLine());
             if(k == 0){
                 if(list.size()==1){
                     bw.write("0\n");
                 }else{
                     bw.write(list.get(1)+"\n");

                     int last = list.remove(list.size()-1);

                     if(list.size()==1){continue;}

                     list.set(1,last);

                     int index =1;
                     while(true){
                         int left = index*2;
                         int right = index*2+1;

                         if(left >= list.size()){break;}
                         int smaller = left;
                         if(right < list.size() && list.get(right)< list.get(left)){
                             smaller = right;
                         }

                         if(list.get(index) <= list.get(smaller)){break;}

                         int temp = list.get(index);
                         list.set(index,list.get(smaller));
                         list.set(smaller,temp);

                         index = smaller;
                     }
                 }
                 continue;
             }
             list.add(k);
             int index = list.size() -1;
             while(index > 1 && list.get(index) < list.get(index /2)) {
                 int temp = list.get(index);
                 list.set(index, list.get(index / 2));
                 list.set(index / 2, temp);
                 index /= 2;
             }
        }
        bw.flush();

    }

}
