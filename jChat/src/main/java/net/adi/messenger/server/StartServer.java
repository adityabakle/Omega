package net.adi.messenger.server;

/**
 * This is the main Class to initialize Server which loads the Server UI.
 * @author Aditya Bakle
 * @version 1.0.0 
 */
public class StartServer 
{
	/**
	 * This fields holds the initial memory before starting the server.
	 */
	public static long lngStart = Runtime.getRuntime().totalMemory();
	public static void main(String[] arg)
    {
		try {
			System.out.println("Initializing Chatting Server... : " + lngStart);
			System.out.println("Default binding port no : 2112.");
	        MainFrame MF = new MainFrame();
	        MF.setLocation(200,100);
	        MF.setVisible(true);
		} catch (Exception E){
			System.out.println("Error : " + E);
		}
    }

}
