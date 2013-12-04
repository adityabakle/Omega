package com.cellmania.cmreports.db;

import java.util.Collection;
import java.util.Date;

import com.cellmania.cmreports.db.masters.CarrierMasterDTO;
import com.cellmania.cmreports.db.masters.CarrierParams;
import com.cellmania.cmreports.db.masters.FileExtensionDTO;
import com.cellmania.cmreports.db.masters.ReportMasterDTO;
import com.cellmania.cmreports.db.masters.ReportParams;
import com.cellmania.cmreports.db.masters.RoleDTO;
import com.cellmania.cmreports.db.masters.UserMasterDTO;
import com.cellmania.cmreports.db.masters.UserParams;
import com.cellmania.cmreports.db.request.ExecutionLogDTO;
import com.cellmania.cmreports.db.request.FrequencyDTO;
import com.cellmania.cmreports.db.request.RequestDTO;
import com.cellmania.cmreports.db.request.RequestParams;
import com.cellmania.cmreports.db.settings.ServerSettingParams;
import com.cellmania.cmreports.db.settings.ServerSettingsDTO;

public interface ICMReports {
	/*
	 * Server Settings API Calls
	 * */
	public Date getDBTimeStamp() throws Exception;
	public String getDBTimeZone() throws Exception;
	public String getServerSettingValue(String key) throws Exception;
	public ServerSettingsDTO getServerSetting(Long id) throws Exception;
	public long getServerSettingCount(ServerSettingParams ssparams) throws Exception;
	public Collection<ServerSettingsDTO> getServerSettings(ServerSettingParams ssparams) throws Exception;
	public void addServerSetting(ServerSettingsDTO dto) throws Exception;
	public void updateServerSetting(ServerSettingsDTO dto) throws Exception;
	public void deleteServerSetting(Long id) throws Exception;
	public boolean duplicateCheck(ServerSettingParams ssparams) throws Exception;
	
	/**
	 * User API 
	 * **/
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
	
	/*
	 * Report Request API
	 * */
	
	public Collection<FrequencyDTO> getSchedulingFrequencies() throws Exception;
	public FrequencyDTO getFrequencyDetails(Long frequencyId) throws Exception;
	public Long addRequest(RequestDTO dto) throws Exception;
	public void updateRequest(RequestDTO dto) throws Exception;
	public RequestDTO getRequestDetails(RequestParams rparams) throws Exception;
	public Long addExecutionLog(ExecutionLogDTO dto) throws Exception;
	public void completeJobExecution(ExecutionLogDTO dto) throws Exception;
	
	public Collection<RequestDTO> getRequestForUser(RequestParams rparams) throws Exception;
	public Collection<ExecutionLogDTO> getExecutionLogForRequest(RequestParams rparams) throws Exception;
	public ExecutionLogDTO getExecutionLogDetails(Long exeLogId) throws Exception;
	public Long getExecutionLogForRequestCount(RequestParams rparams) throws Exception;
	public Long getRequestForUserCount(RequestParams rparams) throws Exception;
	public void unscheduleJob(RequestDTO dto)  throws Exception;
	public void updateMailDetails(RequestDTO dto) throws Exception;
	public RequestDTO getSQLAndHeader(Long requestId) throws Exception;
	public void updateSql(RequestDTO dto) throws Exception;
	
	public void forceTermination(RequestDTO rDto, ExecutionLogDTO eDto) throws Exception;
}
