/**
 * 
 */
package com.cellmania.cmreports.db.request;

import java.util.Date;

import com.cellmania.cmreports.common.ObjectConvertor;

/**
 * @author abakle
 *
 */
public class ExecutionLogDTO {
	private Long id;
	private RequestDTO request;
	private Date startTime;
	private Date endTime;
	private Date reportStartDate;
	private Date reportEndDate;
	private Double taxRate;
	private Double currencyConversion;
	private Boolean success;
	private String errorReason;
	private String fileName;
	private String mailedTo;
	private String mailedCc;
	private Boolean attached;
	
	public ExecutionLogDTO(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RequestDTO getRequest() {
		return request;
	}

	public void setRequest(RequestDTO request) {
		this.request = request;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getReportStartDate() {
		return reportStartDate;
	}

	public void setReportStartDate(Date reportStartDate) {
		this.reportStartDate = reportStartDate;
	}

	public Date getReportEndDate() {
		return reportEndDate;
	}

	public void setReportEndDate(Date reportEndDate) {
		this.reportEndDate = reportEndDate;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public Double getCurrencyConversion() {
		return currencyConversion;
	}

	public void setCurrencyConversion(Double currencyConversion) {
		this.currencyConversion = currencyConversion;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMailedTo() {
		return mailedTo;
	}

	public void setMailedTo(String mailedTo) {
		this.mailedTo = mailedTo;
	}

	public String getMailedCc() {
		return mailedCc;
	}

	public void setMailedCc(String mailedCc) {
		this.mailedCc = mailedCc;
	}

	public Boolean getAttached() {
		return attached;
	}

	public void setAttached(Boolean attached) {
		this.attached = attached;
	}
	
	public String toString(){
		return ObjectConvertor.convertToString(this);
	}
}
