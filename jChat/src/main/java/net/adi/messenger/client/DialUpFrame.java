package net.adi.messenger.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import net.adi.messenger.common.ClientInfoBean;

public class DialUpFrame extends JFrame implements ActionListener 
{
	private static final long serialVersionUID = -7027836165956528296L;
	JTextField txtServerIP,txtServerPort;
	JLabel lblServerIp,lblServerPort,lblStatus,lblStatusMsg;
	JButton btnConnect;
	static String serv = "localhost";
	int flag = 0;
	String nm,ps;
	public static ClientInfoBean objCIB; 
	public static FriendListFrame objFrndList;
	public DialUpFrame()
	{
		super("Connect to...");
		setSize(250,150);
		setLayout(null);
		this.setLocation(300, 300);
		addWindowListener(new WindowAdapter (){
			public void windowClosing (WindowEvent e){
				System.exit(0);
			}
		});

		add(lblServerIp=new JLabel("Server IP:"));
		lblServerIp.setBounds(20,10,80,20);
		
		add(txtServerIP = new JTextField(10));
		txtServerIP.setBounds(100,10,100,20);
		
		add(lblServerPort=new JLabel("Port:"));
		lblServerPort.setBounds(20,40,80,20);
		
		add(txtServerPort = new JTextField("2112"));
		txtServerPort.setBounds(100,40,50,20);
		
		add(btnConnect = new JButton("Connect"));
		btnConnect.setBounds(100,70,90,20);
		
		lblStatusMsg = new JLabel(" Enter Server IP & Port to connect.");
		lblStatusMsg.setBounds(0,100,250,20);
		lblStatusMsg.setBorder(BorderFactory.createLineBorder(Color.black));
		add(lblStatusMsg);
		btnConnect.addActionListener(this);
	}

	public void actionPerformed(ActionEvent ae) 
	{
		if(ae.getSource()== btnConnect) {
			if(txtServerPort.getText().equals("")) {
				lblStatusMsg.setText(" Port No. meust be entered.");
			}
			else {
				connectToServer();
			}	
		}
	}
	
	private void connectToServer() 
	{
		int port = 2000;
		if(txtServerIP.getText() == "") {
			serv = "localhost";
			txtServerIP.setText(serv);
		}
			
		else
			serv = txtServerIP.getText();

		try	{
			port = Integer.parseInt(txtServerPort.getText());
			Socket socCli = new Socket(serv, port);
			objCIB = new ClientInfoBean(null,socCli,socCli.getInetAddress());
			lblStatusMsg.setText(" Connected to server " +socCli.getInetAddress()+":" + socCli.getPort());
			LoginFrame loginFrm = new LoginFrame();
			loginFrm.setLocation(400, 300);
			loginFrm.setResizable(false);
			loginFrm.setVisible(true);
			this.dispose();
		} catch (Exception e) {
			lblStatusMsg.setText(" Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
}

