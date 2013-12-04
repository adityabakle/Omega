/**
 * 
 */
package com.cellmania.cmreports.db.masters;

import java.util.Date;

import com.cellmania.cmreports.common.ObjectConvertor;

/**
 * @author abakle
 *
 */
public class ReportMasterDTO {
	private Long reportId;
	private String name;
	private String displayName;
	private String fileNamePrefix;
	private FileExtensionDTO fileExtension;
	private String dbServiceApiName;
	private String xlsApiName;
	private Boolean enabled;
	private Date createdDate;
	private Date modifiedDate;
	private Long updatedBy;
	private String updatedByName;
	private String csvHeader;
	
	public ReportMasterDTO(){
		
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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getFileNamePrefix() {
		return fileNamePrefix;
	}

	public void setFileNamePrefix(String fileNamePrefix) {
		this.fileNamePrefix = fileNamePrefix;
	}

	public FileExtensionDTO getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(FileExtensionDTO fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getDbServiceApiName() {
		return dbServiceApiName;
	}

	public void setDbServiceApiName(String dbServiceApiName) {
		this.dbServiceApiName = dbServiceApiName;
	}

	public String getXlsApiName() {
		return xlsApiName;
	}

	public void setXlsApiName(String xlsApiName) {
		this.xlsApiName = xlsApiName;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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

	public String getCsvHeader() {
		return csvHeader;
	}

	public void setCsvHeader(String csvHeader) {
		this.csvHeader = csvHeader;
	}

	public String toString(){
		return ObjectConvertor.convertToString(this);
	}
}
