package Client;

import resource.FileController;
import resource.FtpCommand;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class client {
    //command버퍼 스트림
    static BufferedInputStream commandBis;
    static BufferedOutputStream commandBos;

    static Socket commandSocket;
    static Socket dataSocket;
    static ServerSocket dataServerSocket;

    public static ServerSocket OpenDataServerSocket() throws IOException {
        ServerSocket dataServerSocket = new ServerSocket(0);

        int dataPort = dataServerSocket.getLocalPort();
        byte[] ipAddress = commandSocket.getLocalAddress().getAddress();

        String ipAddressString = String.format("%d,%d,%d,%d",
                ipAddress[0] & 0xFF,
                ipAddress[1] & 0xFF,
                ipAddress[2] & 0xFF,
                ipAddress[3] & 0xFF
                );

        String portCommand = String.format("PORT %s,%d,%d",ipAddressString, dataPort/256,dataPort%256);

        commandBos.write((portCommand+"\n").getBytes());
        commandBos.flush();

        return dataServerSocket;
    }

    public static void main(String[] args) throws IOException {
        //command 전송용 21번 포트로 서버 연결
        commandSocket = new Socket("localhost",21);

        //active모드 연결 스트림 생성
        dataServerSocket = null;

        //스트림 객체 생성
        commandBis = new BufferedInputStream(commandSocket.getInputStream());
        commandBos = new BufferedOutputStream(commandSocket.getOutputStream());

        //클라이언트 입력 받기 부분
        BufferedReader clientMsgReader = new BufferedReader(new InputStreamReader(System.in));
        //서버 응답 받아오기
        BufferedReader serverMsgReader = new BufferedReader(new InputStreamReader(commandBis));

        String response;

        while(true) {
            //클라이언트 메세지 입력
            String clientMsg = clientMsgReader.readLine();
            //열거형으로 명령어 받기
            FtpCommand command = FtpCommand.fromString(clientMsg.split(" ")[0]);

            if(clientMsg.equals("quit")){
                break;
            }

            if(command == FtpCommand.MKD){
                commandBos.write((clientMsg+"\n").getBytes());
                commandBos.flush();

                response = serverMsgReader.readLine();
                System.out.println("<-" + response);
            }

            if(command == FtpCommand.RMD){
                commandBos.write((clientMsg+"\n").getBytes());
                commandBos.flush();

                response = serverMsgReader.readLine();
                System.out.println("<-" + response);
            }

            if(command == FtpCommand.CWD){
                commandBos.write((clientMsg+"\n").getBytes());
                commandBos.flush();

                response = serverMsgReader.readLine();
                System.out.println("<-" + response);
            }

            if(command == FtpCommand.PWD){
                commandBos.write((clientMsg+"\n").getBytes());
                commandBos.flush();

                response = serverMsgReader.readLine();
                System.out.println("<-" + response);
            }

            if(command == FtpCommand.LIST){
                commandBos.write((clientMsg+"\n").getBytes());
                commandBos.flush();

                response = serverMsgReader.readLine();
                System.out.println("<-" + response);
            }

            if(command == FtpCommand.STOR){
                    dataServerSocket = OpenDataServerSocket();

                    response = serverMsgReader.readLine();
                    System.out.println("<- " + response);

                    commandBos.write((clientMsg +"\n").getBytes());
                    commandBos.flush();

                    response = serverMsgReader.readLine();
                    System.out.println("<- " + response);

                    dataSocket = dataServerSocket.accept();
                    System.out.println("connect success" + dataSocket.getInetAddress().getHostAddress());

                    String filename = clientMsg.split(" ")[1];

                    try(BufferedOutputStream fbos = new BufferedOutputStream(dataSocket.getOutputStream());
                        FileInputStream fileIs = new FileInputStream(filename)) {

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = fileIs.read(buffer)) != -1) {
                            fbos.write(buffer, 0, bytesRead);
                        }
                        fbos.flush();

                    }catch (FileNotFoundException e){
                        System.out.println("File not found"+e.getMessage());
                    }
                response = serverMsgReader.readLine();
                System.out.println("<- " + response);
                System.out.println("file upload success");

                dataSocket.close();
                dataServerSocket.close();

                dataSocket = null;
                dataServerSocket = null;
            }

            if(command == FtpCommand.RETR){
                dataServerSocket = OpenDataServerSocket();

                response = serverMsgReader.readLine();
                System.out.println("<- " + response);

                commandBos.write((clientMsg +"\n").getBytes());
                commandBos.flush();

                response = serverMsgReader.readLine();
                System.out.println("<- " + response);

                dataSocket = dataServerSocket.accept();
                System.out.println("connect success" + dataSocket.getInetAddress().getHostAddress());

                FileController fileController = new FileController();
                InputStream dataIs = dataSocket.getInputStream();
                fileController.createFile(clientMsg.split(" ")[1], dataIs,false);

                dataSocket.close();
                dataServerSocket.close();

                dataSocket = null;
                dataServerSocket = null;
            }

            if(command == FtpCommand.DELE){
                commandBos.write((clientMsg+"\n").getBytes());
                commandBos.flush();

                response = serverMsgReader.readLine();
                System.out.println("<- " + response);
            }

        }
    }
}
