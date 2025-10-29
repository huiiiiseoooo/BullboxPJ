package resource;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;


public class FolderController {
    private BufferedOutputStream msgbos;
    private static final String UPLOAD_DIR = "Server/uploads/";

    public FolderController(BufferedOutputStream bos) throws IOException {
        msgbos = bos;
    }

    public void makeFolder(String path) throws IOException {
        File file = new File(UPLOAD_DIR + path);
        if(!file.exists()) {
            file.mkdir();
            msgbos.write("200 make new folder success \n".getBytes());
            msgbos.flush();
        }else{
            msgbos.write("folder is already exist \n".getBytes());
            msgbos.flush();
        }
    }

    public void deleteFolder(String path) throws IOException {
        File file = new File(UPLOAD_DIR + path);
        if(file.exists()) {
            file.delete();
            msgbos.write("delete folder success \n".getBytes());
            msgbos.flush();
        }else{
            msgbos.write("delete folder is not exist \n".getBytes());
            msgbos.flush();
        }
    }

}
