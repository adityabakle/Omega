package com.cellmania.carriers.xls;

import java.io.File;
import java.io.FileOutputStream;
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
import com.cellmania.cmreports.common.ObjectConvertor;


public class TelstraXlsWriter extends XLSWriter {
	Logger log = Logger.getLogger(this.getClass());	
	public TelstraXlsWriter() {
		super();
	}
		
	public String createRevShareReport(HashMap<String, Collection<ReportDataDTO>> hmReport){
		log.debug("In RevShare report "+reqReport.getCarrier().getDisplayName());
		String fileName = Utility.getFileName(getReqReport());
		long time = System.currentTimeMillis();
		Collection<ReportDataDTO> colRptDto = hmReport.get("rptDetails");
		try {
			
			if(colRptDto!=null && colRptDto.size()>0) {
				XSSFSheet s = wb.createSheet("Sales Rpt All");
				int dpid = 0;
				// All Store Front. 
				writeRevShareRptSheet(s,colRptDto,dpid);
				log.debug("Rpt for All Store Front done");
				
				// Games Store Front.
				s = wb.createSheet("Games Sales Rpt");
				dpid = 100;
				writeRevShareRptSheet(s,colRptDto,dpid);
				log.debug("Rpt for Games Store Front done");
				
				// Busines App Store Front.
				s = wb.createSheet("B-Apps Sales Rpt");
				dpid = 101;
				writeRevShareRptSheet(s,colRptDto,dpid);
				log.debug("Rpt for Buniness App Store Front done");
				
				// Busines App Store Front.
				s = wb.createSheet("C-Apps Sales Rpt");
				dpid = 102;
				writeRevShareRptSheet(s,colRptDto,dpid);
				log.debug("Rpt for Consumer App Store Front done");
				
				File f = new File(Utility.checkFileName(reqReport.getReportPath() + fileName, 0, getReqReport().getFileExtension()));
				fileName = f.getName();
				FileOutputStream out = new FileOutputStream(f);
				wb.write(out);
				out.close();
				log.info("XLS Generated in : "+ (System.currentTimeMillis() - time) +" : FILE NAme : "+fileName);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		log.info("Report file name : " + fileName);
		
		return fileName;
	}
	
	private void writeRevShareRptSheet(XSSFSheet s,Collection<ReportDataDTO> colRptDto,int dpid) {
		try {
			double dCM_25G=0.0;
			double dCM_25G_3D=0.0;
			double dCM_25G_3D_Actual=0.0;
			double dCM_3G=0.0;
			double dCM_3G_3D=0.0;
			double dCM_3G_3D_Actual=0.0;
			
			double dInFusio_25G=0.0;
			double dInFusio_25G_3D=0.0;
			double dInFusio_25G_3D_Actual=0.0;
			double dInFusio_3G=0.0;
			double dInFusio_3G_3D=0.0;
			double dInFusio_3G_3D_Actual=0.0;
			
			double dTelStra_25G=0.0;
			double dTelStra_25G_3D=0.0;
			double dTelStra_25G_3D_Actual=0.0;
			double dTelStra_3G=0.0;
			double dTelStra_3G_3D=0.0;
			double dTelStra_3G_3D_Actual=0.0;
			
			String prevComp = null;
			double dCompTotal =0.0;
			double dCompTotalBefore =0.0;
			
			double totalSales=0.0;
			double totalSalesBefore3d=0.0;
			double totalSales_Actual=0.0;
			
			int iSubtotalOrd = 0;
			int iTotlaOrders = 0;
			double grandSales = 0.0;
			double grandSales3D = 0.0;
			
			XSSFRow r = null;
			if(colRptDto!=null && colRptDto.size()>0) {
				int iSht = 0;
				int rownum = 0;
				
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
				XLSUtil.addCellToRow(r, iCount++, Header.EXT_PRO_FLAG, csHeader);
			    XLSUtil.addCellToRow(r, iCount++, Header.FLAG_3D, csHeader);
			    XLSUtil.addCellToRow(r, iCount++, Header.DEVICE_TYPE, csHeader);
			    XLSUtil.addCellToRow(r, iCount++, Header.ITEM_NAME, csHeader);			    
			    XLSUtil.addCellToRow(r, iCount++, Header.PRICE, csHeader);
			    XLSUtil.addCellToRow(r, iCount++, Header._3D_REV, csHeader);
			    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
			    XLSUtil.addCellToRow(r, iCount++, Header.TOTAL_BEFORE_3D, csHeader);
			    XLSUtil.addCellToRow(r, iCount++, Header.TOTAL_AFTER_3D, csHeader);
			    s.createFreezePane(1,rownum);
			    log.debug("Headers Generated");
			    
				for(ReportDataDTO rpt : colRptDto) {
					if(dpid>0 && dpid!= rpt.getDpId()) {
						continue;
					}
					String szComp = rpt.getCompanyName();					
					iCount = 0;
					/*
					 * Logic for Creating in-line Sum
					 * */
					
					if(prevComp!=null && !prevComp.equals(szComp)){
						r = s.createRow(rownum++);
						XLSUtil.addCellsToRow(r, 0, 7,Header.LINE,csBody);
						XLSUtil.addCellToRow(r, 7, Header.SUB_TOTAL, csHeader);
						XLSUtil.addCellToRow(r, 8, iSubtotalOrd, csHeader);
						XLSUtil.addCellToRow(r, 9, dCompTotalBefore, csAmt_Bold);
						XLSUtil.addCellToRow(r, 10, dCompTotal, csAmt_Bold);
						prevComp = null;
						iSubtotalOrd = 0;
					}
					
					r = s.createRow(rownum++);
					// Transaction date (MON-YY)
					XLSUtil.addCellToRow(r, iCount++, ObjectConvertor.getDate(rpt.getOrderDate(), "MMM-yy"), csDate);
					
					// Developer name
					
					XLSUtil.addCellToRow(r, iCount++, szComp, csBody);
					
					// External Provider Flag
					XLSUtil.addCellToRow(r, iCount++, rpt.getExternamProviderFlag(), csBody);
					
					//3d Flag
					int i3dFlag = rpt.getAppTypeId();
					XLSUtil.addCellToRow(r, iCount++, i3dFlag==1?"Yes":"No", csBody);
						
					// Device Type
					XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceType(), csBody);
					
					// Item Name
					XLSUtil.addCellToRow(r, iCount++, rpt.getItemName(), csBody);
						
					// Sell_Price
					XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt);	
					
					// 3D Revenue
					XLSUtil.addCellToRow(r, iCount++, i3dFlag==1?rpt.getSellPrice()-2:"", csAmt);
					
						
					// No. of Purchase
					XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
					iSubtotalOrd +=rpt.getNoOfOrders();
					iTotlaOrders +=rpt.getNoOfOrders();
					// Total Sell Price
					// Before 3D
					XLSUtil.addCellToRow(r, iCount++, rpt.getTotalSellPrice(), csAmt);
					totalSalesBefore3d = rpt.getTotalSellPrice();
					grandSales += rpt.getTotalSellPrice();
					
					// After 3D
					if(i3dFlag==1){
						totalSales = (rpt.getSellPrice()-2) * rpt.getNoOfOrders();
						totalSales_Actual = rpt.getTotalSellPrice();
						XLSUtil.addCellToRow(r, iCount++, totalSales, csAmt);
					}
					else {
						totalSales = rpt.getTotalSellPrice();
						totalSales_Actual = 0;
						XLSUtil.addCellToRow(r, iCount++, totalSales, csAmt);
					}
					grandSales3D +=totalSales;
					
					if(prevComp == null){
						dCompTotal = totalSales;
						dCompTotalBefore = totalSalesBefore3d;
					}
					else {
						dCompTotal += totalSales;
						dCompTotalBefore += totalSalesBefore3d;
					}
					
					prevComp = szComp;
						
					if("false".equals(rpt.getExternamProviderFlag())) {  // Cellmania And In-Fusio
						if("In-Fusio".equals(szComp.trim())){ // In-Fusio
							if("2.5G".equals(rpt.getDeviceType())){
								if(i3dFlag==1){
									dInFusio_25G_3D += totalSales;
									dInFusio_25G_3D_Actual += totalSales_Actual;
								}
								else { // Non 3d
									dInFusio_25G += totalSales;
								}
							}
							else { // 3G
								if(i3dFlag==1){
									dInFusio_3G_3D += totalSales;
									dInFusio_3G_3D_Actual += totalSales_Actual;
								}
								else { // Non 3d
									dInFusio_3G += totalSales;
								}
								
							}
						}
						else { // Cellmania
							if("2.5G".equals(rpt.getDeviceType())){
								if(i3dFlag==1){
									dCM_25G_3D += totalSales;
									dCM_25G_3D_Actual += totalSales_Actual;
								}
								else { // Non 3d
									dCM_25G += totalSales;
								}
							}
							else { // 3G
								if(i3dFlag==1){
									dCM_3G_3D += totalSales;
									dCM_3G_3D_Actual += totalSales_Actual;
								}
								else { // Non 3d
									dCM_3G += totalSales;
								}
							}
						}
					}
					else { //Telstra
						if("2.5G".equals(rpt.getDeviceType())){
							if(i3dFlag==1){
								dTelStra_25G_3D += totalSales;
								dTelStra_25G_3D_Actual += totalSales_Actual;
							}
							else { // Non 3d
								dTelStra_25G += totalSales;
							}
						}
						else { // 3G
							if(i3dFlag==1){
								dTelStra_3G_3D += totalSales;
								dTelStra_3G_3D_Actual += totalSales_Actual;
							}
							else { // Non 3d
								dTelStra_3G += totalSales;
							}
						}
					}
					
					if(rownum>=XLSWriter._XLSX_MAX_ROW_COUNT-1) {
						for(int j=0; j<iCount; j++) {
							s.autoSizeColumn((short)j);
						}
						iSht++;
						s = wb.createSheet("..ctd_"+iSht);
						rownum=0;
					}
				}
				log.debug("Data populated");
				r = s.createRow(rownum++);
				XLSUtil.addCellsToRow(r, 0, 7,Header.LINE,null);
				XLSUtil.addCellToRow(r, 7, Header.SUB_TOTAL, csHeader);
				XLSUtil.addCellToRow(r, 8, iSubtotalOrd, csHeader);
				XLSUtil.addCellToRow(r, 9, dCompTotalBefore, csAmt_Bold);
				XLSUtil.addCellToRow(r, 10, dCompTotal, csAmt_Bold);
				
				r = s.createRow(rownum++);
				XLSUtil.addCellsToRow(r, 0, 7,Header.LINE,null);
				XLSUtil.addCellToRow(r, 7, Header.TOTAL, csHeader);
				XLSUtil.addCellToRow(r, 8, iTotlaOrders, csHeader);
				XLSUtil.addCellToRow(r, 9, grandSales,csAmt_Bold);
				XLSUtil.addCellToRow(r, 10, grandSales3D,csAmt_Bold);
				
				prevComp = null;
				iSubtotalOrd = 0;
				
				r = s.createRow(rownum++);
				r = s.createRow(rownum++);
				r = s.createRow(rownum++);
				
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, 0, Header.SUMMARY, csHeader);
				XLSUtil.addCellToRow(r, 1, "", csHeader);
				XLSUtil.addCellToRow(r, 2, "", csHeader);
				XLSUtil.addCellToRow(r, 3, "Before 3D", csHeader);
				XLSUtil.addCellToRow(r, 4, "After 3D", csHeader);
				
				/*
				 * Cellmania Summary
				 * */
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, 0, "Cellmania", csBody);
				XLSUtil.addCellToRow(r, 1, "2.5G", csBody);
				XLSUtil.addCellToRow(r, 2, "Yes", csBody);
				XLSUtil.addCellToRow(r, 3, dCM_25G_3D_Actual, csAmt);
				XLSUtil.addCellToRow(r, 4, dCM_25G_3D, csAmt);
				
				
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, 0, "Cellmania", csBody);
				XLSUtil.addCellToRow(r, 1, "2.5G", csBody);
				XLSUtil.addCellToRow(r, 2, "No", csBody);
				XLSUtil.addCellToRow(r, 3, dCM_25G, csAmt);
				XLSUtil.addCellToRow(r, 4, dCM_25G, csAmt);
				
				
				
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, 0, "Cellmania", csBody);
				XLSUtil.addCellToRow(r, 1, "3G", csBody);
				XLSUtil.addCellToRow(r, 2, "Yes", csBody);
				XLSUtil.addCellToRow(r, 3, dCM_3G_3D_Actual, csAmt);
				XLSUtil.addCellToRow(r, 4, dCM_3G_3D, csAmt);
				
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, 0, "Cellmania", csBody);
				XLSUtil.addCellToRow(r, 1, "3G", csBody);
				XLSUtil.addCellToRow(r, 2, "No", csBody);
				XLSUtil.addCellToRow(r, 3, dCM_3G, csAmt);
				XLSUtil.addCellToRow(r, 4, dCM_3G, csAmt);
								
				/*
				 * In Fusio Summary
				 * */
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, 0, "In-Fusio", csBody);
				XLSUtil.addCellToRow(r, 1, "2.5G", csBody);
				XLSUtil.addCellToRow(r, 2, "Yes", csBody);
				XLSUtil.addCellToRow(r, 3, dInFusio_25G_3D_Actual, csAmt);
				XLSUtil.addCellToRow(r, 4, dInFusio_25G_3D, csAmt);
				
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, 0, "In-Fusio", csBody);
				XLSUtil.addCellToRow(r, 1, "2.5G", csBody);
				XLSUtil.addCellToRow(r, 2, "No", csBody);
				XLSUtil.addCellToRow(r, 3, dInFusio_25G, csAmt);
				XLSUtil.addCellToRow(r, 4, dInFusio_25G, csAmt);
				
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, 0, "In-Fusio", csBody);
				XLSUtil.addCellToRow(r, 1, "3G", csBody);
				XLSUtil.addCellToRow(r, 2, "Yes", csBody);
				XLSUtil.addCellToRow(r, 3, dInFusio_3G_3D_Actual, csAmt);
				XLSUtil.addCellToRow(r, 4, dInFusio_3G_3D, csAmt);
				
				
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, 0, "In-Fusio", csBody);
				XLSUtil.addCellToRow(r, 1, "3G", csBody);
				XLSUtil.addCellToRow(r, 2, "No", csBody);
				XLSUtil.addCellToRow(r, 3, dInFusio_3G, csAmt);
				XLSUtil.addCellToRow(r, 4, dInFusio_3G, csAmt);
				
				
				/*
				 * In Telstra Summary
				 * */
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, 0, "Telstra", csBody);
				XLSUtil.addCellToRow(r, 1, "2.5G", csBody);
				XLSUtil.addCellToRow(r, 2, "Yes", csBody);
				XLSUtil.addCellToRow(r, 3, dTelStra_25G_3D_Actual, csAmt);
				XLSUtil.addCellToRow(r, 4, dTelStra_25G_3D, csAmt);
				
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, 0, "Telstra", csBody);
				XLSUtil.addCellToRow(r, 1, "2.5G", csBody);
				XLSUtil.addCellToRow(r, 2, "No", csBody);
				XLSUtil.addCellToRow(r, 3, dTelStra_25G, csAmt);
				XLSUtil.addCellToRow(r, 4, dTelStra_25G, csAmt);
				
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, 0, "Telstra", csBody);
				XLSUtil.addCellToRow(r, 1, "3G", csBody);
				XLSUtil.addCellToRow(r, 2, "Yes", csBody);
				XLSUtil.addCellToRow(r, 3, dTelStra_3G_3D_Actual, csAmt);
				XLSUtil.addCellToRow(r, 4, dTelStra_3G_3D, csAmt);
				
				
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, 0, "Telstra", csBody);
				XLSUtil.addCellToRow(r, 1, "3G", csBody);
				XLSUtil.addCellToRow(r, 2, "No", csBody);
				XLSUtil.addCellToRow(r, 3, dTelStra_3G, csAmt);
				XLSUtil.addCellToRow(r, 4, dTelStra_3G, csAmt);
				
				r = s.createRow(rownum++);
				XLSUtil.addCellToRow(r, 0, Header.TOTAL, csHeader);
				XLSUtil.addCellToRow(r, 1, "", csHeader);
				XLSUtil.addCellToRow(r, 2, "", csHeader);
				double summaryTotal = dTelStra_3G_3D_Actual + dTelStra_3G 
									+ dTelStra_25G_3D_Actual + dTelStra_25G
									+ dCM_3G_3D_Actual + dCM_3G
									+ dCM_25G_3D_Actual + dCM_25G
									+ dInFusio_3G_3D_Actual + dInFusio_3G
									+ dInFusio_25G_3D_Actual + dInFusio_25G;

				XLSUtil.addCellToRow(r, 3, summaryTotal, csAmt_Bold);
				
				summaryTotal = dTelStra_3G_3D + dTelStra_3G 
				  			 + dTelStra_25G_3D + dTelStra_25G
				  			 + dCM_3G_3D + dCM_3G
				  			 + dCM_25G_3D + dCM_25G
				  			 + dInFusio_3G_3D + dInFusio_3G
				  			 + dInFusio_25G_3D + dInFusio_25G;
				XLSUtil.addCellToRow(r, 4, summaryTotal, csAmt_Bold);
				log.debug("Report summary generated");
				for(int i = 0;i<iCount;i++){
					s.autoSizeColumn((short) i);
				}
				
			}
			
		} catch (Exception e) {
			log.error("Exception writing XLS : " + e);
			e.printStackTrace();
		}
	}
	
	public String createLiveItemReport(HashMap<String,Collection<ReportDataDTO>> hmRpt,ArrayList<String> lsHeader) {
		log.info("In Live Item report Telstra");
		String fileName = Utility.getFileName(getReqReport());
		
		ArrayList<String> catHeader = null;
		try {
			long time = System.currentTimeMillis();
			log.debug("START : "+time);
			
			if(hmRpt!=null && hmRpt.size()>0) {
				
				XSSFSheet s = null;
				if(hmRpt.get("Rpt1")!=null) {
					s = wb.createSheet("Item Listing");
					this.writeLIRDataSheet(s,hmRpt.get("Rpt1"),1);
				}
				if(hmRpt.get("Rpt2")!=null) {
					s = wb.createSheet("Items Per OS");
					this.writeLIRDataSheet(s,hmRpt.get("Rpt2"),2);
				}
				if(hmRpt.get("Rpt3")!=null) {
					s = wb.createSheet("Items Per Category");
					this.writeLIRDataSheet(s,hmRpt.get("Rpt3"),3);
					catHeader = new ArrayList<String>();
					for(ReportDataDTO rd:hmRpt.get("Rpt3")) {
						catHeader.add(rd.getDeviceDisplayName());
					}
				}
				if(hmRpt.get("Rpt4")!=null) {
					s = wb.createSheet("Items Per Device");
					this.writeLIRDataSheet(s,hmRpt.get("Rpt4"),4);
				}
				if(hmRpt.get("Rpt5")!=null) {
					s = wb.createSheet("Items Free_Paid");
					this.writeLIRDataSheet(s,hmRpt.get("Rpt5"),5);
				}
				if(hmRpt.get("Rpt6")!=null) {
					s = wb.createSheet("Items Per Price Point");
					this.writeLIRPricePointSheet(s,hmRpt.get("Rpt6"),lsHeader);
				}
				
				if(hmRpt.get("Rpt7")!=null) {
					s = wb.createSheet("Items Cat Device");
					this.writeDeviceCatSheet(s,hmRpt.get("Rpt7"),catHeader);
				}
				
				File f = new File(Utility.checkFileName(reqReport.getReportPath() + fileName,0,reqReport.getFileExtension()));
				fileName = f.getName();
				FileOutputStream out = new FileOutputStream(f);
				wb.write(out);
				out.close();
				log.info("XLS Generated in : "+ (System.currentTimeMillis() - time) +" : FILE NAme : "+fileName);
			}
		} catch (Exception e) {
			log.error("Exception writing XLS : " + e);
			e.printStackTrace();
		}
		return fileName;
	}
	
	
	protected void writeLIRPricePointSheet(XSSFSheet s, Collection<ReportDataDTO> collection, ArrayList<String> priceHeader) {
		log.info("In telstra writeLIRPricePointSheet Writer");
		XSSFRow r = null;
		int rownum=0;
		
		int iSht=0;
		r=s.createRow(rownum++);
		int iCount=0;
		int startIndex = 0;
		XLSUtil.addCellToRow(r, iCount++, "Category", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "No of \nTitles", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "Paid", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "FREE", csHeader);
		
		for(String price:priceHeader){
			XLSUtil.addCellToRow(r, iCount++, Double.parseDouble(price), csAmt);
		}
		
		for(ReportDataDTO rpt : collection) {
    		iCount = 0;
	    	r = s.createRow(rownum++);
	    	XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceDisplayName(), csBody);
	    	XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
	    	XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders()-rpt.getNoOfZeroOrders(), csBody);
	    	XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfZeroOrders(), csBody);
	    	startIndex=iCount;
	    	if(rpt.getPriceCount()!=null && rpt.getPriceCount().size()>0) {
	    		HashMap<String, Integer> pc = rpt.getPriceCount();
	    		for(String pH:priceHeader){
	    			if(pc.containsKey(pH)){
	    				XLSUtil.addCellToRow(r, priceHeader.indexOf(pH)+startIndex, pc.get(pH), csBody);
	    			}
	    		}
	    	}
	    	if(rownum>=XLSWriter._XLSX_MAX_ROW_COUNT-1) {
				for(int j=0; j<priceHeader.size()+startIndex; j++) {
					s.autoSizeColumn((short)j);
				}
				iSht++;
				s = wb.createSheet("..ctd_"+iSht);
				rownum=0;
			}
		}
				
		for(int j=0; j<priceHeader.size()+startIndex; j++) {
			s.autoSizeColumn((short)j);
		}
		
	}
	
	
	protected void writeDeviceCatSheet(XSSFSheet s, Collection<ReportDataDTO> collection,ArrayList<String> catHeader) {
		log.info("In telstra writeDeviceCatSheet Writer");
		int rownum=0;
		String prevDevice = null;
		XSSFRow r = s.createRow(rownum++);
		int iCell=0;
		XLSUtil.addCellToRow(r, iCell++, "Device\\Category", csHeader);
		for(String cat:catHeader){
			XLSUtil.addCellToRow(r, iCell++, cat, csHeader);
		}
		
		for(ReportDataDTO rd:collection) {
			//Appending Manufacture Name except for Blackberry as Its already in place.
			if(null!= rd.getDeviceName() && !"Blackberry".equals(rd.getDeviceName())) {
				rd.setDeviceType(rd.getDeviceName()+" "+rd.getDeviceType());
			}
			
			if(prevDevice==null || !prevDevice.equals(rd.getDeviceType())){
				r = s.createRow(rownum++);
				XLSUtil.addCellsToRow(r, 1, catHeader.size(), 0, csBody);
				XLSUtil.addCellToRow(r, 0, rd.getDeviceType(), csBody);
			}
			
			XLSUtil.addCellToRow(r, catHeader.indexOf(rd.getDeviceDisplayName())+1, rd.getNoOfOrders(), csBody);
			prevDevice = rd.getDeviceType();
		}
		
		for(int j=0; j<catHeader.size(); j++) {
			s.autoSizeColumn((short)j);
		}
		
	}
	
	
	protected void writeLIRDataSheet(XSSFSheet s,Collection<ReportDataDTO> rpDto, int rptNo) throws Exception {
		log.info("In telstra writeLIRDataSheet Writer");
		XSSFRow r = null;
		int rownum = 0;
		int iSht = 0;
		r = s.createRow(rownum++);
		int iCount = 0;
		if(rptNo==1) {
			XLSUtil.addCellToRow(r, iCount, Header.DEVICE_DISP_NAME, csHeader);
			s.setColumnWidth((short)iCount++,(short)(256*50));
			XLSUtil.addCellToRow(r, iCount, Header.COMPANY_NAME, csHeader);
			s.setColumnWidth((short)iCount++,(short)(256*35));
			XLSUtil.addCellToRow(r, iCount, "Description", csHeader);
			s.setColumnWidth((short)iCount++,(short)(256*120));
			XLSUtil.addCellToRow(r, iCount, Header.PRICE, csHeader);
			s.setColumnWidth((short)iCount++,(short)(256*10));
			XLSUtil.addCellToRow(r, iCount, "File Size", csHeader);
			s.setColumnWidth((short)iCount++,(short)(256*10));
			XLSUtil.addCellToRow(r, iCount, "Supported Devices", csHeader);
			s.setColumnWidth((short)iCount++,(short)(256*100));
			XLSUtil.addCellToRow(r, iCount, "Mapped Categories", csHeader);
			s.setColumnWidth((short)iCount++,(short)(256*50));
			XLSUtil.addCellToRow(r, iCount, "Submit Date", csHeader);
			s.setColumnWidth((short)iCount++,(short)(256*50));
			r.setHeightInPoints(25);
			
		} else {
			String colName = "";
			switch(rptNo){
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
	    s.createFreezePane(0,rownum);
	    log.debug("Headers Generated");
	    int noOfCells= 0;
	    
	    if(rptNo==1) {
		    for(ReportDataDTO rpt : rpDto) {
		    	iCount = 0;
		    	r = s.createRow(rownum++);
		    	XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceDisplayName(), csBody);
		    	XLSUtil.addCellToRow(r, iCount++, rpt.getDeveloperName(), csBody);
		    	XLSUtil.addCellToRow(r, iCount++, rpt.getLongDesc(), csDesc);
		    	XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt);
		    	XLSUtil.addCellToRow(r, iCount++, rpt.getFileSize(), csBody);
		    	XLSUtil.addCellToRow(r, iCount++, rpt.getDevicesCSVTelstra3g(), csDesc);
		    	XLSUtil.addCellToRow(r, iCount++, rpt.getMappedCatCSV(), csDesc);
		    	XLSUtil.addCellToRow(r, iCount++, rpt.getLaunchDate(), csDateLong);
		    	noOfCells=iCount;
				if(rownum>=XLSWriter._XLSX_MAX_ROW_COUNT-1) {
					for(int j=0; j<noOfCells; j++) {
						s.autoSizeColumn((short)j);
					}
					iSht++;
					s = wb.createSheet("..ctd_"+iSht);
					rownum=0;
				}
			}
	    } else {
	    	for(ReportDataDTO rpt : rpDto) {
	    		iCount = 0;
		    	r = s.createRow(rownum++);
		    	XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceDisplayName(), csBody);
		    	XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
		    	noOfCells=iCount;
				if(rownum>=XLSWriter._XLSX_MAX_ROW_COUNT-1) {
					for(int j=0; j<noOfCells; j++) {
						s.autoSizeColumn((short)j);
					}
					iSht++;
					s = wb.createSheet("..ctd_"+iSht);
					rownum=0;
				}
			}
	    }
		
		r = s.createRow(rownum++);
		log.debug("Detail Report generated");
		
		if(rptNo!=1){
			for(int j=0; j<noOfCells; j++) {
				s.autoSizeColumn((short)j);
			}
		}
		
		log.debug("Telstra REPORT WRITING DONE");
	}

	public String writeTopHandsetSalesReport(String[] devices, HashMap<String, Collection<ReportDataDTO>> hmRpt) {
		log.debug("In writeTopHandsetSalesReport Telstra");
		String fileName = Utility.getFileName(getReqReport());
		
		try {
			long time = System.currentTimeMillis();
			log.debug("START : "+time);
			
			if(hmRpt!=null && hmRpt.size()>0) {
				
				XSSFSheet s = null;
				if(devices!=null && devices.length>0){
					for (String d: devices){ 
						if(hmRpt.get(d)!=null) {
							s = wb.createSheet(d);
							writeTHSR(s,hmRpt.get(d),d);
						}
					}
				} else {
					log.error("No devices for writing the Report: "+reqReport);
				}
				File f = new File(Utility.checkFileName(reqReport.getReportPath() + fileName,0,reqReport.getFileExtension()));
				fileName = f.getName();
				FileOutputStream out = new FileOutputStream(f);
				out.close();
				log.info("XLS Generated in : "+ (System.currentTimeMillis() - time) +" : FILE Name : "+fileName);
			}
		} catch (Exception e) {
			log.error("Exception writing writeTopHandsetSalesReport XLS : " + e);
			e.printStackTrace();
		}
		return fileName;
	}

	private void writeTHSR(XSSFSheet s, Collection<ReportDataDTO> colRpt, String deviceName) throws Exception {
		
		XSSFRow r = null;
		int rownum = 0;
		int iSht = 0;
		
		int iCount = 0;
		r = s.createRow(rownum++);
		XLSUtil.addCellToRow(r, iCount, Header.DEVICE_DISP_NAME, csHeader);
		s.setColumnWidth((short)iCount++,(short)(256*50));
		XLSUtil.addCellToRow(r, iCount, "Developer", csHeader);
		s.setColumnWidth((short)iCount++,(short)(256*35));
		XLSUtil.addCellToRow(r, iCount, "Price Point", csHeader);
		s.setColumnWidth((short)iCount++,(short)(256*10));
		XLSUtil.addCellToRow(r, iCount, "Mapped Categories", csHeader);
		s.setColumnWidth((short)iCount++,(short)(256*50));
		//XLSUtil.addCellToRow(r, iCount, "Unit Sales", csHeader);
		//s.setColumnWidth((short)iCount++,(short)(256*15));
		r.setHeightInPoints(25);
		s.createFreezePane(0,rownum);
	    log.debug("Headers Generated");
	    
	    //double dTotalSell = 0.0;
	    for(ReportDataDTO rpt : colRpt) {
	    	iCount = 0;
	    	r = s.createRow(rownum++);
	    	XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceDisplayName(), csBody);
	    	XLSUtil.addCellToRow(r, iCount++, rpt.getDeveloperName(), csBody);
	    	XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt);
	    	XLSUtil.addCellToRow(r, iCount++, rpt.getMappedCatCSV(), csDesc);
	    	//XLSUtil.addCellToRow(r, iCount++, rpt.getTotalSellPrice(), csAmt);
	    	//dTotalSell += rpt.getTotalSellPrice();
	    	
			if(rownum>=XLSWriter._XLSX_MAX_ROW_COUNT-1) {
				iSht++;
				s = wb.createSheet(deviceName+"..ctd_"+iSht);
				rownum=0;
			}
		}
	}
}
