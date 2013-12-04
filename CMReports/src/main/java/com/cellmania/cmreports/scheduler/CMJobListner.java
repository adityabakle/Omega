package com.cellmania.cmreports.scheduler;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import com.cellmania.carriers.util.Utility;
import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.db.request.ExecutionLogDTO;
import com.cellmania.cmreports.db.request.RequestParams;
import com.cellmania.cmreports.web.util.CMDBService;
import com.cellmania.cmreports.web.util.MailAgent;

public class CMJobListner implements JobListener {
	Logger log = Logger.getLogger(this.getClass());
	private static CMDBService cmrDB = new CMDBService();
	
	public String getName() {
		return "CMJobListner";
	}

	
	public void jobExecutionVetoed(JobExecutionContext arg0) {
		// TODO Auto-generated method stub

	}

	
	public void jobToBeExecuted(JobExecutionContext context) {
		
		Long requestId = (Long) context.getJobDetail().getJobDataMap().get(ScheduleReport._REQUEST_ID);
		if(requestId!=null){
			ExecutionLogDTO dto = new ExecutionLogDTO();
			try {
				dto.setRequest(cmrDB.getRequestDetails(new RequestParams(requestId)));
				cmrDB.addExecutionLog(dto);
			} catch (CMException e) {
				log.error("Error creating execution log: ", e);
			}
			context.getJobDetail().getJobDataMap().put(ScheduleReport._EXECUTION_LOG_ID, dto.getId());
			log.debug("I am about to execute the Job with exeID : : "+dto.getId());
		}
		
		
	}

	
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException arg1) {
		Long requestId = (Long) context.getJobDetail().getJobDataMap().get(ScheduleReport._REQUEST_ID);
		Long executionLogId = (Long) context.getJobDetail().getJobDataMap().get(ScheduleReport._EXECUTION_LOG_ID);
		if(requestId!=null && executionLogId!=null){
			ExecutionLogDTO dto = new ExecutionLogDTO();
			try {
				dto.setId(executionLogId);
				dto.setRequest(cmrDB.getRequestDetails(new RequestParams(requestId)));
				dto.getRequest().setNextExecutionDate(context.getTrigger().getNextFireTime());
				dto.setFileName((String) context.getJobDetail().getJobDataMap().get(ScheduleReport._REPORT_FILE_NAME));
				dto.setErrorReason((String) context.getJobDetail().getJobDataMap().get(ScheduleReport._ERROR_REASON));
				if(dto.getFileName()!=null){
					try{
						String mailSub = dto.getRequest().getMail().getSubject()+Utility.getDateForSubject(dto.getRequest());
						dto.getRequest().getMail().setSubject(mailSub);
						MailAgent ma = new MailAgent(dto.getRequest().getMail());
						ma.setFileName(dto.getFileName());
						ma.setExeId(dto.getId());
						log.debug("Sending mail.. "+dto.getId() +":"+dto.getFileName());
						ma.send();
						log.info("Main Sent Successfully for ExecutionID : "+dto.getId() +":"+dto.getFileName());
					} catch(Exception e){
						dto.setErrorReason("Report was generated but failed to send as attachment in mail. "+e.getMessage());
						log.error("Error Mailing the Report "+dto,e);
					}
				}
				cmrDB.completeJobExecution(dto);
			} catch (CMException e) {
				log.error("Error creating execution log: ", e);
			}
			context.getJobDetail().getJobDataMap().remove(ScheduleReport._ERROR_REASON);
			context.getJobDetail().getJobDataMap().remove(ScheduleReport._EXECUTION_LOG_ID);
			context.getJobDetail().getJobDataMap().remove(ScheduleReport._REPORT_FILE_NAME);
			log.info("Done executing the Job #id : "+dto.getId()+" :> " + dto.getFileName());
		}
		
		

	}

}
