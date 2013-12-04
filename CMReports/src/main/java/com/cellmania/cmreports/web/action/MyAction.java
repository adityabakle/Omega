package com.cellmania.cmreports.web.action;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.common.Encryptor;
import com.cellmania.cmreports.db.BaseParams;
import com.cellmania.cmreports.db.masters.CarrierMasterDTO;
import com.cellmania.cmreports.db.masters.CarrierParams;
import com.cellmania.cmreports.db.masters.UserMasterDTO;
import com.cellmania.cmreports.db.masters.UserParams;
import com.cellmania.cmreports.db.request.ExecutionLogDTO;
import com.cellmania.cmreports.db.request.RequestDTO;
import com.cellmania.cmreports.db.request.RequestParams;
import com.cellmania.cmreports.scheduler.ScheduleReport;
import com.cellmania.cmreports.web.util.CMDBService;
import com.cellmania.cmreports.web.util.ServerSettingsConstants;

@SuppressWarnings("serial")
public class MyAction extends WebBaseAction {
	private static Logger log = Logger.getLogger(MyAction.class); 
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
	
	public String dashboard() {
		this.setSelectedMenuIndex(1);
		//addActionMessage("Welcome to your My Action - Dashboard");
		//TODO: Dashboard Screen
		setPerPageRecCount(10);
		RequestParams rp = new RequestParams();
		rp.setStartRow(getPageStartIndex());
		rp.setNumRows(getPerPageRecCount());
		rp.setUserId(getLoggedInUser().getUserId());
		rp.setEmail(getLoggedInUser().getEmail());
		int count = 0;
		try{
			adminAnnouncement = CMDBService.getServerSettingsValue(ServerSettingsConstants._ADMINISTRATOR_ANNOUNCEMENT);
			count = cmrDB.getExecutionLogForRequestCount(rp).intValue();
			getServletRequest().setAttribute("totalReportCount",count);
			if(count>0)
				exeLogs = cmrDB.getExecutionLogForRequest(rp);
		} catch(Exception e){
			log.error("Error fetching reports for dashboard : "+rp);
			addActionError("Some error occured fetching your reports. Please try later.");
		}
		count = 0;
		rp.setExpiredJobs(false);
		rp.setNumRows(5);
		try{
			count = cmrDB.getRequestForUserCount(rp).intValue();
			getServletRequest().setAttribute("totalReqCount",count);
			if(count>0)
				myReqReports = cmrDB.getRequestForUser(rp);
		} catch (Exception e) {
			log.error("Error fetching User Request "+rp,e);
			addActionError("Some error occured fetching your request history. Please try later.");
		}
		return SUCCESS;
	}
  
 
  
	public String reports() {
		this.setSelectedMenuIndex(4);
		RequestParams rp = new RequestParams();
		rp.setStartRow(getPageStartIndex());
		rp.setNumRows(getPerPageRecCount());
		rp.setUserId(getLoggedInUser().getUserId());
		rp.setEmail(getLoggedInUser().getEmail());
		try{
			setPageTotalRecCount(cmrDB.getExecutionLogForRequestCount(rp).intValue());
			if(getPageTotalRecCount()>0){
				exeLogs = cmrDB.getExecutionLogForRequest(rp);
			}
			
		} catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	public String ajaxMyReports(){
		RequestParams rp = new RequestParams();
		rp.setStartRow(getPageStartIndex());
		rp.setNumRows(getPerPageRecCount());
		rp.setCarrierId(reqReport.getCarrier().getCarrierId());
		rp.setReportId(reqReport.getReport().getReportId());
		
		if(requestId>0)
			rp.setRequestId(requestId);
		else{
			rp.setUserId(getLoggedInUser().getUserId());
			rp.setEmail(getLoggedInUser().getEmail());
		}
		
		try{
			//if(getPageTotalRecCount()==0)
				setPageTotalRecCount(cmrDB.getExecutionLogForRequestCount(rp).intValue());
			if(getPageTotalRecCount()>0){
				exeLogs = cmrDB.getExecutionLogForRequest(rp);
			}
		} catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	public String jobsTab(){
		this.setSelectedMenuIndex(3);
		return SUCCESS;
	}
	
	public String scheduledJobs() {
		this.setSelectedMenuIndex(3);
		this.selectedTab = 0;
		RequestParams rp = new RequestParams();
		rp.setStartRow(getPageStartIndex());
		rp.setNumRows(getPerPageRecCount());
		rp.setExpiredJobs(false);
		
		try{
			setPageTotalRecCount(cmrDB.getRequestForUserCount(rp).intValue());
			if(getPageTotalRecCount()>0)
				myReqReports = cmrDB.getRequestForUser(rp);
		} catch (Exception e) {
			log.error("Error fetching User Request "+rp,e);
			addActionError("Some error occured fetching your request history. Please try later.");
		}
		return "jobList";
	}
	
	public String activeJobs() {
		this.setSelectedMenuIndex(3);
		this.selectedTab = 1;
		RequestParams rp = new RequestParams();
		rp.setStartRow(getPageStartIndex());
		rp.setNumRows(getPerPageRecCount());
		rp.setExpiredJobs(false);
		rp.setActiveJobs(true);
		
		try{
			setPageTotalRecCount(cmrDB.getRequestForUserCount(rp).intValue());
			if(getPageTotalRecCount()>0)
				myReqReports = cmrDB.getRequestForUser(rp);
		} catch (Exception e) {
			log.error("Error fetching User Request "+rp,e);
			addActionError("Some error occured fetching your request history. Please try later.");
		}
		return "jobList";
	}
	
	public String jobs() {
		this.selectedTab = 2;
		RequestParams rp = new RequestParams();
		rp.setStartRow(getPageStartIndex());
		rp.setNumRows(getPerPageRecCount());
		rp.setUserId(getLoggedInUser().getUserId());
		rp.setExpiredJobs(false);
		
		try{
			setPageTotalRecCount(cmrDB.getRequestForUserCount(rp).intValue());
			if(getPageTotalRecCount()>0)
				myReqReports = cmrDB.getRequestForUser(rp);
		} catch (Exception e) {
			log.error("Error fetching User Request "+rp,e);
			addActionError("Some error occured fetching your request history. Please try later.");
		}
		return "jobList";
	}
	
	public String history() {
		this.selectedTab = 3;
		RequestParams rp = new RequestParams();
		rp.setStartRow(getPageStartIndex());
		rp.setNumRows(getPerPageRecCount());
		rp.setExpiredJobs(true);
		rp.setSortOrder(BaseParams.SORT_DESC);
		//if(getLoggedInUser().getRole().getRoleId().longValue() != getAdminRoleId())
			rp.setUserId(getLoggedInUser().getUserId());
		
		try{
			setPageTotalRecCount(cmrDB.getRequestForUserCount(rp).intValue());
			if(getPageTotalRecCount()>0)
				myReqReports = cmrDB.getRequestForUser(rp);
		} catch (Exception e) {
			log.error("Error fetching User Request "+rp,e);
			addActionError("Some error occured fetching your request history. Please try later.");
		}
		return "jobList";
	}
	
	public String allJob() {
		this.selectedTab = 4;
		RequestParams rp = new RequestParams();
		rp.setStartRow(getPageStartIndex());
		rp.setNumRows(getPerPageRecCount());
		rp.setAllJobs(true);
		rp.setSortOrder(BaseParams.SORT_DESC);
		try{
			setPageTotalRecCount(cmrDB.getRequestForUserCount(rp).intValue());
			if(getPageTotalRecCount()>0)
				myReqReports = cmrDB.getRequestForUser(rp);
		} catch (Exception e) {
			log.error("Error fetching User Request "+rp,e);
			addActionError("Some error occured fetching your request history. Please try later.");
		}
		return "jobList";
	}
	
	public String failedJob() {
		this.selectedTab = 5;
		RequestParams rp = new RequestParams();
		rp.setStartRow(getPageStartIndex());
		rp.setNumRows(getPerPageRecCount());
		rp.setFailedJobs(true);
		
		try{
			setPageTotalRecCount(cmrDB.getExecutionLogForRequestCount(rp).intValue());
			if(getPageTotalRecCount()>0)
				exeLogs = cmrDB.getExecutionLogForRequest(rp);
		} catch (Exception e) {
			log.error("Error fetching User Request "+rp,e);
			addActionError("Some error occured fetching your request history. Please try later.");
		}
		return "jobList";
	}
	
	public String unschedule() {
		
		log.debug("RequestId for unschedule ("+selectedTab+") : "+requestId);
		RequestDTO dto = null;
		try{
			dto = cmrDB.getRequestDetails(new RequestParams(requestId));
			if(dto!=null && (RequestDTO._SCHEDULED.equals(dto.getState()) || getLoggedInUser().getRole().getName().equalsIgnoreCase("Administrator"))){
				ScheduleReport.removeSchedular(dto);
				dto.setState(RequestDTO._EXPIRED);
				dto.setUpdatedBy(getLoggedInUser().getUserId());
				cmrDB.unscheduleJob(dto);
			} else {
				addActionError("Cannot delete the request as its already expired or its currently executing. Please refresh the page.");
			}
		}  catch (SchedulerException e) {
			log.error("Error unscheduling Job from Schedular ("+requestId+") :"+dto,e);
			addActionError("Error unscheduling the job. Please try again later.");
		} catch(CMException e){
			log.error("Error unscheduling Job ("+requestId+") :"+dto,e);
			addActionError("Error unscheduling the job. Please try again later.");
		}
		log.info("# of jobs in Schedular :"+ScheduleReport.getJobCount());
		switch (selectedTab){
		case 0:
			return scheduledJobs();
		case 1:
			return activeJobs();
		case 2:
			return jobs();
		case 3:
			return history();
		default :
			return scheduledJobs();
		}
	}
	
	public String ajaxJobs() {
		RequestParams rp = new RequestParams();
		
		rp.setCarrierId(reqReport.getCarrier().getCarrierId());
		rp.setReportId(reqReport.getReport().getReportId());
		if(rp.getCarrierId()==0){
			rp.setReportId(0l);
			reqReport.getReport().setReportId(0l);
		}
			
		if(this.selectedTab == 0) // scheduled Jobs 
			rp.setExpiredJobs(false);
		else if(this.selectedTab == 1){ // active Jobs
			rp.setActiveJobs(true);
			rp.setExpiredJobs(false);
		} else if(this.selectedTab ==2){ // My Jobs
			rp.setExpiredJobs(false);
			rp.setUserId(getLoggedInUser().getUserId());
		} else if(this.selectedTab == 3){ // Expired
			rp.setExpiredJobs(true);
			rp.setSortOrder(BaseParams.SORT_DESC);
			//if(getLoggedInUser().getRole().getRoleId().longValue() != getAdminRoleId())
				rp.setUserId(getLoggedInUser().getUserId());
		}  else if(this.selectedTab == 4){ // All Jobs
			rp.setAllJobs(true);
			rp.setSortOrder(BaseParams.SORT_DESC);
		} else if(this.selectedTab == 5){ // Failed Job
			rp.setFailedJobs(true);
			rp.setSortOrder(BaseParams.SORT_DESC);
		}
			
		log.debug(rp.getStartRow());
		log.debug(getPageStartIndex());
		try{
			
			if(this.selectedTab==5){
				setPageTotalRecCount(cmrDB.getExecutionLogForRequestCount(rp).intValue());
				rp.setStartRow(getPageStartIndex());
				rp.setNumRows(getPerPageRecCount());
				exeLogs = cmrDB.getExecutionLogForRequest(rp);
			} else {
				setPageTotalRecCount(cmrDB.getRequestForUserCount(rp).intValue());
				rp.setStartRow(getPageStartIndex());
				rp.setNumRows(getPerPageRecCount());
				myReqReports = cmrDB.getRequestForUser(rp);
			}
		} catch (Exception e) {
			log.error("Error fetching User Request "+rp,e);
			addActionError("Some error occured fetching your request history. Please try later.");
		}
		return SUCCESS;
	}
	
	
	public String profile() {
		this.setSelectedMenuIndex(5);
		userDto = getLoggedInUser();
		RequestParams rp = new RequestParams();
		rp.setUserId(userDto.getUserId());
		
		try {
			rp.setExpiredJobs(false);
			userDto.setScheduledRequest(cmrDB.getRequestForUserCount(rp));
			
			rp.setExpiredJobs(true);
			userDto.setExpiredRequest(cmrDB.getRequestForUserCount(rp));
			
			CarrierParams cp = new CarrierParams();
			cp.setUserId(userDto.getUserId());
			cp.setEnabled(true);
			cp.setSortCol(CarrierParams.SORT_COLUMN_DISPLAY_NAME);
			carriers = cmrDB.getCarriersForUser(cp);
		} catch (CMException e) {
			log.error("error Fetching schedule Count for User :"+userDto,e);
		}
		return SUCCESS;
	}
	
	public String chnagePassword(){
		return SUCCESS;
	}
	
	public String changeMyPassword(){
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
						addActionError("Your account is locked. Please contact the Administrator.");
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
