package chat_before05;


import sun.reflect.generics.tree.VoidDescriptor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.LinkedList;

public class Server0_2_1 {
    ServerSocket serverSocket;
    LinkedList<Message> msgList=new LinkedList<Message>();
    LinkedList<User> users=new LinkedList<User>();
    Message message;
//    ArrayList<ObjectInputStream> objectInputStreams= new ArrayList<ObjectInputStream>();
    ArrayList<ObjectOutputStream> objectOutputStreams= new ArrayList<ObjectOutputStream>();
    Vector<Thread> pool= new Vector<Thread>();
    AceptMegFroCThread aceptMegFroCThread;
//    SendMsgtoClient sendMsgtoClient;
    GetMsgFromClient getMsgFromClient;
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
        aceptMegFroCThread=new Server0_2_1.AceptMegFroCThread();
//        sendMsgtoClient=new SendMsgtoClient();
        pool.add(aceptMegFroCThread);
//        pool.add(sendMsgtoClient);
        aceptMegFroCThread.start();
//        sendMsgtoClient.start();



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
//                        objectInputStreams.add(objectInputStream);
                        objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
                        /*每接收一次消息调用一次GetMsgFromClient
                        * */
                        getMsgFromClient= new Server0_2_1.GetMsgFromClient(objectInputStream,socket);
                        pool.add(getMsgFromClient);
                        getMsgFromClient.start();
                        objectOutputStreams.add(objectOutputStream);
                        for (Thread t:pool){
                            System.out.println("pooladd之前"+t.getId());

                        }

                    }

                } catch (IOException e) {
                    System.out.println("接收客户端消息出错");
                    e.printStackTrace();
                }

            }

            }

    }

/*
* 目前充当转发客户端发送过来的消息的线程
* */
    class  GetMsgFromClient extends Thread{
        Socket s;
        User user;
        ObjectInputStream objectInputStream;
        public GetMsgFromClient(ObjectInputStream obi,Socket socket) {
            this.objectInputStream=obi;
            this.s=socket;
        }
        public void run(){

            while (this.isAlive()){

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

                        }
                       //如果它要退出
                        if(message.getContent().equals("esc")){
//                            objectInputStream.close();无法确定线程同步时要报错
                            user=message.getUser();
                            users.remove(user);
                            message.setUsers(users);


                        }

                    } catch (ClassNotFoundException e) {
                        System.out.println("读取客户端消息出错");
//                        e.printStackTrace();
                        break;
                    }catch(SocketException socketException){
                        System.out.println("----Socket closed1---");
//                        e.printStackTrace();


                    }
                    catch (IOException e) {
                        System.out.println("读取客户端消息出错222");
//                        e.printStackTrace();
                    }
                    if (message!=null){
                        msgList.add(message);
                        System.out.println("------------->"+message.getContent());
                    /*
                    * 打印用来读取输入
                    *
                    * */

                        for(int i=0;i<objectOutputStreams.size();i++) {
                            System.out.println(i);
                            try {

                                /*如果发来的消息是要下线
                                * */
                                if(message.getContent().equals("esc")){
                                    System.out.println("-------message.getContent().equals(\"esc\")------>"+message.getContent().equals("esc"));
                                    /*获取消息方便转发，但关闭输入流
                                     * */
                                    for (Thread t:pool){
                                        System.out.println("pool之前"+t.getId());

                                    }
                                    objectOutputStreams.get(i).writeObject(message);
                                    objectOutputStreams.get(i).flush();
                                    System.out.println(i);
                                    objectInputStream.close();
                                    objectOutputStreams.get(i).close();
//                                    s.close();
                                    /*得先关闭线程再把它移出线程池
                                    * */
                                    pool.remove(aceptMegFroCThread);
                                    pool.remove(getMsgFromClient);
                                    for (Thread t:pool){

                                        System.out.println("pool之后"+t.getId());

                                    }
//                                    System.exit(0);
                                    break;



                                }

                                else{

                                    objectOutputStreams.get(i).writeObject(message);
                                    objectOutputStreams.get(i).flush();
                                }

                            } catch(SocketException socketException){
                                System.out.println("----Socket closed12---");


                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                        //跳出循环，以免服务器在某个客户端下线之后还在重复的等待接收消息
                        if(message.getContent().equals("esc")){ break;}

                    }

            }




        }
    }




    public static void main(String a[] ){
        new Server0_2_1().StarSer();

    }


}
