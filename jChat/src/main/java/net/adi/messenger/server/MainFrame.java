package net.adi.messenger.server;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;

import net.adi.messenger.common.ClientInfoBean;
import net.adi.messenger.common.TransportPacketBean;


/**
 * This is the Class loads the Server UI and is extended to JFrame.
 * The User Interface provided with the input for Server Port. 
 * <code> Default :2112</code>
 * 
 * Interface also displays list of Users connected to the server and an output pane to display server running status. 
 * Server can send message to individual user using the <code>IM To...</code> button or Broadcast message to all user.
 * Class provides major API for Server Level operation.
 * 
 * @author Aditya Bakle
 * @version 1.0.0
 *   
 */

public class MainFrame extends JFrame implements ActionListener,MouseListener
{	
	private static final long serialVersionUID = -1365784587152700199L;
	private static JLabel jlPort,jlUserList,lblMemoryStatus;
	private static JTextField jtxtPort,jtxtBroadCast;
    private static JButton btnStart,btnSendMsg,btnBroadCast;
    private static JScrollPane txtAreaScroll,listUserScroll;
    private static JList listUser;
    private static DefaultListModel listModel;
    private static JTextArea txtOutput;
    private static Vector <ClientInfoBean> vClients;
    private static int iCmdPort, iClientCount,iDataPort;
    private static ConnectionHandler thrConn;
    private static File fileData;
    
	
    /**
     *  Default constructor for the main server UI frame.
     */
    public MainFrame() {
		super("@Di Chatting Server");
		this.setSize(670,750);
		this.setResizable(true);
		addWindowListener(new WindowAdapter ()
							{
								public void windowClosing (WindowEvent e){
									try {
										closeOpenStreams();
									} catch (IOException e1) {
										e1.printStackTrace();
									} catch (Exception exp) {
										exp.printStackTrace();
									}
									dispose();
									System.out.println("Shutting down server : "+ (StartServer.lngStart - Runtime.getRuntime().freeMemory()));
									setAllNull();
									System.exit(0); 
								} 
							} );
		lblMemoryStatus = new JLabel("Memory Used : " + (StartServer.lngStart - Runtime.getRuntime().freeMemory()));
        lblMemoryStatus.setBounds(0,695,662,20);
        lblMemoryStatus.setBorder(BorderFactory.createLineBorder(Color.black));
        lblMemoryStatus.addMouseListener(this);
        //add(lblMemoryStatus);
		
		jlPort = new JLabel("Server Port:");
        //add(jlPort);
        jlPort.setBounds(10,10,100,20);

        jtxtPort = new JTextField("2112");
        //add(jtxtPort);
        jtxtPort.setBounds(85,10,40,20);

        btnStart=new JButton("Start");
        //add(btnStart);
        btnStart.setBounds(130,10,70,20);
        btnStart.addActionListener(this);

        txtOutput=new JTextArea("@Di Chatting Server... Launched ",25,50);
        //txtOutput.setBounds(10,40,460,610);
        txtOutput.setWrapStyleWord(true);
        txtOutput.setEditable(false);
        txtOutput.setBackground(new Color(0,0,0));
        txtOutput.setForeground(new Color(255,255,255));
        txtOutput.addMouseListener(this);
        txtAreaScroll = new JScrollPane(txtOutput);
        txtAreaScroll.setBounds(10,40,450,615);
        txtAreaScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        //add(txtAreaScroll);
        

        jlUserList = new JLabel("Online Users :");
        //add(jlUserList);
        jlUserList.setBounds(470,10,160,20);
        
        listModel = new DefaultListModel();
        listUser = new JList(listModel);
        listUser.setVisibleRowCount(25);
        listUser.setBorder(BorderFactory.createLineBorder(Color.black));
        listUser.setAutoscrolls(true);
        //add(listUser);
        listUser.addMouseListener(this);
        //listUser.setBounds(470,40,180,615);
        listUserScroll = new JScrollPane(listUser);
        listUserScroll.setBounds(470,40,180,615);
        listUserScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
        
        jtxtBroadCast=new JTextField("Message.");
        //add(jtxtBroadCast);
        jtxtBroadCast.addMouseListener(this);
        jtxtBroadCast.setBounds(10,665,300,20);
        jtxtBroadCast.addMouseListener(this);
        
        btnBroadCast=new JButton("Broadcast");
        //add(btnBroadCast);
        btnBroadCast.setBounds(315,665,95,20);
        btnBroadCast.addActionListener(this);

        btnSendMsg = new JButton("IM To...");
        //add(btnSendMsg);
        btnSendMsg.setBounds(560,665,90,20);
        btnSendMsg.setVisible(false);
        btnSendMsg.addActionListener(this);
        
        JPanel content = new JPanel();
        content.setLayout(null);
        content.add(jlPort);
        content.add(jtxtPort);
        content.add(btnStart);
        content.add(txtAreaScroll);
        content.add(listUserScroll);
        content.add(jlUserList);
        content.add(btnSendMsg);
        content.add(jtxtBroadCast);
        content.add(btnBroadCast);
        content.add(lblMemoryStatus);
        this.setContentPane(content);
        
        _MemoryStatus(true);
	}
    
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		_MemoryStatus();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
		_MemoryStatus();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
		_MemoryStatus();	
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) { }

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent AE) {
		// Start Button 
		
		if(AE.getSource()== btnStart) {
			Selector acceptSelector = null;
			listModel.addElement("[ADMIN]");
			vClients = new Vector<ClientInfoBean>();
			
			try {
				addClient(new ClientInfoBean("[ADMIN]",null,null));
				iCmdPort = Integer.parseInt(jtxtPort.getText());
				acceptSelector = Selector.open();
			} catch(Exception E){
				outputToServerCansole("Error initializing list "+ E);
				E.printStackTrace();
			}
			
			iDataPort = iCmdPort + 1001;
			btnStart.setVisible(false);
			jtxtPort.setEnabled(false);
			
			thrConn = new ConnectionHandler(acceptSelector);
			outputToServerCansole("Server listening at port : " + iCmdPort);
			if(listModel.size()>0) {
				btnSendMsg.setVisible(true);
				
				fileData = new File("Data.dat");
		 		if(!fileData.exists()){
		 			try {
		 				fileData.createNewFile();
		 				outputToServerCansole("Server created data file at : " + fileData.getAbsolutePath());
					} catch (IOException e) {
						e.printStackTrace();
					}
		 		}
			}
		}
		
		// IM To.. Button
		if(AE.getSource() == btnSendMsg) {
			String strMsg = JOptionPane.showInputDialog("Enter the Message:");
			if(!(strMsg == null)){
				if(listUser.getSelectedIndex() > 0){
					outputToServerCansole("Message : " + strMsg + " : to " + listModel.get(listUser.getSelectedIndex()));
					send_msg(strMsg,listModel.get(listUser.getSelectedIndex()).toString());
				}
				else{
					JOptionPane.showMessageDialog(this,"No Client Selected","Alert",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		
		// Broadcast button
		if(AE.getSource()== btnBroadCast){
			//for(int t=0;t<listUser.getItemCount();t++){
				send_msg("BROADCAST : " + jtxtBroadCast.getText(),"$BCAST");
			//}
		}
	}

	/**
	 * This method is for sending message to user connected to the server. 
	 * This same API can be used for sending message to a single user or to Broadcast. 
	 * This is based on the value of the parameter <code>p_strClient</code>.
	 * 
	 * @param p_strMsg 	The string to be sent/Broadcasted to the user(s) from server. 
	 * @param p_strClient The string representing User Id to whom the message has to be sent.
	 * 					  In-case of broadcast the value to this parameter is <code>$BCAST</code>. 	 
	 */
	public void send_msg(String p_strMsg,String p_strClient) {
		try {
			String strCmd;
			if(null != p_strClient && !p_strClient.equals("[MAD_MAN]")) {
				for(Object objClinet : vClients){
					ClientInfoBean objCB = (ClientInfoBean)objClinet;
					if(objCB.getStrClientName().equals("[MAD_MAN]") 
							|| (!"$BCAST".equals(p_strClient) 
									&& !p_strClient.equals(objCB.getStrClientName()))) {
						continue;
					}
					if("$BCAST".equals(p_strClient)){
						strCmd = p_strClient;
					} else {
						strCmd = "$SERVER_MSG";
					}
					
					objCB.sendCmd(new TransportPacketBean("CMD",strCmd,objCB.getStrClientName(),
							"SERVER",p_strMsg,strCmd) );
		            if(!"$BCAST".equals(p_strClient) && p_strClient.equals(objCB.getStrClientName())) {
		            	break;
		            }
				}	
			}
		    else
		    	JOptionPane.showMessageDialog(this,p_strMsg,"Server Message",JOptionPane.INFORMATION_MESSAGE);
			
		} catch(Exception objExp) {
			outputToServerCansole("Excemption sending message : "+ objExp);
		}
		
		_MemoryStatus();
	}
	
	
	/**
	 * This method refreshes the online User List on Server UI.
	 */
	private static void refreshList() {
		listModel.removeAllElements();
		for(ClientInfoBean objCB : vClients ) {
			listModel.addElement(objCB.getStrClientName());
		}
		listModel.trimToSize();
		_MemoryStatus();
	}
	
	/**
	 * This method adds a newly created client connection information to vector.
	 * This method after adding the client refreshes the list and also broadcast the updated list to online users.
	 * @param p_objClient This is the object of class {@link ClientInfoBean} having client information.
	 */
	public static void addClient(ClientInfoBean p_objClient) {
		vClients.add(p_objClient);
		setIClientCount(vClients.size());
		refreshList();
		broadastUserList();
		_MemoryStatus();
	}
	
	/**
	 * This methods removes the Client info object from the vector.
	 * This method after removing the client refreshes the list and also broadcast the updated list to online users.
	 * @param p_objClient Client info object to be removed.
	 */
	public static void  removeClient(ClientInfoBean p_objClient)	{
		for(ClientInfoBean objCB : vClients ) {
			if(objCB.getStrClientName().equals(p_objClient.getStrClientName())){
				vClients.remove(objCB);
				break;
			}
		}
		setIClientCount(vClients.size());
		refreshList();
		broadastUserList();
		_MemoryStatus();
	}
	
	
	/**
	 * This method is invoked when Server is closed. 
	 * All open sockets are closed before server is shut down.
	 * @throws IOException Exception thrown while closing the socket.
	 */
	private static void closeOpenStreams() throws IOException {
		_MemoryStatus();
		outputToServerCansole("Executing command $CLOSE.");
		if(vClients != null){
			for(ClientInfoBean objCB : vClients ) {
				try {
					if(objCB.getSocCommand()!= null) {
						objCB.getSocCommand().close();
					}
					if(objCB.getSocData()!= null){
						objCB.getSocData().close();
					}
				} catch (IOException e) {
					outputToServerCansole("Unable to terminate connection with user : "+objCB.getStrClientName());
					outputToServerCansole("Error : " + e.getMessage());
					e.printStackTrace();
				}catch (Exception e) {
					outputToServerCansole("Unknown Exception : "+objCB.getStrClientName());
					outputToServerCansole("Exp : " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
		if(thrConn != null) {
			thrConn.close();
		}
		vClients.removeAllElements();
		outputToServerCansole("$CLOSE Done.");
		outputToServerCansole("Memory Used : " + (StartServer.lngStart - Runtime.getRuntime().freeMemory()));
		_MemoryStatus();
	}
	
	/**
	 * This method outputs the text to the Server console on UI.
	 * @param p_strTxt String to be displayed on the server UI console. 
	 */
	public static void outputToServerCansole(String p_strTxt) {
		txtOutput.append("\n"+p_strTxt);
		try {
			txtOutput.select(txtOutput.getLineStartOffset(txtOutput.getLineCount()-1), txtOutput.getLineStartOffset(txtOutput.getLineCount()-1));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		_MemoryStatus();
	}
	public static int getICmdPort() {
		return iCmdPort;
	}
	public static void setICmdPort(int cmdPort) {
		iCmdPort = cmdPort;
	}
	public static int getIDataPort() {
		return iDataPort;
	}
	public static void setIDataPort(int dataPort) {
		iDataPort = dataPort;
	}
	public static int getIClientCount() {
		return iClientCount;
	}
	public static void setIClientCount(int clientCount) {
		iClientCount = clientCount;
	}
	
	
	/**
	 * @param p_strUserName string user name for which {@link ClientInfoBean} object is required.
	 * @return {@link ClientInfoBean} object for the given user name.
	 */
	public static ClientInfoBean getClientInfoBean(String p_strUserName) {
		_MemoryStatus();
		if(vClients != null){
			for(ClientInfoBean objCB : vClients ) {
				if(objCB.getStrClientName().equals(p_strUserName)){
					return objCB;
				}
			}
		}
		return null;
	}
	
	/**
	 * This method broadcast the online user list available on server to all the connected user.
	 */
	private static void broadastUserList() {
		_MemoryStatus();
		Vector<Object> vUserList = new Vector<Object>();
		for(ClientInfoBean objCBList : vClients){
        	vUserList.add(objCBList.getStrClientName());
        }
		outputToServerCansole("Broad Casting updated List to Users. ");
		for(ClientInfoBean objCB : vClients ) {
			try	{
				if(objCB.getSocCommand()!= null) {
	                objCB.sendCmd(new TransportPacketBean("CMD", "$OUL", null, 
	                		"SERVER", null, "$OUL", vUserList));
				}
			} catch(Exception objExp) {
				System.out.println("Exception in broadcasting : " + objExp);
			}
		}
		_MemoryStatus();
	}
	
	public static void _MemoryStatus(){
		lblMemoryStatus.setText(" Memory Used : " + (StartServer.lngStart - Runtime.getRuntime().freeMemory()) + " bytes");
	}
	
	public static void _MemoryStatus(boolean p_boolean){
		if(p_boolean)
			outputToServerCansole(" Memory Used : " + (StartServer.lngStart - Runtime.getRuntime().freeMemory()) + " bytes");
		_MemoryStatus();
	}
	
	public void setAllNull(){
		MainFrame.vClients = null;
		MainFrame.thrConn = null;
		MainFrame.fileData = null;
	}
	public static File getFileData() {
		return fileData;
	}
	public static void setFileData(File fileData) {
		MainFrame.fileData = fileData;
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		
	}
	
}
