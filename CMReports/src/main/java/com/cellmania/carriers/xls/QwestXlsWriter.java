package com.cellmania.carriers.xls;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.cellmania.carriers.db.ReportDataDTO;
import com.cellmania.carriers.util.Header;
import com.cellmania.carriers.util.Utility;
import com.cellmania.carriers.util.XLSUtil;
import com.cellmania.cmreports.common.ObjectConvertor;



public class QwestXlsWriter extends XLSWriter{
	Logger log = Logger.getLogger(this.getClass());
	public QwestXlsWriter() {
		super();
	}
	
	public String createRevShareReport(HashMap<String, Collection<ReportDataDTO>> hmReport) {
		log.debug("In RevShare report "+reqReport.getCarrier().getDisplayName());
		String fileName = Utility.getFileName(getReqReport());
		try {
			long time = System.currentTimeMillis();
			if(hmReport!=null) {
				
				XSSFSheet s = null;
				if (!getReqReport().getIncludeCP()) {
					
					s = wb.createSheet("Report_Excluding_CPShare");
					writeRevShareRptSheet(s,hmReport, false, false);
					
					s = wb.createSheet("Devloper_Excluding_CPShare");
					writeRevShareRptSheet(s,hmReport, true, false);
				}
				else {
					s = wb.createSheet("Report_Including_CPShare");
					writeRevShareRptSheet(s,hmReport,false, true);
					s = wb.createSheet("Devloper_Including_CPShare");
					writeRevShareRptSheet(s,hmReport, true, true);
					s = wb.createSheet("Report_Excluding_CPShare");
					writeRevShareRptSheet(s,hmReport, false, false);
					s = wb.createSheet("Devloper_Excluding_CPShare");
					writeRevShareRptSheet(s,hmReport, true, false);
				}
				File f = new File(Utility.checkFileName(reqReport.getReportPath() + fileName, 0, getReqReport().getFileExtension()));
				fileName = f.getName();
				FileOutputStream out = new FileOutputStream(f);
				wb.write(out);
				out.close();
				log.info("XLS Generated in : "
						+ (System.currentTimeMillis() - time)
						+ " : FILE NAme : " + fileName);
			}
			
		} catch (Exception e) {
			log.error("Exception writing XLS : " + e);
			e.printStackTrace();
		}
		return fileName;
	}
	
	private void writeRevShareRptSheet(XSSFSheet s,HashMap<String, Collection<ReportDataDTO>> hmReport, boolean devRpt, boolean includeCP) throws Exception {
		XSSFRow r = null;
		Collection<ReportDataDTO> colReport = hmReport.get("rptDetails");
		int rownum = 0;
		int iSht = 0;
		// Generate Headers
		r = s.createRow(rownum++);
		String szVal = reqReport.getCarrier().getDisplayName() + " " +
				   reqReport.getReport().getDisplayName()+ " " + 
				   ObjectConvertor.simpleDate(reqReport.getStartDate(),"dd-MMM-yy") + " to " + ObjectConvertor.simpleDate(reqReport.getEndDate(),"dd-MMM-yyyy");
		
		XLSUtil.addCellToRow(r, 0, szVal, csHeader);
		s.addMergedRegion(new CellRangeAddress(rownum-1,rownum-1,0,7));
		
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		int iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, Header.DATE_TRANS, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.COMPANY_NAME, csHeader);	
		XLSUtil.addCellToRow(r, iCount++, Header.ITEM_ID, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.ITEM_NAME , csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SETTLEMENT_NAME, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.DIR_INDIR, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.ITEM_TYPE, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.DEVICE_DISP_NAME, csHeader);
		
		XLSUtil.addCellToRow(r, iCount++, Header.GROSS_SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_REFUND, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_0_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NET_SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.PLATFORM_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SETTLTMENT_FEE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.TESTING_FEE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.$0_Price_SHARE, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.CP_SHARE, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header._3RD_PARTY_SHARE, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_INV, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CSPCT, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CPPCT, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CMPCT, csHeader);
	    s.createFreezePane(1,rownum);
	    log.debug("Headers Generated");
	    double dTotalSales = 0.0; 
	    double dNetSales=0.0;
	    double dTotalCellmaniaShare=0.0;
	    double dTotalSettFee=0.0; 
	    double dTotalTestFee = 0.0;
	    double dTotalZeroPriceSales = 0.0; 
	    double dTotalCpShare = 0.0;
	    double dTotal3rdShare = 0.0;
	    double dTotalCarSh = 0.0;
	    double dTotalCarInv = 0.0;
	    int iTotlaOrder = 0, iTotalRefunds = 0,iTotalZeroOrders =0;
	    int noOfCells= 0;
	    String prevComp = null;
		double dCompTotal[] = new double[13];
		for(ReportDataDTO rpt : colReport) {
			String szComp = rpt.getCompanyName();					
			iCount = 0;
			
			if(prevComp!=null && !prevComp.equals(szComp) && devRpt){
				r = s.createRow(rownum++);
				XLSUtil.addCellsToRow(r, 0, 7,Header.LINE,csHeader);
				XLSUtil.addCellToRow(r, 7, Header.SUB_TOTAL, csHeader);
				int i = 0;
				for(; i<13; i++){
					if(i>0 && i<4) {
						XLSUtil.addCellToRow(r, i + 8, dCompTotal[i], csHeader);
					}
					else {
						XLSUtil.addCellToRow(r, i + 8, dCompTotal[i], csAmt_Bold);
					}
				}
				XLSUtil.addCellsToRow(r, i+8, 3,Header.LINE,csHeader);
				r = s.createRow(rownum++);
				prevComp = null;
			}
			
			r = s.createRow(rownum++);
			XLSUtil.addCellToRow(r, iCount++, ObjectConvertor.getDate(rpt.getOrderDate(), "MMM-yy"), csDate);
			XLSUtil.addCellToRow(r, iCount++, szComp, csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemId(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemName(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getSettlementName(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getDirectFlag(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemType(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceDisplayName(), csBody);
			
			XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt); 
			dTotalSales += rpt.getSellPrice();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody); 
			iTotlaOrder += rpt.getNoOfOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfRefunds(), csBody);
			iTotalRefunds += rpt.getNoOfRefunds();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfZeroOrders(), csBody);
			iTotalZeroOrders += rpt.getNoOfZeroOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNetSales(), csAmt);
			dNetSales += rpt.getNetSales();
			if(includeCP || "direct".equalsIgnoreCase(rpt.getDirectFlag())) {
				XLSUtil.addCellToRow(r, iCount++, rpt.getCellmaniaShare(), csAmt);
				dTotalCellmaniaShare += rpt.getCellmaniaShare();
			} else 
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
			
			
			XLSUtil.addCellToRow(r, iCount++, rpt.getSettlementRevShare(), csAmt);
			dTotalSettFee += rpt.getSettlementRevShare();
			XLSUtil.addCellToRow(r, iCount++, rpt.getTestingRevShare(), csAmt);
			dTotalTestFee += rpt.getTestingRevShare();
			XLSUtil.addCellToRow(r, iCount++, rpt.getSalesAdjust(), csAmt); // Zero price Sales
			dTotalZeroPriceSales += rpt.getSalesAdjust();
			if(includeCP || "direct".equalsIgnoreCase(rpt.getDirectFlag())) {
				XLSUtil.addCellToRow(r, iCount++, rpt.getCpShare(), csAmt);
				dTotalCpShare += rpt.getCpShare();
			} else 
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
			
			XLSUtil.addCellToRow(r, iCount++, rpt.getThirdParty(), csAmt);
			dTotal3rdShare += rpt.getThirdParty();
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare(), csAmt);
			dTotalCarSh += rpt.getCarrierShare();
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierInvoice(), csAmt);
			dTotalCarInv += rpt.getCarrierInvoice();
			XLSUtil.addCellToRow(r, iCount++, (rpt.getCsPct()/100), csPercentage);
			if(includeCP || "direct".equalsIgnoreCase(rpt.getDirectFlag())) {
				XLSUtil.addCellToRow(r, iCount++, (rpt.getCpSharePct()/100), csPercentage);
				XLSUtil.addCellToRow(r, iCount++, (rpt.getCellmaniaSharePct()/100), csPercentage);
			} else {
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csPercentage);
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csPercentage);
			}
			
			noOfCells = iCount;
			iCount = 0;
			if(prevComp==null){
				dCompTotal[iCount++] = rpt.getSellPrice();
				dCompTotal[iCount++] = rpt.getNoOfOrders();
				dCompTotal[iCount++] = rpt.getNoOfRefunds();
				dCompTotal[iCount++] = rpt.getNoOfZeroOrders();
				dCompTotal[iCount++] = rpt.getNetSales();
				if(includeCP || "direct".equalsIgnoreCase(rpt.getDirectFlag())) {
					dCompTotal[iCount++] = rpt.getCellmaniaShare();
					dCompTotal[iCount++] = rpt.getSettlementRevShare();
				    dCompTotal[iCount++] = rpt.getTestingRevShare();
				    dCompTotal[iCount++] = rpt.getSalesAdjust();
				    dCompTotal[iCount++] = rpt.getCpShare();
				} else {
					dCompTotal[iCount++] = 0;
					dCompTotal[iCount++] = rpt.getSettlementRevShare();
				    dCompTotal[iCount++] = rpt.getTestingRevShare();
				    dCompTotal[iCount++] = rpt.getSalesAdjust();
					dCompTotal[iCount++] = 0;
				}
			    dCompTotal[iCount++] = rpt.getThirdParty();
			    dCompTotal[iCount++] = rpt.getCarrierShare();
			    dCompTotal[iCount++] = rpt.getCarrierInvoice();
			}
			else {
				dCompTotal[iCount++] += rpt.getSellPrice();
				dCompTotal[iCount++] += rpt.getNoOfOrders();
				dCompTotal[iCount++] += rpt.getNoOfRefunds();
				dCompTotal[iCount++] += rpt.getNoOfZeroOrders();
				dCompTotal[iCount++] += rpt.getNetSales();
				if(includeCP || "direct".equalsIgnoreCase(rpt.getDirectFlag())) {
					dCompTotal[iCount++]+= rpt.getCellmaniaShare();
					dCompTotal[iCount++]+= rpt.getSettlementRevShare();
				    dCompTotal[iCount++]+= rpt.getTestingRevShare();
				    dCompTotal[iCount++]+= rpt.getSalesAdjust();
				    dCompTotal[iCount++]+= rpt.getCpShare();
				} else {
					dCompTotal[iCount++] += 0;
					dCompTotal[iCount++] += rpt.getSettlementRevShare();
				    dCompTotal[iCount++] += rpt.getTestingRevShare();
				    dCompTotal[iCount++] += rpt.getSalesAdjust();
					dCompTotal[iCount++] += 0;
				}
			    dCompTotal[iCount++] += rpt.getThirdParty();
			    dCompTotal[iCount++] += rpt.getCarrierShare();
			    dCompTotal[iCount++] += rpt.getCarrierInvoice();
			}
			prevComp = szComp;
			
			if(rownum>=_XLSX_MAX_ROW_COUNT-1) {
				for(int j=0; j<noOfCells; j++) {
					s.autoSizeColumn((short)j);
				}
				iSht++;
				s = wb.createSheet(s.getSheetName()+"..ctd_"+iSht);
				rownum=0;
			}
		}
		if(devRpt) {
			r = s.createRow(rownum++);
			XLSUtil.addCellsToRow(r, 0, 7,Header.LINE,csHeader);
			XLSUtil.addCellToRow(r, 7, Header.SUB_TOTAL, csHeader);
			int i = 0;
			for(; i<13; i++){
				if(i>0 && i<4) {
					XLSUtil.addCellToRow(r, i + 8, dCompTotal[i], csHeader);
				}
				else {
					XLSUtil.addCellToRow(r, i + 8, dCompTotal[i], csAmt_Bold);
				}
			}
			XLSUtil.addCellsToRow(r, i+8, 3,Header.LINE,csHeader);
			r = s.createRow(rownum++);
			prevComp = null;
		}
		log.debug("Detail Report generated");
		log.debug("Generating Summary");
		
		r = s.createRow(rownum++);
		XLSUtil.addCellsToRow(r, 0, 7, Header.LINE , csHeader);
		iCount = 7;
		XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, iTotlaOrder, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalRefunds, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalZeroOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dNetSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCellmaniaShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalSettFee, csAmt_Bold); 
		XLSUtil.addCellToRow(r, iCount++, dTotalTestFee, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalZeroPriceSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCpShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotal3rdShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCarSh, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCarInv, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, Header.LINE,csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.LINE,csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.LINE,csHeader);
		
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, Header.ITEM_TYPE, csHeader);	
		XLSUtil.addCellToRow(r, iCount++, Header.GROSS_SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.MAC_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CELLMANIA_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.PLATFORM_SHARE, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.GB, csHeader);
		r = s.createRow(rownum++);
		colReport = hmReport.get("grandTotal");
		if(colReport!=null) {
			for (ReportDataDTO rpt : colReport) {
				iCount=0;
				XLSUtil.addCellToRow(r, iCount++, rpt.getItemType(), csBody);
				XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt);
				XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare(), csAmt);
				XLSUtil.addCellToRow(r, iCount++, rpt.getCpShare(), csAmt); // MicroVision Share
				XLSUtil.addCellToRow(r, iCount++, rpt.getCellmaniaShare(), csAmt);
				XLSUtil.addCellToRow(r, iCount++, rpt.getPlatformRevShare(), csAmt);
				XLSUtil.addCellToRow(r, iCount++, rpt.getCsPct(), csBody);
			}
		}
		log.debug("Report summary generated");
		for(int j=0; j<noOfCells; j++) {
			s.autoSizeColumn((short)j);
		}
	}

	public String createCreditCardReport(Collection<ReportDataDTO> col) {
		log.debug("In create CreditCard Report "+reqReport.getCarrier().getDisplayName());
		String fileName = Utility.getFileName(getReqReport());
		
		try {
			long time = System.currentTimeMillis();
			if(col!=null) {
				
				XSSFSheet s = wb.createSheet("CC_Report");
				XSSFRow r = null;
				int rownum = 0;
				int iSht = 0;
				// Generate Headers
				r = s.createRow(rownum++);
				String szVal = reqReport.getCarrier().getDisplayName() + " " +
						   reqReport.getReport().getDisplayName()+ " " + 
						   ObjectConvertor.simpleDate(reqReport.getStartDate(),"dd-MMM-yy") + " to " + ObjectConvertor.simpleDate(reqReport.getEndDate(),"dd-MMM-yyyy");
				
				XLSUtil.addCellToRow(r, 0, szVal, csHeader);
				s.addMergedRegion(new CellRangeAddress(rownum-1,rownum-1,0,7));
				
				r = s.createRow(rownum++);
				r = s.createRow(rownum++);
				int iCount = 0;
				XLSUtil.addCellToRow(r, iCount++, "Transaction\nDate", csHeader);
				XLSUtil.addCellToRow(r, iCount++, "Order ID", csHeader);
				XLSUtil.addCellToRow(r, iCount++, "Subscriber ID", csHeader);
				XLSUtil.addCellToRow(r, iCount++, "Transaction ID", csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.ITEM_NAME , csHeader);
				XLSUtil.addCellToRow(r, iCount++, "Settlement\nID" , csHeader);
				XLSUtil.addCellToRow(r, iCount++, "CC", csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.PRICE, csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.NET_SALES, csHeader);
				XLSUtil.addCellToRow(r, iCount++, "Tax", csHeader);
				XLSUtil.addCellToRow(r, iCount++, "Shipping\nCost", csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
			    XLSUtil.addCellToRow(r, iCount++, "Royalty", csHeader);
			    
			    XLSUtil.addCellToRow(r, iCount++, "CC Fee", csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.CELLMANIA_SHARE, csHeader);
				XLSUtil.addCellToRow(r, iCount++, "Owed to\nQwest", csHeader);
				s.createFreezePane(1,rownum);
			    log.debug("Headers Generated");
			    int iTotlaOrder=0;
			    String prevCC = null;
			    double dNetSales=0.0, dTotalTax=0.0,dTotalShippingFee=0.0,dTotalPrice=0.0;
			    double dTotalRoyalty=0.0, dTotalCCFee=0.0,dTotalCellmaniaShare=0.0,dTotalCarSh=0.0;
			    double dCompTotal[] = new double[9];
			    for(ReportDataDTO rpt : col) {
										
					iCount = 0;
					String szCC = rpt.getDirectFlag();					
					
					
					if(prevCC!=null && !prevCC.equals(szCC)){
						r = s.createRow(rownum++);
						XLSUtil.addCellsToRow(r, 0, 7,Header.LINE,csHeader);
						XLSUtil.addCellToRow(r, 7, Header.SUB_TOTAL, csHeader);
						XLSUtil.addCellToRow(r, 8, dCompTotal[0], csHeader);
						int i = 1;
						for(; i<9; i++){
							XLSUtil.addCellToRow(r, i + 8, dCompTotal[i], csAmt_Bold);
						}
						r = s.createRow(rownum++);
						prevCC = null;
					}
					
					r = s.createRow(rownum++);
					XLSUtil.addCellToRow(r, iCount++, ObjectConvertor.getDate(rpt.getOrderDate(), "dd-MMM-yy"), csDateLong);
					XLSUtil.addCellToRow(r, iCount++, rpt.getAppTypeId(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getPtn(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getQpassProductId(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getItemName(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getSettlementName(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getDirectFlag(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt); 
					
					XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody); 
					iTotlaOrder += rpt.getNoOfOrders();
					
					XLSUtil.addCellToRow(r, iCount++, rpt.getNetSales(), csAmt);
					dNetSales+=rpt.getNetSales();
					
					XLSUtil.addCellToRow(r, iCount++, rpt.getTestingRevShare(), csAmt);
					dTotalTax += rpt.getTestingRevShare();
					
					XLSUtil.addCellToRow(r, iCount++, rpt.getSalesAdjust(), csAmt);
					dTotalShippingFee += rpt.getSalesAdjust();
					
					XLSUtil.addCellToRow(r, iCount++, rpt.getTotalSellPrice(), csAmt);
					dTotalPrice += rpt.getTotalSellPrice();
					
					XLSUtil.addCellToRow(r, iCount++, rpt.getSettlementRevShare(), csAmt);
					dTotalRoyalty += rpt.getSettlementRevShare();
					
					XLSUtil.addCellToRow(r, iCount++, rpt.getThirdParty(), csAmt);
					dTotalCCFee += rpt.getThirdParty();
					
					XLSUtil.addCellToRow(r, iCount++, rpt.getCellmaniaShare(), csAmt);
					dTotalCellmaniaShare += rpt.getCellmaniaShare();
					
					XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare(), csAmt);
					dTotalCarSh += rpt.getCarrierShare();
					iCount = 0;
					if(prevCC==null){
						dCompTotal[iCount++] = rpt.getNoOfOrders();
						dCompTotal[iCount++] = rpt.getNetSales();
						dCompTotal[iCount++] = rpt.getTestingRevShare();
						dCompTotal[iCount++] = rpt.getSalesAdjust();
						dCompTotal[iCount++] = rpt.getTotalSellPrice();
						dCompTotal[iCount++] = rpt.getSettlementRevShare();
						dCompTotal[iCount++] = rpt.getThirdParty();
						dCompTotal[iCount++] = rpt.getCellmaniaShare();
						dCompTotal[iCount++] = rpt.getCarrierShare();
					}
					else {
						dCompTotal[iCount++] += rpt.getNoOfOrders();
						dCompTotal[iCount++] += rpt.getNetSales();
						dCompTotal[iCount++] += rpt.getTestingRevShare();
						dCompTotal[iCount++] += rpt.getSalesAdjust();
						dCompTotal[iCount++] += rpt.getTotalSellPrice();
						dCompTotal[iCount++] += rpt.getSettlementRevShare();
						dCompTotal[iCount++] += rpt.getThirdParty();
						dCompTotal[iCount++] += rpt.getCellmaniaShare();
						dCompTotal[iCount++] += rpt.getCarrierShare();
					}
					prevCC = szCC;
					
					if(rownum>=_XLSX_MAX_ROW_COUNT-1) {
						for(int j=0; j<iCount; j++) {
							s.autoSizeColumn((short)j);
						}
						iSht++;
						s = wb.createSheet(s.getSheetName()+"..ctd_"+iSht);
						rownum=0;
					}
				}
			    
				r = s.createRow(rownum++);
				XLSUtil.addCellsToRow(r, 0, 7,Header.LINE,csHeader);
				XLSUtil.addCellToRow(r, 7, Header.SUB_TOTAL, csHeader);
				XLSUtil.addCellToRow(r, 8, dCompTotal[0], csHeader);
				int i = 1;
				for(; i<9; i++){
					XLSUtil.addCellToRow(r, i + 8, dCompTotal[i], csAmt_Bold);
				}
				r = s.createRow(rownum++);
				prevCC = null;
				
			    r = s.createRow(rownum++);
				XLSUtil.addCellsToRow(r, 0, 7, Header.LINE , csHeader);
				iCount = 7;
				XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
				XLSUtil.addCellToRow(r, iCount++, iTotlaOrder, csHeader);
				XLSUtil.addCellToRow(r, iCount++, dNetSales, csAmt_Bold);
				XLSUtil.addCellToRow(r, iCount++, dTotalTax, csAmt_Bold);
				XLSUtil.addCellToRow(r, iCount++, dTotalShippingFee, csAmt_Bold);
				XLSUtil.addCellToRow(r, iCount++, dTotalPrice, csAmt_Bold);
				XLSUtil.addCellToRow(r, iCount++, dTotalRoyalty, csAmt_Bold);
				XLSUtil.addCellToRow(r, iCount++, dTotalCCFee, csAmt_Bold);
				XLSUtil.addCellToRow(r, iCount++, dTotalCellmaniaShare, csAmt_Bold);
				XLSUtil.addCellToRow(r, iCount++, dTotalCarSh, csAmt_Bold);
				for(int j=0; j<iCount; j++) {
					s.autoSizeColumn((short)j);
				}
				
				File f = new File(Utility.checkFileName(reqReport.getReportPath() + fileName, 0, getReqReport().getFileExtension()));
				fileName = f.getName();
				FileOutputStream out = new FileOutputStream(f);
				wb.write(out);
				out.close();
				log.info("XLS Generated in : "+ (System.currentTimeMillis() - time) +" : FILE NAme : "+fileName);
			}
		} catch(Exception e){
			log.error("Error Writing CC Report for Qwest : "+e.getMessage());
			e.printStackTrace();
		}
		return fileName;
	}
}
