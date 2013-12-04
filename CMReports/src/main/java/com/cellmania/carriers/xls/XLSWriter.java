package com.cellmania.carriers.xls;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cellmania.carriers.db.ReportDataDTO;
import com.cellmania.carriers.util.Header;
import com.cellmania.carriers.util.Utility;
import com.cellmania.carriers.util.XLSUtil;
import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.common.ObjectConvertor;
import com.cellmania.cmreports.db.request.RequestDTO;
import com.cellmania.cmreports.web.util.CMDBService;
import com.cellmania.cmreports.web.util.ServerSettingsConstants;

public class XLSWriter {
	private static Logger log = Logger.getLogger(XLSWriter.class);
	public static final int _XLSX_MAX_ROW_COUNT = 1048576;
	protected XSSFCellStyle csHeader = null;
	protected XSSFCellStyle csAmt = null;
	protected XSSFCellStyle csAmt_Bold=null;
	protected XSSFCellStyle csFileSize=null;
	protected XSSFCellStyle csHLink=null;
	protected XSSFCellStyle csBody = null;
	protected XSSFCellStyle csPTN = null;
	protected XSSFCellStyle csDesc=null;
	protected XSSFCellStyle csDate=null;
	protected XSSFCellStyle csPercentage=null;
	protected XSSFCellStyle csDateLong=null;
	protected XSSFWorkbook wb = new XSSFWorkbook();
	private static String szContentURL;
	static {
		reloadStaticConfig();
	}
	
	
	protected String currencyCode="";
	
	public String getCurrencyCode() {
		return currencyCode;
	}

	public static void reloadStaticConfig() {
		try {
			szContentURL = CMDBService.getServerSettingsValue(ServerSettingsConstants._CONTENT_URL);
		} catch (CMException e) {
			log.error("Error reloading statics in XLSWriter",e);
		}
		
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	protected RequestDTO reqReport;
	
	public RequestDTO getReqReport() {
		return reqReport;
	}

	public void setReqReport(RequestDTO reqReport) {
		this.reqReport = reqReport;
		setCurrencyCode(reqReport.getCarrier().getCurrencyCode());
		createStyles();
	}
	
	public XLSWriter(){
		
	}
	
	private void createStyles() {
		csHeader = wb.createCellStyle();
		XSSFFont boldFont = wb.createFont();
		boldFont.setFontHeightInPoints((short) 10);
		boldFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		
		csHeader.setFont(boldFont);
		csHeader.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		csHeader.setBorderTop(XSSFCellStyle.BORDER_THIN);
		csHeader.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
		csHeader.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		csHeader.setBorderRight(XSSFCellStyle.BORDER_THIN);
		csHeader.setWrapText(true);
		
		// Style for  Amt
		csAmt = wb.createCellStyle();
		csAmt.setDataFormat(wb.createDataFormat().getFormat("[$"+currencyCode+"] #,##0.00_);[Red]([$"+currencyCode+"] #,##0.00)"));
		csAmt.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		csAmt.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		csAmt.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		csAmt_Bold = wb.createCellStyle();
		csAmt_Bold.setFont(boldFont);
		csAmt_Bold.setDataFormat(wb.createDataFormat().getFormat("[$"+currencyCode+"] #,##0.00_);[Red]([$"+currencyCode+"] #,##0.00)"));
		csAmt_Bold.setBorderTop(XSSFCellStyle.BORDER_THIN);
		csAmt_Bold.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
		csAmt_Bold.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		csAmt_Bold.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		csPercentage = wb.createCellStyle();
		csPercentage.setDataFormat(wb.createDataFormat().getFormat("#0.00%"));
		csPercentage.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		csPercentage.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		csPercentage.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		csFileSize = wb.createCellStyle();
		csFileSize.setDataFormat(wb.createDataFormat().getFormat("##0.000 K\\B"));
		csFileSize.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		csFileSize.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		csFileSize.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		// Style for PTN
		csPTN = wb.createCellStyle();
		csPTN.setDataFormat(wb.createDataFormat().getFormat("(###) ###-####"));
		csPTN.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		csPTN.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		csPTN.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		// Style for Body Text
		csBody = wb.createCellStyle();
		csBody.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		csBody.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		csBody.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		csHLink = wb.createCellStyle(); 
		XSSFFont hlink_font = wb.createFont();
		hlink_font.setUnderline(XSSFFont.U_SINGLE);
		hlink_font.setColor(XSSFFont.COLOR_NORMAL);
		//csHLink.setFillForegroundColor(new XSSFColor(Color.));
		csHLink.setFont(hlink_font);
		
		// Style for Body Text
		csDesc = wb.createCellStyle();
		csDesc.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		csDesc.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		csDesc.setBorderRight(XSSFCellStyle.BORDER_THIN);
		csDesc.setWrapText(true);
		
		csDate = wb.createCellStyle();
		csDate.setDataFormat(wb.createDataFormat().getFormat("mmm-yy"));
		csDate.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		csDate.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		csDate.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		csDateLong = wb.createCellStyle();
		csDateLong.setDataFormat(wb.createDataFormat().getFormat("dd-mmm-yy"));
		csDateLong.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		csDateLong.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		csDateLong.setBorderRight(XSSFCellStyle.BORDER_THIN);
	}
	
	protected void writeDeviceCatSheet(XSSFSheet s,
			Collection<ReportDataDTO> collection, ArrayList<String> catHeader) {
		XSSFRow r = null;
		int rownum = 0;
		String prevDevice = null;
		r = s.createRow(rownum++);
		int iCell = 0;
		XLSUtil.addCellToRow(r, iCell++, "Device\\Category", csHeader);
		for (String cat : catHeader) {
			XLSUtil.addCellToRow(r, iCell++, cat, csHeader);
		}

		for (ReportDataDTO rd : collection) {
			if (prevDevice == null || !prevDevice.equals(rd.getDeviceType())) {
				r = s.createRow(rownum++);
				XLSUtil.addCellsToRow(r, 1, catHeader.size(), 0, csBody);
				XLSUtil.addCellToRow(r, 0, rd.getDeviceType(), csBody);
			}
			XLSUtil.addCellToRow(r,
					catHeader.indexOf(rd.getDeviceDisplayName()) + 1,
					rd.getNoOfOrders(), csBody);
			prevDevice = rd.getDeviceType();
		}

		for (int j = 0; j < catHeader.size(); j++) {
			s.autoSizeColumn((short) j);
		}

	}
	
	protected void writeLIRPricePointSheet(XSSFSheet s,
			Collection<ReportDataDTO> collection, ArrayList<String> priceHeader) {
		XSSFRow r = null;
		int rownum = 0;
		int iSht = 0;
		r = s.createRow(rownum++);
		int iCount = 0;
		int startIndex = 0;
		XLSUtil.addCellToRow(r, iCount++, "Category", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "No of \nTitles", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "Paid", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "FREE", csHeader);

		for (String price : priceHeader) {
			XLSUtil.addCellToRow(r, iCount++, Double.parseDouble(price),
					csAmt_Bold);
		}

		for (ReportDataDTO rpt : collection) {
			iCount = 0;
			r = s.createRow(rownum++);
			XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceDisplayName(),
					csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
			XLSUtil.addCellToRow(r, iCount++,
					rpt.getNoOfOrders() - rpt.getNoOfZeroOrders(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfZeroOrders(), csBody);
			startIndex = iCount;
			if (rpt.getPriceCount() != null && rpt.getPriceCount().size() > 0) {
				HashMap<String, Integer> pc = rpt.getPriceCount();
				for (String pH : priceHeader) {
					if (pc.containsKey(pH)) {
						XLSUtil.addCellToRow(r, priceHeader.indexOf(pH)
								+ startIndex, pc.get(pH), csBody);
					}
				}
			}
			if (rownum >= _XLSX_MAX_ROW_COUNT-1) {
				for (int j = 0; j < priceHeader.size() + startIndex; j++) {
					s.autoSizeColumn((short) j);
				}
				iSht++;
				s = wb.createSheet("..ctd_" + iSht);
				rownum = 0;
			}
		}

		for (int j = 0; j < priceHeader.size() + startIndex; j++) {
			s.autoSizeColumn((short) j);
		}

	}
	
	protected void writeLIRDataSheet(XSSFSheet s,
			Collection<ReportDataDTO> rpDto, int rptNo) throws Exception {
		XSSFRow r = null;
		int rownum = 0;
		int iSht = 0;
		r = s.createRow(rownum++);
		int iCount = 0;
		if (rptNo == 1) {
			XLSUtil.addCellToRow(r, iCount, "Title", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 50));
			XLSUtil.addCellToRow(r, iCount, Header.COMPANY_NAME, csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 35));
			XLSUtil.addCellToRow(r, iCount, "Short Description", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 100));
			XLSUtil.addCellToRow(r, iCount, "Detailed Description", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 100));
			XLSUtil.addCellToRow(r, iCount, "Launch Date", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 20));
			XLSUtil.addCellToRow(r, iCount, "Rs. Price", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 15));
			XLSUtil.addCellToRow(r, iCount, "Rs. \nRenewal Price", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 15));
			XLSUtil.addCellToRow(r, iCount, "Price Model", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 35));
			XLSUtil.addCellToRow(r, iCount, "File Size (KB)", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 15));
			XLSUtil.addCellToRow(r, iCount, "No. Of \nBuilds", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 15));
			XLSUtil.addCellToRow(r, iCount, "Supported Devices", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 100));
			XLSUtil.addCellToRow(r, iCount, "Mapped Categories", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 50));
			XLSUtil.addCellToRow(r, iCount, "OS", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 20));
			XLSUtil.addCellToRow(r, iCount, "Network Aware", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 10));
			XLSUtil.addCellToRow(r, iCount, "Status", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 10));
			XLSUtil.addCellToRow(r, iCount, "Phone Preview Image", csHeader);
			s.setColumnWidth((short) iCount++, (short) (256 * 10));
			r.setHeightInPoints(25);

		} else {
			String colName = "";
			switch (rptNo) {
			case 2:
				colName = "O.S.";
				break;
			case 3:
				colName = "Category";
				break;
			case 4:
				colName = Header.DEVICE;
				break;
			case 5:
				colName = "Price Model";
			}
			XLSUtil.addCellToRow(r, iCount++, colName, csHeader);
			XLSUtil.addCellToRow(r, iCount++, "No of \nItems", csHeader);
		}
		s.createFreezePane(0, rownum);
		log.debug("Headers Generated");
		int noOfCells = 0;

		if (rptNo == 1) {
			//log.info("No of Records = " + rpDto.size());
			String szUrl = null;
			String invlFileSize = null; // "Following Item(s) have invalid file size value. File size should only have numbers.\n";
			
			for (ReportDataDTO rpt : rpDto) {
				iCount = 0;
				r = s.createRow(rownum++);
				
				szContentURL.replace("{0}", reqReport.getCarrier().getName());
				szUrl = szContentURL.replace("{0}", reqReport.getCarrier().getName())
						+ rpt.getCompanyName() + "/" + rpt.getItemId() + "/"
						+ rpt.getExternalItemId();
				XLSUtil.addCellToRow(r, iCount++, szUrl,
						rpt.getDeviceDisplayName(), csHLink);
				XLSUtil.addCellToRow(r, iCount++, rpt.getDeveloperName(),
						csBody);
				XLSUtil.addCellToRow(r, iCount++, rpt.getShortDesc(), csDesc);
				XLSUtil.addCellToRow(r, iCount++,
						rpt.getLongDesc().replace("\r\n", "\n"), csDesc);
				XLSUtil.addCellToRow(r, iCount++, ObjectConvertor.getDate(
						rpt.getLaunchDate(), "dd-MMM-yyyy"), csDateLong);
				XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt);
				XLSUtil.addCellToRow(r, iCount++, rpt.getRenewalPrice(), csAmt);
				XLSUtil.addCellToRow(r, iCount++, rpt.getPriceModelName(),
						csBody);
				double filesize = 0;
				try {
					filesize = Double.parseDouble(rpt.getFileSize()) / 1024;
				} catch (Exception e) {
					if (null == invlFileSize) {
						invlFileSize = new String();
						invlFileSize = reqReport.getCarrier().getName()+ " Invalid file size item List.\n";
					} 
					invlFileSize += rpt.getDeviceDisplayName() + " : '" + rpt.getFileSize() + "'\n";
					
					// log.error("Invalid Number in File size in the item : "+rpt);
				}
				if (filesize > 0) {
					XLSUtil.addCellToRow(r, iCount++, filesize, csFileSize);
				} else {
					XLSUtil.addCellToRow(r, iCount++, rpt.getFileSize(),
							csFileSize);
				}
				XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
				XLSUtil.addCellToRow(r, iCount++, rpt.getDevicesCSV(), csDesc);
				XLSUtil.addCellToRow(r, iCount++, rpt.getMappedCatCSV(), csDesc);
				XLSUtil.addCellToRow(r, iCount++, rpt.getItemType(), csBody);
				XLSUtil.addCellToRow(r, iCount++,
						(rpt.getNetworkAware() == 0 ? "False" : "True"), csBody);
				XLSUtil.addCellToRow(r, iCount++, rpt.getPublisherFlag(),
						csBody);
				XLSUtil.addCellToRow(r, iCount++, szUrl, csBody);
				noOfCells = iCount;
				if (rownum >= 1000000) {
					for (int j = 0; j < noOfCells; j++) {
						s.autoSizeColumn((short) j);
					}
					iSht++;
					s = wb.createSheet("..ctd_" + iSht);
					rownum = 0;
				}
				
			}
			if (invlFileSize != null ) {
				log.error(invlFileSize);
			}
		} else {
			for (ReportDataDTO rpt : rpDto) {
				iCount = 0;
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceDisplayName(),
						csBody);
				XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
				noOfCells = iCount;
				if (rownum >= _XLSX_MAX_ROW_COUNT-1) {
					for (int j = 0; j < noOfCells; j++) {
						s.autoSizeColumn((short) j);
					}
					iSht++;
					s = wb.createSheet("..ctd_" + iSht);
					rownum = 0;
				}
			}
		}

		r = s.createRow(rownum++);
		log.debug("Detail Report generated");

		if (rptNo != 1) {
			for (int j = 0; j < noOfCells; j++) {
				s.autoSizeColumn((short) j);
			}
		}

		log.debug("Live Item report - DONE");
	}
	
	public String createExceptionReport(Collection<ReportDataDTO> colRevShare,Collection<ReportDataDTO> colRevChange){
		log.debug("In writeExceptionReport for "+getReqReport().getCarrier().getDisplayName());
		String fileName = Utility.getFileName(getReqReport());
		try {
			
			log.info("Report file name : " + fileName);
			long time = System.currentTimeMillis();
			
			wb = new XSSFWorkbook();
			createStyles();
			
			XSSFSheet s = wb.createSheet("Rev Share Exception");
			XSSFRow r = null;
			
			
			int rownum = 0;
			// Generate Headers
			r = s.createRow(rownum++);
			String szVal = reqReport.getCarrier().getDisplayName() + " " +
					   reqReport.getReport().getDisplayName()+ " " + 
					   ObjectConvertor.simpleDate(reqReport.getStartDate(),"dd-MMM-yy") + " to " + ObjectConvertor.simpleDate(reqReport.getEndDate(),"dd-MMM-yyyy");

			XLSUtil.addCellToRow(r, 0, szVal, csHeader);
			s.addMergedRegion(new CellRangeAddress(rownum-1,rownum-1,0,7));
			//rownum++;
			
			r = s.createRow(rownum++);
			
			r = s.createRow(rownum++);
			int iCount = 0;
			XLSUtil.addCellToRow(r, iCount++, Header.COMPANY_NAME, csHeader);	
			XLSUtil.addCellToRow(r, iCount++, Header.QPASS_ID+"/"+Header.SETTLEMENT_NAME, csHeader);
			XLSUtil.addCellToRow(r, iCount++, Header.PUBLISHER_FLAG, csHeader);
			XLSUtil.addCellToRow(r, iCount++, Header.CP_SHARE, csHeader);	
			XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
		    XLSUtil.addCellToRow(r, iCount++, Header.CELLMANIA_SHARE, csHeader);
		    XLSUtil.addCellToRow(r, iCount++, Header._3RD_PARTY_SHARE, csHeader);
		    XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
			
		    log.debug("Headers for revenue share created.");
		    
			if(colRevShare!=null && colRevShare.size()>0) {
				for(ReportDataDTO rpt : colRevShare) {
					iCount = 0;
					r = s.createRow(rownum++);
					// Company Name
					XLSUtil.addCellToRow(r, iCount++, rpt.getCompanyName(), csBody);
				
					// 	Settlement Name / Qpass Product Id
					XLSUtil.addCellToRow(r, iCount++,  rpt.getQpassProductId()==null?rpt.getSettlementName(): rpt.getQpassProductId(), csBody);
					
					//Publisher Flag
					XLSUtil.addCellToRow(r, iCount++, rpt.getPublisherFlag(), csBody);
						
					// CP Share
					XLSUtil.addCellToRow(r, iCount++, rpt.getCpShare()/100, csPercentage);
					
					// Carrier Share
					XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare()/100, csPercentage);
					
					// Cellmania Share
					XLSUtil.addCellToRow(r, iCount++, rpt.getCellmaniaShare()/100, csPercentage);
						
					// 3rd Party Rights Share
					XLSUtil.addCellToRow(r, iCount++, rpt.getThirdParty()/100, csPercentage);	
					
					// Total
					XLSUtil.addCellToRow(r, iCount++, rpt.getTotalShare()/100, csPercentage);
				}
				
				for(int i = 0;i<iCount;i++){
					s.autoSizeColumn((short) i);
				}
			}
			else {
				r = s.createRow(rownum);
				// No Repords Found
				s.addMergedRegion(new CellRangeAddress(rownum-1,rownum-1,0,7));
				XLSUtil.addCellToRow(r, 0, "No Records found", null);
				rownum++;
			}
			log.debug("Revenue share report created.");
			r = s.createRow(rownum++);
			r = s.createRow(rownum++);
			r = s.createRow(rownum++);
			
			r = s.createRow(rownum);
			s.addMergedRegion(new CellRangeAddress(rownum-1,rownum-1,0,7));
			XLSUtil.addCellToRow(r, 0, getReqReport().getCarrier().getDisplayName() 
								+ "Exception Report - Settlement ids that changed during "
								+ ObjectConvertor.simpleDate(reqReport.getStartDate(),"dd-MMM-yy") + " to " 
								+ ObjectConvertor.simpleDate(reqReport.getEndDate(),"dd-MMM-yyyy"), csHeader);
			rownum++;
			r = s.createRow(rownum++);
			// Generate Headers
			r = s.createRow(rownum++);
			iCount = 0;
			XLSUtil.addCellToRow(r, iCount++, Header.COMPANY_NAME, csHeader);			    
			XLSUtil.addCellToRow(r, iCount++, Header.QPASS_ID+"/"+Header.SETTLEMENT_NAME, csHeader);
			XLSUtil.addCellToRow(r, iCount++, Header.LAST_UPDATE, csHeader); //csDate
		    XLSUtil.addCellToRow(r, iCount++, "Updated By", csHeader); //csDate
			XLSUtil.addCellToRow(r, iCount++, Header.CP_SHARE, csHeader);	
			XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
		    XLSUtil.addCellToRow(r, iCount++, Header.CELLMANIA_SHARE, csHeader);
		    XLSUtil.addCellToRow(r, iCount++, Header._3RD_PARTY_SHARE, csHeader);
		    XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
		    
		    log.debug("Header for Id changed report created.");
		    
			if(colRevChange!=null && colRevChange.size()>0) {
				for(ReportDataDTO rpt : colRevChange) {
					iCount = 0;
					r = s.createRow(rownum++);
					// Company Name
					XLSUtil.addCellToRow(r, iCount++, rpt.getCompanyName(), csBody);
					
					XLSUtil.addCellToRow(r, iCount++,  rpt.getQpassProductId()==null?rpt.getSettlementName(): rpt.getQpassProductId(), csBody);
					//Last Updated
					XLSUtil.addCellToRow(r, iCount++, rpt.getDtLastUpdated(), csDate);
					XLSUtil.addCellToRow(r, iCount++, rpt.getDeveloperName(), csBody);
					// CP Share
					XLSUtil.addCellToRow(r, iCount++, rpt.getCpShare()/100, csPercentage);
					
					// Carrier Share
					XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare()/100, csPercentage);
					
					// Cellmania Share
					XLSUtil.addCellToRow(r, iCount++, rpt.getCellmaniaShare()/100, csPercentage);
						
					// 3rd Party Rights Share
					XLSUtil.addCellToRow(r, iCount++, rpt.getThirdParty()/100, csPercentage);	
					
					// Total
					XLSUtil.addCellToRow(r, iCount++, rpt.getTotalShare()/100, csPercentage);
				}
				
				for(int i = 0;i<iCount;i++){
					s.autoSizeColumn((short) i);
				}
			}
			else {
				r = s.createRow(rownum);
				// No Records Found
				s.addMergedRegion(new CellRangeAddress(rownum-1,rownum-1,0,7));
				XLSUtil.addCellToRow(r, 0, "No Records found", null);
			}
			log.debug("Report changed ID created.");
			
			
			File f = new File(Utility.checkFileName(reqReport.getReportPath() + fileName, 0, getReqReport().getFileExtension()));
			fileName = f.getName();
			FileOutputStream out = new FileOutputStream(f);
			wb.write(out);
			out.close();
			log.info("XLS Generated in : " + (System.currentTimeMillis() - time) + " : FILE Name : " + fileName);
		} catch (Exception e) {
			log.error("Cxception in creating Exception Report : " + e,e);
			e.printStackTrace();
		}
		return fileName;
	}
}

