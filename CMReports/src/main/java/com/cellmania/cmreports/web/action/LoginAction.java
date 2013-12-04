package com.cellmania.cmreports.web.action;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.common.Encryptor;
import com.cellmania.cmreports.db.masters.UserMasterDTO;
import com.cellmania.cmreports.db.masters.UserParams;
import com.cellmania.cmreports.db.request.MailDTO;
import com.cellmania.cmreports.web.util.CMDBService;
import com.cellmania.cmreports.web.util.MailAgent;
import com.cellmania.cmreports.web.util.ServerSettingsConstants;
import com.cellmania.cmreports.web.util.SessionAttributeConstant;
import com.cellmania.cmreports.web.util.WebUtility;

@SuppressWarnings("serial")
public class LoginAction extends WebBaseAction {
	private static Logger log = Logger.getLogger(LoginAction.class); 
	private UserParams userParams;
	private Integer usrOffSet;
	private boolean passwordExpired;
	
	private CMDBService cmrDB = null;
	
	
	
	public void prepare() throws Exception { 
    	return;
    }
  
	public String login() {
		return SUCCESS;
	}
	
	public String forgotPassword(){
		return SUCCESS;
	}
	
	public String recoverPassword(){
		if(userParams==null || userParams.getEmail()==null || userParams.getEmail().isEmpty()
		){
			addActionError("Email is mandatory.");
			return ERROR;
		}
		log.debug("User Params : "+userParams);
		UserMasterDTO user = null;
		try {
			user = cmrDB.getUser(userParams);
		} catch (CMException e) {
			log.error("Error fetching user info for forgot password:"+userParams,e);
		}
		
		if(null == user){
			addActionError("Email not found in system. Please verify email address.");
			return ERROR;
		} else {
			user.setPasswordRecoveryKey(WebUtility.getPasswordRecoverKey());
			try {
				cmrDB.updatePasswordRecoveryKey(user);
			} catch (CMException e) {
				log.error("Error updating PasswordRecovery Key for user "+user,e);
			}
			
			MailDTO m = new MailDTO();
			m.setToAddress(user.getEmail());
			m.setSubject("Cellmania Report Portal - Password Recovery");
			m.setAttachment(false);
			m.setIncludeDefaultFooter(true);
			m.setReplyToAddress("no-reply@cmreports.rim.com");
			try {
				m.setBody(getPasswordRecoveryMailBody(user));
			} catch (UnsupportedEncodingException e1) {
				log.error("Error generating message body"+user,e1);
			} catch (CMException e) {
				log.error("Error generating message body"+user,e);
			}
			MailAgent ma = new MailAgent(m);
			try {
				ma.sendPasswordRecoveryMail();
			} catch (Exception e) {
				log.error("Error Sending recovery mail "+ m,e);
			}
			addActionMessage("Request submitted. If you email address exist in the system you will receive an email with passowrd recovery instructions.");
		}
		
		
		return login();
	}
	
	private String getPasswordRecoveryMailBody(UserMasterDTO user) throws UnsupportedEncodingException, CMException {
		StringBuffer sb = new StringBuffer("Hi ");
		sb.append(user.getName()).append("<br/>")
		.append("<p>You have requested for password recovery on Cellmania Report portal.</p>")
		.append("<p> Please <a href=\"")
		.append(CMDBService.getServerSettingsValue(ServerSettingsConstants._SERVER_URL))
		.append("resetPassword.do?key=")
		.append(URLEncoder.encode(user.getPasswordRecoveryKey(),"UTF-8"))
		.append("\">click here</a> to reset you password.</p>");
		return sb.toString();
	}
	
	public String resetPassword(){
		if(getServletRequest().getParameter("key")!=null){
			userParams = new UserParams();
			userParams.setPasswordRecoveryKey(getServletRequest().getParameter("key"));
			return SUCCESS;
		} 
		return "loginPage";
	}
	
	public String changePassword(){
		
		if(userParams.getUserName()!=null && !userParams.getUserName().isEmpty() 
				&& userParams.getPassword()!=null && !userParams.getPassword().isEmpty()
				&& userParams.getNewPassword()!=null && !userParams.getNewPassword().isEmpty()
				&& userParams.getConfirmPassword()!=null && !userParams.getConfirmPassword().isEmpty()
		) {
			if(!userParams.getNewPassword().equals(userParams.getConfirmPassword())){
				addActionError("Both New and Confirm password must be same.");
			} else {
				UserMasterDTO user = null;
				try {
					user = cmrDB.getUser(userParams);
					String oldPwdEnc = Encryptor.encrypt(userParams.getPassword(), Encryptor.getSalt(userParams.getUserName()));
					String newPwdEnc = Encryptor.encrypt(userParams.getNewPassword(), Encryptor.getSalt(userParams.getUserName()));
					if(!user.getPassword().equals(oldPwdEnc)){
						addActionError("Old password do not match. Please try again.");
					} else if(user.getPassword().equals(newPwdEnc)){
						addActionError("New password cannot be same as Old password.");
					}else if(user.getAccountLocked()) {
						addActionError("Your account is locked. Please contactr the Administrator.");
					} else {
						user.setPassword(newPwdEnc);
						cmrDB.resetPassword(user);
						getServletRequest().setAttribute("cpDone", true);
					}
				} catch (CMException e) {
					log.error("Error while chnage Password verification. "+userParams,e);
				} catch (UnsupportedEncodingException e) {
					log.error("Error Encrypting Old Password ",e);
				}
				if(null == user){
					addActionError("Invalid information provided. Please verify your details.");
				}
			}
		} else {
			addActionError("All fields are mandatory");
		}
		return SUCCESS;
	}
	
	public String doReset(){
		if(userParams.getUserName()==null || userParams.getUserName().isEmpty() 
				|| userParams.getEmail()==null || userParams.getEmail().isEmpty()
				|| userParams.getNewPassword()==null || userParams.getNewPassword().isEmpty()
				|| userParams.getConfirmPassword()==null || userParams.getConfirmPassword().isEmpty()
		){
			addActionError("All fields are mandatory.");
			return ERROR;
		}
			
		UserMasterDTO user = null;
		try {
			user = cmrDB.getUser(userParams);
		} catch (CMException e) {
			log.error("Error reseting password."+userParams,e);
		}
		if(null == user){
			addActionError("Invalid User Name or Email for the given reset key.");
			return ERROR;
		} else if(user.getAccountLocked()) {
			addActionError("Account is locked. Please contact the Administrator.");
			return ERROR;
		}
		if((null == userParams.getConfirmPassword() || null == userParams.getNewPassword()) 
			|| !userParams.getConfirmPassword().equals(userParams.getNewPassword())){
			addActionError("Both password must be same.");
			return ERROR;
		}
		try {
			user.setPassword(Encryptor.encrypt(userParams.getNewPassword(), Encryptor.getSalt(user.getUserName())));
			cmrDB.resetPassword(user);
			addActionMessage("Password reset successfull. Please login with your new password.");
		} catch (Exception e) {
			log.error("Error reseting password :"+userParams,e);
			addActionError("Server encountered error while reseting password. Please try again later.");
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	public String authenticate() {
		if(userParams == null || userParams.getUserName() == null || userParams.getUserName().isEmpty() 
				|| userParams.getPassword() == null || userParams.getPassword().isEmpty()
		){
			addActionError("Please enter a valid User Name and Password.");
			return ERROR;
		}
			
		UserMasterDTO user = null;
		try {
			user = cmrDB.getUser(userParams);
		} catch (CMException e) {
			log.error("Error getting User Details"+userParams, e);
			addActionError("Unable to login you at this time. Please try again later or contact the Administrator.");
			return ERROR;
		}
		if(null == user){
			addActionError("Invalid login credentials. Please try again.");
			return ERROR;
		}
		
		if(user!=null){
			String encryptPwd = null;
			try {
				encryptPwd = Encryptor.encrypt(userParams.getPassword(), Encryptor.getSalt(userParams.getUserName()));
			} catch (UnsupportedEncodingException e) {
				log.error("Error Encrypting Password :"+userParams,e);
			}
			
			if(user.getAccountLocked()){
				addActionError("Your acount is locked. Please contact the Administrator.");
				return ERROR;
			}  else if(!user.getEnabled()){
				addActionError("Your acount is disabled. Please contact the Administrator.");
				return ERROR;
			} else if(!user.getPassword().equals(encryptPwd)){
				addActionError("Invalid login credentials. Please try again.");
				try {
					if(user.getLoginAttempts().longValue() + 1 >= Long.parseLong(CMDBService.getServerSettingsValue(ServerSettingsConstants._MAX_LOGIN_ATTEMPTS))){
						cmrDB.lockUserAccount(user.getUserId());
					}
					cmrDB.updateLoginAttemptsCount(user.getUserId());
				} catch (CMException e) {
					log.error("Error Updating Login Attempts Count."+user,e);
				}
				return ERROR;
			} else  if(user.getPasswordExpired()){
				passwordExpired = true;
				return ERROR;
			}
			
			try {
				cmrDB.markUserLoggedIn(user.getUserId());
				log.debug("User Time Zone Offset : "+usrOffSet);
				user.setUserTimeZoneOffset(usrOffSet);
			} catch (CMException e) {
				log.error("Error marking user as logged In: "+user, e);
			}
			getServletRequest().getSession().setAttribute(SessionAttributeConstant._LOGIN_USER, user);
			getServletRequest().getSession().setAttribute(SessionAttributeConstant._LOGIN_USER_TIMEZONE_ID,WebUtility.getUserTimeZone(usrOffSet).getID());
			
		}
		
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

	public boolean isPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(boolean passwordExpired) {
		this.passwordExpired = passwordExpired;
	}
}
