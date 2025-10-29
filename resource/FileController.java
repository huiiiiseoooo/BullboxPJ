package resource;

import java.io.*;

public class FileController {
    BufferedOutputStream msgbos;
    private static final String UPLOAD_DIR = "Server/uploads/";

    public FileController(BufferedOutputStream bos) {
        msgbos = bos;

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs(); // 하위 디렉토리까지 생성
        }
    }

    public void createFile(String fileName, InputStream dataIs) throws IOException {
        File outputFile = new File(UPLOAD_DIR + fileName);
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = dataIs.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }
    }
}

