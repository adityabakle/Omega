package com.cellmania.carriers.util;

import java.io.File;
import java.util.Date;

import com.cellmania.cmreports.common.ObjectConvertor;
import com.cellmania.cmreports.db.request.RequestDTO;

public class Utility {

	// generates a unique File Name in case if exist.
		public static String checkFileName(String fileName,int sufix, String extension) {
			String temp = null;
			if(sufix>0)
				temp = fileName.replace(extension, "["+sufix+"]"+extension);
			else 
				temp = fileName;
			
			File f = new File(temp);
			if(f.exists()){
				return checkFileName(fileName, ++sufix,extension);
			}
			return f.getAbsolutePath();
		}
		
		public static String getFileName(RequestDTO reqDto){
			StringBuffer fileName = new StringBuffer(reqDto.getFileNamePrefix())
			.append(reqDto.getCarrier().getName());
			if(reqDto.getStartDate()!=null && reqDto.getEndDate()!=null){
				String startDate = ObjectConvertor.simpleDate(reqDto.getStartDate(), "dd-MMM-yyyy");
				String endDate = ObjectConvertor.simpleDate(reqDto.getEndDate(), "dd-MMM-yyyy");
				
				if(startDate.equals(endDate))
					fileName.append("_").append(startDate);
				else {
					fileName.append("_").append(startDate)
					.append("_").append(endDate);
				}
			} else {
				fileName.append("_").append(ObjectConvertor.simpleDate(new Date(), "dd-MMM-yyyy"));
			}
			fileName.append(reqDto.getFileExtension());
			
			return fileName.toString();
		}
		
		public static String getDateForSubject(RequestDTO reqDto){
			StringBuffer subDate = new StringBuffer(" ");
			if(reqDto.getStartDate()!=null && reqDto.getEndDate()!=null){
				String startDate = ObjectConvertor.simpleDate(reqDto.getStartDate(), "dd-MMM-yyyy");
				String endDate = ObjectConvertor.simpleDate(reqDto.getEndDate(), "dd-MMM-yyyy");
				
				if(startDate.equalsIgnoreCase(endDate))
					subDate.append("(").append(startDate).append(")");
				else {
					subDate.append("(").append(startDate)
					.append(" to ").append(endDate).append(")");
				}
			}
			return subDate.toString();
		}
}
