package chat_0_2_1;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class Server0_2_1 {
    ServerSocket serverSocket;
    LinkedList<String> msgList=new LinkedList<String>();
    ArrayList<BufferedReader> bReaders= new ArrayList<BufferedReader>();
    ArrayList<PrintWriter> pWriters= new ArrayList<PrintWriter>();

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
        BufferedReader bReader;
        public void run(){

            while(this.isAlive()){

                try {
                    Socket socket = serverSocket.accept();
                    if(socket!=null) {
                        bReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        bReaders.add(bReader);
                        new Server0_2_1.GetMsgFromClient(bReader).start();
                        pWriters.add(new PrintWriter(socket.getOutputStream()));
                    }
                    System.out.println("AceptMegFroCThread-------");

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            }

    }
    class  GetMsgFromClient extends Thread{
        String meg;
        BufferedReader bReader;
        public GetMsgFromClient(BufferedReader bReader) {
//            System.out.println("GetMegFroCThread----this.bReader=bReader--");
            this.bReader=bReader;
        }
        public void run(){

//            System.out.println("GetMegFroCThread----run--");
            while (this.isAlive()){
                try {
                    meg=bReader.readLine();
//                    System.out.println("GetMegFroCThread----meg=bReader.readLine();--");
                    if (meg!=null){
                        System.out.println("客户端发来消息"+meg);
                        System.out.println(meg);
                        msgList.add(meg);
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
                    String msg=msgList.removeLast();
                    for(int i=0;i<pWriters.size();i++) {
                        pWriters.get(i).println(msg);
                        pWriters.get(i).flush();
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
