package net.adi.messenger.client;

import java.net.SocketException;

import net.adi.messenger.common.TransportPacketBean;

public class MessageReceiver extends Thread {
	Thread t;
	public MessageReceiver()
	{
		t = new Thread(this);
		t.start();
	}
	public void run()
	{
		while(true){
			try {
				TransportPacketBean objTpb = (TransportPacketBean) DialUpFrame.objCIB.receiveData();
				processPacket(objTpb);
			} catch(SocketException E) {
				break;
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void processPacket(TransportPacketBean p_objTpb) {
		
		
		if("$CHAT_MSG".equals(p_objTpb.getStrCommand())) {
			String frmUsr = p_objTpb.getStrFromUser();
			ChatWindowFrame objCW = DialUpFrame.objCIB.getHmChatWindows().get(frmUsr);
			objCW.addText(frmUsr +": "+ p_objTpb.getStrMsgBody());
			if(!objCW.isVisible()) { 
				objCW.setVisible(true);
			}
		}
		else {
			System.out.println("Unable to process packet : "+p_objTpb.getStrCommand());
		}
	}
}
