package com.cellmania.cmreports.web.typeConversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import com.opensymphony.xwork2.conversion.TypeConversionException;

public class SmallDateTypeConversion extends StrutsTypeConverter {
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object convertFromString(Map arg0, String[] arg1, Class arg2) {
		Date rDt = null;
		if(Date.class == arg2){
			//System.out.println("Yes Convert to Date:");		
			try {
				if(arg1!=null && arg1[0]!=null && !arg1[0].isEmpty())
					rDt = sdf.parse(arg1[0]);
			} catch (ParseException e) {
				e.printStackTrace();
				throw new TypeConversionException("Given Date is Invalid");
			}
		}
			
		return rDt;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String convertToString(Map arg0, Object arg1) {
		Date rDt = (Date) arg1;
		return sdf.format(rDt);
	}

}
