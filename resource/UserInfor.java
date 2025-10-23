package resource;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserInfor {
    String userId;
    String userPassword;
    BufferedOutputStream bos;
    Map<String, User> users = new HashMap<>();

    public UserInfor(BufferedOutputStream bos) {
        users.put("admin", new User());
        userId = users.get("admin").getUserId();
        userPassword = users.get("admin").getUserPassword();
        this.bos = bos;
    }

    public void userAuth(String authUserId) throws IOException {
        if(userId.equals(authUserId)){
            users.get(userId).IdAuth();
            bos.write("enter the password \n".getBytes());
            bos.flush();
        }else{
            bos.write("user is not exist \n".getBytes());
            bos.flush();
        }
    }

    public void userPasswordAuth(String authPassword) throws IOException {
        if(userPassword.equals(authPassword) && users.get(userId).IDAuth){
            users.get(userId).PasswordAuth();
            bos.write("success login \n".getBytes());
            bos.flush();
        }else{
            bos.write("wrong password \n".getBytes());
            bos.flush();
        }

    }

}
