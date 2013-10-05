package net.ab.dal.nosql.appworld.downloads;

import net.ab.dal.nosql.hbase.HBaseBean;
import net.ab.dal.nosql.hbase.annotations.HBaseColumn;
import net.ab.dal.nosql.hbase.annotations.HBaseRow;
import net.ab.dal.nosql.hbase.annotations.HBaseTable;

/**
 * @author abakle
 * @version BBW 4.x
 *
 */
@HBaseTable(tableName="digitallocker", columnFamily="locker")
public class HBDigitalLockerDAO extends HBaseBean {
	
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
	private Long parentContentId;
	@HBaseColumn
	private Long showContentId;
	@HBaseColumn
	private Long trackContentId;
	@HBaseColumn
	private Long releaseContentId;
	@HBaseColumn
	private Integer status;
	@HBaseColumn
	private Long licenseTypeId;
	@HBaseColumn
	private String fromTrack;
	@HBaseColumn
	private String fromRelease;
	@HBaseColumn
	private String deleteFlag;
	@HBaseColumn
	private Long lastDownloadDate;


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

	public Long getParentContentId() {
		return parentContentId;
	}

	public void setParentContentId(Long parentContentId) {
		this.parentContentId = parentContentId;
	}

	public Long getShowContentId() {
		return showContentId;
	}

	public void setShowContentId(Long showContentId) {
		this.showContentId = showContentId;
	}

	public Long getTrackContentId() {
		return trackContentId;
	}

	public void setTrackContentId(Long trackContentId) {
		this.trackContentId = trackContentId;
	}

	public Long getReleaseContentId() {
		return releaseContentId;
	}

	public void setReleaseContentId(Long releaseContentId) {
		this.releaseContentId = releaseContentId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getLicenseTypeId() {
		return licenseTypeId;
	}

	public void setLicenseTypeId(Long licenseTypeId) {
		this.licenseTypeId = licenseTypeId;
	}

	public String getFromTrack() {
		return fromTrack;
	}

	public void setFromTrack(String fromTrack) {
		this.fromTrack = fromTrack;
	}

	public String getFromRelease() {
		return fromRelease;
	}

	public void setFromRelease(String fromRelease) {
		this.fromRelease = fromRelease;
	}

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Long getLastDownloadDate() {
		return lastDownloadDate;
	}

	public void setLastDownloadDate(Long lastDownloadDate) {
		this.lastDownloadDate = lastDownloadDate;
	}

	@Override
	public boolean isRowKeyComplete() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean isRowKeyComplete(boolean  checkTimeStamp){
		if (null != this.contentId && null != this.customerId
			&& null != this.productTypeId && null != this.contentTypeId
			&& null != releaseId) {
			return true;
		}
		return false;
	}
}
