package com.cellmania.cmreports.web.util;

import java.util.Collection;
import java.util.Date;

import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.db.CMReportFactory;
import com.cellmania.cmreports.db.ICMReports;
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

public class CMDBService {
	public static ICMReports dao = CMReportFactory.getICMReports();
	
	public CMDBService() {
		
	}
	
	public Date getDBTimeStamp(boolean b) throws CMException {
		try{
			return dao.getDBTimeStamp();
		} catch (Exception e){
			throw new CMException(e);
		}
	}
	
	public String getDBTimeZone(boolean b) throws CMException {
		try{
			return dao.getDBTimeZone();
		} catch (Exception e){
			throw new CMException(e);
		}
	}
	
	public static String getServerSettingsValue(String key) throws CMException{
		try {
			return dao.getServerSettingValue(key);
		} catch (Exception e) {
			throw new CMException(e);
		}
		
	}
	
	public  Collection<ServerSettingsDTO> getServerSettings(ServerSettingParams sparams) throws CMException{
		try {
			return dao.getServerSettings(sparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
		
	}
	
	public  long getServerSettingCount(ServerSettingParams sparams) throws CMException{
		try {
			return dao.getServerSettingCount(sparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
		
	}
	
	public  ServerSettingsDTO getServerSetting(Long id) throws CMException{
		try {
			return dao.getServerSetting(id);
		} catch (Exception e) {
			throw new CMException(e);
		}
		
	}
	
	public void addServerSetting(ServerSettingsDTO dto) throws CMException{
		try {
			dao.addServerSetting(dto);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}	
	
	public void updateServerSetting(ServerSettingsDTO dto) throws CMException{
		try {
			dao.updateServerSetting(dto);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}	
	
	public boolean duplicateServerSettingKey(ServerSettingParams ssparams) throws CMException {
		try {
			return dao.duplicateCheck(ssparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public void deleteServerSetting(long settingId)throws CMException {
		try {
			dao.deleteServerSetting(settingId);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public UserMasterDTO getUser(UserParams uparams) throws CMException{
		try {
			return dao.getUser(uparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public Collection<UserMasterDTO> getUsers(UserParams uparams) throws CMException{
		try {
			return dao.getUsers(uparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public Collection<RoleDTO> getRoles() throws CMException {
		try {
			return dao.getRoles();
		} catch (Exception e){
			throw new CMException(e);
		}
	}
	
	public void markUserLogout(Long userId) throws CMException {
		try {
			dao.markUserLogout(userId);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public void markUserLoggedIn(Long userId) throws CMException {
		try {
			dao.markUserLoggedIn(userId);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public void updateLoginAttemptsCount(Long userId) throws CMException{
		try {
			dao.updateLoginAttemptsCount(userId);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public void updatePasswordRecoveryKey(UserMasterDTO dto) throws CMException{
		try {
			dao.updatePasswordRecoveryKey(dto);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public void resetPassword(UserMasterDTO dto) throws CMException{
		try {
			dao.resetPassword(dto);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public void logoutAllUsers() throws CMException {
		try{
			dao.logoutAllUsers();
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public void lockUserAccount(Long userId) throws CMException {
		try{
			dao.lockUserAccount(userId);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public boolean duplicateUserName(UserParams uparams) throws CMException {
		try {
			return dao.duplicateCheck(uparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public void addUser(UserMasterDTO dto) throws CMException {
		try{
			dao.addUser(dto);
		} catch (Exception e) {
			throw new CMException(e);
		}
		
	}
	
	public void updateUser(UserMasterDTO dto) throws CMException {
		try{
			dao.updateUser(dto);
		} catch (Exception e) {
			throw new CMException(e);
		}
		
	}
	
	/*
	 * Report Master API
	 * */
	public Collection<FileExtensionDTO> getFileExtensions() throws CMException {
		try {
			return dao.getFileExtensions();
		} catch (Exception e){
			throw new CMException(e);
		}
	}

	public ReportMasterDTO getReport(ReportParams rparams) throws CMException {
		try {
			return dao.getReport(rparams);
		} catch (Exception e){
			throw new CMException(e);
		}
	}
	
	public boolean duplicateReportName(ReportParams rparams) throws CMException {
		try {
			return dao.duplicateCheck(rparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public void addReport(ReportMasterDTO dto) throws CMException{
		try {
			dao.addReport(dto);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public void updateReport(ReportMasterDTO dto) throws CMException{
		try {
			dao.updateReport(dto);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}

	public Collection<ReportMasterDTO> getReports(ReportParams rparams) throws CMException {
		try {
			return dao.getReports(rparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}

	public Collection<CarrierMasterDTO> getCarriers(CarrierParams cparams)throws CMException {
		try {
			return dao.getCarriers(cparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public Collection<CarrierMasterDTO> getCarriersForUser(CarrierParams cparams)throws CMException {
		try {
			return dao.getCarriersForUser(cparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public CarrierMasterDTO getCarrier(CarrierParams cparams)throws CMException {
		try {
			return dao.getCarrier(cparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public boolean duplicateCarrierName(CarrierParams cparams) throws CMException {
		try {
			return dao.duplicateCheck(cparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public void updateCarrier(CarrierMasterDTO carrier)throws CMException  {
		try {
			dao.updateCarrier(carrier);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public void addCarrier(CarrierMasterDTO carrier)throws CMException  {
		try {
			dao.addCarrier(carrier);
		} catch (Exception e) {
			carrier.setCarrierId(null);
			throw new CMException(e);
		}
	}

	public Collection<FrequencyDTO> getSchedulingFrequencies() throws CMException{
		try{
			return dao.getSchedulingFrequencies();
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public FrequencyDTO getFrequencyDetails(Long frequencyId) throws CMException{
		try{
			return dao.getFrequencyDetails(frequencyId);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public RequestDTO getRequestDetails(RequestParams rparams) throws CMException{
		try{
			return dao.getRequestDetails(rparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	/*
	 * Add Request 
	 * */
	
	public Long addRequest(RequestDTO dto) throws CMException {
		try{
			return dao.addRequest(dto);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public Long addExecutionLog(ExecutionLogDTO dto) throws CMException{
		try{
			return dao.addExecutionLog(dto);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public void completeJobExecution(ExecutionLogDTO dto) throws CMException{
		try{
			dao.completeJobExecution(dto);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}

	public Collection<ReportMasterDTO> getReportsForCarrier(ReportParams rp) throws CMException {
		try{
			return dao.getReportsForCarrier(rp);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public Collection<RequestDTO> getRequestForUser(RequestParams rparams) throws CMException{
		try{
			return dao.getRequestForUser(rparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public Long getRequestForUserCount(RequestParams rparams) throws CMException{
		try{
			return dao.getRequestForUserCount(rparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public Collection<ExecutionLogDTO> getExecutionLogForRequest(RequestParams rparams) throws CMException{
		try{
			return dao.getExecutionLogForRequest(rparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public Long getExecutionLogForRequestCount(RequestParams rparams) throws CMException{
		try{
			return dao.getExecutionLogForRequestCount(rparams);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public ExecutionLogDTO getExecutionLogDetails(Long exeLogId) throws CMException{
		try{
			return dao.getExecutionLogDetails(exeLogId);
		} catch (Exception e) {
			throw new CMException(e);
		}
	}
	
	public void unscheduleJob(RequestDTO dto) throws CMException{
		try{
			dao.unscheduleJob(dto);
		} catch(Exception e){
			throw new CMException(e);
		}
	}
	
	public void updateMailDetails(RequestDTO dto) throws CMException{
		try{
			dao.updateMailDetails(dto);
		} catch(Exception e){
			throw new CMException(e);
		}
	}
	public RequestDTO getSQLAndHeader(Long requestId) throws CMException{
		try{
			return dao.getSQLAndHeader(requestId);
		} catch(Exception e){
			throw new CMException(e);
		}
	}
	
	public void updateSql(RequestDTO dto) throws CMException {
		try{
			dao.updateSql(dto);
		} catch(Exception e){
			throw new CMException(e);
		}
	}
	
	public void forceTermination(RequestDTO rDto, ExecutionLogDTO eDto) throws CMException{
		try{
			dao.forceTermination(rDto, eDto);
		} catch(Exception e){
			throw new CMException(e);
		}
	}
}
