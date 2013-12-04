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


public class SouthernLincXlsWriter extends XLSWriter{
	Logger log = Logger.getLogger(this.getClass());	
	public SouthernLincXlsWriter() {
		super();
	}
	
	public String createRevShareReport(HashMap<String, Collection<ReportDataDTO>> hmReport) throws CMException{
		log.debug("In RevShare report "+reqReport.getCarrier().getName());
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
			log.error("Error writing Aircel XLS : " + e);
			throw new CMException(e);
		}
		return fileName;
	}
	
	private void writeRevShareRptSheet(XSSFSheet s,HashMap<String, Collection<ReportDataDTO>> hmReport, boolean includeCP) throws Exception {
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
	    XLSUtil.addCellToRow(r, iCount++, Header.QPASS_ID, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.PREPAID, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.DIR_INDIR, csHeader);			    
	    XLSUtil.addCellToRow(r, iCount++, Header.DEVICE_DISP_NAME, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_REFUND, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_0_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CP_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header._3RD_PARTY_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_INV, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CSPCT, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CPPCT, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CMPCT, csHeader);
	    
	    s.createFreezePane(1,rownum);
	    log.debug("Headers Generated");
	    double dTotalSales = 0.0,dTotal3RdPartyShare=0.0; 
	    double dTotalCpShare = 0.0,dTotalCarSh = 0.0,dTotalCarInv = 0.0; 
	    int iTotlaOrder = 0, iTotalRefunds = 0, iTotalZeroOrders = 0;
	    int noOfCells= 0;
	    String prevComp = null;
		double dCompTotal[] = new double[8];
		for(ReportDataDTO rpt : colRpt) {
			String szComp = rpt.getCompanyName();					
			iCount = 0;
			
			if(prevComp!=null && !prevComp.equals(szComp)){
				r = s.createRow(rownum++);
				XLSUtil.addCellsToRow(r, 0, 8,Header.LINE,csHeader);
				XLSUtil.addCellToRow(r, 8, Header.SUB_TOTAL, csHeader);
				int i = 0;
				for(; i<8; i++){
					if(i>0 && i<4) {
						XLSUtil.addCellToRow(r, i + 9, dCompTotal[i], csHeader);
					}
					else {
						XLSUtil.addCellToRow(r, i + 9, dCompTotal[i], csAmt_Bold);
					}
				}
				XLSUtil.addCellsToRow(r, i+9, 3,Header.LINE,csHeader);
				r = s.createRow(rownum++);
				prevComp = null;
			}
			
			r = s.createRow(rownum++);
			
			XLSUtil.addCellToRow(r, iCount++, ObjectConvertor.getDate(rpt.getOrderDate(), "MMM-yy"), csDate);
			XLSUtil.addCellToRow(r, iCount++, szComp, csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemId(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemName(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemType(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getQpassProductId(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getPrepaid(), csBody);
		    XLSUtil.addCellToRow(r, iCount++, rpt.getDirectFlag(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceDisplayName(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt); 
			dTotalSales += rpt.getSellPrice();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody); 
			iTotlaOrder += rpt.getNoOfOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfRefunds(), csBody);
			iTotalRefunds += rpt.getNoOfRefunds();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfZeroOrders(), csBody);
			iTotalZeroOrders += rpt.getNoOfZeroOrders();
			if(includeCP || "direct".equalsIgnoreCase(rpt.getDirectFlag())) {
				XLSUtil.addCellToRow(r, iCount++, rpt.getCpShare(), csAmt);
				dTotalCpShare += rpt.getCpShare();
			} else {
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
			}
			XLSUtil.addCellToRow(r, iCount++, rpt.getThirdParty(), csAmt);
			dTotal3RdPartyShare += rpt.getThirdParty();
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
				if(includeCP || "direct".equalsIgnoreCase(rpt.getDirectFlag())) {
					dCompTotal[iCount++] = rpt.getCpShare();
				} else {
					dCompTotal[iCount++] = BigDecimal.ZERO.doubleValue();
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
				if(includeCP || "direct".equalsIgnoreCase(rpt.getDirectFlag())) {
					dCompTotal[iCount++] += rpt.getCpShare();
				} else {
					dCompTotal[iCount++] += BigDecimal.ZERO.doubleValue();
				}
				dCompTotal[iCount++] += rpt.getThirdParty();
				dCompTotal[iCount++] += rpt.getCarrierShare();
				dCompTotal[iCount++] += rpt.getCarrierInvoice();
			}
			prevComp = szComp;
			
			if(rownum>=_XLSX_MAX_ROW_COUNT) {
				for(int j=0; j<noOfCells; j++) {
					s.autoSizeColumn((short)j);
				}
				iSht++;
				s = wb.createSheet(s.getSheetName()+"..ctd_"+iSht);
				rownum=0;
			}
		}
		
		r = s.createRow(rownum++);
		XLSUtil.addCellsToRow(r, 0, 8,Header.LINE,csHeader);
		XLSUtil.addCellToRow(r, 8, Header.SUB_TOTAL, csHeader);
		int i = 0;
		for(; i<8; i++){
			if(i>0 && i<4) {
				XLSUtil.addCellToRow(r, i + 9, dCompTotal[i], csHeader);
			}
			else {
				XLSUtil.addCellToRow(r, i + 9, dCompTotal[i], csAmt_Bold);
			}
		}
		XLSUtil.addCellsToRow(r, i+9, 3,Header.LINE,csHeader);
		r = s.createRow(rownum++);
		prevComp = null;
		
		log.debug("Detail Report generated");
		log.debug("Generating Summary");
		
		r = s.createRow(rownum++);
		XLSUtil.addCellsToRow(r, 0, 8, Header.LINE , csHeader);
		iCount = 8;
		XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, iTotlaOrder, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalRefunds, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalZeroOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalCpShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotal3RdPartyShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCarSh, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCarInv, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		
		dTotalSales = 0.0; dTotal3RdPartyShare = 0.0;
	    dTotalCpShare = 0.0;dTotalCarSh = 0.0;
	    dTotalCarInv = 0.0; 
	    iTotlaOrder = 0; iTotalRefunds = 0; iTotalZeroOrders = 0;
	    
		XLSUtil.addCellToRow(r, 0, Header.SUMMARY, csHeader);		
		r = s.createRow(rownum++);
		iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, Header.DIR_INDIR, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.ITEM_TYPE, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.REFUND, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.PREPAID, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_REFUND, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_0_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CP_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header._3RD_PARTY_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_INV, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CSPCT, csHeader);
	    
	    colRpt = hmReport.get("grandTotal");
	    for(ReportDataDTO rpt : colRpt) {
			iCount = 0;
			
			r = s.createRow(rownum++);
			
			XLSUtil.addCellToRow(r, iCount++, rpt.getDirectFlag(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemType(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getRefund(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getPrepaid(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt); 
			dTotalSales+=rpt.getSellPrice();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
			iTotlaOrder+=rpt.getNoOfOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfRefunds(), csBody);
			iTotalRefunds+=rpt.getNoOfRefunds();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfZeroOrders(), csBody);
			iTotalZeroOrders+=rpt.getNoOfZeroOrders();
			if(includeCP || "direct".equals(rpt.getDirectFlag())){
				XLSUtil.addCellToRow(r, iCount++, rpt.getCpShare(), csAmt);
				dTotalCpShare+=rpt.getCpShare();
			} else {
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
			}
			XLSUtil.addCellToRow(r, iCount++, rpt.getThirdParty(), csAmt);
			dTotal3RdPartyShare+=rpt.getThirdParty();
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare(), csAmt);
			dTotalCarSh+=rpt.getCarrierShare();
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierInvoice(), csAmt);
			dTotalCarInv+=rpt.getCarrierInvoice();
			XLSUtil.addCellToRow(r, iCount++, (rpt.getCsPct()/100), csPercentage);
		}
	    
	    r = s.createRow(rownum++);
		//XLSUtil.addCellsToRow(r, 0, 6, "----" , csHeader);
		iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
		XLSUtil.addCellToRow(r, iCount++, " ", csHeader);
		XLSUtil.addCellToRow(r, iCount++, " ", csHeader);
		XLSUtil.addCellToRow(r, iCount++, " ", csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, iTotlaOrder, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalRefunds, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalZeroOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalCpShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotal3RdPartyShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCarSh, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCarInv, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, " ", csHeader);
		
		log.debug("Report summary generated");
		for(int j=0; j<noOfCells; j++) {
			s.autoSizeColumn((short)j);
		}
	}
}
