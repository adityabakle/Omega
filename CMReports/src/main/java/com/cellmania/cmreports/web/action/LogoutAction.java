package com.cellmania.cmreports.web.action;

import org.apache.log4j.Logger;

import com.cellmania.cmreports.db.masters.UserParams;
import com.cellmania.cmreports.web.util.CMDBService;

@SuppressWarnings("serial")
public class LogoutAction extends WebBaseAction {
	public static Logger log = Logger.getLogger(LogoutAction.class); 
	private UserParams userParams;
	private Integer usrOffSet;
	
	private CMDBService cmrDB = null;
	
	
	
	public void prepare() { 
    	return;
    }
  
	public String execute() {
		getServletRequest().getSession(false).invalidate();
		
		return SUCCESS;
	}
	
	
	
	/**
	 * Setter & Getter methods Defined below
	 * */
	public CMDBService getCmrDB() {
		return cmrDB;
	}

	public void setCmrDB(CMDBService cmrDB) {
		this.cmrDB = cmrDB;
	}
	
	public UserParams getUserParams() {
		return userParams;
	}

	public void setUserParams(UserParams userParams) {
		this.userParams = userParams;
	}

	public Integer getUsrOffSet() {
		return usrOffSet;
	}

	public void setUsrOffSet(Integer usrOffSet) {
		this.usrOffSet = usrOffSet;
	}
}
