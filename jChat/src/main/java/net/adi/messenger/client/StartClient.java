package net.adi.messenger.client;
/**
 * This is the main Class to initialize client which loads IP Dial Up window.
 * @author Aditya Bakle
 * @version 1.0.0 
 */
public class StartClient {
	public static void main(String args[])
	{ 
		DialUpFrame frmDial = new DialUpFrame();
		frmDial.setResizable(false);
		frmDial.setLocation(400, 300);
		frmDial.setVisible(true);
	}
}

