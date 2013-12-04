package com.cellmania.cmreports.db.request;

import com.cellmania.cmreports.common.ObjectConvertor;

public class FrequencyDTO {
	private Long frequencyId;
	private String code;
	private String name;
	private Long incrementPeriod;
	
	public FrequencyDTO(){
		
	}

	public Long getFrequencyId() {
		return frequencyId;
	}

	public void setFrequencyId(Long frequencyId) {
		this.frequencyId = frequencyId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getIncrementPeriod() {
		return incrementPeriod;
	}

	public void setIncrementPeriod(Long incrementPeriod) {
		this.incrementPeriod = incrementPeriod;
	}
	
	public String toString(){
		return ObjectConvertor.convertToString(this);
	}
}
