package resource;

import java.io.BufferedOutputStream;

public class Controller {
    protected static String WORKING_DIR = "Server/uploads/";
    protected static BufferedOutputStream msgbos;

    public Controller(BufferedOutputStream msgbos) {
        this.msgbos = msgbos;
    }

    public Controller() {}
}
