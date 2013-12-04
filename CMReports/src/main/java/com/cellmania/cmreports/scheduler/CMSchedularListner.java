package com.cellmania.cmreports.scheduler;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerKey;

public class CMSchedularListner implements SchedulerListener {
	Logger log = Logger.getLogger(this.getClass());
	
	public void jobAdded(JobDetail arg0) {
		
	}

	
	public void jobDeleted(JobKey arg0) {
		// TODO Auto-generated method stub

	}

	
	public void jobPaused(JobKey arg0) {
		// TODO Auto-generated method stub

	}

	
	public void jobResumed(JobKey arg0) {
		// TODO Auto-generated method stub

	}

	
	public void jobScheduled(Trigger arg0) {
		log.debug("*****Job Shceduled : "+arg0.getKey().getName());

	}

	
	public void jobUnscheduled(TriggerKey arg0) {
		log.debug("*****Job UnShceduled : "+arg0.getName());

	}

	
	public void jobsPaused(String arg0) {
		// TODO Auto-generated method stub

	}

	
	public void jobsResumed(String arg0) {
		// TODO Auto-generated method stub

	}

	
	public void schedulerError(String arg0, SchedulerException arg1) {
		log.error("*****Schedular Error : "+arg0, arg1);
	}

	
	public void schedulerInStandbyMode() {
		// TODO Auto-generated method stub

	}

	
	public void schedulerShutdown() {
		log.info("*****Schedular Shutdown");

	}

	
	public void schedulerShuttingdown() {
		log.info("*****Schedular is Shutting down.");
	}

	
	public void schedulerStarted() {
		log.debug("*****In Lister for Start");

	}

	
	public void schedulingDataCleared() {
		// TODO Auto-generated method stub

	}

	
	public void triggerFinalized(Trigger arg0) {
		// TODO Auto-generated method stub

	}

	
	public void triggerPaused(TriggerKey arg0) {
		log.info("*Trigger Paused: "+arg0.getName());

	}

	
	public void triggerResumed(TriggerKey arg0) {
		log.info("*Trigger Paused: "+arg0.getName());

	}

	
	public void triggersPaused(String arg0) {
		log.info("****Triggers Paused: "+arg0);

	}

	
	public void triggersResumed(String arg0) {
		log.info("****Triggers Resumed: "+arg0);

	}


	public void schedulerStarting() {
		// TODO Auto-generated method stub
		
	}

}
