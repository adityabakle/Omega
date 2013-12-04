package com.cellmania.cmreports.db.masters;

import com.cellmania.cmreports.common.ObjectConvertor;

public class UserCarrierXrefDTO {
	private Long userId;
	private Long carrierId;
	
	public UserCarrierXrefDTO(){
		
	}
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Long carrierId) {
		this.carrierId = carrierId;
	}

	public String toString(){
		return ObjectConvertor.convertToString(this);
	}
}
