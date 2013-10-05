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
@HBaseTable(tableName="download", columnFamily="dl")
public class HBDownloadDAO extends HBaseBean {
	
	@HBaseRow (keyIndex=0)
	private Long customerId;
	@HBaseRow (keyIndex=1)
	private Long productTypeId;
	@HBaseRow (keyIndex=2)
	private Long contentTypeId;
	@HBaseRow (keyIndex=3)
	private Long contentId;
	@HBaseRow (keyIndex=4)
	private Long reverseTimeStamp;
	
	@HBaseColumn(qualifier="fbId")
	private Long fileBundleId;
	@HBaseColumn
	private Long releaseId;
	@HBaseColumn
	private String blackberryModel;
	@HBaseColumn(qualifier="devPin")
	private Long devicePin;
	@HBaseColumn(qualifier="devOsVersion")
	private Long deviceOsVersion;
	@HBaseColumn
	private String locale;
	@HBaseColumn
	private Long carrierId;
	@HBaseColumn
	private Long countryId;
	@HBaseColumn(qualifier="devVendId")
	private Long deviceVendorId;
	@HBaseColumn(qualifier="currMCCId")
	private Long currentMCCId;
	@HBaseColumn
	private Long listGroupId;
	@HBaseColumn
	private Long listId;
	private Long awVersion;
	@HBaseColumn(qualifier="sfId")
	private Long storefrontId;
	@HBaseColumn
	private Long customerPinId;

	@HBaseColumn(qualifier="pdtmId")
	private Integer platformDeviceTypeMapId;
	
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

	public Long getReverseTimeStamp() {
		return reverseTimeStamp;
	}

	public void setReverseTimeStamp(Long reverseTimeStamp) {
		this.reverseTimeStamp = reverseTimeStamp;
	}

	public Long getFileBundleId() {
		return fileBundleId;
	}

	public void setFileBundleId(Long fileBundleId) {
		this.fileBundleId = fileBundleId;
	}

	public Long getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(Long releaseId) {
		this.releaseId = releaseId;
	}

	public String getBlackberryModel() {
		return blackberryModel;
	}

	public void setBlackberryModel(String blackberryModel) {
		this.blackberryModel = blackberryModel;
	}

	public Long getDevicePin() {
		return devicePin;
	}

	public void setDevicePin(Long devicePin) {
		this.devicePin = devicePin;
	}

	public Long getDeviceOsVersion() {
		return deviceOsVersion;
	}

	public void setDeviceOsVersion(Long deviceOsVersion) {
		this.deviceOsVersion = deviceOsVersion;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Long getCarrierId() {
		return carrierId;
	}

	public void setCarrierId(Long carrierId) {
		this.carrierId = carrierId;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getDeviceVendorId() {
		return deviceVendorId;
	}

	public void setDeviceVendorId(Long deviceVendorId) {
		this.deviceVendorId = deviceVendorId;
	}

	public Long getCurrentMCCId() {
		return currentMCCId;
	}

	public void setCurrentMCCId(Long currentMCCId) {
		this.currentMCCId = currentMCCId;
	}

	public Long getListGroupId() {
		return listGroupId;
	}

	public void setListGroupId(Long listGroupId) {
		this.listGroupId = listGroupId;
	}

	public Long getListId() {
		return listId;
	}

	public void setListId(Long listId) {
		this.listId = listId;
	}

	public Long getAwVersion() {
		return awVersion;
	}

	public void setAwVersion(Long awVersion) {
		this.awVersion = awVersion;
	}

	public Long getStorefrontId() {
		return storefrontId;
	}

	public void setStorefrontId(Long storefrontId) {
		this.storefrontId = storefrontId;
	}

	public Long getCustomerPinId() {
		return customerPinId;
	}

	public void setCustomerPinId(Long customerPinId) {
		this.customerPinId = customerPinId;
	}

	public Integer getPlatformDeviceTypeMapId() {
		return platformDeviceTypeMapId;
	}

	public void setPlatformDeviceTypeMapId(Integer platformDeviceTypeMapId) {
		this.platformDeviceTypeMapId = platformDeviceTypeMapId;
	}

	@Override
	public boolean isRowKeyComplete() {
		// TODO Auto-generated method stub
		return false;
	}

}
