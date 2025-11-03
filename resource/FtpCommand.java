package resource;

public enum FtpCommand {
    USER,
    PASS,
    STOR,
    RETR,
    PORT,
    MKD,
    RMD,
    CWD,
    PWD,
    LIST,
    DELE,
    QUIT,
    UNKNOWN;

    public static FtpCommand fromString(String command) {
        try{
            return FtpCommand.valueOf(command.toUpperCase());
        }catch(IllegalArgumentException e){
            return UNKNOWN;
        }
    }
}
