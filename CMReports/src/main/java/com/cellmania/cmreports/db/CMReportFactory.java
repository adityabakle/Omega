package com.cellmania.cmreports.db;

public class CMReportFactory {
	private static ICMReports cmreportDB = null;
	
	private CMReportFactory(){
		
	}
	static public ICMReports getICMReports(){
		if(cmreportDB == null){
			cmreportDB = new CMReportsDao();
		}
		return cmreportDB;
	}
}
