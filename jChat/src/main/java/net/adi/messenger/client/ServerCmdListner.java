package net.adi.messenger.client;

import java.io.IOException;
import java.net.SocketException;

import net.adi.messenger.common.TransportPacketBean;

public class ServerCmdListner implements Runnable 
{
	Thread thSvrCmdLstn;
	TransportPacketBean objTpb;
	ServerCmdListner()
	{
		thSvrCmdLstn = new Thread(this);
		thSvrCmdLstn.start();
	}
	public void run()
	{
		while(true) {
			try {
				objTpb = (TransportPacketBean)DialUpFrame.objCIB.receiveCmd();
				if(objTpb != null) {
					processPacket(objTpb);
				}
				
			} catch (SocketException se) {
				break;
			} catch(IOException E) {
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void processPacket(TransportPacketBean p_objTpb) throws IOException
	{
		if(p_objTpb.getStrCommand().equals("$OUL")) {
			DialUpFrame.objFrndList.flushUserList();
			try {
				FriendListFrame.vUserList = p_objTpb.getVData();
				FriendListFrame.refreshList();
			} catch(Exception E) { 
				System.out.println("Error updating User List "+p_objTpb);
				E.printStackTrace();
			}
		}
		else if(p_objTpb.getStrCommand().equals("$RQT_CHAT"))
		{
			try {
				String strUserId = p_objTpb.getStrToUser();
				String strFriend = p_objTpb.getStrFromUser();
				ChatWindowFrame objCW = new ChatWindowFrame(strUserId,strFriend);
				DialUpFrame.objCIB.addToHmChatWindows(strFriend, objCW);
				//objCW.setVisible(true);
			} catch(Exception E) {
				System.out.println("Error initiating Chat. " + E);
				E.printStackTrace();
			}
		}
		else if(p_objTpb.getStrCommand().equals("$SERVER_MSG") 
				|| p_objTpb.getStrCommand().equals("$BCAST") ) {
			try {
				String msgs = objTpb.getStrMsgBody();
				DialUpFrame.objFrndList.popUpMsg(msgs,objTpb.getStrCommand());
			}
			catch(Exception E) { 
				System.out.println("Error in Reading List by client");
				E.printStackTrace();
			}    
		}
		else if("$EXIT".equals(p_objTpb.getStrCommand())){
			//DialUpFrame.objFrndList.popUpMsg("Connection terminated from Server as you have logged on different terminal.", "$SERVER_MSG");
			DialUpFrame.objCIB.close(true);
			DialUpFrame.objFrndList.logout();
		}
		else {
			System.out.println("Unprocessed Cmd : "+p_objTpb);
		}
	}
	public void refreshChatWindowHashMap()
	{
		// Remove the Chat window object from the Hashmap list and dispose if required.
	}
}