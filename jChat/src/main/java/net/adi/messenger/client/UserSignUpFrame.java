package net.adi.messenger.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import net.adi.messenger.common.EncodeDecode;
import net.adi.messenger.common.TransportPacketBean;


public class UserSignUpFrame extends JFrame implements ActionListener 
{
	private static final long serialVersionUID = -8386906046968276849L;
	JTextField txtUserName;
	JPasswordField txtPassword,txtConfPwd;
	JLabel lblUserName,lblPwd,lblConfPwd,lblStatusMsg;
	JButton b1;

	public UserSignUpFrame()
	{
		super("New User");
		setSize(250,180);
		setLayout(null);
		
		addWindowListener(new WindowAdapter () {
			public void windowClosing (WindowEvent e) {
				dispose();
				//System.exit(0);
			}
		} );
		add(lblUserName=new JLabel("UserName :"));
		lblUserName.setBounds(20,10,80,20);
		add(txtUserName = new JTextField(15));
		txtUserName.setBounds(100,10,110,20);
		add(lblPwd=new JLabel("Password :"));
		lblPwd.setBounds(20,40,80,20);
		add(txtPassword = new JPasswordField(15));
		txtPassword.setBounds(100,40,110,20);
		add(lblConfPwd=new JLabel("Confirm :"));
		lblConfPwd.setBounds(20,70,80,20);
		add(txtConfPwd = new JPasswordField(15));
		txtConfPwd.setBounds(100,70,110,20);
		add(b1 = new JButton("Register"));
		b1.setBounds(130,100,90,20);
		b1.addActionListener(this);
		add(lblStatusMsg=new JLabel("Please Enter the details."));
		lblStatusMsg.setBorder(BorderFactory.createLineBorder(Color.black));
		lblStatusMsg.setBounds(0,125,242,20);
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent AE)
	{
		if(txtUserName.getText().equals("")||txtPassword.getText().equals("")||txtConfPwd.getText().equals("")) {
			lblStatusMsg.setText("All fields are mandatory.");	
		}
		else {
			if(txtConfPwd.getText().equals(txtPassword.getText())) {
				try {
					DialUpFrame.objCIB.sendCmd(new TransportPacketBean("CMD","$SIGNUP",null,
							txtUserName.getText(),EncodeDecode.encode(txtPassword.getText()),"$SIGNUP"));
					TransportPacketBean objTpb = (TransportPacketBean)DialUpFrame.objCIB.receiveCmd();
					if("$SIGNUP_OK".equals(objTpb.getStrCommand())) {
						JOptionPane.showMessageDialog(this,"You are now registered. \n Pleae login with ur new UserName and Password.",
								"SignUp Success",JOptionPane.INFORMATION_MESSAGE);
						dispose();
					}
					lblStatusMsg.setText(objTpb.getStrMsgBody());
				} catch(IOException E) { 
					E.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			else {
				lblStatusMsg.setText("Password & Confirm password not same.");
			}	
		}
	}
}
