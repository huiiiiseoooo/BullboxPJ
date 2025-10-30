package resource;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;


public class FolderController extends Controller {


    public FolderController(BufferedOutputStream bos) throws IOException {
        super(bos);
    }

    public void makeFolder(String path) throws IOException {
        File file = new File(WORKING_DIR + path);
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
        File file = new File(WORKING_DIR + path);
        if(file.exists()) {
            file.delete();
            msgbos.write("200 delete folder success \n".getBytes());
            msgbos.flush();
        }else{
            msgbos.write("delete folder is not exist \n".getBytes());
            msgbos.flush();
        }
    }

    public void changeWorkingDirectory(String newDir) {
        WORKING_DIR =newDir;
        try{
            msgbos.write(("200 success change working directory" +WORKING_DIR+"\n").getBytes());
            msgbos.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void showWorkingDirectory() {
        try{
            msgbos.write((WORKING_DIR+"\n").getBytes());
            msgbos.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
