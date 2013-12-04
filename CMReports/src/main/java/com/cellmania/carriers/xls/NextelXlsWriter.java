package com.cellmania.carriers.xls;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.cellmania.carriers.db.ReportDataDTO;
import com.cellmania.carriers.util.Header;
import com.cellmania.carriers.util.Utility;
import com.cellmania.carriers.util.XLSUtil;
import com.cellmania.cmreports.common.ObjectConvertor;


public class NextelXlsWriter extends XLSWriter {
	Logger log = Logger.getLogger(this.getClass());	
	public NextelXlsWriter() {
		super();
	}
	
	public String createSimpleRevShareReport(Collection<ReportDataDTO> colReport) {
		log.debug("In createSimpleRevShareReport "+reqReport.getCarrier().getDisplayName());
		String fileName = Utility.getFileName(getReqReport());
		
		try {
			long time = System.currentTimeMillis();
			if(colReport!=null) {
				XSSFSheet s = null;
				s = wb.createSheet("Sales Report");
				writeSimpleReportData(s,colReport);
				
				File f = new File(Utility.checkFileName(reqReport.getReportPath() + fileName, 0, getReqReport().getFileExtension()));
				fileName = f.getName();
				FileOutputStream out = new FileOutputStream(f);
				wb.write(out);
				out.close();
				
				log.info("XLS Generated in : "+ (System.currentTimeMillis() - time) +" : File Name : "+fileName);
			}
			
		} catch (Exception e) {
			log.error("Exception writing XLS : " + e);
			e.printStackTrace();
		}
		return fileName;
	}
	
	private void writeSimpleReportData(XSSFSheet s,Collection<ReportDataDTO> colReport) throws Exception {
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
		XLSUtil.addCellToRow(r, iCount++, Header.DATE, csHeader);	
		XLSUtil.addCellToRow(r, iCount++, Header.INTERNAL_TITLE, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.CONTENT_PRO, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.LAUNCH_DATE, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.SUB, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.PRICE, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.SALES, csHeader);
		s.createFreezePane(1,rownum);
		log.debug("Headers Generated");
		
	    double dTotalSales = 0.0; 
	    int iTotlaOrder = 0;
	    int noOfCells= 0;
	    String prevComp = null,prevSub=null;
		double dCompTotal[] = new double[2];
		for(ReportDataDTO rpt : colReport) {
			String szComp = rpt.getCompanyName();
			String szSub = rpt.getSubscription();
			iCount = 0;
			
			if(prevComp!=null && (!prevComp.equals(szComp) || !prevSub.equals(szSub))){
				r = s.createRow(rownum++);
				XLSUtil.addCellsToRow(r, 0, 5,Header.LINE,csHeader);
				XLSUtil.addCellToRow(r, 5, Header.SUB_TOTAL, csHeader);
				XLSUtil.addCellToRow(r, 6, dCompTotal[0], csHeader);
				XLSUtil.addCellToRow(r, 7, dCompTotal[1], csAmt_Bold);
				r = s.createRow(rownum++);
				prevComp = null;
				prevSub = null;
			}
			
			r = s.createRow(rownum++);
			XLSUtil.addCellToRow(r, iCount++, ObjectConvertor.getDate(rpt.getOrderDate(),"MMM-yyyy"), csDate);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemName(), csBody);
			XLSUtil.addCellToRow(r, iCount++, szComp, csBody);
			XLSUtil.addCellToRow(r, iCount++, ObjectConvertor.getDate(rpt.getLaunchDate(),"dd-MM-yyyy"), csDateLong);
			XLSUtil.addCellToRow(r, iCount++, szSub, csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt); 
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody); 
			iTotlaOrder += rpt.getNoOfOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getTotalSellPrice(), csAmt);
			dTotalSales += rpt.getTotalSellPrice();
			
			noOfCells = iCount;
			iCount = 0;
			if(prevComp==null){
				dCompTotal[iCount++] = rpt.getNoOfOrders();
				dCompTotal[iCount++] = rpt.getTotalSellPrice();
			}
			else {
				dCompTotal[iCount++] += rpt.getNoOfOrders();
				dCompTotal[iCount++] += rpt.getTotalSellPrice();
			}
			prevComp = szComp;
			prevSub = szSub;
			
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
		XLSUtil.addCellsToRow(r, 0, 5,Header.LINE,csHeader);
		XLSUtil.addCellToRow(r, 5, Header.SUB_TOTAL, csHeader);
		XLSUtil.addCellToRow(r, 6, dCompTotal[0], csHeader);
		XLSUtil.addCellToRow(r, 7, dCompTotal[1], csAmt_Bold);
		r = s.createRow(rownum++);
		prevComp = null;
		prevSub = null;
		
		log.debug("Detail Report generated");
		log.debug("Generating Summary");
		
		r = s.createRow(rownum++);
		XLSUtil.addCellsToRow(r, 0, 5, Header.LINE , csHeader);
		iCount = 5;
		XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotlaOrder, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalSales,csAmt_Bold);
		
		log.debug("Report summary generated");
		for(int j=0; j<noOfCells; j++) {
			s.autoSizeColumn((short)j);
		}
	}
}
