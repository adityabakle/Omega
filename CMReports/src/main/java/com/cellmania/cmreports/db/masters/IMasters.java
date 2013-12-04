package com.cellmania.cmreports.db.masters;

import java.util.Collection;

public interface IMasters {
	public UserMasterDTO getUser(UserParams params) throws Exception;
	public Collection<UserMasterDTO> getUsers(UserParams params) throws Exception;
	public int getUserCount(UserParams params) throws Exception;
	public void addUser(UserMasterDTO dto) throws Exception;
	public void updateUser(UserMasterDTO dto) throws Exception;
	public void markUserLoggedIn(Long userId) throws Exception;
	public void markUserLogout(Long userId) throws Exception;
	public void lockUserAccount(Long userId) throws Exception;
	public void updateLoginAttemptsCount(Long userId) throws Exception;
	public void updatePasswordRecoveryKey(UserMasterDTO dto) throws Exception;
	public void resetPassword(UserMasterDTO dto) throws Exception;
	public void logoutAllUsers() throws Exception;
	public Collection<RoleDTO> getRoles() throws Exception;
	public boolean duplicateCheck(UserParams params) throws Exception;
	//public void deleteUser(Long userId) throws Exception;
	
	/*
	 * Carrier master API
	 * */
	public Collection<CarrierMasterDTO> getCarriers(CarrierParams cparams) throws Exception;
	public Collection<CarrierMasterDTO> getCarriersForUser(CarrierParams cparams) throws Exception;
	public CarrierMasterDTO getCarrier(CarrierParams cparams) throws Exception;
	public void addCarrier(CarrierMasterDTO dto) throws Exception;
	public void updateCarrier(CarrierMasterDTO dto) throws Exception;
	public boolean duplicateCheck(CarrierParams params) throws Exception;
	
	/*
	 * Report master API
	 * */
	public Collection<FileExtensionDTO> getFileExtensions() throws Exception;
	public Collection<ReportMasterDTO> getReports(ReportParams rparams) throws Exception;
	public ReportMasterDTO getReport(ReportParams rparams) throws Exception;
	public void addReport(ReportMasterDTO dto) throws Exception;
	public void updateReport(ReportMasterDTO dto) throws Exception;
	public boolean duplicateCheck(ReportParams params) throws Exception;
	public Collection<ReportMasterDTO> getReportsForCarrier(ReportParams rparams) throws Exception;
}
