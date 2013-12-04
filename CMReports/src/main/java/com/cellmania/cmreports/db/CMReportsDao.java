package com.cellmania.cmreports.db;

import java.util.Collection;
import java.util.Date;

import com.cellmania.cmreports.db.masters.CarrierMasterDTO;
import com.cellmania.cmreports.db.masters.CarrierParams;
import com.cellmania.cmreports.db.masters.FileExtensionDTO;
import com.cellmania.cmreports.db.masters.IMasters;
import com.cellmania.cmreports.db.masters.MastersDao;
import com.cellmania.cmreports.db.masters.ReportMasterDTO;
import com.cellmania.cmreports.db.masters.ReportParams;
import com.cellmania.cmreports.db.masters.RoleDTO;
import com.cellmania.cmreports.db.masters.UserMasterDTO;
import com.cellmania.cmreports.db.masters.UserParams;
import com.cellmania.cmreports.db.request.ExecutionLogDTO;
import com.cellmania.cmreports.db.request.FrequencyDTO;
import com.cellmania.cmreports.db.request.IRequest;
import com.cellmania.cmreports.db.request.RequestDTO;
import com.cellmania.cmreports.db.request.RequestDao;
import com.cellmania.cmreports.db.request.RequestParams;
import com.cellmania.cmreports.db.settings.IServerSettings;
import com.cellmania.cmreports.db.settings.ServerSettingParams;
import com.cellmania.cmreports.db.settings.ServerSettingsDTO;
import com.cellmania.cmreports.db.settings.ServerSettingsDao;

public class CMReportsDao implements ICMReports {

	
	public Date getDBTimeStamp() throws Exception {
		IServerSettings dao = ServerSettingsDao.getInstance();
		return dao.getDBTimeStamp();
	}

	
	public String getDBTimeZone() throws Exception {
		IServerSettings dao = ServerSettingsDao.getInstance();
		return dao.getDBTimeZone();
	}

	
	public String getServerSettingValue(String key) throws Exception {
		IServerSettings dao = ServerSettingsDao.getInstance();
		return dao.getServerSettingValue(key);
	}

	
	public ServerSettingsDTO getServerSetting(Long id) throws Exception {
		IServerSettings dao = ServerSettingsDao.getInstance();
		return dao.getServerSetting(id);
	}

	
	public long getServerSettingCount(ServerSettingParams ssparams)
			throws Exception {
		IServerSettings dao = ServerSettingsDao.getInstance();
		return dao.getServerSettingCount(ssparams);
	}

	
	public Collection<ServerSettingsDTO> getServerSettings(
			ServerSettingParams ssparams) throws Exception {
		IServerSettings dao = ServerSettingsDao.getInstance();
		return dao.getServerSettings(ssparams);
	}

	
	public void addServerSetting(ServerSettingsDTO dto) throws Exception {
		IServerSettings dao = ServerSettingsDao.getInstance();
		dao.addServerSetting(dto);
	}

	
	public void updateServerSetting(ServerSettingsDTO dto) throws Exception {
		IServerSettings dao = ServerSettingsDao.getInstance();
		dao.updateServerSetting(dto);
		
	}

	
	public void deleteServerSetting(Long id) throws Exception {
		IServerSettings dao = ServerSettingsDao.getInstance();
		dao.deleteServerSetting(id);
	}
	
	
	public boolean duplicateCheck(ServerSettingParams ssparams) throws Exception {
		IServerSettings dao = ServerSettingsDao.getInstance();
		return dao.duplicateCheck(ssparams);
	}
	
	
	public UserMasterDTO getUser(UserParams params) throws Exception {
		IMasters dao = MastersDao.getInstance();
		return dao.getUser(params);
	}

	
	public Collection<UserMasterDTO> getUsers(UserParams params)
			throws Exception {
		IMasters dao = MastersDao.getInstance();
		return dao.getUsers(params);
	}

	
	public int getUserCount(UserParams params) throws Exception {
		IMasters dao = MastersDao.getInstance();
		return dao.getUserCount(params);
	}

	
	public void addUser(UserMasterDTO dto) throws Exception {
		IMasters dao = MastersDao.getInstance();
		dao.addUser(dto);
	}

	
	public void updateUser(UserMasterDTO dto) throws Exception {
		IMasters dao = MastersDao.getInstance();
		dao.updateUser(dto);
	}

	
	public void markUserLoggedIn(Long userId) throws Exception {
		IMasters dao = MastersDao.getInstance();
		dao.markUserLoggedIn(userId);
	}
	
	
	public void markUserLogout(Long userId) throws Exception {
		IMasters dao = MastersDao.getInstance();
		dao.markUserLogout(userId);
	}

	
	public void lockUserAccount(Long userId) throws Exception {
		IMasters dao = MastersDao.getInstance();
		dao.lockUserAccount(userId);
	}

	
	public void updateLoginAttemptsCount(Long userId) throws Exception {
		IMasters dao = MastersDao.getInstance();
		dao.updateLoginAttemptsCount(userId);
	}

	
	public void updatePasswordRecoveryKey(UserMasterDTO dto) throws Exception {
		IMasters dao = MastersDao.getInstance();
		dao.updatePasswordRecoveryKey(dto);
	}

	
	public void resetPassword(UserMasterDTO dto) throws Exception {
		IMasters dao = MastersDao.getInstance();
		dao.resetPassword(dto);
	}

	
	public void logoutAllUsers() throws Exception {
		IMasters dao = MastersDao.getInstance();
		dao.logoutAllUsers();
	}

	
	public Collection<RoleDTO> getRoles() throws Exception {
		IMasters dao = MastersDao.getInstance();
		return dao.getRoles();
	}

	
	public Collection<CarrierMasterDTO> getCarriers(CarrierParams cparams) throws Exception {
		IMasters dao = MastersDao.getInstance();
		return dao.getCarriers(cparams);
	}
	
	
	public Collection<CarrierMasterDTO> getCarriersForUser(CarrierParams cparams) throws Exception {
		IMasters dao = MastersDao.getInstance();
		return dao.getCarriersForUser(cparams);
	}
	
	
	public CarrierMasterDTO getCarrier(CarrierParams cparams) throws Exception {
		IMasters dao = MastersDao.getInstance();
		return dao.getCarrier(cparams);
	}

	
	public void addCarrier(CarrierMasterDTO dto) throws Exception {
		IMasters dao = MastersDao.getInstance();
		dao.addCarrier(dto);
	}

	
	public void updateCarrier(CarrierMasterDTO dto) throws Exception {
		IMasters dao = MastersDao.getInstance();
		dao.updateCarrier(dto);
	}

	
	public Collection<FileExtensionDTO> getFileExtensions() throws Exception {
		IMasters dao = MastersDao.getInstance();
		return dao.getFileExtensions();
	}

	
	public Collection<ReportMasterDTO> getReports(ReportParams rparams) throws Exception {
		IMasters dao = MastersDao.getInstance();
		return dao.getReports(rparams);
	}

	
	public ReportMasterDTO getReport(ReportParams rparams) throws Exception {
		IMasters dao = MastersDao.getInstance();
		return dao.getReport(rparams);
	}

	
	public void addReport(ReportMasterDTO dto) throws Exception {
		IMasters dao = MastersDao.getInstance();
		dao.addReport(dto);
	}

	
	public void updateReport(ReportMasterDTO dto) throws Exception {
		IMasters dao = MastersDao.getInstance();
		dao.updateReport(dto);
	}

	
	public boolean duplicateCheck(UserParams params) throws Exception {
		IMasters dao = MastersDao.getInstance();
		return dao.duplicateCheck(params);
	}
	
	
	public Collection<ReportMasterDTO> getReportsForCarrier(ReportParams rparams) throws Exception{
		IMasters dao = MastersDao.getInstance();
		return dao.getReportsForCarrier(rparams);
	}

	
	public boolean duplicateCheck(CarrierParams params) throws Exception {
		IMasters dao = MastersDao.getInstance();
		return dao.duplicateCheck(params);
	}

	
	public boolean duplicateCheck(ReportParams params) throws Exception {
		IMasters dao = MastersDao.getInstance();
		return dao.duplicateCheck(params);
	}

	
	public Collection<FrequencyDTO> getSchedulingFrequencies() throws Exception {
		IRequest dao = RequestDao.getInstance();
		return dao.getSchedulingFrequencies();
	}
	
	
	public FrequencyDTO getFrequencyDetails(Long frequencyId) throws Exception{
		IRequest dao = RequestDao.getInstance();
		return dao.getFrequencyDetails(frequencyId);
	}
	
	
	public Long addRequest(RequestDTO dto) throws Exception {
		IRequest dao = RequestDao.getInstance();
		return dao.addRequest(dto);
	}

	
	public void updateRequest(RequestDTO dto) throws Exception {
		IRequest dao = RequestDao.getInstance();
		dao.updateRequest(dto);
	}

	
	public RequestDTO getRequestDetails(RequestParams rparams) throws Exception {
		IRequest dao = RequestDao.getInstance();
		return dao.getRequestDetails(rparams);
	}

	
	public Long addExecutionLog(ExecutionLogDTO dto) throws Exception {
		IRequest dao = RequestDao.getInstance();
		return dao.addExecutionLog(dto);
	}
	
	
	public void completeJobExecution(ExecutionLogDTO dto) throws Exception {
		IRequest dao = RequestDao.getInstance();
		dao.completeJobExecution(dto);
	}

	
	public Collection<RequestDTO> getRequestForUser(RequestParams rparams)
			throws Exception {
		IRequest dao = RequestDao.getInstance();
		return dao.getRequestForUser(rparams);
	}

	
	public Collection<ExecutionLogDTO> getExecutionLogForRequest(
			RequestParams rparams) throws Exception {
		IRequest dao = RequestDao.getInstance();
		return dao.getExecutionLogForRequest(rparams);
	}

	
	public ExecutionLogDTO getExecutionLogDetails(Long exeLogId)
			throws Exception {
		IRequest dao = RequestDao.getInstance();
		return dao.getExecutionLogDetails(exeLogId);
	}

	
	public Long getExecutionLogForRequestCount(RequestParams rparams)
			throws Exception {
		IRequest dao = RequestDao.getInstance();
		return dao.getExecutionLogForRequestCount(rparams);
	}

	
	public Long getRequestForUserCount(RequestParams rparams) throws Exception {
		IRequest dao = RequestDao.getInstance();
		return dao.getRequestForUserCount(rparams);
	}

	
	public void unscheduleJob(RequestDTO dto) throws Exception {
		IRequest dao = RequestDao.getInstance();
		dao.unscheduleJob(dto);		
	}

	
	public void updateMailDetails(RequestDTO dto) throws Exception {
		IRequest dao = RequestDao.getInstance();
		dao.updateMailDetails(dto);
		
	}

	
	public RequestDTO getSQLAndHeader(Long requestId) throws Exception {
		IRequest dao = RequestDao.getInstance();
		return dao.getSQLAndHeader(requestId);
	}

	
	public void updateSql(RequestDTO dto) throws Exception {
		IRequest dao = RequestDao.getInstance();
		dao.updateSql(dto);
	}


	public void forceTermination(RequestDTO rDto, ExecutionLogDTO eDto)
			throws Exception {
		IRequest dao = RequestDao.getInstance();
		dao.forceTermination(rDto, eDto);
		
	}
}
