package com.cellmania.cmreports.web.action;

import java.util.Collection;

import org.apache.commons.validator.EmailValidator;
import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.db.masters.CarrierMasterDTO;
import com.cellmania.cmreports.db.masters.CarrierParams;
import com.cellmania.cmreports.db.masters.ReportMasterDTO;
import com.cellmania.cmreports.db.masters.ReportParams;
import com.cellmania.cmreports.db.request.ExecutionLogDTO;
import com.cellmania.cmreports.db.request.RequestDTO;
import com.cellmania.cmreports.db.request.RequestParams;
import com.cellmania.cmreports.scheduler.ScheduleReport;
import com.cellmania.cmreports.web.util.CMDBService;
import com.cellmania.cmreports.web.util.ServerSettingsConstants;
import com.cellmania.cmreports.web.util.WebUtility;

@SuppressWarnings("serial")
public class ReportAction extends WebBaseAction {
	public static Logger log = Logger.getLogger(ReportAction.class); 
	private CMDBService cmrDB = null;
	private RequestDTO reqReport;
	private long requestId;
	private Collection<ExecutionLogDTO> exeLogs;
	
	public void prepare() throws Exception {
		this.setSelectedMenuIndex(2);
		return;
    }
  
	public String home() {
		return SUCCESS;
	}
	
	public String details(){
		try{
			reqReport = cmrDB.getRequestDetails(new RequestParams(requestId));
			if(reqReport==null){
				addActionError("Invalid request Id.");
			}
		}catch(CMException e){
			log.error("Error fetching details for request:"+requestId,e);
			addActionError("Error fetching details for the request. Please try again later.");
		}
		return SUCCESS;
	}
	
	public String ajaxExeLog(){
		RequestParams rp = new RequestParams();
		rp.setStartRow(getPageStartIndex());
		rp.setNumRows(getPerPageRecCount());
		rp.setRequestId(requestId);
		
		try{
			if(getPageTotalRecCount()==0)
				setPageTotalRecCount(cmrDB.getExecutionLogForRequestCount(rp).intValue());
			if(getPageTotalRecCount()>0){
				exeLogs = cmrDB.getExecutionLogForRequest(rp);
			}
		} catch(Exception e){
			
		}
		return SUCCESS;
	}
	
	public String newReport(){
		log.debug("Request DTO "+reqReport);
		
		try{
			ReportMasterDTO rptDto = cmrDB.getReport(new ReportParams(reqReport.getReport().getReportId()));
			CarrierMasterDTO crDto = cmrDB.getCarrier(new CarrierParams(reqReport.getCarrier().getCarrierId()));
			if(rptDto!=null) {
				reqReport.setReport(rptDto);
				reqReport.setFileExtension(rptDto.getFileExtension().getExtension());
				reqReport.setFileNamePrefix(rptDto.getFileNamePrefix());
			} else {
				addActionError("Invalid Report");
			}
			if(crDto!= null){
				reqReport.setCarrier(crDto);
			} else {
				addActionError("Invalid Carrier");
			}
			if(!valid(reqReport) || hasErrors()) 
				return SUCCESS;
			
			reqReport.setFrequency(cmrDB.getFrequencyDetails(reqReport.getFrequency().getFrequencyId()));
			reqReport.setRequestedBy(getLoggedInUser());
			reqReport.getMail().setFromName(getLoggedInUser().getName());
			reqReport.getMail().setFromAddress(CMDBService.getServerSettingsValue(ServerSettingsConstants._MAIL_FROM_ADDRESS));
			reqReport.setUpdatedBy(getLoggedInUser().getUserId());
			reqReport.setServerTimeZone(CMDBService.getServerSettingsValue(ServerSettingsConstants._SERVER_TIME_ZONE));
			
			reqReport.setScheduledDate(WebUtility.getScheduledDateTime(reqReport.getScheduledDate(), getLoggedInUser().getUserTimeZoneOffset(), reqReport.getServerTimeZone()));
			reqReport.setExpiryDate(WebUtility.getScheduledDateTime(reqReport.getExpiryDate(), getLoggedInUser().getUserTimeZoneOffset(), reqReport.getServerTimeZone()));
			
			Long reqId = cmrDB.addRequest(reqReport);
			log.debug("Request : "+reqId);
			
			JobDetail jobDetails =WebUtility.createJobDetailsForReport(reqReport, getLoggedInUser());
			Trigger trigger = WebUtility.createTriggerForReport(reqReport, jobDetails);
			
			try {
				log.debug("Job To Start at : "+trigger.getStartTime());
				ScheduleReport.addToSchedular(jobDetails, trigger);
				addActionMessage("Your request has been submited. Notification will be sent to the email address provided on completion.");
			} catch (SchedulerException e) {
				addActionError("Some Error occurred while scheduling your report. Please try again later.\n"+e.getLocalizedMessage());
				log.error("Error Scheduling report."+reqReport,e);
			}
		} catch(CMException e){
			log.error("Error Creating request",e);
			addActionError("Some Error occurred while processing your request. Please try again later.\n"+e.getCause());
		} catch (Exception e) {
			log.error("Error Creating request",e);
			addActionError("Some Error occurred while processing your request. Please try again later.\n"+e.getCause());
		}
		
		return SUCCESS;
	}
	
	private boolean valid(RequestDTO reqReport2) {
		if(reqReport2==null) 
			addActionError("Invalid request.");
		// Start date and end date is not required for Live date
		if(!reqReport2.getReport().getName().startsWith("liveItem")){
			if(reqReport2.getStartDate()==null)
				addActionError("Start Date cannot be blank.");
			if(reqReport2.getEndDate()== null)
				addActionError("End Date cannot be blank.");
			if(reqReport2.getIncludeCP()==null)
				addActionError("Select if this report needs to include CP share.");
		}
		if(reqReport2.getFrequency()==null || reqReport2.getFrequency().getFrequencyId()==null){
			addActionError("Please select a Scheduling Option.");
		}
		if(reqReport2.getScheduledDate()==null){
			addActionError("Schedule Start Date cannot be blank.");
		}
		if(reqReport2.getExpiryDate()!=null && reqReport2.getScheduledDate().after(reqReport2.getExpiryDate())){
			addActionError("Schedule End Date should be after Schedule Start Date.");
		}
		
		if(reqReport2.getMail().getReplyToAddress()==null || reqReport2.getMail().getReplyToAddress().isEmpty()){
			addActionError("'Reply To' address cannot be blank.");
		} else if(!EmailValidator.getInstance().isValid(reqReport2.getMail().getReplyToAddress()))
			addActionError("Invalid email address in 'Reply To'.");
		
		if(reqReport2.getMail().getToAddress()==null || reqReport2.getMail().getToAddress().isEmpty()){
			addActionError("To address cannot be blank.");
		} 
		
		if(reqReport2.getMail().getSubject()==null || reqReport2.getMail().getSubject().isEmpty())
			addActionError("Subject cannot be blank.");
		
		if(hasErrors())
			return false;
		else 
			return true;
	}

	public String updateReport(){
		log.debug("Request ID: "+requestId);
		log.debug("Mail Details to Save : "+reqReport.getMail());
		try{
			reqReport.setUpdatedBy(getLoggedInUser().getUserId());
			cmrDB.updateMailDetails(reqReport);
			addActionMessage("Mail details updated.");
		} catch (CMException e){
			log.error("Error While updating Mail details for :"+reqReport,e);
			addActionError("Sorry we are unable to save the chnages. Please try again later.");
		}
		return details();
	}
	
	public String viewSql(){
		try{
			reqReport = cmrDB.getSQLAndHeader(requestId);
			log.debug("Request :"+reqReport);
		} catch (Exception e){
			log.error("Error while fetching Sql for Request : "+requestId,e);
		}
		return SUCCESS;
	}
	
	public String updateSql(){
		try{
			reqReport.setUpdatedBy(getLoggedInUser().getUserId());
			cmrDB.updateSql(reqReport);
		} catch (Exception e){
			log.error("Error Updating Sql for Request : "+reqReport,e);
			addActionError("Sorry we are unable to save your changes. Please try again later.");
		}
		return details();
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

	public RequestDTO getReqReport() {
		return reqReport;
	}

	public void setReqReport(RequestDTO reqReport) {
		this.reqReport = reqReport;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public Collection<ExecutionLogDTO> getExeLogs() {
		return exeLogs;
	}

	public void setExeLogs(Collection<ExecutionLogDTO> exeLogs) {
		this.exeLogs = exeLogs;
	}

}
