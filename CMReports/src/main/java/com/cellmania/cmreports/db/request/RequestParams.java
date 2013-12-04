package com.cellmania.cmreports.db.request;

import com.cellmania.cmreports.common.ObjectConvertor;
import com.cellmania.cmreports.db.BaseParams;

public class RequestParams extends BaseParams{
	
	private Long requestId;
	private Long userId;
	private String email;
	private Long reportId;
	private Long carrierId;
	private Long exeLogId;
	private boolean activeJobs;  // if set to true only active non expired jobs will be shown. else all jobs will be listed
	private boolean expiredJobs; // if true then only expired jobs will be selected.
	private boolean allJobs = false; // gets all type of jobs from DB
	private boolean failedJobs = false; // gets all type of jobs from DB
	
	public RequestParams() {
		
	}
	
	public RequestParams(Long requestId) {
		this.requestId = requestId;
	}

	public Long getRequestId() {
		return requestId;
	}
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getReportId() {
		return reportId;
	}
	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
	public Long getCarrierId() {
		return carrierId;
	}
	public void setCarrierId(Long carrierId) {
		this.carrierId = carrierId;
	}
	
	public Long getExeLogId() {
		return exeLogId;
	}

	public void setExeLogId(Long exeLogId) {
		this.exeLogId = exeLogId;
	}

	public boolean isActiveJobs() {
		return activeJobs;
	}

	public void setActiveJobs(boolean activeJobs) {
		this.activeJobs = activeJobs;
	}

	public boolean isExpiredJobs() {
		return expiredJobs;
	}

	public void setExpiredJobs(boolean expiredJobs) {
		this.expiredJobs = expiredJobs;
	}

	public boolean isAllJobs() {
		return allJobs;
	}

	public void setAllJobs(boolean allJobs) {
		this.allJobs = allJobs;
	}

	public boolean isFailedJobs() {
		return failedJobs;
	}

	public void setFailedJobs(boolean failedJobs) {
		this.failedJobs = failedJobs;
	}

	public String toString(){
		return ObjectConvertor.convertToString(this);
	}
}
