package chat_befor06;

import java.io.Serializable;
import java.util.LinkedList;

public class Message implements Serializable {
//    一个类只有实现了Serializable接口，它的对象才是可序列化的。封装为唯一
    private String sender;
    private String receiver;
    private String content;
    private String type;
    private User user;

    public Message() {
        super();
    }

    public Message(String sender, String content, String receiver, User user) {
        this.sender=sender;
        this.content=content;
        this.receiver=receiver;
        this.user=user;
    }


    public void setUsers(LinkedList<User> users) {
        this.users = users;
    }

    LinkedList<User> users= new LinkedList<User>();



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }



    public LinkedList<User> getListUsers() {


        return users;
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
