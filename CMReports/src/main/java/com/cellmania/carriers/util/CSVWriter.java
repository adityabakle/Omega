package com.cellmania.carriers.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.cellmania.cmreports.common.ObjectConvertor;
import com.cellmania.cmreports.db.request.RequestDTO;



public class CSVWriter {
	static Logger log = Logger.getLogger(CSVWriter.class);	
	public static final int BUFFER_SIZE = 64 * 1024;
	
	public static String writeCSV(RequestDTO rp, Collection<String> colData){
		log.debug("In writeCSV: "+rp.getFileNamePrefix());
		
		String  fileName = rp.getFileNamePrefix();
		if(!fileName.endsWith(rp.getFileExtension())){
			if(rp.getStartDate()!=null) {
				String startDate = ObjectConvertor.simpleDate(rp.getStartDate(), "dd-MMM-yyyy");
				String endDate = ObjectConvertor.simpleDate(rp.getEndDate(), "dd-MMM-yyyy");
				
				if(startDate.equals(endDate) || endDate==null)
					fileName += "_"+startDate;
				else {
					fileName += "_"+startDate+"_"+endDate;
				}
			}
			fileName += rp.getFileExtension();
		}
		
		try {			
			log.info("Report file name : " + fileName);
			long time = System.currentTimeMillis();
			if(colData!=null) {
				File f = new File(Utility.checkFileName(rp.getReportPath() + fileName, 0, rp.getFileExtension()));
				fileName = f.getName();
				OutputStream bout = new BufferedOutputStream(new FileOutputStream(f));
				OutputStreamWriter out = new OutputStreamWriter(bout, "UTF-8");
				if(rp.getCsvHeader()!=null){
					out.write(rp.getCsvHeader());
					out.write("\r\n");
				}
				for(String rec:colData){
					out.write(rec);
					out.write("\r\n");
				}
				out.flush();
				out.close();
				log.info("CSV Generated in : "+ (System.currentTimeMillis() - time) +" : FILE NAme : "+fileName);
			}
			
		} catch (Exception e) {
			log.error("Exception writing CSV : "+rp, e);
			e.printStackTrace();
		}
		return fileName;	
	}
}
