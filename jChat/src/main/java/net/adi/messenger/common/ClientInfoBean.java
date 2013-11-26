package net.adi.messenger.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;

import net.adi.messenger.client.ChatWindowFrame;

/**
 * This class is a client bean holding all the client related information 
 * including the Command Socket and Data Socket. The bean also hold the list of 
 * Chat windows open by the client in an hash map with the key as the friends UserID.
 * Command I/O stream and Data I/O stream are also saved in this bean which are used
 * to send and receive {@link TransportPacketBean} object.
 * 
 * @author Aditya Bakle
 * @version 1.0.0
 * 
 */
public class ClientInfoBean 
{
	/**
	 * This fields hold the user Id of the connected client.
	 */
	private String strClientName;
	
	/**
	 * This is the command socket of the client. This is set when user is connected to server(prior login). 
	 */
    private Socket socCommand;
    
    /**
	 * This is the data socket of the client. This is set when user is logged in to server successfully. 
	 */
    private Socket socData;
    
    /**
	 * This field holds the IP address of the Client connected. 
	 */
    private InetAddress intAddrCli;
    
    /**
	 * This is {@link ObjectInputStream} field reading data for client on data {@link Socket} <code>socData</code>. 
	 */
    private ObjectInputStream dataReader;
    
    /**
	 * This is {@link ObjectInputStream} field reading command for client on command {@link Socket} <code>socCommand</code>. 
	 */
    private ObjectInputStream cmdReader;
    
    /**
	 * This is {@link ObjectOutputStream} field writing data for client on data {@link Socket} <code>socData</code>. 
	 */
	private ObjectOutputStream dataWriter;
	
	 /**
	 * This is {@link ObjectOutputStream} field writing command for client on command {@link Socket} <code>socCommand</code>. 
	 */
	private ObjectOutputStream cmdWriter;
	
	/**
	 * This is {@link HashMap} field hold list of all {@link ChatWindowFrame} objects for the client with the key as Friends UserId. 
	 */
	private HashMap<String,ChatWindowFrame> hmChatWindows;
	
    public ClientInfoBean(String p_strName, Socket p_objSocket,InetAddress p_intAddr) throws IOException {
         this.strClientName = p_strName;
         if(p_objSocket != null){
        	 setSocCommand(p_objSocket); 
         }
         this.intAddrCli = p_intAddr;
    }
    
	public String getStrClientName() {
		return strClientName;
	}
	
	public void setStrClientName(String strClientName) {
		this.strClientName = strClientName;
	}
	
	public Socket getSocCommand() {
		return socCommand;
	}
	
	/**
	 * This methods sets Command {@link Socket} created for the client when connected to server. 
	 * In addition is also creates the Input and Output stream for the {@link Socket} over 
	 * which commands are shared across the Server and client. 
	 * <BR>The Streams created are 
	 * <ul>
	 * 	<li>{@link cmdWriter} : for sending command {@link TransportBeanPacket}.</li> 
	 * <li>{@link cmdReader} : for reading command {@link TransportBeanPacket}.</li>
	 * </ul>
	 * 
	 * @param socClient The socket created when client connects to server (prior login).
	 * @throws IOException Exception is thrown while creating the streams for the given {@link Socket}.
	 */
	private void setSocCommand(Socket socClient) throws IOException {
		this.socCommand = socClient;
		cmdWriter = new ObjectOutputStream(this.socCommand.getOutputStream());
		cmdReader = new ObjectInputStream(this.socCommand.getInputStream());
	}
	
	public void setIntAddrCli(InetAddress intAddrCli) {
		this.intAddrCli = intAddrCli;
	}
	
	public InetAddress getIntAddrCli() {
		return intAddrCli;
	}
	
	public ObjectInputStream getCmdReader() {
		return cmdReader;
	}
	
	public ObjectOutputStream getCmdWriter() {
		return cmdWriter;
	}
	
	public void sendCmd(Object p_obj) throws IOException {
		this.cmdWriter.writeObject(p_obj);
	}
	
	public Object receiveCmd() throws IOException, ClassNotFoundException, SocketException{
		return this.cmdReader.readObject();
	}
	
	public Socket getSocData() {
		return socData;
	}
	
	/**
	 * This methods sets Data {@link Socket} created for the client when user login to Server. 
	 * In addition is also creates the Input and Output stream for the {@link Socket} over 
	 * which data (chat messages) are shared from one client to another. 
	 * <BR>The Streams created are 
	 * <ul>
	 * 	<li>{@link dataWriter} : for sending data {@link TransportBeanPacket}.</li> 
	 * <li>{@link dataReader} : for reading data {@link TransportBeanPacket}.</li>
	 * </ul>
	 * 
	 * @param socData The socket created when client login to server.
	 * @throws IOException Exception is thrown while creating the streams for the given {@link Socket}.
	 */
	
	public void setSocData(Socket socData) throws IOException {
		this.socData = socData;
		dataWriter = new ObjectOutputStream(this.socData.getOutputStream());
		dataReader = new ObjectInputStream(this.socData.getInputStream());
		
		hmChatWindows = new HashMap<String, ChatWindowFrame>();
	}
	
	public ObjectInputStream getDataReader() {
		return dataReader;
	}
	
	public ObjectOutputStream getDataWriter() {
		return dataWriter;
	}
	
	public void sendData(Object p_obj) throws IOException {
		this.dataWriter.writeObject(p_obj);
	}
	
	public Object receiveData() throws IOException, ClassNotFoundException {
		return this.dataReader.readObject();
	}
	
	/**
	 * This method closes all the streams associated with the Client object. 
	 * @throws IOException Exception is thrown if streams are not closed properly.
	 */
	public void close() throws IOException {
		if(this.socCommand!=null && !this.socCommand.isClosed()){
			this.socCommand.close();
			this.socCommand = null;
		}
		
		if(this.socData!=null && !this.socData.isClosed()){
			this.socData.close();
			this.socData = null;
		}		
	}
	
	/**
	 * This method overrides the {@link close()} method and called when user Logout. 
	 * This method closes the open chat windows for the given client.
	 * This method also calls the {@link close()} method for closing the stream associated with the client.  
	 * 
	 * @param p_Logout If true then all open chat windows are closed.
	 * @throws IOException Exception is thrown if streams are not closed properly.
	 */
	public void close(boolean p_Logout) throws IOException {
		if(p_Logout){
			if(hmChatWindows != null){
				for(ChatWindowFrame objChatWin :hmChatWindows.values()) {
					objChatWin.dispose();
				}
				hmChatWindows.clear();
				hmChatWindows = null;
			}
		}
		this.close();
	}
	
	
	/**
	 * This methods creates a Exit packet and sends to server over the stream based on the parameter <code>p_strStreamType</code>.
	 * <br> The values for the parameter must be one of the following. 
	 * <ul>
	 * 	<li> <code>C</code> - Specifies that the packet needs to be sent over Command Stream.</li>
	 *  <li> <code>D</code> - Specifies that the packet needs to be sent over data Stream.</li>
	 * </ul>
	 * @param p_strStreamType The parameter holds value for deciding the Stream  over which the packet need to be sent.
	 * @throws IOException is thrown if any issue sending packet over the steam.
	 */
	public void sendExitPacket(String p_strStreamType) throws IOException {
		// Write Exit Packet and send 
		TransportPacketBean objTpb = new TransportPacketBean("CMD", "$EXIT", "SERVER", 
				this.strClientName, "User Logged out or signed on a different terminal.", "$EXIT");
		if("C".equals(p_strStreamType)){
			this.sendCmd(objTpb);
		}
		else if("D".equals(p_strStreamType)){
			this.sendData(objTpb);
		}
	}
	
	/**
	 * This methods enclosed the message and the destination user ID in the {@link TransportPacketBean} 
	 * and send it over the data stream to serve.
	 *  
	 * @param p_strMsg Message string to be enclosed in the packet.
	 * @param p_strUserId User ID to whom the message has to be sent.
	 * @throws IOException is thrown if any issue sending packet over the steam.
	 */
	public void sendChatMsg(String p_strMsg,String p_strUserId) throws IOException {
		TransportPacketBean objTpb = new TransportPacketBean("MSG", "$CHAT_MSG",p_strUserId, 
				this.strClientName,p_strMsg, "$CHAT_MSG");
		this.sendData(objTpb);
	}
	
	public HashMap<String, ChatWindowFrame> getHmChatWindows() {
		return hmChatWindows;
	}

	public void addToHmChatWindows(String p_key, ChatWindowFrame p_objChatWin) {
		this.hmChatWindows.put(p_key, p_objChatWin);
	}
	
	public void removeFromHmChatWindows(String p_key) {
		this.hmChatWindows.remove(p_key);
	}
}
