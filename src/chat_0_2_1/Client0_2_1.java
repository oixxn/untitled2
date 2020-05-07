package chat_0_2_1;
//服务器可以接收到多人发的消息，但是无法再次接收同一客户端再发的消息
import chat0_1.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client0_2_1 {
    Socket client;
    BufferedWriter bufferedWriter;
    BufferedReader bufferedReader;


    public void StarCli(){

        System.out.println("客户端启动，请稍后。。。。");
        // 1.创建 Socket ( ip , port ) , 确定连接到哪里
        try {
                client=new Socket("127.0.0.1",6666);
                new GetMsgFroServer().start();
                new SendMsgtoServer().start();

        } catch (IOException e) {
            System.out.println("无法连接服务器！");
            e.printStackTrace();
        }
    }
    public class SendMsgtoServer extends Thread{
        Scanner in;
        public void run() {
            try {
//
                bufferedWriter=new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //输入向服务器发送的消息
            while(this.isAlive()) {
                System.out.println("请输入要向服务器发送的消息");
                in=new Scanner(System.in);
                String sin=in.nextLine();

                try {

                    bufferedWriter.write(sin+"\n");//服务器读取一行meg=bReader.readLine();需要判断回车
                    bufferedWriter.flush();//即使buffered未满也强制清除

                    System.out.println("CCCCCCCCCCCsssssssssend");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class GetMsgFroServer extends Thread{
        String meg;
        public void run(){
            try {
//
                bufferedReader=new BufferedReader(new InputStreamReader(client.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(this.isAlive()){
                try {
                    meg=bufferedReader.readLine();
                    if (meg!=null){
                        System.out.println("CCCCCCCCCCCgggggggggget");
                        System.out.println(meg);
                    }
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
