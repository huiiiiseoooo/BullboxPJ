import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class client {
    static BufferedInputStream commandbufferedInputStream;
    static BufferedOutputStream commandbufferedOutputStream;
    static Socket commandSocket;
    public static Socket OpenDataServerSocket(BufferedOutputStream commandOutputStream) throws IOException {
        ServerSocket dataServerSocket = new ServerSocket(0);
        Socket socket = null;

        int dataPort = dataServerSocket.getLocalPort();
        byte[] ipAddress = commandSocket.getLocalAddress().getAddress();

        String ipAddressString = String.format("%d,%d,%d,%d",
                ipAddress[0] & 0xFF,
                ipAddress[1] & 0xFF,
                ipAddress[2] & 0xFF,
                ipAddress[3] & 0xFF
                );

        String portCommand = String.format("%s %s\n",ipAddressString, dataPort);

        commandOutputStream.write((portCommand.trim( )+"\n").getBytes());
        commandOutputStream.flush();

        try{
            socket = dataServerSocket.accept();
            System.out.println("Accepted dataport " + dataPort);
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

        return socket;
    }



    public static void main(String[] args) throws IOException {
        //command 전송용 21번 포트로 서버 연결
        commandSocket = new Socket("localhost",21);

        //active모드 연결 스트림 생성
        Socket DataSocket = null;



        //스트림 객체 생성
        InputStream inputStream = commandSocket.getInputStream();
        OutputStream outputStream = commandSocket.getOutputStream();
        commandbufferedInputStream = new BufferedInputStream(inputStream);
        commandbufferedOutputStream = new BufferedOutputStream(outputStream);

        //command 전송 부분
        BufferedReader clientMsgReader = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader serverMsgReader = new BufferedReader(new InputStreamReader(commandbufferedInputStream));
        String response;
        while(true) {
            String command = clientMsgReader.readLine();
            if(command.equals("exit")){
                break;
            }
            if(command.equals("STOR")){
                if(DataSocket == null) {
                    DataSocket = OpenDataServerSocket(commandbufferedOutputStream);
                }
            }
            commandbufferedOutputStream.write((command.trim()+"\n").getBytes());
            commandbufferedOutputStream.flush();

            response = serverMsgReader.readLine();
            System.out.println(response);
        }



    }
}
