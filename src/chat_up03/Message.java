package chat_up03;

import java.io.BufferedReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Message implements Serializable {
//    一个类只有实现了Serializable接口，它的对象才是可序列化的。封装为唯一
    private String sender;
    private String receiver;
    private String content;
    private String type;

    LinkedList<User> users= new LinkedList<User>();

    public LinkedList<User> getUsers() {


        return users;
    }

    public void setUsers(LinkedList<User> users,User user) {

        this.users = users;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }






}
