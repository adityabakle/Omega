package com.cellmania.cmreports.db.masters;

import com.cellmania.cmreports.common.ObjectConvertor;

public class CarrierReportXrefDTO {
	private Long carrierId;
	private Long reportId;
	
	public CarrierReportXrefDTO(){
		
	}
	
	public Long getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Long carrierId) {
		this.carrierId = carrierId;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String toString(){
		return ObjectConvertor.convertToString(this);
	}
}
