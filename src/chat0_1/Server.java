package chat0_1;
//不能收多个clien发来的消息
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket serverSocket;
//    ArrayList<BufferedReader> bReaders= new ArrayList<BufferedReader>();
    public void StarSer(){
        System.out.println("服务器启动，请稍后。。。。");
        try {
            //创建ServerSocket对象，绑定端口开始等待连接
            serverSocket=new ServerSocket(6666);
            //接收accept 方法返回socket对象
        } catch (IOException e) {
            e.printStackTrace();
        }
        new AceptMegFroCThread().start();
    }
      class  AceptMegFroCThread extends Thread{
        public void run(){
            while(this.isAlive()) {
                try {
                    Socket server = serverSocket.accept();
                    InputStream inputStream = server.getInputStream();
                    OutputStream outputStream = server.getOutputStream();
                    outputStream.write("服务器已收到谢谢！".getBytes());
                    byte[] b = new byte[1024];
                    int len = inputStream.read(b);
                    String msg = new String(b, 0, len);
                    System.out.println(msg);
                    //释放资源
                    System.out.println("正在接收消息");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
//          class  SendMegToCThread extends Thread{
//
//          }

    }
    public static void main(String a[] ){
        new Server().StarSer();

    }

}
