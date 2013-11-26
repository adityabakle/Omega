package net.adi.messenger.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.StyledDocument;

public class ChatWindowFrame extends JFrame implements ActionListener,TextListener 
{
	private static final long serialVersionUID = 6636614206587413820L;
	private static JTextArea jtxtAreaChat;
	JTextField jTxtMsg;
	String strUserName;
	String strFriend;
	StyledDocument chatDoc;
	
	public ChatWindowFrame(String p_strUsr,String p_strFriend)
	{
		super(p_strUsr+" <=> "+p_strFriend);
		setSize(400,300);
		setLayout(null);
		setResizable(true);
		strUserName = p_strUsr;
		strFriend = p_strFriend;
		jTxtMsg=new JTextField("");
		jTxtMsg.setBounds(5,240,395,35);
		jTxtMsg.addActionListener(this);
		
		jtxtAreaChat = new JTextArea();
		jtxtAreaChat.setEditable(false);
		JScrollPane editorScrollPane = new JScrollPane(jtxtAreaChat);
		editorScrollPane.setVerticalScrollBarPolicy(
		                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editorScrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		editorScrollPane.setBounds(5,5,395,230);
		
		JPanel content = new JPanel();
        content.setLayout(null);
        content.add(jTxtMsg);
        content.add(editorScrollPane);
        this.setContentPane(content);
        //this.setSize(400, 300);
        this.pack();
		addWindowListener(new WindowAdapter () {
			public void windowClosing (WindowEvent e) {
				setVisible(false);
			}
		} );
	}

	public void textValueChanged(TextEvent TE)
	{
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent TE)
	{
		addText(strUserName +": "+ jTxtMsg.getText());
		try {
			DialUpFrame.objCIB.sendChatMsg(jTxtMsg.getText(),strFriend);
		} catch (IOException e) {
			jtxtAreaChat.append("Error Sending Msg : "+ e);
			e.printStackTrace();
		}
		jTxtMsg.setText("");
	}
	
	public void addText(String p_strText) {
		jtxtAreaChat.append("\n" + p_strText);
	}

	public JTextField getJTxtMsg() {
		return jTxtMsg;
	}
	
	public String getStrUserName() {
		return strUserName;
	}

	public String getStrFriend() {
		return strFriend;
	}
}
