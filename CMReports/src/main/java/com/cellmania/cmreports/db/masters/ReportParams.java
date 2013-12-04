package com.cellmania.cmreports.db.masters;

import com.cellmania.cmreports.common.ObjectConvertor;
import com.cellmania.cmreports.db.BaseParams;

public class ReportParams extends BaseParams {
	
	public static final int SORT_COLUMN_REPORT_ID = 1;
	public static final int SORT_COLUMN_NAME = 2;
	public static final int SORT_COLUMN_DISPLAY_NAME = 3;
	public static final int SORT_COLUMN_ENABLED = 4;
	
	private Long reportId;
	private String name;
	private Boolean enabled;
	private Long carrierId;
	
	public ReportParams(){
		
	}
	
	public ReportParams(long reportId) {
		this.reportId = reportId;
	}

	public String toString() {
		return ObjectConvertor.convertToString(this);
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Long getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Long carrierId) {
		this.carrierId = carrierId;
	}
}
