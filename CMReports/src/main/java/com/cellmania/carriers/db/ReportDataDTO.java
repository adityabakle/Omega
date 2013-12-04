package com.cellmania.carriers.db;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import com.cellmania.cmreports.common.ObjectConvertor;

public class ReportDataDTO {
	private String developerName;
	private String companyName;
	private int appTypeId;
	private String itemName;
	private double sellPrice;
	private String  deviceType;
	private String externamProviderFlag;
	private double d3DRevenuw;
	private int noOfOrders;
	private double totalSellPrice;
	private double netSales;
	private int bundleTypeId;
	private String bundleName;
	// ExceptionReport
	private double cpShare;			// revenue_share
	private double cpSharePct;			// revenue_share
	private double carrierShare;	// revenue_share2
	private double cellmaniaShare;	// revenue_share3
	private double cellmaniaSharePct;	// revenue_share3
	private double thirdParty;		// revenue_share4
	private String settlementName;
	private String settlementDispName; // For Virgin
	private double totalShare;		// sumation of all above Shares.
	private String publisherFlag;
	private Date dtLastUpdated;
	
	private String qpassProductId; // for Kjeet
	private int refund;
	private int itemId;
	private String directFlag;
	private String subscription;
    private String itemType;
    private int itemTypeId;
    private String itemSubType;
    private int itemSubTypeId;
    private String deviceDisplayName;
    private int noOfRefunds;
    private int noOfZeroOrders;
    private int noOfFreeNWApps;
    private double salesTax;
    private double salesAdjust;
    private double platformRevShare;
    private double testingRevShare;
    private double settlementRevShare;
    private double carrierInvoice;
    private double csPct;
    private double zeroPriceSales;
	
    private double totalRevPct;
    private double operatorShare;
    private double operatorSharePct;
    private double operatorCollected;
    private double dueOperator;
    private double primiumContent;
    private String orderDate;
    private String launchDate;
    
    private String prepaid;
    private String deviceName;
    private String ptn;
    private String externalItemId;
	private int dpId;
	
	//Singtel 
	private String longDesc;
	private String shortDesc;
	private String fileSize;
	Collection<String> devices;
	Collection<String> mappedCat;
	private int networkAware;
	private String priceModelName;
	private int circleCode;
	private String circleName;
	
	//Airtel
	HashMap<String,Integer> priceCount = null;
	private int catId;
	private String catName;
	private double renewalPrice;
	
	public String getDeveloperName() {
		return developerName;
	}
	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public int getAppTypeId() {
		return appTypeId;
	}
	public void setAppTypeId(int appTypeId) {
		this.appTypeId = appTypeId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public double getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(double sellPrice) {
		this.sellPrice = sellPrice;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getExternamProviderFlag() {
		return externamProviderFlag;
	}
	public void setExternamProviderFlag(String externamProviderFlag) {
		this.externamProviderFlag = externamProviderFlag;
	}
	public double getD3DRevenuw() {
		return d3DRevenuw;
	}
	public void setD3DRevenuw(double revenuw) {
		d3DRevenuw = revenuw;
	}
	public int getNoOfOrders() {
		return noOfOrders;
	}
	public void setNoOfOrders(int noOfOrders) {
		this.noOfOrders = noOfOrders;
	}
	public double getTotalSellPrice() {
		return totalSellPrice;
	}
	public void setTotalSellPrice(double totalSellPrice) {
		this.totalSellPrice = totalSellPrice;
	}
	
	public double getCpShare() {
		return cpShare;
	}
	public void setCpShare(double cpShare) {
		this.cpShare = cpShare;
	}
	public double getCarrierShare() {
		return carrierShare;
	}
	public void setCarrierShare(double carrierShare) {
		this.carrierShare = carrierShare;
	}
	public double getCellmaniaShare() {
		return cellmaniaShare;
	}
	public void setCellmaniaShare(double cellmaniaShare) {
		this.cellmaniaShare = cellmaniaShare;
	}
	public double getThirdParty() {
		return thirdParty;
	}
	public void setThirdParty(double thirdParty) {
		this.thirdParty = thirdParty;
	}
	public String getSettlementName() {
		return settlementName;
	}
	public void setSettlementName(String settlementName) {
		this.settlementName = settlementName;
	}
	public String getSettlementDispName() {
		return settlementDispName;
	}
	public void setSettlementDispName(String settlementDispName) {
		this.settlementDispName = settlementDispName;
	}
	public double getTotalShare() {
		return totalShare;
	}
	public void setTotalShare(double totalShare) {
		this.totalShare = totalShare;
	}
	public String getPublisherFlag() {
		return publisherFlag;
	}
	public void setPublisherFlag(String publisherFlag) {
		this.publisherFlag = publisherFlag;
	}
	public Date getDtLastUpdated() {
		return dtLastUpdated;
	}
	public String getDtLastUpdated(String szFromat) {
		SimpleDateFormat sdf = new SimpleDateFormat(szFromat);
		return sdf.format(this.dtLastUpdated).toString();
	}
	public void setDtLastUpdated(Date dtlastUpdated) {
		this.dtLastUpdated = dtlastUpdated;
	}
	public String getQpassProductId() {
		return qpassProductId;
	}
	public void setQpassProductId(String passProductId) {
		qpassProductId = passProductId;
	}
	public int getRefund() {
		return refund;
	}
	public void setRefund(int refund) {
		this.refund = refund;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public String getDirectFlag() {
		return directFlag;
	}
	public void setDirectFlag(String directFlag) {
		this.directFlag = directFlag;
	}
	public String getSubscription() {
		return subscription;
	}
	public void setSubscription(String subscription) {
		this.subscription = subscription;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public int getItemTypeId() {
		return itemTypeId;
	}
	public void setItemTypeId(int itemTypeId) {
		this.itemTypeId = itemTypeId;
	}
	public String getDeviceDisplayName() {
		return deviceDisplayName;
	}
	public void setDeviceDisplayName(String deviceDisplayName) {
		this.deviceDisplayName = deviceDisplayName;
	}
	public int getNoOfRefunds() {
		return noOfRefunds;
	}
	public void setNoOfRefunds(int noOfRefunds) {
		this.noOfRefunds = noOfRefunds;
	}
	public int getNoOfZeroOrders() {
		return noOfZeroOrders;
	}
	public void setNoOfZeroOrders(int noOfZeroOrders) {
		this.noOfZeroOrders = noOfZeroOrders;
	}
	public int getNoOfFreeNWApps() {
		return noOfFreeNWApps;
	}
	public void setNoOfFreeNWApps(int noOfFreeNWApps) {
		this.noOfFreeNWApps = noOfFreeNWApps;
	}
	public double getSalesTax() {
		return salesTax;
	}
	public void setSalesTax(double salesTax) {
		this.salesTax = salesTax;
	}
	public double getSalesAdjust() {
		return salesAdjust;
	}
	public void setSalesAdjust(double salesAdjust) {
		this.salesAdjust = salesAdjust;
	}
	public double getPlatformRevShare() {
		return platformRevShare;
	}
	public void setPlatformRevShare(double platformRevShare) {
		this.platformRevShare = platformRevShare;
	}
	public double getTestingRevShare() {
		return testingRevShare;
	}
	public void setTestingRevShare(double testingRevShare) {
		this.testingRevShare = testingRevShare;
	}
	public double getSettlementRevShare() {
		return settlementRevShare;
	}
	public void setSettlementRevShare(double settlementRevShare) {
		this.settlementRevShare = settlementRevShare;
	}
	public double getCarrierInvoice() {
		return carrierInvoice;
	}
	public void setCarrierInvoice(double carrierInvoice) {
		this.carrierInvoice = carrierInvoice;
	}
	public double getCsPct() {
		return csPct;
	}
	public void setCsPct(double csPct) {
		this.csPct = csPct;
	}
	public double getZeroPriceSales() {
		return zeroPriceSales;
	}
	public void setZeroPriceSales(double zeroPriceSales) {
		this.zeroPriceSales = zeroPriceSales;
	}
	public double getNetSales() {
		return netSales;
	}
	public void setNetSales(double netSales) {
		this.netSales = netSales;
	}
	public double getCpSharePct() {
		return cpSharePct;
	}
	public void setCpSharePct(double cpSharePct) {
		this.cpSharePct = cpSharePct;
	}
	public double getCellmaniaSharePct() {
		return cellmaniaSharePct;
	}
	public void setCellmaniaSharePct(double cellmaniaSharePct) {
		this.cellmaniaSharePct = cellmaniaSharePct;
	}
	public double getTotalRevPct() {
		return totalRevPct;
	}
	public void setTotalRevPct(double totalRevPct) {
		this.totalRevPct = totalRevPct;
	}
	public double getOperatorShare() {
		return operatorShare;
	}
	public void setOperatorShare(double operatorShare) {
		this.operatorShare = operatorShare;
	}
	public double getOperatorSharePct() {
		return operatorSharePct;
	}
	public void setOperatorSharePct(double operatorSharePct) {
		this.operatorSharePct = operatorSharePct;
	}
	public double getOperatorCollected() {
		return operatorCollected;
	}
	public void setOperatorCollected(double operatorCollected) {
		this.operatorCollected = operatorCollected;
	}
	public double getDueOperator() {
		return dueOperator;
	}
	public void setDueOperator(double dueOperator) {
		this.dueOperator = dueOperator;
	}
	public String getPrepaid() {
		return prepaid;
	}
	public void setPrepaid(String prepaid) {
		this.prepaid = prepaid;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getLaunchDate() {
		return launchDate;
	}
	public void setLaunchDate(String launchDate) {
		this.launchDate = launchDate;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String toString() {
		return ObjectConvertor.convertToString(this);
	}
	public String getPtn() {
		return ptn;
	}
	public void setPtn(String ptn) {
		this.ptn = ptn;
	}
	public String getExternalItemId() {
		return externalItemId;
	}
	public double getPrimiumContent() {
		return primiumContent;
	}
	public void setPrimiumContent(double primiumContent) {
		this.primiumContent = primiumContent;
	}
	public void setExternalItemId(String externalItemId) {
		this.externalItemId = externalItemId;
	}
	public int getDpId() {
		return dpId;
	}
	public void setDpId(int dpId) {
		this.dpId = dpId;
	}
	public String getLongDesc() {
		return longDesc;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public void setLongDesc(String desc) {
		longDesc = desc;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public Collection<String> getDevices() {
		return devices;
	}
	public void setDevices(Collection<String> devices) {
		this.devices = devices;
	}
	public String getDevicesCSV() {
		StringBuffer sb = new StringBuffer();
		if(devices!=null && devices.size()>0){
			int iC = 0;
			for(String sD:devices) {
				iC++;
				if(sb.length()>0) {
					sb.append(", ");
				if(iC>6)
					sb.append("\n");
				iC=0;
				}
				sb.append(sD);
			}
		}
		return sb.toString();
			
	}
	public String getDevicesCSVTelstra3g() {
		StringBuffer sb = new StringBuffer();
		if(devices!=null && devices.size()>0){
			for(String sD:devices) {
				if(sb.length()>0) {
					sb.append(", ");
				}
				sb.append(sD);
			}
			String ds = sb.toString();
			StringTokenizer st = new StringTokenizer(ds,",");
			HashSet<String> hs = new HashSet<String>();
			while(st.hasMoreTokens()){
				hs.add(st.nextToken());
			}
			sb = new StringBuffer();
			for(String sD:hs) {
				if(sb.length()>0) {
					sb.append(", ");
				}
				sb.append(sD);
			}
		}
		return sb.toString();
			
	}
	public Collection<String> getMappedCat() {
		return mappedCat;
	}
	public void setMappedCat(Collection<String> mappedCat) {
		this.mappedCat = mappedCat;
	}
	public String getMappedCatCSV() {
		StringBuffer sb = new StringBuffer();
		if(mappedCat!=null && mappedCat.size()>0){
			int iC = 0;
			for(String sD:mappedCat) {
				iC++;
				if(sb.length()>0) {
					sb.append(", ");
				if(iC>6)
					sb.append("\n");
				iC=0;
				}
				sb.append(sD);
			}
		}
		return sb.toString();
			
	}
	
	public int getNetworkAware() {
		return networkAware;
	}
	public void setNetworkAware(int networkAware) {
		this.networkAware = networkAware;
	}
	public String getPriceModelName() {
		return priceModelName;
	}
	public void setPriceModelName(String priceModelName) {
		this.priceModelName = priceModelName;
	}
	public int getCircleCode() {
		return circleCode;
	}
	public void setCircleCode(int circleCode) {
		this.circleCode = circleCode;
	}
	public String getCircleName() {
		return circleName;
	}
	public void setCircleName(String circleName) {
		this.circleName = circleName;
	}
	public double getPriceInRs(double price){
		if(price==0) return 0;
		else if(price>0 && price<1) return 25;
		else if (price>=1 && price<3) return 50;
		else if (price>=3 && price<5) return 99;
		else if (price>=5 && price<7) return 150;
		else if (price>=7) return 199;
		else return 0;
		
	}
	public int getBundleTypeId() {
		return bundleTypeId;
	}
	public void setBundleTypeId(int bundleTypeId) {
		this.bundleTypeId = bundleTypeId;
	}
	public String getBundleName() {
		return bundleName;
	}
	public void setBundleName(String bundleName) {
		this.bundleName = bundleName;
	}
	public String getItemSubType() {
		return itemSubType;
	}
	public void setItemSubType(String itemSubType) {
		this.itemSubType = itemSubType;
	}
	public int getItemSubTypeId() {
		return itemSubTypeId;
	}
	public void setItemSubTypeId(int itemSubTypeId) {
		this.itemSubTypeId = itemSubTypeId;
	}
	public HashMap<String, Integer> getPriceCount() {
		return priceCount;
	}
	public void setPriceCount(HashMap<String, Integer> priceCount) {
		this.priceCount = priceCount;
	}
	
	public void addToPriceCount(String price, Integer priceCnt) {
		if(this.priceCount==null) {
			this.priceCount = new HashMap<String, Integer>();
		}
		
		double dPrice = Double.parseDouble(price);
		//System.out.println("PRICE : '"+price+ "' :>  "+dPrice);
		
		String strPrice = String.valueOf((int)dPrice);
		if(this.priceCount.containsKey(strPrice)) {
			this.priceCount.put(strPrice, this.priceCount.get(strPrice) + priceCnt);
		} else {
			this.priceCount.put(strPrice, priceCnt);
		}
		this.noOfZeroOrders = this.noOfZeroOrders - priceCnt;
	}
	public int getCatId() {
		return catId;
	}
	public void setCatId(int catId) {
		this.catId = catId;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public double getRenewalPrice() {
		return renewalPrice;
	}
	public void setRenewalPrice(double renewalPrice) {
		this.renewalPrice = renewalPrice;
	}
}
