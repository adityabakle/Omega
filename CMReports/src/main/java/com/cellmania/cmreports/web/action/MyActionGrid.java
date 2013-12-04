package com.cellmania.cmreports.web.action;

import java.util.Collection;

import org.apache.log4j.Logger;

import com.cellmania.cmreports.db.masters.CarrierMasterDTO;
import com.cellmania.cmreports.db.masters.UserMasterDTO;
import com.cellmania.cmreports.db.masters.UserParams;
import com.cellmania.cmreports.db.request.ExecutionLogDTO;
import com.cellmania.cmreports.db.request.RequestDTO;
import com.cellmania.cmreports.db.request.RequestParams;
import com.cellmania.cmreports.web.util.CMDBService;

@SuppressWarnings("serial")
public class MyActionGrid extends JQueryGridBaseAction {
	
	private static Logger log = Logger.getLogger(MyActionGrid.class); 
	private CMDBService cmrDB = null;
	private Collection<RequestDTO> myReqReports;
	private RequestDTO reqReport;
	private Collection<ExecutionLogDTO> exeLogs;
	private Collection<CarrierMasterDTO> carriers;
	private ExecutionLogDTO exeLog;
	private int selectedTab = 0;
	private long requestId;
	private UserMasterDTO userDto;
	private String adminAnnouncement;
	private UserParams userParams;
	
	public void prepare() throws Exception {
    	return;
    }
	
	public String reports(){
		RequestParams rp = new RequestParams();
		int to = getRows() * getPage();
		
		log.debug("Row : "+getRows()+" : Page "+getPage());
		
		
		if(requestId>0)
			rp.setRequestId(requestId);
		else{
			rp.setUserId(getLoggedInUser().getUserId());
			rp.setEmail(getLoggedInUser().getEmail());
		}
		
		try{
			if(getTotal()==0)
				setTotal(cmrDB.getExecutionLogForRequestCount(rp).intValue());
			
			if(getTotal()>0){
				log.debug("total : "+getTotal());
				rp.setStartRow(to - getRows());
				rp.setNumRows(getRows());
				exeLogs = cmrDB.getExecutionLogForRequest(rp);
			}
		} catch(Exception e){
			log.error("Error Fetching Record"+e,e);
		}
		return JSON;
	}
	
	public String jobsTab(){
		this.setSelectedMenuIndex(3);
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
	public Collection<RequestDTO> getMyReqReports() {
		return myReqReports;
	}
	public void setMyReqReports(Collection<RequestDTO> myReqReports) {
		this.myReqReports = myReqReports;
	}
	public RequestDTO getReqReport() {
		return reqReport;
	}
	public void setReqReport(RequestDTO reqReport) {
		this.reqReport = reqReport;
	}
	public Collection<ExecutionLogDTO> getExeLogs() {
		return exeLogs;
	}
	public void setExeLogs(Collection<ExecutionLogDTO> exeLogs) {
		this.exeLogs = exeLogs;
	}
	public Collection<CarrierMasterDTO> getCarriers() {
		return carriers;
	}

	public void setCarriers(Collection<CarrierMasterDTO> carriers) {
		this.carriers = carriers;
	}

	public ExecutionLogDTO getExeLog() {
		return exeLog;
	}
	public void setExeLog(ExecutionLogDTO exeLog) {
		this.exeLog = exeLog;
	}
	public int getSelectedTab() {
		return selectedTab;
	}
	public void setSelectedTab(int selectedTab) {
		this.selectedTab = selectedTab;
	}
	public long getRequestId() {
		return requestId;
	}
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public UserMasterDTO getUserDto() {
		return userDto;
	}

	public void setUserDto(UserMasterDTO userDto) {
		this.userDto = userDto;
	}

	public String getAdminAnnouncement() {
		return adminAnnouncement;
	}

	public void setAdminAnnouncement(String adminAnnouncement) {
		this.adminAnnouncement = adminAnnouncement;
	}

	public UserParams getUserParams() {
		return userParams;
	}

	public void setUserParams(UserParams userParams) {
		this.userParams = userParams;
	}
}
