package com.cellmania.carriers.xls;

import java.io.File;
import java.io.FileOutputStream;
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


public class VirginXlsWriter extends XLSWriter {
	Logger log = Logger.getLogger(this.getClass());	
	public VirginXlsWriter() {
		super();
	}
		
	public String createRevShareReport(HashMap<String, Collection<ReportDataDTO>> hmReport){
		log.debug("In writeMonthlySalesReport Virgin");
		String fileName = Utility.getFileName(getReqReport());
		try {
			long time = System.currentTimeMillis();
			if(hmReport!=null) {
				XSSFSheet s = null;
				if (!reqReport.getIncludeCP()) {
					s = wb.createSheet("Report_Excluding_CPShare");
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
			log.error("Exception writing XLS : " + e);
			e.printStackTrace();
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
		XLSUtil.addCellToRow(r, iCount++, Header.EXTERNAL_ITEM_ID, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.ITEM_NAME, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.ITEM_TYPE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.HANDSET, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SETTLEMENT_NAME, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SETTLEMENT_DISP_NAME, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.DIR_INDIR, csHeader);			    
	    XLSUtil.addCellToRow(r, iCount++, Header.DEVICE_DISP_NAME, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_REFUND, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_0_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CP_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_INV, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CSPCT, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CPPCT, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SUB, csHeader);
	    s.createFreezePane(1,rownum);
	    log.debug("Headers - Done");
	    
	    int iTotlaOrder = 0, iTotalRefunds = 0, iTotalZeroOrders = 0;
	    int noOfCells= 0;
	    int iTotalCount = 7;
	    
		double dTotalSales = 0.0; 
	    double dTotalCpShare = 0.0;
	    double dTotalCarSh = 0.0;
	    double dTotalCarInv = 0.0; 
	    double dCompTotal[] = new double[iTotalCount];
	    
	    String prevComp = null;
	    
		for(ReportDataDTO rpt : colRpt) {
			String szComp = rpt.getCompanyName();					
			iCount = 0;
			
			if(prevComp!=null && !prevComp.equals(szComp)){
				r = s.createRow(rownum++);
				XLSUtil.addCellsToRow(r, 0, 10,Header.LINE,csHeader);
				XLSUtil.addCellToRow(r, 10, Header.SUB_TOTAL, csHeader);
				int i = 0;
				for(; i<iTotalCount; i++){
					if(i>0 && i<4) {
						XLSUtil.addCellToRow(r, i + 11, dCompTotal[i], csHeader);
					}
					else {
						XLSUtil.addCellToRow(r, i + 11, dCompTotal[i], csAmt_Bold);
					}
				}
				XLSUtil.addCellsToRow(r, i+11, 3,Header.LINE,csHeader);
				r = s.createRow(rownum++);
				prevComp = null;
			}
			
			r = s.createRow(rownum++);
			XLSUtil.addCellToRow(r, iCount++, ObjectConvertor.getDate(rpt.getOrderDate(), "MMM-yy"), csDate);
			XLSUtil.addCellToRow(r, iCount++, szComp, csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemId(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getExternalItemId(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemName(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemType(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceName(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getSettlementName(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getSettlementDispName(), csBody);
		    XLSUtil.addCellToRow(r, iCount++, rpt.getDirectFlag(), null);
			XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceDisplayName(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt); 
			dTotalSales += rpt.getSellPrice();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody); 
			iTotlaOrder += rpt.getNoOfOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfRefunds(), csBody);
			iTotalRefunds += rpt.getNoOfRefunds();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfZeroOrders(), csBody);
			iTotalZeroOrders += rpt.getNoOfZeroOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getCpShare(), csAmt);
			dTotalCpShare += rpt.getCpShare();
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare(), csAmt);
			dTotalCarSh += rpt.getCarrierShare();
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierInvoice(), csAmt);
			dTotalCarInv += rpt.getCarrierInvoice();
			
			XLSUtil.addCellToRow(r, iCount++, (rpt.getCsPct()/100), csPercentage);
			XLSUtil.addCellToRow(r, iCount++, rpt.getCpSharePct()/100, csPercentage);
			
			XLSUtil.addCellToRow(r, iCount++, rpt.getSubscription(), csBody);
			noOfCells = iCount;
			iCount = 0;
			if(prevComp==null){
				dCompTotal[iCount++] = rpt.getSellPrice();
				dCompTotal[iCount++] = rpt.getNoOfOrders();
				dCompTotal[iCount++] = rpt.getNoOfRefunds();
				dCompTotal[iCount++] = rpt.getNoOfZeroOrders();
				dCompTotal[iCount++] = rpt.getCpShare();
				dCompTotal[iCount++] = rpt.getCarrierShare();
				dCompTotal[iCount++] = rpt.getCarrierInvoice();
			}
			else {
				dCompTotal[iCount++] += rpt.getSellPrice();
				dCompTotal[iCount++] += rpt.getNoOfOrders();
				dCompTotal[iCount++] += rpt.getNoOfRefunds();
				dCompTotal[iCount++] += rpt.getNoOfZeroOrders();
				dCompTotal[iCount++] += rpt.getCpShare();
				dCompTotal[iCount++] += rpt.getCarrierShare();
				dCompTotal[iCount++] += rpt.getCarrierInvoice();
			}
			prevComp = szComp;
			if(rownum >= _XLSX_MAX_ROW_COUNT-1) {
				for(int j=0; j<noOfCells; j++) {
					s.autoSizeColumn((short)j);
				}
				iSht++;
				s = wb.createSheet(s.getSheetName()+"..ctd_"+iSht);
				rownum=0;
			}
		}
		
		r = s.createRow(rownum++);
		XLSUtil.addCellsToRow(r, 0, 10,Header.LINE,csHeader);
		XLSUtil.addCellToRow(r, 10, Header.SUB_TOTAL, csHeader);
		int i = 0;
		for(; i<iTotalCount; i++){
			if(i>0 && i<4) {
				XLSUtil.addCellToRow(r, i + 11, dCompTotal[i], csHeader);
			}
			else {
				XLSUtil.addCellToRow(r, i + 11, dCompTotal[i], csAmt_Bold);
			}
		}
		XLSUtil.addCellsToRow(r, i + 11, 3,Header.LINE,csHeader);
		r = s.createRow(rownum++);
		prevComp = null;
		
		log.debug("Detail Report generated");
		log.debug("Generating Summary");
		
		r = s.createRow(rownum++);
		XLSUtil.addCellsToRow(r, 0, 10, Header.LINE, csHeader);
		iCount = 10;
		XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, iTotlaOrder, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalRefunds, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalZeroOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalCpShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCarSh, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCarInv, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		
		dTotalSales = 0.0; 
	    dTotalCpShare = 0.0;
	    dTotalCarSh = 0.0;
	    dTotalCarInv = 0.0; 
	    iTotlaOrder = 0; iTotalRefunds = 0; iTotalZeroOrders = 0;
	    
		XLSUtil.addCellToRow(r, 0, Header.SUMMARY, csHeader);		
		r = s.createRow(rownum++);
		iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, Header.ITEM_TYPE, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.DIR_INDIR, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.REFUND, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_REFUND, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_0_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CP_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_INV, csHeader);
	    
	    colRpt = hmReport.get("grandTotal");
	    for(ReportDataDTO rpt : colRpt) {
			iCount = 0;
			r = s.createRow(rownum++);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemType(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getDirectFlag(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getRefund(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt); 
			dTotalSales+=rpt.getSellPrice();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
			iTotlaOrder+=rpt.getNoOfOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfRefunds(), csBody);
			iTotalRefunds+=rpt.getNoOfRefunds();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfZeroOrders(), csBody);
			iTotalZeroOrders+=rpt.getNoOfZeroOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getCpShare(), csAmt);
			dTotalCpShare+=rpt.getCpShare();
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare(), csAmt);
			dTotalCarSh+=rpt.getCarrierShare();
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierInvoice(), csAmt);
			dTotalCarInv+=rpt.getCarrierInvoice();
		}
	    
	    r = s.createRow(rownum++);
		iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, iTotlaOrder, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalRefunds, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalZeroOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalCpShare, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCarSh, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCarInv, csAmt_Bold);
		
		log.debug("Report summary generated");
		for(int j=0; j<noOfCells; j++) {
			s.autoSizeColumn((short)j);
		}
		
		log.debug("Rev Share Report DONE : "+reqReport.getCarrier().getDisplayName());
	}
	
	public String createJumptapReport(Collection<ReportDataDTO> col) throws CMException {
		log.debug("In createJumptapReport:"+getReqReport().getCarrier().getDisplayName());
		String fileName = Utility.getFileName(getReqReport());
		try {
			log.info("Report file name : " + fileName);
			long time = System.currentTimeMillis();
			if(col!=null) {
				
				XSSFSheet s = null;
				s = wb.createSheet("JumpTap Report");
				XSSFRow r = null;
				int rownum = 0;
				int iSht = 0;
				r = s.createRow(rownum++);
				String szVal = reqReport.getCarrier().getDisplayName() + " " +
						   reqReport.getReport().getDisplayName()+ " " + 
						   ObjectConvertor.simpleDate(reqReport.getStartDate(),"dd-MMM-yy") + " to " + 
						   ObjectConvertor.simpleDate(reqReport.getEndDate(),"dd-MMM-yyyy");
				XLSUtil.addCellsToRow(r, 0, 7,"",csHeader);
				s.addMergedRegion(new CellRangeAddress(rownum-1,rownum-1,0,7));
				XLSUtil.addCellToRow(r, 0, szVal, csHeader);
				// Generate Headers
				r = s.createRow(rownum++);
				r = s.createRow(rownum++);
				int iCount = 0;
				int iTotalOrder=0;
				double dTotalSales = 0.0;
				double dTotalCarrierShare = 0.0;
				XLSUtil.addCellToRow(r, iCount++, "Discovery Point", csHeader);
				XLSUtil.addCellToRow(r, iCount++, "Discovery Family", csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.DEVICE_DISP_NAME, csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.ITEM_ID, csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.HANDSET, csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.SETTLEMENT_NAME, csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.SALES, csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
				for(ReportDataDTO rpt : col) {
					iCount = 0;
					r = s.createRow(rownum++);
					XLSUtil.addCellToRow(r, iCount++, rpt.getDeveloperName(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getCompanyName(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceDisplayName(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getItemId(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceName(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getSettlementName(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getNetSales(), csAmt);
					dTotalSales = dTotalSales + rpt.getNetSales();
					XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
					iTotalOrder = iTotalOrder + rpt.getNoOfOrders();
					XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare(), csAmt);
					dTotalCarrierShare = dTotalCarrierShare + rpt.getCarrierShare();
					
					if(rownum>=_XLSX_MAX_ROW_COUNT-1) {
						r = s.createRow(rownum++);
						XLSUtil.addCellsToRow(r, 0, 5,Header.LINE,csHeader);
						XLSUtil.addCellToRow(r, 5, Header.SUB_TOTAL, csHeader);
						XLSUtil.addCellToRow(r, 6, dTotalSales, csAmt_Bold);
						XLSUtil.addCellToRow(r, 7, iTotalOrder, csHeader);
						XLSUtil.addCellToRow(r, 8, dTotalCarrierShare, csAmt_Bold);
						
						for(int j=0; j<iCount; j++) {
							s.autoSizeColumn((short)j);
						}
						iSht++;
						s = wb.createSheet(s.getSheetName()+"..ctd_"+iSht);
						rownum=0;
					}
				}
				r = s.createRow(rownum++);
				XLSUtil.addCellsToRow(r, 0, 5,Header.LINE,csHeader);
				XLSUtil.addCellToRow(r, 5, Header.TOTAL, csHeader);
				XLSUtil.addCellToRow(r, 6, dTotalSales, csAmt_Bold);
				XLSUtil.addCellToRow(r, 7, iTotalOrder, csHeader);
				XLSUtil.addCellToRow(r, 8, dTotalCarrierShare, csAmt_Bold);
				
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
			
		} catch (Exception e) {
			log.error("Exception writing XLS : " + e.getMessage(),e);
			throw new CMException(e);
		}
		return fileName;
	}
	
	public String createRingtonePurchaseReport(Collection<ReportDataDTO> col) throws CMException{
		log.debug("In createRingtonePurchaseReport");
		String fileName = Utility.getFileName(getReqReport());
		
		try {
			long time = System.currentTimeMillis();
			if(col!=null) {
				XSSFSheet s = null;
				s = wb.createSheet("MDN RT Report");
				XSSFRow r = null;
				int rownum = 0;
				int iSht = 0;
				r = s.createRow(rownum++);
				String szVal = reqReport.getCarrier().getDisplayName() + " " +
						   reqReport.getReport().getDisplayName()+ " " + 
						   ObjectConvertor.simpleDate(reqReport.getStartDate(),"dd-MMM-yy") + " to " + 
						   ObjectConvertor.simpleDate(reqReport.getEndDate(),"dd-MMM-yyyy");
				XLSUtil.addCellsToRow(r, 0, 7,"",csHeader);
				s.addMergedRegion(new CellRangeAddress(rownum-1,rownum-1,0,7));
				XLSUtil.addCellToRow(r, 0, szVal, csHeader);
				// Generate Headers
				r = s.createRow(rownum++);
				r = s.createRow(rownum++);
				int iCount = 0;
				
				XLSUtil.addCellToRow(r, iCount++, Header.DATE_TRANS, csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.PTN, csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.DEVICE_DISP_NAME, csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.ITEM_TYPE, csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
				XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_REFUND, csHeader);
				for(ReportDataDTO rpt : col) {
					iCount = 0;
					r = s.createRow(rownum++);
					XLSUtil.addCellToRow(r, iCount++, ObjectConvertor.getDate(rpt.getOrderDate(),"dd-MMM-yy"), csDateLong);
					XLSUtil.addCellToRow(r, iCount++, Double.parseDouble(rpt.getPtn()), csPTN);
					XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceDisplayName(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getItemType(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
					XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfRefunds(), csBody);
					
					if(rownum>=_XLSX_MAX_ROW_COUNT-1) {
						r = s.createRow(rownum++);
						for(int j=0; j<iCount; j++) {
							s.autoSizeColumn((short)j);
						}
						iSht++;
						s = wb.createSheet(s.getSheetName()+"..ctd_"+iSht);
						rownum=0;
						log.debug("SHEET COMPLETED for ["+fileName+"] NEW SHEET ADDED : "+iSht);
					}
					
				}
				
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
			
		} catch (Exception e) {
			log.error("Exception writing XLS : " +e.getMessage(),e);
			throw new CMException(e);
		}
		return fileName;
	}
}
