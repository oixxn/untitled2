package help_chat;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame{
	Socket socket;
	PrintWriter pWriter;
	BufferedReader bReader;
	JPanel panel;
	JScrollPane sPane;
	JTextArea txtContent;
	JLabel lblName,lblSend;
	JTextField txtName,txtSend;
	JButton btnSend;

	public ChatClient() {

		super("聊天室");
		txtContent = new JTextArea();
		txtContent.setEditable(false);
		sPane = new JScrollPane(txtContent);

		lblName = new JLabel("昵称：");
		txtName = new JTextField(5);
		lblSend = new JLabel("发言：");
		txtSend = new JTextField(20);
		btnSend = new JButton("发送");

		panel = new JPanel();
		panel.add(lblName);
		panel.add(txtName);
		panel.add(lblSend);
		panel.add(txtSend);
		panel.add(btnSend);
		this.add(panel,BorderLayout.SOUTH);

		this.add(sPane);
		this.setSize(500,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			socket = new Socket("127.0.0.1",28888);
			pWriter = new PrintWriter(socket.getOutputStream());
			bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String strName = txtName.getText();
				String strMsg = txtSend.getText();
				if(!strMsg.equals("")) {
					pWriter.println(strName+"说："+strMsg);
					pWriter.flush();
					txtSend.setText("");
				}
			}


		});
		new GetMsgFromServer().start();
	}
	public class GetMsgFromServer extends Thread{
		public void run() {
			while(this.isAlive()) {
				try {
					String strMsg = bReader.readLine();
					if(strMsg !=null) {
						txtContent.append(strMsg+"\n");
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ChatClient().setVisible(true);
	}

}
