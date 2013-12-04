package com.cellmania.cmreports.db.masters;

import com.cellmania.cmreports.common.ObjectConvertor;

public class FileExtensionDTO {
	private Long fileExtensionId;
	private String name;
	private String extension;
	private Boolean enabled;
	
	public FileExtensionDTO(){
		
	}

	public Long getFileExtensionId() {
		return fileExtensionId;
	}

	public void setFileExtensionId(Long fileExtensionId) {
		this.fileExtensionId = fileExtensionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	public String toString(){
		return ObjectConvertor.convertToString(this);
	}
}
