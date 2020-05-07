package chat0_1;
//服务器可以登录打印在线列表
import chat_before05.Message;
import chat_before05.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client0_2_1 {
    Socket client;
    ObjectOutputStream objectOutputStream;
    ObjectInputStream objectInputStream;
    User user;
    SendMsgtoServer sendMsgtoServer;
    GetMsgFroServer getMsgFroServer;
    public void StarCli() {

        System.out.println("客户端启动，请稍后。。。。");
        // 1.创建 Socket ( ip , port ) , 确定连接到哪里
        try {
            client = new Socket("127.0.0.1", 6666);
            sendMsgtoServer=new SendMsgtoServer();
            sendMsgtoServer.start();
            getMsgFroServer=new GetMsgFroServer();
            getMsgFroServer .start();

        } catch (IOException e) {
            System.out.println("无法连接服务器！");
            e.printStackTrace();
        }
    }
/*
* 从键盘输入登录用户名，传到sendMsg。。设置登录状态=false用来实现初次登录
* */
    public class Logn {
        /*
         * 从键盘上获取用户名用于登录
         * */
        public User getLogin() {
            System.out.println("请输入用户名");
            Scanner in = new Scanner(System.in);
            String username = in.nextLine();
            while (username == null || "".equals(username)) {//如果用户名是空

                System.out.println("用户名不能为空请重新输入");
                in = new Scanner(System.in);
                username = in.nextLine();
            }

            user = new User();
            user.setUsername(username);
            user.setStatu(false);
            //将user中的部分数据存入message


            if (user != null) {
//                System.out.println("欢迎你getLogin" + user.getUsername());
                return user;

            }
            return null;
        }

    }

/*
* 发送消息到服务器
* */
    public class SendMsgtoServer extends Thread {
        Message message = new Message();
        Scanner in;
        SimpleDateFormat dateFormat;
        String strTime;

        public void run() {
            /*
             * 如果message类中的user的登录状态不在线
             * */
            Logn logn = new Logn();
//            System.out.println("欢迎你"+user.getUsername());//无法得到全局变量中的user
            User user1 = logn.getLogin();
            System.out.println("欢迎你" + user1.getUsername());

            if (user1 == null) {
                System.out.println("获取不到login中的user");
            } else message.setUser(user1);
//            System.out.println("message.getUser().isStatu():" + message.getUser().isStatu());

            try {

                objectOutputStream = new ObjectOutputStream(client.getOutputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }

            //输入向服务器发送的消息

            while (this.isAlive()) {
                /*
                 * 如果message中的user状态在线
                 * */
                if (message.getUser().isStatu() == true) {
                    System.out.println("请输入发送的信息 " +
                            "1.私聊格式接收者+发送信息 eg：张三+你好啊  " +
                            "2.群发则输入不包含‘+’的句子，" +
                            "3.如果只输入“esc”（区分大小写）则退出" );

                    in = new Scanner(System.in);
                    String sin = in.nextLine();

                    message.setSender(user.getUsername());
                    dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    strTime = dateFormat.format(new Date());
                    /*
                    * 在线且要求下线
                    * */
                    if(sin.equals("esc")){
                        message.setSender(user.getUsername());
                        message.setContent(sin);
                        message.setReceiver("all");
                    }
                    /*
                    在线且私聊
                    * */
                    else if (sin.contains("+")) {
                        System.out.println(sin);
                        String[] s = sin.split("\\+");//按加号分割
                        message.setSender(user.getUsername());
                        message.setReceiver(s[0]);
                        message.setContent(strTime +  "------"+s[1]);
//                        System.out.println("发送者：" + user.getUsername());
                    }
                    /*
                     * 在线且群发
                     * */
                    else {
                        message.setSender(user.getUsername());
                        message.setContent(sin + "------" + strTime);
                        message.setReceiver("all");
                    }
                    /*
                     * 发出客户在线时发出的信息
                     * */
                    try {
                        /*将前面的消息封装到新的message中避免服务端因为message全局变量不更新消息内容
                         * */
                        Message mm = new Message(message.getSender(), message.getContent(), message.getReceiver(), message.getUser());


                        objectOutputStream.writeObject(mm);
                        objectOutputStream.flush();
                        /*在发出消息后如果发出的消息是esc则不再输入消息
                        * */
                        if(message.getContent().equals("esc"))break;

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } /*如果用户没登录，将它要登录的信息由服务器转发到所有客户端
                 */ else {
                    message.setUser(user1);
                    message.setSender(user1.getUsername());
                    message.setContent(user.getUsername() + "用户登录了");//在客户端这设置是它不会一直重复循环输出到servvice

                    try {
                        objectOutputStream.writeObject(message);
                        objectOutputStream.flush();

//                        System.out.println("CCCCCCCCCCCsssssssssend用户登录了");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                message.getUser().setStatu(true);
            }
        }
    }

    /*
     * 获取从服务器转发的消息
     * */
    public class GetMsgFroServer extends Thread {
        Message message = new Message();


        public void run() {
            try {

                objectInputStream = new ObjectInputStream(client.getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
            while (this.isAlive()) {
                try {
                    try {
                        message = (Message) objectInputStream.readObject();

                    } catch (ClassNotFoundException e) {
                        System.out.println("客户端获取信息时发生错误");
//                        e.printStackTrace();
                        break;
                    }
                    if (message != null) {
                        //用来对比信息发出者
                        String un = user.getUsername();
                        /*如果不是刚刚登录
                         * */
                        if (message.getReceiver() != null) {
                            /*如果消息发出者是本人
                            * */
                            if (un.equals(message.getSender())) {
                                        /*
                                        如果收到的消息是群发
                                        * */
                                        if (message.getReceiver().equals("all")) {

                                            /*如果是要群发告诉所有人我要下线了
                                            则在在自己的线程里告知正在退出
                                             * */
                                            if(message.getContent().equals("esc")){

                                                System.out.println("正在退出");
//                                                closeQ c=new closeQ();
//                                                c.close();
                                                break;

                                            }
                                            //如果写信是本人且只是单纯群发消息
                                            else
                                            System.out.println(message.getSender() + "（我）说" + message.getContent());
                                        }
                                                //如果写信人是自己且不是要群发，而是私聊
                                        else {
//                                            System.out.println("CCCCCCCCCCCggggggggggetuser.message.getReceiver()" + message.getReceiver());
                                            System.out.println("我对" + message.getReceiver() + "说" + message.getContent());
                                        }
                            }
                            /*如果不是第一次登陆且写信人不是自己
                             * */
                            else if (message.getReceiver().equals("all")) {

                                    /*如果不是自己的某个ren要退出了
                                     * */
                                    if(message.getContent().equals("esc")){

                                        System.out.println(message.getSender() +"退出了");


                                    }
                                    //如果别人只是单纯群发消息
                                    else{


                                        System.out.println(message.getSender() + "说" + message.getContent());
                                    }


                                }

                            /*
                            如果不是第一次登陆且收信人是自己（发信人可以是自己）。。私聊
                            */
                            if (un.equals(message.getReceiver())) {

                                System.out.println(message.getSender() + "对您说" + message.getContent());
                            }


                        }
                        /*如果是刚刚登录
                        * */
                        else {
                            System.out.println(message.getContent());
                            /*如果消息发出者是本人
                             * */
                            if (un.equals(message.getSender())){

                                for (User u : message.getListUsers()
                                ) {
                                    System.out.println("<-----在线---->" + u.getUsername());

                                }
                            }


                        }
                    }//ifmessge!=null
                } catch (IOException e) {
                    System.out.println("客户端获取信息时发生错误");
                    e.printStackTrace();
                    break;

                }
            }
        }

    }
    public class closeQ{
        public void close(){

            try {
                objectInputStream.close();
                objectOutputStream.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static void main(String a[]) {
        new Client0_2_1().StarCli();
    }

}
