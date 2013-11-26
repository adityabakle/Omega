package net.adi.messenger.client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import net.adi.messenger.common.TransportPacketBean;

public class FriendListFrame extends JFrame implements ActionListener, MouseListener
{
	private static final long serialVersionUID = 4903034624983415705L;
	JLabel lblGreetings,lblUserList;
	private static JList lstUser;
    private static DefaultListModel listModel;
	String strUsrName;
	JScrollPane listUserScroll;
	ServerCmdListner thSCL;
	public static Vector<Object> vUserList;
	public FriendListFrame(String p_strUsrName)
	{
		super("@Di Chatting Client");
		setSize(200,450);
		setLayout(null);
	
		
		strUsrName = p_strUsrName;
		lblGreetings=new JLabel("Hi! " + strUsrName,SwingConstants.CENTER);
		lblGreetings.setBorder(BorderFactory.createLineBorder(Color.black));
		lblGreetings.setAlignmentY(1);
		//add(lblGreetings);
		lblGreetings.setBounds(5,5,175,20);
		
		lblUserList=new JLabel("Online Users :");
		//add(lblUserList);
		lblUserList.setBounds(5,30,180,20);
		
		listModel = new DefaultListModel();
		lstUser = new JList(listModel);
		lstUser.setBorder(BorderFactory.createLineBorder(Color.black));
		listUserScroll = new JScrollPane(lstUser);
        listUserScroll.setBounds(5,50,180,350);
        listUserScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        lstUser.addMouseListener(this);
        
        JPanel content = new JPanel();
        content.setLayout(null);
        content.add(lblGreetings);
        content.add(lblUserList);
        content.add(listUserScroll);
        
        this.setContentPane(content);
		
        addWindowListener(new WindowAdapter () {
			public void windowClosing (WindowEvent e) {
				try {
					logout();
				} catch(Exception IE) { 
					IE.printStackTrace();
					System.exit(0);
				}
			}
		} );
		
		DialUpFrame.objFrndList = this;
		thSCL = new ServerCmdListner();
		new MessageReceiver();
	}

	public void actionPerformed(ActionEvent AE)
	{
		/*if(listModel.get(lstUser.getSelectedIndex()).equals(strUsrName)) {
			JOptionPane.showMessageDialog(this,"Self-chatting is denied", "Warning",JOptionPane.WARNING_MESSAGE);
		}
		else {
			if(AE.getSource()==lstUser) {
				if(!listModel.get(lstUser.getSelectedIndex()).equals("[ADMIN]")) {
					
					try {
						ChatWindowFrame objCW = DialUpFrame.objCIB.getHmChatWindows().get(listModel.get(lstUser.getSelectedIndex()));
						if(objCW != null){
							objCW.setVisible(true);
						}
						else{
							DialUpFrame.objCIB.sendCmd(new TransportPacketBean("CMD", "$INIT_CHAT", 
									listModel.get(lstUser.getSelectedIndex()).toString(), 
									strUsrName, null, "$INIT_CHAT"));
							objCW = new ChatWindowFrame(strUsrName,
									listModel.get(lstUser.getSelectedIndex()).toString());
							objCW.setVisible(true);
							DialUpFrame.objCIB.addToHmChatWindows(
									listModel.get(lstUser.getSelectedIndex()).toString(), objCW);
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(this,"Unable to send Chat request. \n Error : " + e,"Error",JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
				else
					JOptionPane.showMessageDialog(this,"Default Server User : Access Denied","Warning",JOptionPane.WARNING_MESSAGE);
			}
		}*/
	}
	
	public void popUpMsg(String p_strMsg, String p_strType)
	{
		if("$SERVER_MSG".equals(p_strType)){
			JOptionPane.showMessageDialog(this,p_strMsg,"Server Notification",JOptionPane.PLAIN_MESSAGE);
		}else {
			JOptionPane.showMessageDialog(this,p_strMsg,"Server BroadCast",JOptionPane.PLAIN_MESSAGE);
		}
		
		
	}
		
	public void flushUserList() {
		listModel.removeAllElements();
	}
	
	public void addToUserList(String p_User){
		listModel.addElement(p_User);
	}
	
	public static void refreshList() {
		if(null != vUserList){
			listModel.removeAllElements();
			for(Object objUsr : vUserList ) {
				listModel.addElement(objUsr.toString());
			}
			listModel.trimToSize();
		}
	}
	
	public void logout() {
		try {
			DialUpFrame.objCIB.close(true);
			DialUpFrame.objCIB.sendExitPacket("C");
		} catch (IOException e) {
			e.printStackTrace();
		}
		DialUpFrame.objFrndList.dispose();
		System.exit(0);
	}

	public void mouseClicked(MouseEvent me) {
		if(me.getClickCount()==2){
			if(listModel.get(lstUser.getSelectedIndex()).equals(strUsrName)) {
				JOptionPane.showMessageDialog(this,"Self-chatting is denied", "Warning",JOptionPane.WARNING_MESSAGE);
			}
			else {
				if(!listModel.get(lstUser.getSelectedIndex()).equals("[ADMIN]")) {
					try {
						ChatWindowFrame objCW = DialUpFrame.objCIB.getHmChatWindows().get(listModel.get(lstUser.getSelectedIndex()));
						if(objCW != null) {
							objCW.setVisible(true);
						}
						else {
							DialUpFrame.objCIB.sendCmd(new TransportPacketBean("CMD", "$INIT_CHAT", 
									listModel.get(lstUser.getSelectedIndex()).toString(), 
									strUsrName, null, "$INIT_CHAT"));
							objCW = new ChatWindowFrame(strUsrName,
									listModel.get(lstUser.getSelectedIndex()).toString());
							objCW.setVisible(true);
							DialUpFrame.objCIB.addToHmChatWindows(
									listModel.get(lstUser.getSelectedIndex()).toString(), objCW);
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(this,"Unable to send Chat request. \n Error : " + e,"Error",JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
				else
					JOptionPane.showMessageDialog(this,"Default Server User : Access Denied","Warning",JOptionPane.WARNING_MESSAGE);
			
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		refreshList();
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}
}
