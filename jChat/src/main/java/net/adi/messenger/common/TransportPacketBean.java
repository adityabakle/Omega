package net.adi.messenger.common;

import java.io.Serializable;
import java.util.Vector;

/**
 * This class represents the data and command packet shared across the client and Server.
 * The packet bean has header which indicates if the message is Command or data
 * <BR>Header with value <code>CMD</code> is send over the Command Stream where as the. 
 * This packet is usually for registering, login, online User List, logout and sharing commands
 * between Server and Client.  
 * 
 * <BR>Header with value <code>MSG</code> is sent over the Data Stream.
 * This packet is used for sending Chat message from one client to other client via server.    
 * 
 * @author Aditya Bakle
 * @version 1.0.0
 */
public class TransportPacketBean implements Serializable {
	private static final long serialVersionUID = 2750556815539083837L;
	private String strHeader;
	private String strCommand;
	private String strToUser;
	private String strFromUser;
	private String strMsgBody;
	private String strFooter;
	private Vector<Object> vData;
	
	public TransportPacketBean(String strHeader, String strCommand,
			String strToUser, String strFromUser, String strMsgBody,
			String strFooter) {
		super();
		this.strHeader = strHeader;
		this.strCommand = strCommand;
		this.strToUser = strToUser;
		this.strFromUser = strFromUser;
		this.strMsgBody = strMsgBody;
		this.strFooter = strFooter;
	}
	
	public TransportPacketBean(String strHeader, String strCommand,
			String strToUser, String strFromUser, String strMsgBody,
			String strFooter, Vector<Object> vData) {
		super();
		this.strHeader = strHeader;
		this.strCommand = strCommand;
		this.strToUser = strToUser;
		this.strFromUser = strFromUser;
		this.strMsgBody = strMsgBody;
		this.strFooter = strFooter;
		this.vData = vData;
	}
	
	public String getStrHeader() {
		return strHeader;
	}
	public void setStrHeader(String strHeader) {
		this.strHeader = strHeader;
	}
	public String getStrCommand() {
		return strCommand;
	}
	public void setStrCommand(String strCommand) {
		this.strCommand = strCommand;
	}
	public String getStrToUser() {
		return strToUser;
	}
	public void setStrToUser(String strToUser) {
		this.strToUser = strToUser;
	}
	public String getStrFromUser() {
		return strFromUser;
	}
	public void setStrFromUser(String strFromUser) {
		this.strFromUser = strFromUser;
	}
	public String getStrMsgBody() {
		return strMsgBody;
	}
	public void setStrMsgBody(String strMsgBody) {
		this.strMsgBody = strMsgBody;
	}
	public String getStrFooter() {
		return strFooter;
	}
	public void setStrFooter(String strFooter) {
		this.strFooter = strFooter;
	}

	public Vector<Object> getVData() {
		return vData;
	}

	public void setVData(Vector<Object> data) {
		vData = data;
	}
	
	public String toString() {
		StringBuffer strObjValues = new StringBuffer();
		strObjValues.append("\n strHeader : "+strHeader);
		strObjValues.append("\n strCommand : "+strCommand);
		strObjValues.append("\n strToUser : "+strToUser);
		strObjValues.append("\n strFromUser : "+strFromUser);
		strObjValues.append("\n strMsgBody : "+strMsgBody);
		return strObjValues.toString();		
	}
}
