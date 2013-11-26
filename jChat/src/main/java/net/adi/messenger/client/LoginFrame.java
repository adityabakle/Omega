package net.adi.messenger.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.adi.messenger.common.EncodeDecode;
import net.adi.messenger.common.TransportPacketBean;

public class LoginFrame extends JFrame implements ActionListener 
{
	private static final long serialVersionUID = 5788217996251537572L;
	static JTextField txtUserName;
	JLabel lblUserName,lblPassword,l3,lblStatusMsg;
	JButton btnLogin,btnSignUp;
	JPasswordField txtPassword;
	static UserSignUpFrame objUSUF;
	int flag = 0;
	static int po;
	String nm,ps;
	public LoginFrame()
	{
		super("Login to MadMan Chatting Server");
		setSize(250,150);
		setLayout(null);
		addWindowListener(new WindowAdapter () {
			public void windowClosing (WindowEvent e) {
				try {
					DialUpFrame.objCIB.sendExitPacket("C");
					if(objUSUF != null){
						objUSUF.dispose();
					}
					
					DialUpFrame.objCIB.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		} );
		
		add(lblUserName = new JLabel("Username :"));
		lblUserName.setBounds(20,10,80,20);
		add(txtUserName = new JTextField(15));
		txtUserName.setBounds(90,10,100,20);
		add(lblPassword = new JLabel("Password :"));
		lblPassword.setBounds(20,40,80,20);
		add(txtPassword = new JPasswordField(15));
		txtPassword.setBounds(90,40,100,20);
		//txtPassword.setEchoChar('*');
		
		add(btnSignUp = new JButton("SignUp"));
		btnSignUp.setBounds(40,70,80,20);
		add(btnLogin = new JButton("Login"));
		btnLogin.setBounds(130,70,70,20);
		
		lblStatusMsg = new JLabel(" Enter UserName and Password.");
		lblStatusMsg.setBounds(0,95,242,20);
		lblStatusMsg.setBorder(BorderFactory.createLineBorder(Color.black));
		add(lblStatusMsg);

		btnLogin.addActionListener(this);
		btnSignUp.addActionListener(this);	
	}

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent ae) 
	{
		po = 3113;
		TransportPacketBean strServerResp;
		if(ae.getSource()== btnSignUp) {
			objUSUF = new UserSignUpFrame();
			objUSUF.setVisible(true);
			txtUserName.setText("");
			txtPassword.setText("");
		}
		if(ae.getSource() == btnLogin)
		{
			try {
				DialUpFrame.objCIB.sendCmd(new TransportPacketBean("CMD","$LOGIN",null,
						txtUserName.getText(),EncodeDecode.encode(txtPassword.getText()),"$LOGIN"));
				strServerResp = (TransportPacketBean)DialUpFrame.objCIB.receiveCmd();
				
				if("$LOGIN_OK".equals(strServerResp.getStrCommand())) {
					po = Integer.parseInt(strServerResp.getStrMsgBody());
					Socket socData = new Socket(DialUpFrame.serv, po);
					DialUpFrame.objCIB.setSocData(socData);
					DialUpFrame.objCIB.setStrClientName(txtUserName.getText());
					FriendListFrame frndList = new FriendListFrame(txtUserName.getText());
					DialUpFrame.objFrndList = frndList;
					frndList.setVisible(true);
					frndList.setResizable(false);
					this.dispose();
				}
				else {
					lblStatusMsg.setText(strServerResp.getStrMsgBody());
				}
			} catch(Exception E) {
				lblStatusMsg.setText(E.getMessage());
				E.printStackTrace();
			}
		}
	}
}
