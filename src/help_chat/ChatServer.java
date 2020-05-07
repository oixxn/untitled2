package help_chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

public class ChatServer {
	ServerSocket serverSocket;
	ArrayList<BufferedReader> bReaders= new ArrayList<BufferedReader>();
	ArrayList<PrintWriter> pWriters= new ArrayList<PrintWriter>();
	LinkedList<String> msgList=new LinkedList<String>();
	public ChatServer() {
		try {
			serverSocket =new ServerSocket(28888);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new AcceptSocketThread().start();
		new SendMsgToClient().start();
	}
	class AcceptSocketThread extends Thread{
		public void run() {
			while(this.isAlive()) {
				try {
					Socket socket = serverSocket.accept();
					if(socket!=null) {
						BufferedReader bReader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
						bReaders.add(bReader);
						new GetMsgFromClient(bReader).start();
						pWriters.add(new PrintWriter(socket.getOutputStream()));
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
	class GetMsgFromClient extends Thread{
		BufferedReader bReader;
		public GetMsgFromClient(BufferedReader bReader) {
			this.bReader=bReader;
		}
		public void run() {
			while(this.isAlive()) {
				try {
					String strMsg=bReader.readLine();
					if(strMsg!=null) {
						SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String strTime=dateFormat.format(new Date());
						msgList.add("<=="+strTime+"==>\n"+strMsg);

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	class SendMsgToClient extends Thread{
		public void run() {
			while(this.isAlive()) {
				try {
					if(!msgList.isEmpty()) {
						String msg=msgList.removeLast();
						for(int i=0;i<pWriters.size();i++) {
							pWriters.get(i).println(msg);
							pWriters.get(i).flush();
						}
					}

				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		new ChatServer();
	}

}


