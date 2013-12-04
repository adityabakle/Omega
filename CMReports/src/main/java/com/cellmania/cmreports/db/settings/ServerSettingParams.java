package com.cellmania.cmreports.db.settings;

import com.cellmania.cmreports.common.ObjectConvertor;
import com.cellmania.cmreports.db.BaseParams;

public class ServerSettingParams extends BaseParams{
	public static final int SORT_COLUMN_KEY = 3;
	
	private Long id;
	private String key;
	private Boolean enabled;
	
	public ServerSettingParams(){
		
	}
	
	public ServerSettingParams(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String toString() {
		return ObjectConvertor.convertToString(this);
	}
}
