import java.io.*;
import java.net.Socket;

public class client {
    public static void main(String[] args) throws IOException {
        //command 전송용 21번 포트로 서버 연결
        Socket socket = new Socket("localhost",21);

        //스트림 객체 생성
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        BufferedInputStream commandbufferedInputStream = new BufferedInputStream(inputStream);
        BufferedOutputStream commandbufferedOutputStream = new BufferedOutputStream(outputStream);

        //20번 포트를 받아오기
        String inputData;
        byte[] inputDataByte = new byte[1024];
        int readBytes = commandbufferedInputStream.read(inputDataByte);
        inputData = new String(inputDataByte, 0, readBytes);
        int filePort;
        filePort = Integer.parseInt(inputData);
        //data 전송용 20번 포트로 서버 연결
        Socket fileSocket = new Socket("localhost",filePort);
        BufferedOutputStream fbos = new BufferedOutputStream(fileSocket.getOutputStream());

        //command 전송 부분
        BufferedReader clientReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader serverReader = new BufferedReader(new InputStreamReader(commandbufferedInputStream));
        String response;
        while(true) {
            String command = clientReader.readLine();
            if(command.equals("exit")){
                break;
            }
            if(command.equals("STOR")){
                FileInputStream fileInputStream = new FileInputStream("./hello.txt");
                BufferedInputStream fbis = new BufferedInputStream(fileInputStream);
                byte[] filebuffer = new byte[4096];
                int byteRead;
                while((byteRead = fbis.read(filebuffer)) != -1) {
                    fbos.write(filebuffer, 0, byteRead);
                }
                fbos.flush();
            }
            commandbufferedOutputStream.write((command.trim()+"\n").getBytes());
            commandbufferedOutputStream.flush();

            response = serverReader.readLine();
            System.out.println(response);
        }

        //파일 전송부분
//        FileInputStream fileInputStream = new FileInputStream("./hello.txt");
//        BufferedInputStream fbis = new BufferedInputStream(fileInputStream);
//        byte[] filebuffer = new byte[4096];
//        int byteRead;
//        while((byteRead = fbis.read(filebuffer)) != -1) {
//            fbos.write(filebuffer, 0, byteRead);
//        }
//        fbos.flush();



    }
}
