package chat_up03;
//服务器可以接收到多人发的消息，但是无法再次接收同一客户端再发的消息

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
//可以按格式群发私聊，但是还不能显示在线用户列表
public class Client0_2_1 {
    Socket client;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    LinkedList<User> users= new LinkedList<User>();
    User user;

    public void StarCli(){

        System.out.println("客户端启动，请稍后。。。。");
        // 1.创建 Socket ( ip , port ) , 确定连接到哪里
        try {
                client=new Socket("127.0.0.1",6666);
                    System.out.println("请输入用户名");
                    Scanner in=new Scanner(System.in);
                    String username=in.nextLine();
//                    System.out.println(username);
                    user=new User();
                    user.setUsername(username);
                    user.setStatu(true);
                    if (user!=null){
                        System.out.println("连接成功！欢迎你"+user.getUsername());
                    new GetMsgFroServer().start();
                    new SendMsgtoServer().start();
            }




        } catch (IOException e) {
            System.out.println("无法连接服务器！");
            e.printStackTrace();
        }
    }
    public class SendMsgtoServer extends Thread{
        Message message=new Message();
        Scanner in;
        public void run() {

            try {
                objectOutputStream=new ObjectOutputStream(client.getOutputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
            //输入向服务器发送的消息
            while(this.isAlive()) {

                System.out.println("请输入发送的信息 格式接收者+发送信息 eg：张三+你好啊  群发则输入不包含‘+’的句子" );
                in=new Scanner(System.in);
                String sin=in.nextLine();
                System.out.println(sin);
                String[] s=sin.split("\\+");//按加号分割
                message.setSender(user.getUsername());
                if(sin.contains("+")){
//                    message=new Message();
                    message.setReceiver(s[0]);
                    message.setContent(s[1]);
                    System.out.println("发送者："+user.getUsername());
                    System.out.println("发送者："+user.getUsername());

                }
                else
                    message.setContent(sin);




                try {
                    objectOutputStream.writeObject(message);
                    objectOutputStream.flush();

                    System.out.println("CCCCCCCCCCCsssssssssend");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class GetMsgFroServer extends Thread{
        Message message=new Message();


        public void run(){
            try {
//
                objectInputStream=new ObjectInputStream(client.getInputStream());
//              bufferedReader=new BufferedReader(new InputStreamReader(client.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(this.isAlive()){
                try {
//                    meg=bufferedReader.readLine();
                    try {
                        message=(Message)objectInputStream.readObject();
//                        users=message.getUsers();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (message!=null){

                        System.out.println("CCCCCCCCCCCgggggggggget");

                        String un=user.getUsername();
                        System.out.println("CCCCCCCCCCCggggggggggetuser.当前线程拥有者User"+un);
                        System.out.println("CCCCCCCCCCCggggggggggetuser.信接收者"+message.getReceiver());
                        System.out.println("CCCggetmessage.写信人"+message.getSender());
                        if(message.getReceiver()!=null){
                            if (un.equals(message.getSender())){
                                System.out.println("CCCCCCCCCCCggggggggggetuser.message.getReceiver()"+message.getReceiver());
                                System.out.println("我对"+message.getReceiver()+"说"+message.getContent());
                            }
                            if (un.equals(message.getReceiver())){
                                System.out.println(message.getSender()+"对您说"+message.getContent());
                            }
                        }
                        else{
                            System.out.println(message.getSender()+"说"+message.getContent());
                        }
                    }//ifmessge!=null
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    public static void main(String a[]){
        new Client0_2_1().StarCli();
    }

}
