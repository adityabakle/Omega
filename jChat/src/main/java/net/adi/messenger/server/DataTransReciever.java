package net.adi.messenger.server;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

import net.adi.messenger.common.ClientInfoBean;
import net.adi.messenger.common.TransportPacketBean;

/**
 * This class is a thread that listens to the client connected at a specific port for data. 
 * This class get the data packet over the data stream and sends the packet 
 * to the destination user provided in the {@link TransportPacketBean#strToUser} attribute of the object
 *  
 * @author Aditya Bakle
 * @version 1.0.0
 */
public class DataTransReciever implements Runnable 
{
	Thread thrMsgTR;
	private ClientInfoBean objCB;
	
	public DataTransReciever (ClientInfoBean p_objClient)
	{
		thrMsgTR = new Thread(this);
		this.objCB = p_objClient;
		MainFrame.outputToServerCansole("dataTransReciver Thread Started");
		thrMsgTR.start();
	}
	public void run()
	{
		while(true)	{
			try	{
				TransportPacketBean objMpbData = (TransportPacketBean) objCB.receiveData();
				if(null != objMpbData) {
					boolean bQuit = processData(objMpbData);
					if(bQuit) {
						break;
					}
				}
				
			} catch (SocketException se) {
				MainFrame.outputToServerCansole("Socket Status for " + objCB.getStrClientName()+ " isClosed? : "+objCB.getSocData());
				break;
			}catch(EOFException EOF) {
				MainFrame.outputToServerCansole("Connection terminated by Use: " + objCB.getStrClientName());
				//EOF.printStackTrace();
				break;
			}
			catch(Exception E) {
				MainFrame.outputToServerCansole("Exception " + objCB.getStrClientName() +" : "+ E);
				E.printStackTrace();
				break;
				
			}
		}
	}
	
	private boolean processData(TransportPacketBean p_objMpbData)
	{
		if("$CHAT_MSG".equals(p_objMpbData.getStrCommand())){
			sendMessageTo(p_objMpbData);
		}
		return false;
	}
	
	private void sendMessageTo(TransportPacketBean p_objMpbData) {
		try {
			MainFrame.getClientInfoBean(p_objMpbData.getStrToUser()).sendData(p_objMpbData);
		} catch (IOException e) {
			MainFrame.outputToServerCansole("Error writing to client :"+p_objMpbData.getStrToUser());
			e.printStackTrace();
		}
	}
}