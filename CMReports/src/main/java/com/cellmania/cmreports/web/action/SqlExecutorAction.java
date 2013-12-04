package com.cellmania.cmreports.web.action;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.db.masters.ReportMasterDTO;
import com.cellmania.cmreports.db.masters.ReportParams;
import com.cellmania.cmreports.db.request.RequestDTO;
import com.cellmania.cmreports.scheduler.ScheduleReport;
import com.cellmania.cmreports.web.util.CMDBService;
import com.cellmania.cmreports.web.util.ServerSettingsConstants;
import com.cellmania.cmreports.web.util.WebUtility;

@SuppressWarnings("serial")
public class SqlExecutorAction extends WebBaseAction {
	public static Logger log = Logger.getLogger(SqlExecutorAction.class); 
	private CMDBService cmrDB = null;
	private RequestDTO reqReport;
	
	
    public void prepare() throws Exception {
    	setSelectedMenuIndex(7);
    	return;
    }

	public String home(){
		return SUCCESS;
	}
	
	public String sqlSchReport(){
		//log.debug("Request DTO : " + reqReport);
		if(valid(reqReport)){
			try {
				if(reqReport.getReport().getReportId()==null){
					ReportParams rp = new ReportParams();
					rp.setName(reqReport.getReport().getName());
					ReportMasterDTO rptDto = cmrDB.getReport(rp);
					if(rptDto!=null) {
						reqReport.setReport(rptDto);
						reqReport.setFileExtension(rptDto.getFileExtension().getExtension());
						//reqReport.setFileNamePrefix(rptDto.getFileNamePrefix());
					} else {
						addActionError("Invalid Report");
					}
				}
				reqReport.setFrequency(cmrDB.getFrequencyDetails(reqReport.getFrequency().getFrequencyId()));
				reqReport.setRequestedBy(getLoggedInUser());
				reqReport.getMail().setFromName(getLoggedInUser().getName());
				reqReport.getMail().setFromAddress(CMDBService.getServerSettingsValue(ServerSettingsConstants._MAIL_FROM_ADDRESS));
				reqReport.setUpdatedBy(getLoggedInUser().getUserId());
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
					log.error("Error Scheduling report.",e);
				}
			} catch (CMException e) {
				log.error("Error Creating request",e);
				addActionError("Some Error occurred while processing your request. Please try again later.\n"+e.getLocalizedMessage());
			} catch (Exception e) {
				log.error("Error Creating request",e);
				addActionError("Some Error occurred while processing your request. Please try again later.\n"+e.getLocalizedMessage());
			}
		}
		return SUCCESS;
	}
   

	private boolean valid(RequestDTO r) {
		if(r.getFileNamePrefix()==null || r.getFileNamePrefix().isEmpty()
				|| r.getSql() == null || r.getSql().isEmpty()
				|| r.getFrequency()==null || r.getFrequency().getFrequencyId() == null 
				|| r.getScheduledDate() == null || r.getMail()==null 
				|| r.getMail().getToAddress()==null || r.getMail().getToAddress().isEmpty()
				|| r.getMail().getReplyToAddress() == null || r.getMail().getReplyToAddress().isEmpty()
				|| r.getMail().getSubject() == null || r.getMail().getSubject().isEmpty()
				) {
			addActionError("All fields marked with '*' are mandatory.");
			return false;
		}
		else 
			return true;
	}

	/**
     * Getter Setter Methods
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
}
