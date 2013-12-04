package com.cellmania.cmreports.scheduler;

import static org.quartz.impl.matchers.EverythingMatcher.allJobs;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.plugins.management.ShutdownHookPlugin;

import com.cellmania.carriers.db.CarrierReportsDao;
import com.cellmania.carriers.db.ICarrierReports;
import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.db.request.ExecutionLogDTO;
import com.cellmania.cmreports.db.request.RequestDTO;
import com.cellmania.cmreports.db.request.RequestParams;
import com.cellmania.cmreports.web.util.CMDBService;
import com.cellmania.cmreports.web.util.ServerSettingsConstants;
import com.cellmania.cmreports.web.util.WebUtility;

public class ScheduleReport extends ShutdownHookPlugin implements Job {
	private static Scheduler sched; 
	private static CMDBService cmrDB = new CMDBService();
	public static final String _REQUEST_ID = "requestId";
	public static final String _EXECUTION_LOG_ID = "executionLogId";
	public static final String _REPORT_FILE_NAME = "fileName";
	public static final String _ERROR_REASON = "errorReason";
	private static AtomicInteger jobCount = new AtomicInteger(0);
	public static String carrierDBServiceClassName;
	public static String reportPath;
	public static String serverTimeZone;
	static Logger log = Logger.getLogger(ScheduleReport.class);
			
	
	public ScheduleReport() {
		super();
		log.debug("SCHEDULER Constructor CREATED");
		reloadStaticConfig();
	}
	
	public static void reloadStaticConfig(){
		try {
			carrierDBServiceClassName = CMDBService.getServerSettingsValue(ServerSettingsConstants._CARRIER_DB_SERVICE_CLASS);
			reportPath = CMDBService.getServerSettingsValue(ServerSettingsConstants._REPORTS_DIR_PATH);
			serverTimeZone = CMDBService.getServerSettingsValue(ServerSettingsConstants._SERVER_TIME_ZONE);
		} catch (CMException e) {
			log.error("Error fetching Carrier Global Settings",e);
		}
	}
	
	
	public void execute(JobExecutionContext context) throws JobExecutionException 
	{
		log.debug("In Execute :" + context.getJobDetail().getKey().getName());
		Long requestId = (Long) context.getJobDetail().getJobDataMap().get(_REQUEST_ID);
		String fileName = null;
		try {
			fileName = processReport(requestId);
		} catch (Exception e) {
			context.getJobDetail().getJobDataMap().put(_ERROR_REASON,"Some error occured while processing Request : "+e.getCause());
			log.error("Error Procesing Report : "+requestId,e);
		}
		context.getJobDetail().getJobDataMap().put(_REPORT_FILE_NAME, fileName);
		
	}

	private String processReport(Long requestId) throws Exception {
		RequestDTO req = null;
		req = cmrDB.getRequestDetails(new RequestParams(requestId));
		if(req!=null){
			req.setReportPath(reportPath);
			req.setServerTimeZone(serverTimeZone);
			Class<?> cls = Class.forName(carrierDBServiceClassName);
			String apiName = req.getReport().getDbServiceApiName();
			Method meth = cls.getDeclaredMethod(apiName, RequestDTO.class);
			log.debug("DTO "+req);
			if(meth!=null){
				ICarrierReports icrRpt = new CarrierReportsDao();
				log.debug("Inoking API for Query for Carrier Report");
				return (String)meth.invoke(icrRpt, req);
			} else {
				throw new CMException("Invalid Method in the service class ("+apiName+")");
			}
		} else {
			throw new CMException("Invalid Request ID:"+requestId);
		}
	}

	public void initialize(String arg0, Scheduler arg1)
			throws SchedulerException {
		log.debug("SCHEDULER INITIALIZE");
		
		/*if(!sched.isStarted()){
			sched.start();
			log.info("Schedular Started");
		}*/
		
	}

	public void shutdown() {
		log.debug("In SCHEDULER SHUTDOWN");
		terminateRunningJobs();
		
	}

	private void terminateRunningJobs() {
		log.debug("Terminating Jobs...");
		try{
			RequestDTO rDto = new RequestDTO();
			rDto.setState(RequestDTO._SCHEDULED);
			ExecutionLogDTO eDto = new  ExecutionLogDTO();
			eDto.setSuccess(false);
			eDto.setErrorReason("Job terminated due to Server Shutdown.");
			new CMDBService().forceTermination(rDto, eDto);
		} catch(Exception e){
			log.error("Error Terminating Jobs",e);
		}
	}

	public void start() {
		log.debug("SCHEDULER START");
		if(sched==null){
			try {
				sched = StdSchedulerFactory .getDefaultScheduler();
			} catch (SchedulerException e) {
				log.error("Error Fetching Schedular Object");
			}
		}
		if(sched!=null){
			try {
				sched.getListenerManager().addSchedulerListener(new CMSchedularListner());
				sched.getListenerManager().addJobListener(new CMJobListner(), allJobs());
			} catch (SchedulerException e) {
				log.error("Exception Adding Listener to Schedular", e);
			}
		}
		
		loadjobsfromDB();
		
	}
	
	private void loadjobsfromDB() {
		try{
			Collection<RequestDTO> colJobs =  cmrDB.getRequestForUser(new RequestParams());
			if(colJobs!=null && colJobs.size()>0){
				for(RequestDTO req : colJobs){
					JobDetail jd = WebUtility.createJobDetailsForReport(req, req.getRequestedBy());
					// if a request has be executed previously then its Next execution date will be its start date.
					// This way the job wont execute again for previous date if its already executed.
					log.info("Request Id in jobDetails Map: "+jd.getJobDataMap().get(ScheduleReport._REQUEST_ID));
					if(req.getNextExecutionDate()!=null)   
						req.setScheduledDate(req.getNextExecutionDate());
					
					Trigger tr = WebUtility.createTriggerForReport(req, jd);
					addToSchedular(jd,tr);
				}
			}
			log.info("# of jobs Scheduled from DB : "+jobCount.get());
		} catch (Exception e ){
			log.error("Unable to laod jobs as ther was some exception in system",e);
		}
		
	}

	public static synchronized void addToSchedular(JobDetail jobDetails, Trigger trigger) throws SchedulerException{
		if(sched==null){
			sched = StdSchedulerFactory .getDefaultScheduler();
			sched.getListenerManager().addJobListener(new CMJobListner(), allJobs());
		}
		
		sched.scheduleJob(jobDetails, trigger); 
		jobCount.incrementAndGet();
	}
	
	public static synchronized void removeSchedular(RequestDTO req) throws SchedulerException{
		if(sched==null){
			sched = StdSchedulerFactory .getDefaultScheduler();
			sched.getListenerManager().addJobListener(new CMJobListner(), allJobs());
		}
		String jobName = "JOB_"+req.getRequestId()+"_"+req.getRequestedBy().getUserId();
		String groupName = "SCH_GRP_"+req.getCarrier().getCarrierId();
		
		//sched.getTrigger(new TriggerKey(jobName,groupName)).
		sched.unscheduleJob(new TriggerKey(jobName,groupName));
		sched.deleteJob(new JobKey(jobName, groupName));
		jobCount.decrementAndGet();
		
	}

	public static int getJobCount() {
		return jobCount.get();
	}



	public static SchedulerMetaData getMetaData() throws SchedulerException {
		if(sched!=null){
			return sched.getMetaData();
		}
		return null;
	}
	
	public static void standBy() throws SchedulerException{
		if(sched!=null){
			sched.standby();
			log.debug("Scheduler StandBy Status : "+sched.getMetaData().getSummary());
		}
	}
	
	public static void restart() throws SchedulerException{
		if(sched!=null && sched.isInStandbyMode()){
			sched.start();
			log.debug("Scheduler Restart Status : "+sched.getMetaData().getSummary());
		}
	}

	public static void stopSchedular(boolean waitForJobs) throws SchedulerException {
		if(sched!=null)
			sched.shutdown(waitForJobs);
		
	}
}
