package net.ab.dal.nosql.appworld.reviews;

import net.ab.dal.nosql.hbase.HBaseBean;
import net.ab.dal.nosql.hbase.annotations.HBaseColumn;
import net.ab.dal.nosql.hbase.annotations.HBaseRow;
import net.ab.dal.nosql.hbase.annotations.HBaseTable;
import net.ab.dal.nosql.hbase.common.ObjectPrinter;

/**
 * RowKey: <code>contentId-platformDeviceMapId-countryId-languageId-releaseId-reverseTimeStamp</code><br/><br/>
 * <code>reverseTimeStamp</code> - will be set by HBaseOps during add review.
 * @author abakle
 * @version BBW 4.4.0
 *
 */
@HBaseTable (tableName="review", columnFamily="rd")
public class HBReviewDAO extends HBaseBean {
	@HBaseRow (keyIndex=0)
	private Long contentId;
	@HBaseRow (keyIndex=1)
	private Integer platformDeviceMapId;
	@HBaseRow (keyIndex=2)
	private Long countryId;
	@HBaseRow (keyIndex=3)
	private Long languageId;
	@HBaseRow (keyIndex=4)
	private Long releaseId;
	@HBaseRow (keyIndex=5)
	private Long reverseTimeStamp;
	
	@HBaseColumn
	private String title;
	@HBaseColumn
	private String review;
	@HBaseColumn
	private Integer status;
	@HBaseColumn (qualifier="custNickName")
	private String customerNickName;
	@HBaseColumn (qualifier="custPinId")
	private Long customerPinId;
	@HBaseColumn (qualifier="devModelId")
	private Long deviceModelId;
	@HBaseColumn (qualifier="fbId")
	private Long fileBundleId;
	@HBaseColumn (qualifier="rating")
	private Integer ratingValue;
	
	public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public Integer getPlatformDeviceMapId() {
		return platformDeviceMapId;
	}
	public void setPlatformDeviceMapId(Integer platformDeviceMapId) {
		this.platformDeviceMapId = platformDeviceMapId;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public Long getLanguageId() {
		return languageId;
	}
	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}
	public Long getReleaseId() {
		return releaseId;
	}
	public void setReleaseId(Long releaseId) {
		this.releaseId = releaseId;
	}
	public Long getReverseTimeStamp() {
		return reverseTimeStamp;
	}
	public void setReverseTimeStamp(Long reverseTimeStamp) {
		this.reverseTimeStamp = reverseTimeStamp;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCustomerNickName() {
		return customerNickName;
	}
	public void setCustomerNickName(String customerNickName) {
		this.customerNickName = customerNickName;
	}
	public Long getCustomerPinId() {
		return customerPinId;
	}
	public void setCustomerPinId(Long customerPinId) {
		this.customerPinId = customerPinId;
	}
	public Long getDeviceModelId() {
		return deviceModelId;
	}
	public void setDeviceModelId(Long deviceModelId) {
		this.deviceModelId = deviceModelId;
	}
	public Long getFileBundleId() {
		return fileBundleId;
	}
	public void setFileBundleId(Long fileBundleId) {
		this.fileBundleId = fileBundleId;
	}
	
	public Integer getRatingValue() {
		return ratingValue;
	}
	public void setRatingValue(Integer ratingValue) {
		this.ratingValue = ratingValue;
	}
	public String toString(){
		return ObjectPrinter.convertToString(this);
	}
	
	@Override
	public boolean isRowKeyComplete() {
		return isRowKeyComplete(true);
	}
	
	public boolean isRowKeyComplete(boolean  checkTimeStamp){
		if(null!=this.contentId && null!=this.countryId
				&& null!=this.languageId && null!=this.platformDeviceMapId 
				&& null!=this.releaseId && (null!=this.reverseTimeStamp || !checkTimeStamp))
			return true;
		else return false;
		
	}
}
