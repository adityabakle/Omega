package com.cellmania.cmreports.db.masters;

import com.cellmania.cmreports.common.ObjectConvertor;
import com.cellmania.cmreports.db.BaseParams;

public class CarrierParams extends BaseParams {
	public static final int SORT_COLUMN_NAME = 2;
	public static final int SORT_COLUMN_DISPLAY_NAME = 3;
	public static final int SORT_COLUMN_ENABLED = 4;
	
	private Long carrierId;
	private String name;
	private String displayName;
	private Boolean enabled;
	
	private Long userId;
	
	public CarrierParams(){
		
	}
	
	public CarrierParams(Long carrierId){
		super();
		this.carrierId = carrierId;
	}
	
	public String toString() {
		return ObjectConvertor.convertToString(this);
	}

	public Long getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Long carrierId) {
		this.carrierId = carrierId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
