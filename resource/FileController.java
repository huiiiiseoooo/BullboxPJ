package resource;

import java.io.*;

public class FileController {
    BufferedOutputStream msgbos,filebos;
    FileOutputStream fout = null;
    public FileController(BufferedOutputStream bos) {
        msgbos = bos;
    }

    public void makeFolder(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()) {
            file.mkdir();
            msgbos.write("make new folder success \n".getBytes());
            msgbos.flush();
        }else{
            msgbos.write("folder is already exist \n".getBytes());
            msgbos.flush();
        }
    }

    public void deleteFolder(String path) throws IOException {
        File file = new File(path);
        if(file.exists()) {
            file.delete();
            msgbos.write("delete folder success \n".getBytes());
            msgbos.flush();
        }else{
            msgbos.write("delete folder is not exist \n".getBytes());
            msgbos.flush();
        }
    }

    public void fileRead(String path, String fileName, File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);



    }
}

