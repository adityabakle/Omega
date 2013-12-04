package com.cellmania.carriers.xls;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
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


public class VodaXlsWriter extends XLSWriter  {
	Logger log = Logger.getLogger(this.getClass());	
	public VodaXlsWriter() {
		super();
	}
	
	public String createLiveItemReport(HashMap<String,Collection<ReportDataDTO>> hmRpt,ArrayList<String> lsHeader) {
		log.debug("In Live Item report");
		String fileName = Utility.getFileName(getReqReport());
		File f = null;
		ArrayList<String> catHeader = null;
		try {
			long time = System.currentTimeMillis();
			log.debug("START : "+time);
			if(hmRpt!=null && hmRpt.size()>0) {
				
				XSSFSheet s = null;
				if(hmRpt.get("Rpt1")!=null) {
					s = wb.createSheet("Item Listing");
					writeLIRDataSheet(s,hmRpt.get("Rpt1"),1);
				}
				if(hmRpt.get("Rpt2")!=null) {
					s = wb.createSheet("Items Per OS");
					writeLIRDataSheet(s,hmRpt.get("Rpt2"),2);
				}
				if(hmRpt.get("Rpt3")!=null) {
					s = wb.createSheet("Items Per Category");
					writeLIRDataSheet(s,hmRpt.get("Rpt3"),3);
					catHeader = new ArrayList<String>();
					for(ReportDataDTO rd:hmRpt.get("Rpt3")) {
						catHeader.add(rd.getDeviceDisplayName());
					}
				}
				if(hmRpt.get("Rpt4")!=null) {
					s = wb.createSheet("Items Per Device");
					writeLIRDataSheet(s,hmRpt.get("Rpt4"),4);
				}
				if(hmRpt.get("Rpt5")!=null) {
					s = wb.createSheet("Items Free_Paid");
					writeLIRDataSheet(s,hmRpt.get("Rpt5"),5);
				}
				if(hmRpt.get("Rpt6")!=null) {
					s = wb.createSheet("Items Per Price Point");
					writeLIRPricePointSheet(s,hmRpt.get("Rpt6"),lsHeader);
				}
				
				if(hmRpt.get("Rpt7")!=null) {
					s = wb.createSheet("Items Cat Device");
					writeDeviceCatSheet(s,hmRpt.get("Rpt7"),catHeader);
				}
				
				f = new File(Utility.checkFileName(reqReport.getReportPath() + fileName,0,reqReport.getFileExtension()));
				fileName = f.getName();
				FileOutputStream out = new FileOutputStream(f);
				wb.write(out);
				out.close();
				log.info("Live Item report generated in : "+ (System.currentTimeMillis() - time) +" : FILE Name : "+fileName);
			}
		} catch (Exception e) {
			log.error("Exception writing XLS : " + e,e);
			e.printStackTrace();
		}
		return fileName;
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
	
	private void writeRevShareRptSheet(XSSFSheet s, HashMap<String, Collection<ReportDataDTO>> hmReport,boolean includeCP) throws Exception {
		XSSFRow r = null;
		Collection<ReportDataDTO> colRpt = hmReport.get("rptDetails");
		int rownum = 0;
		int iSht = 0;
		
		r = s.createRow(rownum++);
		String szVal = reqReport.getCarrier().getDisplayName() + " " +
				   reqReport.getReport().getDisplayName()+ " " + 
				   ObjectConvertor.simpleDate(reqReport.getStartDate(),"dd-MMM-yy") + " to "+ 
				   ObjectConvertor.simpleDate(reqReport.getEndDate(),"dd-MMM-yyyy");
		XLSUtil.addCellsToRow(r, 0, 7,"",csHeader);
		s.addMergedRegion(new CellRangeAddress(rownum-1,rownum-1,0,7));
		XLSUtil.addCellToRow(r, 0, szVal, csHeader);
		// Generate Headers
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		int iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, Header.DATE_TRANS, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.COMPANY_NAME, csHeader);
		XLSUtil.addCellToRow(r, iCount++, "Type", csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.ITEM_NAME, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.ITEM_ID, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.ITEM_TYPE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.ITEM_SUB_TYPE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.DIR_INDIR, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NET_SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_REFUND, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_0_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CELLMANIA_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CP_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_INV, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CSPCT, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CMPCT, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CPPCT, csHeader);
	    
	    
	    s.createFreezePane(1,rownum);
	    log.debug("Headers Generated");
	    
		double dTotalSales = 0.0,dTotalNetSales=0.0,dTotalCS=0.0,dTotalCM=0.0,dTotalCP=0.0,dTotalCI=0.0; 
	    int iTotalZeroOrders = 0, iTotalOrders = 0,iTotalRefund = 0;
	    int noOfCells= 0;
	    String prevComp = null;
	    int iTotalCount = 9;
		double dCompTotal[] = new double[iTotalCount];
		for(ReportDataDTO rpt : colRpt) {
			String szComp = rpt.getCompanyName();					
			iCount = 0;
			
			if(prevComp!=null && !prevComp.equals(szComp)){
				r = s.createRow(rownum++);
				XLSUtil.addCellsToRow(r, 0, 7,Header.LINE,csHeader);
				XLSUtil.addCellToRow(r, 7, Header.SUB_TOTAL, csHeader);
				int i = 0;
				for(; i<iTotalCount; i++){
					if(i>=2 && i<=4) {
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
			XLSUtil.addCellToRow(r, iCount++, rpt.getPrepaid(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceDisplayName(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemId(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemType(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemSubType(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getDirectFlag(), csBody);
		    XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt); 
			dTotalSales += rpt.getSellPrice();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNetSales(), csAmt); 
			dTotalNetSales += rpt.getNetSales();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
			iTotalOrders += rpt.getNoOfOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfRefunds(), csBody);
			iTotalRefund += rpt.getNoOfRefunds();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfZeroOrders(), csBody);
			iTotalZeroOrders += rpt.getNoOfZeroOrders();
			
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare(), csAmt);
			dTotalCS += rpt.getCarrierShare();
			if(includeCP || "direct".equalsIgnoreCase(rpt.getDirectFlag())) {
				XLSUtil.addCellToRow(r, iCount++, rpt.getCellmaniaShare(), csAmt);
				dTotalCM += rpt.getCellmaniaShare();
				XLSUtil.addCellToRow(r, iCount++, rpt.getCpShare(), csAmt);
				dTotalCP += rpt.getCpShare();
			} else {
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
			}
			
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierInvoice(), csAmt);
			dTotalCI += rpt.getCarrierInvoice();
			XLSUtil.addCellToRow(r, iCount++, (rpt.getCsPct()/100), csPercentage);
			
			if(includeCP || "direct".equalsIgnoreCase(rpt.getDirectFlag())) {
				XLSUtil.addCellToRow(r, iCount++, rpt.getCellmaniaSharePct()/100, csPercentage);
				XLSUtil.addCellToRow(r, iCount++, rpt.getCpSharePct()/100, csPercentage);
			} else {
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csPercentage);
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csPercentage);
			}
			
			noOfCells = iCount;
			iCount = 0;
			if(prevComp==null){
				dCompTotal[iCount++] = rpt.getSellPrice();
				dCompTotal[iCount++] = rpt.getNetSales();
				dCompTotal[iCount++] = rpt.getNoOfOrders();
				dCompTotal[iCount++] = rpt.getNoOfRefunds();
				dCompTotal[iCount++] = rpt.getNoOfZeroOrders();
				dCompTotal[iCount++] = rpt.getCarrierShare();
				if(includeCP || "direct".equalsIgnoreCase(rpt.getDirectFlag())) {
					dCompTotal[iCount++] = rpt.getCellmaniaShare();
					dCompTotal[iCount++] = rpt.getCpShare();
				} else {
					dCompTotal[iCount++] = 0;
					dCompTotal[iCount++] = 0;
				}
				dCompTotal[iCount++] = rpt.getCarrierInvoice();
			}
			else {
				dCompTotal[iCount++] += rpt.getSellPrice();
				dCompTotal[iCount++] += rpt.getNetSales();
				dCompTotal[iCount++] += rpt.getNoOfOrders();
				dCompTotal[iCount++] += rpt.getNoOfRefunds();
				dCompTotal[iCount++] += rpt.getNoOfZeroOrders();
				dCompTotal[iCount++] += rpt.getCarrierShare();
				if(includeCP || "direct".equalsIgnoreCase(rpt.getDirectFlag())) {
					dCompTotal[iCount++] += rpt.getCellmaniaShare();
					dCompTotal[iCount++] += rpt.getCpShare();
				} else {
					dCompTotal[iCount++] += 0;
					dCompTotal[iCount++] += 0;
				}
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
		
		r = s.createRow(rownum++);
		XLSUtil.addCellsToRow(r, 0, 7,Header.LINE,csHeader);
		XLSUtil.addCellToRow(r, 7, Header.SUB_TOTAL, csHeader);
		int i = 0;
		for(; i<iTotalCount; i++){
			if(i>=2 && i<=4) {
				XLSUtil.addCellToRow(r, i + 8, dCompTotal[i], csHeader);
			}
			else {
				XLSUtil.addCellToRow(r, i + 8, dCompTotal[i], csAmt_Bold);
			}
		}
		XLSUtil.addCellsToRow(r, i + 8, 3,Header.LINE,csHeader);
		r = s.createRow(rownum++);
		prevComp = null;
		
		log.debug("Detail Report generated");
		log.debug("Generating Summary");
		
		r = s.createRow(rownum++);
		XLSUtil.addCellsToRow(r, 0, 7, Header.LINE, csHeader);
		iCount = 7;
		XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalNetSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, iTotalOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalRefund, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalZeroOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalCS, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCM, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCP, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCI, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		
		dTotalSales = 0.0; 
		dTotalNetSales = 0.0;
	    dTotalCP = 0.0;
	    dTotalCS = 0.0;
	    dTotalCI = 0.0; 
	    dTotalCM = 0.0;
	    iTotalZeroOrders = 0;
	    iTotalOrders = 0;
	    iTotalRefund = 0;
	    
		XLSUtil.addCellToRow(r, 0, Header.SUMMARY, csHeader);		
		r = s.createRow(rownum++);
		iCount = 0;
		
		XLSUtil.addCellToRow(r, iCount++, Header.DIR_INDIR, csHeader);
		XLSUtil.addCellToRow(r, iCount++, "Type", csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.ITEM_TYPE, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.ITEM_SUB_TYPE, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NET_SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_REFUND, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_0_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CELLMANIA_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CP_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_INV, csHeader);
	    
	    colRpt = hmReport.get("grandTotal");
	    for(ReportDataDTO rpt : colRpt) {
			iCount = 0;
			
			r = s.createRow(rownum++);
			XLSUtil.addCellToRow(r, iCount++, rpt.getDirectFlag(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getPrepaid(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemType(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemSubType(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt); 
			dTotalSales+=rpt.getSellPrice();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNetSales(), csAmt); 
			dTotalNetSales+=rpt.getNetSales();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
			iTotalOrders+=rpt.getNoOfOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfRefunds(), csBody);
			iTotalRefund+=rpt.getNoOfRefunds();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfZeroOrders(), csBody);
			iTotalZeroOrders+=rpt.getNoOfZeroOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare(), csAmt);
			dTotalCS+=rpt.getCarrierShare();
			if(includeCP || "direct".equalsIgnoreCase(rpt.getDirectFlag())) {
				XLSUtil.addCellToRow(r, iCount++, rpt.getCellmaniaShare(), csAmt);
			    dTotalCM+=rpt.getCellmaniaShare();
				XLSUtil.addCellToRow(r, iCount++, rpt.getCpShare(), csAmt);
				dTotalCP+=rpt.getCpShare();
			} else {
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
			}
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierInvoice(), csAmt);
			dTotalCI+=rpt.getCarrierInvoice();
		}
	    
	    r = s.createRow(rownum++);
		iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, "Free Item Share", csBody);
		XLSUtil.addCellsToRow(r, iCount++, 11, "", csBody);
		log.debug("Cell No : "+ (iCount+11));
		XLSUtil.addCellToRow(r, iCount+10, (iTotalZeroOrders*0.05), csAmt);
		dTotalCI +=(iTotalZeroOrders*0.05);
		
	    r = s.createRow(rownum++);
		iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalNetSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, iTotalOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalRefund, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalZeroOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalCS, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCM, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCP, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCI, csAmt_Bold);
		
		
	    log.debug("Report summary generated");
		for(int j=0; j<noOfCells; j++) {
			s.autoSizeColumn((short)j);
		}
		log.debug("Optus REPORT WRITING DONE");
	}
}
