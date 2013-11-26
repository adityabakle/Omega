package net.adi.messenger.server;

import java.io.IOException;
import java.net.*;
import java.nio.channels.Selector;

/**
 * This class accepts connection to server and creates a child thread of class {@link ClientProcessor}
 * to process the client further.
 * 
 * @author Aditya Bakle
 * @version 1.0.0
 */
public class ConnectionHandler extends Thread
{
	private Thread objBase;
	private static int i=1;
	private static ServerSocket serverCmdSocket;
	public static ServerSocket serverDataSocket;
	public ConnectionHandler(Selector acceptSelector)
	{
		objBase = new Thread(this);
		objBase.start();
	}
	public void init(){
		
	}

	public void run()
	{
		if (MainFrame.getICmdPort() < 1024)
		{
			System.out.println("Server Binded to port = 2112 (default)");
			MainFrame.setICmdPort(2112);
			MainFrame.setIDataPort(3113);
		}
		else
		{
			try {
				serverCmdSocket = new ServerSocket(MainFrame.getICmdPort());
				serverDataSocket = new ServerSocket(MainFrame.getIDataPort());
			}catch (Exception e){
					MainFrame.outputToServerCansole(e.toString());
					e.printStackTrace();
	          }
				MainFrame.outputToServerCansole("Server waiting for client on port " + serverCmdSocket.getLocalPort());
	            while(i<15) 
	            {
	            	Socket socClient = null;
					try {
						socClient = serverCmdSocket.accept();
					}catch(SocketException se){
						MainFrame.outputToServerCansole("Server Command Socket Closed");
						break;
					}
					catch (IOException e) {
						e.printStackTrace();
						break;
					}
					if(socClient!=null) {
						MainFrame.outputToServerCansole("New connection accepted " + socClient.getInetAddress() + ":" + socClient.getPort());
		                new ClientProcessor(socClient);
		                i++;
					}
	            	
	            } 
		}
	} //bind
	
	/**
	 * This method closes the {@link ServerSocket} for command and data. 
	 * @throws IOException This exception is thrown if method is unable to close the {@link ServerSocket}
	 */
	private static void closeServerSockets() throws IOException {
		if(serverCmdSocket != null && !serverCmdSocket.isClosed()){
			serverCmdSocket.close();
		}
		if(serverDataSocket != null && !serverDataSocket.isClosed()){
			serverDataSocket.close();
		}
	}
	
	/**
	 * This method invokes private method {@link closeServerSockets()}  
	 * @throws IOException This exception is thrown if method is unable to close the {@link ServerSocket}
	 */
	public void close() throws IOException{
		closeServerSockets();
	}
	
}
