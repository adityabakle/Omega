/**
 * 
 */
package com.cellmania.cmreports.db.masters;

import java.util.Collection;
import java.util.Date;

import com.cellmania.cmreports.common.ObjectConvertor;

/**
 * @author abakle
 *
 */
public class CarrierMasterDTO {
	private Long carrierId;
	private String name;
	private String displayName;
	private String sqlMapFile;
	private String tnsFile;
	private String sqlMapperNamespace;
	private String carrierTimeZone;
	private Double taxRate;
	private String currencyCode;
	private String xlsClassName;
	private String xlsxClassName;
	private Boolean enabled;
	private Date createdDate;
	private Date modifiedDate;
	private Long updatedBy;
	private String updatedByName;
	private Collection<Long> mappedReportIds;
	private Collection<CarrierReportXrefDTO> carrierReportXref;
	private Boolean tnsLookup;
	
	private String dbTnsName;
	private String dbServerName;
	private String dbServiceId;
	private Long dbPort;
	private String dbUserName;
	private String dbPassword;
	
	
	public CarrierMasterDTO(){
		
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

	public String getSqlMapFile() {
		return sqlMapFile;
	}

	public void setSqlMapFile(String sqlMapFile) {
		this.sqlMapFile = sqlMapFile;
	}

	public String getTnsFile() {
		return tnsFile;
	}

	public void setTnsFile(String tnsFile) {
		this.tnsFile = tnsFile;
	}

	public String getSqlMapperNamespace() {
		return sqlMapperNamespace;
	}

	public void setSqlMapperNamespace(String sqlMapperNamespace) {
		this.sqlMapperNamespace = sqlMapperNamespace;
	}

	public String getCarrierTimeZone() {
		return carrierTimeZone;
	}

	public void setCarrierTimeZone(String carrierTimeZone) {
		this.carrierTimeZone = carrierTimeZone;
	}

	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getXlsClassName() {
		return xlsClassName;
	}

	public void setXlsClassName(String xlsClassName) {
		this.xlsClassName = xlsClassName;
	}

	public String getXlsxClassName() {
		return xlsxClassName;
	}

	public void setXlsxClassName(String xlsxClassName) {
		this.xlsxClassName = xlsxClassName;
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

	public Collection<Long> getMappedReportIds() {
		return mappedReportIds;
	}

	public void setMappedReportIds(Collection<Long> mappedReportIds) {
		this.mappedReportIds = mappedReportIds;
	}

	public Collection<CarrierReportXrefDTO> getCarrierReportXref() {
		return carrierReportXref;
	}

	public void setCarrierReportXref(
			Collection<CarrierReportXrefDTO> carrierReportXref) {
		this.carrierReportXref = carrierReportXref;
	}

	public Boolean getTnsLookup() {
		return tnsLookup;
	}

	public void setTnsLookup(Boolean tnsLookup) {
		this.tnsLookup = tnsLookup;
	}

	public String getDbTnsName() {
		return dbTnsName;
	}

	public void setDbTnsName(String dbTnsName) {
		this.dbTnsName = dbTnsName;
	}

	public String getDbServerName() {
		return dbServerName;
	}

	public void setDbServerName(String dbServerName) {
		this.dbServerName = dbServerName;
	}

	public String getDbServiceId() {
		return dbServiceId;
	}

	public void setDbServiceId(String dbServiceId) {
		this.dbServiceId = dbServiceId;
	}

	public Long getDbPort() {
		return dbPort;
	}

	public void setDbPort(Long dbPort) {
		this.dbPort = dbPort;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String toString(){
		return ObjectConvertor.convertToString(this);
	}
}
