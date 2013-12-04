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
public class RoleDTO {
	private Long roleId;
	private String name;
	private String description;
	private Boolean enabled;
	private Date createdDate;
	private Date modifiedDate;
	private Long updatedBy;
	
	public RoleDTO(){
		super();
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	public String toString(){
		return ObjectConvertor.convertToString(this);
	}
}
