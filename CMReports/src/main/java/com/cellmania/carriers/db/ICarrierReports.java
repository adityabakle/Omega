package com.cellmania.carriers.db;

import com.cellmania.cmreports.db.request.RequestDTO;

public interface ICarrierReports {
	
	public String createLiveItemReport(RequestDTO rp) throws Exception;
	public String createFreeItemInvoiceReport(RequestDTO rp) throws Exception;
	public String createPaidItemInvoiceReport(RequestDTO rp) throws Exception;
	public String createdSQLResult(RequestDTO rp) throws Exception;
	
	public String createSimpleRevShareReport(RequestDTO rp) throws Exception;
	
	public String createRevShareReport(RequestDTO rp) throws Exception;
	public String createRevShareReportPaidApps(RequestDTO rp) throws Exception;
	
	public String createExceptionReport(RequestDTO rp)  throws Exception;
	
	public String createTopHandsetReport(RequestDTO rp) throws Exception;
	
	public String createJumptapReport(RequestDTO rp) throws Exception;
	public String createRingtonePurchaseReport(RequestDTO rp) throws Exception;
	
	public String createCreditCardReport(RequestDTO rp) throws Exception;
}

