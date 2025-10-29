package Server;

import resource.FileController;
import resource.FolderController;
import resource.FtpCommand;
import resource.UserInfor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
    static final int commandPort = 21;
    static final int dataPort = 20;

    //active모드 클라이언트 port와 ip
    static String clientDataIp = null;
    static int clientDataPort = -1;

    public static Socket serverStart(){
        ServerSocket commandServerSocket = null;
        Socket socket = null;
        try{
            commandServerSocket = new ServerSocket(commandPort);
            socket = commandServerSocket.accept();
        }catch (Exception e){
            System.out.println("Client.client accept failed \n"+e.getMessage());
        }
        System.out.println("Client.client connected port is"+ commandServerSocket.getLocalPort());
        return socket;
    }

    public static Socket ConnectDataServer(String ipAddress, int dataPort) throws IOException {
        Socket DataSocket = new Socket(ipAddress, dataPort);
        System.out.println("success connect data Server.server \n");
        return DataSocket;
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
        FileController fileController = new FileController(commandBosStream);
        FolderController folderController = new FolderController(commandBosStream);

        String[] clientMsg;
        String response;

        FtpCommand command;

        while(!commandServerSocket.isClosed()) {
            try{
                clientMsg = commandBrStream.readLine().split(" ");
                command = FtpCommand.fromString(clientMsg[0]);
            }catch (Exception e){
                System.out.println("Client.client leave Server.server");
                break;
            }

            //로그인 기능 구현 아직 권한은 미구현
            if(command == FtpCommand.USER){
                userInfor = new UserInfor(commandBosStream);
                userInfor.userAuth(clientMsg[1]);
            }

            if(command == FtpCommand.PASS){
                userInfor.userPasswordAuth(clientMsg[1]);
            }
            //폴더 관리 부분
            if(command == FtpCommand.MKD){
                String dirPath = clientMsg[1];
                folderController.makeFolder(dirPath);

            }

            if(command == FtpCommand.RMD){

            }

            //파일 관리 부분
            //파일 업로드
            if(command == FtpCommand.STOR){
                if(clientDataIp == null && clientDataPort == -1){
                    System.out.println("Client.client data ip is null");
                }else{
                    String filename = clientMsg[1];
                    try{
                        response = "150 Opening ASCII mode data connection for " + filename + "\n";
                        commandBosStream.write(response.getBytes());
                        commandBosStream.flush();

                        //active서버 연결
                        dataSocket = ConnectDataServer(clientDataIp, clientDataPort);

                        InputStream dataIs = dataSocket.getInputStream();
                        fileController.createFile(filename, dataIs);
                        dataSocket.close();

                    }catch (Exception e){
                        System.out.println("Dataserver connect failed"+e.getMessage());
                    }
                    response = "226 Transfer complete.\n";
                    commandBosStream.write(response.getBytes());
                    commandBosStream.flush();
                    //소켓 ip port 초기화
                    dataSocket = null;
                    clientDataIp = null;
                    clientDataPort = -1;
                }
            }

            if(command == FtpCommand.RETR){

            }

            //active모드시 클라이언트로부터 포트 받음
            if(command == FtpCommand.PORT){
                try{
                    String[] parts = clientMsg[1].split(",");
                    clientDataIp = String.format("%s.%s.%s.%s", parts[0], parts[1], parts[2], parts[3]);
                    int p1 = Integer.parseInt(parts[4]);
                    int p2 = Integer.parseInt(parts[5]);
                    clientDataPort = p1*256 + p2;

                    //200 성공 메세지 전송
                    response = "200 PORT command successful.\n";
                    commandBosStream.write(response.getBytes());
                    commandBosStream.flush();
                }catch (Exception e){
                    System.out.println("can't receive port,ip \n"+e.getMessage());
                }
            }


        }

    }

}
