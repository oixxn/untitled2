package chatup04;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server0_2_1 {
    ServerSocket serverSocket;
    LinkedList<Message> msgList=new LinkedList<Message>();
    LinkedList<User> users=new LinkedList<User>();
    Message message;
    ArrayList<ObjectInputStream> objectInputStreams= new ArrayList<ObjectInputStream>();
    ArrayList<ObjectOutputStream> objectOutputStreams= new ArrayList<ObjectOutputStream>();

    public void StarSer(){

        /*
        * 启动服务器打开接收客户端消息的线程，并在线程类中转发消息到客户端
        * */
        System.out.println("服务器启动-------");
        try {
            //创建ServerSocket对象，绑定端口开始等待连接
            serverSocket=new ServerSocket(6666);

            //接收accept 方法返回socket对象
        } catch (IOException e) {
            System.out.println("服务器启动失败");
            e.printStackTrace();
        }
        new Server0_2_1.AceptMegFroCThread().start();
        new Server0_2_1.SendMsgtoClient().start();

    }
    /*
    * 服务器开启accept()接收客户端发来的消息
    * 用输入流集合接收客户端输出流中的信息
    * 初始化服务器端的输入流输出流
    * */
    class  AceptMegFroCThread extends Thread{

        ObjectInputStream objectInputStream;
        ObjectOutputStream objectOutputStream;
        public void run(){

            while(this.isAlive()){

                try {

                    Socket socket = serverSocket.accept();
                    if(socket!=null) {
                        objectInputStream=new ObjectInputStream(socket.getInputStream());
                        objectInputStreams.add(objectInputStream);
                        objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
                        /*每接收一次消息调用一次GetMsgFromClient
                        * */
                        new Server0_2_1.GetMsgFromClient(objectInputStream).start();
                        objectOutputStreams.add(objectOutputStream);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            }

    }

/*
* 目前充当转发客户端发送过来的消息的线程
* */
    class  GetMsgFromClient extends Thread{

        User user;
        ObjectInputStream objectInputStream;
        public GetMsgFromClient(ObjectInputStream obi) {
            this.objectInputStream=obi;
        }
        public void run(){

            while (this.isAlive()){
                try {
                    try {
                         message=(Message) objectInputStream.readObject();
                         /*
                         * 抽出message发来的user存入users
                         * 存入message的listUser列表中方便客户端读取中用来验证打印在线用户
                         * */
                         if(message.getReceiver()==null){
                             user=message.getUser();
                             users.add(user);
                             message.setUsers(users);

                         }/*
                         */
                         if(message.getContent().equals("esc")){
                             user=message.getUser();
                             users.remove(user);
                             message.setUsers(users);
                         }

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (message!=null){
                        msgList.add(message);
                        System.out.println("------------->"+message.getContent());
                    /*
                    * 打印用来读取输入
                    * 我觉得实际上这边的输出流每次都会输出出去每次都只有1次
                    * */

                        for(int i=0;i<objectOutputStreams.size();i++) {

                            try {
                                objectOutputStreams.get(i).writeObject(message);
                                objectOutputStreams.get(i).flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }




        }
    }
    /*
     * 目前不运行
     * */
    class  SendMsgtoClient extends Thread{

        public void run()
        {

            if (this.isAlive()){
                if(!msgList.isEmpty()) {

                      Message message=msgList.removeLast();
                        System.out.println("打印服务器是否改变user状态--->"+message.getUser().isStatu());
                    for(int i=0;i<objectOutputStreams.size();i++) {

                        try {
                            objectOutputStreams.get(i).writeObject(message);
                            objectOutputStreams.get(i).flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
//

        }


    }




    public static void main(String a[] ){
        new Server0_2_1().StarSer();

    }


}
