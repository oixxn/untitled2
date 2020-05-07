package chat_up03;

import java.io.Serializable;

public class User implements Serializable {

    private  String username;
    private  String userId;
    private  String uerPassword;
    private  String sex;

    public boolean isStatu() {
        return statu;
    }

    public void setStatu(boolean statu) {
        this.statu = statu;
    }

    private  boolean statu;







    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUerPassword() {
        return uerPassword;
    }

    public void setUerPassword(String uerPassword) {
        this.uerPassword = uerPassword;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }







}
