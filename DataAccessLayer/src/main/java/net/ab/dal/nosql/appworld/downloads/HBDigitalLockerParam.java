package net.ab.dal.nosql.appworld.downloads;

import net.ab.dal.nosql.hbase.HBaseBean;
import net.ab.dal.nosql.hbase.annotations.HBaseColumn;
import net.ab.dal.nosql.hbase.annotations.HBaseRow;
import net.ab.dal.nosql.hbase.annotations.HBaseTable;
import net.ab.dal.nosql.hbase.common.ObjectPrinter;

/**
 *
 */
@HBaseTable (tableName="digitallocker", columnFamily="locker")
public class HBDigitalLockerParam extends HBaseBean {
	@HBaseRow (keyIndex=0)
	private Long customerId;
	@HBaseRow (keyIndex=1)
	private Long productTypeId;
	@HBaseRow (keyIndex=2)
	private Long contentTypeId;
	@HBaseRow (keyIndex=3)
	private Long contentId;
	@HBaseRow (keyIndex=4)
	private Long releaseId;
	
	@HBaseColumn
	private Integer status;
	@HBaseColumn
	private Long lastDownloadDate;
	@HBaseColumn
	private String deleteFlag;
	
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}

	public Long getContentTypeId() {
		return contentTypeId;
	}

	public void setContentTypeId(Long contentTypeId) {
		this.contentTypeId = contentTypeId;
	}

	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Long getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(Long releaseId) {
		this.releaseId = releaseId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getLastDownloadDate() {
		return lastDownloadDate;
	}

	public void setLastDownloadDate(Long lastDownloadDate) {
		this.lastDownloadDate = lastDownloadDate;
	}

	public String toString(){
		return ObjectPrinter.convertToString(this);
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	@Override
	public boolean isRowKeyComplete() {
		// TODO Auto-generated method stub
		return false;
	}
}
