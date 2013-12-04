package com.cellmania.cmreports.web.util;

import static org.quartz.CalendarIntervalScheduleBuilder.calendarIntervalSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Base64;
import org.quartz.JobDetail;
import org.quartz.Trigger;

import com.cellmania.cmreports.db.masters.UserMasterDTO;
import com.cellmania.cmreports.db.request.RequestDTO;
import com.cellmania.cmreports.scheduler.ScheduleReport;

public class WebUtility {
	
	public static String getPasswordRecoverKey (){
		SecureRandom random = null;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		byte [] key = new byte[16];
		random.nextBytes(key);
		return new String(Base64.encodeBase64(key)).replace("\r\n", "").trim();
	}
	
	public static void main (String[] arg) throws Exception{
		String randKey = getPasswordRecoverKey();
		System.out.println(randKey);
		System.out.println(URLEncoder.encode(randKey,"UTF-8"));
	}
	
	
	public static JobDetail createJobDetailsForReport(RequestDTO reqReport, UserMasterDTO loggdInUser){
		String jobName = "JOB_"+reqReport.getRequestId()+"_"+loggdInUser.getUserId();
		String groupName = "SCH_GRP_"+reqReport.getCarrier().getCarrierId();
		
		JobDetail jobDetails = newJob(ScheduleReport.class)
				.withIdentity(jobName,groupName)
				.build();
		jobDetails.getJobDataMap().put(ScheduleReport._REQUEST_ID, reqReport.getRequestId());
		return jobDetails;
	}
	
	public static Trigger createTriggerForReport(RequestDTO reqReport2,
			JobDetail job) {
		String jobName = job.getKey().getName();
		String groupName = job.getKey().getGroup();

		Trigger trg = null;
		if (reqReport2.getFrequency().getCode().equals("O")) {
			trg = newTrigger().withIdentity(jobName, groupName)
					.startAt(reqReport2.getScheduledDate())
					.endAt(reqReport2.getExpiryDate())
					.forJob(jobName, groupName)
					.withSchedule(simpleSchedule().withRepeatCount(0)).build();
		} else if (reqReport2.getFrequency().getCode().equals("D")) {
			trg = newTrigger()
					.withIdentity(jobName, groupName)
					.startAt(reqReport2.getScheduledDate())
					.endAt(reqReport2.getExpiryDate())
					.forJob(jobName, groupName)
					.withSchedule(
							calendarIntervalSchedule().withIntervalInDays(1))
					.build();
		} else if (reqReport2.getFrequency().getCode().equals("W")) {
			trg = newTrigger()
					.withIdentity(jobName, groupName)
					.startAt(reqReport2.getScheduledDate())
					.endAt(reqReport2.getExpiryDate())
					.forJob(jobName, groupName)
					.withSchedule(
							calendarIntervalSchedule().withIntervalInWeeks(1))
					.build();
		} else if (reqReport2.getFrequency().getCode().equals("B")) {
			trg = newTrigger()
					.withIdentity(jobName, groupName)
					.startAt(reqReport2.getScheduledDate())
					.endAt(reqReport2.getExpiryDate())
					.forJob(jobName, groupName)
					.withSchedule(
							calendarIntervalSchedule().withIntervalInWeeks(2))
					.build();
		} else if (reqReport2.getFrequency().getCode().equals("M")) {
			trg = newTrigger()
					.withIdentity(jobName, groupName)
					.startAt(reqReport2.getScheduledDate())
					.endAt(reqReport2.getExpiryDate())
					.forJob(jobName, groupName)
					.withSchedule(
							calendarIntervalSchedule().withIntervalInMonths(1))
					.build();
		}  else if (reqReport2.getFrequency().getCode().equals("T")) {
			trg = newTrigger()
					.withIdentity(jobName, groupName)
					.startAt(reqReport2.getScheduledDate())
					.endAt(reqReport2.getExpiryDate())
					.forJob(jobName, groupName)
					.withSchedule(
							calendarIntervalSchedule().withIntervalInDays(1))
					.build();
		}  else if (reqReport2.getFrequency().getCode().equals("C")) {
			trg = newTrigger()
					.withIdentity(jobName, groupName)
					.startAt(reqReport2.getScheduledDate())
					.endAt(reqReport2.getExpiryDate())
					.forJob(jobName, groupName)
					.withSchedule(
							calendarIntervalSchedule().withIntervalInDays(1))
					.build();
		}
		return trg;
	}

	public static Date getScheduledDateTime(Date schDate, Integer userOffset,
			String toTZ) throws ParseException {
		return getScheduledDateTime(schDate, getUserTimeZone(userOffset),
				TimeZone.getTimeZone(toTZ));
	}
	
	/*public static Date getScheduledDateTime(Date schDate, TimeZone frmTZ,
			TimeZone toTZ) throws ParseException {
		if(schDate==null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyy HH:mm:ss");
		int i = frmTZ.getDSTSavings();
		String userDate = sdf.format(schDate) + " "
				+ frmTZ.getDisplayName(i > 0 ? true : false, TimeZone.SHORT);
		
		
		SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MMM-yyy HH:mm:ss z");
		i = toTZ.getDSTSavings();
		String toTmzID = toTZ.getDisplayName(i > 0 ? true : false, TimeZone.SHORT);
		toTZ = TimeZone.getTimeZone(toTmzID);
		
		sdf2.setTimeZone(toTZ);
		Date serverDate = sdf2.parse(userDate);
		return serverDate;
	}*/
	
	public static Date getScheduledDateTime(Date schDate, TimeZone frmTZ,
			TimeZone toTZ) throws ParseException {
		// get Offset of User Time Zone
		if(schDate==null) return null;
		int oOff = frmTZ.getOffset(schDate.getTime());
		
		// Change the date to GMT by substracting user timezone offset
		long dateInGMT = schDate.getTime() - oOff;
		Date dtGMT = new Date(dateInGMT);
		
		//Get offset for Server time Zone 
		int noff = toTZ.getOffset(schDate.getTime());
		
		// Add offset to GMT date so that it is converted to Server time 
		long dtInServer = dtGMT.getTime()+noff;
		Date serverDate =new Date(dtInServer) ; 
		
		return serverDate;
	}
	
	public static TimeZone getUserTimeZone(Integer userOffset){
		int iOff = -(userOffset)/60;
		int remainder = userOffset%60;
		TimeZone sTz = TimeZone.getTimeZone("GMT"+(iOff>0?"+"+iOff:iOff)+ (remainder==0?"":":30"));
		return sTz; 
	}
}
