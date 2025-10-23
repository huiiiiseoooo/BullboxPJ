package resource;

public class User {
    private String userId = "admin"; //1차 배열로 변경, 2차 db로 변경
    private String userPassword = "qwer";
    boolean IDAuth = false;
    boolean PasswordAuth = false;

    public User() {
    }

    public void IdAuth(){
        IDAuth = true;
    }
    public void PasswordAuth(){
        PasswordAuth = true;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserId() {
        return userId;
    }
}

