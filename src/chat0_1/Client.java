package chat0_1;//单用户登录可以发多条消息
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Socket client;



    public void StarCli(){
        boolean flag=false;//登录状态
        System.out.println("客户端启动，请稍后。。。。");
        flag=true;
        // 1.创建 Socket ( ip , port ) , 确定连接到哪里
        try {
            while(flag){
                client=new Socket("192.168.5.6",6666);
                OutputStream outputStream=client.getOutputStream();
                System.out.println("请输入要向服务器发送的消息");
                //输入向服务器发送的消息
                Scanner in=new Scanner(System.in);
                String sin=in.nextLine();
                outputStream.write(sin.getBytes());

                InputStream inputStream=client.getInputStream();
                byte[] b=new byte[1024];
                int len=inputStream.read(b);
                String msg=new String(b,0,len);
                System.out.println(msg);
            }


        } catch (IOException e) {
            System.out.println("无法连接服务器！");
            e.printStackTrace();
        }
    }

    public static void main(String a[]){
         new Client().StarCli();
    }


}
