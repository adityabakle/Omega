package com.cellmania.cmreports.db.masters;

import java.util.Collection;
import java.util.LinkedList;

import com.cellmania.cmreports.db.sqlsession.SessionExecutor;

@SuppressWarnings("unchecked")
public class MastersDao implements IMasters {

	private static IMasters dao = null;
	
	public static IMasters getInstance() {
		if(dao == null) {
			dao = new MastersDao();
		}
		return dao;
	}
	
	
	
	public UserMasterDTO getUser(UserParams uparams) throws Exception {
		return (UserMasterDTO) SessionExecutor.selectOne("UserMaster.getUserDetails", uparams);
	}


	
	
	public Collection<UserMasterDTO> getUsers(UserParams params)
			throws Exception {
		return (Collection<UserMasterDTO>) SessionExecutor.selectList(
				"UserMaster.getUserDetails", params, params.getStartRow(),
				params.getNumRows());
	}


	
	public int getUserCount(UserParams params) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	public boolean duplicateCheck(UserParams params) throws Exception{
		long count = (Long) SessionExecutor.selectOne("UserMaster.checkDuplicateUser",params);
		if(count > 0) return true;
		else return false;
	 }


	
	public void addUser(UserMasterDTO dto) throws Exception {
		LinkedList<Object[]> queryAndParams = new LinkedList<Object[]>();
		Long userId = (Long) SessionExecutor.selectOne("UserMaster.generateUserId");
		dto.setUserId(userId);
		for(UserCarrierXrefDTO ucx : dto.getUserCarrierXref()){
			ucx.setUserId(userId);
		}
		
		Object[] params = new Object[2];
		params[0] = "UserMaster.addUser";
		params[1] = dto;
		queryAndParams.add(params);
		
		params = new Object[2];
		params[0] = "UserMaster.insertUserCarrierMapping";
		params[1] = dto.getUserCarrierXref();
		queryAndParams.add(params);
		
		SessionExecutor.insert(queryAndParams);
	}


	
	public void updateUser(UserMasterDTO dto) throws Exception {
		LinkedList<Object[]> queryAndParams = new LinkedList<Object[]>();
		for(UserCarrierXrefDTO ucx : dto.getUserCarrierXref()){
			ucx.setUserId(dto.getUserId());
		}
		
		Object[] params = new Object[2];
		params[0] = "UserMaster.updateUser";
		params[1] = dto;
		queryAndParams.add(params);
		
		params = new Object[2];
		params[0] = "UserMaster.deleteUserCarrierMapping";
		params[1] = dto.getUserId();
		queryAndParams.add(params);
		
		params = new Object[2];
		params[0] = "UserMaster.insertUserCarrierMapping";
		params[1] = dto.getUserCarrierXref();
		queryAndParams.add(params);
		
		SessionExecutor.update(queryAndParams);
	}


	
	public void markUserLoggedIn(Long userId) throws Exception {
		SessionExecutor.update("UserMaster.markUserLoggedIn", userId);
		
	}
	
	
	public void markUserLogout(Long userId) throws Exception {
		SessionExecutor.update("UserMaster.markUserLogout", userId);
		
	}


	
	public void lockUserAccount(Long userId) throws Exception {
		SessionExecutor.update("UserMaster.lockUserAccount", userId);
		
	}


	
	public void updateLoginAttemptsCount(Long userId) throws Exception {
		SessionExecutor.update("UserMaster.updateLoginAttemptsCount", userId);
		
	}


	
	public void updatePasswordRecoveryKey(UserMasterDTO dto) throws Exception {
		SessionExecutor.update("UserMaster.updatePasswordRecoveryKey", dto);
		
	}


	
	public void resetPassword(UserMasterDTO dto) throws Exception {
		SessionExecutor.update("UserMaster.resetPassword", dto);
		
	}


	
	public void logoutAllUsers() throws Exception {
		SessionExecutor.update("UserMaster.logoutAllUsers");
	}


	
	
	public Collection<RoleDTO> getRoles() throws Exception {
		return (Collection<RoleDTO>) SessionExecutor.selectList("UserMaster.getRoles");
	}


	
	
	public Collection<CarrierMasterDTO> getCarriers(CarrierParams cparams) throws Exception {
		return (Collection<CarrierMasterDTO>) SessionExecutor.selectList("CarrierMaster.getCarrierDetails",cparams,cparams.getStartRow(),cparams.getNumRows());
	}
	
	
	
	public Collection<CarrierMasterDTO> getCarriersForUser(CarrierParams cparams) throws Exception {
		return (Collection<CarrierMasterDTO>) SessionExecutor.selectList("CarrierMaster.getCarriersForUser",cparams);
	}

	
	public CarrierMasterDTO getCarrier(CarrierParams cparams) throws Exception {
		return (CarrierMasterDTO) SessionExecutor.selectOne("CarrierMaster.getCarrierDetails", cparams);
	}

	
	public boolean duplicateCheck(CarrierParams params) throws Exception{
		long count = (Long) SessionExecutor.selectOne("CarrierMaster.checkDuplicateCarrier",params);
		if(count > 0) return true;
		else return false;
	 }
	
	
	public void addCarrier(CarrierMasterDTO dto) throws Exception {
		LinkedList<Object[]> queryAndParams = new LinkedList<Object[]>();
		Long carrierId = (Long) SessionExecutor.selectOne("CarrierMaster.generateCarrierId");
		dto.setCarrierId(carrierId);
		for(CarrierReportXrefDTO crx : dto.getCarrierReportXref()){
			crx.setCarrierId(carrierId);
		}
		
		Object[] params = new Object[2];
		params[0] = "CarrierMaster.addCarrier";
		params[1] = dto;
		queryAndParams.add(params);
		
		params = new Object[2];
		params[0] = "CarrierMaster.insertCarrierReportMapping";
		params[1] = dto.getCarrierReportXref();
		queryAndParams.add(params);
		
		SessionExecutor.insert(queryAndParams);
		
	}


	
	public void updateCarrier(CarrierMasterDTO dto) throws Exception {
		LinkedList<Object[]> queryAndParams = new LinkedList<Object[]>();
		for(CarrierReportXrefDTO crx : dto.getCarrierReportXref()){
			crx.setCarrierId(dto.getCarrierId());
		}
		Object[] params = new Object[2];
		params[0] = "CarrierMaster.updateCarrier";
		params[1] = dto;
		queryAndParams.add(params);
		
		params = new Object[2];
		params[0] = "CarrierMaster.deleteCarrierReportMapping";
		params[1] = dto.getCarrierId();
		queryAndParams.add(params);
		
		params = new Object[2];
		params[0] = "CarrierMaster.insertCarrierReportMapping";
		params[1] = dto.getCarrierReportXref();
		queryAndParams.add(params);
		
		SessionExecutor.update(queryAndParams);
		
	}


	
	
	public Collection<FileExtensionDTO> getFileExtensions() throws Exception {
		return (Collection<FileExtensionDTO>) SessionExecutor.selectList("ReportMaster.getFileExtensions");
	}


	
	
	public Collection<ReportMasterDTO> getReports(ReportParams rparams) throws Exception {
		return (Collection<ReportMasterDTO>) SessionExecutor.selectList("ReportMaster.getReportDetails", rparams, rparams.getStartRow(), rparams.getNumRows());
	}


	
	public ReportMasterDTO getReport(ReportParams rparams) throws Exception {
		return (ReportMasterDTO) SessionExecutor.selectOne("ReportMaster.getReportDetails", rparams);
	}
	
	
	public boolean duplicateCheck(ReportParams params) throws Exception{
		long count = (Long) SessionExecutor.selectOne("ReportMaster.checkDuplicateReport",params);
		if(count > 0) return true;
		else return false;
	 }

	
	public void addReport(ReportMasterDTO dto) throws Exception {
		SessionExecutor.insert("ReportMaster.addReport", dto);
		
	}


	
	public void updateReport(ReportMasterDTO dto) throws Exception {
		SessionExecutor.update("ReportMaster.updateReport",dto);
		
	}


	
	
	public Collection<ReportMasterDTO> getReportsForCarrier(ReportParams rparams) throws Exception {
		return (Collection<ReportMasterDTO>) SessionExecutor.selectList("ReportMaster.getReportsForCarrier", rparams);
	}

}
