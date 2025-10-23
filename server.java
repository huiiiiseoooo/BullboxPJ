import resource.FileController;
import resource.UserInfor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    static final int commandPort = 21;
    static final int dataPort = 20;
    public static void main(String[] args) throws IOException {
        //커멘트 포트 연결 21
        Socket commandsocket = serverStart();

        //통신을 한 스트림 연결
        InputStream commandInputStream = commandsocket.getInputStream();
        OutputStream commandOutputStream = commandsocket.getOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(commandOutputStream);
        BufferedReader br = new BufferedReader(new InputStreamReader(commandInputStream));

        //파일 전송 포트 연결 20
        Socket fileSocket = fileSocket(bufferedOutputStream);
        InputStream fileInputStream = fileSocket.getInputStream();
        OutputStream fileOutputStream = fileSocket.getOutputStream();

        UserInfor userInfor = null;
        FileController fileController = null;
        String[] commands;

        while(!commandsocket.isClosed()) {

            try{
                commands = br.readLine().split(" ");
            }catch (Exception e){
                System.out.println("client leave server");
                break;
            }

            //로그인 기능 구현 아직 권한은 미구현
            if(commands[0].equals("USER")){
                userInfor = new UserInfor(bufferedOutputStream);
                userInfor.userAuth(commands[1]);
            }

            if(commands[0].equals("PASS")){
                userInfor.userPasswordAuth(commands[1]);
            }

            if(commands[0].equals("MKD")){
                fileController = new FileController(bufferedOutputStream);
                fileController.makeFolder(commands[1]);
            }

            if(commands[0].equals("RMD")){
                fileController.deleteFolder(commands[1]);
            }

            if(commands[0].equals("STOR")){
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(commands[1])));
                bos.write(fileInputStream.read());
                bos.flush();
            }


        }

    }
    public static Socket serverStart(){
        ServerSocket commandserverSocket = null;
        Socket socket = null;
        try{
            commandserverSocket = new ServerSocket(commandPort);
            socket = commandserverSocket.accept();
        }catch (Exception e){
            System.out.println("client accept failed \n"+e.getMessage());
        }
        System.out.println("client connected port is"+ commandserverSocket.getLocalPort());
        return socket;
    }

    public static Socket fileSocket(BufferedOutputStream bos) throws IOException {
        ServerSocket fileserverSocket = new ServerSocket(dataPort);
        String outputMessage = String.valueOf(dataPort);
        bos.write(outputMessage.getBytes());
        bos.flush();
        Socket filesocket = fileserverSocket.accept();
        System.out.println("client connected port is"+ fileserverSocket.getLocalPort());
        return filesocket;
    }


}
