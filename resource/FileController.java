package resource;

import java.io.*;
import java.net.Socket;

public class FileController {
    private static final String UPLOAD_DIR = "Server/uploads/";

    public FileController( ) {

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // 하위 디렉토리까지 생성
        }
    }

    public void createFile(String fileName, InputStream dataIs, boolean select) throws IOException {
        File outputFile;
        if (select) {
            outputFile = new File(UPLOAD_DIR + fileName);
        }else{
            outputFile = new File(fileName);
        }
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = dataIs.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }

    public void downloadFile(String fn, Socket dataSocket) throws IOException {
        String filename = UPLOAD_DIR + fn;

        try{
            BufferedOutputStream fbos = new BufferedOutputStream(dataSocket.getOutputStream());
            FileInputStream fileIs = new FileInputStream(filename);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileIs.read(buffer)) != -1) {
                fbos.write(buffer, 0, bytesRead);
                }
            fbos.flush();
            fileIs.close();
        }catch (FileNotFoundException e){
            System.out.println("File not found"+e.getMessage());
        }
    }
}

