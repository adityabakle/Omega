/**
 * 
 */
package com.cellmania.cmreports.db.request;

import java.util.Date;

import com.cellmania.cmreports.common.ObjectConvertor;
import com.cellmania.cmreports.db.masters.CarrierMasterDTO;
import com.cellmania.cmreports.db.masters.ReportMasterDTO;
import com.cellmania.cmreports.db.masters.UserMasterDTO;

/**
 * @author abakle
 *
 */
public class RequestDTO {
	public static final String _EXPIRED = "expired";
	public static final String _SCHEDULED = "scheduled";
	public static final String _ACTIVE = "active";
	
	private Long requestId;
	private UserMasterDTO requestedBy;
	private CarrierMasterDTO carrier;
	private ReportMasterDTO report;
	private Date initialStartDate;
	private Date initialEndDate;
	private Date startDate;
	private Date endDate;
	private String fileNamePrefix;
	private String fileExtension;
	private Double currencyConversion;
	private Boolean includeCP;
	private Boolean includeBundles;
	private Double taxRate;
	private Date scheduledDate;
	private Date lastExecutedDate;
	private Date nextExecutionDate;
	private FrequencyDTO frequency;
	private MailDTO mail;
	private int x;
	private int y;
	private int xelement;
	private int yelement;
	private String sql;
	private String csvHeader;
	private Date createdDate;
	private Date modifiedDate;
	private Long updatedBy;
	private String updatedByName;
	private Date expiryDate;
	private String state;
	
	// From ServerSettings
	private String reportPath;
	private String serverTimeZone;
	
	public RequestDTO(){
		
	}

	public Long getRequestId() {
		return requestId;
	}

	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}

	public UserMasterDTO getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(UserMasterDTO requestedBy) {
		this.requestedBy = requestedBy;
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

	public Date getInitialStartDate() {
		return initialStartDate;
	}

	public void setInitialStartDate(Date initialStartDate) {
		this.initialStartDate = initialStartDate;
	}

	public Date getInitialEndDate() {
		return initialEndDate;
	}

	public void setInitialEndDate(Date initialEndDate) {
		this.initialEndDate = initialEndDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getFileNamePrefix() {
		return fileNamePrefix;
	}

	public void setFileNamePrefix(String fileNamePrefix) {
		this.fileNamePrefix = fileNamePrefix;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public Double getCurrencyConversion() {
		return currencyConversion;
	}

	public void setCurrencyConversion(Double currencyConversion) {
		this.currencyConversion = currencyConversion;
	}

	public Boolean getIncludeCP() {
		return includeCP;
	}

	public void setIncludeCP(Boolean includeCP) {
		this.includeCP = includeCP;
	}

	public Boolean getIncludeBundles() {
		return includeBundles;
	}

	public void setIncludeBundles(Boolean includeBundles) {
		this.includeBundles = includeBundles;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public Date getLastExecutedDate() {
		return lastExecutedDate;
	}

	public void setLastExecutedDate(Date lastExecutedDate) {
		this.lastExecutedDate = lastExecutedDate;
	}

	public Date getNextExecutionDate() {
		return nextExecutionDate;
	}

	public void setNextExecutionDate(Date nextExecutionDate) {
		this.nextExecutionDate = nextExecutionDate;
	}

	public FrequencyDTO getFrequency() {
		return frequency;
	}

	public void setFrequency(FrequencyDTO frequency) {
		this.frequency = frequency;
	}

	public MailDTO getMail() {
		return mail;
	}

	public void setMail(MailDTO mail) {
		this.mail = mail;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getXelement() {
		return xelement;
	}

	public void setXelement(int xelement) {
		this.xelement = xelement;
	}

	public int getYelement() {
		return yelement;
	}

	public void setYelement(int yelement) {
		this.yelement = yelement;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getCsvHeader() {
		return csvHeader;
	}

	public void setCsvHeader(String csvHeader) {
		this.csvHeader = csvHeader;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedByName() {
		return updatedByName;
	}

	public void setUpdatedByName(String updatedByName) {
		this.updatedByName = updatedByName;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getReportPath() {
		return reportPath;
	}

	public void setReportPath(String reportPath) {
		this.reportPath = reportPath;
	}

	public String getServerTimeZone() {
		return serverTimeZone;
	}

	public void setServerTimeZone(String serverTimeZone) {
		this.serverTimeZone = serverTimeZone;
	}

	public String toString(){
		return ObjectConvertor.convertToString(this);
	}
}
