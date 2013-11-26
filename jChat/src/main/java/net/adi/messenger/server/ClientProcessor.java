package net.adi.messenger.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import net.adi.messenger.common.ClientInfoBean;
import net.adi.messenger.common.EncodeDecode;
import net.adi.messenger.common.TransportPacketBean;

/**
 * This class deal with all client handling on server. This sends/receives 
 * commands to the client it is processing. This class is also responsible for authenticating users. 
 * 
 * @author Aditya Bakle
 * @version 1.0.0
 */
public class ClientProcessor implements Runnable 
{
	private Thread objThread;
	private Socket socetClient;
	String strUserID = "Anonymous", strPwd;
	ClientInfoBean objCB;
	
	public ClientProcessor(Socket p_socClient)
	{
		this.objThread = new Thread(this);
		this.socetClient = p_socClient;
		this.objThread.start();
	}
	public void run()
	{
		try {
			objCB = new ClientInfoBean(null,socetClient,socetClient.getInetAddress());
		} catch (IOException e1) {
			MainFrame.outputToServerCansole("Error creating ClientInfoBean object.");
			e1.printStackTrace();
		}
		
		int check = 4;
		
		while(check!=3) {
			check = authenticateUser();
			if(check==4)
				break;
		}
		
		if (check == 3) {
			try {
				Socket socData = ConnectionHandler.serverDataSocket.accept();
				objCB.setSocData(socData);
				new DataTransReciever(objCB);
			} catch (IOException e) {
				MainFrame.outputToServerCansole("Error Creating Data Socketfor : " + objCB.getStrClientName());
				e.printStackTrace();
			}
			objCB.setStrClientName(strUserID);
			//objCB.addThread(objThread);
			MainFrame.addClient(objCB);
			MainFrame.outputToServerCansole("Connected User : " + objCB.getStrClientName() + " @ " + objCB.getSocCommand());
			int i=1;
			try
			{
				while(i==1) {
					TransportPacketBean objTpb = (TransportPacketBean)objCB.receiveCmd();
					MainFrame.outputToServerCansole("CMD :"+ objTpb.getStrHeader());
					i = processCommand(objTpb);
					if(i==0)
					{
						objCB.close(true);
					}
				}
			} catch (EOFException eof){
				System.out.println("Connection Terminated by the client.");
			}
			catch(IOException E){
				System.out.println("Error in Reading Client Request" + E);
				E.printStackTrace();
			} catch (ClassNotFoundException cnfe) {
				System.out.println("Class Not found Exp :" + cnfe);
				cnfe.printStackTrace();
			}
			MainFrame.outputToServerCansole("User Logged Out : "+objCB.getStrClientName());
			MainFrame.removeClient(objCB);
		}	
		else
		{
			MainFrame.outputToServerCansole("Connection terminated before login : " + socetClient.getInetAddress());
			try {
				objCB.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private int processCommand(TransportPacketBean p_objTpb)
	{
		if(p_objTpb.getStrCommand().equals("$SIGNOUT")) {
			return 0;
		}	
		else if(p_objTpb.getStrCommand().equals("$INIT_CHAT")) // initialize Chat with a friend
		{
			String strFriendID="";
			try {
				strFriendID = p_objTpb.getStrToUser();
				ClientInfoBean objFriendCIB = MainFrame.getClientInfoBean(strFriendID);
				if(objFriendCIB!=null){
					p_objTpb.setStrCommand("$RQT_CHAT");
					p_objTpb.setStrFooter("$RQT_CHAT");
				} else {
					p_objTpb.setStrCommand("$END_CHAT");
					p_objTpb.setStrFooter("$END_CHAT");
					p_objTpb.setStrMsgBody("Your Friend \"" + strFriendID + "\" is no more online.");
				}
				objFriendCIB.sendCmd(p_objTpb);
			}catch(Exception Exp){
				MainFrame.outputToServerCansole("Error Reading Request : " + Exp);
				Exp.printStackTrace();
			}
			return 1;
		}
		else if("$EXIT".equals(p_objTpb.getStrCommand())){
			return 0;
		}
		else {
			System.out.println(" Unprocessed Cmd : "+p_objTpb.getStrCommand());
			return 1;
		}
	}

	public int authenticateUser()
	{
		/*
		 * 0 - User not in file. 
		 * 1 - User in file.  
		 * 2 - User in file but password mismatch
		 * 3 - User in file Password match. 
		 * 4 - Connection Terminated before login.
		 * */
		int userStatus = 0;  
		TransportPacketBean objTpb = null;
		DataInputStream rdFile = null;
		try {
			objTpb = (TransportPacketBean) objCB.receiveCmd();
			
			if(objTpb.getStrCommand().equals("$EXIT")){
				userStatus = 4;
				return userStatus;
			}
			
			strUserID = objTpb.getStrFromUser();
			strPwd = objTpb.getStrMsgBody();
			MainFrame.outputToServerCansole(" Authenticating user - " + strUserID);
			rdFile = new DataInputStream(new FileInputStream(MainFrame.getFileData()));
			String line = null;
			userStatus = 2;
			while((line = rdFile.readUTF())!=null) {
	        	
	        		if(strUserID.equals(line)) {
		        		userStatus = 1;
		        		if(objTpb.getStrCommand().equals("$SIGNUP")){
		        			break;
		        		}
		        		
		        		else {
		        			String encUsrPwd = EncodeDecode.encodePassword(strPwd, strUserID);
		        			line = rdFile.readUTF(); // read pwd from file.
			        			if(encUsrPwd.equals(line)) {
			        			userStatus = 3;
			                	break;
			                } 
		        		}
		        	} 
	        }
		} catch(EOFException objEOF){
			userStatus = 0;
		} catch(Exception objExp) {
			MainFrame.outputToServerCansole(" Error Reading file - " + strUserID +" : " + objExp);
			objExp.printStackTrace();
		}finally {
			if(null!= rdFile)
				try {
					rdFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					MainFrame.outputToServerCansole("Error closing user file.: "+e);
				}
		}
		
	    if(objTpb.getStrCommand().equals("$SIGNUP")) {
	    	switch(userStatus){
	    	case 0:
	    		try {
		    		DataOutputStream wrtFile = new DataOutputStream(new FileOutputStream(MainFrame.getFileData(),true));
		    		wrtFile.writeUTF(strUserID);
		    		wrtFile.writeUTF(EncodeDecode.encodePassword(objTpb.getStrMsgBody(),strUserID));
		    		wrtFile.close();
		    		MainFrame.outputToServerCansole("New guest signed up. Name : " + strUserID);
		    		objTpb.setStrCommand("$SIGNUP_OK");
		    		objTpb.setStrMsgBody("User regisered successfully.");
		    	} catch(Exception objExp){
		    		MainFrame.outputToServerCansole(" Error writing file - " + strUserID +" : " + objExp);
		    	}
	    		break;
	    	case 1:
	    		objTpb.setStrCommand("$SIGNUP_FAIL_001"); // User ID Not available try other.
	    		objTpb.setStrMsgBody("UserName already used, please try another.");
	    		break;
	    	}
	    	try {
				objCB.sendCmd(objTpb);
			} catch (IOException e) {
				System.out.println("Error Sending packet : "+objTpb);
				e.printStackTrace();
			}
	    }
	    else if(objTpb.getStrCommand().equals("$LOGIN")) {
	    	switch(userStatus){
	    	case 0:
	    		objTpb.setStrCommand("$LOGIN_FAIL_001"); // User not on file.
	    		objTpb.setStrMsgBody("UserName not registered.");
	    		MainFrame.outputToServerCansole("$LOGIN_FAIL_001:Authentication FAILED for user - " + strUserID);
	    		break;
	    	case 2:
	    		objTpb.setStrCommand("$LOGIN_FAIL_002"); // Invalid password
	    		objTpb.setStrMsgBody("Invalid UserName or password.");
	    		MainFrame.outputToServerCansole("$LOGIN_FAIL_001:Authentication FAILED for user - " + strUserID);
	    		break;
	    	case 3:
	    		MainFrame.outputToServerCansole("User '" + strUserID + "' Authenticated");
	    		objTpb.setStrCommand("$LOGIN_OK"); // User Login success.
	    		objTpb.setStrMsgBody(""+MainFrame.getIDataPort());
	    		checkUserAlreadyLoggedIn(strUserID);
	    		break;
	    	}
	    	try {
				objCB.sendCmd(objTpb);
			} catch (IOException e) {
				System.out.println("Error Sending packet : "+objTpb);
				e.printStackTrace();
			}
	    }
	    
	    return userStatus;
	}
	
	public void checkUserAlreadyLoggedIn (String p_strUserId) {
		if(MainFrame.getClientInfoBean(p_strUserId)!=null){
			try {
				MainFrame.getClientInfoBean(p_strUserId).sendExitPacket("C");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}


