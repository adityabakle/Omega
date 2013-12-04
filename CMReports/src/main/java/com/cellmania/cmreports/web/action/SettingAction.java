package com.cellmania.cmreports.web.action;

import java.net.URLEncoder;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;

import com.cellmania.carriers.sqlSession.CarrierSqlSessionConfig;
import com.cellmania.carriers.xls.XLSWriter;
import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.common.Encryptor;
import com.cellmania.cmreports.db.masters.CarrierMasterDTO;
import com.cellmania.cmreports.db.masters.CarrierParams;
import com.cellmania.cmreports.db.masters.ReportMasterDTO;
import com.cellmania.cmreports.db.masters.ReportParams;
import com.cellmania.cmreports.db.masters.UserMasterDTO;
import com.cellmania.cmreports.db.masters.UserParams;
import com.cellmania.cmreports.db.request.MailDTO;
import com.cellmania.cmreports.db.request.RequestParams;
import com.cellmania.cmreports.db.settings.ServerSettingParams;
import com.cellmania.cmreports.db.settings.ServerSettingsDTO;
import com.cellmania.cmreports.scheduler.ScheduleReport;
import com.cellmania.cmreports.web.util.CMDBService;
import com.cellmania.cmreports.web.util.MailAgent;
import com.cellmania.cmreports.web.util.ServerSettingsConstants;
import com.cellmania.cmreports.web.util.WebUtility;

@SuppressWarnings("serial")
public class SettingAction extends WebBaseAction {
	private static Logger log = Logger.getLogger(SettingAction.class); 
	private CMDBService cmrDB = null;
	private Collection<UserMasterDTO> users;
	private Collection<CarrierMasterDTO> carriers;
	private Collection<ReportMasterDTO> reports;
	private Collection<ServerSettingsDTO> serverSettings;
	private ServerSettingsDTO serverSetting;
	
	private UserMasterDTO user;
	private CarrierMasterDTO carrier;
	private ReportMasterDTO report;
	private long userId;
	private long carrierId;
	private long reportId;
	private long settingId;
	private SchedulerMetaData schMD;
	
	private String alertMessage;
	private boolean reloadList = false;
	private int selectedTab = 0;
	
	public void prepare() throws Exception {
		this.setSelectedMenuIndex(6);
		return;
    }
  
	public String home() {
		return SUCCESS;
	}
	
	public String user(){
		selectedTab = 0;
		log.debug("In User Tab");
		UserParams uparams = new  UserParams();
		try {
			uparams.setSortCol(UserParams.SORT_COLUMN_NAME);
			users = cmrDB.getUsers(uparams);
		} catch (CMException e) {
			log.error("Error while fetching user list. ",e);
			addActionError("Error while fetching User List :"+e.getMessage());
		}
		return "userList";
	}
	public String delUser(){
		log.info("Will Delete User : "+userId);
		reloadList = true;
		alertMessage = "User Deleted Successfully.";
		return "userDetails";
	}
	public String userDetails(){
		selectedTab = 0;
		try {
			if(userId>0)
				user = cmrDB.getUser(new UserParams(userId));
			//log.debug(user);
			CarrierParams cparams = new CarrierParams();
			cparams.setEnabled(true);
			cparams.setSortCol(CarrierParams.SORT_COLUMN_DISPLAY_NAME);
			carriers = cmrDB.getCarriers(cparams);
		} catch (CMException e) {
			log.error("Error fetching details for User ; "+userId,e);
		}
		
		return "userDetails";
	}
	public String sendInvite(){
		if(userId>0)
			try {
				CarrierParams cparams = new CarrierParams();
				cparams.setEnabled(true);
				cparams.setSortCol(CarrierParams.SORT_COLUMN_DISPLAY_NAME);
				carriers = cmrDB.getCarriers(cparams);
				
				
				user = cmrDB.getUser(new UserParams(userId));
				if(user!=null){
					
					MailDTO invite = new MailDTO();
					invite.setToAddress(user.getEmail());
					invite.setSubject("Cellmania RevShare Report Portal - Registration.");
					String serverUrl = CMDBService.getServerSettingsValue(ServerSettingsConstants._SERVER_URL);
					
					StringBuffer msgbody = new StringBuffer("<html>");
				    msgbody.append("Hi,")
				    .append(user.getName())
				    .append("<br/><br/>Yor account on<a href=\""+serverUrl+"/login.do\"/>Cellmania RevShare Report Portal</a> is created.")
				    .append("<br/>Please follow the below instruction to access the portal.")
				    .append("<ul><li>Your Username is : ")
				    .append(user.getUserName())
				    .append("</li><li>Click <a href=\"")
				    .append(serverUrl)
				    .append("resetPassword.do?key=")
				    .append(URLEncoder.encode(user.getPasswordRecoveryKey(),"UTF-8"))
				    .append("\">here</a> to create your password.</li></ul>")
				    .append("<br><i>Please Note: Your need to be on <b>RIM/CELLMANIA</b> network to access this URL.</i><br>");
				    invite.setBody(msgbody.toString());
				    MailAgent ma = new MailAgent(invite);
				    ma.sendNewRegister();
				    alertMessage = "Email with login credentails has been mailed to the ("+user.getEmail()+")";
					reloadList = true;
				}
			} catch (Exception e) {
				reloadList = false;
				alertMessage = "Some error occured while sending invite. "+e.getLocalizedMessage();
				log.error("Error sending invite",e);
			}
		
		
		return "userDetails";
	}
	
	public String saveUser() {
		selectedTab = 0;
		
		try {
			CarrierParams cparams = new CarrierParams();
			cparams.setEnabled(true);
			cparams.setSortCol(CarrierParams.SORT_COLUMN_DISPLAY_NAME);
			carriers = cmrDB.getCarriers(cparams);
			
			if(!validateUser(user)){
				return "userDetails";
			}
			
			user.setUpdatedBy(getLoggedInUser().getUserId());
			UserParams uparams = new UserParams(user.getUserId());
			uparams.setUserName(user.getUserName());
			uparams.setEmail(user.getEmail());
			if(cmrDB.duplicateUserName(uparams)){
				addFieldError(
						"mandateField",
						"This username already exist. Please choose another.");
				return "userDetails";
			}
			try {
				if (user.getUserId() != null) {
					cmrDB.updateUser(user);
					alertMessage = "User details Saved Successfully";
				} else {
					user.setPassword(Encryptor.encrypt(user.getPassword(), Encryptor.getSalt(user.getUserName())));
					user.setPasswordRecoveryKey(WebUtility.getPasswordRecoverKey());
					cmrDB.addUser(user);
					alertMessage = "New User Successfully added.";
				}
				reloadList = true;
				
			} catch (Exception e) {
				
				log.error("Error Saving User :" + user, e);
				
				addFieldError(
						"mandateField",
						"Some error occurred while saving carrier. "
								+ e.getMessage());
			}
			
		} catch (CMException e) {
			log.error("Error fetching Carriers & reports for list ", e);
		}
		return "userDetails";
	}
	
	private boolean validateUser(UserMasterDTO valUser) {
		if(valUser.getUserName() == null || valUser.getUserName().isEmpty()
			|| valUser.getName() == null || valUser.getName().isEmpty()
			|| valUser.getAccountLocked() == null || valUser.getEnabled() == null
			|| valUser.getEmail()== null || valUser.getEmail().isEmpty()
			|| (valUser.getUserId()== null && (valUser.getPassword() == null || valUser.getPassword().isEmpty()))
			|| valUser.getUserCarrierXref() == null || valUser.getUserCarrierXref().size()==0
		){
			addFieldError("mandateField", "All fields are mandatory.");
			return false;
		}
		
		if(valUser.getUserId()== null && !valUser.getPassword().equals(valUser.getConfirmPassword())){
			addFieldError("mandateField", "Password & Confirm Password must be same.");
			return false;
		}
			
		return true;
	}

	public String carrier(){
		selectedTab = 1;
		try {
			CarrierParams cparams = new CarrierParams();
			cparams.setSortCol(CarrierParams.SORT_COLUMN_DISPLAY_NAME);
			carriers = cmrDB.getCarriers(cparams);
			if(carriers!=null)
			log.debug(carriers.size());
		} catch (CMException e) {
			log.error("Error fetching Carriers & reports for list ",e);
		}
		return "carrierList";
	}
	
	public String carrierDetails(){
		selectedTab = 1;
		try {
			if(carrierId>0)
				carrier = cmrDB.getCarrier(new CarrierParams(carrierId));
			log.debug(carrier);
			ReportParams rparams = new ReportParams();
			rparams.setEnabled(true);
			rparams.setSortCol(ReportParams.SORT_COLUMN_DISPLAY_NAME);
			reports = cmrDB.getReports(rparams);
		} catch (CMException e) {
			log.error("Error fetching Carriers & reports for list ",e);
		}
		return "carrierDetails";
	}
	
	public String saveCarrier(){
		selectedTab = 1;
		
		try {
			ReportParams rparams = new ReportParams();
			rparams.setEnabled(true);
			rparams.setSortCol(ReportParams.SORT_COLUMN_DISPLAY_NAME);
			reports = cmrDB.getReports(rparams);
			
			if(!validateCarrier(carrier)){
				return "carrierDetails";
			}
			
			carrier.setUpdatedBy(getLoggedInUser().getUserId());
			CarrierParams cparams = new CarrierParams(carrier.getCarrierId());
			cparams.setName(carrier.getName());
			if(cmrDB.duplicateCarrierName(cparams)){
				addFieldError(
						"mandateField",
						"Carrier with this name already exist. Please choose another.");
				return "carrierDetails";
			}
			try {
				if(carrier.getCarrierId() !=null ){ // This is update
					cmrDB.updateCarrier(carrier);
				} else { // this is Add
					cmrDB.addCarrier(carrier);		
				}
				reloadList = true;
				alertMessage = "Carrier Saved Successfully";
			} catch (CMException e) {
				log.error("Error Saving new carrier :"+carrier,e);
				addFieldError("mandateField","Some error occurred while saving carrier. "+e.getMessage());
			}
			
			
		} catch (CMException e) {
			log.error("Error fetching Carriers & reports for list ",e);
		}
		log.debug("Carrier to save : "+carrier);
		return "carrierDetails";
	}
	
	private boolean validateCarrier(CarrierMasterDTO carrier2) {
		if(carrier2.getName()== null || carrier2.getName().isEmpty()
				|| carrier2.getEnabled() == null || carrier2.getCurrencyCode() == null || carrier2.getCurrencyCode().isEmpty()
				|| carrier2.getCarrierReportXref() == null || carrier2.getCarrierReportXref().size()==0
				|| carrier2.getSqlMapperNamespace()== null || carrier2.getSqlMapperNamespace().isEmpty()
				|| carrier2.getCarrierTimeZone() == null || carrier2.getCarrierTimeZone().isEmpty()
				|| carrier2.getDisplayName() == null || carrier2.getDisplayName().isEmpty()
				|| carrier2.getSqlMapFile()==null || carrier2.getSqlMapFile().isEmpty()
				|| carrier2.getTaxRate() == null
				|| carrier2.getTnsFile() == null || carrier2.getTnsFile().isEmpty()
				|| carrier2.getXlsClassName() == null || carrier2.getXlsClassName().isEmpty()
		){
			addFieldError("mandateField", "All fields are mandatory.");
			return false;
		}
		return true;
	}

	public String report(){
		selectedTab = 2;
		try {
			ReportParams rparams = new ReportParams();
			rparams.setSortCol(ReportParams.SORT_COLUMN_DISPLAY_NAME);
			reports = cmrDB.getReports(rparams);
		} catch (CMException e) {
			log.error("Error fetching reports for list ",e);
		}
		return "reportList";
	}
	
	public String reportDetails(){
		selectedTab = 2;
		try {
			if(reportId > 0)
				report = cmrDB.getReport(new ReportParams(reportId));
		} catch (CMException e) {
			log.error("Error fetching details for User ; "+userId,e);
		}
		return "reportDetails";
	}
	
	public String saveReport() {
		selectedTab = 2;
		if (report.getName() == null || report.getName().isEmpty()
				|| report.getDisplayName() == null
				|| report.getDisplayName().isEmpty()
				|| report.getEnabled() == null
				|| report.getFileExtension() == null
				|| report.getFileExtension().getFileExtensionId() == null
				|| report.getDbServiceApiName() == null
				|| report.getDbServiceApiName().isEmpty()
				|| report.getXlsApiName() == null
				|| report.getXlsApiName().isEmpty()) {
			addFieldError("mandateField", "All fields are mandatory.");
			return "reportDetails";
		}
		try {
			report.setUpdatedBy(getLoggedInUser().getUserId());
			ReportParams rparams = new ReportParams();
			rparams.setReportId(report.getReportId());
			rparams.setName(report.getName());
			if (cmrDB.duplicateReportName(rparams)) {
				addFieldError("mandateField",
						"Carrier with this name already exist. Please choose another.");
				return "carrierDetails";
			}
			log.debug("New Report to save : " + report);
			if (report.getReportId() != null) { // This is update
				cmrDB.updateReport(report);
			} else { // This is New Report
				cmrDB.addReport(report);
			}
			reloadList = true;
			alertMessage = "Report Saved Successfully";
		} catch (CMException e) {
			log.error("Error Saving new report :" + report, e);
			addFieldError(
					"mandateField",
					"Some error occurred while saving report. "
							+ e.getMessage());
		}
		return "reportDetails";
	}

	
	public String server(){
		selectedTab = 3;
		try {
			ServerSettingParams ssparams = new ServerSettingParams();
			ssparams.setStartRow(getPageStartIndex());
			ssparams.setNumRows(getPerPageRecCount());
			ssparams.setSortCol(ServerSettingParams.SORT_COLUMN_ID);
			if(getPageTotalRecCount()==0)
				setPageTotalRecCount((int) cmrDB.getServerSettingCount(ssparams));
			if(getPageTotalRecCount()>0)
				serverSettings = cmrDB.getServerSettings(ssparams);
		} catch (CMException e) {
			log.error("Error Fetching Server Setting Details for Id : "+settingId,e);
		}
		return "serverSetList";
	}
	
	public String serverDetails(){
		selectedTab = 3;
		if(settingId>0){
			try {
				serverSetting = cmrDB.getServerSetting(settingId);
			} catch (CMException e) {
				log.error("Error Fetching Server Setting Details for Id : "+settingId,e);
			}
		}
		return "serverSetDetails";
	}
	
	public String saveServerSetting(){
		selectedTab = 3;
		if(serverSetting.getKey()==null || serverSetting.getKey().isEmpty() 
				|| serverSetting.getValue()==null ||serverSetting.getValue().isEmpty()
				|| serverSetting.getEnabled() == null ) {
			addFieldError("mandateField", "All fields are mandatory.");
			return "serverSetDetails";
		}
		
		try {
			ServerSettingParams ssparams = new ServerSettingParams(serverSetting.getId());
			ssparams.setKey(serverSetting.getKey());
			if(!cmrDB.duplicateServerSettingKey(ssparams)){
				serverSetting.setUpdatedBy(getLoggedInUser().getUserId());
				
				if(serverSetting.getId()!=null){
					cmrDB.updateServerSetting(serverSetting);
				} else {
					cmrDB.addServerSetting(serverSetting);
				}
				reloadList = true;
				alertMessage = "Settings Saved Successfully";
			} else {
				addFieldError("mandateField", "This key is already been used.");
			}
		} catch (CMException e) {
			log.error("Error Fetching Server Setting Details for Id : "+settingId,e);
		}
	
		return "serverSetDetails";
	}
	
	public String deleteServerSetting(){
		if(settingId>0){
			log.debug("Deleting Server Setting "+settingId);
			try{
				cmrDB.deleteServerSetting(settingId);
			}catch(CMException e){
				log.error("Error Deleting Server Settings "+settingId,e);
			}
		}
		return server();
	}
	
	/*
	 * Schedular Details 
	 * */
	
	public String schedulerDetails(){
		selectedTab = 4;
		try {
			schMD = ScheduleReport.getMetaData();
		} catch (SchedulerException e) {
			log.error("Error getting scheduler metadata",e);
		}
		return "schedulerDetails";
	}
	
	public String schStandBy(){
		log.debug("Will schStandBy the Schedular");
		try {
			ScheduleReport.standBy();
		} catch (SchedulerException e) {
			log.error("Error while putting the schedular in standby mode.",e);
			addActionError("Error while placing the schedular in standby mode.");
		}
		return SUCCESS;
	}
	
	public String schReStart(){
		log.debug("Will Resume the Schedular");
		try {
			ScheduleReport.restart();
		} catch (SchedulerException e) {
			log.error("Error while restarting the schedular from standby mode.",e);
			addActionError("Error while restarting the schedular from standby mode.");
		}
		return SUCCESS;
	}
	
	public String staticValues(){
		selectedTab = 5;
		return "staticValues";
	}
	
	public String reloadStaticValues(){
		RequestParams rp = new RequestParams();
		rp.setStartRow(getPageStartIndex());
		rp.setNumRows(getPerPageRecCount());
		rp.setExpiredJobs(false);
		rp.setActiveJobs(true);
		
		try{
			int activeJob = cmrDB.getRequestForUserCount(rp).intValue();
			if(activeJob==0){
				ScheduleReport.reloadStaticConfig();
				MailAgent.reloadStaticConfig();
				CarrierSqlSessionConfig.reloadStaticConfig();
				XLSWriter.reloadStaticConfig();
				addActionMessage("All static values are reloaded.");
			} else {
				addActionMessage(activeJob +"  Job(s) currently executing. Please try later.");
			}
			
		} catch (Exception e) {
			log.error("Error reloadStaticValues "+rp,e);
			addActionError("Some error occured . Please try later."+e.getMessage());
		}
		return "staticValues";
	}
	
	
	/*
	 * Setter & Getter methods Defined below
	 * */
	public CMDBService getCmrDB() {
		return cmrDB;
	}

	public void setCmrDB(CMDBService cmrDB) {
		this.cmrDB = cmrDB;
	}

	public Collection<UserMasterDTO> getUsers() {
		return users;
	}

	public void setUsers(Collection<UserMasterDTO> users) {
		this.users = users;
	}

	public Collection<CarrierMasterDTO> getCarriers() {
		return carriers;
	}

	public void setCarriers(Collection<CarrierMasterDTO> carriers) {
		this.carriers = carriers;
	}

	public UserMasterDTO getUser() {
		return user;
	}

	public void setUser(UserMasterDTO user) {
		this.user = user;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Collection<ReportMasterDTO> getReports() {
		return reports;
	}

	public void setReports(Collection<ReportMasterDTO> reports) {
		this.reports = reports;
	}

	public Collection<ServerSettingsDTO> getServerSettings() {
		return serverSettings;
	}

	public void setServerSettings(Collection<ServerSettingsDTO> serverSettings) {
		this.serverSettings = serverSettings;
	}

	public ServerSettingsDTO getServerSetting() {
		return serverSetting;
	}

	public void setServerSetting(ServerSettingsDTO serverSetting) {
		this.serverSetting = serverSetting;
	}

	public long getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(long carrierId) {
		this.carrierId = carrierId;
	}

	public CarrierMasterDTO getCarrier() {
		return carrier;
	}

	public void setCarrier(CarrierMasterDTO carrier) {
		this.carrier = carrier;
	}

	public ReportMasterDTO getReport() {
		return report;
	}

	public void setReport(ReportMasterDTO report) {
		this.report = report;
	}

	public long getReportId() {
		return reportId;
	}

	public void setReportId(long reportId) {
		this.reportId = reportId;
	}

	public long getSettingId() {
		return settingId;
	}

	public void setSettingId(long settingId) {
		this.settingId = settingId;
	}

	public SchedulerMetaData getSchMD() {
		return schMD;
	}

	public void setSchMD(SchedulerMetaData schMD) {
		this.schMD = schMD;
	}

	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}

	public boolean isReloadList() {
		return reloadList;
	}

	public void setReloadList(boolean reloadList) {
		this.reloadList = reloadList;
	}

	public int getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(int selectedTab) {
		this.selectedTab = selectedTab;
	}

}
