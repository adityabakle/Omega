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
import com.cellmania.cmreports.common.CMException;
import com.cellmania.cmreports.common.ObjectConvertor;


public class NextelPeXlsWriter extends XLSWriter{
	Logger log = Logger.getLogger(this.getClass());	
	public NextelPeXlsWriter() {
		super();
	}
	
	public String createRevShareReport(HashMap<String, Collection<ReportDataDTO>> hmReport) throws CMException {
		log.debug("In RevShare report "+reqReport.getCarrier().getDisplayName());
		String fileName = Utility.getFileName(getReqReport());
		try {
			log.info("Report file name : " + fileName);
			long time = System.currentTimeMillis();
			if (hmReport != null) {
				XSSFSheet s = null;
				if (!reqReport.getIncludeCP()) {
					s = wb.createSheet("Excluding_CPShare");
					writeRevShareRptSheet(s, hmReport, false);
				} else {
					s = wb.createSheet("Excluding_CPShare");
					writeRevShareRptSheet(s, hmReport, false);
					s = wb.createSheet("Including_CPShare");
					writeRevShareRptSheet(s, hmReport, true);
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
			log.error("Error writing "+getReqReport().getCarrier().getDisplayName()+" XLS : " + e);
			throw new CMException(e);
		}
		return fileName;
	}
	
	private void writeRevShareRptSheet(XSSFSheet s,HashMap<String, Collection<ReportDataDTO>> hmReport,boolean includeCP) throws Exception {
		XSSFRow r = null;
		Collection<ReportDataDTO> colRpt = hmReport.get("rptDetails");
		int rownum = 0;
		int iSht = 0;
		r = s.createRow(rownum++);
		
		String szVal = reqReport.getCarrier().getDisplayName() + " " +
				   reqReport.getReport().getDisplayName()+ " " + 
				   ObjectConvertor.simpleDate(reqReport.getStartDate(),"dd-MMM-yy") + " to " + ObjectConvertor.simpleDate(reqReport.getEndDate(),"dd-MMM-yyyy");
		
		XLSUtil.addCellsToRow(r, 0, 7,"",csHeader);
		s.addMergedRegion(new CellRangeAddress(rownum-1,rownum-1,0,7));
		XLSUtil.addCellToRow(r, 0, szVal, csHeader);
		// Generate Headers
		
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		int iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, Header.DATE_TRANS, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.COMPANY_NAME, csHeader);	
		XLSUtil.addCellToRow(r, iCount++, Header.ITEM_ID, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.ITEM_NAME, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.ITEM_TYPE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SETTLEMENT_NAME, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.DIR_INDIR, csHeader);			    
	    XLSUtil.addCellToRow(r, iCount++, Header.DEVICE_DISP_NAME, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Sales Net\nof Tax", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_REFUND, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_0_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.PLATFORM_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header._3RD_PARTY_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CP_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "MIN CP Share", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Final CP Share", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CELLMANIA_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_INV, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CSPCT, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CPPCT, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CMPCT, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SUB, csHeader);
	    s.createFreezePane(1,rownum);
	    log.debug("Headers Generated");
	    double dTotalSales = 0.0,dNetSales = 0.0,d3PartyShare=0.0,dPremiumCnt=0.0,dFinalCP=0.0;
	    //dTotal3RdPartyShare=0.0; 
	    double dTotalCpShare = 0.0,dTotalCarSh = 0.0,dTotalCarInv = 0.0,dTotalPlatformShare=0.0,dTotalCMShare=0.0;  
	    
	    int iTotlaOrder = 0, iTotalRefunds = 0, iTotalZeroOrders = 0;
	    int noOfCells= 0;
	    String prevComp = null;
		double dCompTotal[] = new double[13];
		for(ReportDataDTO rpt : colRpt) {
			String szComp = rpt.getCompanyName();					
			iCount = 0;
			
			if(prevComp!=null && !prevComp.equals(szComp)){
				r = s.createRow(rownum++);
				XLSUtil.addCellsToRow(r, 0, 7,Header.LINE,csHeader);
				XLSUtil.addCellToRow(r, 7, Header.SUB_TOTAL, csHeader);
				int i = 0;
				for(; i<13; i++){
					if(i>1 && i<5) {
						XLSUtil.addCellToRow(r, i + 8, dCompTotal[i], csHeader);
					}
					else {
						XLSUtil.addCellToRow(r, i + 8, dCompTotal[i], csAmt_Bold);
					}
				}
				XLSUtil.addCellsToRow(r, i + 8, 4,Header.LINE,csHeader);
				
				r = s.createRow(rownum++);
				prevComp = null;
			}
			
			r = s.createRow(rownum++);
			
			XLSUtil.addCellToRow(r, iCount++, ObjectConvertor.getDate(rpt.getOrderDate(), "MMM-yy"), csDate);
			XLSUtil.addCellToRow(r, iCount++, szComp, csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemId(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemName(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemType(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getSettlementName(), csBody);
		    XLSUtil.addCellToRow(r, iCount++, rpt.getDirectFlag(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceDisplayName(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt); 
			dTotalSales += rpt.getSellPrice();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNetSales(), csAmt); 
			dNetSales += rpt.getNetSales();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody); 
			iTotlaOrder += rpt.getNoOfOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfRefunds(), csBody);
			iTotalRefunds += rpt.getNoOfRefunds();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfZeroOrders(), csBody);
			iTotalZeroOrders += rpt.getNoOfZeroOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getPlatformRevShare(), csAmt);
			dTotalPlatformShare += rpt.getPlatformRevShare();
			XLSUtil.addCellToRow(r, iCount++, rpt.getThirdParty(), csAmt);
			d3PartyShare += rpt.getThirdParty();
			if(includeCP || "direct".equals(rpt.getDirectFlag())) {
				XLSUtil.addCellToRow(r, iCount++, rpt.getCpShare(), csAmt);
				dTotalCpShare += rpt.getCpShare();
				
				// MIN CP Share
				XLSUtil.addCellToRow(r, iCount++, rpt.getPrimiumContent(), csAmt);
				dPremiumCnt += rpt.getPrimiumContent();
				// Final CP
				XLSUtil.addCellToRow(r, iCount++, rpt.getSalesAdjust(), csAmt);
				dFinalCP += rpt.getSalesAdjust();
				
				XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare(), csAmt);
				dTotalCarSh += rpt.getCarrierShare();
				XLSUtil.addCellToRow(r, iCount++, rpt.getCellmaniaShare(), csAmt);
				dTotalCMShare += rpt.getCellmaniaShare();
				
			} else {
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
				// MIN CP Share
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
				// Final CP
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
				XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare(), csAmt);
				dTotalCarSh += rpt.getCarrierShare();
				
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
			}
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierInvoice(), csAmt);
			dTotalCarInv += rpt.getCarrierInvoice();
			XLSUtil.addCellToRow(r, iCount++, (rpt.getCsPct()/100), csPercentage);
			if(includeCP || "direct".equals(rpt.getDirectFlag())) {
				XLSUtil.addCellToRow(r, iCount++, rpt.getCpSharePct()/100, csPercentage);
				XLSUtil.addCellToRow(r, iCount++, rpt.getCellmaniaSharePct()/100, csPercentage);
			} else {
				XLSUtil.addCellToRow(r, iCount++,BigDecimal.ZERO.doubleValue(), csPercentage);
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csPercentage);
			}
			XLSUtil.addCellToRow(r, iCount++, rpt.getSubscription(), csBody);
			
			noOfCells = iCount;
			iCount = 0;
			if(prevComp==null){
				dCompTotal[iCount++] = rpt.getSellPrice();
				dCompTotal[iCount++] = rpt.getNetSales();
				dCompTotal[iCount++] = rpt.getNoOfOrders();
				dCompTotal[iCount++] = rpt.getNoOfRefunds();
				dCompTotal[iCount++] = rpt.getNoOfZeroOrders();
				dCompTotal[iCount++] = rpt.getPlatformRevShare();
				dCompTotal[iCount++] = rpt.getThirdParty();
				if(includeCP || "direct".equals(rpt.getDirectFlag())) {
					dCompTotal[iCount++] = rpt.getCpShare();
					dCompTotal[iCount++] = rpt.getPrimiumContent(); // Min CP Share
					dCompTotal[iCount++] = rpt.getSalesAdjust(); // Final CP Share
					dCompTotal[iCount++] = rpt.getCarrierShare();
					dCompTotal[iCount++] = rpt.getCellmaniaShare();
				} else {
					dCompTotal[iCount++] = BigDecimal.ZERO.doubleValue();
					dCompTotal[iCount++] = BigDecimal.ZERO.doubleValue(); // Min CP Share
					dCompTotal[iCount++] = BigDecimal.ZERO.doubleValue(); // Final CP Share
					dCompTotal[iCount++] = rpt.getCarrierShare();
					dCompTotal[iCount++] = BigDecimal.ZERO.doubleValue();
				}
				dCompTotal[iCount++] = rpt.getCarrierInvoice();
			}
			else {
				dCompTotal[iCount++] += rpt.getSellPrice();
				dCompTotal[iCount++] += rpt.getNetSales();
				dCompTotal[iCount++] += rpt.getNoOfOrders();
				dCompTotal[iCount++] += rpt.getNoOfRefunds();
				dCompTotal[iCount++] += rpt.getNoOfZeroOrders();
				dCompTotal[iCount++] += rpt.getPlatformRevShare();
				dCompTotal[iCount++] += rpt.getThirdParty();
				if(includeCP || "direct".equals(rpt.getDirectFlag())) {
					dCompTotal[iCount++] += rpt.getCpShare();
					dCompTotal[iCount++] += rpt.getPrimiumContent(); // Min CP Share
					dCompTotal[iCount++] += rpt.getSalesAdjust(); // Final CP Share
					dCompTotal[iCount++] += rpt.getCarrierShare();
					dCompTotal[iCount++] += rpt.getCellmaniaShare();
				} else {
					dCompTotal[iCount++] += BigDecimal.ZERO.doubleValue();
					dCompTotal[iCount++] += BigDecimal.ZERO.doubleValue(); // Min CP Share
					dCompTotal[iCount++] += BigDecimal.ZERO.doubleValue(); // Final CP Share
					dCompTotal[iCount++] += rpt.getCarrierShare();
					dCompTotal[iCount++] += BigDecimal.ZERO.doubleValue();
				}
				dCompTotal[iCount++] += rpt.getCarrierInvoice();
			}
			prevComp = szComp;
			
			if(rownum>=_XLSX_MAX_ROW_COUNT) {
				for(int j=0; j<noOfCells; j++) {
					s.autoSizeColumn((short)j);
				}
				iSht++;
				s = wb.createSheet("..ctd_"+iSht);
				rownum=0;
			}
		}
		
		r = s.createRow(rownum++);
		XLSUtil.addCellsToRow(r, 0, 7,Header.LINE,csHeader);
		XLSUtil.addCellToRow(r, 7, Header.SUB_TOTAL, csHeader);
		int i = 0;
		for(; i<13; i++){
			if(i>1 && i<5) {
				XLSUtil.addCellToRow(r, i + 8, dCompTotal[i], csHeader);
			}
			else {
				XLSUtil.addCellToRow(r, i + 8, dCompTotal[i], csAmt_Bold);
			}
		}
		XLSUtil.addCellsToRow(r, i + 8, 4,Header.LINE,csHeader);
		r = s.createRow(rownum++);
		prevComp = null;
		
		log.debug("Detail Report generated");
		log.debug("Generating Summary");
		
		r = s.createRow(rownum++);
		XLSUtil.addCellsToRow(r, 0, 7, Header.LINE , csHeader);
		iCount = 7;
		XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dNetSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, iTotlaOrder, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalRefunds, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalZeroOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalPlatformShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, d3PartyShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCpShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dPremiumCnt, csAmt_Bold); // Min CP Share
		XLSUtil.addCellToRow(r, iCount++, dFinalCP, csAmt_Bold); // Final CP Share
		XLSUtil.addCellToRow(r, iCount++, dTotalCarSh, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCMShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCarInv, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		
		dTotalSales = 0.0;
		dPremiumCnt=0.0;
		d3PartyShare=0.0;
		dTotalCMShare=0.0;
		dNetSales = 0.0;
		dFinalCP = 0.0;
	    dTotalCpShare = 0.0;dTotalCarSh = 0.0;
	    dTotalPlatformShare=0.0;
	    dTotalCarInv = 0.0; 
	    iTotlaOrder = 0; iTotalRefunds = 0; iTotalZeroOrders = 0;
	    
		XLSUtil.addCellToRow(r, 0, Header.SUMMARY, csHeader);		
		r = s.createRow(rownum++);
		iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, Header.DIR_INDIR, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.ITEM_TYPE, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.SUB, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.REFUND, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NET_SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_REFUND, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_0_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.PLATFORM_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header._3RD_PARTY_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CP_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Min CP Share", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Final CP Share", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CELLMANIA_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_INV, csHeader);
	    
	    
	    colRpt = hmReport.get("grandTotal");
	    for(ReportDataDTO rpt : colRpt) {
			iCount = 0;
			
			r = s.createRow(rownum++);
			
			XLSUtil.addCellToRow(r, iCount++, rpt.getDirectFlag(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemType(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getSubscription(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getRefund(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt); 
			dTotalSales+=rpt.getSellPrice();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNetSales(), csAmt); 
			dNetSales+=rpt.getNetSales();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
			iTotlaOrder+=rpt.getNoOfOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfRefunds(), csBody);
			iTotalRefunds+=rpt.getNoOfRefunds();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfZeroOrders(), csBody);
			iTotalZeroOrders+=rpt.getNoOfZeroOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getPlatformRevShare(), csAmt);
			dTotalPlatformShare+=rpt.getPlatformRevShare();
			XLSUtil.addCellToRow(r, iCount++, rpt.getThirdParty(), csAmt);
			d3PartyShare += rpt.getThirdParty();
			if(includeCP || "direct".equals(rpt.getDirectFlag())) {
				XLSUtil.addCellToRow(r, iCount++, rpt.getCpShare(), csAmt);
				dTotalCpShare+=rpt.getCpShare();
				XLSUtil.addCellToRow(r, iCount++, rpt.getPrimiumContent(), csAmt);
				dPremiumCnt += rpt.getPrimiumContent();
				XLSUtil.addCellToRow(r, iCount++, rpt.getSalesAdjust(), csAmt);
				dFinalCP += rpt.getSalesAdjust();
				XLSUtil.addCellToRow(r, iCount++, rpt.getCellmaniaShare(), csAmt);
				dTotalCMShare+=rpt.getCellmaniaShare();
			} else {
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
			}
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare(), csAmt);
			dTotalCarSh+=rpt.getCarrierShare();
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierInvoice(), csAmt);
			dTotalCarInv+=rpt.getCarrierInvoice();
		}
	    
	    r = s.createRow(rownum++);
		iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
		XLSUtil.addCellToRow(r, iCount++, " ", csHeader);
		XLSUtil.addCellToRow(r, iCount++, " ", csHeader);
		XLSUtil.addCellToRow(r, iCount++, " ", csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dNetSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, iTotlaOrder, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalRefunds, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalZeroOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalPlatformShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, d3PartyShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCpShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dPremiumCnt, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dFinalCP, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCMShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCarSh, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCarInv, csAmt_Bold);
		
		
		log.debug("Report summary generated");
		for(int j=0; j<noOfCells; j++) {
			s.autoSizeColumn((short)j);
		}
	}
}
