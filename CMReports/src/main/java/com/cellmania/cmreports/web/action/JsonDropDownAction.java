package com.cellmania.cmreports.web.action;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.db.masters.CarrierMasterDTO;
import com.cellmania.cmreports.db.masters.CarrierParams;
import com.cellmania.cmreports.db.masters.FileExtensionDTO;
import com.cellmania.cmreports.db.masters.ReportMasterDTO;
import com.cellmania.cmreports.db.masters.ReportParams;
import com.cellmania.cmreports.db.masters.RoleDTO;
import com.cellmania.cmreports.db.masters.UserMasterDTO;
import com.cellmania.cmreports.db.masters.UserParams;
import com.cellmania.cmreports.db.request.FrequencyDTO;
import com.cellmania.cmreports.db.request.RequestDTO;
import com.cellmania.cmreports.web.util.CMDBService;

@SuppressWarnings("serial")
public class JsonDropDownAction extends WebBaseAction {
	private static Logger log = Logger.getLogger(JsonDropDownAction.class);
	private static final String _JSON_RESULT = "jsonResult";
	private RequestDTO reqReport;
	
	private CMDBService cmrDB = null;
	private Collection<RoleDTO> roles;
	private Collection<UserMasterDTO> users;
	private Collection<FileExtensionDTO> fileExtensions;
	private Collection<CarrierMasterDTO> carriers;
	private Collection<ReportMasterDTO> reports;
	private Collection<FrequencyDTO> schFrequencies;
	
	public void prepare() throws Exception {
    	return;
    }
	
	public String fetchRoles(){
		try {
			roles = cmrDB.getRoles();
		} catch (CMException e) {
			log.error("Error fetching roles",e);
		}
		return _JSON_RESULT;
	}
	
	public String fetchUsers(){
		try {
			UserParams uparams = new UserParams();
			uparams.setSortCol(UserParams.SORT_COLUMN_NAME);
			users = cmrDB.getUsers(uparams);
		} catch (CMException e) {
			log.error("Error fetching users",e);
		}
		return _JSON_RESULT;
	}
	
	public String fetchFileExtensions(){
		try {
			fileExtensions = cmrDB.getFileExtensions();
		} catch (CMException e) {
			log.error("Error fetching fileExtensions",e);
		}
		return _JSON_RESULT;
	}
	
	public String fetchCarriersForUser(){
		CarrierParams cparams = new CarrierParams();
		cparams.setEnabled(true);
		cparams.setSortCol(CarrierParams.SORT_COLUMN_DISPLAY_NAME);
		cparams.setUserId(getLoggedInUser().getUserId());
		try {
			carriers = cmrDB.getCarriersForUser(cparams);
		} catch (CMException e) {
			log.error("Error fetching Carriers for user "+cparams,e);
		}
		return _JSON_RESULT;
	}
	
	public String fetchSchedulingOptions(){
		try {
			schFrequencies = cmrDB.getSchedulingFrequencies();
		} catch (CMException e) {
			log.error("Error fetching Scheduling Frequency.",e);
		}
		return _JSON_RESULT;
	}
	
	public String fetchReportsForCarrier(){
		try {
			ReportParams rp = new ReportParams();
			rp.setEnabled(true);
			rp.setCarrierId(reqReport.getCarrier().getCarrierId());
			 reports = cmrDB.getReportsForCarrier(rp);
		} catch (CMException e) {
			log.error("Error fetching Scheduling Frequency.",e);
		}
		return _JSON_RESULT;
	}
	
	
	/**
	 * Setter & Getter methods Defined below
	 * */
	/*private CMDBService getCmrDB() {
		return cmrDB;
	}*/

	public void setCmrDB(CMDBService cmrDB) {
		this.cmrDB = cmrDB;
	}

	public RequestDTO getReqReport() {
		return reqReport;
	}

	public void setReqReport(RequestDTO reqReport) {
		this.reqReport = reqReport;
	}

	public Collection<RoleDTO> getRoles() {
		return roles;
	}

	public void setRoles(Collection<RoleDTO> roles) {
		this.roles = roles;
	}

	public Collection<UserMasterDTO> getUsers() {
		return users;
	}

	public void setUsers(Collection<UserMasterDTO> users) {
		this.users = users;
	}

	public Collection<FileExtensionDTO> getFileExtensions() {
		return fileExtensions;
	}

	public void setFileExtensions(Collection<FileExtensionDTO> fileExtensions) {
		this.fileExtensions = fileExtensions;
	}

	public Collection<CarrierMasterDTO> getCarriers() {
		return carriers;
	}

	public void setCarriers(Collection<CarrierMasterDTO> carriers) {
		this.carriers = carriers;
	}

	public Collection<ReportMasterDTO> getReports() {
		return reports;
	}

	public void setReports(Collection<ReportMasterDTO> reports) {
		this.reports = reports;
	}

	public Collection<FrequencyDTO> getSchFrequencies() {
		return schFrequencies;
	}

	public void setSchFrequencies(Collection<FrequencyDTO> schFrequencies) {
		this.schFrequencies = schFrequencies;
	}

}
