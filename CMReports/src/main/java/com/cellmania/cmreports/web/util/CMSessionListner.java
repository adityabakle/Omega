package com.cellmania.cmreports.web.util;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.db.masters.UserMasterDTO;

public class CMSessionListner implements HttpSessionListener {

	private static Logger log = Logger.getLogger(CMSessionListner.class);
	private static CMDBService cmrDB = new CMDBService();
	public void sessionCreated(HttpSessionEvent arg0) {
		
	}

	public void sessionDestroyed(HttpSessionEvent sessEvent) {
		UserMasterDTO user = (UserMasterDTO) sessEvent.getSession().getAttribute(SessionAttributeConstant._LOGIN_USER);
		UserMasterDTO originalUser = (UserMasterDTO) sessEvent.getSession().getAttribute(SessionAttributeConstant._ORIGINAL_LOGIN_USER);
		//log.debug("Session about to invalidate for User "+user);
		if(null!= user){
			try {
				if(originalUser!=null){
					cmrDB.markUserLogout(originalUser.getUserId());
					log.debug("User "+originalUser.getName()+" was successfully logout out of System");
				}
				else {
					cmrDB.markUserLogout(user.getUserId());
					log.debug("User "+user.getName()+" was successfully logout out of System");
				}
			} catch (CMException e) {
				if(originalUser!=null)
					log.error("Unable to mark user for logout. "+originalUser, e);
				else 
					log.error("Unable to mark user for logout. "+user, e);
			}
			
		}
	}

}
