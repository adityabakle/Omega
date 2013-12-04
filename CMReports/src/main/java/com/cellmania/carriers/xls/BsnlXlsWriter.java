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


public class BsnlXlsWriter extends XLSWriter {
	Logger log = Logger.getLogger(this.getClass());
	
	public BsnlXlsWriter() {
		super();;
	}
	
	public String createLiveItemReport(HashMap<String,Collection<ReportDataDTO>> hmRpt,ArrayList<String> lsHeader) {
		log.debug("In Live Item report");
		String fileName = Utility.getFileName(reqReport);
		
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
				
				File f = new File(Utility.checkFileName(reqReport.getReportPath() + fileName,0,reqReport.getFileExtension()));
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
	
	public String createFreeItemInvoiceReport(
			HashMap<String, Collection<ReportDataDTO>> hmReport) {
		log.debug("In writeFreeItemInvoiceRpt Bsnl");
		String fileName = Utility.getFileName(getReqReport());

		try {
			log.info("Report file name : " + fileName);
			long time = System.currentTimeMillis();
			if (hmReport != null) {
				int sRI_sheetNum = 1;
				int rownum_sR = 0, rownum_sJK = 0, rownum_sNE = 0, rownum_sRI = 0;
				double totSal_R = 0.0, totSal_JK = 0.0, totSal_NE = 0.0, totSal_RI = 0.0;
				XSSFSheet sR, sJK, sNE, sRI = null;
				XSSFRow r = null;
				sR = wb.createSheet("Rajasthan");
				r = sR.createRow(rownum_sR);
				createFreeInvoiceHeaders(r);
				
				sJK = wb.createSheet("JammuAndKashmir");
				r = sJK.createRow(rownum_sJK);
				createFreeInvoiceHeaders(r);
				
				sNE = wb.createSheet("NorthEastCircle");
				r = sNE.createRow(rownum_sNE);
				createFreeInvoiceHeaders(r);
				
				sRI = wb.createSheet("RestOfIndia");
				r = sRI.createRow(rownum_sRI);
				createFreeInvoiceHeaders(r);

				Collection<ReportDataDTO> colRpt = hmReport.get("rptDetails");
				if (colRpt != null) {
					for (ReportDataDTO rDto : colRpt) {
						if (rDto.getCircleName().equals("Rajasthan")) {
							rownum_sR++;
							r = sR.createRow(rownum_sR);
							this.createFreeInvoiceRecord(r, rDto);
							totSal_R += rDto.getNetSales();
						} else if (rDto.getCircleName().equals(
								"Jammu and Kashmir")) {
							rownum_sJK++;
							r = sJK.createRow(rownum_sJK);
							this.createFreeInvoiceRecord(r, rDto);
							totSal_JK += rDto.getNetSales();
						} else if (rDto.getCircleName().equals("North East")) {
							rownum_sNE++;
							r = sNE.createRow(rownum_sNE);
							this.createFreeInvoiceRecord(r, rDto);
							totSal_NE += rDto.getNetSales();
						} else {
							rownum_sRI++;
							if (rownum_sRI > 1000000) {
								sRI = wb.createSheet("RestOfIndia_ctd_"
										+ sRI_sheetNum++);
								rownum_sRI = 0;
								r = sRI.createRow(rownum_sRI);
								createFreeInvoiceHeaders(r);
								rownum_sRI++;
							}
							r = sRI.createRow(rownum_sRI);
							this.createFreeInvoiceRecord(r, rDto);
							totSal_RI += rDto.getNetSales();
						}
					}

					creatFreeSummary(sR, totSal_R, rownum_sR + 1);
					creatFreeSummary(sJK, totSal_JK, rownum_sJK + 1);
					creatFreeSummary(sNE, totSal_NE, rownum_sNE + 1);
					creatFreeSummary(sRI, totSal_RI, rownum_sRI + 1);
				}
				
				File f = new File(Utility.checkFileName(reqReport.getReportPath() + fileName,0,reqReport.getFileExtension()));
				fileName = f.getName();
				FileOutputStream out = new FileOutputStream(f);
				wb.write(out);
				out.close();
				log.info("Free Item Invoice Report Generated in : "
						+ (System.currentTimeMillis() - time)
						+ " : FILE NAme : " + fileName);
			}

		} catch (Exception e) {
			log.error("Exception writeFreeItemInvoiceRpt Bsnl XLS : " + e);
			e.printStackTrace();
		}
		return fileName;
	}
		
	public String createPaidItemInvoiceRpt(HashMap<String, Collection<ReportDataDTO>> hmReport){
		log.debug("In writePaidItemInvoiceRpt Bsnl");
		String fileName = Utility.getFileName(getReqReport());
		
		try {
			log.info("Report file name : " + fileName);
			long time = System.currentTimeMillis();
			if(hmReport!=null) {
				int sRI_sheetNum=1;
				int rownum_sR=0, rownum_sJK=0, rownum_sNE=0, rownum_sRI = 0;
				double totSal_R = 0.0, totSal_JK = 0.0, totSal_NE = 0.0, totSal_RI = 0.0;
				double totHits_R = 0.0, totHits_JK = 0.0, totHits_NE = 0.0, totHits_RI = 0.0;
				XSSFSheet sR, sJK, sNE, sRI = null;
				XSSFRow r = null;
				
				sR = wb.createSheet("Rajasthan");
				r = sR.createRow(rownum_sR);
				createPaidInvoiceHeaders(r);
				
				sJK = wb.createSheet("JammuAndKashmir");
				r = sJK.createRow(rownum_sJK);
				createPaidInvoiceHeaders(r);
				
				sNE = wb.createSheet("NorthEastCircle");
				r = sNE.createRow(rownum_sNE);
				createPaidInvoiceHeaders(r);
				
				sRI = wb.createSheet("RestOfIndia");
				r = sRI.createRow(rownum_sRI);
				createPaidInvoiceHeaders(r);
				
				Collection<ReportDataDTO> colRpt = hmReport.get("rptDetails");
				if(colRpt!=null){
					for (ReportDataDTO rDto:colRpt){
						if(rDto.getCircleName().equals("Rajasthan")){
							rownum_sR++;
							r = sR.createRow(rownum_sR);
							this.createPaidInvoiceRecord(r, rDto);
							totSal_R+= rDto.getSellPrice();
							totHits_R+= rDto.getNoOfOrders();
						} else if(rDto.getCircleName().equals("Jammu and Kashmir")) {
							rownum_sJK++;
							r = sJK.createRow(rownum_sJK);
							this.createPaidInvoiceRecord(r, rDto);
							totSal_JK+= rDto.getSellPrice();
							totHits_JK+= rDto.getNoOfOrders();
						} else if(rDto.getCircleName().equals("North East")) {
							rownum_sNE++;
							r = sNE.createRow(rownum_sNE);
							this.createPaidInvoiceRecord(r, rDto);
							totSal_NE+= rDto.getSellPrice();
							totHits_NE+= rDto.getNoOfOrders();
						} else {
							rownum_sRI++;
							if(rownum_sRI>1000000){
								sRI = wb.createSheet("RestOfIndia_ctd_"+sRI_sheetNum++);
								rownum_sRI = 0;
								r = sRI.createRow(rownum_sRI);
								createPaidInvoiceHeaders(r);
								rownum_sRI++;
							}
							r = sRI.createRow(rownum_sRI);
							this.createPaidInvoiceRecord(r, rDto);
							totSal_RI+= rDto.getSellPrice();
							totHits_RI+= rDto.getNoOfOrders();
						}
					}
					
					creatPaidSummary(sR,totSal_R,totHits_R,rownum_sR+1);
					creatPaidSummary(sJK,totSal_JK,totHits_JK,rownum_sJK+1);
					creatPaidSummary(sNE,totSal_NE,totHits_NE,rownum_sNE+1);
					creatPaidSummary(sRI,totSal_RI,totHits_RI,rownum_sRI+1);
				}
				
				File f = new File(Utility.checkFileName(reqReport.getReportPath() + fileName,0,reqReport.getFileExtension()));
				fileName = f.getName();
				FileOutputStream out = new FileOutputStream(f);
				wb.write(out);
				out.close();
				log.info("XLS Generated in : "+ (System.currentTimeMillis() - time) +" : FILE NAme : "+fileName);
			}
			
		} catch (Exception e) {
			log.error("Exception writing Bsnl XLS : " + e);
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
		XLSUtil.addCellsToRow(r, 0, 8,"",csHeader);
		s.addMergedRegion(new CellRangeAddress(rownum-1,rownum-1,0,8));
		XLSUtil.addCellToRow(r, 0, szVal, csHeader);
		
		// Generate Headers
		r = s.createRow(rownum++);
		r = s.createRow(rownum++);
		int iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, Header.DATE_TRANS, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.COMPANY_NAME, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.ITEM_NAME, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.ITEM_ID, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.ITEM_TYPE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.DIR_INDIR, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.SALES, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Net sales \nExcluding 1.5% \nEmployee Purchase", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Net sales \nof "+reqReport.getCurrencyConversion()+"% \nCRT Tax", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_REFUND, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_0_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Network Aware\nFree Apps", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CELLMANIA_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CP_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_INV, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CSPCT, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CMPCT, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CPPCT, csHeader);
	    
	    
	    s.createFreezePane(1,rownum);
	    log.debug("Headers Generated");
	    
		double dTotalSales = 0.0,dTotalNetSales=0.0,dTotalCS=0.0,dTotalCM=0.0,dTotalCP=0.0,dTotalCI=0.0,dTotalNetSalesCRT=0.0; 
	    int iTotalZeroOrders = 0, iTotalOrders = 0,iTotalRefund = 0, iNetworkAwareApps = 0;
	    int noOfCells= 0;
	    String prevComp = null;
	    int iTotalCount = 11;
		double dCompTotal[] = new double[iTotalCount];
		for(ReportDataDTO rpt : colRpt) {
			String szComp = rpt.getCompanyName();					
			iCount = 0;
			
			if(prevComp!=null && !prevComp.equals(szComp)){
				r = s.createRow(rownum++);
				XLSUtil.addCellsToRow(r, 0, 5,Header.LINE,csHeader);
				XLSUtil.addCellToRow(r, 5, Header.SUB_TOTAL, csHeader);
				int i = 0;
				for(; i<iTotalCount; i++){
					if(i>=3 && i<=6) {
						XLSUtil.addCellToRow(r, i + 6, dCompTotal[i], csHeader);
					}
					else {
						XLSUtil.addCellToRow(r, i + 6, dCompTotal[i], csAmt_Bold);
					}
				}
				XLSUtil.addCellsToRow(r, i+6, 3,Header.LINE,csHeader);
				r = s.createRow(rownum++);
				prevComp = null;
			}
			
			r = s.createRow(rownum++);
			XLSUtil.addCellToRow(r, iCount++, ObjectConvertor.getDate(rpt.getOrderDate(), "MMM-yy"), csDate);
			XLSUtil.addCellToRow(r, iCount++, szComp, csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getDeviceDisplayName(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemId(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemType(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getDirectFlag(), null);
		    XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt); 
			dTotalSales += rpt.getSellPrice();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNetSales(), csAmt); 
			dTotalNetSales += rpt.getNetSales();
			XLSUtil.addCellToRow(r, iCount++, rpt.getSalesTax(), csAmt); 
			dTotalNetSalesCRT += rpt.getSalesTax();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
			iTotalOrders += rpt.getNoOfOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfRefunds(), csBody);
			iTotalRefund += rpt.getNoOfRefunds();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfZeroOrders(), csBody);
			iTotalZeroOrders += rpt.getNoOfZeroOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfFreeNWApps(), csBody);
			iNetworkAwareApps+=rpt.getNoOfFreeNWApps();
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare(), csAmt);
			dTotalCS += rpt.getCarrierShare();
			if(includeCP || "direct".equals(rpt.getDirectFlag())) {
				XLSUtil.addCellToRow(r, iCount++, rpt.getCellmaniaShare(), csAmt);
				dTotalCM += rpt.getCellmaniaShare();
				XLSUtil.addCellToRow(r, iCount++, rpt.getCpShare(), csAmt);
				dTotalCP += rpt.getCpShare();
				XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierInvoice(), csAmt);
				dTotalCI += rpt.getCarrierInvoice();
				XLSUtil.addCellToRow(r, iCount++, (rpt.getCsPct()/100), csPercentage);
				XLSUtil.addCellToRow(r, iCount++, rpt.getCellmaniaSharePct()/100, csPercentage);
				XLSUtil.addCellToRow(r, iCount++, rpt.getCpSharePct()/100, csPercentage);
				
			} else {
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
				
				XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierInvoice(), csAmt);
				dTotalCI += rpt.getCarrierInvoice();
				
				XLSUtil.addCellToRow(r, iCount++, (rpt.getCsPct()/100), csPercentage);
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csPercentage);
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csPercentage);
			}
			
			noOfCells = iCount;
			iCount = 0;
			if(prevComp==null){
				dCompTotal[iCount++] = rpt.getSellPrice();
				dCompTotal[iCount++] = rpt.getNetSales();
				dCompTotal[iCount++] = rpt.getSalesTax();
				dCompTotal[iCount++] = rpt.getNoOfOrders();
				dCompTotal[iCount++] = rpt.getNoOfRefunds();
				dCompTotal[iCount++] = rpt.getNoOfZeroOrders();
				dCompTotal[iCount++] = rpt.getNoOfFreeNWApps();
				dCompTotal[iCount++] = rpt.getCarrierShare();
				if(includeCP || "direct".equals(rpt.getDirectFlag())){
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
				dCompTotal[iCount++] += rpt.getSalesTax();
				dCompTotal[iCount++] += rpt.getNoOfOrders();
				dCompTotal[iCount++] += rpt.getNoOfRefunds();
				dCompTotal[iCount++] += rpt.getNoOfZeroOrders();
				dCompTotal[iCount++] += rpt.getNoOfFreeNWApps();
				dCompTotal[iCount++] += rpt.getCarrierShare();
				if(includeCP || "direct".equals(rpt.getDirectFlag())){
					dCompTotal[iCount++] += rpt.getCellmaniaShare();
					dCompTotal[iCount++] += rpt.getCpShare();
				} else {
					dCompTotal[iCount++] += 0;
					dCompTotal[iCount++] += 0;
				}
				dCompTotal[iCount++] += rpt.getCarrierInvoice();
			}
			prevComp = szComp;
			if(rownum>=1000000) {
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
		int i = 0;
		for(; i<iTotalCount; i++){
			if(i>=2 && i<=5) {
				XLSUtil.addCellToRow(r, i + 6, dCompTotal[i], csHeader);
			}
			else {
				XLSUtil.addCellToRow(r, i + 6, dCompTotal[i], csAmt_Bold);
			}
		}
		XLSUtil.addCellsToRow(r, i + 6, 3,Header.LINE,csHeader);
		r = s.createRow(rownum++);
		prevComp = null;
		
		log.debug("Detail Report generated");
		log.debug("Generating Summary");
		
		r = s.createRow(rownum++);
		XLSUtil.addCellsToRow(r, 0, 5, Header.LINE, csHeader);
		iCount = 5;
		XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalNetSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalNetSalesCRT, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, iTotalOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalRefund, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalZeroOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iNetworkAwareApps, csHeader);
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
		dTotalNetSalesCRT =0.0;
	    dTotalCP = 0.0;
	    dTotalCS = 0.0;
	    dTotalCI = 0.0; 
	    dTotalCM = 0.0;
	    iTotalZeroOrders = 0;
	    iNetworkAwareApps = 0;
	    iTotalOrders = 0;
	    iTotalRefund = 0;
	    
		XLSUtil.addCellToRow(r, 0, Header.SUMMARY, csHeader);		
		r = s.createRow(rownum++);
		iCount = 0;
		
		XLSUtil.addCellToRow(r, iCount++, Header.DIR_INDIR, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.ITEM_TYPE, csHeader);
		XLSUtil.addCellToRow(r, iCount++, Header.SALES, csHeader);
		XLSUtil.addCellToRow(r, iCount++, "Net sales \nExcluding 1.5% \nEmployee Purchase", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Net sales \nof "+reqReport.getCurrencyConversion()+"% \nCRT Tax", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_REFUND, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.NO_OF_0_ORDER, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Network Aware\nFree Apps", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CELLMANIA_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CP_SHARE, csHeader);
	    XLSUtil.addCellToRow(r, iCount++, Header.CARRIER_INV, csHeader);
	    
	    colRpt = hmReport.get("grandTotal");
	    for(ReportDataDTO rpt : colRpt) {
			iCount = 0;
			
			r = s.createRow(rownum++);
			XLSUtil.addCellToRow(r, iCount++, rpt.getDirectFlag(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getItemType(), csBody);
			XLSUtil.addCellToRow(r, iCount++, rpt.getSellPrice(), csAmt); 
			dTotalSales+=rpt.getSellPrice();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNetSales(), csAmt); 
			dTotalNetSales+=rpt.getNetSales();
			XLSUtil.addCellToRow(r, iCount++, rpt.getSalesTax(), csAmt); 
			dTotalNetSalesCRT+=rpt.getSalesTax();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfOrders(), csBody);
			iTotalOrders+=rpt.getNoOfOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfRefunds(), csBody);
			iTotalRefund+=rpt.getNoOfRefunds();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfZeroOrders(), csBody);
			iTotalZeroOrders+=rpt.getNoOfZeroOrders();
			XLSUtil.addCellToRow(r, iCount++, rpt.getNoOfFreeNWApps(), csBody);
			iNetworkAwareApps+=rpt.getNoOfFreeNWApps();
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierShare(), csAmt);
			dTotalCS+=rpt.getCarrierShare();
			if(includeCP || "direct".equals(rpt.getDirectFlag())){
				XLSUtil.addCellToRow(r, iCount++, rpt.getCellmaniaShare(), csAmt);
			    dTotalCM+=rpt.getCellmaniaShare();
				XLSUtil.addCellToRow(r, iCount++, rpt.getCpShare(), csAmt);
				dTotalCP+=rpt.getCpShare();
			} else {
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
			    dTotalCM += 0;
				XLSUtil.addCellToRow(r, iCount++, BigDecimal.ZERO.doubleValue(), csAmt);
				dTotalCP += 0;
			}
			
			XLSUtil.addCellToRow(r, iCount++, rpt.getCarrierInvoice(), csAmt);
			dTotalCI+=rpt.getCarrierInvoice();
		}
	    
	    r = s.createRow(rownum++);
		iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, "Free Item Share\n (Network Aware Apps)", csBody);
		XLSUtil.addCellsToRow(r, iCount++, 10, "", csBody);
		log.debug("Cell No : "+ (iCount+10));
		XLSUtil.addCellToRow(r, iCount+10, (iNetworkAwareApps*0.5), csAmt);
		dTotalCI +=(iNetworkAwareApps*0.5);
		
	    r = s.createRow(rownum++);
		iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, Header.TOTAL, csHeader);
		XLSUtil.addCellToRow(r, iCount++, "", csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalNetSales, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalNetSalesCRT, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, iTotalOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalRefund, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iTotalZeroOrders, csHeader);
		XLSUtil.addCellToRow(r, iCount++, iNetworkAwareApps, csHeader);
		XLSUtil.addCellToRow(r, iCount++, dTotalCS, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCM, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCP, csAmt_Bold);
		XLSUtil.addCellToRow(r, iCount++, dTotalCI, csAmt_Bold);
		
		log.debug("Report summary generated");
		for(int j=0; j<noOfCells; j++) {
			s.autoSizeColumn((short)j);
		}
	    
		log.debug("Rev Share Report DONE : "+reqReport.getCarrier().getDisplayName());
	}
	
	private void creatFreeSummary(XSSFSheet s, double totSal, int row) {
		XSSFRow r = s.createRow(row++);
		XLSUtil.addCellToRow(r, 8, "Total", csHeader);
		XLSUtil.addCellToRow(r, 9, "", csHeader);
		XLSUtil.addCellToRow(r, 10, totSal, csAmt_Bold);
		try{
		for(int j=0; j<11; j++) {
			s.autoSizeColumn((short)j);
		}
		}catch(Exception e){
			log.error("Erro while expanding Column in Report Invoice for Bsnl",e);
		}
		
	}
	
	private void createFreeInvoiceHeaders(XSSFRow r){
		int iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, "Month", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "Date", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "Circle Name", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "Content Type / Keyword", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Count", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Emp Ded @ --%", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Emp Ded", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Net Counts", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "EUP(As per Service)", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Gross Amount", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Amt After WPC", csHeader);
	}
	
	private void createFreeInvoiceRecord(XSSFRow r, ReportDataDTO rDto){
		int iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, rDto.getOrderDate(),csDate);
		XLSUtil.addCellToRow(r, iCount++, rDto.getLaunchDate(),csDateLong);
		XLSUtil.addCellToRow(r, iCount++, rDto.getCircleName(),csBody);
		XLSUtil.addCellToRow(r, iCount++, rDto.getDeviceDisplayName(), csBody);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getNoOfOrders(), csBody);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getCsPct(), csPercentage);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getCarrierShare(), csBody);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getCellmaniaShare(), csBody);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getCellmaniaSharePct(), csAmt);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getSellPrice(), csAmt);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getNetSales(), csAmt);
	}
	
	private void creatPaidSummary(XSSFSheet s, double totSal,double totHits, int row) {
		XSSFRow r = s.createRow(row++);
		XLSUtil.addCellToRow(r, 0, "Total", csHeader);
		XLSUtil.addCellToRow(r, 1, "", csHeader);
		XLSUtil.addCellToRow(r, 2, totHits, csHeader);
		XLSUtil.addCellToRow(r, 3, totSal, csAmt_Bold);
		XLSUtil.addCellToRow(r, 4, "", csHeader);
		String szFormula = "Sum("+XLSUtil.convertColumnNumberToChars(5+1)+"2:"+XLSUtil.convertColumnNumberToChars(5+1)+""+(row-1)+")";
		XLSUtil.addFormulaCellToRow(r, 5, szFormula, csAmt_Bold);
		szFormula = "Sum("+XLSUtil.convertColumnNumberToChars(6+1)+"2:"+XLSUtil.convertColumnNumberToChars(6+1)+""+(row-1)+")";
		XLSUtil.addFormulaCellToRow(r, 6, szFormula, csAmt_Bold);
		XLSUtil.addCellToRow(r, 7, "", csHeader);
		szFormula = "Sum("+XLSUtil.convertColumnNumberToChars(8+1)+"2:"+XLSUtil.convertColumnNumberToChars(8+1)+""+(row-1)+")";
		XLSUtil.addFormulaCellToRow(r, 8, szFormula, csAmt_Bold);
		szFormula = "Sum("+XLSUtil.convertColumnNumberToChars(9+1)+"2:"+XLSUtil.convertColumnNumberToChars(9+1)+""+(row-1)+")";
		XLSUtil.addFormulaCellToRow(r, 9, szFormula, csAmt_Bold);
		XLSUtil.addCellToRow(r, 10, "", csHeader);
		szFormula = "Sum("+XLSUtil.convertColumnNumberToChars(11+1)+"2:"+XLSUtil.convertColumnNumberToChars(11+1)+""+(row-1)+")";
		XLSUtil.addFormulaCellToRow(r, 11, szFormula, csAmt_Bold);
		try{
		for(int j=0; j<12; j++) {
			s.autoSizeColumn((short)j);
		}
		}catch(Exception e){
			log.error("Erro while expanding Column in Report Invoice for Bsnl",e);
		}
		
	}
	
	private void createPaidInvoiceHeaders(XSSFRow r){
		int iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, "Circle Name", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "Title", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "Total Hits", csHeader);
		XLSUtil.addCellToRow(r, iCount++, "Gross Sales", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Emp Ded @ --%", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Emp Ded Amt", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Total After\nEmp Ded", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "WPC & Telcom\nLic Fee %", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "WPC & Telcom\nLic Fee Amt", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Amt After WPC", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "RS %", csHeader);
	    XLSUtil.addCellToRow(r, iCount++, "Rs Amt", csHeader);
	    
	}
	
	private void createPaidInvoiceRecord(XSSFRow r, ReportDataDTO rDto){
		int iCount = 0;
		XLSUtil.addCellToRow(r, iCount++, rDto.getCircleName(),csBody);
		XLSUtil.addCellToRow(r, iCount++, rDto.getDeviceDisplayName(), csBody);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getNoOfOrders(), csBody);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getSellPrice(), csAmt);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getCsPct(), csPercentage);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getCarrierShare(), csAmt);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getCellmaniaShare(), csAmt);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getCpSharePct(), csPercentage);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getSalesAdjust(), csAmt);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getNetSales(), csAmt);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getTotalRevPct(), csPercentage);
	    XLSUtil.addCellToRow(r, iCount++, rDto.getCarrierInvoice(), csAmt);
	}
}
