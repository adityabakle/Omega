package usermaster;

import com.cellmania.cmreports.db.CMReportFactory;
import com.cellmania.cmreports.db.ICMReports;
import com.cellmania.cmreports.db.masters.UserMasterDTO;
import com.cellmania.cmreports.db.masters.UserParams;

public class GetUsers {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		UserParams user = new UserParams();
		user.setUserName("cmreports");
		
		ICMReports dao = CMReportFactory.getICMReports();
		
		UserMasterDTO usr = dao.getUser(user);
		
		System.out.println(usr);
		
		
		if(usr.getLoggedIn()) 
			dao.markUserLogout(usr.getUserId());
		else 
			dao.markUserLoggedIn(usr.getUserId());
		
		usr = dao.getUser(user);
		
		System.out.println("After login : "+usr);
		
		
	}

}
