/**
 * 
 */
package com.cellmania.cmreports.db.masters;

import java.util.Collection;
import java.util.Date;

import com.cellmania.cmreports.common.ObjectConvertor;

/**
 * @author abakle
 *
 */
public class UserMasterDTO {
	private Long userId;
	private String userName;
	private String password;
	private String confirmPassword;
	private String name;
	private String email;
	private Long roleId;
	private RoleDTO role;
	private Date createdDate;
	private Date modifiedDate;
	private Long updatedBy;
	private String UpdatedByName;
	private Boolean passwordExpired;
	private Date passwordLastUpdated;
	private Long loginAttempts;
	private Boolean accountLocked;
	private Boolean enabled;
	private String passwordRecoveryKey;
	private Date lastLoginDate;
	private Boolean loggedIn;
	private String signature;
	private Collection<UserCarrierXrefDTO> userCarrierXref;
	
	private Integer userTimeZoneOffset;
	
	// USer Statistic Information
	private long scheduledRequest;
	private long expiredRequest;
	
	public UserMasterDTO(){
		
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

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public RoleDTO getRole() {
		return role;
	}

	public void setRole(RoleDTO role) {
		this.role = role;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedByName() {
		return UpdatedByName;
	}

	public void setUpdatedByName(String updatedByName) {
		UpdatedByName = updatedByName;
	}

	public Boolean getPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(Boolean passwordExpired) {
		this.passwordExpired = passwordExpired;
	}

	public Date getPasswordLastUpdated() {
		return passwordLastUpdated;
	}

	public void setPasswordLastUpdated(Date passwordLastUpdated) {
		this.passwordLastUpdated = passwordLastUpdated;
	}

	public Long getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(Long loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

	public Boolean getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getPasswordRecoveryKey() {
		return passwordRecoveryKey;
	}

	public void setPasswordRecoveryKey(String passwordRecoveryKey) {
		this.passwordRecoveryKey = passwordRecoveryKey;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Boolean getLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(Boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
	
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public Collection<UserCarrierXrefDTO> getUserCarrierXref() {
		return userCarrierXref;
	}

	public void setUserCarrierXref(Collection<UserCarrierXrefDTO> userCarrierXref) {
		this.userCarrierXref = userCarrierXref;
	}

	public Integer getUserTimeZoneOffset() {
		return userTimeZoneOffset;
	}

	public void setUserTimeZoneOffset(Integer userTimeZoneOffset) {
		this.userTimeZoneOffset = userTimeZoneOffset;
	}

	public long getScheduledRequest() {
		return scheduledRequest;
	}

	public void setScheduledRequest(long scheduledRequest) {
		this.scheduledRequest = scheduledRequest;
	}

	public long getExpiredRequest() {
		return expiredRequest;
	}

	public void setExpiredRequest(long expiredRequest) {
		this.expiredRequest = expiredRequest;
	}

	public String toString(){
		return ObjectConvertor.convertToString(this);
	}
}
