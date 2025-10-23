package Server;

import resource.FileController;
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
        String[] commands;

        while(!commandServerSocket.isClosed()) {
            try{
                commands = commandBrStream.readLine().split(" ");
            }catch (Exception e){
                System.out.println("Client.client leave Server.server");
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
                fileController.makeFolder(commands[1]);
            }

            if(commands[0].equals("RMD")){
                fileController.deleteFolder(commands[1]);
            }

            if(commands[0].equals("STOR")){
                if(clientDataIp == null && clientDataPort == -1){
                    System.out.println("Client.client data ip is null");
                }else{
                    String filename = commands[1];
                    try{
                        String response = "150 Opening ASCII mode data connection for " + filename + "\n";
                        commandBosStream.write(response.getBytes());
                        commandBosStream.flush();

                        //active서버 연결
                        dataSocket = ConnectDataServer(clientDataIp, clientDataPort);
                        System.out.println("success connect data Server.server \n");

                        InputStream dataIs = dataSocket.getInputStream();
                        fileController.createFile(filename, dataIs);

                        response = "226 Transfer complete.\n";

                        commandBosStream.write(response.getBytes());
                        commandBosStream.flush();
                        dataSocket.close();
                    }catch (Exception e){
                        System.out.println("Dataserver connect failed"+e.getMessage());
                    }

                    //소켓 ip port 초기화
                    dataSocket = null;
                    clientDataIp = null;
                    clientDataPort = -1;
                }
            }

            if(commands[0].toUpperCase().equals("PORT")){
                try{
                    String[] parts = commands[1].split(",");
                    clientDataIp = String.format("%s.%s.%s.%s", parts[0], parts[1], parts[2], parts[3]);
                    int p1 = Integer.parseInt(parts[4]);
                    int p2 = Integer.parseInt(parts[5]);
                    clientDataPort = p1*256 + p2;

                    //200 성공 메세지 전송
                    String response = "200 PORT command successful.\n";
                    commandBosStream.write(response.getBytes());
                    commandBosStream.flush();
                }catch (Exception e){
                    System.out.println("can't receive port,ip \n"+e.getMessage());
                }
            }


        }

    }

}
