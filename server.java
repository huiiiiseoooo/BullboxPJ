import resource.FileController;
import resource.UserInfor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    static final int commandPort = 21;
    static final int dataPort = 20;

    public static Socket serverStart(){
        ServerSocket commandServerSocket = null;
        Socket socket = null;
        try{
            commandServerSocket = new ServerSocket(commandPort);
            socket = commandServerSocket.accept();
        }catch (Exception e){
            System.out.println("client accept failed \n"+e.getMessage());
        }
        System.out.println("client connected port is"+ commandServerSocket.getLocalPort());
        return socket;
    }

    public static Socket ConnectDataServer(String ipAddress, int dataPort) throws IOException {
        Socket DataSocket = new Socket(ipAddress, dataPort);
        System.out.println("success connect data server \n");
        return DataSocket;
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

    public static void main(String[] args) throws IOException {
        //커멘트 포트 연결 21
        Socket commandServerSocket = serverStart();

        //통신을 한 스트림 연결
        InputStream commandInputStream = commandServerSocket.getInputStream();
        OutputStream commandOutputStream = commandServerSocket.getOutputStream();
        BufferedOutputStream commandBosStream = new BufferedOutputStream(commandOutputStream);
        BufferedReader commandBrStream = new BufferedReader(new InputStreamReader(commandInputStream));

        //파일 전송 포트 연결 20
        Socket dataSocket = null;

        UserInfor userInfor = null;
        FileController fileController = null;
        String[] commands;

        while(!commandServerSocket.isClosed()) {
            try{
                commands = commandBrStream.readLine().split(" ");
            }catch (Exception e){
                System.out.println("client leave server");
                break;
            }

            //로그인 기능 구현 아직 권한은 미구현
            if(commands[0].equals("USER")){
                userInfor = new UserInfor(commandBosStream);
                userInfor.userAuth(commands[1]);
            }

            if(commands[0].equals("PASS")){
                userInfor.userPasswordAuth(commands[1]);
            }

            if(commands[0].equals("MKD")){
                fileController = new FileController(commandBosStream);
                fileController.makeFolder(commands[1]);
            }

            if(commands[0].equals("RMD")){
                fileController.deleteFolder(commands[1]);
            }

            if(commands[0].equals("STOR")){
                if(dataSocket == null){
                    dataSocket = ConnectDataServer(commands[0], Integer.parseInt(commands[1]));
                }
            }


        }

    }

}
