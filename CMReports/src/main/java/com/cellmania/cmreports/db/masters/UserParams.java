package com.cellmania.cmreports.db.masters;

import com.cellmania.cmreports.common.ObjectConvertor;
import com.cellmania.cmreports.db.BaseParams;

public class UserParams extends BaseParams {

	public static final int SORT_COLUMN_USERNAME = 2;
	public static final int SORT_COLUMN_NAME = 3;
	public static final int SORT_COLUMN_EMAIL = 4;
	public static final int SORT_COLUMN_LASTLOGIN = 5;
	
	private Long userId;
	private String userName;
	private String password;
	private String newPassword;
	private String confirmPassword;
	private String passwordRecoveryKey;
	private String email;
	private Long roleId;
	
	public UserParams(){
		
	}
	
	public UserParams(Long userId){
		super();
		this.userId = userId;
	}
	
	public String toString() {
		return ObjectConvertor.convertToString(this);
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getPasswordRecoveryKey() {
		return passwordRecoveryKey;
	}

	public void setPasswordRecoveryKey(String passwordRecoveryKey) {
		this.passwordRecoveryKey = passwordRecoveryKey;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	
	
}
