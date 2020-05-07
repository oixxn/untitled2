package chat_up03;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server0_2_1 {
    ServerSocket serverSocket;
    LinkedList<Message> msgList=new LinkedList<Message>();
//    ArrayList<BufferedReader> bReaders= new ArrayList<BufferedReader>();
//    ArrayList<PrintWriter> pWriters= new ArrayList<PrintWriter>();
//    ObjectInputStream objectInputStream;
//    ObjectOutputStream objectOutputStream;
    ArrayList<ObjectInputStream> objectInputStreams= new ArrayList<ObjectInputStream>();
    ArrayList<ObjectOutputStream> objectOutputStreams= new ArrayList<ObjectOutputStream>();
    public void StarSer(){
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
    }
    class  AceptMegFroCThread extends Thread{
//        BufferedReader bReader;
        ObjectInputStream objectInputStream;
        ObjectOutputStream objectOutputStream;
        public void run(){

            while(this.isAlive()){

                try {

                    Socket socket = serverSocket.accept();
                    System.out.println("AceptMegFroCThread---isAlive----");
                    if(socket!=null) {
//                        bReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        objectInputStream=new ObjectInputStream(socket.getInputStream());
//                        bReaders.add(bReader);
                        objectInputStreams.add(objectInputStream);
                        objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
                        new Server0_2_1.GetMsgFromClient(objectInputStream).start();
//                        pWriters.add(new PrintWriter(socket.getOutputStream()));
                        objectOutputStreams.add(objectOutputStream);
                    }

                    System.out.println("AceptMegFroCThread-------");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            }

    }
    class  GetMsgFromClient extends Thread{
//        String meg;
        Message message;
//        BufferedReader bReader;
        ObjectInputStream objectInputStream;
        public GetMsgFromClient(ObjectInputStream obi) {
//            System.out.println("GetMegFroCThread----this.bReader=bReader--");
            this.objectInputStream=obi;
        }
        public void run(){

            System.out.println("GetMegFroCThread----run--");
            while (this.isAlive()){
                try {
//                    meg=bReader.readLine();
                    try {
                         message=(Message) objectInputStream.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    System.out.println("GetMegFroCThread----meg=bReader.readLine();--");
                    if (message!=null){
                        System.out.println(message.getSender()+"发来消息"+message.getContent());
//                        System.out.println(meg);
                        msgList.add(message);
                        new SendMsgtoClient().start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }

    class  SendMsgtoClient extends Thread{

        public void run()
        {
            if (this.isAlive()){
                if(!msgList.isEmpty()) {
//                    String msg=msgList.removeLast();
                      Message message=msgList.removeLast();
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
